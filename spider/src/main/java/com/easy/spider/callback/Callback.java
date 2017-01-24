package com.easy.spider.callback;

/**
 * Copyright: 杭州医本健康科技有限公司(2017)
 * Created by handgunbreak on 17/1/13.
 * Mail: handgunbreak@gmail.com
 * <p>
 * project: Phantom
 * Description:回调接口
 * Version: v:1.0
 */
public interface Callback<T> {

    void onSuccess(T result);

    void onFailed(Exception exception);
}
