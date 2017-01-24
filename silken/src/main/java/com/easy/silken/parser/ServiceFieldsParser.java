package com.easy.silken.parser;

import com.easy.silken.generator.BindFieldsGenerator;
import com.easy.silken.info.FieldInfo;
import com.easy.silken.info.RpcFieldBind;
import com.easy.silken.util.ElementUtil;
import com.easy.silken.util.MessageUtil;
import com.easy.silken.util.Validator;
import com.easy.spider.annotation.Service;
import com.squareup.javapoet.TypeName;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import static com.easy.silken.util.AnnotationElementUtil.getAnnotationElements;

/**
 * Copyright: 杭州医本健康科技有限公司(2017)
 * Created by handgunbreak on 17/1/14.
 * Mail: handgunbreak@gmail.com
 * <p>
 * project: Phantom
 * Description:
 * Version: v:
 */
public class ServiceFieldsParser {

    public static void parseRpcAnnotatedField(Filer filer) {

        //缓存被@GService注解的对象
        Map<TypeElement, FieldInfo> rpcAnnotatedFieldInfoMap = new HashMap<>();
        Set<? extends Element> restServices = getAnnotationElements(Service.class);
        for (Element element : restServices) {

            parseRpcAnnotatedField(rpcAnnotatedFieldInfoMap, element);
        }
        //生成被@RpcService注解的属性所在类新类(_SiLinkBinder)
        BindFieldsGenerator.brewJava(rpcAnnotatedFieldInfoMap, filer);
    }

    /**
     * 解析被@RpcService注解的Field Element
     *
     * @param rpcAnnotatedFieldInfoMap 缓存被@RpcService注解的 Element类信息
     * @param element                  Element
     */
    private static void parseRpcAnnotatedField(Map<TypeElement, FieldInfo> rpcAnnotatedFieldInfoMap, Element element) {

        if (Validator.isInaccessibleViaGeneratedCode(Service.class, element)) {

            return;
        }
        //Field所指类类型
        TypeMirror fieldTypeMirror = element.asType();
        //Field变量名
        String fieldVariableName = element.getSimpleName().toString();

        if (!Validator.isInterface(fieldTypeMirror)) {

            MessageUtil.error(element, "@%s Type which annotated by %s should be Interface type.", fieldVariableName, Service.class);
        }
        //TypeName Field所指类的TypeName
        TypeName fieldClassTypeName = TypeName.get(fieldTypeMirror);
        //Field所在类的TypeElement(所在类)
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
        //生成RpcFieldBind对象
        RpcFieldBind rpcFieldBind = new RpcFieldBind(fieldVariableName, fieldClassTypeName);
        //检查是否有缓存
        FieldInfo fieldInfo = rpcAnnotatedFieldInfoMap.get(enclosingElement);
        if (fieldInfo == null) {

            String packageName = ElementUtil.getPackageName(enclosingElement);
            String className = ElementUtil.getClassName(enclosingElement);
            fieldInfo = new FieldInfo(className, packageName, className);
        }
        fieldInfo.addRpcFieldBind(rpcFieldBind);
        rpcAnnotatedFieldInfoMap.put(enclosingElement, fieldInfo);
    }
}
