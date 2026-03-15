# Splitwise: Efficient Generative LLM Inference Using Phase Splitting

## What is the paper about?

Splitwise, a model deployment and scheduling technique that splits the two phases of LLM inference requests on to separate machines.

**Key Insights:**

1. Different inference services may have widely different prompt and token distributions.
2. Mixed continuous batching spends most of the time with very few active tokens batched.
3. For most requests, the majority of the E2E time is spent in the token generation phase.
4. The prompt phase batch size should be limited to ensure good performance. In contrast, batching the token generation phase yields high throughput without any downside.
5. Batching during the prompt phase is compute-bound, whereas the token phase is limited by memory capacity.
6. While the prompt phase utilizes the power budget of the GPU efficiently, the token phase does not.
7. Token generation can be run on less compute-capable hardware for better Perf/W and Perf/$ efficiencies.

## What is new compared to prior work?

...

## What experiments were run to support the arguments in this paper?

To evaluate our proposal on real hardware, we implement Splitwise’s KV-cache transfer mechanism on top of vLLM.

**Simulator:** https://github.com/Mutinifni/splitwise-sim

1. We first **profile the LLM on the target hardware** with various input/output sizes.
2. The simulator takes as input **the request traces, SLOs, the performance model, and the configurations for cluster and scheduler**.
3. The simulator provides **the achieved metrics per request (TTFT, TBT, E2E)**, and the machine utilization levels.
4. We also **validated the simulator end-to-end using production load** with over 50K iterations to ensure fidelity.

## What are the shortcomings/limitations of this paper?

If the prompt or the token machine fail, Splitwise simply restarts requests from scratch, similar to today’s LLM serving systems.

## What is a reasonable next step to build upon this paper?

...

## Basic Concepts (Related Knowledge)

...

## References (Related Works)

...
