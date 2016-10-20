package utils.grammar;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;

import com.mino.jdict.objects.basic.GrammarObject;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Created by Mino on 2014-07-20.
 */
public class InputUtils {

    // jak wystepuje w literal VS jak wystepuje w decomposition
    private static final Map<Integer, ArrayList<Pair<String, String>>> COMPONENTLIST;
    public static int numberHiraganaStart, numberHiraganaEnd, numberKatakanaStart, numberKatakanaEnd, numberKanjiStart, numberKanjiEnd,
            numberPunctuationStart, numberPunctuationEnd, numberFullWidthRomanAndHalfWidthKanaStart, numberFullWidthRomanAndHalfWidthKanaEnd, numberHiraganaSimpleEnd;

    private static String punctuationStart = "3000", punctuationEnd = "303f", hiraganaStart = "3040", hiraganaEnd = "309f", hiraganaSimpleEnd = "3096", katakanaStart = "30a0", katakanaEnd = "30ff",
            fullWidthRomanAndHalfWidthKanaStart = "ff00", fullWidthRomanAndHalfWidthKanaEnd = "ffef", kanjiStart = "4e00", kanjiEnd = "9faf";

    static {
        numberHiraganaStart = Integer.parseInt(hiraganaStart, 16);
        numberHiraganaEnd = Integer.parseInt(hiraganaEnd, 16);
        numberHiraganaSimpleEnd = Integer.parseInt(hiraganaSimpleEnd, 16);

        numberKatakanaStart = Integer.parseInt(katakanaStart, 16);
        numberKatakanaEnd = Integer.parseInt(katakanaEnd, 16);

        numberKanjiStart = Integer.parseInt(kanjiStart, 16);
        numberKanjiEnd = Integer.parseInt(kanjiEnd, 16);

        numberPunctuationStart = Integer.parseInt(punctuationStart, 16);
        numberPunctuationEnd = Integer.parseInt(punctuationEnd, 16);

        numberPunctuationStart = Integer.parseInt(fullWidthRomanAndHalfWidthKanaStart, 16);
        numberPunctuationEnd = Integer.parseInt(fullWidthRomanAndHalfWidthKanaEnd, 16);

        Map<Integer, ArrayList<Pair<String, String>>> tmp = new HashMap<Integer, ArrayList<Pair<String, String>>>();

        ArrayList<Pair<String, String>> list1 = new ArrayList<Pair<String, String>>();

        list1.add(new Pair<String, String>("一", "一"));
        list1.add(new Pair<String, String>("丨", "｜"));
        list1.add(new Pair<String, String>("丶", "丶"));
        list1.add(new Pair<String, String>("ノ", "ノ"));
        list1.add(new Pair<String, String>("乙", "乙"));
        list1.add(new Pair<String, String>("亅", "亅"));
        tmp.put(1, list1);

        ArrayList<Pair<String, String>> list2 = new ArrayList<Pair<String, String>>();

        list2.add(new Pair<String, String>("二", "二"));
        list2.add(new Pair<String, String>("亠", "亠"));
        list2.add(new Pair<String, String>("人", "人"));
        list2.add(new Pair<String, String>("亻", "⺅"));
        list2.add(new Pair<String, String>("", "𠆢"));
        list2.add(new Pair<String, String>("入", "入"));
        list2.add(new Pair<String, String>("八", "ハ"));
        //list2.add(new Pair<String, String>("ハ", "ハ")); // brak literal w bazie
        list2.add(new Pair<String, String>("儿", "儿"));
        list2.add(new Pair<String, String>("丷", "并")); // w bazie decomp=literal
        list2.add(new Pair<String, String>("冫", "冫"));
        list2.add(new Pair<String, String>("凵", "凵"));
        list2.add(new Pair<String, String>("匚", "匚"));
        list2.add(new Pair<String, String>("冂", "冂"));
        list2.add(new Pair<String, String>("冖", "冖"));
        list2.add(new Pair<String, String>("几", "几"));
        list2.add(new Pair<String, String>("九", "九"));
        list2.add(new Pair<String, String>("力", "力"));
        list2.add(new Pair<String, String>("刀", "刀"));
        list2.add(new Pair<String, String>("刂", "刂"));
        list2.add(new Pair<String, String>("乃", "乃"));
        list2.add(new Pair<String, String>("勹", "勹"));
        list2.add(new Pair<String, String>("マ", "マ"));
        list2.add(new Pair<String, String>("匕", "匕"));
        list2.add(new Pair<String, String>("十", "十"));
        list2.add(new Pair<String, String>("卜", "卜"));
        list2.add(new Pair<String, String>("又", "又"));
        list2.add(new Pair<String, String>("厶", "厶"));
        list2.add(new Pair<String, String>("卩", "卩"));
        list2.add(new Pair<String, String>("厂", "厂"));

        list2.add(new Pair<String, String>("ユ", "ユ"));
        tmp.put(2, list2);

        ArrayList<Pair<String, String>> list3 = new ArrayList<Pair<String, String>>();

        list3.add(new Pair<String, String>("广", "广"));
        list3.add(new Pair<String, String>("口", "口"));
        list3.add(new Pair<String, String>("囗", "囗"));
        list3.add(new Pair<String, String>("土", "土"));
        list3.add(new Pair<String, String>("士", "士"));
        list3.add(new Pair<String, String>("夂", "夂"));
        list3.add(new Pair<String, String>("夕", "夕"));
        list3.add(new Pair<String, String>("尢", "尢"));
        list3.add(new Pair<String, String>("大", "大"));
        list3.add(new Pair<String, String>("女", "女"));
        list3.add(new Pair<String, String>("子", "子"));
        list3.add(new Pair<String, String>("宀", "宀"));
        list3.add(new Pair<String, String>("寸", "寸"));
        list3.add(new Pair<String, String>("小", "小"));
        list3.add(new Pair<String, String>("", "⺌"));
        list3.add(new Pair<String, String>("幺", "幺"));
        list3.add(new Pair<String, String>("尸", "尸"));
        list3.add(new Pair<String, String>("山", "山"));
        list3.add(new Pair<String, String>("川", "川"));
        list3.add(new Pair<String, String>("工", "工"));
        list3.add(new Pair<String, String>("已", "已"));
        list3.add(new Pair<String, String>("亡", "亡"));
        list3.add(new Pair<String, String>("巾", "巾"));
        list3.add(new Pair<String, String>("干", "干"));
        list3.add(new Pair<String, String>("廾", "廾"));
        list3.add(new Pair<String, String>("廴", "廴"));
        list3.add(new Pair<String, String>("", "辶"));
        list3.add(new Pair<String, String>("也", "也"));
        list3.add(new Pair<String, String>("弋", "弋"));
        list3.add(new Pair<String, String>("弓", "弓"));
        list3.add(new Pair<String, String>("彐", "彐")); //brak?
        list3.add(new Pair<String, String>("彡", "彡"));
        list3.add(new Pair<String, String>("彳", "彳"));
        list3.add(new Pair<String, String>("氵", "氵"));
        list3.add(new Pair<String, String>("丬", "爿"));
        list3.add(new Pair<String, String>("犭", "犭"));
        list3.add(new Pair<String, String>("扌", "扌"));
        list3.add(new Pair<String, String>("艹", "⺾"));
        list3.add(new Pair<String, String>("", "⻖"));
        list3.add(new Pair<String, String>("", "⻏"));
        list3.add(new Pair<String, String>("忄", "忄"));

        list3.add(new Pair<String, String>("久", "久"));
        list3.add(new Pair<String, String>("彑", "彑"));
        list3.add(new Pair<String, String>("及", "及"));

        list3.add(new Pair<String, String>("巛", "巛"));
        list3.add(new Pair<String, String>("屮", "屮"));

        tmp.put(3, list3);

        ArrayList<Pair<String, String>> list4 = new ArrayList<Pair<String, String>>();

        list4.add(new Pair<String, String>("心", "心"));
        list4.add(new Pair<String, String>("戈", "戈"));
        list4.add(new Pair<String, String>("戸", "戸"));
        list4.add(new Pair<String, String>("手", "手"));
        list4.add(new Pair<String, String>("攵", "攵"));
        list4.add(new Pair<String, String>("文", "文"));
        list4.add(new Pair<String, String>("斗", "斗"));
        list4.add(new Pair<String, String>("斤", "斤"));
        list4.add(new Pair<String, String>("方", "方"));
        list4.add(new Pair<String, String>("日", "日"));
        list4.add(new Pair<String, String>("曰", "曰"));
        list4.add(new Pair<String, String>("木", "木"));
        list4.add(new Pair<String, String>("欠", "欠"));
        list4.add(new Pair<String, String>("止", "止"));
        list4.add(new Pair<String, String>("歹", "歹"));
        list4.add(new Pair<String, String>("殳", "殳"));
        list4.add(new Pair<String, String>("毋", "毋"));
        list4.add(new Pair<String, String>("比", "比"));
        list4.add(new Pair<String, String>("毛", "毛"));
        list4.add(new Pair<String, String>("氏", "氏"));
        list4.add(new Pair<String, String>("气", "气"));
        list4.add(new Pair<String, String>("水", "水"));
        list4.add(new Pair<String, String>("火", "火"));
        list4.add(new Pair<String, String>("灬", "灬"));
        //list4.add(new Pair<String, String>("⺤", "爪"));
        list4.add(new Pair<String, String>("爪", "爪"));
        list4.add(new Pair<String, String>("父", "父"));
        list4.add(new Pair<String, String>("牛", "牛"));
        list4.add(new Pair<String, String>("犬", "犬"));
        list4.add(new Pair<String, String>("王", "王"));
        list4.add(new Pair<String, String>("礻", "礻"));
        list4.add(new Pair<String, String>("耂", "⺹"));
        list4.add(new Pair<String, String>("勿", "勿"));
        list4.add(new Pair<String, String>("五", "五"));
        list4.add(new Pair<String, String>("巴", "巴"));
        list4.add(new Pair<String, String>("元", "元"));
        list4.add(new Pair<String, String>("井", "井"));
        list4.add(new Pair<String, String>("井", "井"));
        list4.add(new Pair<String, String>("予", "子")); //?
        list4.add(new Pair<String, String>("屯", "屯"));

        list4.add(new Pair<String, String>("片", "片"));
        list4.add(new Pair<String, String>("爿", "爿"));
        list4.add(new Pair<String, String>("支", "支"));
        list4.add(new Pair<String, String>("无", "无"));
        list4.add(new Pair<String, String>("尤", "尤"));
        list4.add(new Pair<String, String>("爻", "爻"));

        tmp.put(4, list4);

        ArrayList<Pair<String, String>> list5 = new ArrayList<Pair<String, String>>();

        list5.add(new Pair<String, String>("甘", "甘"));
        list5.add(new Pair<String, String>("生", "生"));
        list5.add(new Pair<String, String>("用", "用"));
        list5.add(new Pair<String, String>("田", "田"));
        list5.add(new Pair<String, String>("疋", "疋"));
        list5.add(new Pair<String, String>("疒", "疒"));
        list5.add(new Pair<String, String>("癶", "癶"));
        list5.add(new Pair<String, String>("白", "白"));
        list5.add(new Pair<String, String>("皮", "皮"));
        list5.add(new Pair<String, String>("皿", "皿"));
        list5.add(new Pair<String, String>("目", "目"));
        list5.add(new Pair<String, String>("矢", "矢"));
        list5.add(new Pair<String, String>("石", "石"));
        list5.add(new Pair<String, String>("示", "示"));
        list5.add(new Pair<String, String>("禾", "禾"));
        list5.add(new Pair<String, String>("穴", "穴"));
        list5.add(new Pair<String, String>("立", "立"));
        list5.add(new Pair<String, String>("母", "母"));
        list5.add(new Pair<String, String>("衤", "衤"));
        list5.add(new Pair<String, String>("罒", "罒"));
        list5.add(new Pair<String, String>("世", "世"));
        list5.add(new Pair<String, String>("冊", "冊"));

        list5.add(new Pair<String, String>("巨", "巨"));
        list5.add(new Pair<String, String>("禸", "禸"));
        list5.add(new Pair<String, String>("矛", "矛"));
        list5.add(new Pair<String, String>("玄", "玄"));
        list5.add(new Pair<String, String>("瓦", "瓦"));
        list5.add(new Pair<String, String>("牙", "牙"));

        tmp.put(5, list5);

        ArrayList<Pair<String, String>> list6 = new ArrayList<Pair<String, String>>();

        list6.add(new Pair<String, String>("瓜", "瓜"));
        list6.add(new Pair<String, String>("而", "而"));

        //list6.add(new Pair<String, String>("⺮", "竹"));
        list6.add(new Pair<String, String>("竹", "竹"));

        list6.add(new Pair<String, String>("米", "米"));
        list6.add(new Pair<String, String>("糸", "糸"));
        list6.add(new Pair<String, String>("缶", "缶"));
        list6.add(new Pair<String, String>("羊", "羊"));
        list6.add(new Pair<String, String>("羽", "羽"));
        list6.add(new Pair<String, String>("耳", "耳"));
        list6.add(new Pair<String, String>("自", "自"));
        list6.add(new Pair<String, String>("至", "至"));
        list6.add(new Pair<String, String>("舌", "舌"));
        list6.add(new Pair<String, String>("舟", "舟"));
        list6.add(new Pair<String, String>("艮", "艮"));
        list6.add(new Pair<String, String>("虍", "虍"));
        list6.add(new Pair<String, String>("虫", "虫"));
        list6.add(new Pair<String, String>("血", "血"));
        list6.add(new Pair<String, String>("行", "行"));
        list6.add(new Pair<String, String>("衣", "衣"));
        list6.add(new Pair<String, String>("覀", "西"));

        list6.add(new Pair<String, String>("耒", "耒"));
        list6.add(new Pair<String, String>("肉", "肉"));
        list6.add(new Pair<String, String>("色", "色"));
        list6.add(new Pair<String, String>("臼", "臼"));
        list6.add(new Pair<String, String>("聿", "聿"));

        tmp.put(6, list6);

        ArrayList<Pair<String, String>> list7 = new ArrayList<Pair<String, String>>();

        list7.add(new Pair<String, String>("臣", "臣"));
        list7.add(new Pair<String, String>("見", "見"));
        list7.add(new Pair<String, String>("角", "角"));
        list7.add(new Pair<String, String>("言", "言"));
        list7.add(new Pair<String, String>("谷", "谷"));
        list7.add(new Pair<String, String>("豆", "豆"));
        list7.add(new Pair<String, String>("豕", "豕"));
        list7.add(new Pair<String, String>("貝", "貝"));
        list7.add(new Pair<String, String>("足", "足"));
        list7.add(new Pair<String, String>("身", "身"));
        list7.add(new Pair<String, String>("車", "車"));
        list7.add(new Pair<String, String>("辛", "辛"));
        list7.add(new Pair<String, String>("辰", "辰"));
        list7.add(new Pair<String, String>("酉", "酉"));
        list7.add(new Pair<String, String>("里", "里"));
        list7.add(new Pair<String, String>("赤", "赤"));
        list7.add(new Pair<String, String>("走", "走"));

        list7.add(new Pair<String, String>("豸", "豸"));
        list7.add(new Pair<String, String>("釆", "釆"));
        list7.add(new Pair<String, String>("麦", "麦"));
        list7.add(new Pair<String, String>("舛", "舛"));
        list7.add(new Pair<String, String>("邑", "邑"));
        tmp.put(7, list7);

        ArrayList<Pair<String, String>> list8 = new ArrayList<Pair<String, String>>();

        list8.add(new Pair<String, String>("金", "金"));
        list8.add(new Pair<String, String>("長", "長"));
        list8.add(new Pair<String, String>("門", "門"));
        list8.add(new Pair<String, String>("隹", "隹"));
        list8.add(new Pair<String, String>("雨", "雨"));
        list8.add(new Pair<String, String>("青", "青"));
        list8.add(new Pair<String, String>("岡", "岡"));
        list8.add(new Pair<String, String>("免", "免"));
        list8.add(new Pair<String, String>("斉", "斉"));

        list8.add(new Pair<String, String>("隶", "隶"));
        list8.add(new Pair<String, String>("非", "非"));
        list8.add(new Pair<String, String>("奄", "奄"));
        tmp.put(8, list8);

        ArrayList<Pair<String, String>> list9 = new ArrayList<Pair<String, String>>();

        list9.add(new Pair<String, String>("音", "音"));
        list9.add(new Pair<String, String>("頁", "頁"));
        list9.add(new Pair<String, String>("食", "食"));
        list9.add(new Pair<String, String>("首", "首"));
        list9.add(new Pair<String, String>("品", "品"));

        list9.add(new Pair<String, String>("面", "面"));
        list9.add(new Pair<String, String>("飛", "飛"));
        list9.add(new Pair<String, String>("革", "革"));
        list9.add(new Pair<String, String>("韭", "韭"));
        list9.add(new Pair<String, String>("風", "風"));
        list9.add(new Pair<String, String>("香", "香"));
        tmp.put(9, list9);

        ArrayList<Pair<String, String>> list10 = new ArrayList<Pair<String, String>>();

        list10.add(new Pair<String, String>("馬", "馬"));
        list10.add(new Pair<String, String>("高", "高"));
        list10.add(new Pair<String, String>("鬲", "鬲"));
        list10.add(new Pair<String, String>("韋", "韋"));
        list10.add(new Pair<String, String>("竜", "竜"));
        list10.add(new Pair<String, String>("骨", "骨"));
        list10.add(new Pair<String, String>("鬼", "鬼"));
        list10.add(new Pair<String, String>("髟", "髟"));
        list10.add(new Pair<String, String>("鬥", "鬥"));
        list10.add(new Pair<String, String>("鬯", "鬯"));
        tmp.put(10, list10);

        ArrayList<Pair<String, String>> list11 = new ArrayList<Pair<String, String>>();

        list11.add(new Pair<String, String>("啇", "滴"));
        list11.add(new Pair<String, String>("麻", "麻"));
        list11.add(new Pair<String, String>("鳥", "鳥"));
        list11.add(new Pair<String, String>("鹿", "鹿"));
        list11.add(new Pair<String, String>("黒", "黒"));
        list11.add(new Pair<String, String>("魚", "魚"));
        list11.add(new Pair<String, String>("黄", "黄"));
        list11.add(new Pair<String, String>("鹵", "鹵"));
        tmp.put(11, list11);


        ArrayList<Pair<String, String>> list12 = new ArrayList<Pair<String, String>>();

        list12.add(new Pair<String, String>("無", "無"));
        list12.add(new Pair<String, String>("歯", "歯"));
        list12.add(new Pair<String, String>("黍", "黍"));
        list12.add(new Pair<String, String>("黹", "黹"));
        tmp.put(12, list12);

        ArrayList<Pair<String, String>> list13 = new ArrayList<Pair<String, String>>();

        list13.add(new Pair<String, String>("黽", "黽"));
        list13.add(new Pair<String, String>("鼎", "鼎"));
        list13.add(new Pair<String, String>("鼓", "鼓"));
        list13.add(new Pair<String, String>("鼠", "鼠"));
        tmp.put(13, list13);

        ArrayList<Pair<String, String>> list14 = new ArrayList<Pair<String, String>>();

        list14.add(new Pair<String, String>("鼻", "鼻"));
        list14.add(new Pair<String, String>("齊", "齊"));
        tmp.put(14, list14);

        ArrayList<Pair<String, String>> list17 = new ArrayList<Pair<String, String>>();

        list17.add(new Pair<String, String>("龠", "龠"));
        tmp.put(17, list17);

        COMPONENTLIST = Collections.unmodifiableMap(tmp);
    }

    public static boolean isKana(String character) {
        return isStringAllKana(character);
    }

    public static boolean containsKanji(String word) {

        if (word == null) return false;
        if (word.isEmpty()) return false;

        for (Character c : word.toCharArray()) {

            int charNumber = (int) c;

            if (!(charNumber >= numberKanjiStart && charNumber <= numberKanjiEnd)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isNumeric(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }

    public static boolean isStringAllKanaOrKanji(String word) {
        if (word.isEmpty()) return false;

        for (Character c : word.toCharArray()) {

            int charNumber = (int) c;

            if (!(charNumber >= numberHiraganaStart && charNumber <= numberHiraganaEnd) &&
                    !(charNumber >= numberKatakanaStart && charNumber <= numberKatakanaEnd) &&
                    !(charNumber >= numberKanjiStart && charNumber <= numberKanjiEnd) &&
                    !(Character.isDigit(c))
                    && !(c == '々')) {

                return false;
            }
        }

        return true;
    }

    public static boolean isStringAllKana(String word) {

        if (word == null) return false;
        if (word.isEmpty()) return false;

        for (Character c : word.toCharArray()) {

            int charNumber = (int) c;

            if (!(charNumber >= numberHiraganaStart && charNumber <= numberHiraganaEnd) &&
                    !(charNumber >= numberKatakanaStart && charNumber <= numberKatakanaEnd)) {

                return false;
            }
        }

        return true;
    }

    public static boolean isCharKanji(Character character) {

        int charNumber = (int) character;

        if (!(charNumber >= numberKanjiStart && charNumber <= numberKanjiEnd)) {
            return false;
        }

        return true;
    }

    public static boolean isStringAllKanji(String word) {

        if (word.isEmpty()) return false;

        for (Character c : word.toCharArray()) {

            int charNumber = (int) c;

            if (!(charNumber >= numberKanjiStart && charNumber <= numberKanjiEnd)) {
                return false;
            }
        }

        return true;
    }

    public static boolean isStringAllRomaji(String word) {
        for (char c : word.toCharArray()) {
            if (((int) c) > 127 && c != 'ー') {
                return false;
            }
        }
        return true;
    }

    public enum CharType {Punctuation, Hiragana, Katakana, FullWidthRomanAndHalfWidthKana, Kanji, Digit, Other}


    @NonNull
    public static Pair<String, HashMap<Character, CharType>> getStringDescription(String inSearchString) {
        String cleanString = inSearchString.trim();
        char[] charArray = cleanString.toCharArray();
        HashMap<Character, CharType> charTypeHashMap = new HashMap<Character, CharType>();

        cleanString = "";

        for (char c : charArray) {

            CharType currentCharType = CharType.Other;

            int charNumber = (int) c;

            if (Character.isDigit(c)) {
                currentCharType = CharType.Digit;
            } else if (charNumber >= numberPunctuationStart && charNumber <= numberPunctuationEnd) {
                currentCharType = CharType.Punctuation;
            } else if (c == '[' || c == ']' || c == '(' || c == ')' || c == '、' || c == ' ') {
                currentCharType = CharType.Punctuation;
            } else if (charNumber >= numberHiraganaStart && charNumber <= numberHiraganaEnd) {
                currentCharType = CharType.Hiragana;
            } else if (charNumber >= numberKatakanaStart && charNumber <= numberKatakanaEnd) {
                currentCharType = CharType.Katakana;
            } else if (charNumber >= numberFullWidthRomanAndHalfWidthKanaStart && charNumber <= numberFullWidthRomanAndHalfWidthKanaEnd) {
                currentCharType = CharType.FullWidthRomanAndHalfWidthKana;
            } else if (charNumber >= numberKanjiStart && charNumber <= numberKanjiEnd) {
                currentCharType = CharType.Kanji;
            }

            cleanString += c;
            charTypeHashMap.put(c, currentCharType);

        }
        return new Pair<String, HashMap<Character, CharType>>(cleanString, charTypeHashMap);
    }


    public static String getWordTypesString(String inWordTypeShortcuts) {

        String result = "";
        String[] parts = inWordTypeShortcuts.split(";");

        for (String part : parts) {

            if (GrammarDictionaries.ENTITY_TYPE_MAP.containsKey(part)) {
                if (!result.contains(GrammarDictionaries.ENTITY_TYPE_MAP.get(part).getSimpleForm())) { // temp
                    result += GrammarDictionaries.ENTITY_TYPE_MAP.get(part).getSimpleForm() + ", ";
                }
            }
        }

        if (result.length() > 0) {
            return result.substring(0, result.length() - 2);
        } else return result;
    }

    public static String getGrammarWordType(String inGrammar) {
        if (GrammarDictionaries.GRAMMAR_WORDTYPE.containsKey(inGrammar)) {
            return GrammarDictionaries.GRAMMAR_WORDTYPE.get(inGrammar);
        } else return "";
    }

    public static String getWordTypeString(String inWordTypeShortcut) {
        if (GrammarDictionaries.ENTITY_TYPE_MAP.containsKey(inWordTypeShortcut)) {
            return GrammarDictionaries.ENTITY_TYPE_MAP.get(inWordTypeShortcut).getSimpleForm();
        } else return "";
    }

    public static String getVocabularySpinnerItem(int inSelectedItem) {
        if (GrammarDictionaries.VOCABULARY_SPINNER_LIST.containsKey(inSelectedItem)) {
            return GrammarDictionaries.VOCABULARY_SPINNER_LIST.get(inSelectedItem);
        } else return "";
    }

    public static String getKanjiSpinnerItem(int inSelectedItem) {
        if (GrammarDictionaries.KANJI_SPINNER_LIST.containsKey(inSelectedItem)) {
            return GrammarDictionaries.KANJI_SPINNER_LIST.get(inSelectedItem);
        } else return "";
    }


    public static String getKanjiSchoolGradesItem(int inSelectedItem) {
        if (GrammarDictionaries.KANJI_SCHOOL_GRADES_LIST.containsKey(inSelectedItem)) {
            return GrammarDictionaries.KANJI_SCHOOL_GRADES_LIST.get(inSelectedItem);
        } else return "";
    }

    public static String getKanjiJLPTItem(int inSelectedItem) {
        if (GrammarDictionaries.KANJI_JLPT_LIST.containsKey(inSelectedItem)) {
            return GrammarDictionaries.KANJI_JLPT_LIST.get(inSelectedItem);
        } else return "";
    }

    public static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public static ArrayList<GrammarObject> getPossibleSimpleForms(String inSearchString) {

        if (inSearchString.length() == 0) return new ArrayList<GrammarObject>();

        String last = inSearchString.substring(inSearchString.length() - 1, inSearchString.length());
        Boolean isKana = InputUtils.isKana(last);
        Boolean isRomaji = InputUtils.isStringAllRomaji(inSearchString);

        if (!isKana && !isRomaji) return new ArrayList<GrammarObject>();
        for (int i = 0; i < inSearchString.length(); i++) {
            if (Character.isDigit(inSearchString.charAt(i))) {
                return new ArrayList<GrammarObject>();
            }
        }


        return addAllSimpleForms(inSearchString, isRomaji);
    }

    private static ArrayList<GrammarObject> addAllSimpleForms(String inSearchString, Boolean isStringAllRomaji) {

        ArrayList<String> simpleFormStrings = new ArrayList<String>();
        Map<String, ArrayList<Pair<String, String>>> simpleFormStringTypes = new HashMap<String, ArrayList<Pair<String, String>>>();
        ArrayList<GrammarObject> simpleForms = new ArrayList<GrammarObject>();
        Queue<GrammarObject> simpleFormsQueue = new ArrayDeque<GrammarObject>();
        ArrayList<GrammarObject> toRemove = new ArrayList<GrammarObject>();

        GrammarObject startObject = new GrammarObject("aux", "", inSearchString, "", false);

        int grammarCount = GrammarDictionaries.GRAMMAR_LIST.size();

        // kolejka częściowo upraszczanych słow - na początek "uproszczoną" formą jest szukany ciąg znaków
        simpleForms.add(startObject);
        toRemove.add(startObject);

        boolean startObjectRemoved = false;
        boolean somethingAdded = true;
        while (somethingAdded) {

            somethingAdded = false;

            for (int i = 0; i < grammarCount; i++) {

                List<GrammarObject> currentGrammar = GrammarDictionaries.GRAMMAR_LIST.get(i);

                for (GrammarObject gObj : currentGrammar) {
                    if (InputUtils.isStringAllRomaji(gObj.getSimpleForm()) != isStringAllRomaji)
                        continue;

                    ArrayList<GrammarObject> removeTemp = addSimpleForms(inSearchString, simpleFormStrings, simpleFormStringTypes, simpleForms, gObj);

                    if (!removeTemp.isEmpty()) {
                        somethingAdded = true;
                        toRemove.addAll(removeTemp);
                    }
                }

            }
/*
            if (somethingAdded && !startObjectRemoved) {
                if (simpleForms.size() > 1) {

                    // startObject remove
                    simpleForms.remove(0);
                    startObjectRemoved = true;
                }
            }
            */
        }

        toRemove.clear();

        return simpleForms;
    }

    private static ArrayList<GrammarObject> addSimpleForms(String inSearchString, ArrayList<String> simpleFormStrings, Map<String, ArrayList<Pair<String, String>>> simpleFormStringTypes, ArrayList<GrammarObject> simpleForms, GrammarObject gObj) {

        ArrayList<GrammarObject> temp = new ArrayList<GrammarObject>();
        ArrayList<GrammarObject> toRemove = new ArrayList<GrammarObject>();

        String grammar = gObj.getGrammar();
        String grammarWordType = InputUtils.getGrammarWordType(grammar);

        // sprawdzamy, czy formy które z innych gramatyk zostały dotychczas uproszczone może też zawierają tę formę gramatyczną

        for (GrammarObject currentObj : simpleForms) {

            String currentObjGrammar = currentObj.getGrammar();

            if (!currentObjGrammar.equals("")) {
                if (GrammarDictionaries.CAN_BE_IN_CHAIN.containsKey(grammar)) {

                    List<String> grammars = GrammarDictionaries.CAN_BE_IN_CHAIN.get(grammar);
                    if (!grammars.contains(currentObjGrammar)) {
                        continue;
                    }

                    if (grammarWordType != null && !grammarWordType.isEmpty()) {

                        if (currentObj.getGrammarWordType() != null && !currentObj.getGrammarWordType().isEmpty()
                                && !currentObj.getGrammarWordType().equals("aux")
                                && !currentObjGrammar.equals("polite") && !currentObj.getGrammarWordType().equals(grammarWordType)) {
                                continue;
                        }
                        else
                        if (!currentObj.getWordType().equals("aux")
                                && !currentObjGrammar.equals("polite") && !currentObj.getWordType().equals(grammarWordType)) {
                            continue;
                        }
                    }


                } else continue;
            }

            if (addSimpleFormsAux(currentObj.getSimpleForm(), simpleFormStrings, simpleFormStringTypes, temp, currentObjGrammar, currentObj.getWordType(), gObj, grammar, currentObj.getGrammarChain())) {

                // zostawiamy tylko bardziej uproszczoną opcję, a poprzednią usuwamy

                //if (currentObj.getWordType().equals(gObj.getWordType()) || currentObj.getWordType().equals("aux")) {
                toRemove.add(currentObj);
                // }
            }

        }

        simpleForms.addAll(temp);

        return toRemove;
    }

    private static boolean containsWordType(ArrayList<Pair<String, String>> simpleFormStringTypes, String wordType) {

        for (Pair<String, String> obj : simpleFormStringTypes) {
            if (obj.first.equals(wordType)) {
                return true;
            }
        }

        return false;
    }

    private static boolean containsWordTypeWithSpecifiedGrammar(ArrayList<Pair<String, String>> simpleFormStringTypes, String wordType, String grammar) {

        for (Pair<String, String> obj : simpleFormStringTypes) {
            if (obj.first.equals(wordType) && obj.second.equals(grammar)) {
                return true;
            }
        }

        return false;
    }

    private static boolean checkSameCreationRule(GrammarObject gObj, String simpleFormString, Map<String, ArrayList<Pair<String, String>>> simpleFormStringTypes) {
        boolean ret = false;

        Map<String, List<Pair<String, String>>> creationRuleMap = GrammarDictionaries.HAVE_SAME_CREATION_RULE;

        for (String wordType : creationRuleMap.keySet()) {
            List<Pair<String, String>> list = creationRuleMap.get(wordType);

            for (Pair<String, String> pair : list) {

                ret |= simpleFormStringTypes.containsKey(simpleFormString)
                        && (gObj.getWordType().equals(wordType) && gObj.getGrammar().equals(pair.first) && containsWordTypeWithSpecifiedGrammar(simpleFormStringTypes.get(simpleFormString), wordType, pair.second)
                        && !containsWordTypeWithSpecifiedGrammar(simpleFormStringTypes.get(simpleFormString), wordType, pair.first));

                ret |= simpleFormStringTypes.containsKey(simpleFormString)
                        && (gObj.getWordType().equals(wordType) && gObj.getGrammar().equals(pair.second) && containsWordTypeWithSpecifiedGrammar(simpleFormStringTypes.get(simpleFormString), wordType, pair.first)
                        && !containsWordTypeWithSpecifiedGrammar(simpleFormStringTypes.get(simpleFormString), wordType, pair.second));
            }
        }

        return ret;
    }

    // patrzymy czy wyszukany ciąg znaków ma konkretną formę gramatyczną. jeśli tak dodajemy formę prostą (a raczej uproszczoną o tę formę) do simpleForms
    // simpleFormStrings i simpleFormStringTypes zapewniają, że mogę dwa razy dodać ten sam string o ile dotyczy innych typów słów (np v5g i v5k)
    // ZMIANA : mogą być DWA takie same stringi dla tego samego typu słowa - v1 passive i potential (todo sprawdzić resztę)
    private static boolean addSimpleFormsAux(String inSearchString, ArrayList<String> simpleFormStrings, Map<String, ArrayList<Pair<String, String>>> simpleFormStringTypes, ArrayList<GrammarObject> simpleForms, String currentGrammar, String currentWordType, GrammarObject gObj, String grammar, String grammarChain) {

        String changedForm = gObj.getChangedForm();
        int changedFormSize = changedForm.length();

        boolean added = false;

        if (inSearchString.endsWith(gObj.getChangedForm())) { //&& !inSearchString.equals(gObj.getChangedForm())) {
            String simpleFormString = inSearchString.substring(0, inSearchString.length() - changedFormSize) + gObj.getSimpleForm();

            // możemy trafić na słowo z różnych typów po drodze -> problem jest np przy hanasemasen -> dostajemy v1, a interesuje nas hanaseru v5s
            //&& (currentWordType == gObj.getWordType() || gObj.getWordType() == "aux" || currentWordType == "aux")

            if ((!currentGrammar.equals(gObj.getGrammar()))
                    && (
                    !simpleFormStrings.contains(simpleFormString) // nie mieliśmy jeszcze takiej uproszczonej formy
                            || !(simpleFormStringTypes.containsKey(simpleFormString)  // mieliśmy taka samą
                            && (containsWordType(simpleFormStringTypes.get(simpleFormString), gObj.getWordType())
                            || gObj.getWordType().equals("aux"))) // ale miała inny wordType
                            || checkSameCreationRule(gObj, simpleFormString, simpleFormStringTypes)

            )) {

                simpleFormStrings.add(simpleFormString);

                if (simpleFormStringTypes.containsKey(simpleFormString)) {
                    simpleFormStringTypes.get(simpleFormString).add(new Pair<String, String>(gObj.getWordType(), gObj.getGrammar()));
                } else {
                    ArrayList<Pair<String, String>> formStringTypeList = new ArrayList<Pair<String, String>>();

                    if (!gObj.getWordType().equals("aux")) {
                        formStringTypeList.add(new Pair<String, String>(gObj.getWordType(), gObj.getGrammar()));
                    }

                    simpleFormStringTypes.put(simpleFormString, formStringTypeList);
                }

                added = true;

                String grammarWordType = InputUtils.getGrammarWordType(currentGrammar);
                //if (grammarWordType == null || grammarWordType.isEmpty()) {
                //    grammarWordType = gObj.getWordType();
                //}

                GrammarObject newGrammarObj = new GrammarObject(gObj.getWordType(), grammarWordType, inSearchString, simpleFormString, grammar, grammarChain);
                newGrammarObj.addToGrammarChain(gObj.getGrammar());

                simpleForms.add(newGrammarObj);
            }

        }

        return added;
    }

    public enum KanaType {Hiragana, Katakana}

    ;

    public static String getKana(String inRomajiString, KanaType inType) {
        if (!InputUtils.isStringAllRomaji(inRomajiString)) {

            return inRomajiString;
        }

        inRomajiString = inRomajiString.toLowerCase();
        inRomajiString = inRomajiString.replace("'", "$");
        inRomajiString = inRomajiString.replace("m$p", "n$p").replace("m$b", "n$b");
        inRomajiString = inRomajiString.replace("mp", "np").replace("mb", "nb");
        inRomajiString = inRomajiString.replace("nn", "n$n");

        Map<String, String> kanaMap = inType == KanaType.Hiragana ? GrammarDictionaries.ROMAJI_HIRAGANA_LIST : GrammarDictionaries.ROMAJI_KATAKANA_LIST;

        String retValue = "";
        boolean added = false;

        for (int i = 0; i < inRomajiString.length(); i++) {

            int charactersToCheck = Math.min(3, inRomajiString.length() - i);

            added = false;
            while (charactersToCheck > 0) {

                String checkStr = inRomajiString.substring(i, i + charactersToCheck);

                if (kanaMap.containsKey(checkStr)) {

                    retValue += kanaMap.get(checkStr);
                    i += charactersToCheck - 1;
                    added = true;

                    break;
                } else if (checkStr.length() > 1) {
                    if (checkStr.substring(0, 1).equals(checkStr.substring(1, 2)) &&
                            !(checkStr.charAt(0) == 'a') &&
                            !(checkStr.charAt(0) == 'i') &&
                            !(checkStr.charAt(0) == 'u') &&
                            !(checkStr.charAt(0) == 'e') &&
                            !(checkStr.charAt(0) == 'o')) {

                        retValue += kanaMap.get("double_sign");
                        added = true;
                        break;
                    }
                }

                charactersToCheck--;
            }

            if (!added) {
                return null;
            }

        }

        return retValue;
    }

    public static String prepareQueryForSearchRomaji(String inQuery) {
        return inQuery.replace("nb", "mb")
                .replace("np", "mp")
                .replace("n$p", "m$p")
                .replace("n$b", "m$b")
                .replace("nn", "n$n")
                .replace("mb", "m$b")
                .replace("mp", "m$p");
    }


    public static String getRomaji(String inKanaString, boolean inToShow) {

        if (InputUtils.isStringAllRomaji(inKanaString)) {

            String outString = "";

            for (int i = 0; i < inKanaString.length(); i++) {
                if (i < inKanaString.length() - 1 && (inKanaString.charAt(i) == 'n' || inKanaString.charAt(i) == 'm')
                        && !(inKanaString.charAt(i + 1) == '$')
                        && !(inKanaString.charAt(i + 1) == 'a')
                        && !(inKanaString.charAt(i + 1) == 'i')
                        && !(inKanaString.charAt(i + 1) == 'u')
                        && !(inKanaString.charAt(i + 1) == 'e')
                        && !(inKanaString.charAt(i + 1) == 'o')
                        && !(inKanaString.charAt(i + 1) == 'y') // nyuu/nya itd.
                        ) {
                    outString += String.valueOf(inKanaString.charAt(i)) + (inToShow ? "" : "$");
                } else outString += String.valueOf(inKanaString.charAt(i));
            }

            if (outString.endsWith("n") && !inToShow) {
                outString += "$";
            }


            return outString;
        }

        String romajiString = "";
        Boolean doubleNext = false;

        int kanaLength = inKanaString.length();

        for (int i = 0; i < kanaLength; i++) {
            String kana = "";

            String nextKana = inKanaString.substring(i, i + 1);
            if (!InputUtils.isKana(nextKana)) {
                doubleNext = false;
                romajiString += nextKana;
                continue;
            }

            if (i < inKanaString.length() - 1) {
                if (inKanaString.charAt(i) == 'っ' || inKanaString.charAt(i) == 'ッ') {
                    doubleNext = true;
                    continue;
                }

                String tryGet = inKanaString.substring(i, i + 2);

                // jo,ju itp

                if (GrammarDictionaries.KANA_ROMAJI_LIST.containsKey(tryGet)) {
                    kana = GrammarDictionaries.KANA_ROMAJI_LIST.get(tryGet);
                }
            }

            if (inKanaString.charAt(i) == 'ー') {
                romajiString += "ー";
                continue;
            }

            if (kana != null && !kana.isEmpty()) {
                i++;
            } else {
                if (GrammarDictionaries.KANA_ROMAJI_LIST.containsKey(inKanaString.substring(i, i + 1))) {
                    kana = GrammarDictionaries.KANA_ROMAJI_LIST.get(inKanaString.substring(i, i + 1));
                }
            }

            if (kana != null && !kana.isEmpty()) {
                if (doubleNext) {
                    kana = kana.substring(0, 1) + kana;
                    doubleNext = false;
                }

                romajiString += kana;

            } else {
                romajiString += inKanaString.substring(i, i + 1);
            }
        }

        romajiString = romajiString.replace("n$p", "m$p").replace("n$b", "m$b");

        if (inToShow) {

            String outString = "";
            for (int i = 0; i < romajiString.length(); i++) {
                if (!(i < romajiString.length() - 1 && romajiString.charAt(i) == '$'
                        && !(romajiString.charAt(i + 1) == 'a')
                        && !(romajiString.charAt(i + 1) == 'i')
                        && !(romajiString.charAt(i + 1) == 'u')
                        && !(romajiString.charAt(i + 1) == 'e')
                        && !(romajiString.charAt(i + 1) == 'o')
                        && !(romajiString.charAt(i + 1) == 'y')
                )) {
                    outString += String.valueOf(romajiString.charAt(i));
                }
            }

            outString = outString.replace("$", "'");

            if (outString.endsWith("'")) {
                outString = outString.substring(0, outString.length() - 1);
            }

            return outString;
        }

        return romajiString;
    }

    public static ArrayList<Pair<String, String>> getComponentCharacterEntSeq(int strokeCount) {
        if (strokeCount < 8) {
            return COMPONENTLIST.get(strokeCount);
        } else {
            ArrayList<Pair<String, String>> outList = new ArrayList<Pair<String, String>>();

            for (int i = 8; i <= 14; i++) {
                outList.addAll(COMPONENTLIST.get(i));
            }

            outList.addAll(COMPONENTLIST.get(17));

            return outList;
        }
    }

    public enum WildCardMode {Left, Right, Middle, RightLeft, NoWildCard}

    public static String getWildCardString(String ftsTable, String property, String matchString, String searchString, int propertyIndex, boolean inReturnProperty) {

        WildCardMode mode = WildCardMode.Middle;
        String trimmedString = searchString.trim();
        mode = getMode(mode, trimmedString);

        return getWildCardString(ftsTable, property, searchString, matchString, propertyIndex, inReturnProperty, mode);
    }

    @Nullable
    public static String getWildCardString(String ftsTable, String property, String searchString, String matchString, int propertyIndex, boolean inReturnProperty, WildCardMode mode) {

        String trimmedString = searchString.trim();
        String searchStringWithoutStars = matchString.replace("*", "");

        switch (mode) {

            case NoWildCard:
                // dotyczy tylko compounds
                return "select ent_seq as ent_seq, keb as keb from k_ele_fts where keb_with_spaces match '" + searchString + "' ";

            // X*Y , X*Y*Z etc.
            case Middle:
                return "select c0ent_seq as ent_seq " +
                        (inReturnProperty ? (", c" + propertyIndex + property + " as " + property) : "")
                        + " from " + ftsTable + "_content where c" + propertyIndex + property + " glob '" + trimmedString + "' and length(c" + propertyIndex + property + ") > " + searchString.length();

            // X*
            case Right:

                return " select ent_seq " +
                        (inReturnProperty ? (", " + property) : "") +
                        " from " + ftsTable + " where " + property + "  match '^" + searchStringWithoutStars.trim() + "*' and length(" + property + ") > "
                        + searchStringWithoutStars.length() + " collate nocase ";

            // *X*
            case RightLeft:

                // pojedyńcze kanji -> optymalizacja z użyciem withSpaces
                if (ftsTable.equals("k_ele_fts") && searchStringWithoutStars.length() == 1 && !InputUtils.isStringAllKana((searchStringWithoutStars)) && !InputUtils.isStringAllRomaji(searchStringWithoutStars)) {
                    return "select ent_seq " +
                            (inReturnProperty ? (", " + property) : "") +
                            " from k_ele_fts where keb_with_spaces match '" + searchStringWithoutStars + "' \n" +
                            "except \n" +
                            "select ent_seq " +
                            (inReturnProperty ? (", " + property) : "") +
                            " from k_ele_fts where keb match '" + searchStringWithoutStars + "*'\n" +
                            "except \n" +
                            "select c0ent_seq as ent_seq " +
                            (inReturnProperty ? (", c" + propertyIndex + property + " as " + property) : "") +
                            " from k_ele_fts_content where c1keb glob '*" + searchStringWithoutStars + "'";
                }

                return "select c0ent_seq as ent_seq " +
                        (inReturnProperty ? (", c" + propertyIndex + property + " as " + property) : "") +
                        " from " + ftsTable + "_content where c" + propertyIndex + property + " glob '*" + searchStringWithoutStars + "*'\n" +
                        "except select ent_seq " +
                        (inReturnProperty ? (", " + property) : "") +
                        " from " + ftsTable + " where " + property + " match '" + searchStringWithoutStars + "*'\n" +
                        "except select c0ent_seq as ent_seq " +
                        (inReturnProperty ? (", c" + propertyIndex + property + " as " + property) : "") +
                        " from " + ftsTable + "_content where c" + propertyIndex + property + " glob '*" + searchStringWithoutStars + "'";


            // *X
            case Left:

                return " select c0ent_seq as ent_seq " +
                        (inReturnProperty ? (", c" + propertyIndex + property + " as " + property) : "") +
                        " from " + ftsTable + "_content where c" + propertyIndex + property + " glob '*" + searchStringWithoutStars + "'\n" +
                        " except select ent_seq " +
                        (inReturnProperty ? (", " + property) : "") +
                        " from " + ftsTable + " where " + property + " match '^" + searchStringWithoutStars + "'";

        }
        return null;
    }

    private static WildCardMode getMode(WildCardMode mode, String trimmedString) {
        if (trimmedString.startsWith("*") && trimmedString.endsWith("*")) {
            mode = WildCardMode.RightLeft;
        } else if (trimmedString.startsWith("*")) {
            mode = WildCardMode.Left;
        } else if (trimmedString.endsWith("*")) {
            mode = WildCardMode.Right;
        }
        return mode;
    }

    public static String addSpaces(String inQuery) {

        String result = "";
        for (int i = 0; i < inQuery.length(); i++) {
            result += inQuery.charAt(i);

            if (inQuery.charAt(i) != '*') {
                result += " ";
            }
        }

        return result;
    }


}
