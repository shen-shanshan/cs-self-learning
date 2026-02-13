# Ray Notes

## Basic

In Ray, the driver is the Python script that calls `ray.init` (只能被调用一次).

## Scheduling

One bundle can be used by multiple tasks and actors (i.e., the bundle to task (or actor) is a one-to-many relationship).

If you don’t specify a bundle, the actor (or task) is scheduled on a random bundle that has unallocated reserved resources.

Reserved resources (bundles) from the placement group are automatically freed when the driver or detached actor that creates placement group exits. -> free the reserved resources manually.

When you remove the placement group, actors or tasks that still use the reserved resources are forcefully killed.

```python
from ray.util.placement_group import (
    placement_group,
    placement_group_table,
    remove_placement_group,
)
from ray.util.scheduling_strategies import PlacementGroupSchedulingStrategy


import ray
ray.init(num_cpus=2, num_gpus=2)


pg = placement_group([{"CPU": 1, "GPU": 1}])
# pg = ray.util.placement_group(
#     name=f"dp_rank_{len(placement_groups)}",
#     strategy=placement_strategy,
#     bundles=bundles,
# )

actor = Actor.options(
    scheduling_strategy=PlacementGroupSchedulingStrategy(
        placement_group=pg,
    )
).remote()
ray.get(actor.ready.remote(), timeout=10)
```

## Elastic

Ray Autoscaler?
