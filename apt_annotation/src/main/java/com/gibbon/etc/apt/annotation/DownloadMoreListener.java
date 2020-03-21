package com.gibbon.etc.apt.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhipeng.zhuo
 * @date 2020-03-13
 * 搭配recycleview使用
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface DownloadMoreListener {
    /**
     * 默认和属性同名的id
     * */
    int value() default -1;

    /**
    * 设置上滑到距离最后几个条目的时候开始加载更多
    * */
    int loadPosition() default -1;
}
