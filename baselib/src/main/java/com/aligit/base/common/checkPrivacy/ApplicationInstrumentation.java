package com.aligit.base.common.checkPrivacy;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;

/**
 * Created by gzp1124
 * Desc:
 */
public class ApplicationInstrumentation extends Instrumentation {

    private static final String TAG = "ApplicationInstrumentation";

    // ActivityThread中原始的对象, 保存起来
    Instrumentation mBase;
    private String checkName;


    public ApplicationInstrumentation(Instrumentation base,String checkName) {
        mBase = base;
        this.checkName = checkName;
    }

    @Override
    public Activity newActivity(ClassLoader cl, String className,
                                Intent intent)
            throws InstantiationException, IllegalAccessException,
            ClassNotFoundException {
        className = CheckApp.getApp().getActivityName(className,checkName);
        return mBase.newActivity(cl, className, intent);
    }

}



