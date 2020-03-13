package com.gibbon.etc.inject;

import android.app.Activity;
import android.view.View;

/**
 * @author zhipeng.zhuo
 * @date 2020-03-13
 */
public class ViewInjector {

    private static final String SUFFIX = "$ViewInject";

    public static void inject(Activity activity) {
        ViewInject viewInject = getProxyInject(activity);
        if (viewInject != null) {
            viewInject.inject(activity, activity);
        }
    }

    public static void inject(Object object, View view) {
        ViewInject viewInject = getProxyInject(object);
        if (viewInject != null) {
            viewInject.inject(object, view);
        }
    }

    private static ViewInject getProxyInject(Object object) {
        try {
            Class cls = object.getClass();
            Class injectorCls = Class.forName(cls.getName() + SUFFIX);
            return (ViewInject) injectorCls.newInstance();
        } catch (Exception e) {

        }

        return null;
    }
}
