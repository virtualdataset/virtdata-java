---
title: EngineBlock Users Guide
type: index
layout: single
weight: 0
---

[![Latest Release](https://maven-badges.herokuapp.com/maven-central/io.engineblock/eb-api/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.engineblock/engineblock/) [![Build Status](https://travis-ci.org/engineblock/engineblock.svg?branch=master)](https://travis-ci.org/engineblock/engineblock)

EngineBlock is a new power tool in the test tooling arsenal.

The design goals:

1. Provide a useful and intuitive Reusable Machine Pattern for constructing and
   reasoning about concurrent performance tests. To encourage this, the runtime
   machinery is based on 
   [simple and tangible core concepts](/user-guide/concepts/).
2. Reduce testing time of complex scenarios with many variables. This is
   achieved by controlling tests from an
   [open javascript sandbox](/user-guide/scenario_scripting/).
   This makes more sophisticated scenarios possible when needed. 
3. Minimize the amount of effort required to get empirical results from a 
   test cycle. For this, [metrics reporting](/user-guide/using_metrics/) is baked in.

In short, EngineBlock wishes to be a programmable power tool for performance
testing. However, it is somewhat generic. It doesn't know directly about a
particular type of system, or protocol. It simply provides a suitable machine
harness within which to put your drivers and testing logic. If you know how to
build a client for a particular kind of system, EngineBlock will let you load it
like a plugin and control it dynamically.

The most direct way to do this, if you are a tool developer, is to implement
your own ActivityTypes and embed EngineBlock with them as the core runtime. You
can always experiment with it and learn how it works by using the built-in
diagnostic drivers.

### History

The Engine Block project started as a branch of [test client](http://github.com/jshook/testclient). 
It has since evolved to be more generic.

## License

EngineBlock is licensed under the Apache Public License 2.0

## Contributing

If you are interested in contributing to Engine Block, more information is
available in the 
[Developer's Guide](/dev-guide/).





