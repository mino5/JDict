package utils.other;

import android.content.Context;
import android.database.Cursor;

import com.mino.jdict.objects.activities.ListObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import utils.database.JMDictHelper;
import utils.database.WordSearchState;
import utils.grammar.InputUtils;

/**
 * Created by Dominik on 10/26/2015.
 */
public class StartupStaticData {

    private static String[] NotToSplitStr = new String[]{
            "そして",
            //"はな",
            "大学",
            "日本",
            "高校生",
            "おかげ", "ところ", "ちょう",
            "している",
            "おおさか",
            "はずがない",
            "はず",
            "かなり",
            "ながら",
            "こない",
            "だろう",
            "らしい",
            "でしょう",
            //"かもしれません",
            "かもしれ",
            "たばこ",
            "たくさん",
            //"のどか",
            //"のど",
            //"あっという",
            "という",
            "です",
            "ならない",
            "できる",
            "ものの",
            "とても",
            "もし",
            "もっと",
            "かねない",
            "なければならない",
            "のしかか",
            "いられない",
            "まくって",
            "ではな",
            "へん",
            "だから",
            "だいたい",
            "なので",
            "そんな",
            "こんな",
            "あんな",
            "なんて",
            "ずっと",
            "いつも",
            "なか",
            "しかし",
            "かも",
            "かな",
            "あと"

    };
    private static String[] DoubleCheckPreloadedStr = new String[]{
            "なんで",
            "でしょう",
            "です",
            //"いた",
            "して",
            "した",
            "しか",
            "なぜ",
            "ここ",
            "きた",
            "ので",
            "かな",
            "あった",
            "その",
            "じゃない",
            "でも",
            "から", "もん", "です", "こと", "この", "もの", "どこ", "きた", "どう", "それ",
            "する",
            "さん", "ない", "へん",
            "のみ",
            "何", "所", "彼", "そう", "まだ", "いる", "いい", "なん",
            "の",
            "が",
            "へ",
            "は",
            "を", "に", "ら",
            "な",
            "か",
            "で",
            "や",
            "と","も"};

    //"な",

    public static HashMap<String, ListObject> DOUBLE_CHECK_PRELOADED_OBJECTS = new HashMap<String, ListObject>();
    public static HashMap<WordSearchState, ListObject> EXTRACT_SAVED_OBJECTS = new HashMap<WordSearchState, ListObject>();
    public static HashMap<String, String> PARTICLE_WORDTYPE_PREFERENCES = new HashMap<String, String>();
    public static HashMap<String, String> PARTICLE_WORDTYPE_FORBIDDEN = new HashMap<String, String>();
    public static ArrayList<String> DoubleCheckPreloadedStrings = new ArrayList<String>();
    public static ArrayList<String> NotToSplitStrings = new ArrayList<String>();

    static {
        Collections.addAll(DoubleCheckPreloadedStrings, DoubleCheckPreloadedStr);
        Collections.addAll(NotToSplitStrings, NotToSplitStr);
    }


    public static void Init(Context inContext) {

        JMDictHelper dbHelper = new JMDictHelper(inContext);
        dbHelper.getSetting().getGlobalSettings();

        ArrayList<ListObject> objList = new ArrayList<ListObject>();

        for (String s : DoubleCheckPreloadedStrings) {
            if (InputUtils.isStringAllKanji(s)) continue;
            if (s.length() > 1) continue;

            Cursor c = dbHelper.getExtract().getEntryForExtract(s, new WordSearchState(0, s.length(), 0, s.length(), s, "", "", "", "", false));

            if (c.getCount() > 0) {

                dbHelper.getWord().getDataFromCursor(objList, c, false, false, false, true);

                if (objList.size() > 0) {
                    ListObject listObj = objList.get(0);
                    listObj.Query.set(s);
                    listObj.IsPreloaded.set(true);

                    DOUBLE_CHECK_PRELOADED_OBJECTS.put(s, listObj);
                    objList.clear();
                }
            }
        }

        PARTICLE_WORDTYPE_PREFERENCES.put("の", "n");
        PARTICLE_WORDTYPE_PREFERENCES.put("を", "vt");
        PARTICLE_WORDTYPE_PREFERENCES.put("に", "v");
        PARTICLE_WORDTYPE_PREFERENCES.put("へ", "v");

        //PARTICLE_WORDTYPE_FORBIDDEN.put("を", "prt");
        //PARTICLE_WORDTYPE_FORBIDDEN.put("へ", "n");
    }
}
