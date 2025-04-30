# Continuous Batching

## Introduction

**Continuous batching** schedules LLM execution at the granularity of **iterations** instead of requests. After each LLM decoding iteration, the inference engine checks each request’s status and sends the generated results of all finished requests to the client. This design also allows the inference engine to start processing newly arrived requests without waiting for all current requests to complete.

## Papers

《Orca: A distributed serving system for Transformer-Based generative models》
