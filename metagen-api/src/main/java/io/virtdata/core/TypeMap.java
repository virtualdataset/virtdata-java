package io.virtdata.core;

import java.util.Date;

public enum TypeMap {
    INTEGER(Integer.class,true),
    LONG(Long.class,true),
    STRING(String.class,false),
    DATE(Date.class,false);

    private final Class<?> typeClass;
    private final boolean isPrimitive;

    TypeMap(Class<?> typeClass, boolean isPrimitive) {
        this.typeClass = typeClass;
        this.isPrimitive = isPrimitive;
    }
}
