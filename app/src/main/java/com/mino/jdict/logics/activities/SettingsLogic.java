package com.mino.jdict.logics.activities;

import android.app.ActionBar;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.mino.jdict.JDictApplication;
import com.mino.jdict.R;
import com.mino.jdict.activities.ExampleDetailsActivity;
import com.mino.jdict.activities.ItemDetailsActivity;
import com.mino.jdict.activities.JLPTActivity;
import com.mino.jdict.activities.KanjiActivity;
import com.mino.jdict.activities.KanjiComponentDetailsActivity;
import com.mino.jdict.activities.KanjiDetailsActivity;
import com.mino.jdict.activities.MainActivity;
import com.mino.jdict.activities.NoteActivity;
import com.mino.jdict.activities.VocabularyActivity;
import com.mino.jdict.interfaces.IClearable;
import com.mino.jdict.logics.basic.Logic;

import utils.other.ExTextView;

/**
 * Created by Dominik on 9/21/2015.
 */
public class SettingsLogic extends Logic {

    private ActionBar mActionBar;
    private CheckBox mSaveSearchSettings, mKanaAsRomaji;

    public void initialize() {

        mSaveSearchSettings = (CheckBox) mActivity.findViewById(R.id.checkBoxSaveSearchSettings);
        mSaveSearchSettings.setChecked(JDictApplication.getInstance().getGlobalSetting("SearchSettingsGlobal").contentEquals("True"));

        mSaveSearchSettings.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                JDictApplication.getDatabaseHelper().getSetting().updateSetting("SearchSettingsGlobal", ((CheckBox)v).isChecked() ? "True" : "False");
            }
        });

        mKanaAsRomaji = (CheckBox) mActivity.findViewById(R.id.checkBoxKanaAsRomaji);
        mKanaAsRomaji.setChecked(JDictApplication.getInstance().getGlobalSetting("KanaAsRomaji").contentEquals("True"));

        mKanaAsRomaji.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                JDictApplication.getDatabaseHelper().getSetting().updateSetting("KanaAsRomaji", ((CheckBox)v).isChecked() ? "True" : "False");

                Logic.refreshActivity(NoteActivity.class, IClearable.ClearanceLevel.Hard);
                Logic.refreshActivity(ItemDetailsActivity.class, IClearable.ClearanceLevel.Hard);
                Logic.refreshActivity(KanjiDetailsActivity.class, IClearable.ClearanceLevel.Hard);
                Logic.refreshActivity(KanjiComponentDetailsActivity.class, IClearable.ClearanceLevel.Hard);
                Logic.refreshActivity(ExampleDetailsActivity.class, IClearable.ClearanceLevel.Hard);
                Logic.refreshActivity(MainActivity.class, IClearable.ClearanceLevel.Hard);
                Logic.refreshActivity(KanjiActivity.class, IClearable.ClearanceLevel.Hard);
                Logic.refreshActivity(VocabularyActivity.class, IClearable.ClearanceLevel.Hard);
                Logic.refreshActivity(JLPTActivity.class, IClearable.ClearanceLevel.Hard);
                Logic.refreshActivity(SchoolGradesLogic.class, IClearable.ClearanceLevel.Hard);
            }
        });
    }

    private void restoreActionBar() {
        mActionBar = mActivity.getActionBar();
        mActionBar.setTitle(mActivity.getString(R.string.title_section11));
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

        mActionBarHeader.setText(mActivity.getString(R.string.title_section11));
    }
}
