package com.ximalaya.glue.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface GlueListener {

    String name() default "";

    /*
        从低到高执行

        order/before/after 只能设置一个
     */
    int order() default 0;

    String before() default "";

    String after() default "";
}
