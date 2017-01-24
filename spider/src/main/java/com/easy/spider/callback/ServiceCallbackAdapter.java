package com.easy.spider.callback;

/**
 * Created by handgunbreak on 16/12/27.
 * Mail: handgunbreak@gmail.com
 * Copyright: 杭州医本健康科技有限公司(2016)
 * project: EasyBenefit-Android-App
 * Version: v:0.1
 * Description: 服务callback适配器定义
 */
public abstract class ServiceCallbackAdapter<T> implements ServiceCallback<T> {

    private static ExceptionHandler mExceptionHandler;

    public static void setExceptionHandler(ExceptionHandler exceptionHandler) {

        ServiceCallbackAdapter.mExceptionHandler = exceptionHandler;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onProgress(Integer... progress) {

    }

    @Override
    public abstract T onExecute();

    @Override
    public abstract void onSuccess(T result);

    @Override
    public void onFailed(Exception e) {

        if (mExceptionHandler != null) {

            mExceptionHandler.handle(e);
        }
    }
}
