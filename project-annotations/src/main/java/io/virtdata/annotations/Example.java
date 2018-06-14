package io.virtdata.annotations;


import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.CONSTRUCTOR})
@Repeatable(value = Examples.class)
public @interface Example {
    String[] value();
}
