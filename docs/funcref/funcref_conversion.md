# CATEGORY conversion
## LongToByte

Convert the input long value to a byte, with negative values
masked away.

- long -> LongToByte() -> Byte

## LongToShort

Convert the input value from long to short.

- long -> LongToShort() -> Short

## ModuloToBigDecimal

Return a {@code BigDecimal} value as the result of modulo division with the specified divisor.

- long -> ModuloToBigDecimal() -> java.math.BigDecimal
- long -> ModuloToBigDecimal(long: modulo) -> java.math.BigDecimal

## ModuloToBigInt

Return a {@code BigInteger} value as the result of modulo division with the specified divisor.

- long -> ModuloToBigInt() -> java.math.BigInteger
- long -> ModuloToBigInt(long: modulo) -> java.math.BigInteger

## ModuloToBoolean

Return a boolean value as the result of modulo division with the specified divisor.

- long -> ModuloToBoolean() -> Boolean

## ModuloToByte

Return a byte value as the result of modulo division with the specified divisor.

- long -> ModuloToByte(long: modulo) -> Byte

## ModuloToShort

Return a boolean value as the result of modulo division with the specified divisor.

- long -> ModuloToShort(long: modulo) -> Short

## StringDateWrapper

This function wraps an epoch time in milliseconds into a String
as specified in the format. The valid formatters are documented
at See <a href="https://www.joda.org/joda-time/apidocs/org/joda/time/format/DateTimeFormat.html">DateTimeFormat API Docs</a>

- long -> StringDateWrapper(String: format) -> String

## ToBigInt

Convert the input value to a {@code BigInteger}

- long -> ToBigInt() -> java.math.BigInteger

## ToBoolean

Convert the input value to a {@code boolean}

- long -> ToBoolean() -> Boolean
- Double -> ToBoolean() -> Boolean
- Float -> ToBoolean() -> Boolean
- Integer -> ToBoolean() -> Boolean

## ToByte

Convert the input value to a {@code Byte}.

- double -> ToByte() -> Byte
- double -> ToByte(int: modulo) -> Byte
- Float -> ToByte() -> Byte
- Float -> ToByte(int: modulo) -> Byte
- int -> ToByte() -> Byte
- int -> ToByte(int: modulo) -> Byte
- long -> ToByte() -> Byte
- long -> ToByte(int: modulo) -> Byte
- Short -> ToByte(int: scale) -> Byte
- Short -> ToByte() -> Byte
- String -> ToByte() -> Byte

## ToByteBuffer

Convert the input value to a {@code ByteBuffer}

- double -> ToByteBuffer() -> java.nio.ByteBuffer
- Float -> ToByteBuffer() -> java.nio.ByteBuffer
- int -> ToByteBuffer() -> java.nio.ByteBuffer
- long -> ToByteBuffer() -> java.nio.ByteBuffer
- Short -> ToByteBuffer() -> java.nio.ByteBuffer
- String -> ToByteBuffer() -> java.nio.ByteBuffer

## ToDouble

Convert the input value to a double.

- long -> ToDouble() -> double

## ToFloat

Convert the input value into a float.

- double -> ToFloat(double: scale) -> Float
- double -> ToFloat() -> Float
- int -> ToFloat(int: scale) -> Float
- int -> ToFloat() -> Float
- long -> ToFloat(long: scale) -> Float
- long -> ToFloat() -> Float
- Short -> ToFloat() -> Float
- String -> ToFloat() -> Float

## ToInetAddress

Convert the input value to a {@code java.net.InetAddress}

- long -> ToInetAddress() -> java.net.InetAddress

## ToInt

Convert the input value to an int with long modulus remainder.
If the scale is chosen, then the value is wrapped at this value.
Otherwise, Integer#MAX_VALUE is used.

- long -> ToInt() -> int
- Object -> ToInt() -> Integer
- double -> ToInt(int: scale) -> int
- double -> ToInt() -> int
- Double -> ToInt(int: scale) -> Integer
- Double -> ToInt() -> Integer
- long -> ToInt(int: scale) -> int
  - *ex:* `ToInt(1000)` - *converts a long input value to an int between 0 and 999, inclusive*
- long -> ToInt() -> int
  - *ex:* `ToInt()` - *converts a long input value to an int between 0 and 2147483647, inclusive *
- String -> ToInt() -> Integer

## ToLong

Convert the input value to a long.

- String -> ToLong() -> Long
- double -> ToLong(long: scale) -> long
- double -> ToLong() -> long
- Float -> ToLong(long: scale) -> Long
- Float -> ToLong() -> Long

## ToShort

Convert the input value to a short.

- double -> ToShort() -> Short
- double -> ToShort(int: modulo) -> Short
- Float -> ToShort() -> Short
- Float -> ToShort(int: modulo) -> Short
- int -> ToShort() -> Short
- int -> ToShort(int: scale) -> Short
- long -> ToShort() -> Short
- long -> ToShort(int: wrapat) -> Short
  - *notes:* This form allows for limiting the short values at a lower limit than Short.MAX_VALUE.
- String -> ToShort() -> Short

## ToString

Converts the input to the most obvious string representation with String.valueOf(...).
Forms which accept a function will evaluate that function first and then apply
String.valueOf() to the result.

- Object -> ToString() -> String
- double -> ToString() -> String
  - *ex:* `ToString()` - *map the double input value to a String*
- double -> ToString(java.util.function.DoubleUnaryOperator: df) -> String
  - *ex:* `ToString(Add(5.7D))` - *map the double input value X to X+5.7D and then to a String*
- double -> ToString(java.util.function.DoubleFunction<Double>: df) -> String
- double -> ToString(java.util.function.Function<Double,Double>: df) -> String
- Float -> ToString() -> String
- int -> ToString() -> String
- long -> ToString() -> String
- long -> ToString(java.util.function.LongUnaryOperator: f) -> String
- long -> ToString(java.util.function.LongFunction<?>: f) -> String
- long -> ToString(java.util.function.Function<Long,?>: f) -> String
- long -> ToString(java.util.function.LongToIntFunction: f) -> String
- long -> ToString(java.util.function.LongToDoubleFunction: f) -> String
- long -> ToString(io.virtdata.basicsmappers.from_long.to_byte.LongToByte: f) -> String
- long -> ToString(io.virtdata.basicsmappers.from_long.to_short.LongToShort: f) -> String

