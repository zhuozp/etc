package com.gibbon.etc.apt.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhipeng.zhuo
 * @date 2020-03-12
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface CheckBoxListener {
    /**
     * 默认使用同名方法，若加了使用设置的Id
     * */
    int value() default -1;
}
