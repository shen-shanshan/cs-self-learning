# SO PRs

## PR1: platform 重构

`interface.py`:

```python
class Platform:
    @classmethod
    def supports_structured_output(cls) -> bool:
        """
        Returns whether the current platform can support structured output.
        """
        return False
```

`cpu.py / cuda.py / hpu.py / neuron.py / rocm.py / xpu.py`: `return True`

`tpu.py`:

```python
class TPUPlatform:
    @classmethod
    def supports_structured_output(cls) -> bool:
        logger.warning("Structured output is not supported on TPU.")
        return False
```

`v1/engine/processor.py`:

```diff
class Processor:
    def _validate_structured_output():
        import vllm.platforms
-       if vllm.platforms.current_platform.is_tpu():
-           raise ValueError("Structured output is not supported on TPU.")
+       if not vllm.platforms.current_platform.supports_structured_output():
+           return
```

## PR2: validate_so 重构

`v1/engine/core.py`:

```diff
class EngineCore:
    def add_request(self, request: EngineCoreRequest):
        ...

        req = Request.from_engine_core_request(request)
        if req.use_structured_output:
            # Start grammar compilation asynchronously
-           self.structured_output_manager.grammar_init(req)
+           self.structured_output_manager.init_grammar(req)

        ...
```

`v1/engine/processor.py`:

```python
from xxx import StructuredOutputManager

class Processor:
    def _validate_structured_output(self, params: SamplingParams) -> None:
        import vllm.platforms
        if vllm.platforms.current_platform.is_tpu():
            raise ValueError("Structured output is not supported on TPU.")

        if not params.guided_decoding or not self.decoding_config:
            return

        engine_level_backend = self.decoding_config.guided_decoding_backend
        supported_backends = StructuredOutputManager.supported_backends

        if engine_level_backend not in supported_backends:
            raise ValueError(f"Only {supported_backends} structured output is "
                             "supported in V1.")
        if params.guided_decoding.backend:
            if params.guided_decoding.backend != engine_level_backend:
                raise ValueError("Request-level structured output backend "
                                 "must match engine-level backend. "
                                 f"{params.guided_decoding.backend}"
                                 f" != {engine_level_backend}")
        else:
            params.guided_decoding.backend = engine_level_backend

        # Request content validation
        StructuredOutputManager.validate_structured_output_request(engine_level_backend, params)
```

`v1/structured_output/__init__.py`:

```diff
class StructuredOutputManager:
-   def grammar_init(self, request: Request) -> None:
+   def init_grammar(self, request: Request) -> None:
        if request.structured_output_request is None:
            return

        # Initialize the backend the first time it is needed.
-       #
        # NOTE: We only support a single backend. We do NOT support different
        # backends on a per-request basis in V1 (for now, anyway...).
        if self.backend is None:
            backend_name = request.sampling_params.guided_decoding.backend_name
            if backend_name == "xgrammar":
-               from vllm.v1.structured_output.backend_xgrammar import (
-                   XgrammarBackend)
-                                                            
                self.backend = XgrammarBackend(self.vllm_config)
            elif backend_name == "guidance":
                self.backend = GuidanceBackend(self.vllm_config)
            else:
                raise ValueError(
                    f"Unsupported structured output backend: {backend_name}")

        grammar = self.executor.submit(self._async_create_grammar, request)
        request.structured_output_request.grammar = grammar  # type: ignore[assignment]
```

```python
from vllm.v1.structured_output.backend_xgrammar import XgrammarBackend
from vllm.v1.structured_output.backend_guidance import GuidanceBackend

class StructuredOutputManager:

    supported_backends = [
        "xgrammar",
        "xgrammar:disable-any-whitespace",
        "guidance",
        "guidance:disable-any-whitespace",
        "auto",
    ]

    @static  # or @classmethod ?
    def validate_structured_output_request(self,
                                           engine_level_backend: str,
                                           params: SamplingParams) -> None:
        '''
        Validate rquest content according to engine_level_backend
        '''
        if engine_level_backend.startswith("xgrammar"):
            XgrammarBackend.validate_grammar(params)
        elif engine_level_backend.startswith("guidance"):
            GuidanceBackend.validate_grammar(params)
        else:
            # engine_level_backend == "auto"
            # "auto" is an opt-in to opinionated behavior where we try to
            # choose a backend based on request contents. This is not the
            # default as it is less predictable and subject to change
            # between releases as feature support changes.
            try:
                XgrammarBackend.validate_grammar(params)
                engine_level_backend = "xgrammar"
            except ValueError:
                # The request includes some jsonschema feature(s) that
                # are not supported in xgrammar. Fall back to guidance.
                engine_level_backend = "guidance"
        
        params.guided_decoding.backend = engine_level_backend
```

将 `validate_grammar()` 放到各个 `SOBackend` 类中，变成静态方法 or 类方法？

`v1/structured_output/backend_types.py`:

```python
class StructuredOutputBackend(ABC):
    @abstractmethod
    @classmethod
    def validate_grammar(params: SamplingParams):
        ...
```

`v1/structured_output/backend_xgrammar.py`:

```python
class XgrammarBackend(StructuredOutputBackend):
    @classmethod
    def validate_grammar(params: SamplingParams):
        # Xgrammar with no fallback.
```

`v1/structured_output/backend_guidance.py`:

```python
class GuidanceBackend(StructuredOutputBackend):
    @classmethod
    def validate_grammar(params: SamplingParams):
        # TODO: ideally we would have the LLTokenizer here as Lark syntax
        # allows <|special_token|> and similar, see
        # https://github.com/guidance-ai/llguidance/blob/main/docs/syntax.md#special-tokens
        # Without tokenizer these are disallowed in grammars.
```

## PR3: outlines 支持
