package com.mino.jdict.logics.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mino.jdict.JDictApplication;
import com.mino.jdict.R;
import com.mino.jdict.activities.AddNoteActivity;
import com.mino.jdict.activities.ItemDetailsActivity;
import com.mino.jdict.activities.KanjiActivity;
import com.mino.jdict.activities.KanjiComponentDetailsActivity;
import com.mino.jdict.activities.MainActivity;
import com.mino.jdict.interfaces.IClearable;
import com.mino.jdict.interfaces.IKanjiObject;
import com.mino.jdict.logics.basic.Logic;
import com.mino.jdict.models.basic.SearchValues;
import com.mino.jdict.objects.activities.KanjiBaseObject;
import com.mino.jdict.objects.activities.KanjiDetailObject;
import com.mino.jdict.objects.activities.KanjiObject;
import com.mino.jdict.objects.activities.ListObject;
import com.mino.jdict.objects.activities.RecentObject;

import java.util.ArrayList;

import utils.database.entry.Vocabulary;
import utils.other.ClassObject;
import utils.other.StaticObjectContainer;
import utils.other.VocabularyPopup;

/**
 * Created by Dominik on 7/26/2015.
 */
public class KanjiDetailsLogic extends KanjiBaseLogic {

    public void initialize() {

        if (mBundle == null) {
            Bundle extras = mActivity.getIntent().getExtras();
            selectedCharacterSeq = extras.getInt("SelectedObjectEntSeq");
        }

        if (StaticObjectContainer.StaticObject instanceof KanjiObject) {

            mBaseObject = ((KanjiObject) StaticObjectContainer.StaticObject).Detail.get();
            StaticObjectContainer.StaticObject = null;
        } else if (mActivity.getIntent().getParcelableExtra("PAR") != null) {
            mBaseObject = ((IKanjiObject) mActivity.getIntent().getParcelableExtra("PAR")).getKanjiObject();
            selectedCharacterSeq = mBaseObject.CharacterID.get();
        } else {
            mBaseObject = null;
        }

        Logic.refreshActivity(KanjiActivity.class, IClearable.ClearanceLevel.Soft);

        if (mBaseObject != null || selectedCharacterSeq > 0) {
            fillDetails(true, true);
        }
    }

    @Override
    public void getMore() {

        getMoreCompounds();
    }

    public void onCreateOptionsMenu(Menu menu) {
        restoreActionBar();

        final View actionBarCustomView = mActionBar.getCustomView();

        ImageView mFavButton =
                (ImageView) actionBarCustomView.findViewById(R.id.favButton);

        TextView mTextViewHeader =
                (TextView) actionBarCustomView.findViewById(R.id.textViewHeader);

        mTextViewHeader.setText(mActivity.getString(R.string.kanji_header));

        mPopup = new VocabularyPopup(mActivity, mFavButton, Vocabulary.ElementType.Kanji);
        mPopup.checkIfInVocabList(selectedCharacterSeq);

        mFavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopup.showPopup(selectedCharacterSeq);
            }
        });

    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {

        selectedCharacterSeq = savedInstanceState.getInt("kanjiSeq");

        if (mListView == null || mListView.getCount() == 0) {
            fillDetails(true, true);
        }
    }

    public void onNewIntent(Intent intent) {

        mBaseObject = ((IKanjiObject) intent.getParcelableExtra("PAR")).getKanjiObject();
        selectedCharacterSeq = mBaseObject.CharacterID.get();

        fillDetails(true, true);
    }

    public void resume() {

        ClassObject mObj = StaticObjectContainer.GetClassObject(this.hashCode());

        if (mObj != null && mObj.getObject1() != null && mObj.getObject1() instanceof KanjiBaseObject) {
            mBaseObject = (KanjiBaseObject) mObj.getObject1();
            selectedCharacterSeq = mBaseObject.CharacterID.get();
        }

        if (mPopup != null) {
            mPopup.checkIfInVocabList(selectedCharacterSeq);
        }

        if (mToRefresh) {
            mToRefresh = false;

            if (mLoadCompounds == null || mLoadCompounds.getStatus() != AsyncTask.Status.RUNNING) {
                fillDetails(true, true);
            }
        }

        super.resume();
    }

    private void restoreActionBar() {
        mActionBar = mActivity.getActionBar();
        assert mActionBar != null;
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
        mActionBar.setDisplayShowHomeEnabled(true);
        mActionBar.setCustomView(R.layout.actionbar_favourites);
        mActionBar.setDisplayShowCustomEnabled(true);
    }

    @Override
    protected void setAdapter() {
        super.setAdapter();

        mListView.setClickable(true);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                Object o = mListView.getItemAtPosition(position);

                if (o instanceof KanjiDetailObject && ((KanjiDetailObject) o).getIsKanjiObject()) {

                    for (int i = 0; i < mAdapter.getCount(); i++) {
                        if (mAdapter.getItem(i).StrokeCount.get() > 0) {
                            String svg = mAdapter.getItem(i).StrokeSvg.get();

                            if (svg != null && svg.length() > 0) {

                                showStrokeOrder(svg);
                            }
                        }
                    }
                }

                if (o instanceof KanjiDetailObject && ((KanjiDetailObject) o).IsComponent.get() && ((KanjiDetailObject) o).getKanjiObject().CharacterID.get() > 0) {

                    Intent i = new Intent(mActivity.getBaseContext(), KanjiComponentDetailsActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    Bundle mBundle = new Bundle();
                    mBundle.putParcelable("PAR", ((KanjiDetailObject) o));
                    i.putExtras(mBundle);
                    mActivity.startActivity(i);
                }

                if (o instanceof KanjiDetailObject && ((KanjiDetailObject) o).IsNote.get()) {

                    noteObject = ((KanjiDetailObject) o);
                    Intent i = new Intent(mActivity.getBaseContext(), AddNoteActivity.class);
                    i.putExtra("text", noteObject.Note.get());

                    i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    mActivity.startActivityForResult(i, 1);
                }

                if (o instanceof KanjiDetailObject && ((KanjiDetailObject) o).StrokeCount.get() > 0) {

                    String svg = ((KanjiDetailObject) o).StrokeSvg.get();

                    if (svg != null && svg.length() > 0) {

                        showStrokeOrder(svg);
                    }
                }

                if (o instanceof KanjiDetailObject && ((KanjiDetailObject) o).ListObject.get() != null) {

                    ListObject listObj = (ListObject) ((KanjiDetailObject) o).ListObject.get();

                    RecentObject recentObj = new RecentObject.RecentObjectFactory().factory();

                    SearchValues values = new SearchValues();
                    ArrayList<Boolean> cbValues = new ArrayList<Boolean>();
                    cbValues.add(false);
                    values.setCheckBoxValues(cbValues);

                    recentObj.init(listObj.getFirst(), listObj.getSecond(), listObj.ID.get(), "", values);


                    if (!recentObj.getPureKeb().isEmpty() || !recentObj.getPureReb().isEmpty()) {
                        JDictApplication.getDatabaseHelper().getWord().insertRecentWords(recentObj);
                        Logic.refreshActivity(MainActivity.class, IClearable.ClearanceLevel.Soft);

                        Intent i = new Intent(mActivity.getBaseContext(), ItemDetailsActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        i.putExtra("SelectedObjectEntSeq", listObj.ID.get());
                        StaticObjectContainer.StaticObject = null;
                        mActivity.startActivity(i);
                    }
                }
            }
        });
    }
}
