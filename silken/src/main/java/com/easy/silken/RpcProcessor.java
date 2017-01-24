package com.easy.silken;

import com.easy.spider.annotation.Config;
import com.easy.spider.annotation.GlobalInterceptor;
import com.easy.spider.annotation.Interceptor;
import com.easy.spider.annotation.Interceptors;
import com.easy.spider.annotation.Method;
import com.easy.spider.annotation.Grpc;
import com.easy.spider.annotation.Service;
import com.google.auto.service.AutoService;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by HandGunBreak on 2016/4/5 - 18:36.
 * Mail: handgunbreak@gmail.com
 * Copyright: 杭州医本健康科技有限公司(2015-2016)
 * Description: Processor类
 */
@SuppressWarnings("unused")
@AutoService(Processor.class)
public class RpcProcessor extends AbstractProcessor {

    private ProcessingEnvironment mProcessingEnvironment;

    /**
     * 初始化，系统调用
     *
     * @param processingEnvironment 编译期解析环境
     */
    @Override
    public void init(ProcessingEnvironment processingEnvironment) {

        super.init(processingEnvironment);
        mProcessingEnvironment = processingEnvironment;
    }

    /**
     * 编译期注解解析方法
     *
     * @param annotations 被注解元素集
     * @param roundEnv    env
     * @return 处理结果
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        RpcAnnotationParser.getInstance().parser(roundEnv, mProcessingEnvironment.getFiler(), mProcessingEnvironment.getTypeUtils(), mProcessingEnvironment.getElementUtils());
        return true;
    }

    /**
     * 定义支持注解类型
     *
     * @return 注解类型集
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {

        Set<String> annotationTypes;
        annotationTypes = new LinkedHashSet<>();
        annotationTypes.add(GlobalInterceptor.class.getCanonicalName());
        annotationTypes.add(Interceptors.class.getCanonicalName());
        annotationTypes.add(Interceptor.class.getCanonicalName());
        annotationTypes.add(Config.class.getCanonicalName());
        annotationTypes.add(Service.class.getCanonicalName());
        annotationTypes.add(Method.class.getCanonicalName());
        annotationTypes.add(Grpc.class.getCanonicalName());
        return annotationTypes;
    }

    /**
     * 支持注解类型编译版本号
     *
     * @return 版本号
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {

        return SourceVersion.latest();
    }

}
