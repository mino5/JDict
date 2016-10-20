package com.mino.jdict.logics.activities;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mino.jdict.JDictApplication;
import com.mino.jdict.R;
import com.mino.jdict.activities.ExampleActivity;
import com.mino.jdict.activities.ExampleDetailsActivity;
import com.mino.jdict.activities.ItemDetailsActivity;
import com.mino.jdict.activities.KanjiActivity;
import com.mino.jdict.activities.KanjiDetailsActivity;
import com.mino.jdict.activities.MainActivity;
import com.mino.jdict.activities.VocabularyActivity;
import com.mino.jdict.adapters.VocabularyAdapter;
import com.mino.jdict.interfaces.IClearable;
import com.mino.jdict.logics.basic.Logic;
import com.mino.jdict.models.basic.SearchValues;
import com.mino.jdict.objects.activities.ExampleObject;
import com.mino.jdict.objects.activities.ExampleRecentObject;
import com.mino.jdict.objects.activities.KanjiBaseObject;
import com.mino.jdict.objects.activities.KanjiDetailObject;
import com.mino.jdict.objects.activities.KanjiObject;
import com.mino.jdict.objects.activities.KanjiRecentObject;
import com.mino.jdict.objects.activities.ListObject;
import com.mino.jdict.objects.activities.RecentObject;
import com.mino.jdict.objects.activities.VocabularyObject;

import org.acra.ACRA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import utils.database.JMDictHelper;
import utils.database.entry.Vocabulary;
import utils.other.StaticObjectContainer;

/**
 * Created by Dominik on 7/25/2015.
 */
public class VocabularyLogic extends Logic implements IClearable {

    private static boolean mEditState = false;
    private ArrayList<VocabularyObject> objectList = new ArrayList<VocabularyObject>();
    private VocabularyAdapter mAdapter;
    private ListView mListView;
    private Button mButton;
    private ActionBar mActionBar;
    private TextView mActionBarHeader;
    private int mCurrentID = 0;
    private Boolean mToRefresh = false;
    private LoadFolders mLoadFolders;
    private VocabularyObject mCurrentObject = null;

    private class State {

        private int mCurrentID;
        private Boolean mEditState;
        private VocabularyObject mObject;

        public State(Boolean inEditState, int inCurrentID, VocabularyObject inObject) {

            mCurrentID = inCurrentID;
            mEditState = inEditState;
            mObject = inObject;
        }

        public int getCurrentID() {
            return mCurrentID;
        }

        public Boolean getEditState() {
            return mEditState;
        }

        public VocabularyObject getObject() {
            return mObject;
        }
    }

    public void initialize() {

        mEditState = false;
        objectList.clear();

        setAdapter();

        if (StaticObjectContainer.StaticObject != null && StaticObjectContainer.StaticObject instanceof State) {
            State state = (State) StaticObjectContainer.StaticObject;
            mEditState = state.getEditState();
            mCurrentID = state.getCurrentID();
            mCurrentObject = state.getObject();

            StaticObjectContainer.StaticObject = null;
        }

        (mLoadFolders = new LoadFolders(mActivity, mButton)).execute(new VocabularyState(mCurrentID, mEditState));
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {

        StaticObjectContainer.StaticObject = new State(mEditState, mCurrentID, mCurrentObject);
    }

    private void getTask(AsyncTask task) {
        if (task != null && task.getStatus() == AsyncTask.Status.RUNNING) {
            try {
                task.get();
            } catch (Exception ex) {
                ACRA.getErrorReporter().handleSilentException(ex);
            }
        }
    }

    private void reloadFolders() {
        objectList.clear();
        mAdapter.notifyDataSetChanged();

        if (mLoadFolders != null) {
            getTask(mLoadFolders);
        }

        if ((mLoadFolders != null ? mLoadFolders.getStatus() : null) != AsyncTask.Status.RUNNING) {
            (mLoadFolders = new LoadFolders(mActivity.getBaseContext(), mButton)).execute(new VocabularyState(mCurrentID, mEditState));
        }
    }

    public void clear(IClearable.ClearanceLevel inClearanceLevel) {

        objectList.clear();
        mAdapter.clear();
        mAdapter.notifyDataSetChanged();
        mToRefresh = true;
    }

    public void resume() {

        if (mToRefresh) {
            mToRefresh = false;

            reloadFolders();
        }

        super.resume();
    }

    private void setAdapter() {


        mAdapter = new VocabularyAdapter(mActivity, R.layout.vocabulary_list_item, objectList);
        mListView = (ListView) mActivity.findViewById(R.id.listVocabularyLists);
        mListView.setAdapter(mAdapter);

        mListView.setClickable(true);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                final VocabularyObject o = (VocabularyObject) mListView.getItemAtPosition(position);

                if (o.Element.get() != null) {

                    if (o.EditTypeProp.get() == VocabularyObject.EditType.Remove) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                        builder.setTitle(mActivity.getString(R.string.delete_entry));

                        String message = "";

                        if (o.Element.get().KanjiObject.get() != null) {
                            message += o.Element.get().KanjiObject.get().Character.get() + " " + o.Element.get().KanjiObject.get().Meaning.get();
                        } else if (o.Element.get().ExampleObject.get() != null) {
                            message += o.Element.get().ExampleObject.get().Detail.get().getReb(true);
                        } else if (o.Element.get().ListObject.get() != null) {
                            message += o.Element.get().ListObject.get().getFirst() + " " + o.Element.get().ListObject.get().getSecond();
                        }

                        builder.setMessage(message);

                        builder.setPositiveButton(mActivity.getString(R.string.OK), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                JDictApplication.getDatabaseHelper().getVocabulary().removeEntryFromList(mCurrentID, o.Element.get());
                                Toast.makeText(mActivity, mActivity.getString(R.string.entry_deleted), Toast.LENGTH_SHORT).show();


                                objectList.remove(o);
                                //changeEditState();
                                //reloadFolders();
                                Logic.refreshActivity(VocabularyActivity.class, IClearable.ClearanceLevel.Soft);
                                mAdapter.notifyDataSetChanged();
                            }
                        });

                        builder.setNegativeButton(mActivity.getString(R.string.Cancel), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();
                    } else if (o.Element.get().KanjiObject.get() != null) {
                        Intent i = new Intent(mActivity.getBaseContext(), KanjiDetailsActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        Bundle mBundle = new Bundle();

                        KanjiDetailObject kdobj = new KanjiDetailObject(o.Element.get().KanjiObject.get(), false, false);
                        mBundle.putParcelable("PAR", kdobj);
                        i.putExtras(mBundle);

                        KanjiRecentObject recentObj = new KanjiRecentObject.KanjiRecentObjectFactory().factory();
                        KanjiObject kanjiObject = new KanjiObject(o.Element.get().KanjiObject.get());

                        SearchValues values = new SearchValues();
                        ArrayList<Boolean> cbValues = new ArrayList<Boolean>();
                        cbValues.add(false);
                        values.setCheckBoxValues(cbValues);

                        recentObj.bind(kanjiObject, values);

                        JDictApplication.getDatabaseHelper().getWord().insertRecentWords(recentObj);
                        Logic.refreshActivity(KanjiActivity.class, IClearable.ClearanceLevel.Soft);

                        mActivity.startActivity(i);
                    } else if (o.Element.get().ExampleObject.get() != null) {

                        ExampleObject obj = o.Element.get().ExampleObject.get();

                        Intent i = new Intent(mActivity.getBaseContext(), ExampleDetailsActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        i.putExtra("SelectedObjectEntSeq", obj.ID.get());
                        StaticObjectContainer.StaticObject = ((ExampleObject) obj);

                        ExampleRecentObject recentObj = new ExampleRecentObject.ExampleRecentObjectFactory().factory();

                        SearchValues values = new SearchValues();
                        ArrayList<Boolean> cbValues = new ArrayList<Boolean>();
                        cbValues.add(false);
                        values.setCheckBoxValues(cbValues);

                        recentObj.bind(obj, values);
                        JDictApplication.getDatabaseHelper().getWord().insertRecentWordsV2(recentObj);
                        Logic.refreshActivity(ExampleActivity.class, IClearable.ClearanceLevel.Soft);

                        mActivity.startActivity(i);
                    } else if (o.Element.get().ListObject.get() != null) {
                        RecentObject recentObj = new RecentObject.RecentObjectFactory().factory();
                        ListObject lObj = o.Element.get().ListObject.get();

                        SearchValues values = new SearchValues();
                        ArrayList<Boolean> cbValues = new ArrayList<Boolean>();
                        cbValues.add(false);
                        values.setCheckBoxValues(cbValues);
                        recentObj.init(lObj.getFirst(), lObj.getSecond(), lObj.ID.get(), "", values);

                        if (!recentObj.getPureKeb().isEmpty() || !recentObj.getPureReb().isEmpty()) {
                            JDictApplication.getDatabaseHelper().getWord().insertRecentWords(recentObj);
                            Logic.refreshActivity(MainActivity.class, IClearable.ClearanceLevel.Soft);

                            Intent i = new Intent(mActivity.getBaseContext(), ItemDetailsActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            i.putExtra("SelectedObjectEntSeq", lObj.ID.get());

                            mActivity.startActivity(i);
                        }
                    }

                } else if (o.HeaderText.get().isEmpty() && o.Element.get() == null) {
                    switch (o.EditTypeProp.get()) {
                        case Remove:

                            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                            builder.setTitle(mActivity.getString(R.string.delete_list));
                            String message = o.Name.get();

                            if (o.SublistCount.get() > 0) {
                                message += " " + mActivity.getString(R.string.with) + " " + o.SublistCount.get() + " " + mActivity.getString(R.string.sublists).toLowerCase();
                            }

                            builder.setMessage(message);

                            builder.setPositiveButton(mActivity.getString(R.string.OK), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {


                                    JDictApplication.getDatabaseHelper().getVocabulary().removeFolder(o.ID_Vocablist.get());
                                    Toast.makeText(mActivity, mActivity.getString(R.string.set_deleted), Toast.LENGTH_SHORT).show();

                                    int pos = objectList.indexOf(o);
                                    objectList.remove(o);

                                    // ostatnia subLista - kasujemy Header
                                    if (pos > 0 && !objectList.get(pos - 1).HeaderText.get().isEmpty()) {
                                        objectList.remove(pos - 1);
                                    }

                                    //reloadFolders();
                                    Logic.refreshActivity(VocabularyActivity.class, IClearable.ClearanceLevel.Soft);
                                    mAdapter.notifyDataSetChanged();
                                }
                            });

                            builder.setNegativeButton(mActivity.getString(R.string.Cancel), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            AlertDialog alert = builder.create();
                            alert.show();

                            break;

                        case Add:

                            final EditText input = new EditText(mActivity);

                            InputMethodManager inputMethodManager = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                            if (inputMethodManager != null) {
                                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                            }

                            input.requestFocus();

                            new AlertDialog.Builder(mActivity)
                                    .setTitle(mActivity.getString(R.string.create_new_list))
                                    .setMessage(mActivity.getString(R.string.enter_list_name))
                                    .setView(input)
                                    .setPositiveButton(mActivity.getString(R.string.OK), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            Editable value = input.getText();
                                            long res;

                                            if (mCurrentID == 0) {
                                                res = JDictApplication.getDatabaseHelper().getVocabulary().insertFolder(value.toString());
                                            } else {
                                                res = JDictApplication.getDatabaseHelper().getVocabulary().insertFolder(value.toString(), mCurrentID);
                                            }

                                            hideKeyboard(input.getWindowToken());

                                            VocabularyObject vocabObject = new VocabularyObject((int) res, mCurrentID, value.toString(), null, false, 0, 0, VocabularyObject.EditType.NonEditable);
                                            changeEditState();

                                            if (objectList.size() > 1) {
                                                VocabularyObject next = objectList.get(0);

                                                if (!next.HeaderText.get().equals(mActivity.getString(R.string.lists)) && !next.HeaderText.get().equals(mActivity.getString(R.string.sublists))) {

                                                    if (mCurrentID == 0) {
                                                        objectList.add(0, new VocabularyObject(mActivity.getString(R.string.lists)));
                                                    } else {
                                                        VocabularyObject sublists = new VocabularyObject(mActivity.getString(R.string.sublists));
                                                        sublists.IsSubLists.set(true);
                                                        objectList.add(0, sublists);
                                                    }
                                                }
                                            }

                                            objectList.add(1, vocabObject);

                                            mAdapter.notifyDataSetChanged();
                                            Logic.refreshActivity(VocabularyActivity.class, IClearable.ClearanceLevel.Soft);
                                        }
                                    }).setNegativeButton(mActivity.getString(R.string.Cancel), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {


                                    dialog.dismiss();
                                    hideKeyboard(input.getWindowToken());
                                }
                            }).show();

                            break;


                        case NonEditable:

                            mActionBarHeader.setText(o.Name.get());
                            o.Parent.set(mCurrentObject);

                            mCurrentObject = o;
                            mCurrentID = o.ID_Vocablist.get();
                            reloadFolders();

                            break;
                    }
                }

            }
        });

    }

    private void hideKeyboard(IBinder windowToken) {

        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(
                Context.INPUT_METHOD_SERVICE);

        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        imm.hideSoftInputFromWindow(windowToken, 0);
    }

    private void restoreActionBar() {
        mActionBar = mActivity.getActionBar();
        mActionBar.setTitle(mActivity.getString(R.string.notepad));
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
        mActionBar.setDisplayShowHomeEnabled(true);
        mActionBar.setCustomView(R.layout.vocabulary_actionbar_layout);
        mActionBar.setDisplayShowCustomEnabled(true);
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        restoreActionBar();
        final View actionBarCustomView = mActionBar.getCustomView();

        mActionBarHeader =
                (TextView) actionBarCustomView.findViewById(R.id.actionBarHeader);

        mButton =
                (Button) actionBarCustomView.findViewById(R.id.edit);

        setActionBarText();

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changeEditState();
            }
        });

        return true;
    }

    private void changeEditState() {
        mEditState = !mEditState;
        setActionBarText();
        //reloadFolders();
        VocabularyObject.EditType mode = mEditState ? VocabularyObject.EditType.Remove : VocabularyObject.EditType.NonEditable;
        for (VocabularyObject vObj : objectList) {
            vObj.EditTypeProp.set(mode);
        }

        if (mEditState) {
            objectList.add(0, new VocabularyObject(mActivity.getString(R.string.create)));
            objectList.add(1, new VocabularyObject(mActivity.getString(R.string.create_new_list), VocabularyObject.EditType.Add));
        } else {
            objectList.remove(0);
            objectList.remove(0);
        }

        mAdapter.notifyDataSetChanged();
    }

    private void setActionBarText() {
        if (mEditState) {
            mButton.setText(mActivity.getString(R.string.done));
        } else {
            mButton.setText(mActivity.getString(R.string.edit));
        }

        if (mCurrentID == 0) {
            mActionBarHeader.setText(mActivity.getString(R.string.vocab_lists));
        } else if (mCurrentObject != null) {
            mActionBarHeader.setText(mCurrentObject.Name.get());
        }
    }

    public boolean onBackPressed() {

        if (mEditState) {
            mEditState = false;
            setActionBarText();
            reloadFolders();
        } else if (mCurrentID == 0) {
            //super.onBackPressed();
            return true;
        } else {
            mCurrentID = mCurrentObject.IDVocablist.get();

            if (mCurrentID > 0) {
                mCurrentObject = mCurrentObject.Parent.get();
                // get new currentObject
            }

            setActionBarText();
            reloadFolders();
        }

        return false;
    }

    private class VocabularyState {

        int mID;
        Boolean mEditState;

        public VocabularyState(int ID, Boolean editState) {

            mID = ID;
            mEditState = editState;
        }

        public Boolean getEditState() {
            return mEditState;
        }

        public int getID() {
            return mID;
        }
    }

    private class LoadFolders extends AsyncTask<VocabularyState, Void, ArrayList<VocabularyObject>> {

        JMDictHelper db;
        ArrayList<VocabularyObject> result;
        Context mContext;
        Button mButton;

        public LoadFolders(Context inContext, Button inButton) {
            super();
            mContext = inContext;
            mButton = inButton;
        }

        @Override
        protected void onPreExecute() {

            if (mButton != null) {
                mButton.setEnabled(false);
                mButton.setClickable(false);
            }
        }

        @Override
        protected ArrayList<VocabularyObject> doInBackground(VocabularyState... params) {

            db = JDictApplication.getDatabaseHelper();
            result = new ArrayList<VocabularyObject>();
            VocabularyState state = params[0];
            Boolean editState = state.getEditState();
            int ID = state.getID();

            if (editState) {
                result.add(new VocabularyObject(mActivity.getString(R.string.create)));
                result.add(new VocabularyObject(mActivity.getString(R.string.create_new_list), VocabularyObject.EditType.Add));
            }

            if (ID == 0) {
                result.add(new VocabularyObject(mActivity.getString(R.string.lists)));
            }

            Cursor cursor = null;

            if (ID == 0) {
                cursor = db.getVocabulary().getFolders();
            } else {
                cursor = db.getVocabulary().getSubFolders(ID);
            }

            VocabularyObject.EditType mode = editState ? VocabularyObject.EditType.Remove : VocabularyObject.EditType.NonEditable;

            if (cursor != null && cursor.getCount() > 0) {

                if (ID > 0) {
                    VocabularyObject sublists = new VocabularyObject(mActivity.getString(R.string.sublists));
                    sublists.IsSubLists.set(true);
                    result.add(sublists);
                }

                int idIndex = cursor.getColumnIndex("ID_VocabList");
                int idVocabIndex = cursor.getColumnIndex("IDVocabList");
                int nameIndex = cursor.getColumnIndex("Name");
                int creationdateIndex = cursor.getColumnIndex("Creationdate");
                int learnGroupIndex = cursor.getColumnIndex("IsLearnGroup");
                int entryCountIndex = cursor.getColumnIndex("EntryCount");
                int sublistCountIndex = cursor.getColumnIndex("SublistCount");

                do {
                    VocabularyObject vocabObject = new VocabularyObject(cursor.getInt(idIndex), cursor.getInt(idVocabIndex), cursor.getString(nameIndex), cursor.getString(creationdateIndex), cursor.getInt(learnGroupIndex) != 0, cursor.getInt(entryCountIndex), cursor.getInt(sublistCountIndex), mode);
                    result.add(vocabObject);

                } while (cursor.moveToNext());

            }

            if (cursor != null) {
                cursor.close();
            }

            // entries
            if (ID > 0) {

                boolean addedSomething = false;
                Cursor cursorEntries = db.getVocabulary().getEntries(ID, Vocabulary.ElementType.Entry);

                if (cursorEntries.getCount() > 0) {
                    VocabularyObject vObj1 = new VocabularyObject(mActivity.getString(R.string.entries_vocab));
                    result.add(vObj1);
                    addVocabElement(mode, cursorEntries);

                    addedSomething = true;
                }

                Cursor cursorKanji = db.getVocabulary().getEntries(ID, Vocabulary.ElementType.Kanji);
                if (cursorKanji.getCount() > 0) {

                    VocabularyObject vObj2 = new VocabularyObject(mActivity.getString(R.string.kanji_vocab));
                    result.add(vObj2);
                    addVocabElement(mode, cursorKanji);

                    addedSomething = true;
                }

                Cursor cursorExamples = db.getVocabulary().getEntries(ID, Vocabulary.ElementType.Sentence);
                if (cursorExamples.getCount() > 0) {

                    VocabularyObject vObj3 = new VocabularyObject(mActivity.getString(R.string.examples_vocab));
                    result.add(vObj3);
                    addVocabElement(mode, cursorExamples);

                    addedSomething = true;
                }

                // show entries Header when nothing in list
                if (!addedSomething) {
                    VocabularyObject vObj1 = new VocabularyObject(mActivity.getString(R.string.entries_vocab));
                    result.add(vObj1);
                }
            }

            return result;
        }

        private void addVocabElement(VocabularyObject.EditType mode, Cursor cursorEntries) {

            Map<Integer, VocabularyObject> tempListEntry = new HashMap<Integer, VocabularyObject>();
            Map<Integer, VocabularyObject> tempListKanji = new HashMap<Integer, VocabularyObject>();
            Map<Integer, VocabularyObject> tempListExample = new HashMap<Integer, VocabularyObject>();
            ArrayList<Integer> entIds = new ArrayList<Integer>();
            ArrayList<Integer> charIds = new ArrayList<Integer>();
            ArrayList<Integer> sentenceIds = new ArrayList<Integer>();

            if (cursorEntries != null && cursorEntries.getCount() > 0) {

                int charIndex = cursorEntries.getColumnIndex("character_seq");
                int entSeqIndex = cursorEntries.getColumnIndex("ent_seq");
                int sentenceIndex = cursorEntries.getColumnIndex("sentence_seq");
                int cdIndex = cursorEntries.getColumnIndex("CreationDate");
                int lastGuessIndex = cursorEntries.getColumnIndex("LastGuess");
                int lastShownIndex = cursorEntries.getColumnIndex("LastShown");

                do {
                    int charSeq = cursorEntries.getInt(charIndex);
                    int sentenceSeq = cursorEntries.getInt(sentenceIndex);
                    int entSeq = cursorEntries.getInt(entSeqIndex);
                    String cd = cursorEntries.getString(cdIndex);
                    int lastGuess = cursorEntries.getInt(lastGuessIndex);
                    String lastShown = cursorEntries.getString(lastShownIndex);

                    VocabularyObject vObj = new VocabularyObject(charSeq, sentenceSeq, entSeq, cd, lastGuess, lastShown, mode);

                    if (vObj.Element.get().EntSeq.get() > 0) {

                        tempListEntry.put(vObj.Element.get().EntSeq.get(), vObj);
                        entIds.add(vObj.Element.get().EntSeq.get());
                    } else if (vObj.Element.get().CharacterSeq.get() > 0) {

                        tempListKanji.put(vObj.Element.get().CharacterSeq.get(), vObj);
                        charIds.add(vObj.Element.get().CharacterSeq.get());
                    } else if (vObj.Element.get().SentenceSeq.get() > 0) {

                        tempListExample.put(vObj.Element.get().SentenceSeq.get(), vObj);
                        sentenceIds.add(vObj.Element.get().SentenceSeq.get());
                    }

                } while (cursorEntries.moveToNext());
            }

            if (cursorEntries != null) {
                cursorEntries.close();
            }

            Map<Integer, ListObject> lObjMap = JDictApplication.getDatabaseHelper().getWord().getBasicDetails2(entIds, false);
            for (int entSeq : tempListEntry.keySet()) {
                if (lObjMap.containsKey(entSeq)) {
                    VocabularyObject vObj = tempListEntry.get(entSeq);
                    vObj.Element.get().ListObject.set(lObjMap.get(entSeq));
                    result.add(vObj);
                }
            }

            for (int characterSeq : tempListKanji.keySet()) {
                KanjiBaseObject kanjiObject = JDictApplication.getDatabaseHelper().getKanji().getBasicKanjiDetails(characterSeq);

                if (kanjiObject != null) {
                    VocabularyObject vObj = tempListKanji.get(characterSeq);
                    vObj.Element.get().KanjiObject.set(kanjiObject);
                    result.add(vObj);
                }
            }

            for (int sentenceSeq : tempListExample.keySet()) {
                ExampleObject exampleObject = new ExampleObject(JDictApplication.getDatabaseHelper().getExample().getBasicExampleDetails(sentenceSeq));

                if (exampleObject != null) {
                    VocabularyObject vObj = tempListExample.get(sentenceSeq);
                    vObj.Element.get().ExampleObject.set(exampleObject);
                    result.add(vObj);
                }
            }
        }

        @Override
        protected void onPostExecute(ArrayList<VocabularyObject> result) {


            objectList.clear();
            for (VocabularyObject obj : result) {

                objectList.add(obj);
            }

            mAdapter.notifyDataSetChanged();

            if (mButton != null) {
                mButton.setEnabled(true);
                mButton.setClickable(true);
            }
        }
    }
}
