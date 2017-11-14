package com.ap.base;

import android.app.Application;
import android.content.Context;

/**
 * 类描述：
 * 创建人：swallow.li
 * 创建时间：
 * Email: swallow.li@kemai.cn
 * 修改备注：
 */
public class BaseApplication extends Application {

    private static BaseApplication mInstance;
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mContext=getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }

    public static BaseApplication getAppContext() {
        return mInstance;
    }
}
