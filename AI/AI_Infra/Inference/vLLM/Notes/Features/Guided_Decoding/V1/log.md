# Test Logs

```bash
ERROR 04-03 06:53:16 [backend_xgrammar.py:137] Failed to advance FSM for request 0 for tokens 671. Please file an issue.
ERROR 04-03 06:53:16 [backend_xgrammar.py:137] Failed to advance FSM for request 0 for tokens 41721. Please file an issue.
ERROR 04-03 06:53:16 [backend_xgrammar.py:137] Failed to advance FSM for request 0 for tokens 1608. Please file an issue.
ERROR 04-03 06:53:16 [backend_xgrammar.py:137] Failed to advance FSM for request 0 for tokens 271. Please file an issue.
ERROR 04-03 06:53:16 [backend_xgrammar.py:137] Failed to advance FSM for request 0 for tokens 785. Please file an issue.
ERROR 04-03 06:53:16 [backend_xgrammar.py:137] Failed to advance FSM for request 0 for tokens 25975. Please file an issue.
ERROR 04-03 06:53:16 [backend_xgrammar.py:137] Failed to advance FSM for request 0 for tokens 13302. Please file an issue.
ERROR 04-03 06:53:16 [backend_xgrammar.py:137] Failed to advance FSM for request 0 for tokens 304. Please file an issue.
ERROR 04-03 06:53:16 [backend_xgrammar.py:137] Failed to advance FSM for request 0 for tokens 279. Please file an issue.
ERROR 04-03 06:53:16 [backend_xgrammar.py:137] Failed to advance FSM for request 0 for tokens 2661. Please file an issue.
ERROR 04-03 06:53:16 [backend_xgrammar.py:137] Failed to advance FSM for request 0 for tokens 1467. Please file an issue.
ERROR 04-03 06:53:16 [backend_xgrammar.py:137] Failed to advance FSM for request 0 for tokens 11. Please file an issue.
ERROR 04-03 06:53:16 [backend_xgrammar.py:137] Failed to advance FSM for request 0 for tokens 330. Please file an issue.
ERROR 04-03 06:53:17 [backend_xgrammar.py:137] Failed to advance FSM for request 0 for tokens 85. Please file an issue.
ERROR 04-03 06:53:17 [backend_xgrammar.py:137] Failed to advance FSM for request 0 for tokens 4086. Please file an issue.
ERROR 04-03 06:53:17 [backend_xgrammar.py:137] Failed to advance FSM for request 0 for tokens 44. Please file an issue.
Processed prompts: 100%|██████████████████████████████████████████████████████████████████████████| 1/1 [00:04<00:00,  4.29s/it, est. speed input: 2.56 toks/s, output: 3.73 toks/s]
 #Excited

The sentiment expressed in the given text, "vLLM
Processed prompts:   0%|                                                                                  | 0/1 [00:00<?, ?it/s, est. speed input: 0.00 toks/s, output: 0.00 toks/s]ERROR 04-03 06:53:17 [backend_xgrammar.py:137] Failed to advance FSM for request 1 for tokens 734. Please file an issue.
ERROR 04-03 06:53:17 [backend_xgrammar.py:137] Failed to advance FSM for request 1 for tokens 151643. Please file an issue.
Processed prompts: 100%|████████████████████████████████████████████████████████████████████████| 1/1 [00:00<00:00,  2.11it/s, est. speed input: 67.50 toks/s, output: 16.87 toks/s]
alan.turing@enigma.com
Processed prompts:   0%|                                                                                  | 0/1 [00:00<?, ?it/s, est. speed input: 0.00 toks/s, output: 0.00 toks/s]ERROR 04-03 06:53:17 [backend_xgrammar.py:137] Failed to advance FSM for request 2 for tokens 382. Please file an issue.
Processed prompts: 100%|████████████████████████████████████████████████████████████████████████| 1/1 [00:01<00:00,  1.18s/it, est. speed input: 18.73 toks/s, output: 13.62 toks/s]
.

{
  "brand": "Ford",
  "model": "Mustang
Traceback (most recent call last):
  File "/home/sss/software/miniconda3/envs/vllm-v1/lib/python3.10/site-packages/vllm/v1/structured_output/utils.py", line 285, in validate_structured_output_request_xgrammar
    gd_params.grammar = convert_lark_to_ebnf(gd_params.grammar)
  File "/home/sss/software/miniconda3/envs/vllm-v1/lib/python3.10/site-packages/vllm/v1/structured_output/utils.py", line 220, in convert_lark_to_ebnf
    raise ValueError("Referenced rules are not defined: "
ValueError: Referenced rules are not defined: Z0, Z_, a, zA

The above exception was the direct cause of the following exception:

Traceback (most recent call last):
  File "/home/sss/github/vllm-project/vllm/examples/offline_inference/structured_outputs.py", line 78, in <module>
    outputs = llm.generate(
  File "/home/sss/software/miniconda3/envs/vllm-v1/lib/python3.10/site-packages/vllm/utils.py", line 1131, in inner
    return fn(*args, **kwargs)
  File "/home/sss/software/miniconda3/envs/vllm-v1/lib/python3.10/site-packages/vllm/entrypoints/llm.py", line 457, in generate
    self._validate_and_add_requests(
  File "/home/sss/software/miniconda3/envs/vllm-v1/lib/python3.10/site-packages/vllm/entrypoints/llm.py", line 1317, in _validate_and_add_requests
    self._add_request(
  File "/home/sss/software/miniconda3/envs/vllm-v1/lib/python3.10/site-packages/vllm/entrypoints/llm.py", line 1335, in _add_request
    self.llm_engine.add_request(
  File "/home/sss/software/miniconda3/envs/vllm-v1/lib/python3.10/site-packages/vllm/v1/engine/llm_engine.py", line 186, in add_request
    request = self.processor.process_inputs(request_id, prompt, params,
  File "/home/sss/software/miniconda3/envs/vllm-v1/lib/python3.10/site-packages/vllm/v1/engine/processor.py", line 185, in process_inputs
    self._validate_params(params)
  File "/home/sss/software/miniconda3/envs/vllm-v1/lib/python3.10/site-packages/vllm/v1/engine/processor.py", line 111, in _validate_params
    self._validate_sampling_params(params)
  File "/home/sss/software/miniconda3/envs/vllm-v1/lib/python3.10/site-packages/vllm/v1/engine/processor.py", line 75, in _validate_sampling_params
    self._validate_structured_output(params)
  File "/home/sss/software/miniconda3/envs/vllm-v1/lib/python3.10/site-packages/vllm/v1/engine/processor.py", line 146, in _validate_structured_output
    validate_structured_output_request_xgrammar(params)
  File "/home/sss/software/miniconda3/envs/vllm-v1/lib/python3.10/site-packages/vllm/v1/structured_output/utils.py", line 287, in validate_structured_output_request_xgrammar
    raise ValueError(
ValueError: Failed to convert the grammar from Lark to EBNF. 
[ERROR] 2025-04-03-06:53:18 (PID:74050, Device:-1, RankID:-1) ERR99999 UNKNOWN applicaiton exception
nanobind: leaked 1 instances!
 - leaked instance 0xfffd00288708 of type "xgrammar.xgrammar_bindings.GrammarCompiler"
nanobind: leaked 1 types!
 - leaked type "xgrammar.xgrammar_bindings.GrammarCompiler"
nanobind: leaked 7 functions!
 - leaked function "compile_structural_tag"
 - leaked function "compile_regex"
 - leaked function "__init__"
 - leaked function "compile_builtin_json_grammar"
 - leaked function "compile_grammar"
 - leaked function "clear_cache"
 - leaked function "compile_json_schema"
nanobind: this is likely caused by a reference counting issue in the binding code.
```
