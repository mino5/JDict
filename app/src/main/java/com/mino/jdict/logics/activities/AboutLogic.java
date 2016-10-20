package com.mino.jdict.logics.activities;

import android.app.ActionBar;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.mino.jdict.R;
import com.mino.jdict.logics.basic.Logic;

import utils.other.ExTextView;

/**
 * Created by Dominik on 9/21/2015.
 */
public class AboutLogic extends Logic {

    private ActionBar mActionBar;

    public void initialize() {

        TextView mVerText = (ExTextView) mActivity.findViewById(R.id.textViewVer);

        PackageInfo pinfo;
        try {
            pinfo = mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), 0);
            String versionName = pinfo.versionName;
            mVerText.setText("Ver: " + versionName + "-r." + pinfo.versionCode);
            //ET2.setText(versionNumber);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void restoreActionBar() {
        mActionBar = mActivity.getActionBar();
        mActionBar.setTitle(mActivity.getString(R.string.title_section9));
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
        mActionBar.setDisplayShowHomeEnabled(true);
        mActionBar.setCustomView(R.layout.simple_actionbar_layout);
        mActionBar.setDisplayShowCustomEnabled(true);
    }

    public void onCreateOptionsMenu(Menu menu) {
        restoreActionBar();

        final View actionBarCustomView = mActionBar.getCustomView();

        TextView mActionBarHeader =
                (TextView) actionBarCustomView.findViewById(R.id.actionBarHeader);

        mActionBarHeader.setText(mActivity.getString(R.string.title_section9));
    }
}
