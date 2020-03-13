package com.gibbon.etc.apt.generate;

import com.gibbon.etc.apt.annotation.ViewById;

import java.util.Iterator;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * @author zhipeng.zhuo
 * @date 2020-03-13
 */
public class ViewByIdGenerator implements IGenerator {

    @Override
    public void generateMethod(List<Element> elements, TypeElement typeElement, StringBuilder builder) {
        builder.append("public void initViewById(" + typeElement.getQualifiedName() + " host, Object source ) {\n");
        Iterator<Element> iterator = elements.iterator();
        while (iterator.hasNext()) {
            Element element = iterator.next();
            ViewById annotation = element.getAnnotation(ViewById.class);
            if (annotation != null) {
                VariableElement variableElement = (VariableElement) element;
                generate(variableElement, builder);
                iterator.remove();
            }
        }
        builder.append("}");
    }

    @Override
    public void generate(Element executableElement, StringBuilder builder) {
        //获取注解值
        int id = executableElement.getAnnotation(ViewById.class).value();
        //获取变量类型
        String type = executableElement.asType().toString();
        //获取变量名字
        String name = executableElement.getSimpleName().toString();

        builder.append(" if(source instanceof android.app.Activity){\n");
        builder.append("host." + name).append(" = ");
        if (id == -1) {
            builder.append("(" + type + ")(((android.app.Activity)source).findViewById( " + "R.id." + name + "));\n");
        } else {
            builder.append("(" + type + ")(((android.app.Activity)source).findViewById( " + id + "));\n");
        }
        builder.append("\n}else{\n");

        builder.append("host." + name).append(" = ");
        if (id == -1) {
            builder.append("(" + type + ")(((android.view.View)source).findViewById( " + "R.id." + name + "));\n");
        } else {
            builder.append("(" + type + ")(((android.view.View)source).findViewById( " + id + "));\n");
        }
        builder.append("}\n");
    }

    @Override
    public void generateInitMethod(StringBuilder builder) {
        builder.append("initViewById(host,source);\n ");
    }

    @Override
    public void generateImport(StringBuilder builder) {
        builder.append("import com.gibbon.etc.apt.annotation.ViewById;\n");
    }
}
