package com.mino.jdict.logics.activities;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mino.jdict.JDictApplication;
import com.mino.jdict.R;
import com.mino.jdict.activities.AddNoteActivity;
import com.mino.jdict.activities.KanjiActivity;
import com.mino.jdict.activities.KanjiDetailsActivity;
import com.mino.jdict.activities.StrokeOrderActivity;
import com.mino.jdict.adapters.AdapterHelper;
import com.mino.jdict.adapters.KanjiDetailsAdapter;
import com.mino.jdict.interfaces.IClearable;
import com.mino.jdict.logics.basic.Logic;
import com.mino.jdict.models.basic.SearchValues;
import com.mino.jdict.objects.activities.KanjiBaseObject;
import com.mino.jdict.objects.activities.KanjiDetailObject;
import com.mino.jdict.objects.activities.KanjiObject;
import com.mino.jdict.objects.activities.KanjiRecentObject;
import com.mino.jdict.objects.activities.ListObject;

import java.util.ArrayList;

import utils.database.JMDictHelper;
import utils.database.entry.Vocabulary;
import utils.grammar.GrammarDictionaries;
import utils.grammar.InputUtils;
import utils.other.ClassObject;
import utils.other.StaticObjectContainer;
import utils.other.VocabularyPopup;

/**
 * Created by Dominik on 7/26/2015.
 */
public class KanjiBaseLogic extends Logic implements IClearable {

    protected ListView mListView;
    protected KanjiDetailsAdapter mAdapter;
    protected int selectedCharacterSeq;

    protected ArrayList<KanjiDetailObject> objectList = new ArrayList<KanjiDetailObject>();
    protected int mCurrentOffset = 0;
    protected KanjiDetailObject noteObject = null;
    private int mAllCount = 0;
    protected KanjiBaseObject mBaseObject = null;
    protected ActionBar mActionBar;
    protected VocabularyPopup mPopup = null;
    protected Boolean mToRefresh = false;
    protected LoadCompounds mLoadCompounds = null;
    protected InputUtils.WildCardMode mCurrentMode = InputUtils.WildCardMode.NoWildCard;

    public void initialize() {
        mPreviousMode = InputUtils.WildCardMode.NoWildCard;
    }

    public void clear(IClearable.ClearanceLevel inClearanceLevel) {

        mCurrentOffset = 0;
        mAllCount = 0;
        objectList.clear();
        mAdapter.clear();
        mPreviousMode = InputUtils.WildCardMode.NoWildCard;
        mToRefresh = true;
    }

    public void onPause() {
        StaticObjectContainer.SetClassObject(this.hashCode(), new ClassObject(mBaseObject));
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putInt("kanjiSeq", mBaseObject.CharacterID.get());
    }

    protected void fillDetails(boolean inShowComponents, boolean inShowCompounds) {

        objectList.clear();
        mPreviousMode = InputUtils.WildCardMode.NoWildCard;

        getBasicDetails();

        //mBaseObject = GetBaseObject();

        objectList.add(new KanjiDetailObject(mBaseObject, false, false));

        if (!mBaseObject.getKunyomi().isEmpty() || !mBaseObject.getOnyomi().isEmpty() || !mBaseObject.getNanori().isEmpty()) {
            addHeader(R.string.reading_section);
        }

        if (!mBaseObject.getKunyomi().isEmpty()) {
            KanjiDetailObject obj = new KanjiDetailObject(mBaseObject, false, false);
            obj.IsKunyomi.set(true);
            objectList.add(obj);
        }

        if (!mBaseObject.getOnyomi().isEmpty()) {
            KanjiDetailObject obj = new KanjiDetailObject(mBaseObject, false, false);
            obj.IsOnyomi.set(true);
            objectList.add(obj);
        }

        if (!mBaseObject.getNanori().isEmpty()) {
            KanjiDetailObject obj = new KanjiDetailObject(mBaseObject, false, false);
            obj.IsNanori.set(true);
            objectList.add(obj);
        }

        getTranslations();

        if (inShowComponents) {
            getComponents();
        }

        addHeader(R.string.misc_section);
        getMisc();
        addHeader(R.string.character_sets_section);
        getCharacterSets();

        addHeader(R.string.note_section);
        addNote();

        if (inShowCompounds) {
            getCompounds();
        }

        getAdditionalData();
        setAdapter();
    }

    private void addNote() {
        KanjiDetailObject noteObject = new KanjiDetailObject();

        Cursor c = JDictApplication.getDatabaseHelper().getVocabulary().getNote(selectedCharacterSeq, Vocabulary.ElementType.Kanji);

        try {
            if (c != null && c.getCount() > 0) {

                int noteIndex = c.getColumnIndex("Note");
                String note = c.getString(noteIndex);
                noteObject.Note.set(note);
                noteObject.IsInDatabase.set(true);
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }

        noteObject.IsNote.set(true);
        objectList.add(noteObject);
    }

    protected void getAdditionalData() {
    }

    protected void getBasicDetails() {
        mBaseObject = JDictApplication.getDatabaseHelper().getKanji().getBasicKanjiDetails(selectedCharacterSeq);
    }

    private String getKanjiStrokeOrder() {

        String strokeOrder = "";
        Cursor cursor = JDictApplication.getDatabaseHelper().getKanji().getKanjiSvg(mBaseObject.CharacterID.get());

        if (cursor != null && cursor.getCount() > 0) {

            int strokeIndex = cursor.getColumnIndex("stroke_svg");

            do {

                strokeOrder = cursor.getString(strokeIndex);

            } while (cursor.moveToNext());

        }

        assert cursor != null;
        cursor.close();

        return strokeOrder;
    }

    private void getComponents() {
        Cursor cursor = JDictApplication.getDatabaseHelper().getKanji().getKanjiDecompositionLiterals(mBaseObject.CharacterID.get());

        if (cursor != null && cursor.getCount() > 0) {

            addHeader(R.string.kanji_components);
            int decompIndex = cursor.getColumnIndex("decomposition");

            do {

                String decomposition = cursor.getString(decompIndex);

                if (!decomposition.isEmpty()) {

                    String[] components = decomposition.split(";");

                    for (String component : components) {

                        Cursor cursorKanji = JDictApplication.getDatabaseHelper().getKanji().getKanjiBasicInfo(component);
                        AdapterHelper.GetKanjiBasicInfo(cursorKanji, new AdapterHelper.KanjiBasicInfoInterface() {
                            @Override
                            public void onGotData(KanjiBaseObject result) {

                                result.ShowArrow.set(true);
                                objectList.add(new KanjiDetailObject(result, true, false));
                            }
                        }, component);

                        cursor.close();
                    }
                }

            } while (cursor.moveToNext());

        }

        assert cursor != null;
        cursor.close();
    }

    private void getCompounds() {

        (mLoadCompounds = new LoadCompounds(mActivity, mCurrentMode)).execute(mBaseObject);
    }

    public void getMore() {
    }

    public void getCompounds(InputUtils.WildCardMode inMode) {

        if (mLoadCompounds.getStatus() == AsyncTask.Status.RUNNING) {
            return;
        }

        mAllCount = 0;
        mCurrentOffset = 0;
        mCurrentMode = inMode;

        int i = mAdapter.getCount();

        while (i-- > 0) {

            if (mAdapter.getItem(i).ListObject.get() == null) break;
            mAdapter.remove(mAdapter.getItem(i));
        }

        mAdapter.notifyDataSetChanged();
        getCompounds();
    }

    protected void getMoreCompounds() {

        mCurrentOffset += 5;
        getCompounds();
    }

    public void showStrokeOrder(String svg) {

        Intent i = new Intent(mActivity.getBaseContext(), StrokeOrderActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        Bundle mBundle = new Bundle();
        mBundle.putString("SVG", svg);
        i.putExtras(mBundle);
        mActivity.startActivity(i);
    }


    protected void getTranslations() {
        Cursor cursor = JDictApplication.getDatabaseHelper().getKanji().getKanjiMeaningEng(mBaseObject.CharacterID.get());

        if (cursor != null && cursor.getCount() > 0) {

            addHeader(R.string.meaning_section);

            int meanIndex = cursor.getColumnIndex("meanGroup");

            do {

                int flagId = GrammarDictionaries.LANGUAGES_LIST.get("");
                objectList.add(new KanjiDetailObject(cursor.getString(meanIndex), flagId));

            } while (cursor.moveToNext());

        }

        assert cursor != null;
        cursor.close();

    }


    protected void getMisc() {

        String svg = getKanjiStrokeOrder();
        Cursor cursor = JDictApplication.getDatabaseHelper().getKanji().getKanjiMisc(mBaseObject.CharacterID.get());
        KanjiDetailObject obj;

        if (cursor != null && cursor.getCount() > 0) {

            int gradeIndex = cursor.getColumnIndex("grade");
            int strokeIndex = cursor.getColumnIndex("stroke_count");
            int freqIndex = cursor.getColumnIndex("freq");
            int jlptIndex = cursor.getColumnIndex("jlpt");

            do {

                int strokeCount = cursor.getInt(strokeIndex);
                if (strokeCount > 0) {
                    obj = new KanjiDetailObject();
                    obj.StrokeCount.set(strokeCount);
                    obj.StrokeSvg.set(svg);
                    objectList.add(obj);
                }

                int jlpt = cursor.getInt(jlptIndex);
                if (jlpt > 0) {
                    obj = new KanjiDetailObject();
                    obj.JLPT.set(jlpt);
                    objectList.add(obj);
                }

                int grade = cursor.getInt(gradeIndex);
                if (grade > 0) {
                    obj = new KanjiDetailObject();
                    obj.Grade.set(grade);
                    objectList.add(obj);
                }

                int freq = cursor.getInt(freqIndex);
                if (freq > 0) {
                    obj = new KanjiDetailObject();
                    obj.Freq.set(freq);
                    objectList.add(obj);
                }

            } while (cursor.moveToNext());

        }

        assert cursor != null;
        cursor.close();
    }

    protected void getCharacterSets() {
        Cursor cursor = JDictApplication.getDatabaseHelper().getKanji().getCharacterSets(mBaseObject.CharacterID.get());
        KanjiDetailObject obj;

        if (cursor != null && cursor.getCount() > 0) {

            int typeIndex = cursor.getColumnIndex("cp_type");
            int valueIndex = cursor.getColumnIndex("cp_value");

            do {

                String type = cursor.getString(typeIndex);

                String val = cursor.getString(valueIndex);
                if (!val.isEmpty()) {
                    obj = new KanjiDetailObject();
                    obj.setCharacterSet(type, val);
                    objectList.add(obj);
                }


            } while (cursor.moveToNext());

        }

        assert cursor != null;
        cursor.close();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (selectedCharacterSeq > 0) {
            AddNoteLogic.noteResult(requestCode, resultCode, data, selectedCharacterSeq, noteObject, mActivity.getString(R.string.addnote), mAdapter, Vocabulary.ElementType.Kanji);
        }
    }

    protected void setAdapter() {
        mAdapter = new KanjiDetailsAdapter(mActivity, R.layout.detail_itemtype_item, objectList);
        mListView = (ListView) mActivity.findViewById(R.id.meaningListView);
        mListView.setAdapter(mAdapter);

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

                if (o instanceof KanjiDetailObject && ((KanjiDetailObject) o).StrokeCount.get() > 0) {

                    String svg = ((KanjiDetailObject) o).StrokeSvg.get();

                    if (svg != null && svg.length() > 0) {

                        showStrokeOrder(svg);
                    }
                } else if (o instanceof KanjiDetailObject && ((KanjiDetailObject) o).IsNote.get()) {

                    noteObject = ((KanjiDetailObject) o);
                    Intent i = new Intent(mActivity.getBaseContext(), AddNoteActivity.class);
                    i.putExtra("text",  noteObject.Note.get());

                    i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    mActivity.startActivityForResult(i, 1);
                }
                /*
                else if (o instanceof KanjiDetailObject && ((KanjiDetailObject) o).IsNote().get()) {

                    Intent i = new Intent(mActivity.getBaseContext(), AddNoteActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    mActivity.startActivity(i);

                } */
                else if (o instanceof KanjiDetailObject && ((KanjiDetailObject) o).IsComponentKanji.get()) {

                    KanjiBaseObject obj = ((KanjiDetailObject) o).getKanjiObject();

                    if (obj != null) {
                        Intent i = new Intent(mActivity.getBaseContext(), KanjiDetailsActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        Bundle mBundle = new Bundle();
                        mBundle.putParcelable("PAR", ((KanjiDetailObject) o));
                        i.putExtras(mBundle);

                        KanjiRecentObject recentObj = new KanjiRecentObject.KanjiRecentObjectFactory().factory();
                        KanjiObject kanjiObject = new KanjiObject(obj);

                        SearchValues values = new SearchValues();
                        ArrayList<Boolean> cbValues = new ArrayList<Boolean>();
                        cbValues.add(false);
                        values.setCheckBoxValues(cbValues);

                        recentObj.bind(kanjiObject, values);

                        JDictApplication.getDatabaseHelper().getWord().insertRecentWords(recentObj);
                        Logic.refreshActivity(KanjiActivity.class, IClearable.ClearanceLevel.Soft);

                        mActivity.startActivity(i);
                    }
                }
            }

        });
    }

    protected void addHeader(int stringID) {
        KanjiDetailObject meaningHeader = new KanjiDetailObject();
        meaningHeader.SectionHeader.set(mActivity.getResources().getString(stringID));
        objectList.add(meaningHeader);
    }

    private InputUtils.WildCardMode mPreviousMode = InputUtils.WildCardMode.NoWildCard;

    protected class LoadCompounds extends AsyncTask<KanjiBaseObject, Void, ArrayList<ListObject>> {

        public static final int Limit = 5;
        JMDictHelper db;
        ArrayList<ListObject> result;
        Context mContext;
        String mCharacter = null;
        InputUtils.WildCardMode mMode = InputUtils.WildCardMode.NoWildCard;

        public LoadCompounds(Context inContext, InputUtils.WildCardMode inMode) {

            super();
            mContext = inContext;
            mMode = inMode;
        }

        @Override
        protected ArrayList<ListObject> doInBackground(KanjiBaseObject... params) {

            db = JDictApplication.getDatabaseHelper();
            result = new ArrayList<ListObject>();

            Cursor cursor = null;
            Cursor cursorCount = null;

            KanjiBaseObject baseObject = params[0];
            mCharacter = baseObject.Character.get();

            cursor = db.getKanji().getCompounds(mCharacter, Limit, mCurrentOffset, mMode);

            if (mAllCount == 0) {
                cursorCount = db.getKanji().getCompoundsCount(baseObject.Character.get(), mMode);

                if (cursorCount != null && cursorCount.getCount() > 0) {

                    int countIndex = cursorCount.getColumnIndex("Count");
                    mAllCount += cursorCount.getInt(countIndex);
                }

                assert cursorCount != null;
                cursorCount.close();
            }

            //cursor = db.getEntryAlike("*" + GetBaseObject().getCharacter() + "*", limit, mCurrentOffset, false);

            if (mCurrentOffset == 0 && (cursor.getCount() > 0 || mMode != InputUtils.WildCardMode.NoWildCard)) {
                ListObject meaningHeader = new ListObject();
                meaningHeader.AddHeader.set(mContext.getResources().getString(R.string.compounds));
                meaningHeader.CompoundsSectionCharacter.set(mCharacter);
                meaningHeader.WildCard.set(mMode);
                result.add(meaningHeader);
            }

            boolean getMore = false;
            //if (cursor.getCount() == Limit) getMore = true;
            if (mCurrentOffset + Limit < mAllCount && mAllCount > 0) getMore = true;

            db.getWord().getDataFromCursor(result, cursor, false, true, false, false);

            if (getMore) {

                //result.add(new ListObject(true));
                result.add(new ListObject(true, mAllCount, Limit + mCurrentOffset, "compounds")); //?)
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

        protected void onPostExecute(ArrayList<ListObject> result) {

            if (mCurrentOffset > 0) {
                mAdapter.remove(mAdapter.getItem(mAdapter.getCount() - 1));
            }

            for (ListObject obj : result) {
                mAdapter.add(new KanjiDetailObject(obj));
            }

            mAdapter.notifyDataSetChanged();

            if (mCurrentOffset > 0 || mPreviousMode != mMode)
                mListView.post(new Runnable() {
                    @Override
                    public void run() {
                        mListView.smoothScrollToPosition(mListView.getCount());
                    }
                });

            mPreviousMode = mMode;
        }
    }
}
