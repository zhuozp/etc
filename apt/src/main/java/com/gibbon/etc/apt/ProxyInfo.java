package com.gibbon.etc.apt;

import com.gibbon.etc.apt.factory.ElementStateFactory;
import com.gibbon.etc.apt.uitls.Utils;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * @author zhipeng.zhuo
 * @date 2020-03-12
 */
public class ProxyInfo {

    /**
     * 包名
     * */
    private String packageName;

    /**
     * 生成的类名
     * */
    private String proxyClassName;

    /**
     * 外部类
     * */
    private TypeElement typeElement;

    /**
     * 类相关注解集合
     * */
    public List<Element> elementList = new ArrayList<>();

    /**
     * 类相关注解名称集合
     * */
    public HashSet<Class> classList = new HashSet<>();

    public static final String PROXY = "ViewInject";

    public ProxyInfo(Elements elements, TypeElement typeElement) {
        this.typeElement = typeElement;
        PackageElement packageElement = elements.getPackageOf(typeElement);
        this.packageName = packageElement.getQualifiedName().toString();
        String className = Utils.getClassName(typeElement, packageName);
        this.proxyClassName = className + "$" + PROXY;
    }

    /**
     * 获取全名
     *
     * @return
     */
    public String getProxyClassFullName() {
        return packageName + "." + proxyClassName;
    }

    /**
     * 获取TypeElement
     *
     * @return
     */
    public TypeElement getTypeElement() {
        return typeElement;
    }

    /**
     * 门面
     * */
    public String generate() {
        StringBuilder builder = ElementStateFactory.gegerateImport(classList, packageName);
        ElementStateFactory.gegerateClassName(builder, proxyClassName, typeElement);
        ElementStateFactory.generateInjectMethods(builder, classList, typeElement);
        ElementStateFactory.generateMethods(builder, classList, typeElement, elementList);

        return builder.toString();
    }
}
