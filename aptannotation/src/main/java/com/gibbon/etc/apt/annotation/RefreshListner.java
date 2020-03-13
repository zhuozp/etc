package com.gibbon.etc.apt.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhipeng.zhuo
 * @date 2020-03-13
 * 搭配SwipeRefreshLayout控件ID
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface RefreshListner {

    /**
     * 默认和属性同名的id
     * */
    int value() default -1;

    /**
     * 刷新空间颜色设置，默认跟着app默认的
     * */
    int[] colors() default {};
}
