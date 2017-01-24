package com.easy.silken.generator;

import com.easy.silken.info.ConfigInfo;
import com.easy.silken.util.ClassBuilderUtil;
import com.easy.silken.util.MethodBuilderUtil;
import com.easy.silken.util.VarArgsBuilder;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.Arrays;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;

import io.grpc.Channel;
import io.grpc.ClientInterceptors;
import io.grpc.okhttp.OkHttpChannelBuilder;

/**
 * Copyright: 杭州医本健康科技有限公司(2017)
 * Created by handgunbreak on 17/1/14.
 * Mail: handgunbreak@gmail.com
 * <p>
 * project: Phantom
 * Description: @Configure注解配置文件生成
 * Version: v:1.0
 */
public class ConfigureGenerator {

    /**
     * 生成inject方法
     *
     * @param configInfo cfg
     * @return MethodSpec
     */
    private static MethodSpec createInjectMethod(ConfigInfo configInfo) {

        //生成方法、方法修饰符、返回值
        MethodSpec.Builder methodSpecBuilder = MethodBuilderUtil.build(ConfigInfo.INJECT_METHOD_NAME, void.class, Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC);
        //添加hostName参数
        methodSpecBuilder.addParameter(TypeName.get(String.class), "hostName");
        //添加port参数
        methodSpecBuilder.addParameter(TypeName.get(int.class), "port");
        //添加Channel生成
        methodSpecBuilder.addStatement("$T channel = $T.forAddress(hostName, port).usePlaintext(true).build()", Channel.class, OkHttpChannelBuilder.class);
        //有全局拦截器定义
        if (configInfo.interceptorClassName != null && configInfo.interceptorClassName.size() > 0) {

            //生成带占位符语句
            String quota = MethodBuilderUtil.buildLooper("$T list = $T.asList(", configInfo.interceptorClassName.size(), "new $T()");

            //生成可变长参数
            Object[] classNames = new VarArgsBuilder<ClassName>()
                    .addArg(ClassName.get(List.class))
                    .addArg(ClassName.get(Arrays.class))
                    .addArgs(configInfo.interceptorClassName)
                    .build(ClassName.class);
            methodSpecBuilder.addStatement(quota, classNames);
            methodSpecBuilder.addStatement("channel = $T.intercept(channel, list)", ClientInterceptors.class);
        }
        methodSpecBuilder.addStatement("mChannel = channel");
        return methodSpecBuilder.build();
    }

    /**
     * 生成getChannel方法
     *
     * @param configInfo cfg
     * @return MethodSpec
     */
    private static MethodSpec createChannelGetMethod(ConfigInfo configInfo) {

        //生成方法、方法修饰符、返回值
        MethodSpec.Builder methodSpecBuilder = MethodBuilderUtil.build(ConfigInfo.CHANNEL_GET_METHOD_NAME, Channel.class, Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC);
        //添加hostName参数
        methodSpecBuilder.addStatement("return mChannel");
        return methodSpecBuilder.build();
    }

    /**
     * 生成.java类
     *
     * @param configInfo 配置
     * @param filer      filer
     */
    public static void brewJava(ConfigInfo configInfo, Filer filer) {

        //方法集
        List<MethodSpec> clazzMethodSpecs = Arrays.asList(createInjectMethod(configInfo), createChannelGetMethod(configInfo));

        //生成类
        TypeSpec classTypeSpec = TypeSpec.classBuilder(configInfo.getClassName())
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethods(clazzMethodSpecs)
                .addField(Channel.class, "mChannel", Modifier.PRIVATE, Modifier.STATIC)
                .build();

        ClassBuilderUtil.build(filer, configInfo.packageName, classTypeSpec);
    }

}
