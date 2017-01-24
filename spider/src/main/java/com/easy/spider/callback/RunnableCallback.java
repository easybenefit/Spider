package com.easy.spider.callback;

/**
 * Copyright: 杭州医本健康科技有限公司(2017)
 * Created by handgunbreak on 17/1/14.
 * Mail: handgunbreak@gmail.com
 * <p>
 * project: Phantom
 * Description:
 * Version: v:
 */
public interface RunnableCallback {

    void run(ServiceCallback<?> callback);

    void doInBackground(ServiceCallback<?> callback);

    void doInMainThread(ServiceCallback<?> callback);
}
