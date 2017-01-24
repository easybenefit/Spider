package com.easy.silken.parser;

import com.easy.silken.InsertInterceptor;
import com.easy.silken.generator.InterfaceGenerator;
import com.easy.silken.info.InterfaceInfo;
import com.easy.silken.info.MethodInfo;
import com.easy.silken.util.AnnotationElementUtil;
import com.easy.silken.util.ElementUtil;
import com.easy.silken.util.TypeUtil;
import com.easy.silken.util.Validator;
import com.easy.spider.annotation.Grpc;
import com.easy.spider.annotation.Interceptor;
import com.easy.spider.annotation.Interceptors;
import com.easy.spider.annotation.Method;
import com.squareup.javapoet.ClassName;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.MirroredTypeException;

/**
 * Copyright: 杭州医本健康科技有限公司(2017)
 * Created by handgunbreak on 17/1/14.
 * Mail: handgunbreak@gmail.com
 * <p>
 * project: Phantom
 * Description:
 * Version: v:
 */
public class InterfaceParser {

    public static void interfaceParser(Filer filer, ClassName cfgClassName) {

        //缓存RpcAnnotatedInterfaceInfo对象
        Map<Element, InterfaceInfo> rpcAnnotatedInterfaceInfoMap = new LinkedHashMap<>();
        //解析@Rpc注解的接口类
        for (Element element : AnnotationElementUtil.getAnnotationElements(Grpc.class)) {

            if (!Validator.isBindingInWrongPackage(Grpc.class, element)) {

                parseServiceAnnotatedInterface(rpcAnnotatedInterfaceInfoMap, element);
            }
        }
        //对@Rpc注解的接口类生成对应的_Rpc类
        for (InterfaceInfo interfaceInfo : rpcAnnotatedInterfaceInfoMap.values()) {

            InterfaceGenerator.brewJava(cfgClassName, interfaceInfo, filer);
        }
    }

    /**
     * Rpc接口注解解析
     *
     * @param rpcServiceClassInfoMap 被@Rpc注解的接口类缓存Map
     * @param element                被@Rpc注解的element
     */
    private static void parseServiceAnnotatedInterface(Map<Element, InterfaceInfo> rpcServiceClassInfoMap, Element element) {

        //检查注解对象类型
        if (Validator.isElementAnnotationValid(Grpc.class, element)) {

            TypeElement typeElement = (TypeElement) element;
            //Service类信息
            InterfaceInfo interfaceInfo = new InterfaceInfo();
            Grpc service = element.getAnnotation(Grpc.class);
            try {

                //取service类
                service.service();
            } catch (MirroredTypeException mirroredTypeException) {

                interfaceInfo.mServiceTypeName = ElementUtil.getClassName(mirroredTypeException);
            }
            try {

                //取stub类
                service.stub();
            } catch (MirroredTypeException mirroredTypeException) {

                interfaceInfo.mStubTypeName = ElementUtil.getClassName(mirroredTypeException);
            }

            //缓存
            rpcServiceClassInfoMap.put(element, interfaceInfo);
            //包名
            interfaceInfo.packageName = ElementUtil.getPackageName(typeElement);
            //类名
            interfaceInfo.setClassName(ElementUtil.getClassName(typeElement));
            //被GService注解的Element(接口类)
            interfaceInfo.setServiceClassElement(typeElement);
            //解析Rpc接口类Interceptors拦截器注解
            Interceptors interfaceInterceptors = element.getAnnotation(Interceptors.class);
            //抽取RpcInterceptors注解的接口类的拦截器
            if (interfaceInterceptors != null) {

                extractInterceptorsClass(InsertInterceptor.CLASS_INTERCEPTOR, interfaceInterceptors, interfaceInfo);
            }
            //抽取RpcInterceptor注解的接口类的拦截器
            Interceptor interfaceInterceptor = element.getAnnotation(Interceptor.class);
            if (interfaceInterceptor != null) {

                extractInterceptorClass(InsertInterceptor.CLASS_INTERCEPTOR, interfaceInterceptor, interfaceInfo);
            }
            //Rpc接口中的方法Elements
            List<? extends Element> enclosedElements = element.getEnclosedElements();

            //遍历每个接口方法
            for (Element enclosedElement : enclosedElements) {

                if (!(enclosedElement instanceof ExecutableElement) || enclosedElement.getKind() != ElementKind.METHOD) {

                    throw new IllegalStateException(String.format("@%s annotation must be on a method.", enclosedElement.getSimpleName()));
                }
                //方法对象
                MethodInfo methodInfo = new MethodInfo();
                //接口定义方法Element
                ExecutableElement executableElement = (ExecutableElement) enclosedElement;

                //GMethod方法注解解析
                extractServiceMethodAction(executableElement, methodInfo);

                //方法级单拦截器解析
                Interceptor interceptor = executableElement.getAnnotation(Interceptor.class);
                if (interceptor != null) {

                    extractInterceptorClass(InsertInterceptor.METHOD_INTERCEPTOR, interceptor, methodInfo);
                }
                //方法级多拦截器注解类解析
                Interceptors rpcInterceptors = executableElement.getAnnotation(Interceptors.class);
                if (rpcInterceptors != null) {

                    extractInterceptorsClass(InsertInterceptor.METHOD_INTERCEPTOR, rpcInterceptors, methodInfo);
                }
                //方法参数解析
                extraServiceMethodExecutableParam(executableElement, methodInfo);

                //缓存方法对象
                interfaceInfo.addInvokeMethodInfo(methodInfo);
            }
        }
    }


    /**
     * 抽取方法参数
     *
     * @param executableElement 方法Element
     * @param methodInfo        缓存对象
     */
    private static void extraServiceMethodExecutableParam(ExecutableElement executableElement, MethodInfo methodInfo) {

        List<String> params;

        methodInfo.mServiceMethodName = executableElement.getSimpleName().toString();
        //参数
        List<? extends VariableElement> methodParameters = executableElement.getParameters();
        if (methodParameters != null && methodParameters.size() > 0) {

            params = new ArrayList<>(methodParameters.size());
            //参数
            methodInfo.addAllVariableElement(methodParameters);

            //提取callback回调方法的方法名及泛型类型,同时提取参数名
            for (VariableElement variableElement : methodParameters) {

                boolean result = TypeUtil.implementsClass(variableElement);
                if (result) {

                    methodInfo.mCallbackParamName = variableElement.toString();
                    String genericClassName = ElementUtil.doubleGeneric(variableElement.asType());
                    if (genericClassName != null) {

                        methodInfo.mReturnTypeMirror = ElementUtil.getTypeMirror(genericClassName);
                    }
                    continue;
                }
                params.add(variableElement.toString());
            }
            methodInfo.mMethodParamName = params;
        }
    }


    /**
     * Rpc Action 解析
     *
     * @param executableElement element
     * @param methodInfo        存储
     */
    private static void extractServiceMethodAction(ExecutableElement executableElement, MethodInfo methodInfo) {

        //方法级注解Element
        Method method = executableElement.getAnnotation(Method.class);

        //Spider自定义方法名
        methodInfo.mServiceMethodName = executableElement.getSimpleName().toString();
        if (method != null) {

            //grpc自定义方法名
            methodInfo.mGrpcServiceMethodName = method.methodName();
            if (methodInfo.mGrpcServiceMethodName.equals("")) {

                methodInfo.mGrpcServiceMethodName = methodInfo.mServiceMethodName;
            }
        } else {

            throw new IllegalStateException(String.format("@%s method must be annotated by @Method.", methodInfo.mServiceMethodName));
        }
    }

    /**
     * 方法级拦截器Interceptor类解析
     *
     * @param interceptor       拦截器注解
     * @param insertInterceptor append 接口
     */
    private static void extractInterceptorClass(int type, Interceptor interceptor, InsertInterceptor insertInterceptor) {

        try {

            interceptor.value();
        } catch (MirroredTypeException mirroredTypeException) {

            insertInterceptor.insertInterceptor(type, ElementUtil.getClassName(mirroredTypeException));
        }
    }

    /**
     * 方法级拦截器Interceptors注解类解析
     *
     * @param insertInterceptor invokeMethodInfo
     */
    private static void extractInterceptorsClass(int type, Interceptors interceptors, InsertInterceptor insertInterceptor) {

        Interceptor[] rpcInterceptors = interceptors.value();
        if (rpcInterceptors.length > 0) {

            for (Interceptor subRpcInterceptor : rpcInterceptors) {

                extractInterceptorClass(type, subRpcInterceptor, insertInterceptor);
            }
        }
    }

}
