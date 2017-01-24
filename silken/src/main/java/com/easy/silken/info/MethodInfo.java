package com.easy.silken.info;


import com.easy.silken.InsertInterceptor;
import com.squareup.javapoet.ClassName;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by HandGunBreak on 2015/11/6 - 21:01.
 * Mail: HandGunBreak@gmail.com
 * Copyright: 杭州医本健康科技有限公司(2014-2015)
 * Description:
 */
@SuppressWarnings("unused")
public class MethodInfo implements InsertInterceptor {

    //类级别拦截器
    public List<ClassName> mClassInterceptorClassName;
    //方法级别拦截器
    public List<ClassName> mMethodInterceptorClassName;

    public Map<String, String> mCallbackParam;
    //参数列表
    private List<VariableElement> variableElements;
    //回调方法参数类 java.lang.String
    public ClassName mCallbackParamClass;
    //方法返回结果类型
    public TypeMirror mReturnTypeMirror;

    //回调参数类com.RpcServiceCallback<java.lang.String>
    public String mCallbackClass;
    //回调参数名
    public String mCallbackParamName;
    //接口定义的方法名
    public String mServiceMethodName;
    //Grpc自动生成的方法名
    public String mGrpcServiceMethodName;
    //方法参数名称
    public List<String> mMethodParamName;


    public void addVariableElement(VariableElement variableElement) {

        if (variableElement == null) {

            return;
        }
        if (variableElements == null) {

            variableElements = new ArrayList<>();
        }
        variableElements.add(variableElement);
    }

    public void addAllVariableElement(List<? extends VariableElement> variableElement) {

        if (variableElement == null) {

            return;
        }
        if (variableElements == null) {

            variableElements = new ArrayList<>();
        }
        variableElements.addAll(variableElement);
    }

    public List<VariableElement> getVariableElements() {

        return variableElements;
    }

    @Override
    public void insertInterceptor(int type, ClassName interceptorClazzName) {

        if (type == CLASS_INTERCEPTOR || type == METHOD_INTERCEPTOR) {

            if (type == CLASS_INTERCEPTOR) {

                if (mClassInterceptorClassName == null) {

                    mClassInterceptorClassName = new ArrayList<>();
                }
                mClassInterceptorClassName.add(interceptorClazzName);
            } else {

                if (mMethodInterceptorClassName == null) {

                    mMethodInterceptorClassName = new ArrayList<>();
                }
                mMethodInterceptorClassName.add(interceptorClazzName);
            }
        }
    }

    public List<ClassName> getClassInterceptorClassName() {

        return mClassInterceptorClassName;
    }
}
