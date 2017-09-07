# conversions library

## description

This library allows you to convert between all primitive types and some additional object
types which are in common use. Scalar conversions are supported between all the primitive
types with a default behavior of wrapping at the maximum value of smaller target types.
Further, each conversion function may allow you to pass a modulo limit that can take
the place of the default MAX_VALUE. 

This behavior is more appropriate for generating data in the sense that you generally
want your data values to wrap rather than overflow. This allows you to generate simple
recipes which can continue providing data rather than throwing an error.

In general, conversion are supported from wider types to narrower types, and if any
converse conversion is provided, it will not have a wrapping parameter. For example,
it makes sense to modulo divide a long value down to an int range, but not necessarily
the other way around.

Nearly all conversion are supported between floating point and integer types, regardless
of the width of the data types involved.