package io.virtdata.annotations;

import java.lang.annotation.*;


/**
 * Direct the user to additional resources
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SeeList {
    See[] value();
}
