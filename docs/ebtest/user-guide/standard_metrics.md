---
title: Standard Metrics
weight: 11
menu:
  main:
    parent: User Guide
    identifier: standard metrics
    weight: 23
---

Engineblock comes with a set of standard metrics that will be part of every
activity type. Each activity type enhances the metrics available by adding their
own metrics with the engineblock APIs. This section explains what the standard
metrics are, and how to interpret them.

### read-input

Within engineblock, a data stream provider called an _Input_ is responsible for
providing the actual cycle number that will be used by consumer threads. Because
different _Input_ implementations may perform differently, a separate metric is
provided to track the performance in terms of client-side overhead. The
**read-input** metric is a timer that only measured the time it takes
for a given activity thread to read the input value, nothing more.

### strides

A stride represents the work-unit for a thread within engineblock. It allows
a set of cycles to be logically grouped together for purposes of optimization --
or in some cases -- to simulate realistic client-side behavior over
multiple operations. The stride is the number of cycles that will be allocated
to each thread before it starts iterating on them.

The **strides** timer measures the time each stride takes, including all
cycles within the stride. It starts measuring time before the cycle starts,
and stops measuring after the last cycle in the stride has run.

### cycles

Within engineblock, each logical iteration of a statement is handled within a
distinct cycle. A cycle represents an iteration of a workload. This corresponds
to a single operation executed according to some statement definition.

The **cycles** metric is a timer that starts counting at the start of a cycle,
before any specific activity behavior has control. It stops timing once the
logical cycle is complete. This includes and additional phases that are executed
by multi-phase actions.

### phases (*deprecated*)

Some activity types can support multiple phases. Each time a cycle is executed
within an activity thread, it executes at least one phase. For activity types
which do not explicitly support multiple phases per action, the cycle execution
is still counted as a phase. They measure nearly the same thing in that case.

While it is up to each activity type to determine what a phase is good for,
some common reasons to use phases include:

- async eventing
- multi-page operations
- long-running state management

Refer to the respective activity type documentation to see what phases mean for
your activity.

The phase timer starts just before the cycle action is called within each thread,
and ends immediately after it returns. It also measures the "full cycle" of a
single cycle when multi-phase actions are not part of an activity.

{{< note >}}
The phases feature will be deprecated in a future version of EngineBlock. Activity types that
need this feature will be responsible for implementing something equivalent. This
change is necessary to simplify the API for developers.
{{< /note >}}

## Metrics Visually

### Basic Activity Types

For single-phase (basic) activity types, the following diagram describes
accurately the scope of the core metrics.  While the phases timer *is* active in
this scenario, it is effectively the same as the cycle timer, so they are
combined in the diagram for the sake of simplicity.

<img src="/diagrams/eb_iterates_cycles.svg" width="50%" align="center"></img>

### Multi-Phase Activity Types

The picture get slightly more detailed for actions that can support multiple phases.
The initial phase is called as runCycle(), and additional phases are called as runPhase().

<img src="/diagrams/eb_iterates_phases.svg" width="50%" align="center"></img>


