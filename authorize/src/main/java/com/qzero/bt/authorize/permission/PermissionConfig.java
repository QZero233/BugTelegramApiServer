package com.qzero.bt.authorize.permission;

import com.qzero.bt.authorize.data.TokenEntity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionConfig {

    /**
     * The permission name
     * @return
     */
    String value();

    int minPermissionLevel() default TokenEntity.PERMISSION_LEVEL_APPLICATION;

    int[] deniedPermissionLevels() default {};

}
