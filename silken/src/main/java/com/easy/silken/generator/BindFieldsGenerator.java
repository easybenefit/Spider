package com.easy.silken.generator;

import com.easy.silken.info.FieldInfo;
import com.easy.silken.info.RpcFieldBind;
import com.easy.silken.util.ClassBuilderUtil;
import com.easy.spider.Bind;
import com.squareup.javapoet.*;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by HandGunBreak on 2016/4/13 - 14:22.
 * Mail: handgunbreak@gmail.com
 * Copyright: 杭州医本健康科技有限公司(2015-2016)
 * Description: 被@RpcService注解的元素所在类的新生成类的自动生成类
 */
public class BindFieldsGenerator {

    /**
     * 生成Bind方法
     *
     * @param fieldInfo fieldInfo
     * @param filer     filer
     * @return bind方法描述
     */
    private static MethodSpec createBindMethod(FieldInfo fieldInfo, Filer filer, String className) {

        MethodSpec.Builder result = MethodSpec.methodBuilder("bind")
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("\n@bind method was auto generated in .\n", className)
                .addParameter(TypeVariableName.get("T"), "object", Modifier.FINAL);

        for (RpcFieldBind fieldBind : fieldInfo.getRpcFieldBindMap().values()) {

            ClassName fieldBindClassName = ClassName.get(fieldInfo.packageName, fieldBind.getTargetTypeName());

            result.addStatement("object.$L = new $T(object)", fieldBind.name, fieldBindClassName);
        }
        return result.build();
    }

    /**
     * 生成解绑方法
     *
     * @param filer     Filer
     * @param className 所丰类类名
     * @return unbind方法描述
     */
    private static MethodSpec createUnbindMethod(FieldInfo fieldInfo, Filer filer, String className) {

        MethodSpec.Builder result = MethodSpec.methodBuilder("unbind")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TypeVariableName.get("T"), "object", Modifier.FINAL);

        result.addJavadoc("\n@bind method was auto generated in $S.class.\n", className);

        for (RpcFieldBind fieldBind : fieldInfo.getRpcFieldBindMap().values()) {

            result.addStatement("object.$L = null", fieldBind.name);
        }
        return result.build();
    }


    /**
     * 生成 @RpcService注解的Field所在类的新类($$SiLinkBinder)
     *
     * @param rpcAnnotatedFieldInfoMap
     * @param filer
     */
    public static void brewJava(Map<TypeElement, FieldInfo> rpcAnnotatedFieldInfoMap, Filer filer) {

        for (FieldInfo fieldInfo : rpcAnnotatedFieldInfoMap.values()) {

            //生成方法、方法修饰符、返回值
            List<MethodSpec> methodSpecs = Arrays.asList(
                    createBindMethod(fieldInfo, filer, fieldInfo.targetClassName),
                    createUnbindMethod(fieldInfo, filer, fieldInfo.targetClassName));

            //生成类
            TypeSpec classTypeSpec = TypeSpec.classBuilder(fieldInfo.targetClassName)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addJavadoc("@{$N was auto generated.}\n", fieldInfo.targetClassName)
                    .addMethods(methodSpecs)
                    .addSuperinterface(ParameterizedTypeName.get(ClassName.get(Bind.class), TypeVariableName.get("T")))
                    .addTypeVariable(TypeVariableName.get("T", ClassName.bestGuess(fieldInfo.className)))
                    .build();

            ClassBuilderUtil.build(filer, fieldInfo.packageName, classTypeSpec);
        }
    }

}
