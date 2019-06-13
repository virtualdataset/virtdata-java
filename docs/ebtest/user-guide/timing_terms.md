---
title: Timing Terms
weight: 11
menu:
  main:
    parent: User Guide
    identifier: timing-terms
    weight: 22
---

Often, terms used to describe latency can create confusion. In fact, the term 
_latency_ is so overloaded in practice that it is not useful by itself.
Because of this, EngineBlock will avoid using the term latency _except in a
specific way_. Instead, the terms described in this section will be used.

EngineBlock is a client-centric testing tool. The measurement of operations occurs on 
the client, without visibility to what happens in transport or on the server. This 
means that the client *can* see how long an operation takes, but it *cannot see* how 
much of the operational time is spent in transport and otherwise. This has a bearing on
the terms that are adopted with EngineBlock.

Some terms are anchored by the context in which they are used. For latency terms, 
*service time* can be subjective. When using this term to describe other effects in your 
system, what is included depends on the perspective of the requester. The concept of 
service is universal, and every layer in a system can be seen as a service. Thus, the 
service time is defined by the vantage point of the requester.

### responsetime

**The duration of time a user has to wait for a response from the time they submitted the request.**
Response time is the duration of time from when a request was expected to start, to the time at 
which the response is finally seen by the user. A request is generally expected to start immediately
when users make a request. For example, when a user enters a URL into a browser, they expect the request
to start immediately when they hit enter. 

In EngineBlock, the response time can be calculated by adding the wait time and and the service time.

### waittime
 
**The duration of time between when an operation is intended to start and when it actually 
starts on a client.** This is also called *scheduling delay* in some places. Wait
time occurs because clients are not able to make all requests instantaneously.
There is an ideal time at which the request would be made according to user
demand. This ideal time is always earlier than the actual time in practice.
When there is a shortage of resources *of any kind* that delays a client request,
it must wait.

### servicetime

**The duration of time it takes a server or other system to fully process to a request and 
send a response.** From the perspective of a testing client, the _system_ includes the
infrastructure as well as remote servers. As such, the service time metrics in EngineBlock
include any operational time that is external to the client, including transport latency.
This will be the convention adopted going forward, and previously named metrics which are 
not explicit in this will be deprecated over time.

## Timing, Visually

<div align="middle"><img src="/diagrams/eb_latency_terms.svg" width="50%"></img></div>

## Semantics 

### Resources

As shown, the client has to wait for some resource to become available before
an operation can be started. *This resource can be anything, including memory,
threads, sockets, or time, as enforced by artificial constraints like rate 
limits.* In this diagram, the resource is a general purpose place holder 
for anything that can cause delay before a request is started from a client.
This even includes process and IO scheduling systems as found within operating
systems.

### Generality
 
Further, the basic relationship that is shown here between a client entity, a 
queue, and a service entity is a general purpose abstraction. 
You can apply this view of timing to any messaging system in which you 
have those elements in play. For example, within a server, multiple subsystems 
are needed to fulfill a request. A server process will often need to wait for
sufficient IO capacity in the event of a cold read. In this case, the server
process becomes a client of the IO subsystem, with the IO scheduler acting
as arbiter of the request. You can simply change the terms on the diagram
to match such a scenario and it will still ring true.
