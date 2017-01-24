package com.easy.spider.task;

import com.easy.spider.callback.ServiceCallback;


/**
 * Copyright: 杭州医本健康科技有限公司(2017)
 * Created by handgunbreak on 17/1/13.
 * Mail: handgunbreak@gmail.com
 * <p>
 * project: Phantom
 * Description: 异步任务封装
 * Version: v:1.0
 */
@SuppressWarnings("unchecked")
public class ServiceTaskWrapper {

    private static Class<? extends ServiceInvokeTask> mRunnable;

    public static void setRunnable(Class<? extends ServiceInvokeTask> runnable) {

        mRunnable = runnable;
    }

    /**
     * 异步任务静态方法调用
     *
     * @param serviceCallback call入参
     */
    public static void invoke(ServiceCallback<?> serviceCallback) {

        try {

            ServiceInvokeTask serviceInvokeTask = mRunnable.newInstance();
            serviceInvokeTask.setServiceCallback(serviceCallback);
            serviceInvokeTask.execute();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
