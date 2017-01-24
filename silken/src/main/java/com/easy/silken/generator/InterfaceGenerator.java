package com.easy.silken.generator;

import com.easy.silken.info.InterfaceInfo;
import com.easy.silken.info.MethodInfo;
import com.easy.silken.util.ClassBuilderUtil;
import com.easy.silken.util.MethodBuilderUtil;
import com.easy.silken.util.VarArgsBuilder;
import com.easy.spider.callback.ServiceCallbackAdapter;
import com.easy.spider.task.ServiceTaskWrapper;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.VariableElement;

import io.grpc.Channel;
import io.grpc.ClientInterceptors;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;

/**
 * Created by HandGunBreak on 2016/4/13 - 14:22.
 * Mail: handgunbreak@gmail.com
 * Copyright: 杭州医本健康科技有限公司(2015-2016)
 * Description:
 */
public class InterfaceGenerator {

    public static void brewJava(ClassName className, InterfaceInfo interfaceInfo, Filer filer) {

        if (interfaceInfo != null && interfaceInfo.getMethodInfo() != null && interfaceInfo.getMethodInfo().size() > 0) {

            //方法集
            List<MethodSpec> clazzMethodSpecs = new ArrayList<>();

            //循环生成接口定义的方法定义
            for (MethodInfo methodInfo : interfaceInfo.getMethodInfo()) {

                buildMethod(interfaceInfo, clazzMethodSpecs, methodInfo);
            }

            //添加构造方法
            MethodSpec.Builder builder = MethodSpec.constructorBuilder()
                    .addModifiers(PUBLIC)
                    .addParameter(Object.class, "object")
                    .addStatement("this.$N = $N", "mObject", "object")
                    .addStatement("$T channel = $T.getChannel()", ClassName.get(Channel.class), className);

            //存在类拦截器
            if (interfaceInfo.getInterceptorClassName() != null && interfaceInfo.getInterceptorClassName().size() > 0) {

                int size = interfaceInfo.getInterceptorClassName().size();
                String quota = MethodBuilderUtil.buildLooper("channel = $T.intercept(channel, ", size, "new $T()");

                Object[] classNames = new VarArgsBuilder<ClassName>()
                        .addArg(ClassName.get(ClientInterceptors.class))
                        .addArgs(interfaceInfo.interceptorClassName)
                        .build(ClassName.class);
                builder.addStatement(quota, classNames);
            }
            builder.addStatement("mServiceBlockingStub = $T.newBlockingStub(channel)", interfaceInfo.mServiceTypeName);
            builder.addStatement("mChannel = channel");
            MethodSpec constructor = builder.build();

            //生成类
            TypeSpec classTypeSpec = TypeSpec.classBuilder(interfaceInfo.getClassName())
                    .addModifiers(PUBLIC, FINAL)
                    .addMethods(clazzMethodSpecs)
                    .addMethod(constructor)
                    .addField(Channel.class, "mChannel", PRIVATE)
                    .addField(interfaceInfo.mStubTypeName, "mServiceBlockingStub", PRIVATE)
                    .addField(Object.class, "mObject", PRIVATE, FINAL)
                    .addSuperinterface(ClassName.get(interfaceInfo.packageName, interfaceInfo.className))
                    .build();

            ClassBuilderUtil.build(filer, interfaceInfo.packageName, classTypeSpec);
        }
    }

    /**
     * 构建由@GService注解的类当中的接口方法
     *
     * @param interfaceInfo    接口定义信息
     * @param clazzMethodSpecs 当前类方法定义集
     * @param methodInfo       方法定义信息
     */
    private static void buildMethod(InterfaceInfo interfaceInfo, List<MethodSpec> clazzMethodSpecs, MethodInfo methodInfo) {

        //生成方法、方法修饰符、返回值
        MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder(methodInfo.mServiceMethodName)
                .addModifiers(PUBLIC, FINAL)
                .addAnnotation(Override.class)
                .returns(void.class);

        //添加方法参数
        for (VariableElement variableElement : methodInfo.getVariableElements()) {

            String variableElementName = variableElement.toString();
            methodSpecBuilder.addParameter(TypeName.get(variableElement.asType()), variableElementName, FINAL);
        }
        //添加Stub变量
        methodSpecBuilder.addStatement("$T serviceBlockingStub = mServiceBlockingStub", interfaceInfo.mStubTypeName);
        //添加方法级拦截器
        if (methodInfo.mMethodInterceptorClassName != null && methodInfo.mMethodInterceptorClassName.size() > 0) {

            String quota = MethodBuilderUtil.buildLooper("$T channel = $T.intercept(mChannel, ", methodInfo.mMethodInterceptorClassName.size(), "new $T()");

            Object[] classNames = new VarArgsBuilder<ClassName>()
                    .addArg(ClassName.get(Channel.class))
                    .addArg(ClassName.get(ClientInterceptors.class))
                    .addArgs(methodInfo.mMethodInterceptorClassName)
                    .build(ClassName.class);
            //添加拦截器statement
            methodSpecBuilder.addStatement(quota, classNames);
            //新新成stub;
            methodSpecBuilder.addStatement("serviceBlockingStub = $T.newBlockingStub(channel)", interfaceInfo.mServiceTypeName);
        }
        methodSpecBuilder.addStatement("final $T blockingStub = serviceBlockingStub", interfaceInfo.mStubTypeName);
        //添加异步任务调用
        methodSpecBuilder.addStatement("$T.invoke($L)", ClassName.get(ServiceTaskWrapper.class), buildInnerClass(methodInfo));
        //添加接口类定义方法
        clazzMethodSpecs.add(methodSpecBuilder.build());
    }


    /**
     * 构建 ServiceCallbackAdapter回调内部类
     *
     * @param methodInfo methodInfo
     * @return TypeSpec
     */
    private static TypeSpec buildInnerClass(MethodInfo methodInfo) {

        TypeSpec.Builder builder = TypeSpec.anonymousClassBuilder("");
        //生成onExecute方法
        TypeName returnTypeName = TypeName.get(methodInfo.mReturnTypeMirror);

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("onExecute")
                .addAnnotation(Override.class)
                .addModifiers(PUBLIC)
                .returns(returnTypeName);
        List<String> paramNames = methodInfo.mMethodParamName;
        if (paramNames != null && paramNames.size() > 0) {

            //生成带占位符的语句
            String quota = MethodBuilderUtil.buildLooper("return blockingStub.$N(", paramNames.size(), "$N");

            //生成可变长参数
            Object[] classNames = new VarArgsBuilder<String>()
                    .addArg(methodInfo.mGrpcServiceMethodName)
                    .addArgs(paramNames)
                    .build(String.class);
            methodBuilder.addStatement(quota, classNames);
        }
        builder.addSuperinterface(ParameterizedTypeName.get(ClassName.get(ServiceCallbackAdapter.class), returnTypeName))
                .addMethod(methodBuilder.build());

        //生成onSuccess方法
        builder.addMethod(MethodSpec.methodBuilder("onSuccess")
                .addAnnotation(Override.class)
                .addParameter(returnTypeName, "result")
                .addModifiers(PUBLIC)
                .addCode(""
                        + "if($N !=null){\n"
                        + "   \n$N.onSuccess(result);\n"
                        + "}\n", methodInfo.mCallbackParamName, methodInfo.mCallbackParamName)
                .build());

        //生成onFailed方法
        builder.addMethod(MethodSpec.methodBuilder("onFailed")
                .addAnnotation(Override.class)
                .addParameter(TypeName.get(Exception.class), "exception")
                .addModifiers(PUBLIC)
                .addCode("\n"
                        + " super.onFailed(exception);\n"
                        + " if($N !=null){\n"
                        + "   \n$N.onFailed(exception);\n"
                        + "}\n", methodInfo.mCallbackParamName, methodInfo.mCallbackParamName)
                .build());

        return builder.build();
    }

}
