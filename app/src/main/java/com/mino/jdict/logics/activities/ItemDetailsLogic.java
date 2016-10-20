package com.mino.jdict.logics.activities;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mino.jdict.JDictApplication;
import com.mino.jdict.R;
import com.mino.jdict.activities.AddNoteActivity;
import com.mino.jdict.activities.ExampleActivity;
import com.mino.jdict.activities.ExampleDetailsActivity;
import com.mino.jdict.activities.KanjiActivity;
import com.mino.jdict.activities.KanjiDetailsActivity;
import com.mino.jdict.activities.MainActivity;
import com.mino.jdict.adapters.AdapterHelper;
import com.mino.jdict.adapters.ItemDetailsAdapter;
import com.mino.jdict.interfaces.IClearable;
import com.mino.jdict.logics.basic.Logic;
import com.mino.jdict.models.basic.SearchValues;
import com.mino.jdict.objects.activities.ExampleObject;
import com.mino.jdict.objects.activities.ExampleRecentObject;
import com.mino.jdict.objects.activities.ItemDetailObject;
import com.mino.jdict.objects.activities.KanjiBaseObject;
import com.mino.jdict.objects.activities.KanjiObject;
import com.mino.jdict.objects.activities.KanjiRecentObject;
import com.mino.jdict.objects.activities.ListObject;
import com.mino.jdict.objects.basic.GrammarObject;
import com.mino.jdict.objects.basic.SenseObject;

import java.util.ArrayList;
import java.util.List;

import utils.database.entry.Vocabulary;
import utils.grammar.FuriganaHelper;
import utils.grammar.GrammarDictionaries;
import utils.grammar.InputUtils;
import utils.other.ShowUtils;
import utils.other.StaticObjectContainer;
import utils.other.VocabularyPopup;

/**
 * Created by Dominik on 7/25/2015.
 */
public class ItemDetailsLogic extends Logic implements IClearable {

    private static Boolean added = false;
    private final static int OFFSET_SIZE = 5;

    private int allCount = 0;
    private ListView mListView;
    private String wordContent;
    private ItemDetailsAdapter mAdapter;
    private ArrayList<ItemDetailObject> objectList = new ArrayList<ItemDetailObject>();
    private String[] wordType;
    private int currentAlternative = 0;
    private int currentAlternativeReading = 0;
    private boolean currentReNoKanji = false;
    private int selectedObjectEntSeq;
    private FuriganaHelper mFuriganaHelper;
    private ListObject mSelectedObject;
    private int mCurrentOffset = 0;
    private String query = "";
    private ActionBar mActionBar;
    private VocabularyPopup mPopup = null;
    private ItemDetailObject noteObject = null;
    private Boolean mToRefresh = false;
    private LoadExamples mLoadExamples;

    public void clear(IClearable.ClearanceLevel inClearanceLevel) {

        mCurrentOffset = 0;
        allCount = 0;
        objectList.clear();
        mAdapter.clear();
        mToRefresh = true;
    }

    public void resume() {

        if (mPopup != null && mSelectedObject != null) {
            mPopup.checkIfInVocabList(mSelectedObject.ID.get());
        }

        if (mToRefresh) {
            mToRefresh = false;

            if (mLoadExamples == null || mLoadExamples.getStatus() != AsyncTask.Status.RUNNING) {
                fillDetails();
            }
        }

        super.resume();
    }

    public void initialize() {
        if (mBundle == null) {

            Bundle extras = mActivity.getIntent().getExtras();

            selectedObjectEntSeq = extras.getInt("SelectedObjectEntSeq");
            query = extras.getString("ConjugatedQuery");

            if (StaticObjectContainer.StaticObject instanceof ListObject) {
                mSelectedObject = (ListObject) StaticObjectContainer.StaticObject;
                StaticObjectContainer.StaticObject = null;
            } else StaticObjectContainer.StaticObject = null;

            currentAlternative = 0;

            Logic.refreshActivity(MainActivity.class, IClearable.ClearanceLevel.Soft);
            fillDetails();

        }
    }

    private void restoreActionBar() {
        mActionBar = mActivity.getActionBar();
        assert mActionBar != null;
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
        mActionBar.setDisplayShowHomeEnabled(true);
        mActionBar.setCustomView(R.layout.actionbar_favourites);
        mActionBar.setDisplayShowCustomEnabled(true);
    }


    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putInt("alternative", currentAlternative);
        savedInstanceState.putInt("selectedObjectEntSeq", selectedObjectEntSeq);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {

        currentAlternative = savedInstanceState.getInt("alternative");
        selectedObjectEntSeq = savedInstanceState.getInt("selectedObjectEntSeq");

        if (objectList == null || objectList.size() == 0) {
            fillDetails();
        }
    }

    public void onNewIntent(Intent intent) {

        Bundle extras = intent.getExtras();

        mSelectedObject = null;
        selectedObjectEntSeq = extras.getInt("SelectedObjectEntSeq");
        currentAlternative = 0;

        if (StaticObjectContainer.StaticObject instanceof ListObject) {
            mSelectedObject = (ListObject) StaticObjectContainer.StaticObject;
            StaticObjectContainer.StaticObject = null;
        } else StaticObjectContainer.StaticObject = null;
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        restoreActionBar();

        final View actionBarCustomView = mActionBar.getCustomView();

        ImageView mFavButton =
                (ImageView) actionBarCustomView.findViewById(R.id.favButton);

        TextView mTextViewHeader =
                (TextView) actionBarCustomView.findViewById(R.id.textViewHeader);

        mTextViewHeader.setText(mActivity.getString(R.string.word_header));

        mPopup = new VocabularyPopup(mActivity, mFavButton, Vocabulary.ElementType.Entry);

        if (mSelectedObject != null) {
            mPopup.checkIfInVocabList(mSelectedObject.ID.get());
        }

        mFavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopup.showPopup(mSelectedObject.ID.get());
            }
        });

        return true;
    }

    private void fillDetails() {

        objectList.clear();

        if (mFuriganaHelper == null) {
            mFuriganaHelper = new FuriganaHelper();
        }

        setAdapter();

        if (selectedObjectEntSeq > 0) {

            if (mSelectedObject == null) {
                getBasicDetails();
            } else {
                JDictApplication.getDatabaseHelper().getSense().getSenseObjects(true, mSelectedObject.getSenseList(), mSelectedObject.ID.get());
            }

            if (mSelectedObject != null) {

                addMainObjectWithFurigana();
                getAlternatives();

                wordType = mSelectedObject.WordType.get().split(";");

                addHeader(R.string.meaning_section);
                addMeaning();

                //getTranslations();

                checkForSuruVerb();
                addKanjiDecomposition();
                addBasicInfo();
                addConjugation();

                mCurrentOffset = 0;

                addHeader(R.string.note_section);
                addNote();
                addExamples();

            }
        }
    }

    private void addNote() {
        ItemDetailObject noteObject = new ItemDetailObject();

        Cursor c = JDictApplication.getDatabaseHelper().getVocabulary().getNote(mSelectedObject.ID.get(), Vocabulary.ElementType.Entry);

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

    private void addMainObjectWithFurigana() {
        ItemDetailObject MainObject = new ItemDetailObject();

        if (mSelectedObject.getKebListLen() > 0) {

            Pair<String, Boolean> currentKanjiReading = JDictApplication.getDatabaseHelper().getWord().getRebForKeb(mSelectedObject.getFromKebList(currentAlternative), mSelectedObject).get(currentAlternativeReading);

            if (currentKanjiReading.second) {
                mFuriganaHelper.matchFurigana(MainObject, currentKanjiReading.first, currentKanjiReading.first);
            } else {
                mFuriganaHelper.matchFurigana(MainObject, mSelectedObject.getFromKebList(currentAlternative), currentKanjiReading.first);
            }

        } else if (mSelectedObject.getRebListLen() > 0) {
            String reb = mSelectedObject.getFromRebList(currentAlternative);
            mFuriganaHelper.matchFurigana(MainObject, reb, reb);
        }

        MainObject.IsMain.set(true);
        objectList.add(MainObject);
    }

    private boolean containsString(ArrayList<String> list, String str) {
        for (String p : list) {
            if (p.equals(str)) {
                return false;
            }
        }
        return true;
    }

    private void getAlternatives() {

        boolean alternativeHeaderAdded = false;

        // has entry with kanji
        if (mSelectedObject.getKebListLen() > 0) {

            ArrayList<Pair<String, Boolean>> allReadingAlternatives = JDictApplication.getDatabaseHelper().getWord().getRebForKeb(mSelectedObject.getFromKebList(currentAlternative), mSelectedObject);
            Pair<String, Boolean> currentKanjiReading = allReadingAlternatives.get(currentAlternativeReading);
            ArrayList<String> addedAlternativeKanaReading = new ArrayList<String>();

            currentReNoKanji = currentKanjiReading.second;
            wordContent = currentReNoKanji ? currentKanjiReading.first : mSelectedObject.getFromKebList(currentAlternative);

            getKanjiInfo(mSelectedObject.getFromKebList(currentAlternative), mSelectedObject.ID.get()); // ke_inf
            getReadingInfo(currentKanjiReading.first, mSelectedObject.ID.get()); // re_inf

            if (allReadingAlternatives.size() > 1) {
                addHeader(R.string.alternatives);
                alternativeHeaderAdded = true;
            }

            addedAlternativeKanaReading.add(allReadingAlternatives.get(currentAlternativeReading).first);

            // selected word in kanji might have other alternative readings
            for (int j = 0; j < allReadingAlternatives.size(); j++) {

                if (j != currentAlternativeReading) {
                    ItemDetailObject obj = null;

                    if (!allReadingAlternatives.get(j).second) {
                        obj = new ItemDetailObject(allReadingAlternatives.get(j).first, mSelectedObject.getFromKebList(currentAlternative), true);
                        obj.setIsAlternative(true, currentAlternative, j);
                        objectList.add(obj);
                    } else {
                        String str = allReadingAlternatives.get(j).first;

                        if (containsString(addedAlternativeKanaReading, str)) {
                            obj = new ItemDetailObject("", str, true);
                            addedAlternativeKanaReading.add(str);

                            obj.setIsAlternative(true, currentAlternative, j);
                            objectList.add(obj);
                        }
                    }
                }
            }

            // has alternative kanji words
            if (mSelectedObject.getKebListLen() > 1) {

                if (!alternativeHeaderAdded) {
                    addHeader(R.string.alternatives);
                }

                for (int i = 0; i < mSelectedObject.getKebListLen(); i++)
                    if (i != currentAlternative) {
                        allReadingAlternatives = JDictApplication.getDatabaseHelper().getWord().getRebForKeb(mSelectedObject.getFromKebList(i), mSelectedObject);

                        for (int j = 0; j < allReadingAlternatives.size(); j++) {
                            ItemDetailObject obj = null;

                            if (!allReadingAlternatives.get(j).second) {
                                obj = new ItemDetailObject(allReadingAlternatives.get(j).first, mSelectedObject.getFromKebList(i), true);

                                obj.setIsAlternative(true, i, j);
                                objectList.add(obj);
                            } else {
                                String str = allReadingAlternatives.get(j).first;

                                if (containsString(addedAlternativeKanaReading, str)) {
                                    obj = new ItemDetailObject("", str, true);
                                    addedAlternativeKanaReading.add(str);

                                    obj.setIsAlternative(true, i, j);
                                    objectList.add(obj);
                                }
                            }
                        }
                    }
            }
        } else // has alternative kana words
        {
            if (mSelectedObject.getRebListLen() > 0) {
                wordContent = mSelectedObject.getFromRebList(currentAlternative);

                if (mSelectedObject.getRebListLen() > 1) {
                    if (!alternativeHeaderAdded) {
                        addHeader(R.string.alternatives);
                    }

                    for (int i = 0; i < mSelectedObject.getRebListLen(); i++) {

                        if (i != currentAlternative) {
                            ItemDetailObject obj = new ItemDetailObject("", mSelectedObject.getFromRebList(i), true);
                            obj.setIsAlternative(true, i, 0);
                            objectList.add(obj);
                        }
                    }
                }
            }
        }
    }


    private void addConjugation() {
        boolean conjugationHeaderAdded = false;

        for (List<GrammarObject> currentObj : GrammarDictionaries.GRAMMAR_LIST) {

            for (GrammarObject gObj : currentObj) {

                for (String s : wordType) {
                    if (s.contentEquals(gObj.getWordType())) {

                        if (gObj.getIsVisibleInDetails()) {

                            if (gObj.getWordType().equals("adj-na")) {
                                conjugationHeaderAdded = addConjugationHeader(conjugationHeaderAdded);
                                ItemDetailObject conjItem = new ItemDetailObject();

                                conjItem.setConjugationItem(gObj.getGrammar(), wordContent + gObj.getChangedForm().replace("_", ""), GrammarObject.convertGrammarChainToList(mSelectedObject.GrammarChain.get()).contains(gObj.getGrammar()));

                                objectList.add(conjItem);
                            } else if (wordContent.contains(gObj.getSimpleForm()) || s.contentEquals("vs") || s.contentEquals("vk") || s.contentEquals("vs-i")) {

                                String conjugatedFormString = wordContent.substring(0, wordContent.length() - gObj.getSimpleForm().length()) + gObj.getChangedForm();
                                conjugationHeaderAdded = addConjugationHeader(conjugationHeaderAdded);
                                ItemDetailObject conjItem = new ItemDetailObject();

                                conjItem.setConjugationItem(gObj.getGrammar(), conjugatedFormString.replace("_", ""), GrammarObject.convertGrammarChainToList(mSelectedObject.GrammarChain.get()).contains(gObj.getGrammar()));
                                objectList.add(conjItem);
                            } else if (wordContent.equals("有る")) { // wyjątek, ucinamy kanji
                                conjugationHeaderAdded = addConjugationHeader(conjugationHeaderAdded);
                                ItemDetailObject conjItem = new ItemDetailObject();

                                conjItem.setConjugationItem(gObj.getGrammar(), gObj.getChangedForm(), GrammarObject.convertGrammarChainToList(mSelectedObject.GrammarChain.get()).contains(gObj.getGrammar()));

                                objectList.add(conjItem);
                            }
                        }
                    }
                }
            }

        }
    }

    private boolean addConjugationHeader(boolean conjugationHeaderAdded) {
        if (!conjugationHeaderAdded) {
            addHeader(R.string.conjugation_section);
            conjugationHeaderAdded = true;
        }
        return conjugationHeaderAdded;
    }

    private void checkForSuruVerb() {
        boolean hasVS = false, hasN = false;

        for (String s : wordType) {
            if (s.contentEquals("vs"))
                hasVS = true;

            if (s.contentEquals("n"))
                hasN = true;
        }

        if (hasN && hasVS) {
            wordContent += "する";
        }
    }

    private void addHeader(int stringID) {
        ItemDetailObject meaningHeader = new ItemDetailObject();
        meaningHeader.SectionHeader.set(mActivity.getResources().getString(stringID));
        objectList.add(meaningHeader);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (mSelectedObject != null) {
            AddNoteLogic.noteResult(requestCode, resultCode, data, mSelectedObject.ID.get(), noteObject, mActivity.getString(R.string.addnote), mAdapter, Vocabulary.ElementType.Entry);
        }
    }

    private void setAdapter() {
        mAdapter = new ItemDetailsAdapter(mActivity, R.layout.detail_itemtype_item, objectList);
        mListView = (ListView) mActivity.findViewById(R.id.meaningListView);
        mListView.setAdapter(mAdapter);

        mListView.setClickable(true);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                Object o = mListView.getItemAtPosition(position);

                if (o instanceof ItemDetailObject) {

                    ItemDetailObject obj = ((ItemDetailObject) o);

                    if (obj.Xref.get() != null && obj.Xref.get().second > 0) {

                        goToItemDetails(((ItemDetailObject) o).Xref.get().second);
                    }

                    if (obj.Ant.get() != null && obj.Ant.get().second > 0) {

                        goToItemDetails(((ItemDetailObject) o).Ant.get().second);
                    }

                    if (obj.IsGetNewResults.get()) {

                        mCurrentOffset += OFFSET_SIZE;
                        addExamples();
                    }

                    if (obj.IsNewResultsForSelectedSense.get()) {

                        obj.SenseObject.get().setOffsetSize(OFFSET_SIZE);
                        ArrayList<ItemDetailObject> exampleList = JDictApplication.getDatabaseHelper().getSense().getMoreExamples(obj.SenseObject.get(), mSelectedObject.SenseID.get());

                        int pos = mAdapter.getPosition(obj);
                        objectList.remove(pos);
                        objectList.addAll(pos, exampleList);
                        mAdapter.notifyDataSetChanged();

                        mListView.smoothScrollToPosition(pos + OFFSET_SIZE);
                    }

                    if (obj.IsAlternative.get()) {

                        currentAlternative = ((ItemDetailObject) o).AlternativeNumber.get();
                        currentAlternativeReading = ((ItemDetailObject) o).AlternativeReading.get();
                        fillDetails();
                    }

                    if (obj.IsNote.get()) {

                        noteObject = ((ItemDetailObject) o);
                        Intent i = new Intent(mActivity.getBaseContext(), AddNoteActivity.class);
                        i.putExtra("text",  noteObject.Note.get());

                        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        mActivity.startActivityForResult(i, 1);
                    }

                    if (obj.getIsKanjiObject()) {

                        Intent i = new Intent(mActivity.getBaseContext(), KanjiDetailsActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        Bundle mBundle = new Bundle();
                        mBundle.putParcelable("PAR", ((ItemDetailObject) o));
                        i.putExtras(mBundle);

                        ItemDetailObject listObj = ((ItemDetailObject) o);
                        KanjiRecentObject recentObj = new KanjiRecentObject.KanjiRecentObjectFactory().factory();
                        KanjiObject kanjiObj = new KanjiObject(listObj.getKanjiObject());

                        SearchValues values = new SearchValues();
                        ArrayList<Boolean> cbValues = new ArrayList<Boolean>();
                        cbValues.add(false);
                        values.setCheckBoxValues(cbValues);

                        recentObj.bind(kanjiObj, values);

                        JDictApplication.getDatabaseHelper().getWord().insertRecentWords(recentObj);
                        Logic.refreshActivity(KanjiActivity.class, IClearable.ClearanceLevel.Soft);

                        mActivity.startActivity(i);
                    }

                    if (obj.IsExample.get()) {
                        ExampleObject exampleObj = new ExampleObject((ItemDetailObject) o);

                        Intent i = new Intent(mActivity.getBaseContext(), ExampleDetailsActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        i.putExtra("SelectedObjectEntSeq", exampleObj.ID.get());
                        StaticObjectContainer.StaticObject = ((ExampleObject) exampleObj);

                        ExampleRecentObject recentObj = new ExampleRecentObject.ExampleRecentObjectFactory().factory();

                        SearchValues values = new SearchValues();
                        ArrayList<Boolean> cbValues = new ArrayList<Boolean>();
                        cbValues.add(false);
                        values.setCheckBoxValues(cbValues);

                        recentObj.bind(exampleObj, values);
                        JDictApplication.getDatabaseHelper().getWord().insertRecentWordsV2(recentObj);
                        Logic.refreshActivity(ExampleActivity.class, IClearable.ClearanceLevel.Soft);

                        mActivity.startActivity(i);
                    }
                }
            }
        });
    }

    private void addBasicInfo() {

        boolean basicHeaderAdded = false;

        if (mSelectedObject.IsCommon.get()) {

            addHeader(R.string.additional_section);
            basicHeaderAdded = true;

            ItemDetailObject obj = new ItemDetailObject(true);
            objectList.add(obj);
        }

        String keb = null, reb = null;

        if (mSelectedObject.getKebListLen() > 0) {
            keb = mSelectedObject.getFromKebList(currentAlternative);
            reb = JDictApplication.getDatabaseHelper().getWord().getRebForKeb(mSelectedObject.getFromKebList(currentAlternative), mSelectedObject).get(currentAlternativeReading).first;
        } else if (mSelectedObject.getRebListLen() > 0) {
            reb = mSelectedObject.getFromRebList(currentAlternative);
        }

        Cursor cursor;
        Cursor cursor_reb = null;

        // word is in kana - in that case we search ID_r_ele in JLPT table
        if (reb == null && InputUtils.isStringAllKana(keb)) {
            cursor = JDictApplication.getDatabaseHelper().getWord().getJLPTLevel(keb, null, selectedObjectEntSeq);
        } else {
            cursor = JDictApplication.getDatabaseHelper().getWord().getJLPTLevel(reb, keb, selectedObjectEntSeq);
            cursor_reb = JDictApplication.getDatabaseHelper().getWord().getJLPTLevel(reb, null, selectedObjectEntSeq);
        }

        int JLPT_level = 0, JLPT_level_reb = 0;

        try {
            if (cursor != null && cursor.getCount() > 0) {

                int jlptIndex = cursor.getColumnIndex("JLPT_level");

                do {

                    JLPT_level = cursor.getInt(jlptIndex);

                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        try {
            if (cursor_reb != null && cursor_reb.getCount() > 0) {

                int jlptIndex = cursor_reb.getColumnIndex("JLPT_level");

                do {

                    JLPT_level_reb = cursor_reb.getInt(jlptIndex);

                } while (cursor_reb.moveToNext());
            }
        } finally {
            if (cursor_reb != null) {
                cursor_reb.close();
            }
        }

        if (JLPT_level > 0 || JLPT_level_reb > 0) {

            basicHeaderAdded = checkAdditionalHeader(basicHeaderAdded);

            ItemDetailObject obj = new ItemDetailObject(JLPT_level, JLPT_level_reb);
            objectList.add(obj);
        }

        if (mSelectedObject.IsConjugated.get()) {

            basicHeaderAdded = checkAdditionalHeader(basicHeaderAdded);

            ItemDetailObject obj = new ItemDetailObject();
            obj.ConjugatedQuery.set(query);
            obj.GrammarChain.set(mSelectedObject.GrammarChain.get());
            objectList.add(obj);
        }
    }

    private boolean checkAdditionalHeader(boolean basicHeaderAdded) {
        if (!basicHeaderAdded) {
            addHeader(R.string.additional_section);
            basicHeaderAdded = true;
        }
        return basicHeaderAdded;
    }

    public void goToItemDetails(int ent_seq) {

        ShowUtils.showEntry(ent_seq, mActivity);
    }

    private void addMeaning() {

        if (mSelectedObject.getSenseList() != null) {

            ArrayList<SenseObject> senseList = mSelectedObject.getSenseList();

            for (int i = 0; i < senseList.size(); i++) {

                if (senseList.get(i).getSenseID() == mSelectedObject.SenseID.get()) {
                    senseList.get(i).addItemDetailObjects(objectList, true);

                    if (i != senseList.size() - 1) {
                        addSenseSeparator();
                    }

                    break;
                }
            }

            for (int i = 0; i < senseList.size(); i++) {

                // could have a lot of distinct meanings (Sense)
                SenseObject senseObj = senseList.get(i);

                if (senseObj.getSenseID() != mSelectedObject.SenseID.get()) {
                    senseObj.addItemDetailObjects(objectList, false);

                    if (i != senseList.size() - 1) {
                        addSenseSeparator();
                    }
                }
            }
        }
    }

    private void addSenseSeparator() {
        ItemDetailObject senseSeparator = new ItemDetailObject();
        senseSeparator.IsSenseSeparator.set(true);
        objectList.add(senseSeparator);
    }

    private void addKanjiDecomposition() {

        if (mSelectedObject.getKebListLen() > 0 && !currentReNoKanji) {
            String keb = mSelectedObject.getFromKebList(currentAlternative);

            if (!InputUtils.isStringAllKana(keb) && !InputUtils.isStringAllRomaji(keb)) {

                added = false;

                for (int i = 0; i < keb.length(); i++) {

                    String character = keb.substring(i, i + 1);
                    if (!InputUtils.isKana(character)) {


                        Cursor cursorKanji = JDictApplication.getDatabaseHelper().getKanji().getKanjiBasicInfo(character);
                        AdapterHelper.GetKanjiBasicInfo(cursorKanji, new AdapterHelper.KanjiBasicInfoInterface() {
                            @Override
                            public void onGotData(KanjiBaseObject result) {

                                if (!added) {
                                    addHeader(R.string.kanji_decomp_section);
                                    added = true;
                                }

                                result.ShowArrow.set(true);

                                objectList.add(new ItemDetailObject(result));
                            }
                        }, null);
                    }
                }
            }
        }
    }

    private void addExamples() {

        (mLoadExamples = new LoadExamples(mActivity)).execute();
    }

    private void getKanjiInfo(String k_ele, int ent_seq) {
        Cursor cursor = JDictApplication.getDatabaseHelper().getWord().getKeInf(k_ele, ent_seq);

        if (cursor != null && cursor.getCount() > 0) {

            int keInfIndex = cursor.getColumnIndex("ke_inf");

            do {

                objectList.add(new ItemDetailObject(cursor.getString(keInfIndex)));

            } while (cursor.moveToNext());

        }

        assert cursor != null;
        cursor.close();
    }


    private void getReadingInfo(String r_ele, int ent_seq) {
        Cursor cursor = JDictApplication.getDatabaseHelper().getWord().getReInf(r_ele, ent_seq);

        if (cursor != null && cursor.getCount() > 0) {

            int reInfIndex = cursor.getColumnIndex("re_inf");

            do {

                objectList.add(new ItemDetailObject(cursor.getString(reInfIndex)));

            } while (cursor.moveToNext());

        }

        assert cursor != null;
        cursor.close();
    }

    private void getBasicDetails() {

        mSelectedObject = JDictApplication.getDatabaseHelper().getWord().getBasicDetails(selectedObjectEntSeq, false);
    }

    private class LoadExamples extends AsyncTask<Void, Void, ArrayList<ItemDetailObject>> {

        final int limit = OFFSET_SIZE;
        ArrayList<ItemDetailObject> result;
        Context mContext;

        public LoadExamples(Context inContext) {
            super();
            mContext = inContext;
        }

        @Override
        protected ArrayList<ItemDetailObject> doInBackground(Void... params) {

            result = new ArrayList<ItemDetailObject>();

            Cursor cursor = null;
            Cursor cursorCount = null;

            if (allCount == 0) {

                cursorCount = JDictApplication.getDatabaseHelper().getExample().getExamplesCount(selectedObjectEntSeq);

                if (cursorCount != null && cursorCount.getCount() > 0) {

                    int countIndex = cursorCount.getColumnIndex("Count");
                    allCount += cursorCount.getInt(countIndex);
                }
            }

            if (cursorCount != null) {
                cursorCount.close();
            }

            cursor = JDictApplication.getDatabaseHelper().getExample().getExamples(mCurrentOffset, mSelectedObject.ID.get());

            if (cursor != null && cursor.getCount() > 0) {

                if (mCurrentOffset == 0) {
                    ItemDetailObject meaningHeader = new ItemDetailObject();
                    meaningHeader.SectionHeader.set(mContext.getResources().getString(R.string.examples));
                    result.add(meaningHeader);
                }

                int jp_a_index = cursor.getColumnIndex("sentence_jp_a");
                int eng_index = cursor.getColumnIndex("sentence_eng");
                int sentence_seq_index = cursor.getColumnIndex("sentence_seq");

                int i = 0;

                do {

                    ItemDetailObject obj = null;
                    if (i == limit) {

                        obj = new ItemDetailObject(true, allCount, limit + mCurrentOffset);

                    } else {
                        obj = new ItemDetailObject(cursor.getString(jp_a_index), cursor.getString(eng_index), true);
                        obj.setExample(cursor.getInt(sentence_seq_index));
                    }

                    result.add(obj);

                    i++;

                } while (cursor.moveToNext());
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

        protected void onPostExecute(ArrayList<ItemDetailObject> result) {

            if (mCurrentOffset > 0) {
                mAdapter.remove(mAdapter.getItem(mAdapter.getCount() - 1));
            }

            mAdapter.addAll(result);
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
