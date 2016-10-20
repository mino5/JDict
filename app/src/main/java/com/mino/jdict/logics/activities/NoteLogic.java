package com.mino.jdict.logics.activities;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.mino.jdict.JDictApplication;
import com.mino.jdict.R;
import com.mino.jdict.activities.ExampleDetailsActivity;
import com.mino.jdict.activities.ItemDetailsActivity;
import com.mino.jdict.activities.KanjiDetailsActivity;
import com.mino.jdict.adapters.NoteAdapter;
import com.mino.jdict.interfaces.IClearable;
import com.mino.jdict.logics.basic.Logic;
import com.mino.jdict.objects.activities.ExampleObject;
import com.mino.jdict.objects.activities.KanjiBaseObject;
import com.mino.jdict.objects.activities.ListObject;
import com.mino.jdict.objects.activities.NoteObject;

import java.util.ArrayList;

import utils.database.JMDictHelper;
import utils.other.StaticObjectContainer;

/**
 * Created by Dominik on 7/24/2015.
 */
public class NoteLogic extends Logic implements IClearable {

    private static String lastDate = null;
    private int mCurrentOffset = 0;
    private int allCount = 0;
    private ArrayList<NoteObject> objectList = new ArrayList<NoteObject>();
    private NoteAdapter mAdapter;
    private ListView mListView;
    private boolean mToRefresh = false;
    private LoadNotes loadNotes;
    private ActionBar mActionBar;

    public void initialize() {
        objectList.clear();

        setAdapter();
        (loadNotes = new LoadNotes(mActivity)).execute();
    }

    public void getMoreNotes() {

        mCurrentOffset += 10;
        (loadNotes = new LoadNotes(mActivity)).execute();
    }

    public void clear(IClearable.ClearanceLevel inClearanceLevel) {

        mCurrentOffset = 0;
        allCount = 0;
        mAdapter.clear();
        mAdapter.notifyDataSetChanged();
        mToRefresh = true;
    }
    public void resume() {

        if (mToRefresh) {
            mToRefresh = false;

            if (loadNotes == null || loadNotes.getStatus() != AsyncTask.Status.RUNNING) {
                (loadNotes = new LoadNotes(mActivity)).execute();
            }
        }

        super.resume();
    }

    private void restoreActionBar() {
        mActionBar = mActivity.getActionBar();
        mActionBar.setTitle(mActivity.getString(R.string.notes));
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

        mActionBarHeader.setText(mActivity.getString(R.string.notes));
    }

    private void setAdapter() {

        mAdapter = new NoteAdapter(mActivity, R.layout.detail_itemtype_item, objectList);
        mListView = (ListView)mActivity.findViewById(R.id.listNotes);
        mListView.setAdapter(mAdapter);

        mListView.setClickable(true);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                NoteObject o = (NoteObject) mListView.getItemAtPosition(position);

                if (o.EntSeq.get() > 0) {

                    Intent i = new Intent(mActivity.getBaseContext(), ItemDetailsActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    i.putExtra("SelectedObjectEntSeq", o.EntSeq.get());
                    StaticObjectContainer.StaticObject = null;
                    mActivity.startActivity(i);
                } else if (o.CharacterSeq.get() > 0) {

                    Intent i = new Intent(mActivity.getBaseContext(), KanjiDetailsActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    Bundle mBundle = new Bundle();
                    mBundle.putInt("SelectedObjectEntSeq", o.CharacterSeq.get());
                    i.putExtras(mBundle);
                    StaticObjectContainer.StaticObject = o.KanjiObject.get();
                    mActivity.startActivity(i);
                } else if (o.SentenceSeq.get() > 0) {

                    Intent i = new Intent(mActivity.getBaseContext(), ExampleDetailsActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    Bundle mBundle = new Bundle();
                    mBundle.putInt("SelectedObjectEntSeq", o.SentenceSeq.get());
                    i.putExtras(mBundle);
                    StaticObjectContainer.StaticObject = o.ListObject.get();
                    mActivity.startActivity(i);
                }
            }

        });
    }


    private class LoadNotes extends AsyncTask<Void, Void, ArrayList<NoteObject>> {

        public static final int Limit = 10;
        JMDictHelper db;
        ArrayList<NoteObject> result;
        Context mContext;

        public LoadNotes(Context inContext) {
            super();
            mContext = inContext;
        }

        @Override
        protected ArrayList<NoteObject> doInBackground(Void... params) {

            db = JDictApplication.getDatabaseHelper();
            result = new ArrayList<NoteObject>();

            Cursor cursor;
            Cursor cursorCount;

            cursor = db.getVocabulary().getNotes(Limit, mCurrentOffset);

            if (allCount == 0) {

                NoteObject noteObject = new NoteObject();
                noteObject.Header.set(mActivity.getString(R.string.recent_notes));
                result.add(noteObject);

                cursorCount = db.getVocabulary().getNotesCount();

                if (cursorCount != null && cursorCount.getCount() > 0) {

                    int countIndex = cursorCount.getColumnIndex("Count");
                    allCount += cursorCount.getInt(countIndex);
                    cursorCount.close();
                }
            }

            if (cursor != null && cursor.getCount() > 0) {

                int charIndex = cursor.getColumnIndex("character_seq");
                int entSeqIndex = cursor.getColumnIndex("ent_seq");
                int sentenceIndex = cursor.getColumnIndex("sentence_seq");
                int ledIndex = cursor.getColumnIndex("Lasteditdate");
                int noteIndex = cursor.getColumnIndex("Note");

                do {
                    NoteObject noteObject = new NoteObject(cursor.getInt(charIndex), cursor.getInt(sentenceIndex), cursor.getInt(entSeqIndex), cursor.getString(ledIndex), cursor.getString(noteIndex));

                    if (noteObject.EntSeq.get() > 0) {
                        ListObject listObject = db.getWord().getBasicDetails(noteObject.EntSeq.get(), false);
                        noteObject.ListObject.set(listObject);
                    } else if (noteObject.CharacterSeq.get() > 0) {
                        KanjiBaseObject kanjiObject = db.getKanji().getBasicKanjiDetails(noteObject.CharacterSeq.get());
                        noteObject.KanjiObject.set(kanjiObject);
                    }
                    else if (noteObject.SentenceSeq.get() > 0) {
                        ExampleObject exampleObject = new ExampleObject(db.getExample().getBasicExampleDetails(noteObject.SentenceSeq.get()));
                        noteObject.ExampleObject.set(exampleObject);
                }

                result.add(noteObject);

                } while (cursor.moveToNext());

            }

            boolean getMore = false;
            if (mCurrentOffset + Limit < allCount && allCount > 0) getMore = true;

            if (getMore) {
                result.add(new NoteObject(true, allCount, Limit + mCurrentOffset, "notes")); //?)
            }

            if (cursor != null) {
                cursor.close();
            }

            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected void onPostExecute(ArrayList<NoteObject> result) {

            if (mCurrentOffset > 0) {
                mAdapter.remove(mAdapter.getItem(mAdapter.getCount() - 1));
            }

            for (NoteObject obj : result) {

                if (obj != null && obj.LastEditDate.get() != null && (lastDate == null || !obj.LastEditDate.get().equals(lastDate))) {
                    lastDate = obj.LastEditDate.get();

                    NoteObject dateObject = new NoteObject();
                    dateObject.Header.set(lastDate);
                    mAdapter.add(dateObject);
                }

                mAdapter.add(obj);
            }

            mAdapter.notifyDataSetChanged();

            if (mCurrentOffset > 0)
                mListView.post(new Runnable() {
                    @Override
                    public void run() {
                        mListView.smoothScrollToPosition(mListView.getCount());
                    }
                });

        }
    }

}
