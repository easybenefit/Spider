package com.easy.spider.callback;

/**
 * Copyright: 杭州医本健康科技有限公司(2017)
 * Created by handgunbreak on 17/1/13.
 * Mail: handgunbreak@gmail.com
 * <p>
 * project: Phantom
 * Description:Service接口定义
 * Version: v:1.0
 */
public interface ServiceCallback<T> {

    void onStart();

    void onProgress(Integer... progress);

    T onExecute();

    void onSuccess(T result);

    void onFailed(Exception e);
}
