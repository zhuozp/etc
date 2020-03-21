package com.gibbon.etc.apt.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhipeng.zhuo
 * @date 2020-03-12
 */
@Target(ElementType.FIELD)
@Inherited
@Retention(RetentionPolicy.CLASS)
public @interface ViewById {
    /**
     * 默认和属性同名的id，使用findViewById
     * */
    int value() default -1;
}
