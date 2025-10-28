# vllm AFD 源码阅读

## (1) entrypoints/afd_ffn_server.py

Running FFN servers:

```bash
python -m vllm.entrypoints.afd_ffn_server /path/to/model \
--tensor-parallel-size 8 \
--afd-config '{"afd_connector": "dummy", "afd_role": "ffn"}'
```

1. FServerCommand
2. AFDFFNServer: main()
3. **AFDFFNServer: start()**
   1. executor_class = Executor.get_class(self.vllm_config)
   2. self.model_executor = executor_class(vllm_config=self.vllm_config)
      1. Worker: init_device()
         1. model_runner = GPUFFNModelRunner()
            1. **self.connector = AFDConnectorFactory.create_connector(get_world_group().rank, get_world_group().local_rank, self.vllm_config)**
   3. self._run_server_loop()
      1. **self.model_executor.collective_rpc("start_ffn_server_loop")**
      2. **(2) ...**
      3. KeyboardInterrupt: self.model_executor.collective_rpc("stop_ffn_server_loop")

## (2) worker/gpu_worker.py

1. **start_ffn_server_loop()**
   1. self.model_runner.capture_model()
   2. **self.model_runner.initialize_afd_connector()**
   3. self.profiler.start()/stop()
   4. **self._ffn_thread = threading.Thread(target=ffn_worker_loop, daemon=True).start()**
      1. torch.cuda.set_device(self.device)
      2. **self.model_runner.execute_model(scheduler_output=None)**
      3. **(3) ...**

## (3) worker/gpu_ffn_model_runner.py

1. execute_model()
   1. **hidden_states, metadata = self.connector.recv_attn_output()**
   2. ffn computation (eager or graph):
      - rank_ffn_output = self._execute_with_cuda_graph(hidden_states, cuda_graph_info)
      - rank_ffn_output = self._execute_eager_mode(hidden_states, current_layer_idx)
        1. **ffn_output = self.model.compute_ffn_output(current_layer_idx, gathered_hidden_states)**
           1. **(4) ...**
   3. **self.connector.send_ffn_output(rank_ffn_output, metadata)**

## (4) models/step3_text.py

1. compute_ffn_output()
   - ffn:
      1. self.mlp(hidden_states)
   - moe:
      1. **share_output = self.share_expert(hidden_states)**
      2. **moe_output = self.moe(hidden_states) - FusedMoEBlock**
      3. share_output + moe_output
