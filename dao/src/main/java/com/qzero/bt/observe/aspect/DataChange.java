package com.qzero.bt.observe.aspect;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface DataChange {

    boolean enabled() default true;

    boolean delete() default false;

}
