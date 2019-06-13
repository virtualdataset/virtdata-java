---
title: Parameter Usage
weight: 12
menu:
  main:
    parent: Parameters
    identifier: parameter-usage
    weight: 26
---

To configure an EngineBlock activity to do something meaningful, you have to
provide parameters to it. This can occur in one of several ways. This section
is a guide on EngineBlock parameters, how they layer together, and when to
use one form over another.

## Command Line

The command line is used to configure both the overall EngineBlock runtime
(logging, etc) as well as the individual activities and scripts. Global
EngineBLock options can be distinguished from scenario commands and their
parameters because because global options always start with a single or double
hyphen.

In this section, we will only focus on scenario commands and their options.
Scenario commands are single words without any leading hyphens. Any command-line
argument that follows a scenario command in the form of `<name>=<value>` is a
parameter to that command.

## Activity Params

An activity is initially configured with a set of `<name>=<value>` arguments on
the command line. Parameters can have various useful properties as described below.

### Universal Params

Universal activity parameters are understood by all activities because they are
part of the core API. Universal params include type*, *alias*, and *threads*,
for example. Parameters that are universal should be documented as such anywhere
they are mentioned.
These parameters are explained individually in the [Activity Params](../activity_params)

### Dynamic Params
 
Dynamic parameters are parameters which may be changed while an activity is
running. This means that scenario scripting logic may change some variables
while an activity is running, and that the runtime should dynamically adjust to
match. Parameters that are dynamic should be documented as such anywhere they
are mentioned. The *threads* parameter is both a universal and a dynamic
parameter. For more information on using dynamic parameters while an activity is
running, see [Scripting Environment](/user-guide/scenario_scripting/#scripting-environment)   

## Statement Params

Some activities make use of a [Standard YAML](/user-guide/standard_yaml/) format
that allows for a set of statements and associated parameters to be defined in
declarative form. When this format contains `param:` maps, the params apply
directly to the associated statement definitions. These are called _statement
params_. Be careful, however, not to confuse these params with the values that
may be assigned to an individual instance of a statement or operation. The
params defined in the YAML allow you to affect *how* a statement is used in a
workload, not which values are associated with each instance of that statement.
(That is the job of the [bindings](/user-guide/standard_yaml/#bindings) in the
YAML format.)

## Template Params

If you need to provide general-purpose overrides to a named section of the
standard YAML, then you may use a mechanism called [Template
Params](/user-guide/standard_yaml/#template-params). These params are taken from
the values provided for an activity, and if no such named parameter is provided,
then the default is used as specified in the named template.

## Precedence

Now that we've described all the parameter types, let's tie them together.
When an activity is loaded from the command line or script, the parameters are
resolved in the following order:

1. The `type` parameter tells EngineBlock which activity type implementation to load.
2. The activity type implementation creates an activity.
3. The activity is initialized with the parameters provided.
4. If the activity uses the [Standard YAML](/user-guide/standard_yaml) as a config
   file, then tye `yaml` parameter is used to locate and load that file.
5. Any template parameters in the file in `<<varname:default value>>` form
   are resolved, taking override values from the provided params.
6. Finally, the activity is started.

