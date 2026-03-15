# MoE-Prism: Disentangling Monolithic Experts for Elastic MoE Services via Model-System Co-Designs

## What is the paper about?

The name (MoE-Prism) reflects its core function: just as a prism decomposes a single beam of light into a spectrum of colors, MoE-Prism refactors a monolithic expert into a spectrum of fine-grained sub-experts. This introduces architectural elasticity (right), transforming the cliff into a smooth trade-off curve and enabling the selection of an optimal "Target Quality" point that was previously unattainable.

**解决的问题？**

Reducing the expert count, even by the smallest possible step, often triggers a massive and disproportionate drop in model quality. We call this the **Quality Cliff**: the inability to make a small sacrifice in quality for a commensurate gain in performance.

**Key insight:**

High degree of **activation sparsity** at the **sub-expert** level: Activating an entire expert is computationally wasteful, as the vast majority of its neurons contribute minimally to the final output for any specific token.

By activating only the essential sub-units at runtime, we can finally unlock the **smooth, elastic performance-quality trade-off** that the MoE architecture has always promised.

## What is new compared to prior work?

- The **Offline Refactoring Engine** performs a one-time transformation of the MoE model.
- The **Online Scheduling Engine** exploits this newfound architectural elasticity during online inference.

This offline-online design is a deliberate choice. By paying a one-time, upfront computational cost during the offline phase, we unlock permanent runtime flexibility. This avoids imposing the overhead of model analysis onto the critical path of online inference, enabling the serving system to be both intelligent and highly performant.

## What experiments were run to support the arguments in this paper?

...

## What are the shortcomings/limitations of this paper?

...

## What is a reasonable next step to build upon this paper?

...

## Basic Concepts (Related Knowledge)

...

## References (Related Works)

...
