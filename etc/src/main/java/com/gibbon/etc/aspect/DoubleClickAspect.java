package com.gibbon.etc.aspect;

import android.util.Log;

import com.gibbon.etc.annotation.ClickThrottle;
import com.gibbon.etc.utils.Utils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author zhipeng.zhuo
 * @date 2020-03-11
 */
@Aspect
public class DoubleClickAspect {

    public static final String TAG = DoubleClickAspect.class.getSimpleName();

    private boolean isDoubleClick = false;

    // normal onClick
    private static final String ON_CLICK_POINTCUTS = "execution(* android.view.View.OnClickListener.onClick(..))";
    // 如果 onClick 是写在 xml 里面的
    private static final String ON_CLICK_IN_XML_POINTCUTS = "execution(* android.support.v7.app.AppCompatViewInflater.DeclaredOnClickListener.onClick(..))";
    // butterknife on click
    private static final String ON_CLICK_IN_BUTTER_KNIFE_POINTCUTS = "execution(@butterknife.OnClick * *(..))";

    @Before("execution(@com.gibbon.etc.annotation.ClickThrottle  * *(..))")
    public void beforeEnableDoubleClcik(JoinPoint joinPoint) throws Throwable {
        ClickThrottle clickThrottle = Utils.getMethodAnnotation(joinPoint, ClickThrottle.class);
        if (clickThrottle.value() <= 0) {
            isDoubleClick = true;
        } else {
            NoDoubleClickUtils.SPACE_TIME = clickThrottle.value();
        }
    }

    @Pointcut(ON_CLICK_POINTCUTS)
    public void onClickPointcuts() {
    }

    @Pointcut(ON_CLICK_IN_XML_POINTCUTS)
    public void onClickInXmlPointcuts() {
    }

    @Pointcut(ON_CLICK_IN_BUTTER_KNIFE_POINTCUTS)
    public void onClickInButterKnifePointcuts() {
    }

    @Around("onClickPointcuts() || onClickInXmlPointcuts() || onClickInButterKnifePointcuts()")
    public void onClickLitener(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        if (isDoubleClick || !NoDoubleClickUtils.isDoubleClick()) {
            proceedingJoinPoint.proceed();
            isDoubleClick = false;
        } else {
            Log.d(TAG, "double click, SPACE_TIME is:  " + NoDoubleClickUtils.SPACE_TIME);
        }
    }

    public static class NoDoubleClickUtils {
        private static int SPACE_TIME = 500;
        private static long lastClickTime;

        public synchronized static boolean isDoubleClick() {
            long currentTime = System.currentTimeMillis();
            boolean isClick;
            if (currentTime - lastClickTime > SPACE_TIME) {
                isClick = false;
            } else {
                isClick = true;
            }
            lastClickTime = currentTime;
            return isClick;
        }
    }
}


