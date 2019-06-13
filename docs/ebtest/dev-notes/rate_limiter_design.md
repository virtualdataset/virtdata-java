---
title: Rate Limiter Design
weight: 10
menu:
  main:
    parent: Dev Notes
    identifier: rate-limiter-design
    weight: 20
---

# Synopsis

This document elaborates on the design of the core rate limiters, to help
those who are curious or want to know how some of the design choices were made.

The rate limiter has to be fast enough that it doesn't hinder workloads. Further
it has to scale well under thread contention.

After exploring multiple approaches, the one that was the easiest to reason
about without sacrificing scalability was used. That is the design that
is explained below.

## Expectations

A useful rate limiter must be able to do the following:

1. Be efficient. A rate limiter should get out of the way of the work that
   it is throttling when there is external wait time.
2. Scale up and down gracefully with available threads.
3. 
    

## Approaches 

The best source of basic high-resolution timing is System.nanoTime(), even though
accuracy varies. This provides a long value which works well with our technique,
described below. In terms of pausing a thread for a period of time, there are
multiple techniques that have been found with varying degrees of efficiency and
accuracy. We wish to avoid spin loops or other unconventional ways of forcing
timing precision in this case. Techniques which burn CPU cycles or avoid yielding
thread time back to the OS are wasteful of resources and thus steal valuable
capacity away from the testing instrument. For this reason, the two essential
ingredients to our approach are System.nanoTime() and Thread.sleep(millis,nanos).

## Unit of Work

By using nanos as a unit of work sizing, it is possible to track all grants for
time slots in terms of atomically incrementing long values, representing time
slices in nanoseconds. Simply put, a target rate of 500_000 yields a time slice
of 1_000_000_000 / 500_000, or 2000 nanoseconds. With this time slice, it is trivial
to atomically increment a moving accumulator that represents the time slices
given out, pacing each operation according to the accumulated value.

Further, the timer state is recorded in terms of views of actual time passing.

The ticks accumulator is one such counter. It simply tracks the monotonically
and atomically incrementing schedule pointer for the next available time slice.
When a caller calls the blocking acquire() method, the ticks accumulator already
holds the nanosecond time of the next available time slice. This value is
fetched and incremented atomically. The value left in the ticks accumulator is
the start time of the next time slice that will be given to a subsequent caller.

## Timing overhead

The bulk of time spent in a typical acquire() call is in the System.nanoTime()
call itself. This is problematic when you want to support high rate limits while
calling the system timer function every iteration. At a basic calling cost of
around 25ns each, the maximum theoretical limit that could be supported is
1000000000/25, or 40M ops/s. Single threaded, the Guava RateLimiter tops out at
around 25M ops/s on my 8 core system. However, we are not stuck with this as an
upper limit. It is important to optimize the timer not just to support higher
rates of operation, but also to preserve system thread time for the actual
workloads that need it.

At very high rate limits, timing accuracy becomes less critical, as other
elements of system flow will cause a degree of work spreading. Blocking queues,
thread pools, and SERDES will all make it difficult to preserve any sub-10ns
level timing downstream. For this reason, it is not critical to achieve sub-10ns
level isochronous dispatch at these rates. In short, fine-grained timing
precision is low priority. Rate discipline is high priority. These are not the
same thing. This offers room for an accuracy and speed trade-off.

The timer implementation thus keeps a view of system nanosecond passage of time
in another timeline register, called the *last-seen nanotime*. This means that
threads can share information about the last seen nanotime, avoiding calling it
except in cases that it may be needed in order to make progress on dispatch for
the current caller. The basic rule is that the last seen nanotime is only ever
updated to the current system nanotime IFF it has not advanced enough to unblock
the current caller. Doing so updates the shared view of the timeline register,
which again allows a number of subsequent callers to take the fast path for 
high op rates.







The rate limiter is an essential part of a good testing kit.


## Terms

*pool_size* - The size of the active pool when it is 100% full.

*backfill_rate* - The amortized rate at which the active pool is topped
up by the waiting pool. This is a proportion of the normal rate that
can be added to provide "burst rate" semantics. A value of 0.25 means
"25% faster". Take care to avoid confusing this with _burst ratio_
semantics from other places, which is relative to the normal rate.

*active_pool* - A pool of tokens which are
directly available for consumers to take.

*waiting_pool*
A pool of tokens which are kept in reserve,
to be added back to the active pool at a managed rate.
 
Each time the tokens are refilled, all remaining space in the
active pool is filled first, and then extra tokens are added to the
waiting pool. If there were no extra tokens, and there is still
room in the active pool, then some tokens are moved from the
waiting pool to the active pool according to the backfill rate.

All of the tokens are retained and moved forward to the active
pool as the backfill rate allows. No individual token is
destroyed nor scaled. Only the amount of tokens that
may be moved forward to the active bucket is affected,
at a relative rate. This means that tokens can be equated to
discrete units of time, and are thus easy to reason about.
Tokens only enter the active and waiting pools by way of a
call to {@link #refill(long)}, Never as a direct result of a consumer.
Therefore, tokens only ever enter or leave the waiting pool
as an amortized true-up at the time tokens are added.

The number of waiting tokens that may be transferred to the active
pool is limited by several factors:

1. The available room in the active pool and backfill pools combined, according
   to the configured *pool_size* and *backfill_rate*.
   This is calculated as `max( pool_size + (pool_size * backfill_rate) - active_tokens)`

2. The fill rate with respect to the pool size and refill amount.
   This is calculated as `refill_tokens * pool_size`, and
   limits the contribution from the waiting pool the active pool as amortized over
   cumulative tokens refilled.

3. The number of available tokens in the waiting pool, after spill-over from
   refill_tokens to waiting_pool.

For the sake of calculation, it is easier to think about the active and
waiting pool as being three pools: The <em>active</em> pool, the <em>backfill</em> pool,
and the *waiting_pool*, where the active pool is strictly limited by pool size
and the backfill pool is strictly limited by `(pool_size * backfill_ratio)`.
In this model, consumers can take tokens from either of the first two pools.
In practice, these two virtual pools are combined into one active pool that
is simply allowed to grow to size* `(pool_size + (pool_size * backfill_rate)`

## Examples

    TODO: Add a good example here and rewrite code below

// * Example:
// * If you have a pulse width of 1000 units and a burst ratio of 1.25,
// * then the maximum active pool is 1000 * 1.25 = 1250.
// * With this, a refill of 500 may add up to 500 tokens to the
// * active pool unless it is already at or above 1000. However,
// * an additional 62.5 tokens may be added from the wait pool
// * for up to 1250 tokens total. The number of wait tokens that can
// * be transferred is pro-rata the refill amount with respect to
// * the pulse width. In this case, that is (500.0/1000.0), so a
// * maximum backfill of 0.5 * (1.25*1000 - 1.0*1000) or 125.
// * This ensures that the backfill is amortized over the density
// * of tokens added to the pool.
 * </p>
 * <p>
 * Thus, if you have an active pool size of 1000, and you add 500 tokens
 * to it repeatedly, then each of those will allow 1/2 of the
 * backfill to occur according to the backfill ratio, constituting
 * the full 25% of "bursting" that was intended. This can ONLY occur
 * if there was a backlog created by consumer failing to consume
 * tokens from the active pool at a token normalized rate.
 * </p>



## Calling Overhead Compensation

The net effect of keeping a last-seen nanotime register is to allow for the
calling overhead of System.nanoTime() to be automatically mitigated, with any
saved nanoseconds being fed directly to the scheduling algorithm, which moves
scheduling logic into each thread for the optimal case. This creates an
automatic type of calling overhead compensation, wherein the more overhead
System.nanotime (and the acquire method itself) has in terms of time used, the
bigger the time gap created for rapid catch-up on calling threads. This does
create a possible type of leap-frogging effect in the timer logic. This should
be more pronounced as extremely high rates and less common at lower rates in
which threads are expected to be blocked more of the time. As well, it means
that in the catch-up case (the case in which last-seen doesn't need advancing in
order to make progress for the caller), the scheduling algorithm can do nearly
all stack-local and atomic accesses, which vastly speeds up the achievable op
rates.

## Strict vs Average

The two-timeline approach described above works well in terms of speed, but it
is not sufficient to control uniformity of schedule from one call to the next.

The reason for this is that as callers are delayed for their own reasons, the
"open timespan" gets larger. The longer time progresses without the ticks
accumulator following it, the larger the gap, and thus the availability of total
time at any one instant.

If you are targeting total average rate, this allows threads to effectively
burst over the rate limit in order to approach the average rate limit. Sometimes
this is desirable, but not as an unlimited bursting which might allow the short-term
measured op rate to be many times that of the target average rate.

However, you may prefer strict rate limiting from one call to the next. In other
words, you may require that with a 2000 ops/s rate limit, no two operations
occur less than 500_000 ns apart. This is called "Strict Rate Limiting".

## Burst Compensation

To support a mix of these two modes, a feature called *Burst Compensation* is
added to the scheduling algorithm. This feature takes any gap that exists
between the next available time slice and the current system nanotime (the 
open time span) and reduces it by a fraction. The fraction is a power of two, but
it is set loosely with a *ratio* of gap time, with an approximate register
shift occurring only in the case of a blocked caller. This has the effect of
soaking up lead time that is not being used, but in a proportional way.

A burst limit compensation value of 0.25 will cause 1/4 of the gap to be closed, 
a value of 0.125 will cause 1/8 to be closed, and so forth. Values in between 
are rounded down. This type of burst compensation is adaptive in that it will
allow longer periods of idle callers to provide more catch-up time while also
disallowing extended bursting to occur. Further study is needed to characterize
how this manifests for different caller timings.

As well, you may want to have the ability to catch up to an average target rate
with some amount of constant bursting. In other words, you may want to set a
target rate of R ops/s so long as the callers are keeping up with the allowed
rate, but allow them to go 110% R rate when behind to catch up. This is
represented with a strictness value of 1.1. Essentially, any strictness value
greater than 1.0 is considered a burst allowance. This works out nicely in
contrast to the burst compensation setting represented by strictness values less
than 1.0, since they are mutually exclusive.

A value of 0.0D disables limit compensation, which is will cause the rate
limiter to only throttle once the average rate is achieved. A value of 1.0D will
cause strict limiting between each iteration. A value of 1.15D will allow for
15% overspeed relative to the target rate. In practice none of these is best, as
thread pools have timing jitter, GCs occur, and systems need to warm up to their
achievable rates.

By default, the limit compensation is set to <s>1/32.</s> <em>TBD</em>



## EngineBlock Rate Limiter Design

### Requirements

Since a rate limiter in a test system is one point of unavoidable contention,
its design becomes a focus of critical concern, pun intended. As well, since
EngineBlock is designed to enable dynamic testing methods, general purpose
rate limiter designs are not sufficient. To be specific, a suitable rate
limiter for EngineBlock must include:

- wait time calculation
- strict rate limiting
- burst rate limiting
- high throughput
- reconfiguration

These elements will be described in more detail below for clarity. But first,
some elaboration of terms:

=TODO= Square up all terms

**requester** - In this document, _requester_ refers to the client logic
that uses the rate limiter. It can be thought of as a representative of
a client, a thread, or other process that needs to use a rate
limiter for the purposes of throttling request rates or packet rates.

**scheduled time** - The time at which an operation would start if all operations
before it were started on time, notwithstanding concurrency limits. 

**start time** - The time at which an operation starts. This can be determined by
external factors affecting the operations themselves or the rate limiter logic, or 
both.

**wait time** - The duration that passes between the scheduled time and the start
time. Conceptually, this is how long an operation waits in a queue until it starts
executing. When the wait time for an operation is zero, then the rate limiter is
known to be allowing operations to start at the target rate with no external
delays.

#### Requirement: Wait Time Calculation

This simply means that the rate limiter must be able to tell what the wait time is
for each operation. Since the rate limiter is the governor for when operations
are allowed to start, it is the only part of the system that can easily provide
wait time for use in other calculations.

#### Requirement: Strict Rate Limiting

When a minimum gap in time is ensured between any two operations, then the achieved
rate is strictly limited to how timely the request is submitted. In other words,
if a request is not made at the time the rate limiter would allow it to start,
the time is forfeited and all operations from that point must also be delayed by
that extra time.

#### Requirement: Burst Rate Limiting

In some cases, it is desirable to allow operations to execute temporarily at a
higher rate than normal, but only long enough to reduce the wait time back to
zero.

#### Requirement: Reconfiguration

Because EngineBlock is built to support dynamic workload adjustment, any rate
limiter it provides must also be dynamically adjustable. This simply means that
a rate limiter can be reconfigured on the fly without error, and without loss
of state.

#### Requirement: High Throughput

As mentioned above, a practical rate limiter imposes that contention is unavoidable between
multiple client threads. The requirement for high throughput merely emphasizes how
important it is for the rate limiter implementation to be as efficient as possible
for concurrent use. This means that some trade-offs in how it is implemented may
be necessary.


## Basic Design

The core rate limiter design uses a few distinct atomic values. Each of these
is conceptually an accumulator of nanoseconds which is modified atomically as
shared state across requester threads.

- n<sub>**A**</sub> - _allocated_nanos_ - The total time allocated to operations so far.
- n<sub>**I**</sub> - _idle_nanos_ - The total time given away as idle time.

Additionally, we may treat the system clock as such a value that we simply read.

- n<sub>**C**</sub> - _clock_nanos_ - The system clock.

1. When a rate limiter is initialized, the rate is converted to a number of nanoseconds per operation.
   This is called the _time_slice_ for the rate limiter.
2. When a caller requests to start, the limiter takes the current value in _allocated_nanos_ as the
   _scheduled_time_, and adds _time_slice_ to _allocated_ (for subsequent ops).
3. _op_delay_ is calculated as `clock_nanos - scheduled_time`. The meaning of op_delay is the
   duration between when the rate limiter would have allowed to the op to start to when
   the requester actually asked the rate limiter if it could start.
4. If _op_delay_ is negative, then the op is considered _early_ with respect to its scheduled time.
   The op can only be allowed to start in the future in order to make delay positive.
   This is another way of saying that the order of the clock time and op time is 1) clock 2) op.
   In this case, the limiter is required to add additional delay by holding the op in a sleep
   or other delay mechanism before it is allowed to proceed.
5. If _op_delay_ is positive, then the op is considered _late_ with respect to its scheduled time.
   It is in the past with respect to the clock time. This is another way of saying that the op and
   the clock are in order 1) op 2) clock. In this case, the op may immediately proceed.

## Design Trade-Offs

The above description shows a basic implementation of a rate limiter. However, due to calling overhead
incurred by simply reading the system clock, it is impossible to have accuracy at the level of
precision offered by nanosecond clock sources. Further, the accuracy of mechanisms for adding delay
is very poor. For example, it takes roughly 25ns to call `System.nanoTime()` and about 350ns to call
`Thread.sleep(millis,nanos)` once the VM is warmed up. This has some implications for trading off
wasted precision with valuable efficiency.

Given that the overhead of these calls makes nano-second timing accuracy unlikely even in the best case,
it is best then to relax the expectation of accuracy and simply avoid calling these functions for
every operation.

### Optimization: System.nanoTime()

The clock value is cached in a variable. If it is known by looking at this cached value that an
operation is already 'late', then calling System.nanoTime() again does nothing to improve the
accuracy beyond what is already achievable. Bypassing the call offers drastic speedups. Because
of this, System.nanoTime() is only called in the case that the op is possibly early, and then
the condition is rechecked to determine whether the op is actually early or late. The net effect
of this is that at higher rates, where the nanoTime() becomes a hotter source of contention,
it is actually called less with respect to the rate, but still maintains sufficient injection
of the system clock value to keep the accuracy errors bounded. 

Additionally, the number of times the fast path is executed before the system clock is updated
is bounded by a counter. It is much lighter cost to update a counter of fast path branches
than it is to call nanoTime(), so this yields a net speedup of around 50% on the top end of
rate limiter performance.

=TODO= Consider updating C without CAS

### Optimization: Thread.sleep(millis,nanos)

Worse than the calling overhead of System.nanoTime() is Thread.sleep(millis,nanos). While there are
other more arcane and timing capable methods of shaving off nanos where needed, these methods
are not efficient in a runtime that doesn't explicitly schedule all hardware threads. Thus,
to maintain overall efficiency, we resort to the standard primitives for slowing down threads
when needed: Sleeping. Still, calling an empty Thread.sleep(millis,nanos) has around 350ns overhead,
meaning that if you need to delay for less time than that with Sleep, you can't. In order to avoid the
inaccuracies of calling this method in this case, the delay threshold is further adjusted to
require more than 350 (actually 2x350) nanos of delay, or the op in question is simply allowed
to go unimpeded.

## Timing Accuracy

Because calling overhead, context switching, and other sources of jitter make it nearly impossible
to have precise and accurate timing at the nanosecond level, it should be made clear that
the rate limiter does not claim to do so. The rate limiter logic is tested in multiple ways,
including with synthetic clocks. Still, the testing is meant to ensure the baseline logic is
sane, and it can not account for system behavior in a real test. Results will vary, but
the design is focused on the need for maintaining a target rate at some *reasonable* level of
accuracy at some *usefully* high op rate.

## Maximum Throughput

# Other Things

- n<sub>**E**</sub> - _elapsed_ Nanos - The total amount of elapsed time according to the system clock.

## Sleep overhead compensation

An operation must be executed on or after its scheduled time, but not before.
This is the basic invariant that the rate limiter must ensure.

Given a rate per second _r_, and an operation in position
_p_ of a sequence of operations, the scheduled start time for that operation
is simply (r*p). However, the scheduled time is not predetermined for each and every
operation ahead of time. It is simply calculated by the rate limiter as requests
call it before starting an operation.



It is only the rate limiter that can determine when an operation may
actually be allowed to proceed, thus only the rate limiter can determine
the time difference between when an operation should have started and
when it actually started.
 
 
 
 
 
 ## Dynamic Rate Limiter Part Two
 
 This rate limiter design takes some ideas from existing designs and combines them
 together. The notion of token-based grants is used to facilitate high throughput
 in the highly contended case. Other mechanisms are used to allow for waittime
 calculation and bursting support.
 
 ## Concepts
 
 *Time Accumulator* - atomic time accumulators that track progress of different
 *measurable quantities of time. In the case of this rate limiter, these
 *represent a _relative_ idea of progress. As such, when the rate limiter is
 started, each of the accumulators is "zeroed out" to the current realtime
 clock value.
 
 *ideal_nanos* - The nano accumulator that tracks how many operations have completed according to a
 known op rate. This represents "work started", and nothing else. The total number of operations
 started can be calculated as `()`
 
 *scheduled_nanos* - The nano accumulator that shows how much time has been spent, whether or
 not this time was spent on starting work. Time can be spent by giving it to an operation, or
 it can be spent by giving it to wait time. When operations attempt to start too late, some
 of the previously unused time may be forfeited to waittime.
 
 *available_tokens quantity* - An atomic unit of work register, which can be accessed quickly.
 Callers are required to be blocked until this value is positive. They may then
 atomically decrement the value by one and then proceed.
 
 ## Explanation
 
 Under normal operation, the rate limiter has a free-running thread which does housekeeping
 at a configurable periodic interval, around 1us by default. There are two paths of interaction
 in the rate limiter that merit detailed explanation.
 
 ### The calling thread
 
 1. Checks for tokens, takes one when available, updates ideal time, and then returns waittime
 
 
 # BELOW THIS TBD
 
 ### The rate manager
 
 The rate manager is simply a thread that runs on behalf of the rate limiter for the purposes
 of proactively pacing and granting operations their time slices and timing data. To be specific:
 
 1. The gap from scheduled time to now is calculated. Up to a window size (the burst window) of
 tokens are added to the token bucket, and that amount of time is added to 
 
 The time delta between ideal time and optime is called waittime.
 The time delta between scheduled time and nowtime time is called  
 
 When there is no waittime, scheduled nanos and ideal nanos are equal.
 In this ideal case, the time delta between scheduled nanos and nowtime can be converted to
 tokens, and the scheduled time and ideal time can be advanced. However, there is a maximum
 delta that can be given to each.
 
 1. If the 
 
 
 1. The rate manager measures the time advance (positive difference) from the scheduled nanos
 and the current time, and allocates some  
 
 ##  
