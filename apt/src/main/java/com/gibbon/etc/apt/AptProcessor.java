package com.gibbon.etc.apt;

import com.gibbon.etc.apt.annotation.DownloadMoreListener;
import com.gibbon.etc.apt.annotation.RefreshListner;
import com.gibbon.etc.apt.annotation.ViewById;
import com.gibbon.etc.apt.factory.ViewInjectStateFactory;
import com.gibbon.etc.apt.uitls.Utils;
import com.google.auto.service.AutoService;


import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;

/**
 * @author zhipeng.zhuo
 * @date 2020-03-12
 */
@AutoService(Processor.class)
public class AptProcessor extends AbstractProcessor {

    private Filer filer;
    private Elements elements;
    private Messager messager;

    private HashMap<String, ProxyInfo> proxyInfoHashMap = new HashMap<>();
    private List<Class> classList = new ArrayList<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
        elements = processingEnvironment.getElementUtils();
        messager = processingEnvironment.getMessager();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotationTypes = new LinkedHashSet<>();
        //添加需要支持的注解
        annotationTypes.add(ViewById.class.getCanonicalName());
        annotationTypes.add(DownloadMoreListener.class.getCanonicalName());
        annotationTypes.add(RefreshListner.class.getCanonicalName());
        return annotationTypes;

    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        proxyInfoHashMap.clear();

        classList.add(ViewById.class);
        classList.add(DownloadMoreListener.class);
        classList.add(RefreshListner.class);

        //保存注解
        if (!saveAnnotation(roundEnvironment)) {

            return false;
        }

        ViewInjectStateFactory.generateViewInject(filer);
        ViewInjectStateFactory.generateViewInjector(filer);

        //生成类
        for (String key : proxyInfoHashMap.keySet()) {
            ProxyInfo proxyInfo = proxyInfoHashMap.get(key);
            try {
                //创建一个新的源文件，并返回一个对象以允许写入它
                JavaFileObject jfo = filer.createSourceFile(
                        proxyInfo.getProxyClassFullName(),
                        proxyInfo.getTypeElement());
                Writer writer = jfo.openWriter();
                writer.write(proxyInfo.generate());
                writer.flush();
                writer.close();
            } catch (IOException e) {
                Utils.error(messager, proxyInfo.getTypeElement(),
                        "Unable to write injector for type %s: %s",
                        proxyInfo.getTypeElement(), e.getMessage());
            }
        }
        return true;
    }

    private boolean saveAnnotation(RoundEnvironment roundEnvironment) {
        for (Class cls : classList) {
            //获取被注解的元素
            Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(cls);
            for (Element element : elements) {
                //检查element类型
                if (!Utils.checkAnnotationValid(messager, element)) {
                    return false;
                }
                //获取到这个变量的外部类
                TypeElement typeElement = (TypeElement) element.getEnclosingElement();
                //获取外部类的类名
                String qualifiedName = typeElement.getQualifiedName().toString();
                //以外部类为单位保存
                ProxyInfo proxyInfo = proxyInfoHashMap.get(qualifiedName);
                if (proxyInfo == null) {
                    proxyInfo = new ProxyInfo(this.elements, typeElement);
                    proxyInfoHashMap.put(qualifiedName, proxyInfo);
                }
                //把这个注解保存到proxyInfo里面，用于实现功能
                proxyInfo.elementList.add(element);
                proxyInfo.classList.add(cls);
            }
        }

        return true;
    }
}
