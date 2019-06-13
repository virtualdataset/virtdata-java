---
title: Example Command
weight: 13
menu:
  main:
    parent: Parameters
    identifier: example-command
    weight: 27
---

Most new users will be interested in a command line pattern that looks very much like this:

    ./eb... run type=stdout alias=test1 yaml=mystmts cycles=5M -v
    
This is basically telling EngineBlock to load an activity type known as
_stdout_, naming it _test1_, with an input interval between 0 (inclusive) and
5000000 (exclusive), with statements and any other settings loaded from the file
_test1.yaml_, using the the [Standard YAML](/user-guide/standard_yaml). This is
a very common usage pattern for tools based on EngineBlock.

If you wanted to
crank up the concurrency and thread-level work size for speed, you could add
`threads=100` and `stride=1000` respectively. This would tell EngineBlock to
also use 100 threads for this activity, with each thread taking 1000 cycle
values at a time before going back to the input for the next set of cycle values
to iterate on.

