package com.house.everything_house_backend.config;

import java.lang.annotation.*;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AuthAccess {
}
