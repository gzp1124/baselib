package com.aligit.base.common.checkPrivacy;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.multidex.MultiDexApplication;

import com.aligit.base.Settings;

import java.util.List;

/**
 * Created by gzp1124
 * Desc:检查用户是否给与权限的application
 */
public abstract class CheckApp extends MultiDexApplication {

    /**
     * 用户是否同意隐私协议
     */
    private static final String KEY_USER_AGREE = CheckApp.class.getName() + "_key_user_agree";
    private static final String KEY_CHECK_ACTIVITY = "com.gzp1124.check.activity";
    private static final String SAVE_KEY = "save_p_key";

    private boolean userAgree;

    protected static CheckApp app;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        if (!Settings.INSTANCE.getUseSplashCheckPrivacy()){
            return;
        }
        userAgree = getSharedPreferences(SAVE_KEY, Context.MODE_PRIVATE).getBoolean(getUserAgreeKey(this),false);
        getCheckActivityName(base);
        if (!userAgree) {
            //只有在用户不同意的情况下才hook ，避免性能损失
            try {
                HookUtil.attachContext(checkActivityName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected String getUserAgreeKey(Context base) {
        if (checkForEachVersion()) {
            try {
                long longVersionCode = base.getPackageManager().getPackageInfo(base.getPackageName(), 0).versionCode;
                return KEY_USER_AGREE + "_version_" + longVersionCode;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return KEY_USER_AGREE;

    }

    /**
     * 是否每个版本都检查是否拥有用户隐私权限
     *
     * @return
     */
    protected boolean checkForEachVersion() {
        return true;
    }

    private static boolean initSDK = false;//是否已经初始化了SDK

    String checkActivityName = null;

    private void getCheckActivityName(Context base) {
        mPackageManager = base.getPackageManager();
        try {
            ApplicationInfo appInfo = mPackageManager.getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            checkActivityName = appInfo.metaData.getString(KEY_CHECK_ACTIVITY);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getActivityName(String name,String checkActivityName) {
        if (isUserAgree()) {
            return name;
        } else {
            setRealFirstActivityName(name);
            return checkActivityName;
        }
    }

    @Override
    public final void onCreate() {
        super.onCreate();
        if (!isRunOnMainProcess()) {
            return;
        }
        app = this;
        //初始化那些和隐私无关的SDK
        initSafeSDK();

        if (!Settings.INSTANCE.getUseSplashCheckPrivacy()){
            initSDK = true;
            HookUtil.initProvider(this);
            initSDK();
        }else if (userAgree && !initSDK) {
            initSDK = true;
            HookUtil.initProvider(this);
            initSDK();
        }
    }


    public static CheckApp getApp() {
        return app;
    }


    /**
     * 初始化那些和用户隐私无关的SDK
     * 如果无法区分，建议只使用initSDK一个方法
     */
    protected void initSafeSDK() {

    }


    /**
     * 判断用户是否同意
     *
     * @return
     */
    public boolean isUserAgree() {
        return userAgree;
    }


    static PackageManager mPackageManager;


    private static String realFirstActivityName = null;

    private static void setRealFirstActivityName(String realFirstActivityName) {
        CheckApp.realFirstActivityName = realFirstActivityName;
    }

    public void agree(Activity activity, boolean gotoFirstActivity, Bundle extras) {
        getSharedPreferences(SAVE_KEY, Context.MODE_PRIVATE).edit().putBoolean(getUserAgreeKey(this), true).apply();
        userAgree = true;

        if (!initSDK) {
            initSDK = true;
            HookUtil.initProvider(this);
            initSDK();
        }

        //启动真正的启动页
        if (!gotoFirstActivity) {
            //已经是同一个界面了，不需要自动打开
            return;
        }
        try {
            Intent intent = new Intent(activity, Class.forName(realFirstActivityName));
            if (extras != null) {
                intent.putExtras(extras);//也许是从网页中调起app，这时候extras中含有打开特定新闻的参数。需要传递给真正的启动页
            }
            activity.startActivity(intent);
            activity.finish();//关闭当前页面
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


    /**
     * 子类重写用于初始化SDK等相关工作
     */
    abstract protected void initSDK();

    /**
     * 判断是否在主进程中，一些SDK中的PushServer可能运行在其他进程中。
     * 也就会造成Application初始化两次,而只有在主进程中才需要初始化。
     * * @return
     */
    public boolean isRunOnMainProcess() {
        ActivityManager am = ((ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = this.getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }


}


