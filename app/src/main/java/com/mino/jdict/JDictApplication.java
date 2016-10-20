package com.mino.jdict;

import android.content.Context;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import java.util.Map;

import utils.database.JMDictHelper;

/**
 * Created by Mino on 2014-08-12.
 */

/*

@ReportsCrashes(
        formKey = "",
        formUri = "https://acme.jdict.iriscouch.com/acra-jdict/_design/acra-storage/_update/report",
        reportType = org.acra.sender.HttpSender.Type.JSON,
        httpMethod = org.acra.sender.HttpSender.Method.PUT,
        formUriBasicAuthLogin = "mino",
        formUriBasicAuthPassword = "acratest"
        } */


@ReportsCrashes(formKey = "", // will not be used
        mailTo = "jdictcrashes@gmail.com",
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.crash_toast_text)
public class JDictApplication extends android.app.Application {

    private static Map<String, String> GlobalSettings = null;
    private static JDictApplication mSingleton = null;
    private static JMDictHelper mHelper = null;

    public static JMDictHelper getDatabaseHelper() {
        if (mHelper == null) {
            mHelper = new JMDictHelper(getContext());
            mHelper.getReadableDatabase();
        }

        return mHelper;
    }

    public String getGlobalSetting(String key) {

        if (GlobalSettings == null) {
            JMDictHelper helper = getDatabaseHelper();
            GlobalSettings = helper.getSetting().getGlobalSettings();
        }

        if (GlobalSettings.containsKey(key)) {
            return GlobalSettings.get(key);
        } else return null;
    }

    public void setGlobalSetting(String key, String value) {

        if (GlobalSettings == null) {
            getGlobalSetting(key);
        }

        GlobalSettings.put(key, value);
    }

    public static JDictApplication getInstance() {
        return mSingleton;
    }

    public static Context getContext() {
        return mSingleton.getApplicationContext();
    }

    @Override
    public void onCreate() {
        // The following line triggers the initialization of ACRA
        ACRA.init(this);
        super.onCreate();
        mSingleton = this;
    }

}
