package utils.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.google.android.vending.expansion.downloader.Helpers;
import com.readystatesoftware.sqliteasset.CustomSQLiteAssetHelper;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import utils.database.entry.Example;
import utils.database.entry.Extract;
import utils.database.entry.Kanji;
import utils.database.entry.Sense;
import utils.database.entry.Setting;
import utils.database.entry.Vocabulary;
import utils.database.entry.Word;

public class JMDictHelper extends CustomSQLiteAssetHelper {

    private static final String DATABASE_NAME = "main.23.com.mino.jdict.obb";
    private static final int DATABASE_VERSION = 12;

    private Context mContext;

    private Sense senseEntries;
    private Kanji kanjiEntries;
    private Word wordEntries;
    private Example exampleEntries;
    private Vocabulary vocabularyEntries;
    private Extract extractEntries;
    private Setting settingEntries;

    public JMDictHelper(Context context) {
        //Helpers.getSaveFilePath(context) + File.separator,
        super(context, DATABASE_NAME, Environment.getExternalStorageDirectory().toString() + "/Android/obb/" + context.getPackageName() + "/",  null, DATABASE_VERSION);

        mContext = context;
        senseEntries = new Sense(this);
        kanjiEntries = new Kanji(this);
        wordEntries = new Word(this);
        exampleEntries = new Example(this);
        vocabularyEntries = new Vocabulary(this);
        extractEntries = new Extract(this);
        settingEntries = new Setting(this);
    }

    public Setting getSetting() { return settingEntries; }

    public Example getExample() {
        return exampleEntries;
    }

    public Sense getSense() { return senseEntries; }

    public Kanji getKanji() {
        return kanjiEntries;
    }

    public Word getWord() {
        return wordEntries;
    }

    public Vocabulary getVocabulary() {
        return vocabularyEntries;
    }

    public Extract getExtract() { return extractEntries; }

    public Context getContext() { return mContext; }
}