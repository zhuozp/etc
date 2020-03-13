package com.gibbon.etc.apt.factory;

import com.gibbon.etc.apt.ProxyInfo;
import com.gibbon.etc.apt.annotation.DownloadMoreListener;
import com.gibbon.etc.apt.annotation.RefreshListner;
import com.gibbon.etc.apt.annotation.ViewById;
import com.gibbon.etc.apt.generate.DownLoadMoreGenerator;
import com.gibbon.etc.apt.generate.IGenerator;
import com.gibbon.etc.apt.generate.RefreshGenerator;
import com.gibbon.etc.apt.generate.ViewByIdGenerator;
import com.gibbon.etc.apt.uitls.Utils;

import java.util.HashSet;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.swing.text.View;

/**
 * @author zhipeng.zhuo
 * @date 2020-03-12
 */
public class ElementStateFactory {

    public static StringBuilder gegerateImport(HashSet<Class> classList, String packageName) {
        StringBuilder builder = new StringBuilder();
        builder.append("// Generated code. Do not modify!\n");
        builder.append("package ").append(packageName).append(";\n\n");
        builder.append("import android.view.View;\n");
        builder.append("import ").append(Utils.getLibrayPath(packageName)).append(".R.*;\n");
        builder.append("import com.gibbon.etc.inject.ViewInject;\n");

        IGenerator generate = null;
        for (Class annotation : classList) {
            if (annotation == DownloadMoreListener.class) {
                generate = new DownLoadMoreGenerator();
            } else if (annotation == RefreshListner.class) {
                generate = new RefreshGenerator();
            } else if (annotation == ViewById.class) {
                generate = new ViewByIdGenerator();
            }


            if (generate != null) {
                generate.generateImport(builder);
            }
        }

        builder.append('\n');


        return builder;
    }

    public static void gegerateClassName(StringBuilder builder, String proxyClassName,  TypeElement element) {
        builder.append("public class ").append(proxyClassName).append(" implements " + ProxyInfo.PROXY + "<" + element.getQualifiedName() + ">");
        builder.append(" {\n");
    }

    public static void generateInjectMethods(StringBuilder builder, HashSet<Class> classList, TypeElement element) {
        builder.append("@Override\n ");
        builder.append("public void inject(" + element.getQualifiedName() + " host, Object source ) {\n");

        IGenerator generate = null;
        for (Class annotation : classList) {
            if (annotation == DownloadMoreListener.class) {
                generate = new DownLoadMoreGenerator();
            } else if (annotation == RefreshListner.class) {
                generate = new RefreshGenerator();
            } else if (annotation == ViewById.class) {
                generate = new ViewByIdGenerator();
            }


            if (generate != null) {
                generate.generateInitMethod(builder);
            }
        }

        builder.append("  }\n");
    }

    public static void generateMethods(StringBuilder builder, HashSet<Class> classList, TypeElement typeElement, List<Element> elements) {
        IGenerator generate = null;
        for (Class annotation : classList){
            if (annotation == DownloadMoreListener.class) {
                generate = new DownLoadMoreGenerator();
            } else if (annotation == RefreshListner.class) {
                generate = new RefreshGenerator();
            } else if (annotation == ViewById.class) {
                generate = new ViewByIdGenerator();
            }


            if (generate != null) {
                generate.generateMethod(elements, typeElement, builder);
            }
        }

        builder.append('\n');
        builder.append("}\n");
    }
}
