# CATEGORY state
## Clear

Clears the per-thread map which is used by the Expr function.

- Object -> Clear() -> Object
  - *notes:* Clear all named entries from the per-thread map.
  - *ex:* `Clear()` - *clear all thread-local variables*
- Object -> Clear(String... names) -> Object
  - *notes:* Clear the specified names from the per-thread map.
  - *ex:* `Clear('foo')` - *clear the thread-local variable 'foo'*
  - *ex:* `Clear('foo','bar')` - *clear the thread-local variables 'foo' and 'bar'*
- long -> Clear() -> long
  - *notes:* Clear all named entries from the per-thread map.
  - *ex:* `Clear()` - *clear all thread-local variables*
- long -> Clear(String... names) -> long
  - *notes:* Clear the specified names from the per-thread map.
  - *ex:* `Clear('foo')` - *clear the thread-local variable 'foo'*
  - *ex:* `Clear('foo','bar')` - *clear the thread-local variables 'foo' and 'bar'*

## Load

Load a named value from the per-thread state map.
The previous input value will be forgotten, and the named value will replace it
before the next function in the chain.

- double -> Load(String: name) -> double
  - *ex:* `Load('foo')` - *for the current thread, load a double value from the named variable*
- double -> Load(String: name, double: defaultValue) -> double
  - *ex:* `Load('foo',432.0D)` - *for the current thread, load a double value from the named variable, or the defaultvalue if it is not yet defined.*
- double -> Load(java.util.function.Function<Object,Object>: nameFunc) -> double
  - *ex:* `Load(NumberNameToString())` - *for the current thread, load a double value from the named variable, where the variablename is provided by a function.*
- double -> Load(java.util.function.Function<Object,Object>: nameFunc, double: defaultValue) -> double
  - *ex:* `Load(NumberNameToString(),1234.5D)` - *for the current thread, load a double value from the named variable, where the variablename is provided by a function, or the default value if the named value is not yet defined.*
- long -> Load(String: name) -> long
  - *ex:* `Load('foo')` - *for the current thread, load a long value from the named variable*
- long -> Load(String: name, long: defaultValue) -> long
  - *ex:* `Load('foo', 423L)` - *for the current thread, load a long value from the named variable, or the default value if the variable is not yet defined*
- long -> Load(java.util.function.Function<Object,Object>: nameFunc) -> long
  - *ex:* `Load(NumberNameToString())` - *for the current thread, load a long value from the named variable, where the variable name is provided by the provided by a function.*
- long -> Load(java.util.function.Function<Object,Object>: nameFunc, long: defaultvalue) -> long
  - *ex:* `Load(NumberNameToString(),22L)` - *for the current thread, load a long value from the named variable, where the variable name is provided by the provided by a function, or the default value if the variable is not yet defined*
- Object -> Load(String: name) -> Object
  - *ex:* `Load('foo')` - *for the current thread, load an Object value from the named variable*
- Object -> Load(java.util.function.Function<Object,Object>: nameFunc) -> Object
  - *ex:* `Load(NumberNameToString())` - *for the current thread, load an Object value from the named variable, where the variable name is returned by the provided function*
- Object -> Load(String: name, Object: defaultValue) -> Object
  - *ex:* `Load('foo','testvalue')` - *for the current thread, load an Object value from the named variable, or the default value if the variable is not yet defined.*
- Object -> Load(java.util.function.Function<Object,Object>: nameFunc, Object: defaultValue) -> Object
  - *ex:* `Load(NumberNameToString(),'testvalue')` - *for the current thread, load an Object value from the named variable, where the variable name is returned by the provided function, or thedefault value if the variable is not yet defined.*
- long -> Load(String: name) -> Object
  - *ex:* `Load('foo')` - *for the current thread, load an Object value from the named variable*
- long -> Load(java.util.function.LongFunction<Object>: nameFunc) -> Object
  - *ex:* `Load(NumberNameToString())` - *for the current thread, load an Object value from the named variable, where the variable name is returned by the provided function*
- long -> Load(String: name, Object: defaultValue) -> Object
  - *ex:* `Load('foo','testvalue')` - *for the current thread, load an Object value from the named variable, or the default value if the variable is not yet defined.*
- long -> Load(java.util.function.LongFunction<Object>: nameFunc, Object: defaultValue) -> Object
  - *ex:* `Load(NumberNameToString(),'testvalue')` - *for the current thread, load an Object value from the named variable, where the variable name is returned by the provided function, or thedefault value if the variable is not yet defined.*
- int -> Load(String: name) -> int
  - *ex:* `Load('foo')` - *for the current thread, load an int value from the named variable*
- int -> Load(String: name, int: defaultValue) -> int
  - *ex:* `Load('foo',42)` - *for the current thread, load an int value from the named variable, or return the default value if it is undefined.*
- int -> Load(java.util.function.Function<Object,Object>: nameFunc) -> int
  - *ex:* `Load(NumberNameToString())` - *for the current thread, load an int value from the named variable, where the variable name is provided by a function.*
- int -> Load(java.util.function.Function<Object,Object>: nameFunc, int: defaultValue) -> int
  - *ex:* `Load(NumberNameToString(),42)` - *for the current thread, load an int value from the named variable, where the variable name is provided by a function, or the default value if the named variable is undefined.*
- String -> Load(String: name) -> String
  - *ex:* `Load('foo')` - *for the current thread, load a String value from the named variable*
- String -> Load(String: name, String: defaultvalue) -> String
  - *ex:* `Load('foo','track05')` - *for the current thread, load a String value from the named variable, or teh default value if the variable is not yet defined.*
- String -> Load(java.util.function.Function<Object,Object>: nameFunc) -> String
  - *ex:* `Load(NumberNameToString())` - *for the current thread, load a String value from the named variable, where the variable name is provided by a function*
- String -> Load(java.util.function.Function<Object,Object>: nameFunc, String: defaultValue) -> String
  - *ex:* `Load(NumberNameToString(),'track05')` - *for the current thread, load a String value from the named variable, where the variable name is provided by a function, or the default value if the variable is not yet defined.*

## LoadDouble

Load a value from a named thread-local variable, where the variable
name is fixed or a generated variable name from a provided function.
If the named variable is not defined, then the default value is returned.

- Object -> LoadDouble(String: name) -> Double
  - *ex:* `LoadDouble('foo')` - *for the current thread, load a double value from the named variable.*
- Object -> LoadDouble(String: name, double: defaultValue) -> Double
  - *ex:* `LoadDouble('foo',23D)` - *for the current thread, load a double value from the named variable,or the default value if the named variable is not defined.*
- Object -> LoadDouble(java.util.function.Function<Object,Object>: nameFunc) -> Double
  - *ex:* `LoadDouble(NumberNameToString())` - *for the current thread, load a double value from the named variable, where the variable name is provided by a function.*
- Object -> LoadDouble(java.util.function.Function<Object,Object>: nameFunc, double: defaultValue) -> Double
  - *ex:* `LoadDouble(NumberNameToString(),23D)` - *for the current thread, load a double value from the named variable,where the variable name is provided by a function, or the default value if the namedvariable is not defined.*
- long -> LoadDouble(String: name) -> double
  - *ex:* `LoadDouble('foo')` - *for the current thread, load a double value from the named variable.*
- long -> LoadDouble(String: name, double: defaultValue) -> double
  - *ex:* `LoadDouble('foo',23D)` - *for the current thread, load a double value from the named variable,or the default value if the named variable is not defined.*
- long -> LoadDouble(java.util.function.LongFunction<Object>: nameFunc) -> double
  - *ex:* `LoadDouble(NumberNameToString())` - *for the current thread, load a double value from the named variable, where the variable name is provided by a function.*
- long -> LoadDouble(java.util.function.LongFunction<Object>: nameFunc, double: defaultValue) -> double
  - *ex:* `LoadDouble(NumberNameToString(),23D)` - *for the current thread, load a double value from the named variable,where the variable name is provided by a function, or the default value if the namedvariable is not defined.*

## LoadFloat

Load a value from a named thread-local variable, where the variable
name is fixed or a generated variable name from a provided function.
If the named variable is not defined, then the default value is returned.

- Object -> LoadFloat(String: name) -> Float
  - *ex:* `LoadFloat('foo')` - *for the current thread, load a float value from the named variable.*
- Object -> LoadFloat(String: name, float: defaultValue) -> Float
  - *ex:* `LoadFloat('foo',23F)` - *for the current thread, load a float value from the named variable,or the default value if the named variable is not defined.*
- Object -> LoadFloat(java.util.function.Function<Object,Object>: nameFunc) -> Float
  - *ex:* `LoadFloat(NumberNameToString())` - *for the current thread, load a float value from the named variable,where the variable name is provided by a function.*
- Object -> LoadFloat(java.util.function.Function<Object,Object>: nameFunc, float: defaultValue) -> Float
  - *ex:* `LoadFloat(NumberNameToString(),23F)` - *for the current thread, load a float value from the named variable,where the variable name is provided by a function, or the default value if the namedvariable is not defined.*
- long -> LoadFloat(String: name) -> Float
  - *ex:* `LoadFloat('foo')` - *for the current thread, load a float value from the named variable.*
- long -> LoadFloat(String: name, float: defaultValue) -> Float
  - *ex:* `LoadFloat('foo',23F)` - *for the current thread, load a float value from the named variable,or the default value if the named variable is not defined.*
- long -> LoadFloat(java.util.function.LongFunction<Object>: nameFunc) -> Float
  - *ex:* `LoadFloat(NumberNameToString())` - *for the current thread, load a float value from the named variable,where the variable name is provided by a function.*
- long -> LoadFloat(java.util.function.LongFunction<Object>: nameFunc, float: defaultValue) -> Float
  - *ex:* `LoadFloat(NumberNameToString(),23F)` - *for the current thread, load a float value from the named variable,where the variable name is provided by a function, or the default value if the namedvariable is not defined.*

## LoadInteger

Load a value from a named thread-local variable, where the variable
name is fixed or a generated variable name from a provided function.
If the named variable is not defined, then the default value is returned.

- Object -> LoadInteger(String: name) -> Integer
  - *ex:* `LoadInteger('foo')` - *for the current thread, load an integer value from the named variable.*
- Object -> LoadInteger(String: name, int: defaultValue) -> Integer
  - *ex:* `LoadInteger('foo',42)` - *for the current thread, load an integer value from the named variable, or the default value if the named variable is not defined.*
- Object -> LoadInteger(java.util.function.Function<Object,Object>: nameFunc) -> Integer
  - *ex:* `LoadInteger(NumberNameToString())` - *for the current thread, load an integer value from the named variable,where the variable name is provided by a function.*
- Object -> LoadInteger(java.util.function.Function<Object,Object>: nameFunc, int: defaultValue) -> Integer
  - *ex:* `LoadInteger(NumberNameToString(),42)` - *for the current thread, load an integer value from the named variable,where the variable name is provided by a function, or the default value if the namedvariable is not defined.*
- long -> LoadInteger(String: name) -> int
  - *ex:* `LoadInteger('foo')` - *for the current thread, load an integer value from the named variable.*
- long -> LoadInteger(String: name, int: defaultValue) -> int
  - *ex:* `LoadInteger('foo',42)` - *for the current thread, load an integer value from the named variable, or the default value if the named variable is not defined.*
- long -> LoadInteger(java.util.function.LongFunction<Object>: nameFunc) -> int
  - *ex:* `LoadInteger(NumberNameToString())` - *for the current thread, load an integer value from the named variable,where the variable name is provided by a function.*
- long -> LoadInteger(java.util.function.LongFunction<Object>: nameFunc, int: defaultValue) -> int
  - *ex:* `LoadInteger(NumberNameToString(),42)` - *for the current thread, load an integer value from the named variable,where the variable name is provided by a function, or the default value if the namedvariable is not defined.*

## LoadLong

Load a value from a named thread-local variable, where the variable
name is fixed or a generated variable name from a provided function.
If the named variable is not defined, then the default value is returned.

- Object -> LoadLong(String: name) -> Long
  - *ex:* `LoadLong('foo',42L)` - *for the current thread, load a long value from the named variable.*
- Object -> LoadLong(String: name, long: defaultValue) -> Long
  - *ex:* `LoadLong('foo',42L)` - *for the current thread, load a long value from the named variable, or the default value if the named variable is not defined.*
- Object -> LoadLong(java.util.function.Function<Object,Object>: nameFunc) -> Long
  - *ex:* `LoadLong(NumberNameToString(),42L)` - *for the current thread, load a long value from the named variable,where the variable name is provided by a function.*
- Object -> LoadLong(java.util.function.Function<Object,Object>: nameFunc, long: defaultValue) -> Long
  - *ex:* `LoadLong(NumberNameToString(),42L)` - *for the current thread, load a long value from the named variable,where the variable name is provided by a function, or the default value if the namedvariable is not defined.*
- long -> LoadLong(String: name) -> long
  - *ex:* `LoadLong('foo',42L)` - *for the current thread, load a long value from the named variable.*
- long -> LoadLong(String: name, long: defaultValue) -> long
  - *ex:* `LoadLong('foo',42L)` - *for the current thread, load a long value from the named variable, or the default value if the named variable is not defined.*
- long -> LoadLong(java.util.function.LongFunction<Object>: nameFunc) -> long
  - *ex:* `LoadLong(NumberNameToString(),42L)` - *for the current thread, load a long value from the named variable,where the variable name is provided by a function.*
- long -> LoadLong(java.util.function.LongFunction<Object>: nameFunc, long: defaultValue) -> long
  - *ex:* `LoadLong(NumberNameToString(),42L)` - *for the current thread, load a long value from the named variable,where the variable name is provided by a function, or the default value if the namedvariable is not defined.*

## LoadString

Load a value from a named thread-local variable, where the variable
name is fixed or a generated variable name from a provided function.
If the named variable is not defined, then the default value is returned.

- Object -> LoadString(String: name) -> String
  - *ex:* `LoadString('foo','examplevalue')` - *for the current thread, load a String value from the named variable.*
- Object -> LoadString(String: name, String: defaultValue) -> String
  - *ex:* `LoadString('foo','examplevalue')` - *for the current thread, load a String value from the named variable, or the default value if the named variable is not defined.*
- Object -> LoadString(java.util.function.Function<Object,Object>: nameFunc) -> String
  - *ex:* `LoadString(NumberNameToString(),'examplevalue')` - *for the current thread, load a String value from the named variable, or the default value if the named variable is not defined.*
- Object -> LoadString(java.util.function.Function<Object,Object>: nameFunc, String: defaultValue) -> String
  - *ex:* `LoadString(NumberNameToString(),'examplevalue')` - *for the current thread, load a String value from the named variable,where the variable name is provided by a function, or the default value if the namedvariable is not defined.*
- long -> LoadString(String: name) -> String
  - *ex:* `LoadString('foo','examplevalue')` - *for the current thread, load a String value from the named variable.*
- long -> LoadString(String: name, String: defaultValue) -> String
  - *ex:* `LoadString('foo','examplevalue')` - *for the current thread, load a String value from the named variable, or the default value if the named variable is not defined.*
- long -> LoadString(java.util.function.LongFunction<Object>: nameFunc) -> String
  - *ex:* `LoadString(NumberNameToString(),'examplevalue')` - *for the current thread, load a String value from the named variable, or the default value if the named variable is not defined.*
- long -> LoadString(java.util.function.LongFunction<Object>: nameFunc, String: defaultValue) -> String
  - *ex:* `LoadString(NumberNameToString(),'examplevalue')` - *for the current thread, load a String value from the named variable,where the variable name is provided by a function, or the default value if the namedvariable is not defined.*

## Save

Save the current input value at this point in the function chain to a thread-local variable name.
The input value is unchanged, and available for the next function in the chain to use as-is.

- double -> Save(String: name) -> double
  - *ex:* `Save('foo')` - *for the current thread, save the current double value to the named variable.*
- double -> Save(java.util.function.Function<Object,Object>: nameFunc) -> double
  - *ex:* `Save(NumberNameToString())` - *for the current thread, save the current double value to the name 'foo' in this thread, where the variable name is provided by a function.*
- long -> Save(String: name) -> long
  - *ex:* `Save('foo')` - *save the current long value to the name 'foo' in this thread*
- long -> Save(java.util.function.Function<Object,Object>: nameFunc) -> long
  - *ex:* `Save(NumberNameToString())` - *save the current long value to the name generated by the function given.*
- Object -> Save(String: name) -> Object
  - *ex:* `Save('foo')` - *for the current thread, save the input object value to the named variable*
- Object -> Save(java.util.function.Function<Object,Object>: nameFunc) -> Object
  - *ex:* `Save(NumberNameToString())` - *for the current thread, save the current input object value to the named variable,where the variable name is provided by a function.*
- long -> Save(String: name) -> long
  - *ex:* `Save('foo')` - *for the current thread, save the input object value to the named variable*
- long -> Save(java.util.function.LongFunction<Object>: nameFunc) -> long
  - *ex:* `Save(NumberNameToString())` - *for the current thread, save the current input object value to the named variable,where the variable name is provided by a function.*
- int -> Save(String: name) -> int
  - *ex:* `Save('foo')` - *save the current int value to the name 'foo' in this thread*
- int -> Save(java.util.function.Function<Object,Object>: nameFunc) -> int
  - *ex:* `Save(NumberNameToString())` - *save the current int value to a named variable in this thread,where the variable name is provided by a function.*
- String -> Save(String: name) -> String
  - *ex:* `Save('foo')` - *save the current String value to the name 'foo' in this thread*
- String -> Save(java.util.function.Function<Object,Object>: nameFunc) -> String
  - *ex:* `Save(NumberNameToString())` - *save the current String value to a named variable in this thread, where the variable name is provided by a function*

## SaveDouble

Save a value to a named thread-local variable, where the variable
name is fixed or a generated variable name from a provided function.
Note that the input type is not that suitable for constructing names,
so this is more likely to be used in an indirect naming pattern like
`SaveDouble(Load('id'))`

- double -> SaveDouble(String: name) -> double
  - *ex:* `Save('foo')` - *save the current double value to the name 'foo' in this thread*
- double -> SaveDouble(java.util.function.Function<Object,Object>: nameFunc) -> double
  - *ex:* `Save(NumberNameToString())` - *save a double value to a named variable in the current thread, where the variable name is provided by a function.*
- long -> SaveDouble(String: name) -> double
  - *ex:* `Save('foo')` - *save the current double value to the name 'foo' in this thread*
- long -> SaveDouble(java.util.function.LongFunction<Object>: nameFunc) -> double
  - *ex:* `Save(NumberNameToString())` - *save a double value to a named variable in the current thread, where the variable name is provided by a function.*

## SaveFloat

Save a value to a named thread-local variable, where the variable
name is fixed or a generated variable name from a provided function.
Note that the input type is not that suitable for constructing names,
so this is more likely to be used in an indirect naming pattern like
`SaveDouble(Load('id'))`

- Float -> SaveFloat(String: name) -> Float
  - *ex:* `SaveFloat('foo')` - *save the current float value to a named variable in this thread.*
- Float -> SaveFloat(java.util.function.Function<Object,Object>: nameFunc) -> Float
  - *ex:* `SaveFloat(NumberNameToString())` - *save the current float value to a named variable in this thread, where the variable name is provided by a function.*
- long -> SaveFloat(String: name) -> Float
  - *ex:* `SaveFloat('foo')` - *save the current float value to a named variable in this thread.*
- long -> SaveFloat(java.util.function.LongFunction<Object>: nameFunc) -> Float
  - *ex:* `SaveFloat(NumberNameToString())` - *save the current float value to a named variable in this thread, where the variable name is provided by a function.*

## SaveInteger

Save a value to a named thread-local variable, where the variable
name is fixed or a generated variable name from a provided function.
Note that the input type is not that suitable for constructing names,
so this is more likely to be used in an indirect naming pattern like
`SaveDouble(Load('id'))`

- int -> SaveInteger(String: name) -> int
  - *ex:* `SaveInteger('foo')` - *save the current integer value to a named variable in this thread.*
- int -> SaveInteger(java.util.function.Function<Object,Object>: nameFunc) -> int
  - *ex:* `SaveInteger(NumberNameToString())` - *save the current integer value to a named variable in this thread, where the variable name is provided by a function.*
- long -> SaveInteger(String: name) -> int
  - *ex:* `SaveInteger('foo')` - *save the current integer value to a named variable in this thread.*
- long -> SaveInteger(java.util.function.LongFunction<Object>: nameFunc) -> int
  - *ex:* `SaveInteger(NumberNameToString())` - *save the current integer value to a named variable in this thread, where the variable name is provided by a function.*

## SaveLong

Save a value to a named thread-local variable, where the variable
name is fixed or a generated variable name from a provided function.
Note that the input type is not that suitable for constructing names,
so this is more likely to be used in an indirect naming pattern like
`SaveDouble(Load('id'))`

- long -> SaveLong(String: name) -> long
  - *ex:* `SaveLong('foo')` - *save the current long value to a named variable in this thread.*
- long -> SaveLong(java.util.function.Function<Object,Object>: nameFunc) -> long
  - *ex:* `SaveLong(NumberNameToString())` - *save the current long value to a named variable in this thread, where the variable name is provided by a function.*
- long -> SaveLong(String: name) -> long
  - *ex:* `SaveLong('foo')` - *save the current long value to a named variable in this thread.*
- long -> SaveLong(java.util.function.Function<Object,Object>: nameFunc) -> long
  - *ex:* `SaveLong(NumberNameToString())` - *save the current long value to a named variable in this thread, where the variable name is provided by a function.*

## SaveString

Save a value to a named thread-local variable, where the variable
name is fixed or a generated variable name from a provided function.
Note that the input type is not that suitable for constructing names,
so this is more likely to be used in an indirect naming pattern like
`SaveDouble(Load('id'))`

- String -> SaveString(String: name) -> String
  - *ex:* `SaveString('foo')` - *save the current String value to a named variable in this thread.*
- String -> SaveString(java.util.function.Function<Object,Object>: nameFunc) -> String
  - *ex:* `SaveString(NumberNameToString())` - *save the current String value to a named variable in this thread, where the variable name is provided by a function.*
- long -> SaveString(String: name) -> String
  - *ex:* `SaveString('foo')` - *save the current String value to a named variable in this thread.*
- long -> SaveString(java.util.function.LongFunction<Object>: nameFunc) -> String
  - *ex:* `SaveString(NumberNameToString())` - *save the current String value to a named variable in this thread, where the variable name is provided by a function.*

## Show

Show diagnostic values for the thread-local variable map.

- Object -> Show() -> String
  - *ex:* `Show()` - *Show all values in a json-like format*
- Object -> Show(String... names) -> String
  - *ex:* `Show('foo')` - *Show only the 'foo' value in a json-like format*
  - *ex:* `Show('foo','bar')` - *Show the 'foo' and 'bar' values in a json-like format*
- long -> Show() -> String
  - *ex:* `Show()` - *Show all values in a json-like format*
- long -> Show(String... names) -> String
  - *ex:* `Show('foo')` - *Show only the 'foo' value in a json-like format*
  - *ex:* `Show('foo','bar')` - *Show the 'foo' and 'bar' values in a json-like format*

## Swap

Load a named value from the per-thread state map.
The previous input value will be stored in the named value, and the previously
stored value will be returned. A default value to return may be provided
in case there was no previously stored value under the given name.

- long -> Swap(String: name) -> long
  - *ex:* `Swap('foo')` - *for the current thread, swap the input value with the named variable and returned the named variable.*
- long -> Swap(String: name, long: defaultValue) -> long
  - *ex:* `Swap('foo',234L)` - *for the current thread, swap the input value with the named variable and returned the named variable,or the default value if the named variable is not defined.*
- long -> Swap(java.util.function.LongFunction<String>: nameFunc) -> long
  - *ex:* `Swap(NumberNameToString())` - *for the current thread, swap the input value with the named variable and returned the named variable, where the variable name is generated by the provided function.*
- long -> Swap(java.util.function.LongFunction<String>: nameFunc, long: defaultValue) -> long
  - *ex:* `Swap(NumberNameToString(), 234L)` - *for the current thread, swap the input value with the named variable and returned the named variable, where the variable name is generated by the provided function, or the default value if the named variable is not defined.*
- Object -> Swap(String: name) -> Object
  - *ex:* `Swap('foo')` - *for the current thread, swap the input value with the named variable and returned the named variable*
- Object -> Swap(String: name, Object: defaultValue) -> Object
  - *ex:* `Swap('foo','examplevalue')` - *for the current thread, swap the input value with the named variable and returned the named variable, or return the default value if the named value is not defined.*
- Object -> Swap(java.util.function.Function<Object,Object>: nameFunc) -> Object
  - *ex:* `Swap(NumberNameToString())` - *for the current thread, swap the input value with the named variable and returned the named variable, where the variable name is generated by the provided function.*
- Object -> Swap(java.util.function.Function<Object,Object>: nameFunc, Object: defaultValue) -> Object
  - *ex:* `Swap(NumberNameToString(),'examplevalue')` - *for the current thread, swap the input value with the named variable and returned the named variable, where the variable name is generated by the provided function, or the default value if the named value is not defined.*
- long -> Swap(String: name) -> Object
  - *ex:* `Swap('foo')` - *for the current thread, swap the input value with the named variable and returned the named variable*
- long -> Swap(String: name, Object: defaultValue) -> Object
  - *ex:* `Swap('foo','examplevalue')` - *for the current thread, swap the input value with the named variable and returned the named variable, or return the default value if the named value is not defined.*
- long -> Swap(java.util.function.LongFunction<Object>: nameFunc) -> Object
  - *ex:* `Swap(NumberNameToString())` - *for the current thread, swap the input value with the named variable and returned the named variable, where the variable name is generated by the provided function.*
- long -> Swap(java.util.function.LongFunction<Object>: nameFunc, Object: defaultValue) -> Object
  - *ex:* `Swap(NumberNameToString(),'examplevalue')` - *for the current thread, swap the input value with the named variable and returned the named variable, where the variable name is generated by the provided function, or the default value if the named value is not defined.*

