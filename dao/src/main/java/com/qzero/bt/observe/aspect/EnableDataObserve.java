package com.qzero.bt.observe.aspect;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)

public @interface EnableDataObserve {

    boolean enabled() default true;
    String idFieldName();
    int dataType();

}
