package com.gibbon.etc.apt.uitls;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import static javax.lang.model.element.Modifier.PRIVATE;

/**
 * @author zhipeng.zhuo
 * @date 2020-03-13
 */
public class Utils {
    /**
     * 判断变量是不是PRIVATE
     *
     * @param annotatedClass
     * @return
     */
    public static boolean isPrivate(Element annotatedClass) {
        return annotatedClass.getModifiers().contains(PRIVATE);
    }


    /**
     * 拼接类名
     *
     * @param type
     * @param packageName
     * @return
     */
    public static String getClassName(TypeElement type, String packageName) {
        int packageLen = packageName.length() + 1;
        return type.getQualifiedName().toString().substring(packageLen)
                .replace('.', '$');
    }

    /**
     * 获取包名
     *
     * @param packageName
     * @return
     */
    public static String getLibrayPath(String packageName) {
        try {
            return packageName.substring(0, ordinalIndexOf(packageName, ".", 3));
        } catch (Exception e) {
            return packageName;
        }
    }

    public static int ordinalIndexOf(String str, String substr, int n) {
        int pos = str.indexOf(substr);
        while (--n > 0 && pos != -1) {
            pos = str.indexOf(substr, pos + 1);
        }
        return pos;
    }

    /**
     * 检测注解使用是否正确
     *
     * @param annotatedElement
     * @return
     */
    public static boolean checkAnnotationValid(Messager messager, Element annotatedElement) {
        //检测是否是变量
       /* if (annotatedElement.getKind() != ElementKind.FIELD) {
            error(annotatedElement, "%s must be declared on field.", clazz.getSimpleName());
            return false;
        }*/
        //检测这个变量是不是公有的
        if (isPrivate(annotatedElement)) {
            error(messager, annotatedElement, "%s() must can not be private.", annotatedElement.getSimpleName());
            return false;
        }
        return true;
    }

    public static void error(Messager messager, Element element, String message, Object... args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }
        messager.printMessage(Diagnostic.Kind.NOTE, message, element);
    }
}
