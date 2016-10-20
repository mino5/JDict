package com.mino.jdict;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.google.android.vending.expansion.downloader.DownloaderClientMarshaller;

/**
 * Created by Dominik on 9/21/2015.
 */
public class MyReceiver extends android.content.BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            DownloaderClientMarshaller.startDownloadServiceIfRequired(context,
                    intent, MyDownloaderService.class);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
