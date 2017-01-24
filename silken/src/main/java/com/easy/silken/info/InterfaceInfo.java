package com.easy.silken.info;

import com.easy.silken.Constants;
import com.easy.silken.InsertInterceptor;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.TypeElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HandGunBreak on 2015/11/6 - 21:01.
 * Mail: HandGunBreak@gmail.com
 * Copyright: 杭州医本健康科技有限公司(2014-2015)
 * Description: 类级解析信息
 */
public class InterfaceInfo implements InsertInterceptor {

    //类名
    public String className;
    //包名
    public String packageName;
    //类Element
    private TypeElement serviceClassElement;
    //类级别拦截器
    public List<ClassName> interceptorClassName;
    //方法信息
    private List<MethodInfo> mMethodInfo;

    public TypeName mStubTypeName;

    //serviceClass
    public TypeName mServiceTypeName;

    //返回带后缀的Rpc接口类名
    public String getClassName() {

        if (className == null || className.length() == 0) {

            return null;
        }
        return className + Constants.GRPC_CLASS_SUFFIX;
    }

    public void setClassName(String className) {

        this.className = className;
    }

    public TypeElement getServiceClassElement() {

        return serviceClassElement;
    }

    public void setServiceClassElement(TypeElement serviceClassElement) {

        this.serviceClassElement = serviceClassElement;
    }

    public List<MethodInfo> getMethodInfo() {

        return mMethodInfo;
    }

    public List<ClassName> getInterceptorClassName() {

        return interceptorClassName;
    }

    @Override
    public void insertInterceptor(int type, ClassName interceptorClazzName) {

        if (interceptorClassName == null) {

            interceptorClassName = new ArrayList<>();
        }
        interceptorClassName.add(interceptorClazzName);
    }

    /**
     * 添加方法级信息类
     *
     * @param methodInfo 方法级信息
     */
    public void addInvokeMethodInfo(MethodInfo methodInfo) {

        if (methodInfo == null) {

            return;
        }

        if (this.mMethodInfo == null) {

            this.mMethodInfo = new ArrayList<>();
        }
        this.mMethodInfo.add(methodInfo);
    }


}
