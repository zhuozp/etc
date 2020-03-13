package com.gibbon.etc.apt.generate;

import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * @author zhipeng.zhuo
 * @date 2020-03-12
 */
public interface IGenerator {
    void generateMethod(List<Element> elements, TypeElement typeElement, StringBuilder builder);

    void generate(Element executableElement, StringBuilder builder);

    void generateInitMethod(StringBuilder builder);

    void generateImport(StringBuilder builder);
}
