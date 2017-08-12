package com.alacoder.lion.tcc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Compensable {

    public boolean simplified() default false;

    public Class<?> interfaceClass();

    public String confirmableKey() default "";

    public String cancellableKey() default "";

}
