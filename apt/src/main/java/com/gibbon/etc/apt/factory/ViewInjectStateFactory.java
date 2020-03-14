package com.gibbon.etc.apt.factory;

import java.io.IOException;
import java.io.Writer;

import javax.annotation.processing.Filer;
import javax.tools.JavaFileObject;

/**
 * @author zhipeng.zhuo
 * @date 2020-03-14
 */
public class ViewInjectStateFactory {

    public static final String VIEW_INJECT_NAME = "com.gibbon.etc.inject.ViewInject";
    public static final String VIEW_INJECTOR_NAME = "com.gibbon.etc.inject.ViewInjector";

    public static void generateViewInject(Filer filer) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("package com.gibbon.etc.inject;\n" +
                "\n" +
                "public interface ViewInject<A> {\n" +
                "    void inject(A a, Object source);\n" +
                "}");

        try {
            JavaFileObject jfo = filer.createSourceFile(VIEW_INJECT_NAME, null);
            Writer writer = jfo.openWriter();
            writer.write(stringBuilder.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {

        }
    }

    public static void generateViewInjector(Filer filer) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("package com.gibbon.etc.inject;\n" +
                "\n" +
                "import android.app.Activity;\n" +
                "import android.view.View;\n" +
                "\n" +

                "public class ViewInjector {\n" +
                "\n" +
                "    private static final String SUFFIX = \"$ViewInject\";\n" +
                "\n" +
                "    public static void inject(Activity activity) {\n" +
                "        ViewInject viewInject = getProxyInject(activity);\n" +
                "        if (viewInject != null) {\n" +
                "            viewInject.inject(activity, activity);\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    public static void inject(Object object, View view) {\n" +
                "        ViewInject viewInject = getProxyInject(object);\n" +
                "        if (viewInject != null) {\n" +
                "            viewInject.inject(object, view);\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    private static ViewInject getProxyInject(Object object) {\n" +
                "        try {\n" +
                "            Class cls = object.getClass();\n" +
                "            Class injectorCls = Class.forName(cls.getName() + SUFFIX);\n" +
                "            return (ViewInject) injectorCls.newInstance();\n" +
                "        } catch (Exception e) {\n" +
                "\n" +
                "        }\n" +
                "\n" +
                "        return null;\n" +
                "    }\n" +
                "}");

        try {
            JavaFileObject jfo = filer.createSourceFile(VIEW_INJECTOR_NAME , null);
            Writer writer = jfo.openWriter();
            writer.write(stringBuilder.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {

        }
    }
}
