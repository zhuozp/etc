package com.gibbon.etc.asepect;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.gibbon.etc.annotation.RequestPermissions;
import com.gibbon.etc.callback.IPermissionCallback;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import io.reactivex.functions.Consumer;

/**
 * @author zhipeng.zhuo
 * @date 2020-03-10
 */
@Aspect
public class ReqeustPermissionAspect {
    public static final String TAG = ReqeustPermissionAspect.class.getSimpleName();

    @SuppressLint("CheckResult")
    @Around("execution(@com.gibbon.etc.annotation.RequestPermissions void *(..))")
    public void requestPermission(final ProceedingJoinPoint joinPoint) throws Throwable {
        Log.d(TAG, "in requestPermission");
        Object[] args = joinPoint.getArgs();
        IPermissionCallback permissionCallback = null;
        if (args != null && args.length > 0) {
            permissionCallback = (IPermissionCallback) args[args.length - 1];
        }


        RequestPermissions requestPermissions = getMethodAnnotation(joinPoint, RequestPermissions.class);
        String[] permissions = requestPermissions.value();

        Object target = joinPoint.getTarget();
        FragmentActivity activity = null;
        if (target instanceof FragmentActivity){
            activity = (FragmentActivity) target;
        } else if (target instanceof Fragment){
            activity = ((Fragment)target).getActivity();
        }

        RxPermissions rxPermissions = new RxPermissions(activity);

        final IPermissionCallback callback = permissionCallback;
        rxPermissions.request(permissions).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    try {
                        joinPoint.proceed();
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                } else {
                    if (callback != null) {
                        callback.permissionGrantedState();
                    }
                }
            }
        });
    }

    private <T extends Annotation> T getMethodAnnotation(ProceedingJoinPoint joinPoint, Class<T> clazz) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        return method.getAnnotation(clazz);
    }
}
