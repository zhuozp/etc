package com.gibbon.etc.asepect;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import com.gibbon.etc.UtMonitor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * @author zhipeng.zhuo
 * @date 2020-03-09
 */
//@Aspect
public class PageMonitorAspect {

    public static final String TAG = PageMonitorAspect.class.getSimpleName();

//    @Around("execution(* android.app.Activity.onStart(..))")
    public void hookOnStart(ProceedingJoinPoint point) throws Throwable {
        if (point.getThis() instanceof Context) {
            Context context = (Context) point.getThis();
            if (UtMonitor.getInstance().getPageTimeMonitorMap().get(context.getClass().getCanonicalName()) == null) {
                UtMonitor.getInstance().getPageTimeMonitorMap().put(context.getClass().getCanonicalName(), SystemClock.uptimeMillis());
            }
        }

        point.proceed();
    }

//    @Around("execution(* android.app.Activity.onStop(..))")
    public void hookOnStop(ProceedingJoinPoint point) throws Throwable {
        if (point.getThis() instanceof Context) {
            Context context = (Context) point.getThis();
            Long time = UtMonitor.getInstance().getPageTimeMonitorMap().get(context.getClass().getCanonicalName());
            UtMonitor.getInstance().getPageTimeMonitorMap().remove(context.getClass().getCanonicalName());
            if (time != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("in ");
                sb.append(context.getClass().getSimpleName());
                sb.append(" page stay time is: ");
                sb.append(time);
                Log.i(TAG, sb.toString());
            }
        }

        point.proceed();
    }
}
