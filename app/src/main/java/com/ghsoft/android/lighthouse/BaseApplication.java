package com.ghsoft.android.lighthouse;

import android.app.Application;

import com.tsengvn.typekit.Typekit;

public class BaseApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "BMJUA_ttf.ttf"));
//                .addCustom5(Typekit.createFromAsset(this, "Yache.ttf"))
//                .addCustom6(Typekit.createFromAsset(this, "Yache Bold.ttf"));
    }
}