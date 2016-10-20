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
import com.mino.jdict.adapters.ExampleDetailsAdapter;
import com.mino.jdict.interfaces.IClearable;
import com.mino.jdict.logics.basic.Logic;
import com.mino.jdict.objects.activities.ExampleDetailObject;
import com.mino.jdict.objects.activities.ExampleDetailObjects;
import com.mino.jdict.objects.activities.ExampleObject;
import com.mino.jdict.objects.activities.ListObject;
import com.mino.jdict.objects.basic.GrammarObject;

import org.acra.ACRA;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import utils.database.JMDictHelper;
import utils.database.entry.Vocabulary;
import utils.grammar.FuriganaHelper;
import utils.grammar.GrammarDictionaries;
import utils.grammar.InputUtils;
import utils.other.ClassObject;
import utils.other.ShowUtils;
import utils.other.StaticObjectContainer;
import utils.other.StringUtils;
import utils.other.VocabularyPopup;

/**
 * Created by Dominik on 7/26/2015.
 */
public class ExampleDetailsLogic extends Logic implements IClearable {
    private ListView mListView;
    private ExampleDetailsAdapter mAdapter;
    private ArrayList<ExampleDetailObjects> objectList = new ArrayList<ExampleDetailObjects>();
    private int selectedSentenceSeq;
    private FuriganaHelper mFuriganaHelper;
    private LoadExampleWords mLoadExampleWords;
    private ExampleObject mSelectedObject;
    private ActionBar mActionBar;
    private VocabularyPopup mPopup = null;
    private ExampleDetailObjects noteObject = null;
    private boolean mToRefresh = false;
    private boolean mDetailsFilled = false;

    public void clear(IClearable.ClearanceLevel inClearanceLevel) {

        objectList.clear();
        mAdapter.clear();
        mToRefresh = true;
    }

    public void initialize() {

        if (mBundle == null) {

            Bundle extras = mActivity.getIntent().getExtras();
            selectedSentenceSeq = extras.getInt("SelectedObjectEntSeq");

            if (StaticObjectContainer.StaticObject != null && StaticObjectContainer.StaticObject instanceof ExampleObject) {
                mSelectedObject = (ExampleObject) StaticObjectContainer.StaticObject;
                selectedSentenceSeq = mSelectedObject.Detail.get().SentenceSeq.get();
                StaticObjectContainer.StaticObject = null;
            } else {
                StaticObjectContainer.StaticObject = null;
            }

        } else if (StaticObjectContainer.StaticObject instanceof ExampleObject) {
            mSelectedObject = (ExampleObject) StaticObjectContainer.StaticObject;
            selectedSentenceSeq = mSelectedObject.Detail.get().SentenceSeq.get();
        }

        Logic.refreshActivity(ExampleActivity.class, IClearable.ClearanceLevel.Soft);

        fillDetails();
    }

    public void onNewIntent(Intent intent) {

        Bundle extras = intent.getExtras();
        selectedSentenceSeq = extras.getInt("SelectedObjectEntSeq");

        fillDetails();
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putInt("selectedObjectEntSeq", selectedSentenceSeq);
        StaticObjectContainer.StaticObject = mSelectedObject;
    }

    public void resume() {

        ClassObject mObj = StaticObjectContainer.GetClassObject(this.hashCode());

        if (mObj != null && mObj.getObject1() != null && mObj.getObject1() instanceof ExampleObject) {
            mSelectedObject = (ExampleObject) mObj.getObject1();

            if (mSelectedObject != null && mSelectedObject.Detail.get() != null) {
                selectedSentenceSeq = mSelectedObject.Detail.get().SentenceSeq.get();
            }
        }

        if (mPopup != null) {
            mPopup.checkIfInVocabList(mSelectedObject.ID.get());
        }

        if (mToRefresh) {
            mToRefresh = false;

            if (mLoadExampleWords == null || mLoadExampleWords.getStatus() != AsyncTask.Status.RUNNING) {
                fillDetails();
            }
        }

        super.resume();
    }

    public void onBackPressed() {
        cancelLoadExampleWords();
    }

    public void onPause() {
        StaticObjectContainer.SetClassObject(this.hashCode(), new ClassObject(mSelectedObject));
        cancelLoadExampleWords();
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {

        selectedSentenceSeq = savedInstanceState.getInt("selectedObjectEntSeq");

        if (mListView == null || mListView.getCount() == 0) {
            fillDetails();
        }
    }

    public void onCreateOptionsMenu(Menu menu) {

        restoreActionBar();

        final View actionBarCustomView = mActionBar.getCustomView();

        ImageView mFavButton =
                (ImageView) actionBarCustomView.findViewById(R.id.favButton);

        TextView mTextViewHeader =
                (TextView) actionBarCustomView.findViewById(R.id.textViewHeader);

        mTextViewHeader.setText(mActivity.getString(R.string.example_header));

        mPopup = new VocabularyPopup(mActivity, mFavButton, Vocabulary.ElementType.Sentence);

        if (mSelectedObject != null) {
            mPopup.checkIfInVocabList(mSelectedObject.ID.get());
        }

        mFavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopup.showPopup(mSelectedObject.ID.get());
            }
        });
    }

    private void restoreActionBar() {
        mActionBar = mActivity.getActionBar();
        assert mActionBar != null;
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
        mActionBar.setDisplayShowHomeEnabled(true);
        mActionBar.setCustomView(R.layout.actionbar_favourites);
        mActionBar.setDisplayShowCustomEnabled(true);
    }

    private void getBasicDetails() {

        mSelectedObject = new ExampleObject(JDictApplication.getDatabaseHelper().getExample().getBasicExampleDetails(selectedSentenceSeq));
    }

    private void fillDetails() {
        //  Debug.startMethodTracing("FillDetailsExample");
        mDetailsFilled = false;

        if (mFuriganaHelper == null) {
            mFuriganaHelper = new FuriganaHelper();
        }

        if (selectedSentenceSeq == 0) {
            mActivity.onBackPressed();
        }

        if (mSelectedObject == null || mSelectedObject.Detail.get() == null) {
            getBasicDetails();
        }

        setAdapter();
        ListObject exampleHeader = new ListObject();
        exampleHeader.AddHeader.set(mActivity.getResources().getString(R.string.example));
        objectList.add(new ExampleDetailObjects(exampleHeader));

        final Pair<ExampleDetailObjects, ExampleDetailObjects> objects = fillFuriganaExample();

        ListObject meaningHeader = new ListObject();
        meaningHeader.AddHeader.set(mActivity.getResources().getString(R.string.meaning_section));
        objectList.add(new ExampleDetailObjects(meaningHeader));

        if (objects != null && objects.first.ExampleDetailObjectList.get().size() > 0) {
            objectList.add(new ExampleDetailObjects(objects.first.ExampleDetailObjectList.get().get(0).Translation.get()));
        }

        addHeader(R.string.note_section);
        addNote();

        mAdapter.notifyDataSetChanged();
        fillCompounds(objects);

        mDetailsFilled = true;

        // ...
        // stop tracing
        // Debug.stopMethodTracing();
    }

    private void addHeader(int stringID) {
        ExampleDetailObjects meaningHeader = new ExampleDetailObjects();
        meaningHeader.SectionHeader.set(mActivity.getResources().getString(stringID));
        objectList.add(meaningHeader);
    }

    private void fillCompounds(Pair<ExampleDetailObjects, ExampleDetailObjects> objects) {

        ListObject wordsHeader = new ListObject();
        wordsHeader.AddHeader.set(mActivity.getResources().getString(R.string.example_words));
        objectList.add(new ExampleDetailObjects(wordsHeader));

        (mLoadExampleWords = new LoadExampleWords(mActivity)).execute(objects);
    }

    // objects for details + objects to set ListObject for adapter onClick
    private Pair<ExampleDetailObjects, ExampleDetailObjects> fillFuriganaExample() {
        Cursor cursor;
        ExampleDetailObjects objects = new ExampleDetailObjects();
        ExampleDetailObjects wordsToReturn = new ExampleDetailObjects();

        if (mSelectedObject != null && mSelectedObject.Detail.get() != null && mSelectedObject.Detail.get().getRebKebList() != null && mSelectedObject.Detail.get().getRebKebList().size() > 0) {

            String jp_a = mSelectedObject.Detail.get().getRebKebList().get(0).first;
            // String eng = mSelectedObject.getDetail().getRebKebList().get(0).getR();

            cursor = JDictApplication.getDatabaseHelper().getExample().getExampleDetail(mSelectedObject.Detail.get().SentenceSeq.get());
            try {
                if (cursor != null && cursor.getCount() > 0) {

                    boolean wrongReb = false;
                    int jp_a_index = cursor.getColumnIndex("sentence_jp_a");
                    int order_in_sentence_index = cursor.getColumnIndex("orderInSentence");
                    int eng_index = cursor.getColumnIndex("sentence_eng");
                    int word_index = cursor.getColumnIndex("word");
                    int reb_index = cursor.getColumnIndex("rebSimple");
                    int entry_seq_index = cursor.getColumnIndex("entry_seq");
                    int readingChangedForm_index = cursor.getColumnIndex("readingChangedForm");
                    int certified_index = cursor.getColumnIndex("certified");
                    int sense_index = cursor.getColumnIndex("ID_sense");

                    wordsToReturn.ExampleDetailObjectList.add(new ExampleDetailObject(cursor.getString(eng_index)));

                    if (jp_a == null || jp_a.isEmpty()) {
                        jp_a = cursor.getString(jp_a_index);
                    }

                    objects.IsMain.set(true);

                    int order = 0;

                    do {

                        int order_in_sentence = cursor.getInt(order_in_sentence_index);

                        if (order_in_sentence == order && !wrongReb) {
                            continue;
                        } else {
                            order = order_in_sentence;
                        }

                        String word = cursor.getString(word_index);
                        String reb = cursor.getString(reb_index);
                        int entry_seq = cursor.getInt(entry_seq_index);
                        String changedForm = cursor.getString(readingChangedForm_index);
                        short certified = cursor.getShort(certified_index);
                        int idSense = cursor.getInt(sense_index);

                        if (changedForm != null && !changedForm.isEmpty()) {
                            wordsToReturn.ExampleDetailObjectList.add(new ExampleDetailObject(entry_seq, reb, word, changedForm, certified == 1, idSense));
                        } else {
                            wordsToReturn.ExampleDetailObjectList.add(new ExampleDetailObject(entry_seq, reb, word, certified == 1, idSense));
                        }

                        String notAWordPart = "";

                        if (!wrongReb) {
                            if (changedForm == null || changedForm.isEmpty()) {
                                if (!jp_a.contains(word)) continue;

                                while (!(jp_a.startsWith(word))) {

                                    notAWordPart += jp_a.substring(0, 1);
                                    jp_a = jp_a.substring(1, jp_a.length());
                                }

                                jp_a = jp_a.substring(word.length(), jp_a.length());
                            } else {

                                if (!jp_a.contains(changedForm)) continue;

                                while (!(jp_a.startsWith(changedForm))) {

                                    notAWordPart += jp_a.substring(0, 1);
                                    jp_a = jp_a.substring(1, jp_a.length());
                                }

                                jp_a = jp_a.substring(changedForm.length(), jp_a.length());
                            }


                            if (!notAWordPart.isEmpty()) {
                                ExampleDetailObject notAWordObj = new ExampleDetailObject();
                                notAWordObj.IsMain.set(true);
                                notAWordObj.addRebKeb(notAWordPart, notAWordPart);
                                objects.ExampleDetailObjectList.add(notAWordObj);
                            }
                        } else wrongReb = false;

                        ExampleDetailObject obj = new ExampleDetailObject(entry_seq);
                        obj.IsMain.set(true);

                        if (InputUtils.isStringAllKana(word) || (changedForm != null && !changedForm.isEmpty() && InputUtils.isStringAllKana(changedForm))) {

                            if (changedForm == null || changedForm.isEmpty()) {
                                mFuriganaHelper.matchFurigana(obj, word, word);
                            } else {

                                mFuriganaHelper.matchFurigana(obj, changedForm, changedForm);
                            }
                        } else if (changedForm == null || changedForm.isEmpty() || changedForm.contentEquals(reb)) {

                            // ケー番 problem
                            if (word.contains("ー") && !reb.contains("ー")) {

                                String copyKana = "";

                                for (int i = 0; i < word.length(); i++) {

                                    String currentCharacter = word.substring(i, i + 1);

                                    if (InputUtils.isKana(currentCharacter)) {
                                        copyKana += currentCharacter;
                                    } else {
                                        copyKana += reb.substring(i, reb.length());
                                        reb = copyKana;
                                        break;
                                    }
                                }
                            }

                            mFuriganaHelper.matchFurigana(obj, word, reb);
                        } else if (InputUtils.isStringAllKanji(word)) {
                            mFuriganaHelper.matchFurigana(obj, word, reb);
                        } else
                            // na-adjective
                            if (changedForm.length() > 1 && word.length() > 0
                                    && changedForm.substring(changedForm.length() - 2, changedForm.length() - 1).equals(word.substring(word.length() - 1, word.length()))
                                    && InputUtils.isStringAllKanji(word.substring(word.length() - 1, word.length()))
                                    && changedForm.substring(changedForm.length() - 1, changedForm.length()).equals("な")) {

                                mFuriganaHelper.matchFurigana(obj, word + "な", reb + "な");
                            } else {
                                int matchSize = 0;

                                final int len = changedForm.length();
                                final int len2 = Math.min(len, word.length());
                                int start;

                                // może być prefiks np お który musimy uciąć, bo nie ma go w word
                                String prefix = "";

                                if (len > 0) {
                                    String first = changedForm.substring(0, 1);

                                    if (len != word.length() && InputUtils.isKana(first) && !InputUtils.getRomaji(first, false).equals(InputUtils.getRomaji(reb.substring(0, 1), false)) && !InputUtils.getRomaji(first, false).equals(InputUtils.getRomaji(word.substring(0, 1), false))) {
                                        for (start = 0; start < len; start++) {

                                            String currentCharacter = changedForm.substring(start, start + 1);

                                            if (InputUtils.isKana(currentCharacter)) {

                                                // word i changedForm mogą mieć też wspólnie ten sam prefiks w hiraganie...
                                                if (word.length() > start) {
                                                    String currentCharacterWord = word.substring(start, start + 1);

                                                    if (currentCharacter.contentEquals(currentCharacterWord)) {
                                                        break;
                                                    }
                                                }

                                                prefix += currentCharacter;

                                            } else break;
                                        }
                                    }
                                }

                                String newReb = prefix + reb; // oau
                                word = prefix + word;

                                for (int i = 0; i < len2; i++) {

                                    String wordSubstr = word.substring(i, i + 1);
                                    String changedFormSubstr = changedForm.substring(i, i + 1);

                                    // fix: changedForm can have other kanji variant
                                    if ((!InputUtils.isKana(wordSubstr) && !InputUtils.isKana(changedFormSubstr)) || InputUtils.getRomaji(wordSubstr, false).contentEquals(InputUtils.getRomaji(changedFormSubstr, false))) {
                                        matchSize++;
                                    } else break;
                                }

                                if (matchSize > 0) {

                                    String rebChanged = newReb.substring(0, matchSize + newReb.length() - word.length()) + changedForm.substring(matchSize, changedForm.length());

                                    wrongReb = checkReb(changedForm, rebChanged);
                                    if (!wrongReb) {

                                        newReb = newReb.substring(0, matchSize + newReb.length() - word.length()) + changedForm.substring(matchSize, changedForm.length());
                                    } else if (changedForm.length() < word.length() && changedForm.length() > 0 // 取り扱い vs 取扱い
                                            && word.length() > 0 && changedForm.charAt(0) == word.charAt(0)
                                            && word.charAt(word.length() - 1) == changedForm.charAt(changedForm.length() - 1)) {

                                        boolean hasAll = true;
                                        for (int i = 0; i < changedForm.length(); i++) {
                                            if (!word.contains(String.valueOf(changedForm.charAt(i)))) {
                                                hasAll = false;
                                                break;
                                            }
                                        }

                                        if (hasAll) {
                                            wrongReb = false;
                                        }
                                    }
                                }
                                if (matchSize == 0 || wrongReb) // changedForm and simpleForm are hard to match -> let's find out the simple form of ChangedForm and try to change the form of reading by applying grammar
                                {
                                    ArrayList<GrammarObject> simpleForms = InputUtils.getPossibleSimpleForms(changedForm);
                                    int bestMatchSize = 0;
                                    int bestMatchPos = -1;

                                    for (int j = 0; j < simpleForms.size(); j++) {

                                        String match = simpleForms.get(j).getSimpleForm();

                                        String LCS = StringUtils.longestSubstring(InputUtils.getRomaji(match, false), InputUtils.getRomaji(newReb, false));
                                        int mSize = LCS.length();
                                        if (mSize > bestMatchSize) {

                                            bestMatchSize = mSize;
                                            bestMatchPos = j;

                                        } else if (mSize == bestMatchSize) {
                                            bestMatchPos = j;
                                        }
                                    }

                                    if (bestMatchPos > -1) {

                                        GrammarObject foundObj = simpleForms.get(bestMatchPos);

                                        String[] grammarForms = foundObj.getGrammarChain().split(" < ");
                                        String currentWordType = foundObj.getWordType();

                                        if (grammarForms.length > 0) {
                                            for (String grammarForm : grammarForms) {

                                                if (currentWordType.isEmpty() || grammarForm.isEmpty()) {

                                                    // 申し込み　申込
                                                    if (StringUtils.longestSubstring(changedForm, word).length() > 0) {
                                                        wrongReb = false;
                                                        newReb = reb;
                                                    }
                                                    break;
                                                }

                                                List<GrammarObject> grammar = GrammarDictionaries.GRAMMAR_MAP.get(grammarForm);

                                                for (GrammarObject grammarObj : grammar) {

                                                    if (grammarObj.getWordType().equals(currentWordType) || (grammarObj.getWordType().equals("aux")) && changedForm.contains(grammarObj.getChangedForm())) {
                                                        newReb = newReb.substring(0, newReb.length() - grammarObj.getSimpleForm().length()) + grammarObj.getChangedForm();

                                                        wrongReb = checkReb(changedForm, newReb);

                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                if (!wrongReb) {
                                    mFuriganaHelper.matchFurigana(obj, changedForm, newReb);
                                }
                            }

                        objects.ExampleDetailObjectList.add(obj);


                    } while (cursor.moveToNext());

                    if (!jp_a.isEmpty()) {

                        ExampleDetailObject notAWordObj = new ExampleDetailObject();
                        notAWordObj.IsMain.set(true);
                        notAWordObj.addRebKeb(jp_a, jp_a);
                        objects.ExampleDetailObjectList.add(notAWordObj);
                    }

                    objectList.add(objects);
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

            //mAdapter.notifyDataSetChanged();

        }

        return new Pair<ExampleDetailObjects, ExampleDetailObjects>(wordsToReturn, objects);
    }

    private boolean checkReb(String changedForm, String newReb) {
        // sprawdzamy czy na pewno reb który jest z zapytania jest dobrym rebem (a raczej szukamy przesłanki ku temu wyszukując zabłąkanej hiragany, której nie ma w rebie...)

        if (!InputUtils.isStringAllKana(newReb)) return true;
        String newRebHiragana = InputUtils.getKana(InputUtils.getRomaji(newReb, false), InputUtils.KanaType.Hiragana);

        String changedFormWithoutKanji = "";
        for (int i = 0; i < changedForm.length(); i++) {

            String str = changedForm.substring(i, i + 1);
            if (InputUtils.isKana(str)) {
                changedFormWithoutKanji += str;
            }
        }

        changedFormWithoutKanji = InputUtils.getKana(InputUtils.getRomaji(changedFormWithoutKanji, false), InputUtils.KanaType.Hiragana);

        for (int i = 0; i < changedFormWithoutKanji.length(); i++) {

            String str = changedFormWithoutKanji.substring(i, i + 1);

            if (!newRebHiragana.contains(str)) {

                return true;
            }
        }
        return false;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (mSelectedObject != null) {
            AddNoteLogic.noteResult(requestCode, resultCode, data, mSelectedObject.ID.get(), noteObject, mActivity.getString(R.string.addnote), mAdapter, Vocabulary.ElementType.Sentence);
        }
    }

    private void addNote() {
        ExampleDetailObjects noteObject = new ExampleDetailObjects();

        Cursor c = JDictApplication.getDatabaseHelper().getVocabulary().getNote(selectedSentenceSeq, Vocabulary.ElementType.Sentence);

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


    private void setAdapter() {

        objectList.clear();

        if (mAdapter != null) {
            mAdapter.clear();
            mAdapter.notifyDataSetChanged();
        }

        mAdapter = new ExampleDetailsAdapter(mActivity, objectList);
        mListView = (ListView) mActivity.findViewById(R.id.meaningListView);
        mListView.setAdapter(mAdapter);

        mListView.setClickable(true);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                Object o = mListView.getItemAtPosition(position);
                if (o instanceof ExampleDetailObjects && ((ExampleDetailObjects) o).IsNote.get()) {

                    noteObject = ((ExampleDetailObjects) o);
                    Intent i = new Intent(mActivity.getBaseContext(), AddNoteActivity.class);
                    i.putExtra("text", noteObject.Note.get());

                    i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    mActivity.startActivityForResult(i, 1);
                } else if (o instanceof ExampleDetailObjects && ((ExampleDetailObjects) o).ListObject.notNull()) {
                    goToItemDetails(((ExampleDetailObjects) o).ListObject.get());
                }
            }
        });
    }

    private void cancelLoadExampleWords() {
        if (mLoadExampleWords != null && mLoadExampleWords.getStatus() == AsyncTask.Status.RUNNING) {
            try {
                mLoadExampleWords.cancel(true);
                mToRefresh = true;
            } catch (Exception ex) {
                ACRA.getErrorReporter().handleSilentException(ex);
            }
        }

        mLoadExampleWords = null;
    }

    public void goToItemDetails(ListObject inListObj) {
        if (inListObj != null && inListObj.ID.get() > 0) {

            if (mLoadExampleWords != null && mLoadExampleWords.getStatus() == AsyncTask.Status.RUNNING) {
                return;
            }

            if (!mDetailsFilled) return;

            StaticObjectContainer.StaticObject = inListObj;

            if (inListObj != null) {
                //ShowUtils.showEntry(inListObj.getEntSeq(), mDictHelper, mActivity.getBaseContext());
                ShowUtils.showEntry(inListObj, mActivity.getBaseContext());
            }
        }
    }

    private class LoadExampleWords extends AsyncTask<Pair<ExampleDetailObjects, ExampleDetailObjects>, Void, ArrayList<ExampleDetailObjects>> {

        JMDictHelper db;
        ArrayList<ExampleDetailObjects> result;
        Context mContext;

        public LoadExampleWords(Context inContext) {
            super();
            mContext = inContext;
            result = new ArrayList<ExampleDetailObjects>();
        }

        @Override
        protected ArrayList<ExampleDetailObjects> doInBackground(Pair<ExampleDetailObjects, ExampleDetailObjects>... params) {

            db = JDictApplication.getDatabaseHelper();
            Pair<ExampleDetailObjects, ExampleDetailObjects> objects = params[0];

            ArrayList<Integer> ent_seqs = new ArrayList<Integer>();

            for (ExampleDetailObject obj : objects.first.ExampleDetailObjectList.get()) {

                if (obj.Translation.notEmpty()) {
                    continue;
                }

                ent_seqs.add(obj.EntSeq.get());
            }

            Map<Integer, ListObject> lObjMap = JDictApplication.getDatabaseHelper().getWord().getBasicDetails2(ent_seqs, false);


            for (ExampleDetailObject obj : objects.first.ExampleDetailObjectList.get()) {

                if (obj.Translation.notEmpty()) {
                    continue;
                }

                ListObject lObj = lObjMap.get(obj.EntSeq.get());

                lObj.SenseID.set(obj.IDSense.get());

                if (obj.IsCertified.get()) lObj.IsCertified.set(true);

                if (obj.ChangedKeb.notEmpty()) {

                    ArrayList<GrammarObject> simpleForms = InputUtils.getPossibleSimpleForms(obj.ChangedKeb.get());

                    if (simpleForms != null && simpleForms.size() > 0) {

                        for (GrammarObject gObj : simpleForms) {

                            if (gObj.getSimpleForm().contentEquals(obj.Keb.get()) || gObj.getSimpleForm().contentEquals(obj.Reb.get())) {
                                lObj.GrammarChain.set(gObj.getGrammarChain());

                                if (!gObj.getGrammarChain().isEmpty()) {
                                    lObj.IsConjugated.set(true);
                                }

                                break;
                            }
                        }
                    }
                }

                for (ExampleDetailObject obj2 : objects.second.ExampleDetailObjectList.get()) {
                    if (obj.EntSeq.get().equals(obj2.EntSeq.get())) {
                        obj2.ListObj.set(lObj);
                    }
                }

                result.add(new ExampleDetailObjects(lObj));
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected void onPostExecute(ArrayList<ExampleDetailObjects> result) {

            for (ExampleDetailObjects objs : result) {
                objectList.add(objs);
            }

            mAdapter.notifyDataSetChanged();
        }
    }

}
