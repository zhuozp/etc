package com.gibbon.etc.apt.generate;

import com.gibbon.etc.apt.annotation.RefreshListner;

import java.util.Iterator;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * @author zhipeng.zhuo
 * @date 2020-03-13
 */
public class RefreshGenerator implements IGenerator {

    @Override
    public void generate(Element executableElement, StringBuilder builder) {
        //获取注解值
        int id = executableElement.getAnnotation(RefreshListner.class).value();
        int[] colors = executableElement.getAnnotation(RefreshListner.class).colors();

        //获取变量名字
        String mothed = executableElement.getSimpleName().toString();
        builder.append("if (source instanceof android.app.Activity) {\n");
        if (id == -1) {
            builder.append("view = ((SwipeRefreshLayout) (((android.app.Activity) source).findViewById(" + "R.id." + mothed + ")));\n");
        } else {
            builder.append("view = ((SwipeRefreshLayout) (((android.app.Activity) source).findViewById(" + id + ")));\n");
        }
        builder.append("}else{\n");
        if (id == -1) {
            builder.append("view = ((SwipeRefreshLayout) (((android.view.View) source).findViewById(" + "R.id." + mothed + ")));\n");
        } else {
            builder.append("view = ((SwipeRefreshLayout) (((android.view.View) source).findViewById(" + id + ")));\n");
        }
        builder.append("}\n");
        if (colors.length > 0) {
            builder.append("view.setColorSchemeResources(");
            for (int i = 0; i < colors.length; i++) {
                if (i != colors.length - 1) {
                    builder.append(colors[i] + ",");
                } else {
                    builder.append(colors[i]);
                }
            }
            builder.append(");\n");
        }
        builder.append("view.setOnRefreshListener(() -> host." + mothed + "());\n");
    }

    @Override
    public void generateMethod(List<Element> elements, TypeElement typeElement, StringBuilder builder) {
        builder.append("public void initRefreshView(" + typeElement.getQualifiedName() + " host, Object source ) {\n");
        builder.append("SwipeRefreshLayout view;\n");
        Iterator<Element> iterator = elements.iterator();
        while (iterator.hasNext()) {
            Element element = iterator.next();
            RefreshListner annotation = element.getAnnotation(RefreshListner.class);
            if (annotation != null) {
                ExecutableElement variableElement = (ExecutableElement) element;
                generate(variableElement, builder);
                iterator.remove();
            }
        }
        builder.append("}");
    }

    @Override
    public void generateInitMethod(StringBuilder builder) {
        builder.append("initRefreshView(host,source);\n");
    }

    @Override
    public void generateImport(StringBuilder builder) {
        builder.append("import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;\n");
        builder.append("import com.gibbon.etc.apt.annotation.RefreshListner;\n");
    }
}
