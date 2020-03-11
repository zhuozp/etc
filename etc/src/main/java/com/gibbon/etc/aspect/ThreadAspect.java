package com.gibbon.etc.aspect;

import android.annotation.SuppressLint;

import com.gibbon.etc.annotation.Background;
import com.gibbon.etc.annotation.UiThread;
import com.gibbon.etc.utils.Utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zhipeng.zhuo
 * @date 2020-03-09
 */
@Aspect
public class ThreadAspect {

    @SuppressLint("CheckResult")
    @Around("execution(@com.gibbon.etc.annotation.Background void *(..))")
    public void doBackground(final ProceedingJoinPoint joinPoint) {
        final Background background = Utils.getMethodAnnotation(joinPoint, Background.class);
         Single.timer(background.delay(), TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        try {
                            joinPoint.proceed();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }
                });
    }


    @SuppressLint("CheckResult")
    @Around("execution(@com.gibbon.etc.annotation.UiThread void *(..))")
    public void doUiThread(final ProceedingJoinPoint joinPoint) {

        UiThread uiThread = Utils.getMethodAnnotation(joinPoint, UiThread.class);

        Single.timer(uiThread.delay(), TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        try {
                            joinPoint.proceed();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }
                });
    }

}