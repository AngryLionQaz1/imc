package com.snow.imc.config.annotation;

import java.lang.annotation.*;


@Target({ElementType.PARAMETER, ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Author {

    String value()  default "";


}
