---
title: Coordinated Omission
weight: 11
menu:
  main:
    parent: User Guide
    identifier: coordinated-omission
    weight: 25
---

Coordinated omission is a term of art coined by Gil Tene. In practice,
coordinated omission follows from the fact that all systems have finite
concurrency and scheduling capacity, including the clients that test them. While
the term doesn't cover all important aspects of scale and performance testing,
it does put a focus on a particularly troublesome problem that often occurs with
testing methods where effective concurrency is a meaningful limitation.

Scheduling constraints are pervasive in distributed systems. _Coordinated Omission_ 
is simply a focal point from the much broader topic
[Queueing Theory](https://en.wikipedia.org/wiki/Queueing_theory).
Still, it is appropriate to focus on the coordinated omission scenario as an object
lesson. It can provide common ground for discussion and understanding of more common
operational goals, without requiring that users do a costly bulk-import of other
theory. 

EngineBlock has specific features to help shed a light on the effects of
coordinated omission. This section explains some of them, how they work, and how
to use them.

To make sense of this, it is important to be aware of the specific meaning
of the terms used for timing with systems testing. For a specific view on
how timing terms are used with EngineBlock, see [timing terms](../timing_terms).

As EngineBlock is a client-side tool, only the first two are directly
observable: *wait time* and *latency*. The most important, *response* is 
calculated separately per operation. The reason for this will be made clearer below.

## Coordinated Omission and Async Clients

The effects of coordinated omission can happen with synchronous as well as asynchronous
clients and servers. This follows from a maxim: All systems have finite capacity.
It is true that asynchronous clients can usually manage higher concurrency with lower
operational cost. This is not the same as saying async clients avoid problems with
coordinated omission suffered by synchronous clients. This is simply not true, and
in practice is simply a matter of moving the measurement of scheduling constraints
to a different limiting resource. The idea that asynchronous clients may somehow
avoid these issues is based a common fallacy: Async clients that are practical do not
generally delegate *all* responsibility for a message to external systems. This is
not practical, as every pending operation needs to be tracked by a client at some
level in order to ensure robustness in behavior. For example, such a client would
have no definition of a timeout, and this is not practical in robust system design.
Thus, no asynchronous mechanism that is robust can totally offload work to the
surrounding system, and thus must have a finite capacity. For asynchronous clients
this tends to manifest in internal work queues and timers as well as in-memory
state associated with pending requests.

To be clear, asynchronous clients can't avoid coordinated omission. They may simply
defer it to another level of activity, but it is still there when scheduling constraints
occur.

## C.O. Rate Controls

When rate limiting is enabled, the rate limiter is responsible for both limiting
the start time of each operation as well as tracking how far the operations lag
behind the ideal schedule. 

### In Current EngineBlock versions

In the current version of EngineBlock, this is retained as a scheduling delay specific to
each operation and added to any measurements of latency in order to arrive at an
overall response time for each operation. The phrase *scheduling delay* here
is a specific form of wait time that is imposed upon the client in order to simulate
some consistent rate of ideal users in the system, with operations spread over time.

When the rate controls are enabled, you have to decide whether the scheduling
delay measured by the rate limiter will be used in response time metrics or not. If
you choose not to include the delay, then the response time metrics only
measure latency. You can still observe the scheduling delay metric
in this case, as it is tracked and reported separately.
If you choose to include the delay, then latency metrics reported are effectively 
promoted to *response time* metrics.

In the EngineBlock documentation, metrics which can have this adjustment made
are called ***coordinated omission aware***. In general, any where you see a
configuration option with `co` in it, this represent the option of enabling a
total response metric that is coordinated omission aware, or converting a
latency metric to a response metric.

There are reasons to measure either way, depending on the kind of testing you
are doing. All systems have concurrency limitations and critical resource
constraints. Also, systems are comprised of clients *and* servers with various
concurrency models. Sometimes it is useful to see system behavior through a more
conventional lens.

### In Future EngineBlock versions

The metrics for latency, wait time, and response time will all be calculated
consistently. Only latency will be provided as a standard metric, but when
rate limiting is enabled, additional metrics for wait time and response time
will be provided.

## Using C.O. metrics

You can enable coordinated awareness in your metrics by using the following activity parameters:

- [striderate](/parameters/activity_params/#striderate)
- [cyclerate](/parameters/activity_params/#cyclerate)
- [phaserate](/parameters/activity_params/#phaserate)

These rate controls nest from the outside-in. The relationship between them is explained
in more detail under [Activity Params](/parameters/activity_params).

Setting a target rate for more than one of these at a time is not something that is
generally useful, since nested rates can become askew from each other unless they are
perfectly matched together in terms of counts and rates.

In general, simply add `,co` to the end of any rate specifier to ensure that it is enabled.
For example, `cyclerate=1000,0.0,co`. The second number in the rate specifier will be
a scheduling strictness control. For now, it is required to be `0.0`, which means burstable
average rate limiting. Still, the scheduling delay is included in the result as long as `,co`
is present. Alternately, you can use the `co_` prefix for the rate controls to achieve
the same effect, as in `co_cyclerate=1000`.

## Metric Names

When a rate limiter is set, the [cycles](/user-guide/standard_metrics/#cycles)
metric is becomes a total response time metric instead of simply an operational
latency metric. If you have an activity named "myactivity" by the
`alias=myactivity` parameter, then this metric will be named
`<prefix>.myactivity.cycles`, where the prefix is set by the metrics prefix
option. The same apples for the stride and phase metrics, using the same
conventions.

Additionally, there are two additional metrics that are reported for rate limiters:

- `<alias>.<stride|cycle|phase>.cco_delay_gauge` - A meter of the cumulative
  scheduling delay for an activity with the alias and the respective rate limiter
  level. Although this is a gauge, the scheduled delay that is added to latencies is calculated
  per-op.
  - example: `testactivity.cycle.cco_delay_gauge` is the cycle delay gauge for an activity with the alias
    'testactivity'.
- `<alias>.<stride|cycle|phase>.avg_targetrate_gauge` - This simply reports the intended average
  target rate for complete metric views in dashboards.
  
## Concurrency Model

Activity types can implement either a per-thread concurrency model or an asynchronous concurrency
model which allows threads to manage a number of simultaneous requests each. In practice, this can
be a factor for how many pending requests a client can put into flight. Async implementations generally 
have the ability to juggle a higher number of pending operations, as idle time on threads is reduced
as well as context switching that occurs when high thread counts are used to drive effective concurrency.

However, even asynchronous implementations have limits as to where blocking states can occur. Regardless
of whether your client-server system is synchronous or asynchronous, there will be a limitation of
how much work can be enqueued without going into a backlogging mode somewhere in the flow. It is
important to bear this in mind when interpreting coordinated omission results, as there is no simple
and universal remedy for resource contention at saturation. If you are not driving a saturating load
to the target system, and you are not blocking your client or server op rates by hitting 
concurrency limits, then there is little difference between the two concurrency models in terms of
measurement.

In effect, the coordinated omission awareness and the ability to have an asynchronous execution model
should be considered complimentary. If you want to use async activities, and the activity type supports
asynchronous operations, then you can enable it separately. Going forward, the convention for enabling
async on an activity that supports it will be the `async` parameter, which will be documented
in the respective activity type documentation.




    
    


 
 



