package com.mino.jdict.logics.activities;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.mino.jdict.JDictApplication;
import com.mino.jdict.R;
import com.mino.jdict.activities.ExampleDetailsActivity;
import com.mino.jdict.activities.ItemDetailsActivity;
import com.mino.jdict.activities.KanjiComponentDetailsActivity;
import com.mino.jdict.activities.KanjiDetailsActivity;
import com.mino.jdict.activities.NoteActivity;
import com.mino.jdict.interfaces.IClearable;
import com.mino.jdict.logics.basic.Logic;
import com.mino.jdict.objects.activities.ANoteObject;
import com.mino.jdict.objects.activities.ExampleDetailObjects;
import com.mino.jdict.objects.activities.ItemDetailObject;
import com.mino.jdict.objects.activities.KanjiDetailObject;

import utils.database.entry.Vocabulary;
import utils.other.LinedEditText;

/**
 * Created by Dominik on 7/26/2015.
 */
public class AddNoteLogic extends Logic {

    private LinedEditText mEditText;
    private ActionBar mActionBar;

    public void initialize() {

        mEditText = (LinedEditText) mActivity.findViewById(R.id.editText);
        mEditText.setText(mBundle.getString("text"));

        InputMethodManager inputMethodManager = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }

        mEditText.requestFocus();
    }

    public void onCreateOptionsMenu(Menu menu) {

        restoreActionBar();
        final View actionBarCustomView = mActionBar.getCustomView();

        Button mDone = (Button) actionBarCustomView.findViewById(R.id.done);

        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideKeyboard();

                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", mEditText.getText().toString());
                mActivity.setResult(Activity.RESULT_OK, returnIntent);
                mActivity.finish();

            }
        });
    }

    private void hideKeyboard() {

        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(
                Context.INPUT_METHOD_SERVICE);

        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    private void restoreActionBar() {
        mActionBar = mActivity.getActionBar();
        mActionBar.setTitle(mActivity.getString(R.string.notepad));
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
        mActionBar.setDisplayShowHomeEnabled(true);
        mActionBar.setCustomView(R.layout.addnote_actionbar_layout);
        mActionBar.setDisplayShowCustomEnabled(true);
    }

    public static void noteResult(int requestCode, int resultCode, Intent data, int id, ANoteObject noteObject, String addNote, ArrayAdapter adapter, Vocabulary.ElementType type) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK && data != null) {

                String result = data.getStringExtra("result");

                if (noteObject != null) {

                    if (!result.isEmpty()) {
                        noteObject.Note.set(result);


                        if (noteObject.IsInDatabase.get()) {
                            JDictApplication.getDatabaseHelper().getVocabulary().updateNote(id, type, noteObject.Note.get());
                        } else {
                            JDictApplication.getDatabaseHelper().getVocabulary().insertNote(id, type, noteObject.Note.get());
                        }

                        noteObject.IsInDatabase.set(true);

                    } else {

                        if (noteObject.IsInDatabase.get()) {

                            JDictApplication.getDatabaseHelper().getVocabulary().deleteNote(id, type);
                        }

                        noteObject.Note.set(addNote);
                        noteObject.IsInDatabase.set(false);
                    }

                    Logic.refreshActivity(NoteActivity.class, IClearable.ClearanceLevel.Soft);

                    if (noteObject instanceof ItemDetailObject) {
                        Logic.refreshActivity(ItemDetailsActivity.class, IClearable.ClearanceLevel.Soft);
                    }

                    if (noteObject instanceof KanjiDetailObject) {
                        Logic.refreshActivity(KanjiDetailsActivity.class, IClearable.ClearanceLevel.Soft);
                        Logic.refreshActivity(KanjiComponentDetailsActivity.class, IClearable.ClearanceLevel.Soft);
                    }

                    if (noteObject instanceof ExampleDetailObjects) {
                        Logic.refreshActivity(ExampleDetailsActivity.class, IClearable.ClearanceLevel.Soft);
                    }
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }

            adapter.notifyDataSetChanged();
        }
    }

}
