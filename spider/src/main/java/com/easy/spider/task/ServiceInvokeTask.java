package com.easy.spider.task;

import com.easy.spider.callback.ServiceCallback;

import java.net.PortUnreachableException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Copyright: 杭州医本健康科技有限公司(2017)
 * Created by handgunbreak on 17/1/12.
 * Mail: handgunbreak@gmail.com
 * <p>
 * project: Phantom
 * Description:异步任务封装
 * Version: v:1.0
 */
public class ServiceInvokeTask<T> {

    //线程池
    private static ExecutorService mExecutors;
    /**
     * Callback
     */
    private ServiceCallback<T> mServiceCallback;


    public ServiceInvokeTask() {

        if (mExecutors == null) {

            mExecutors = Executors.newCachedThreadPool();
        }
    }

    /**
     * 构造方法
     *
     * @param serviceCallback callback
     */
    public ServiceInvokeTask(ServiceCallback<T> serviceCallback) {

        if (serviceCallback == null) {

            throw new IllegalArgumentException("ServiceCallback cannot be null, please buildInterceptor it.");
        }
        mServiceCallback = serviceCallback;
        if (mExecutors == null) {

            mExecutors = Executors.newCachedThreadPool();
        }
    }

    void setServiceCallback(ServiceCallback<T> serviceCallback) {

        if (serviceCallback == null) {

            throw new IllegalArgumentException("ServiceCallback cannot be null, please buildInterceptor it.");
        }
        mServiceCallback = serviceCallback;
    }

    /**
     * 执行任务
     */
    void execute() {

        if (mServiceCallback != null) {

            mServiceCallback.onStart();

            mExecutors.execute(new Runnable() {
                @Override
                public void run() {

                    Runnable runnable;
                    try {

                        final T result = mServiceCallback.onExecute();
                        runOnUIThread(new Runnable() {
                            @Override
                            public void run() {
                                onPostExecute(result);
                            }
                        });
                    } catch (final Exception e) {

                        e.printStackTrace();
                        runOnUIThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mServiceCallback != null) {

                                    mServiceCallback.onFailed(e);
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    protected void runOnUIThread(Runnable runnable) {

    }

    /**
     * 主线程运行,返回结果
     *
     * @param result 结果
     */
    private void onPostExecute(T result) {

        if (result != null && result instanceof Exception) {

            mServiceCallback.onFailed((Exception) result);
        } else {

            try {

                mServiceCallback.onSuccess(result);
            } catch (Exception e) {

                mServiceCallback.onFailed(e);
            }
        }
    }

}

