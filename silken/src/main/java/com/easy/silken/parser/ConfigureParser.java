package com.easy.silken.parser;

import com.easy.silken.generator.ConfigureGenerator;
import com.easy.silken.info.ConfigInfo;
import com.easy.silken.util.ElementUtil;
import com.easy.silken.util.MessageUtil;
import com.easy.silken.util.Validator;
import com.easy.spider.annotation.Config;
import com.easy.spider.annotation.GlobalInterceptor;
import com.easy.spider.annotation.Grpc;
import com.squareup.javapoet.ClassName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import static com.easy.silken.util.AnnotationElementUtil.getAnnotationElements;

/**
 * Copyright: 杭州医本健康科技有限公司(2017)
 * Created by handgunbreak on 17/1/14.
 * Mail: handgunbreak@gmail.com
 * <p>
 * project: Phantom
 * Description:Configure注解解析
 * Version: v:1.0
 */
public class ConfigureParser {

    /**
     * 解析Configure注解类并生成新类
     *
     * @param filer filter
     * @return 新生成的xxCfg.java新类的ClassName
     */
    public static ClassName parserConfigureAnnotation(Filer filer) {

        List<ClassName> classNames = new ArrayList<>();
        //解析全局拦截器
        for (Element element : getAnnotationElements(GlobalInterceptor.class)) {

            if (!Validator.isBindingInWrongPackage(GlobalInterceptor.class, element)) {

                parseAnnotatedGlobalInterceptor(element, classNames);
            }
        }
        String packageName = null;
        String className = null;
        //解析Configure配置文件
        for (Element element : getAnnotationElements(Config.class)) {

            if (Validator.isElementAnnotationValid(Config.class, element)) {

                ConfigInfo configInfo = new ConfigInfo();
                TypeElement typeElement = (TypeElement) element;
                configInfo.interceptorClassName = classNames;
                //包名
                configInfo.packageName = ElementUtil.getPackageName(typeElement);
                packageName = configInfo.packageName;
                //类名
                configInfo.className = ElementUtil.getClassName(typeElement);
                className = configInfo.getClassName();
                //生成Configure类
                ConfigureGenerator.brewJava(configInfo, filer);
                break;
            }
        }
        if (packageName != null && className != null) {

            return ClassName.get(packageName, className);
        }
        return null;
    }

    /**
     * 解析全局拦截器
     *
     * @param element    element
     * @param classNames 缓存TypeName
     */
    private static void parseAnnotatedGlobalInterceptor(Element element, List<ClassName> classNames) {

        //检查注解对象类型
        if (!Validator.isClass(element.asType())) {

            MessageUtil.error(element, "@%s-annotated class incorrectly in Interface. (%s)", Grpc.class.getSimpleName(), element.getSimpleName());
            return;
        }
        if (!(element instanceof TypeElement)) {

            MessageUtil.warning(element, "@%s-annotated class not TypeElement. (%s)", Grpc.class.getSimpleName(), element.getSimpleName());
            return;
        }
        classNames.add(ClassName.get((TypeElement) element));
    }
}
