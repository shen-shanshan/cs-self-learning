# What does this PR do?

This PR is a refactoring of `current_memory_usage()` function in `DeviceMemoryProfiler`.

I put this function into `Platform` and implement it in each `XxxPlatform` subclass, making `DeviceMemoryProfiler` platform agnostic.

Refactor `current_memory_usage()` function in `DeviceMemoryProfiler` into `Platform`
