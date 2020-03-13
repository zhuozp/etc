package com.gibbon.etc.inject;

/**
 * @author zhipeng.zhuo
 * @date 2020-03-13
 */
public interface ViewInject<A> {
    void inject(A a, Object source);
}
