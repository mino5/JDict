package utils.grammar;

import android.util.Pair;

import com.mino.jdict.R;
import com.mino.jdict.objects.basic.GrammarObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mino on 2014-08-31.
 */
public final class GrammarDictionaries {
    public static final Map<String, String> KANA_ROMAJI_LIST;
    public static final Map<String, String> ROMAJI_HIRAGANA_LIST;
    public static final Map<String, String> ROMAJI_KATAKANA_LIST;

    public static final List<GrammarObject> PASSIVES_LIST;
    public static final List<GrammarObject> POLITE_FORM_LIST;
    public static final List<GrammarObject> NEGATIVE_FORM_LIST;
    public static final List<GrammarObject> TAI_FORM_LIST;
    public static final List<GrammarObject> I_FORM_LIST;
    public static final List<GrammarObject> TE_FORM_LIST;
    public static final List<GrammarObject> TEIRU_FORM_LIST;
    public static final List<GrammarObject> TEOKU_FORM_LIST;
    public static final List<GrammarObject> POTENTIAL_FORM_LIST;
    public static final List<GrammarObject> PASSIVE_FORM_LIST;
    public static final List<GrammarObject> CAUSATIVE_FORM_LIST;
    public static final List<GrammarObject> PROVISIONAL_CONDITIONAL_FORM_LIST;
    public static final List<GrammarObject> CONDITIONAL_RA_LIST;
    public static final List<GrammarObject> IMPERATIVE_FORM_LIST;
    public static final List<GrammarObject> VOLITIONAL_FORM_LIST;
    public static final List<GrammarObject> TO_ADV_FORM_LIST;
    public static final List<GrammarObject> TO_NOUN_FORM_LIST;
    public static final List<GrammarObject> TARI_FORM_LIST;

    public static final Map<String, GrammarObject> ENTITY_TYPE_MAP;
    public static final Map<String, Integer> LANGUAGES_LIST;
    public static final List<List<GrammarObject>> GRAMMAR_LIST;
    public static final Map<Integer, String> KANJI_SPINNER_LIST;
    public static final Map<Integer, String> KANJI_SCHOOL_GRADES_LIST;
    public static final Map<Integer, String> KANJI_JLPT_LIST;
    public static final Map<Integer, String> VOCABULARY_SPINNER_LIST;
    public static final Map<String, List<GrammarObject>> GRAMMAR_MAP;
    //public static final Map<String, String> WORD_TYPE_AFTER_MAP;

    public static final Map<String, List<String>> CAN_BE_IN_CHAIN;
    public static final Map<String, List<Pair<String, String>>> HAVE_SAME_CREATION_RULE;
    public static final Map<String, List<String>> NIG_KANA;
    public static final Map<String, String> GRAMMAR_WORDTYPE;

    static {
        Map<String, String> tmp = new HashMap<String, String>();

        tmp.put("double_sign", "ッ");

        tmp.put("a", "ア");
        tmp.put("i", "イ");
        tmp.put("u", "ウ");
        tmp.put("e", "エ");
        tmp.put("o", "オ");
        tmp.put("ka", "カ");
        tmp.put("ki", "キ");
        tmp.put("ku", "ク");
        tmp.put("ke", "ケ");
        tmp.put("ko", "コ");
        tmp.put("sa", "サ");
        tmp.put("shi", "シ");
        tmp.put("su", "ス");
        tmp.put("se", "セ");
        tmp.put("so", "ソ");
        tmp.put("ta", "タ");
        tmp.put("chi", "チ");
        tmp.put("tsu", "ツ");
        tmp.put("te", "テ");
        tmp.put("to", "ト");
        tmp.put("ha", "ハ");
        tmp.put("hi", "ヒ");
        tmp.put("fu", "フ");
        tmp.put("he", "ヘ");
        tmp.put("ho", "ホ");
        tmp.put("na", "ナ");
        tmp.put("ni", "ニ");
        tmp.put("nu", "ヌ");
        tmp.put("ne", "ネ");
        tmp.put("no", "ノ");
        tmp.put("ma", "マ");
        tmp.put("mi", "ミ");
        tmp.put("mu", "ム");
        tmp.put("me", "メ");
        tmp.put("mo", "モ");
        tmp.put("ra", "ラ");
        tmp.put("ri", "リ");
        tmp.put("ru", "ル");
        tmp.put("re", "レ");
        tmp.put("ro", "ロ");
        tmp.put("wa", "ワ");
        tmp.put("wo", "ヲ");
        tmp.put("n$", "ン");
        tmp.put("n", "ン");

        tmp.put("ga", "ガ");
        tmp.put("gi", "ギ");
        tmp.put("gu", "グ");
        tmp.put("ge", "ゲ");
        tmp.put("go", "ゴ");
        tmp.put("za", "ザ");
        tmp.put("ji", "ジ");
        tmp.put("du", "ヅ");
        tmp.put("zu", "ズ");
        tmp.put("ze", "ゼ");
        tmp.put("zo", "ゾ");
        tmp.put("da", "ダ");
        tmp.put("de", "デ");
        tmp.put("di", "ヂ");
        tmp.put("do", "ド");
        tmp.put("pa", "パ");
        tmp.put("pi", "ピ");
        tmp.put("pu", "プ");
        tmp.put("pe", "ペ");
        tmp.put("po", "ポ");
        tmp.put("ba", "バ");
        tmp.put("bi", "ビ");
        tmp.put("bu", "ブ");
        tmp.put("be", "ベ");
        tmp.put("bo", "ボ");

        tmp.put("ja", "ジャ");
        tmp.put("ju", "ジュ");
        tmp.put("jo", "ジョ");
        tmp.put("gya", "ギャ");
        tmp.put("gyu", "ギュ");
        tmp.put("gyo", "ギョ");
        tmp.put("bya", "ビャ");
        tmp.put("byu", "ビュ");
        tmp.put("byo", "ビョ");
        tmp.put("pya", "ピャ");
        tmp.put("pyu", "ピュ");
        tmp.put("pyo", "ピョ");
        tmp.put("dya", "ヂャ");
        tmp.put("dyu", "ヂュ");
        tmp.put("dyo", "ヂョ");

        tmp.put("kya", "キャ");
        tmp.put("kyu", "キュ");
        tmp.put("kyo", "キョ");
        tmp.put("sha", "シャ");
        tmp.put("shu", "シュ");
        tmp.put("she", "シェ");
        tmp.put("sho", "ショ");
        tmp.put("cha", "チャ");
        tmp.put("che", "チェ");
        tmp.put("chu", "チュ");
        tmp.put("cho", "チョ");
        tmp.put("nya", "ニャ");
        tmp.put("nyu", "ニュ");
        tmp.put("nyo", "ニョ");
        tmp.put("hya", "ヒャ");
        tmp.put("hyu", "ヒュ");
        tmp.put("hyo", "ヒョ");
        tmp.put("mya", "ミャ");
        tmp.put("myu", "ミュ");
        tmp.put("myo", "ミョ");
        tmp.put("rya", "リャ");
        tmp.put("ryu", "リュ");
        tmp.put("ryo", "リョ");
        tmp.put("fa", "ファ");
        tmp.put("fe", "フェ");
        tmp.put("fo", "フォ");

        tmp.put("ya", "ャ");
        tmp.put("yo", "ョ");
        tmp.put("yu", "ュ");
        tmp.put("xtu", "ッ");
        tmp.put("ya", "ヤ");
        tmp.put("yo", "ヨ");
        tmp.put("yu", "ユ");
        tmp.put("va", "ヴァ");
        tmp.put("vi", "ヴぃ");
        tmp.put("vu", "ヴ");
        tmp.put("ve", "ヴぇ");
        tmp.put("vo", "ヴぉ");
        tmp.put("ti", "ティ");

        tmp.put("~", "・");
        tmp.put("ー","ー");

        ROMAJI_KATAKANA_LIST = Collections.unmodifiableMap(tmp);
    }

    static {
        Map<String, String> tmp = new HashMap<String, String>();

        tmp.put("double_sign", "っ");

        tmp.put("a", "あ");
       // tmp.put("a", "ぁ");
        tmp.put("i", "い");
      //  tmp.put("i", "ぃ");
        tmp.put("u", "う");
     //   tmp.put("u", "ぅ");
        tmp.put("e", "え");
      //  tmp.put("e", "ぇ");
        tmp.put("o", "お");
     //   tmp.put("o", "ぉ");
        tmp.put("ka", "か");
        tmp.put("ki", "き");
        tmp.put("ku", "く");
        tmp.put("ke", "け");
        tmp.put("ko", "こ");
        tmp.put("sa", "さ");
        tmp.put("shi", "し");
        tmp.put("su", "す");
        tmp.put("se", "せ");
        tmp.put("so", "そ");
        tmp.put("ta", "た");
        tmp.put("chi", "ち");
        tmp.put("tsu", "つ");
        tmp.put("te", "て");
        tmp.put("to", "と");
        tmp.put("ha", "は");
        tmp.put("hi", "ひ");
        tmp.put("fu", "ふ");
        tmp.put("he", "へ");
        tmp.put("ho", "ほ");
        tmp.put("na", "な");
        tmp.put("ni", "に");
        tmp.put("nu", "ぬ");
        tmp.put("ne", "ね");
        tmp.put("no", "の");
        tmp.put("ma", "ま");
        tmp.put("mi", "み");
        tmp.put("mu", "む");
        tmp.put("me", "め");
        tmp.put("mo", "も");
        tmp.put("ra", "ら");
        tmp.put("ri", "り");
        tmp.put("ru", "る");
        tmp.put("re", "れ");
        tmp.put("ro", "ろ");
        tmp.put("wa", "わ");
        tmp.put("wo", "を");
        tmp.put("n", "ん");
        tmp.put("n$", "ん");

        tmp.put("ga", "が");
        tmp.put("gi", "ぎ");
        tmp.put("gu", "ぐ");
        tmp.put("ge", "げ");
        tmp.put("go", "ご");
        tmp.put("za", "ざ");
        tmp.put("ji", "じ"); // !
        tmp.put("di", "ぢ"); // !
        tmp.put("du", "づ"); // !
        tmp.put("zu", "ず"); // !
        tmp.put("ze", "ぜ");
        tmp.put("zo", "ぞ");
        tmp.put("da", "だ");
        tmp.put("de", "で");
        tmp.put("do", "ど");
        tmp.put("pa", "ぱ");
        tmp.put("pi", "ぴ");
        tmp.put("pu", "ぷ");
        tmp.put("pe", "ぺ");
        tmp.put("po", "ぽ");
        tmp.put("ba", "ば");
        tmp.put("bi", "び");
        tmp.put("bu", "ぶ");
        tmp.put("be", "べ");
        tmp.put("bo", "ぼ");

        tmp.put("ja", "じゃ");
        tmp.put("ju", "じゅ");
        tmp.put("jo", "じょ");
        tmp.put("gya", "ぎゃ");
        tmp.put("gyu", "ぎゅ");
        tmp.put("gyo", "ぎょ");
        tmp.put("bya", "びや");
        tmp.put("byu", "びゅ");
        tmp.put("byo", "びょ");
        tmp.put("pya", "ぴゃ");
        tmp.put("pyu", "ぴゅ");
        tmp.put("pyo", "ぴょ");
        tmp.put("dya", "ぢゃ");
        tmp.put("dyu", "ぢゅ");
        tmp.put("dyo", "ぢょ");

        tmp.put("kya", "きゃ");
        tmp.put("kyu", "きゅ");
        tmp.put("kyo", "きょ");
        tmp.put("sha", "しゃ");
        tmp.put("she", "しぇ");
        tmp.put("shu", "しゅ");
        tmp.put("sho", "しょ");
        tmp.put("cha", "ちゃ");
        tmp.put("che", "ちぇ");
        tmp.put("chu", "ちゅ");
        tmp.put("cho", "ちょ");
        tmp.put("nya", "にゃ");
        tmp.put("nyu", "にゅ");
        tmp.put("nyo", "にょ");
        tmp.put("hya", "ひゃ");
        tmp.put("hyu", "ひゅ");
        tmp.put("hyo", "ひょ");
        tmp.put("mya", "みゃ");
        tmp.put("myu", "みゅ");
        tmp.put("myo", "みょ");
        tmp.put("rya", "りゃ");
        tmp.put("ryu", "りゅ");
        tmp.put("ryo", "りょ");

        tmp.put("ya", "ゃ");
        tmp.put("yo", "ょ");
        tmp.put("yu", "ゅ");
        tmp.put("ya", "や");
        tmp.put("yo", "よ");
        tmp.put("yu", "ゆ");
        tmp.put("tu", "っ");
        tmp.put("ti", "てぃ");
        tmp.put("ー","ー");

        ROMAJI_HIRAGANA_LIST = Collections.unmodifiableMap(tmp);
    }

    static {
        Map<String, String> tmp = new HashMap<String, String>();

// hiragana

        tmp.put("あ", "a");
        tmp.put("ぁ", "a");
        tmp.put("い", "i");
        tmp.put("ぃ", "i");
        tmp.put("う", "u");
        tmp.put("ぅ", "u");
        tmp.put("え", "e");
        tmp.put("ぇ", "e");
        tmp.put("お", "o");
        tmp.put("ぉ", "o");
        tmp.put("か", "ka");
        tmp.put("き", "ki");
        tmp.put("く", "ku");
        tmp.put("け", "ke");
        tmp.put("こ", "ko");
        tmp.put("さ", "sa");
        tmp.put("し", "shi");
        tmp.put("す", "su");
        tmp.put("せ", "se");
        tmp.put("そ", "so");
        tmp.put("た", "ta");
        tmp.put("ち", "chi");
        tmp.put("つ", "tsu");
        tmp.put("て", "te");
        tmp.put("と", "to");
        tmp.put("は", "ha");
        tmp.put("ひ", "hi");
        tmp.put("ふ", "fu");
        tmp.put("へ", "he");
        tmp.put("ほ", "ho");
        tmp.put("な", "na");
        tmp.put("に", "ni");
        tmp.put("ぬ", "nu");
        tmp.put("ね", "ne");
        tmp.put("の", "no");
        tmp.put("ま", "ma");
        tmp.put("み", "mi");
        tmp.put("む", "mu");
        tmp.put("め", "me");
        tmp.put("も", "mo");
        tmp.put("ら", "ra");
        tmp.put("り", "ri");
        tmp.put("る", "ru");
        tmp.put("れ", "re");
        tmp.put("ろ", "ro");
        tmp.put("わ", "wa");
        tmp.put("を", "wo");
        tmp.put("ん", "n$");

        tmp.put("が", "ga");
        tmp.put("ぎ", "gi");
        tmp.put("ぐ", "gu");
        tmp.put("げ", "ge");
        tmp.put("ご", "go");
        tmp.put("ざ", "za");
        tmp.put("じ", "ji");
        tmp.put("ぢ", "di");
        tmp.put("づ", "du");
        tmp.put("ず", "zu");
        tmp.put("ぜ", "ze");
        tmp.put("ぞ", "zo");
        tmp.put("だ", "da");
        tmp.put("で", "de");
        tmp.put("ど", "do");
        tmp.put("ぱ", "pa");
        tmp.put("ぴ", "pi");
        tmp.put("ぷ", "pu");
        tmp.put("ぺ", "pe");
        tmp.put("ぽ", "po");
        tmp.put("ば", "ba");
        tmp.put("び", "bi");
        tmp.put("ぶ", "bu");
        tmp.put("べ", "be");
        tmp.put("ぼ", "bo");

        tmp.put("じゃ", "ja");
        tmp.put("じゅ", "ju");
        tmp.put("じょ", "jo");
        tmp.put("ぎゃ", "gya");
        tmp.put("ぎゅ", "gyu");
        tmp.put("ぎょ", "gyo");
        tmp.put("びや", "bya");
        tmp.put("びゅ", "byu");
        tmp.put("びょ", "byo");
        tmp.put("ぴゃ", "pya");
        tmp.put("ぴゅ", "pyu");
        tmp.put("ぴょ", "pyo");
        tmp.put("ぢゃ", "dya");
        tmp.put("ぢゅ", "dyu");
        tmp.put("ぢょ", "dyo");

        tmp.put("きゃ", "kya");
        tmp.put("きゅ", "kyu");
        tmp.put("きょ", "kyo");
        tmp.put("しゃ", "sha");
        tmp.put("しぇ", "she");
        tmp.put("しゅ", "shu");
        tmp.put("しょ", "sho");
        tmp.put("ちゃ", "cha");
        tmp.put("ちぇ", "che");
        tmp.put("ちゅ", "chu");
        tmp.put("ちょ", "cho");
        tmp.put("にゃ", "nya");
        tmp.put("にゅ", "nyu");
        tmp.put("にょ", "nyo");
        tmp.put("ひゃ", "hya");
        tmp.put("ひゅ", "hyu");
        tmp.put("ひょ", "hyo");
        tmp.put("みゃ", "mya");
        tmp.put("みゅ", "myu");
        tmp.put("みょ", "myo");
        tmp.put("りゃ", "rya");
        tmp.put("りゅ", "ryu");
        tmp.put("りょ", "ryo");

        tmp.put("ぇ", "e");
        tmp.put("ぉ", "o");
        tmp.put("ゃ", "ya");
        tmp.put("ょ", "yo");
        tmp.put("ゅ", "yu");
        tmp.put("や", "ya");
        tmp.put("よ", "yo");
        tmp.put("ゆ", "yu");
        tmp.put("っ", "tu");
        tmp.put("てぃ", "ti");


        // katakana

        tmp.put("ア", "a");
        tmp.put("イ", "i");
        tmp.put("ウ", "u");
        tmp.put("エ", "e");
        tmp.put("オ", "o");
        tmp.put("カ", "ka");
        tmp.put("キ", "ki");
        tmp.put("ク", "ku");
        tmp.put("ケ", "ke");
        tmp.put("コ", "ko");
        tmp.put("サ", "sa");
        tmp.put("シ", "shi");
        tmp.put("ス", "su");
        tmp.put("セ", "se");
        tmp.put("ソ", "so");
        tmp.put("タ", "ta");
        tmp.put("チ", "chi");
        tmp.put("ツ", "tsu");
        tmp.put("テ", "te");
        tmp.put("ト", "to");
        tmp.put("ハ", "ha");
        tmp.put("ヒ", "hi");
        tmp.put("フ", "fu");
        tmp.put("ヘ", "he");
        tmp.put("ホ", "ho");
        tmp.put("ナ", "na");
        tmp.put("ニ", "ni");
        tmp.put("ヌ", "nu");
        tmp.put("ネ", "ne");
        tmp.put("ノ", "no");
        tmp.put("マ", "ma");
        tmp.put("ミ", "mi");
        tmp.put("ム", "mu");
        tmp.put("メ", "me");
        tmp.put("モ", "mo");
        tmp.put("ラ", "ra");
        tmp.put("リ", "ri");
        tmp.put("ル", "ru");
        tmp.put("レ", "re");
        tmp.put("ロ", "ro");
        tmp.put("ワ", "wa");
        tmp.put("ヲ", "wo");
        tmp.put("ン", "n$");

        tmp.put("ガ", "ga");
        tmp.put("ギ", "gi");
        tmp.put("グ", "gu");
        tmp.put("ゲ", "ge");
        tmp.put("ゴ", "go");
        tmp.put("ザ", "za");
        tmp.put("ジ", "ji");
        tmp.put("ヅ", "du");
        tmp.put("ズ", "zu");
        tmp.put("ゼ", "ze");
        tmp.put("ゾ", "zo");
        tmp.put("ダ", "da");
        tmp.put("デ", "de");
        tmp.put("ヂ", "di");
        tmp.put("ド", "do");
        tmp.put("パ", "pa");
        tmp.put("ピ", "pi");
        tmp.put("プ", "pu");
        tmp.put("ペ", "pe");
        tmp.put("ポ", "po");
        tmp.put("バ", "ba");
        tmp.put("ビ", "bi");
        tmp.put("ブ", "bu");
        tmp.put("ベ", "be");
        tmp.put("ボ", "bo");

        tmp.put("ジャ", "ja");
        tmp.put("ジュ", "ju");
        tmp.put("ジョ", "jo");
        tmp.put("ギャ", "gya");
        tmp.put("ギュ", "gyu");
        tmp.put("ギョ", "gyo");
        tmp.put("ビャ", "bya");
        tmp.put("ビュ", "byu");
        tmp.put("ビョ", "byo");
        tmp.put("ピャ", "pya");
        tmp.put("ピュ", "pyu");
        tmp.put("ピョ", "pyo");
        tmp.put("ヂャ", "dya");
        tmp.put("ヂュ", "dyu");
        tmp.put("ヂョ", "dyo");

        tmp.put("キャ", "kya");
        tmp.put("キェ", "kye");
        tmp.put("キュ", "kyu");
        tmp.put("キョ", "kyo");
        tmp.put("シャ", "sha");
        tmp.put("シェ", "she");
        tmp.put("シュ", "shu");
        tmp.put("ショ", "sho");
        tmp.put("チャ", "cha");
        tmp.put("チェ", "che");
        tmp.put("チュ", "chu");
        tmp.put("チョ", "cho");
        tmp.put("ニャ", "nya");
        tmp.put("ニェ", "nye");
        tmp.put("ニュ", "nyu");
        tmp.put("ニョ", "nyo");
        tmp.put("ヒャ", "hya");
        tmp.put("ヒュ", "hyu");
        tmp.put("ヒェ", "hye");
        tmp.put("ヒョ", "hyo");
        tmp.put("ミャ", "mya");
        tmp.put("ミュ", "myu");
        tmp.put("ミェ", "mye");
        tmp.put("ミョ", "myo");
        tmp.put("リャ", "rya");
        tmp.put("リェ", "rye");
        tmp.put("リュ", "ryu");
        tmp.put("リョ", "ryo");
        tmp.put("ファ", "fa");
        tmp.put("フェ", "fe");
        tmp.put("フォ", "fo");

        tmp.put("ャ", "ya");
        tmp.put("ョ", "yo");
        tmp.put("ュ", "yu");
        tmp.put("ッ", "xtu");
        tmp.put("ヤ", "ya");
        tmp.put("ヨ", "yo");
        tmp.put("ユ", "yu");
        tmp.put("ヴァ", "va");
        tmp.put("ヴぃ", "vi");
        tmp.put("ヴ", "vu");
        tmp.put("ヴぇ", "ve");
        tmp.put("ヴぉ", "vo");
        tmp.put("ティ", "ti");

        tmp.put("ァ", "a");
        tmp.put("ィ", "i");
        tmp.put("ゥ", "u");
        tmp.put("ェ", "e");
        tmp.put("ォ", "o");
        tmp.put("・", "~");

        KANA_ROMAJI_LIST = Collections.unmodifiableMap(tmp);
    }

    static {
        List<GrammarObject> tmp = new ArrayList<GrammarObject>();

        tmp.add(new GrammarObject("aux", "だった", "だ", "past", true));
        tmp.add(new GrammarObject("aux", "でした", "です", "past", true));
        tmp.add(new GrammarObject("aux", "ました", "ます", "past", true));
        tmp.add(new GrammarObject("vs", "した", "する", "past", true));
        tmp.add(new GrammarObject("vs-i", "した", "する", "past", true));
        tmp.add(new GrammarObject("vk", "きた", "くる", "past", true));
        tmp.add(new GrammarObject("vk", "来た", "来る", "past", false));
        tmp.add(new GrammarObject("v5u", "った", "う", "past", true));
        tmp.add(new GrammarObject("v5u-s", "うた", "う", "past", true));
        tmp.add(new GrammarObject("v5k", "いた", "く", "past", true));
        tmp.add(new GrammarObject("v5k-s", "った", "く", "past", true));
        tmp.add(new GrammarObject("v5g", "いだ", "ぐ", "past", true));
        tmp.add(new GrammarObject("v5s", "した", "す", "past", true));
        tmp.add(new GrammarObject("v5t", "った", "つ", "past", true));
        tmp.add(new GrammarObject("v5n", "んだ", "ぬ", "past", true));
        tmp.add(new GrammarObject("v5b", "んだ", "ぶ", "past", true));
        tmp.add(new GrammarObject("v5m", "んだ", "む", "past", true));
        tmp.add(new GrammarObject("v5r", "った", "る", "past", true));
        tmp.add(new GrammarObject("v5r-i", "った", "る", "past", true));
        tmp.add(new GrammarObject("v5aru", "った", "る", "past", true));
        tmp.add(new GrammarObject("v5aru", "いらした", "いらっしゃる", "past", true));
        tmp.add(new GrammarObject("v1", "た", "る", "past", true));
        tmp.add(new GrammarObject("v1-s", "た", "る", "past", true));
        tmp.add(new GrammarObject("adj-i", "かった", "い", "past", true));
        tmp.add(new GrammarObject("adj-na", "だった", "な", "past", true));

        tmp.add(new GrammarObject("aux", "datta", "da", "past", false));
        tmp.add(new GrammarObject("aux", "deshita ", "desu", "past", false));
        tmp.add(new GrammarObject("aux", "mashita", "masu", "past", false));
        tmp.add(new GrammarObject("vs", "shita", "suru", "past", false));
        tmp.add(new GrammarObject("vs-i", "shita", "suru", "past", false));
        tmp.add(new GrammarObject("vk", "kita", "kuru", "past", false));
        tmp.add(new GrammarObject("v5u", "tta", "u", "past", false));
        tmp.add(new GrammarObject("v5u-s", "uta", "u", "past", false)); // trzeba szukać tylko verbów
        //tmp.add(new GrammarObject("v5u-s", "ota", "u"));
        tmp.add(new GrammarObject("v5k", "ita", "ku", "past", false));
        tmp.add(new GrammarObject("v5k-s", "tta", "ku", "past", false));
        tmp.add(new GrammarObject("v5g", "ida", "gu", "past", false));
        tmp.add(new GrammarObject("v5s", "shita", "su", "past", false));
        tmp.add(new GrammarObject("v5t", "tta", "tsu", "past", false));
        tmp.add(new GrammarObject("v5n", "nda", "nu", "past", false));
        tmp.add(new GrammarObject("v5b", "nda", "bu", "past", false));
        tmp.add(new GrammarObject("v5m", "nda", "mu", "past", false));
        tmp.add(new GrammarObject("v5r", "tta", "ru", "past", false));
        tmp.add(new GrammarObject("v5r-i", "tta", "ru", "past", false));
        tmp.add(new GrammarObject("v5aru", "tta", "ru", "past", false));
        tmp.add(new GrammarObject("v5aru", "irashita", "irassharu", "past", false));
        tmp.add(new GrammarObject("v1", "ta", "ru", "past", false));
        tmp.add(new GrammarObject("v1-s", "ta", "ru", "past", false));
        tmp.add(new GrammarObject("adj-i", "katta", "i", "past", false));
        tmp.add(new GrammarObject("adj-na", "datta", "na", "past", false));

        PASSIVES_LIST = Collections.unmodifiableList(tmp);
    }

    static {
        List<GrammarObject> tmp = new ArrayList<GrammarObject>();

        tmp.add(new GrammarObject("aux", "です", "だ", "polite", true));
        tmp.add(new GrammarObject("v5u", "います", "う", "polite", true));
        tmp.add(new GrammarObject("v5t", "ちます ", "つ", "polite", true));
        tmp.add(new GrammarObject("v5r", "ります", "る", "polite", true));
        tmp.add(new GrammarObject("v5k", "きます", "く", "polite", true));
        tmp.add(new GrammarObject("v5g", "ぎます", "ぐ", "polite", true));
        tmp.add(new GrammarObject("v5b", "びます", "ぶ", "polite", true));
        tmp.add(new GrammarObject("v5m", "みます", "む", "polite", true));
        tmp.add(new GrammarObject("v5s", "します", "す", "polite", true));
        tmp.add(new GrammarObject("v5k-s", "きます", "く", "polite", true));
        tmp.add(new GrammarObject("v1", "ます", "る", "polite", true));
        tmp.add(new GrammarObject("v1-s", "ます", "る", "polite", true));
        tmp.add(new GrammarObject("vs", "します", "する", "polite", true));
        tmp.add(new GrammarObject("vs-i", "します", "する", "polite", true));
        tmp.add(new GrammarObject("vk", "きます", "くる", "polite", true));
        tmp.add(new GrammarObject("vk", "来ます", "来る", "polite", false));
        tmp.add(new GrammarObject("v5aru", "ります", "る", "polite", true));
        tmp.add(new GrammarObject("v5r-i", "ります", "る", "polite", true));

        tmp.add(new GrammarObject("aux", "desu", "da", "polite", true));
        tmp.add(new GrammarObject("v5u", "imasu", "u", "polite", false));
        tmp.add(new GrammarObject("v5t", "chimasu", "tsu", "polite", false));
        tmp.add(new GrammarObject("v5r", "rimasu", "ru", "polite", false));
        tmp.add(new GrammarObject("v5k", "kimasu", "ku", "polite", false));
        tmp.add(new GrammarObject("v5k-s", "kimasu", "ku", "polite", false));
        tmp.add(new GrammarObject("v5g", "gimasu", "gu", "polite", false));
        tmp.add(new GrammarObject("v5b", "bimasu", "bu", "polite", false));
        tmp.add(new GrammarObject("v5m", "mimasu", "mu", "polite", false));
        tmp.add(new GrammarObject("v5s", "shimasu", "su", "polite", false));
        tmp.add(new GrammarObject("v1", "masu", "ru", "polite", false));
        tmp.add(new GrammarObject("v1-s", "masu", "ru", "polite", false));
        tmp.add(new GrammarObject("vs", "shimasu", "suru", "polite", false));
        tmp.add(new GrammarObject("vs-i", "shimasu", "suru", "polite", false));
        tmp.add(new GrammarObject("vk", "kimasu", "kuru", "polite", false));
        tmp.add(new GrammarObject("v5aru", "rimasu", "ru", "polite", false));
        tmp.add(new GrammarObject("v5r-i", "rimasu", "ru", "polite", false));

        POLITE_FORM_LIST = Collections.unmodifiableList(tmp);
    }

    static {
        List<GrammarObject> tmp = new ArrayList<GrammarObject>();

        tmp.add(new GrammarObject("adj-i", "く", "い", "adjective to adverb", true));
        tmp.add(new GrammarObject("adj-i", "ku", "i", "adjective to adverb", false));

        TO_ADV_FORM_LIST = Collections.unmodifiableList(tmp);
    }

    static {
        List<GrammarObject> tmp = new ArrayList<GrammarObject>();

        tmp.add(new GrammarObject("adj-i", "さ", "い", "adjective to noun", true));
        tmp.add(new GrammarObject("adj-i", "sa", "i", "adjective to noun", false));

        TO_NOUN_FORM_LIST = Collections.unmodifiableList(tmp);
    }

    static {
        List<GrammarObject> tmp = new ArrayList<GrammarObject>();

        tmp.add(new GrammarObject("aux", "ではない", "だ", "negative", true));
        tmp.add(new GrammarObject("aux", "じゃない ", "だ", "negative", false));
        tmp.add(new GrammarObject("aux", "ではありません", "です", "negative", true));
        tmp.add(new GrammarObject("aux", "じゃありません", "です", "negative", true));
        tmp.add(new GrammarObject("vs", "しない", "する", "negative", true));
        tmp.add(new GrammarObject("vs", "さない", "する", "negative", false));
        tmp.add(new GrammarObject("vs-i", "しない", "する", "negative", true));
        tmp.add(new GrammarObject("vs-i", "さない", "する", "negative", true));
        tmp.add(new GrammarObject("vk", "こない", "くる", "negative", true));
        tmp.add(new GrammarObject("vk", "来ない", "来る", "negative", false));
        tmp.add(new GrammarObject("v5u", "わない", "う", "negative", true));
        tmp.add(new GrammarObject("v5u-s", "わない", "う", "negative", true));
        tmp.add(new GrammarObject("v5k", "かない", "く", "negative", true));
        tmp.add(new GrammarObject("v5k-s", "かない", "く", "negative", true));
        tmp.add(new GrammarObject("v5g", "がない", "ぐ", "negative", true));
        tmp.add(new GrammarObject("v5s", "さない", "す", "negative", true));
        tmp.add(new GrammarObject("v5t", "たない", "つ", "negative", true));
        tmp.add(new GrammarObject("v5n", "なない", "ぬ", "negative", true));
        tmp.add(new GrammarObject("v5b", "ばない", "ぶ", "negative", true));
        tmp.add(new GrammarObject("v5m", "まない", "む", "negative", true));
        tmp.add(new GrammarObject("v5r", "らない", "る", "negative", true));
        tmp.add(new GrammarObject("v5r-i", "ない", "ある", "negative", true));
        tmp.add(new GrammarObject("v5aru", "らない", "る", "negative", true));
        tmp.add(new GrammarObject("v1-s", "ない", "る", "negative", true));
        tmp.add(new GrammarObject("v1", "ない", "る", "negative", true));
        tmp.add(new GrammarObject("adj-i", "くない", "い", "negative", true));
        tmp.add(new GrammarObject("adj-na", "ではない", "な", "negative", true));
        tmp.add(new GrammarObject("adj-na", "じゃない", "な", "negative", true));
        tmp.add(new GrammarObject("aux", "ません", "ます", "negative", true));

        tmp.add(new GrammarObject("aux", "de wa nai", "da", "negative", false));
        tmp.add(new GrammarObject("aux", "ja nai", "da", "negative", false));
        tmp.add(new GrammarObject("aux", "de wa arimasen", "desu", "negative", false));
        tmp.add(new GrammarObject("aux", "ja arimasen", "desu", "negative", false));
        tmp.add(new GrammarObject("vs", "shinai", "suru", "negative", false));
        tmp.add(new GrammarObject("vs", "sanai", "suru", "negative", false));
        tmp.add(new GrammarObject("vs-i", "shinai", "suru", "negative", false));
        tmp.add(new GrammarObject("vs-i", "sanai", "suru", "negative", false));
        tmp.add(new GrammarObject("vk", "konai", "kuru", "negative", false));
        tmp.add(new GrammarObject("v5u", "wanai", "u", "negative", false));
        tmp.add(new GrammarObject("v5u-s", "wanai", "u", "negative", false));
        tmp.add(new GrammarObject("v5k", "kanai", "ku", "negative", false));
        tmp.add(new GrammarObject("v5k-s", "kanai", "ku", "negative", false));
        tmp.add(new GrammarObject("v5g", "ganai", "gu", "negative", false));
        tmp.add(new GrammarObject("v5s", "sanai", "su", "negative", false));
        tmp.add(new GrammarObject("v5t", "tanai", "tsu", "negative", false));
        tmp.add(new GrammarObject("v5n", "nanai", "nu", "negative", false));
        tmp.add(new GrammarObject("v5b", "banai", "bu", "negative", false));
        tmp.add(new GrammarObject("v5m", "manai", "mu", "negative", false));
        tmp.add(new GrammarObject("v5r", "ranai", "ru", "negative", false));
        tmp.add(new GrammarObject("v5r-i", "nai", "aru", "negative", false));
        tmp.add(new GrammarObject("v5aru", "ranai", "ru", "negative", false));
        tmp.add(new GrammarObject("v1", "nai", "ru", "negative", false));
        tmp.add(new GrammarObject("v1-s", "nai", "ru", "negative", false));
        tmp.add(new GrammarObject("adj-i", "kunai", "i", "negative", false));
        tmp.add(new GrammarObject("adj-na", "de wa nai", "na", "negative", false));
        tmp.add(new GrammarObject("adj-na", "ja nai", "na", "negative", false));
        tmp.add(new GrammarObject("aux", "masen", "masu", "negative", false));

        // ZU-form
        tmp.add(new GrammarObject("v5u", "わず", "う", "negative", false));
        tmp.add(new GrammarObject("v5u-s", "わず", "う", "negative", false));
        tmp.add(new GrammarObject("v5k", "かず", "く", "negative", false));
        tmp.add(new GrammarObject("v5k-s", "かず", "く", "negative", false));
        tmp.add(new GrammarObject("v5g", "がず", "ぐ", "negative", false));
        tmp.add(new GrammarObject("v5s", "さず", "す", "negative", false));
        tmp.add(new GrammarObject("v5t", "たず", "つ", "negative", false));
        tmp.add(new GrammarObject("v5n", "なず", "ぬ", "negative", false));
        tmp.add(new GrammarObject("v5b", "ばず", "ぶ", "negative", false));
        tmp.add(new GrammarObject("v5m", "まず", "む", "negative", false));
        tmp.add(new GrammarObject("v5r", "らず", "る", "negative", false));
        tmp.add(new GrammarObject("v5r-i", "らず", "る", "negative", false));
        tmp.add(new GrammarObject("v5aru", "らず", "る", "negative", false));
        tmp.add(new GrammarObject("v1-s", "ず", "る", "negative", false));
        tmp.add(new GrammarObject("v1", "ず", "る", "negative", false));

        tmp.add(new GrammarObject("v5u", "wazu", "u", "negative", false));
        tmp.add(new GrammarObject("v5u-s", "wazu", "u", "negative", false));
        tmp.add(new GrammarObject("v5k", "kazu", "ku", "negative", false));
        tmp.add(new GrammarObject("v5k-s", "kazu", "ku", "negative", false));
        tmp.add(new GrammarObject("v5g", "gazu", "gu", "negative", false));
        tmp.add(new GrammarObject("v5s", "sazu", "su", "negative", false));
        tmp.add(new GrammarObject("v5t", "tazu", "tsu", "negative", false));
        tmp.add(new GrammarObject("v5n", "nazu", "nu", "negative", false));
        tmp.add(new GrammarObject("v5b", "bazu", "bu", "negative", false));
        tmp.add(new GrammarObject("v5m", "mazu", "mu", "negative", false));
        tmp.add(new GrammarObject("v5r", "razu", "ru", "negative", false));
        tmp.add(new GrammarObject("v5r-i", "razu", "ru", "negative", false));
        tmp.add(new GrammarObject("v5aru", "razu", "ru", "negative", false));
        tmp.add(new GrammarObject("v1-s", "zu", "ru", "negative", false));
        tmp.add(new GrammarObject("v1", "zu", "ru", "negative", false));

        NEGATIVE_FORM_LIST = Collections.unmodifiableList(tmp);
    }

    static {
        List<GrammarObject> tmp = new ArrayList<GrammarObject>();

        tmp.add(new GrammarObject("vs", "したり", "する", "-tari form", true));
        tmp.add(new GrammarObject("vs-i", "したり", "する", "-tari form", true));
        tmp.add(new GrammarObject("vk", "きたり", "くる", "-tari form", true));
        tmp.add(new GrammarObject("vk", "来たり", "来る", "-tari form", false));
        tmp.add(new GrammarObject("v5u", "ったり", "う", "-tari form", true));
        tmp.add(new GrammarObject("v5u-s", "うたり", "う", "-tari form", true));
        tmp.add(new GrammarObject("v5k", "いたり", "く", "-tari form", true));
        tmp.add(new GrammarObject("v5k-s", "ったり", "く", "-tari form", true));
        tmp.add(new GrammarObject("v5g", "いだり", "ぐ", "-tari form", true));
        tmp.add(new GrammarObject("v5s", "したり", "す", "-tari form", true));
        tmp.add(new GrammarObject("v5t", "ったり", "つ", "-tari form", true));
        tmp.add(new GrammarObject("v5n", "んだり", "ぬ", "-tari form", true));
        tmp.add(new GrammarObject("v5b", "んだり", "ぶ", "-tari form", true));
        tmp.add(new GrammarObject("v5m", "んだり", "む", "-tari form", true));
        tmp.add(new GrammarObject("v5r", "ったり", "る", "-tari form", true));
        tmp.add(new GrammarObject("v5r-i", "ったり", "る", "-tari form", true));
        tmp.add(new GrammarObject("v5aru", "ったり", "る", "-tari form", true));
        tmp.add(new GrammarObject("v5aru", "いらしたり", "いらっしゃる", "-tari form", true));
        tmp.add(new GrammarObject("v1", "たり", "る", "-tari form", true));
        tmp.add(new GrammarObject("v1-s", "たり", "る", "-tari form", true));
        tmp.add(new GrammarObject("adj-i", "かったり", "い", "-tari form", true));
        tmp.add(new GrammarObject("adj-na", "だったり", "な", "-tari form", true));

        tmp.add(new GrammarObject("vs", "shitari", "suru", "-tari form", false));
        tmp.add(new GrammarObject("vs-i", "shitari", "suru", "-tari form", false));
        tmp.add(new GrammarObject("vk", "kitari", "kuru", "-tari form", false));
        tmp.add(new GrammarObject("v5u", "ttari", "u", "-tari form", false));
        //tmp.add(new GrammarObject("v5u-s", "utari", "u", "-tari form", false)); // trzeba szukać tylko verbów
        //tmp.add(new GrammarObject("v5u-s", "ota", "u"));
        tmp.add(new GrammarObject("v5k", "itari", "ku", "-tari form", false));
        tmp.add(new GrammarObject("v5k-s", "ttari", "ku", "-tari form", false));
        tmp.add(new GrammarObject("v5g", "idari", "gu", "-tari form", false));
        tmp.add(new GrammarObject("v5s", "shitari", "su", "-tari form", false));
        tmp.add(new GrammarObject("v5t", "ttari", "tsu", "-tari form", false));
        tmp.add(new GrammarObject("v5n", "ndari", "nu", "-tari form", false));
        tmp.add(new GrammarObject("v5b", "ndari", "bu", "-tari form", false));
        tmp.add(new GrammarObject("v5m", "ndari", "mu", "-tari form", false));
        tmp.add(new GrammarObject("v5r", "ttari", "ru", "-tari form", false));
        tmp.add(new GrammarObject("v5r-i", "ttari", "ru", "-tari form", false));
        tmp.add(new GrammarObject("v5aru", "ttari", "ru", "-tari form", false));
        tmp.add(new GrammarObject("v5aru", "irashitari", "irassharu", "-tari form", false));
        tmp.add(new GrammarObject("v1", "tari", "ru", "-tari form", false));
        tmp.add(new GrammarObject("v1-s", "tari", "ru", "-tari form", false));
        tmp.add(new GrammarObject("adj-i", "kattari", "i", "-tari form", false));
        tmp.add(new GrammarObject("adj-na", "dattari", "na", "-tari form", false));

        TARI_FORM_LIST = Collections.unmodifiableList(tmp);
    }

    static {
        List<GrammarObject> tmp = new ArrayList<GrammarObject>();

        tmp.add(new GrammarObject("vs", "したい", "する", "-tai form", true));
        tmp.add(new GrammarObject("vs-i", "したい", "する", "-tai form", true));
        tmp.add(new GrammarObject("vk", "きたい", "くる", "-tai form", true));
        tmp.add(new GrammarObject("vk", "来たい", "来る", "-tai form", false));
        tmp.add(new GrammarObject("v5u", "いたい", "う", "-tai form", true));
        tmp.add(new GrammarObject("v5u-s", "いたい", "う", "-tai form", true));
        tmp.add(new GrammarObject("v5k", "きたい", "く", "-tai form", true));
        tmp.add(new GrammarObject("v5k-s", "きたい", "く", "-tai form", true));
        tmp.add(new GrammarObject("v5g", "ぎたい", "ぐ", "-tai form", true));
        tmp.add(new GrammarObject("v5s", "したい", "す", "-tai form", true));
        tmp.add(new GrammarObject("v5t", "ちたい", "つ", "-tai form", true));
        tmp.add(new GrammarObject("v5n", "にたい", "ぬ", "-tai form", true));
        tmp.add(new GrammarObject("v5b", "びたい", "ぶ", "-tai form", true));
        tmp.add(new GrammarObject("v5m", "みたい", "む", "-tai form", true));
        tmp.add(new GrammarObject("v5r", "りたい", "る", "-tai form", true));
        tmp.add(new GrammarObject("v5r-i", "りたい", "る", "-tai form", true));
        tmp.add(new GrammarObject("v5aru", "いたい", "る", "-tai form", true));
        tmp.add(new GrammarObject("v1", "たい", "る", "-tai form", true));
        tmp.add(new GrammarObject("v1-s", "たい", "る", "-tai form", true));

        tmp.add(new GrammarObject("vs", "shitai", "suru", "-tai form", false));
        tmp.add(new GrammarObject("vs-i", "shitai", "suru", "-tai form", false));
        tmp.add(new GrammarObject("vk", "kitai", "kuru", "-tai form", false));
        tmp.add(new GrammarObject("v5u", "itai", "u", "-tai form", false));
        tmp.add(new GrammarObject("v5u-s", "itai", "u", "-tai form", false));
        tmp.add(new GrammarObject("v5k", "kitai", "ku", "-tai form", false));
        tmp.add(new GrammarObject("v5k-s", "kitai", "ku", "-tai form", false));
        tmp.add(new GrammarObject("v5g", "gitai", "gu", "-tai form", false));
        tmp.add(new GrammarObject("v5s", "shitai", "su", "-tai form", false));
        tmp.add(new GrammarObject("v5t", "chitai", "tsu", "-tai form", false));
        tmp.add(new GrammarObject("v5n", "nitai", "nu", "-tai form", false));
        tmp.add(new GrammarObject("v5b", "bitai", "bu", "-tai form", false));
        tmp.add(new GrammarObject("v5m", "mitai", "mu", "-tai form", false));
        tmp.add(new GrammarObject("v5r", "ritai", "ru", "-tai form", false));
        tmp.add(new GrammarObject("v5r-i", "ritai", "ru", "-tai form", false));
        tmp.add(new GrammarObject("v5aru", "itai", "ru", "-tai form", false));
        tmp.add(new GrammarObject("v1", "tai", "ru", "-tai form", false));
        tmp.add(new GrammarObject("v1-s", "tai", "ru", "-tai form", false));

        TAI_FORM_LIST = Collections.unmodifiableList(tmp);
    }

    static {
        List<GrammarObject> tmp = new ArrayList<GrammarObject>();

        tmp.add(new GrammarObject("aux", "であり ", "だ", "-i form", true));
        tmp.add(new GrammarObject("aux", "であり", "です", "-i form", true));
        tmp.add(new GrammarObject("vs", "し", "する", "-i form", true));
        tmp.add(new GrammarObject("vs-i", "し", "する", "-i form", true));
        tmp.add(new GrammarObject("vk", "き", "くる", "-i form", true));
        tmp.add(new GrammarObject("vk", "来", "来る", "-i form", false));
        tmp.add(new GrammarObject("v5u", "い", "う", "-i form", true));
        tmp.add(new GrammarObject("v5u-s", "い", "う", "-i form", true));
        tmp.add(new GrammarObject("v5k", "き", "く", "-i form", true));
        tmp.add(new GrammarObject("v5k-s", "き", "く", "-i form", true));
        tmp.add(new GrammarObject("v5g", "ぎ", "ぐ", "-i form", true));
        tmp.add(new GrammarObject("v5s", "し", "す", "-i form", true));
        tmp.add(new GrammarObject("v5t", "ち", "つ", "-i form", true));
        tmp.add(new GrammarObject("v5n", "に", "ぬ", "-i form", true));
        tmp.add(new GrammarObject("v5b", "び", "ぶ", "-i form", true));
        tmp.add(new GrammarObject("v5m", "み", "む", "-i form", true));
        tmp.add(new GrammarObject("v5r", "り", "る", "-i form", true));
        tmp.add(new GrammarObject("v5r-i", "り", "る", "-i form", true));
        tmp.add(new GrammarObject("v5aru", "い", "る", "-i form", true));


        for (int i = InputUtils.numberHiraganaStart + 1; i <= InputUtils.numberHiraganaSimpleEnd; i++) {
            char[] s = Character.toChars(i);
            String hiragana = String.valueOf(s);

            tmp.add(new GrammarObject("v1", hiragana, hiragana + "る", "-i form", true));
            tmp.add(new GrammarObject("v1-s",  hiragana, hiragana + "る", "-i form", true));
        }


        tmp.add(new GrammarObject("aux", "de ari", "da", "-i form", false));
        tmp.add(new GrammarObject("aux", "de ari", "desu", "-i form", false));
        tmp.add(new GrammarObject("vs", "shi", "suru", "-i form", false));
        tmp.add(new GrammarObject("vs-i", "shi", "suru", "-i form", false));
        tmp.add(new GrammarObject("vk", "ki", "kuru", "-i form", false));
        tmp.add(new GrammarObject("v5u", "i", "u", "-i form", false));
        tmp.add(new GrammarObject("v5u-s", "i", "u", "-i form", false));
        tmp.add(new GrammarObject("v5k", "ki", "ku", "-i form", false));
        tmp.add(new GrammarObject("v5k-s", "ki", "ku", "-i form", false));
        tmp.add(new GrammarObject("v5g", "gi", "gu", "-i form", false));
        tmp.add(new GrammarObject("v5s", "shi", "su", "-i form", false));
        tmp.add(new GrammarObject("v5t", "chi", "tsu", "-i form", false));
        tmp.add(new GrammarObject("v5n", "ni", "nu", "-i form", false));
        tmp.add(new GrammarObject("v5b", "bi", "bu", "-i form", false));
        tmp.add(new GrammarObject("v5m", "mi", "mu", "-i form", false));
        tmp.add(new GrammarObject("v5r", "ri", "ru", "-i form", false));
        tmp.add(new GrammarObject("v5r-i", "ri", "ru", "-i form", false));
        tmp.add(new GrammarObject("v5aru", "i", "ru", "-i form", false));

        for (int i = InputUtils.numberHiraganaStart + 1; i <= InputUtils.numberHiraganaSimpleEnd; i++) {

            char c = (char)i;
            String romaji = InputUtils.getRomaji(String.valueOf(c), false);

            tmp.add(new GrammarObject("v1", romaji, romaji + "る", "-i form", true));
            tmp.add(new GrammarObject("v1-s", romaji, romaji + "る", "-i form", true));
        }

        //tmp.add(new GrammarObject("v1", "_", "ru", "-i form", false));
        //tmp.add(new GrammarObject("v1-s", "_", "ru", "-i form", false));

        I_FORM_LIST = Collections.unmodifiableList(tmp);
    }

    static {
        List<GrammarObject> tmp = new ArrayList<GrammarObject>();

        tmp.add(new GrammarObject("aux", "で", "だ", "-te form", true));
        tmp.add(new GrammarObject("vs", "して", "する", "-te form", true));
        tmp.add(new GrammarObject("vs-i", "して", "する", "-te form", true));
        tmp.add(new GrammarObject("vk", "きて", "くる", "-te form", true));
        tmp.add(new GrammarObject("vk", "来て", "来る", "-te form", false));
        tmp.add(new GrammarObject("aux", "まして", "ます", "-te form", true));
        tmp.add(new GrammarObject("v5u", "って", "う", "-te form", true));
        tmp.add(new GrammarObject("v5u-s", "うて", "う", "-te form", true));
        tmp.add(new GrammarObject("v5k", "いて", "く", "-te form", true));
        tmp.add(new GrammarObject("v5k-s", "いって", "いく", "-te form", true));
        tmp.add(new GrammarObject("v5g", "いで", "ぐ", "-te form", true));
        tmp.add(new GrammarObject("v5s", "して", "す", "-te form", true));
        tmp.add(new GrammarObject("v5t", "って", "つ", "-te form", true));
        tmp.add(new GrammarObject("v5n", "んで", "ぬ", "-te form", true));
        tmp.add(new GrammarObject("v5b", "んで", "ぶ", "-te form", true));
        tmp.add(new GrammarObject("v5m", "んで", "む", "-te form", true));
        tmp.add(new GrammarObject("v5r", "って", "る", "-te form", true));
        tmp.add(new GrammarObject("v5r-i", "って", "る", "-te form", true));
        tmp.add(new GrammarObject("v5aru", "って", "る", "-te form", true));
        tmp.add(new GrammarObject("v1", "て", "る", "-te form", true));
        tmp.add(new GrammarObject("v1-s", "て", "る", "-te form", true));
        tmp.add(new GrammarObject("adj-i", "くて", "い", "-te form", true));
        tmp.add(new GrammarObject("adj-i", "ないで", "ない", "-te form", false));
        tmp.add(new GrammarObject("adj-na", "で", "な", "-te form", true));

        tmp.add(new GrammarObject("aux", "de", "da", "-te form", false));
        tmp.add(new GrammarObject("vs", "shite", "suru", "-te form", false));
        tmp.add(new GrammarObject("vs-i", "shite", "suru", "-te form", false));
        tmp.add(new GrammarObject("vk", "kite", "kuru", "-te form", false));
        tmp.add(new GrammarObject("aux", "mashite", "masu", "-te form", false));
        tmp.add(new GrammarObject("v5u", "tte", "u", "-te form", false));
        tmp.add(new GrammarObject("v5u-s", "ute", "u", "-te form", false));
        tmp.add(new GrammarObject("v5k", "ite", "ku", "-te form", false));
        tmp.add(new GrammarObject("v5k-s", "itte", "iku", "-te form", false));
        tmp.add(new GrammarObject("v5g", "ide", "gu", "-te form", false));
        tmp.add(new GrammarObject("v5s", "shite", "su", "-te form", false));
        tmp.add(new GrammarObject("v5t", "tte", "tsu", "-te form", false));
        tmp.add(new GrammarObject("v5n", "nde", "nu", "-te form", false));
        tmp.add(new GrammarObject("v5b", "nde", "bu", "-te form", false));
        tmp.add(new GrammarObject("v5m", "nde", "mu", "-te form", false));
        tmp.add(new GrammarObject("v5r", "tte", "ru", "-te form", false));
        tmp.add(new GrammarObject("v5r-i", "tte", "ru", "-te form", false));
        tmp.add(new GrammarObject("v5aru", "tte", "ru", "-te form", false));
        tmp.add(new GrammarObject("v1", "te", "ru", "-te form", false));
        tmp.add(new GrammarObject("v1-s", "te", "ru", "-te form", false));
        tmp.add(new GrammarObject("adj-i", "kute", "i", "-te form", false));
        tmp.add(new GrammarObject("adj-i", "naide", "nai", "-te form", false));
        tmp.add(new GrammarObject("adj-na", "de", "na", "-te form", false));

        TE_FORM_LIST = Collections.unmodifiableList(tmp);
    }

    static {
        List<GrammarObject> tmp = new ArrayList<GrammarObject>();

        tmp.add(new GrammarObject("vs", "している", "する", "progressive", true));
        tmp.add(new GrammarObject("vs-i", "している", "する", "progressive", true));
        tmp.add(new GrammarObject("vk", "きている", "くる", "progressive", true));
        tmp.add(new GrammarObject("vk", "来ている", "来る", "progressive", false));
        tmp.add(new GrammarObject("", "ましている", "ます", "progressive", true));
        tmp.add(new GrammarObject("v5u", "っている", "う", "progressive", true));
        tmp.add(new GrammarObject("v5u-s", "うている", "う", "progressive", true));
        tmp.add(new GrammarObject("v5k", "いている", "く", "progressive", true));
        tmp.add(new GrammarObject("v5k-s", "いっている", "いく", "progressive", true));
        tmp.add(new GrammarObject("v5k-s", "行っている", "行く", "progressive", false));
        tmp.add(new GrammarObject("v5g", "いでいる", "ぐ", "progressive", true));
        tmp.add(new GrammarObject("v5s", "している", "す", "progressive", true));
        tmp.add(new GrammarObject("v5t", "っている", "つ", "progressive", true));
        tmp.add(new GrammarObject("v5n", "んでいる", "ぬ", "progressive", true));
        tmp.add(new GrammarObject("v5b", "んでいる", "ぶ", "progressive", true));
        tmp.add(new GrammarObject("v5m", "んでいる", "む", "progressive", true));
        tmp.add(new GrammarObject("v5r", "っている", "る", "progressive", true));
        tmp.add(new GrammarObject("v5r-i", "っている", "る", "progressive", true));
        tmp.add(new GrammarObject("v5aru", "っている", "る", "progressive", true));
        tmp.add(new GrammarObject("v1", "ている", "る", "progressive", true));
        tmp.add(new GrammarObject("v1-s", "ている", "る", "progressive", true));

        tmp.add(new GrammarObject("vs", "shiteiru", "suru", "progressive", false));
        tmp.add(new GrammarObject("vs-i", "shiteiru", "suru", "progressive", false));
        tmp.add(new GrammarObject("vk", "kiteiru", "kuru", "progressive", false));
        tmp.add(new GrammarObject("aux", "mashiteiru", "masu", "progressive", false));
        tmp.add(new GrammarObject("v5u", "tteiru", "u", "progressive", false));
        tmp.add(new GrammarObject("v5u-s", "uteiru", "u", "progressive", false));
        tmp.add(new GrammarObject("v5k", "iteiru", "ku", "progressive", false));
        tmp.add(new GrammarObject("v5k-s", "itteiru", "iku", "progressive", false));
        tmp.add(new GrammarObject("v5g", "ideiru", "gu", "progressive", false));
        tmp.add(new GrammarObject("v5s", "shiteiru", "su", "progressive", false));
        tmp.add(new GrammarObject("v5t", "tteiru", "tsu", "progressive", false));
        tmp.add(new GrammarObject("v5n", "ndeiru", "nu", "progressive", false));
        tmp.add(new GrammarObject("v5b", "ndeiru", "bu", "progressive", false));
        tmp.add(new GrammarObject("v5m", "ndeiru", "mu", "progressive", false));
        tmp.add(new GrammarObject("v5r", "tteiru", "ru", "progressive", false));
        tmp.add(new GrammarObject("v5r-i", "tteiru", "ru", "progressive", false));
        tmp.add(new GrammarObject("v5aru", "tteiru", "ru", "progressive", false));
        tmp.add(new GrammarObject("v1", "teiru", "ru", "progressive", false));
        tmp.add(new GrammarObject("v1-s", "teiru", "ru", "progressive", false));

        tmp.add(new GrammarObject("vs", "してる", "する", "progressive", false));
        tmp.add(new GrammarObject("vs-i", "してる", "する", "progressive", false));
        tmp.add(new GrammarObject("vk", "きてる", "くる", "progressive", true));
        tmp.add(new GrammarObject("vk", "来てる", "来る", "progressive", false));
        tmp.add(new GrammarObject("", "ましてる", "ます", "progressive", false));
        tmp.add(new GrammarObject("v5u", "ってる", "う", "progressive", false));
        tmp.add(new GrammarObject("v5u-s", "うてる", "う", "progressive", false));
        tmp.add(new GrammarObject("v5k", "いてる", "く", "progressive", false));
        tmp.add(new GrammarObject("v5k-s", "いってる", "いく", "progressive", false));
        tmp.add(new GrammarObject("v5k-s", "行ってる", "行く", "progressive", false));
        tmp.add(new GrammarObject("v5g", "いでる", "ぐ", "progressive", false));
        tmp.add(new GrammarObject("v5s", "してる", "す", "progressive", false));
        tmp.add(new GrammarObject("v5t", "ってる", "つ", "progressive", false));
        tmp.add(new GrammarObject("v5n", "んでる", "ぬ", "progressive", false));
        tmp.add(new GrammarObject("v5b", "んでる", "ぶ", "progressive", false));
        tmp.add(new GrammarObject("v5m", "んでる", "む", "progressive", false));
        tmp.add(new GrammarObject("v5r", "ってる", "る", "progressive", false));
        tmp.add(new GrammarObject("v5r-i", "ってる", "る", "progressive", false));
        tmp.add(new GrammarObject("v5aru", "ってる", "る", "progressive", false));
        tmp.add(new GrammarObject("v1", "てる", "る", "progressive", false));
        tmp.add(new GrammarObject("v1-s", "てる", "る", "progressive", false));

        tmp.add(new GrammarObject("vs", "shiteru", "suru", "progressive", false));
        tmp.add(new GrammarObject("vs-i", "shiteru", "suru", "progressive", false));
        tmp.add(new GrammarObject("vk", "kiteru", "kuru", "progressive", false));
        tmp.add(new GrammarObject("aux", "mashiteru", "masu", "progressive", false));
        tmp.add(new GrammarObject("v5u", "tteru", "u", "progressive", false));
        tmp.add(new GrammarObject("v5u-s", "uteru", "u", "progressive", false));
        tmp.add(new GrammarObject("v5k", "iteru", "ku", "progressive", false));
        tmp.add(new GrammarObject("v5k-s", "itteru", "iku", "progressive", false));
        tmp.add(new GrammarObject("v5g", "ideru", "gu", "progressive", false));
        tmp.add(new GrammarObject("v5s", "shiteru", "su", "progressive", false));
        tmp.add(new GrammarObject("v5t", "tteru", "tsu", "progressive", false));
        tmp.add(new GrammarObject("v5n", "nderu", "nu", "progressive", false));
        tmp.add(new GrammarObject("v5b", "nderu", "bu", "progressive", false));
        tmp.add(new GrammarObject("v5m", "nderu", "mu", "progressive", false));
        tmp.add(new GrammarObject("v5r", "tteru", "ru", "progressive", false));
        tmp.add(new GrammarObject("v5r-i", "tteru", "ru", "progressive", false));
        tmp.add(new GrammarObject("v5aru", "tteru", "ru", "progressive", false));
        tmp.add(new GrammarObject("v1", "teru", "ru", "progressive", false));
        tmp.add(new GrammarObject("v1-s", "teru", "ru", "progressive", false));


        TEIRU_FORM_LIST = Collections.unmodifiableList(tmp);
    }

    static {
        List<GrammarObject> tmp = new ArrayList<GrammarObject>();

        tmp.add(new GrammarObject("vs", "しとく", "する", "-teoku short form", false));
        tmp.add(new GrammarObject("vs-i", "しとく", "する", "-teoku short form", false));
        tmp.add(new GrammarObject("v5u", "っとく", "う", "-teoku short form", false));
        tmp.add(new GrammarObject("v5u-s", "うとく", "う", "-teoku short form", false));
        tmp.add(new GrammarObject("v5k", "いとく", "く", "-teoku short form", false));
        tmp.add(new GrammarObject("v5k-s", "いっとく", "いく", "-teoku short form", false));
        tmp.add(new GrammarObject("v5g", "いどく", "ぐ", "-teoku short form", false));
        tmp.add(new GrammarObject("v5s", "しとく", "す", "-teoku short form", false));
        tmp.add(new GrammarObject("v5t", "っとく", "つ", "-teoku short form", false));
        tmp.add(new GrammarObject("v5n", "んどく", "ぬ", "-teoku short form", false));
        tmp.add(new GrammarObject("v5b", "んどく", "ぶ", "-teoku short form", false));
        tmp.add(new GrammarObject("v5m", "んどく", "む", "-teoku short form", false));
        tmp.add(new GrammarObject("v5r", "っとく", "る", "-teoku short form", false));
        tmp.add(new GrammarObject("v5r-i", "っとく", "る", "-teoku short form", false));
        tmp.add(new GrammarObject("v1", "とく", "る", "-teoku short form", false));
        tmp.add(new GrammarObject("v1-s", "とく", "る", "-teoku short form", false));

        tmp.add(new GrammarObject("vs", "shitoku", "suru", "-teoku short form", false));
        tmp.add(new GrammarObject("vs-i", "shitoku", "suru", "-teoku short form", false));
        tmp.add(new GrammarObject("v5u", "ttoku", "u", "-teoku short form", false));
        tmp.add(new GrammarObject("v5u-s", "utoku", "u", "-teoku short form", false));
        tmp.add(new GrammarObject("v5k", "itoku", "ku", "-teoku short form", false));
        tmp.add(new GrammarObject("v5k-s", "ittoku", "iku", "-teoku short form", false));
        tmp.add(new GrammarObject("v5g", "idoku", "gu", "-teoku short form", false));
        tmp.add(new GrammarObject("v5s", "shitoku", "su", "-teoku short form", false));
        tmp.add(new GrammarObject("v5t", "ttoku", "tsu", "-teoku short form", false));
        tmp.add(new GrammarObject("v5n", "ndoku", "nu", "-teoku short form", false));
        tmp.add(new GrammarObject("v5b", "ndoku", "bu", "-teoku short form", false));
        tmp.add(new GrammarObject("v5m", "ndoku", "mu", "-teoku short form", false));
        tmp.add(new GrammarObject("v5r", "ttoku", "ru", "-teoku short form", false));
        tmp.add(new GrammarObject("v5r-i", "ttoku", "ru", "-teoku short form", false));
        tmp.add(new GrammarObject("v1", "toku", "ru", "-teoku short form", false));
        tmp.add(new GrammarObject("v1-s", "toku", "ru", "-teoku short form", false));

        TEOKU_FORM_LIST = Collections.unmodifiableList(tmp);
    }

    static {
        List<GrammarObject> tmp = new ArrayList<GrammarObject>();

        tmp.add(new GrammarObject("vs", "出来る", "する", "potential", false));
        tmp.add(new GrammarObject("vs", "できる", "する", "potential", true));
        tmp.add(new GrammarObject("vs", "せる", "する", "potential", false));
        tmp.add(new GrammarObject("vs", "せられる", "する", "potential", false));
        tmp.add(new GrammarObject("vs-i", "出来る", "する", "potential", false));
        tmp.add(new GrammarObject("vs-i", "できる", "する", "potential", true));
        tmp.add(new GrammarObject("vs-i", "せる", "する", "potential", false));
        tmp.add(new GrammarObject("vs-i", "せられる", "する", "potential", false));
        tmp.add(new GrammarObject("vk", "こられる", "くる", "potential", true));
        tmp.add(new GrammarObject("vk", "これる", "くる", "potential", true));
        tmp.add(new GrammarObject("vk", "来られる", "来る", "potential", false));
        tmp.add(new GrammarObject("vk", "来れる", "来る", "potential", false));
        tmp.add(new GrammarObject("v5u", "える", "う", "potential", true));
        tmp.add(new GrammarObject("v5u-s", "える", "う", "potential", true));
        tmp.add(new GrammarObject("v5k", "ける", "く", "potential", true));
        tmp.add(new GrammarObject("v5k-s", "ける", "く", "potential", true));
        tmp.add(new GrammarObject("v5g", "げる", "ぐ", "potential", true));
        tmp.add(new GrammarObject("v5s", "せる", "す", "potential", true));
        tmp.add(new GrammarObject("v5t", "てる", "つ", "potential", true));
        tmp.add(new GrammarObject("v5n", "ねる", "ぬ", "potential", true));
        tmp.add(new GrammarObject("v5b", "べる", "ぶ", "potential", true));
        tmp.add(new GrammarObject("v5m", "める", "む", "potential", true));
        tmp.add(new GrammarObject("v5r", "れる", "る", "potential", true));
        tmp.add(new GrammarObject("v5r-i", "あり得る", "ある", "potential", true));
        tmp.add(new GrammarObject("v5aru", "り得る", "る", "potential", true));
        tmp.add(new GrammarObject("v1", "られる", "る", "potential", true)); // official
        tmp.add(new GrammarObject("v1", "れる", "る", "potential", false));
        tmp.add(new GrammarObject("v1-s", "られる", "る", "potential", true)); // official
        tmp.add(new GrammarObject("v1-s", "れる", "る", "potential", false));

        tmp.add(new GrammarObject("vs", "dekiru", "suru", "potential", false));
        tmp.add(new GrammarObject("vs", "seru", "suru", "potential", false));
        tmp.add(new GrammarObject("vs", "serareru", "suru", "potential", false));
        tmp.add(new GrammarObject("vs-i", "dekiru", "suru", "potential", false));
        tmp.add(new GrammarObject("vs-i", "seru", "suru", "potential", false));
        tmp.add(new GrammarObject("vs-i", "serareru", "suru", "potential", false));
        tmp.add(new GrammarObject("vk", "korareru", "kuru", "potential", false));
        tmp.add(new GrammarObject("vk", "koreru", "kuru", "potential", false));
        tmp.add(new GrammarObject("v5u", "eru", "u", "potential", false));
        tmp.add(new GrammarObject("v5u-s", "eru", "u", "potential", false));
        tmp.add(new GrammarObject("v5k", "keru", "ku", "potential", false));
        tmp.add(new GrammarObject("v5k-s", "keru", "ku", "potential", false));
        tmp.add(new GrammarObject("v5g", "geru", "gu", "potential", false));
        tmp.add(new GrammarObject("v5s", "seru", "su", "potential", false));
        tmp.add(new GrammarObject("v5t", "teru", "tsu", "potential", false));
        tmp.add(new GrammarObject("v5n", "neru", "nu", "potential", false));
        tmp.add(new GrammarObject("v5b", "beru", "bu", "potential", false));
        tmp.add(new GrammarObject("v5m", "meru", "mu", "potential", false));
        tmp.add(new GrammarObject("v5r", "reru", "ru", "potential", false));
        tmp.add(new GrammarObject("v5r-i", "arieru", "aru", "potential", false));
        tmp.add(new GrammarObject("v5aru", "rieru", "ru", "potential", false));
        tmp.add(new GrammarObject("v1", "rareru", "ru", "potential", false));
        tmp.add(new GrammarObject("v1", "reru", "ru", "potential", false));
        tmp.add(new GrammarObject("v1-s", "rareru", "ru", "potential", false));
        tmp.add(new GrammarObject("v1-s", "reru", "ru", "potential", false));

        POTENTIAL_FORM_LIST = Collections.unmodifiableList(tmp);
    }

    static {
        List<GrammarObject> tmp = new ArrayList<GrammarObject>();

        tmp.add(new GrammarObject("vs", "される", "する", "passive", true));
        tmp.add(new GrammarObject("vs-i", "される", "する", "passive", true));
        tmp.add(new GrammarObject("vk", "こられる", "くる", "passive", true));
        tmp.add(new GrammarObject("vk", "来られる", "来る", "passive", false));
        tmp.add(new GrammarObject("v5u", "われる", "う", "passive", true));
        tmp.add(new GrammarObject("v5u-s", "われる", "う", "passive", true));
        tmp.add(new GrammarObject("v5k", "かれる", "く", "passive", true));
        tmp.add(new GrammarObject("v5k-s", "かれる", "く", "passive", true));
        tmp.add(new GrammarObject("v5g", "がれる", "ぐ", "passive", true));
        tmp.add(new GrammarObject("v5s", "される", "す", "passive", true));
        tmp.add(new GrammarObject("v5t", "たれる", "つ", "passive", true));
        tmp.add(new GrammarObject("v5n", "なれる", "ぬ", "passive", true));
        tmp.add(new GrammarObject("v5b", "ばれる", "ぶ", "passive", true));
        tmp.add(new GrammarObject("v5m", "まれる", "む", "passive", true));
        tmp.add(new GrammarObject("v5r", "られる", "る", "passive", true));
        tmp.add(new GrammarObject("v1", "られる", "る", "passive", true));
        tmp.add(new GrammarObject("v1-s", "られる", "る", "passive", true));

        tmp.add(new GrammarObject("vs", "sareru", "suru", "passive", false));
        tmp.add(new GrammarObject("vs-i", "sareru", "suru", "passive", false));
        tmp.add(new GrammarObject("vk", "korareru", "kuru", "passive", false));
        tmp.add(new GrammarObject("v5u", "wareru", "u", "passive", false));
        tmp.add(new GrammarObject("v5u-s", "wareru", "u", "passive", false));
        tmp.add(new GrammarObject("v5k", "kareru", "ku", "passive", false));
        tmp.add(new GrammarObject("v5k-s", "kareru", "ku", "passive", false));
        tmp.add(new GrammarObject("v5g", "gareru", "gu", "passive", false));
        tmp.add(new GrammarObject("v5s", "sareru", "su", "passive", false));
        tmp.add(new GrammarObject("v5t", "tareru", "tsu", "passive", false));
        tmp.add(new GrammarObject("v5n", "nareru", "nu", "passive", false));
        tmp.add(new GrammarObject("v5b", "bareru", "bu", "passive", false));
        tmp.add(new GrammarObject("v5m", "mareru", "mu", "passive", false));
        tmp.add(new GrammarObject("v5r", "rareru", "ru", "passive", false));
        tmp.add(new GrammarObject("v1", "rareru", "ru", "passive", false));
        tmp.add(new GrammarObject("v1-s", "rareru", "ru", "passive", false));

        PASSIVE_FORM_LIST = Collections.unmodifiableList(tmp);
    }

    static {
        List<GrammarObject> tmp = new ArrayList<GrammarObject>();

        tmp.add(new GrammarObject("vs", "させる", "する", "causative", true));
        tmp.add(new GrammarObject("vs-i", "させる", "する", "causative", true));
        tmp.add(new GrammarObject("vk", "こさせる", "くる", "causative", false));
        tmp.add(new GrammarObject("vk", "来させる", "来る", "causative", false));
        tmp.add(new GrammarObject("vk", "こられる", "くる", "causative", true));
        tmp.add(new GrammarObject("v5u", "わせる", "う", "causative", true));
        tmp.add(new GrammarObject("v5u-s", "わせる", "う", "causative", true));
        tmp.add(new GrammarObject("v5k", "かせる", "く", "causative", true));
        tmp.add(new GrammarObject("v5k-s", "かせる", "く", "causative", true));
        tmp.add(new GrammarObject("v5g", "がせる", "ぐ", "causative", true));
        tmp.add(new GrammarObject("v5s", "させる", "す", "causative", true));
        tmp.add(new GrammarObject("v5t", "たせる", "つ", "causative", true));
        tmp.add(new GrammarObject("v5n", "なせる", "ぬ", "causative", true));
        tmp.add(new GrammarObject("v5b", "ばせる", "ぶ", "causative", true));
        tmp.add(new GrammarObject("v5m", "ませる", "む", "causative", true));
        tmp.add(new GrammarObject("v5r", "らせる", "る", "causative", true));
        tmp.add(new GrammarObject("v5r-i", "らせる", "る", "causative", true));
        tmp.add(new GrammarObject("v1", "させる", "る", "causative", true));
        tmp.add(new GrammarObject("v1-s", "させる", "る", "causative", true));

        tmp.add(new GrammarObject("vs", "saseru", "suru", "causative", false));
        tmp.add(new GrammarObject("vs-i", "saseru", "suru", "causative", false));
        tmp.add(new GrammarObject("vk", "kosaseru", "kuru", "causative", false));
        tmp.add(new GrammarObject("v5u", "waseru", "u", "causative", false));
        tmp.add(new GrammarObject("v5u-s", "waseru", "u", "causative", false));
        tmp.add(new GrammarObject("v5k", "kaseru", "ku", "causative", false));
        tmp.add(new GrammarObject("v5k-s", "kaseru", "ku", "causative", false));
        tmp.add(new GrammarObject("v5g", "gaseru", "gu", "causative", false));
        tmp.add(new GrammarObject("v5s", "saseru", "su", "causative", false));
        tmp.add(new GrammarObject("v5t", "taseru", "tsu", "causative", false));
        tmp.add(new GrammarObject("v5n", "naseru", "nu", "causative", false));
        tmp.add(new GrammarObject("v5b", "baseru", "bu", "causative", false));
        tmp.add(new GrammarObject("v5m", "maseru", "mu", "causative", false));
        tmp.add(new GrammarObject("v5r", "raseru", "ru", "causative", false));
        tmp.add(new GrammarObject("v5r-i", "raseru", "ru", "causative", false));
        tmp.add(new GrammarObject("v1", "saseru", "ru", "causative", false));
        tmp.add(new GrammarObject("v1-s", "saseru", "ru", "causative", false));

        CAUSATIVE_FORM_LIST = Collections.unmodifiableList(tmp);
    }

    static {
        List<GrammarObject> tmp = new ArrayList<GrammarObject>();

        tmp.add(new GrammarObject("aux", "であれば", "だ", "provisional conditional", true));
        tmp.add(new GrammarObject("vs", "すれば", "する", "provisional conditional", true));
        tmp.add(new GrammarObject("vs-i", "すれば", "する", "provisional conditional", true));
        tmp.add(new GrammarObject("vk", "くれば", "くる", "provisional conditional", true));
        tmp.add(new GrammarObject("vk", "来れば", "来る", "provisional conditional", false));
        tmp.add(new GrammarObject("v5u", "えば", "う", "provisional conditional", true));
        tmp.add(new GrammarObject("v5u-s", "えば", "う", "provisional conditional", true));
        tmp.add(new GrammarObject("v5k", "けば", "く", "provisional conditional", true));
        tmp.add(new GrammarObject("v5k-s", "けば", "く", "provisional conditional", true));
        tmp.add(new GrammarObject("v5g", "げば", "ぐ", "provisional conditional", true));
        tmp.add(new GrammarObject("v5s", "せば", "す", "provisional conditional", true));
        tmp.add(new GrammarObject("v5t", "てば", "つ", "provisional conditional", true));
        tmp.add(new GrammarObject("v5n", "ねば", "ぬ", "provisional conditional", true));
        tmp.add(new GrammarObject("v5b", "べば", "ぶ", "provisional conditional", true));
        tmp.add(new GrammarObject("v5m", "めば", "む", "provisional conditional", true));
        tmp.add(new GrammarObject("v5r", "れば", "る", "provisional conditional", true));
        tmp.add(new GrammarObject("v5r-i", "れば", "る", "provisional conditional", true));
        tmp.add(new GrammarObject("v1", "れば", "る", "provisional conditional", true));
        tmp.add(new GrammarObject("v1-s", "れば", "る", "provisional conditional", true));
        tmp.add(new GrammarObject("adj-i", "ければ", "い", "provisional conditional", true));
        tmp.add(new GrammarObject("adj-na", "であれば", "な", "provisional conditional", true));
        tmp.add(new GrammarObject("aux-adj", "なければ", "ない", "provisional conditional", true));

        tmp.add(new GrammarObject("aux", "de areba", "da", "provisional conditional", false));
        tmp.add(new GrammarObject("vs", "sureba", "suru", "provisional conditional", false));
        tmp.add(new GrammarObject("vs-i", "sureba", "suru", "provisional conditional", false));
        tmp.add(new GrammarObject("vk", "kureba", "kuru", "provisional conditional", false));
        tmp.add(new GrammarObject("v5u", "eba", "u", "provisional conditional", false));
        tmp.add(new GrammarObject("v5u-s", "eba", "u", "provisional conditional", false));
        tmp.add(new GrammarObject("v5k", "keba", "ku", "provisional conditional", false));
        tmp.add(new GrammarObject("v5k-s", "keba", "ku", "provisional conditional", false));
        tmp.add(new GrammarObject("v5g", "geba", "gu", "provisional conditional", false));
        tmp.add(new GrammarObject("v5s", "seba", "su", "provisional conditional", false));
        tmp.add(new GrammarObject("v5t", "teba", "tsu", "provisional conditional", false));
        tmp.add(new GrammarObject("v5n", "neba", "nu", "provisional conditional", false));
        tmp.add(new GrammarObject("v5b", "beba", "bu", "provisional conditional", false));
        tmp.add(new GrammarObject("v5m", "meba", "mu", "provisional conditional", false));
        tmp.add(new GrammarObject("v5r", "reba", "ru", "provisional conditional", false));
        tmp.add(new GrammarObject("v5r-i", "reba", "ru", "provisional conditional", false));
        tmp.add(new GrammarObject("v1", "reba", "ru", "provisional conditional", false));
        tmp.add(new GrammarObject("v1-s", "reba", "ru", "provisional conditional", false));
        tmp.add(new GrammarObject("adj-i", "kereba", "i", "provisional conditional", false));
        tmp.add(new GrammarObject("adj-na", "de areba", "na", "provisional conditional", false));
        tmp.add(new GrammarObject("aux-adj", "nakereba", "nai", "provisional conditional", false));

        PROVISIONAL_CONDITIONAL_FORM_LIST = Collections.unmodifiableList(tmp);
    }

    static {
        List<GrammarObject> tmp = new ArrayList<GrammarObject>();

        tmp.add(new GrammarObject("aux", "だったら", "だ", "conditional", true));
        tmp.add(new GrammarObject("vs", "したら", "する", "conditional", true));
        tmp.add(new GrammarObject("vs-i", "したら", "する", "conditional", true));
        tmp.add(new GrammarObject("vk", "きたら", "くる", "conditional", true));
        tmp.add(new GrammarObject("vk", "来たら", "来る", "conditional", false));
        tmp.add(new GrammarObject("v5u", "ったら", "う", "conditional", true));
        tmp.add(new GrammarObject("v5u-s", "うたら", "う", "conditional", true));
        tmp.add(new GrammarObject("v5k", "いたら", "く", "conditional", true));
        tmp.add(new GrammarObject("v5k-s", "ったら", "く", "conditional", true));
        tmp.add(new GrammarObject("v5g", "いだら", "ぐ", "conditional", true));
        tmp.add(new GrammarObject("v5s", "したら", "す", "conditional", true));
        tmp.add(new GrammarObject("v5t", "ったら", "つ", "conditional", true));
        tmp.add(new GrammarObject("v5n", "んだら", "ぬ", "conditional", true));
        tmp.add(new GrammarObject("v5b", "んだら", "ぶ", "conditional", true));
        tmp.add(new GrammarObject("v5m", "んだら", "む", "conditional", true));
        tmp.add(new GrammarObject("v5r", "ったら", "る", "conditional", true));
        tmp.add(new GrammarObject("v5r-i", "ったら", "る", "conditional", true));
        tmp.add(new GrammarObject("v5aru", "ったら", "る", "conditional", true));
        tmp.add(new GrammarObject("v5aru", "いらしたら", "いらっしゃる", "conditional", true));
        tmp.add(new GrammarObject("v1", "たら", "る", "conditional", true));
        tmp.add(new GrammarObject("v1-s", "たら", "る", "conditional", true));
        tmp.add(new GrammarObject("adj-i", "かったら", "い", "conditional", true));
        tmp.add(new GrammarObject("adj-na", "だったら", "な", "conditional", true));

        tmp.add(new GrammarObject("aux", "dattara", "da", "conditional", false));
        tmp.add(new GrammarObject("vs", "shitara", "suru", "conditional", false));
        tmp.add(new GrammarObject("vs-i", "shitara", "suru", "conditional", false));
        tmp.add(new GrammarObject("vk", "kitara", "kuru", "conditional", false));
        tmp.add(new GrammarObject("v5u", "ttara", "u", "conditional", false));
        tmp.add(new GrammarObject("v5u-s", "utara", "u", "conditional", false)); // trzeba szukać tylko verbów
        //tmp.add(new GrammarObject("v5u-s", "otara", "u"));
        tmp.add(new GrammarObject("v5k", "itara", "ku", "conditional", false));
        tmp.add(new GrammarObject("v5k-s", "ttara", "ku", "conditional", false));
        tmp.add(new GrammarObject("v5g", "idara", "gu", "conditional", false));
        tmp.add(new GrammarObject("v5s", "shitara", "su", "conditional", false));
        tmp.add(new GrammarObject("v5t", "ttara", "tsu", "conditional", false));
        tmp.add(new GrammarObject("v5n", "ndara", "nu", "conditional", false));
        tmp.add(new GrammarObject("v5b", "ndara", "bu", "conditional", false));
        tmp.add(new GrammarObject("v5m", "ndara", "mu", "conditional", false));
        tmp.add(new GrammarObject("v5r", "ttara", "ru", "conditional", false));
        tmp.add(new GrammarObject("v5r-i", "ttara", "ru", "conditional", false));
        tmp.add(new GrammarObject("v5aru", "ttara", "ru", "conditional", false));
        tmp.add(new GrammarObject("v5aru", "irashitara", "irassharu", "conditional", false));
        tmp.add(new GrammarObject("v1", "tara", "ru", "conditional", false));
        tmp.add(new GrammarObject("v1-s", "tara", "ru", "conditional", false));
        tmp.add(new GrammarObject("adj-i", "kattara", "i", "conditional", false));
        tmp.add(new GrammarObject("adj-na", "dattara", "na", "conditional", false));

        CONDITIONAL_RA_LIST = Collections.unmodifiableList(tmp);
    }

    static {
        List<GrammarObject> tmp = new ArrayList<GrammarObject>();

        tmp.add(new GrammarObject("aux", "であれ", "だ", "imperative", true));
        tmp.add(new GrammarObject("vs", "しろ", "する", "imperative", true));
        tmp.add(new GrammarObject("vs", "せよ", "する", "imperative", false));
        tmp.add(new GrammarObject("vs", "せ", "する", "imperative", false));
        tmp.add(new GrammarObject("vs-i", "しろ", "する", "imperative", true));
        tmp.add(new GrammarObject("vs-i", "せよ", "する", "imperative", false));
        tmp.add(new GrammarObject("vs-i", "せ", "する", "imperative", false));
        tmp.add(new GrammarObject("vk", "こい", "くる", "imperative", true));
        tmp.add(new GrammarObject("vk", "来い", "来る", "imperative", false));
        tmp.add(new GrammarObject("aux", "ませ", "ます", "imperative", true));
        tmp.add(new GrammarObject("v5u", "え", "う", "imperative", true));
        tmp.add(new GrammarObject("v5u-s", "え", "う", "imperative", true));
        tmp.add(new GrammarObject("v5k", "け", "く", "imperative", true));
        tmp.add(new GrammarObject("v5k-s", "け", "く", "imperative", true));
        tmp.add(new GrammarObject("v5g", "げ", "ぐ", "imperative", true));
        tmp.add(new GrammarObject("v5s", "せ", "す", "imperative", true));
        tmp.add(new GrammarObject("v5t", "て", "つ", "imperative", true));
        tmp.add(new GrammarObject("v5n", "ね", "ぬ", "imperative", true));
        tmp.add(new GrammarObject("v5b", "べ", "ぶ", "imperative", true));
        tmp.add(new GrammarObject("v5m", "め", "む", "imperative", true));
        tmp.add(new GrammarObject("v5r", "れ", "る", "imperative", true));
        tmp.add(new GrammarObject("v5r-i", "れ", "る", "imperative", true));
        tmp.add(new GrammarObject("v5aru", "れ", "い", "imperative", true));
        tmp.add(new GrammarObject("v1", "ろ", "る", "imperative", true));
        tmp.add(new GrammarObject("v1-s", "_", "る", "imperative", true));
        //tmp.add(new GrammarObject("v1", "いろ", "いる", "imperative", true));
        tmp.add(new GrammarObject("v1", "いよ", "いる", "imperative", false));
        //tmp.add(new GrammarObject("v1", "えろ", "える", "imperative", true));
        tmp.add(new GrammarObject("v1", "えよ", "える", "imperative", false));

        tmp.add(new GrammarObject("aux", "de are", "da", "imperative", false));
        tmp.add(new GrammarObject("vs", "shiro", "suru", "imperative", false));
        tmp.add(new GrammarObject("vs", "seyo", "suru", "imperative", false));
        tmp.add(new GrammarObject("vs", "se", "suru", "imperative", false));
        tmp.add(new GrammarObject("vs-i", "shiro", "suru", "imperative", false));
        tmp.add(new GrammarObject("vs-i", "seyo", "suru", "imperative", false));
        tmp.add(new GrammarObject("vs-i", "se", "suru", "imperative", false));
        tmp.add(new GrammarObject("vk", "koi", "kuru", "imperative", false));
        tmp.add(new GrammarObject("aux", "mase", "masu", "imperative", false));
        tmp.add(new GrammarObject("v5u", "e", "u", "imperative", false));
        tmp.add(new GrammarObject("v5u-s", "e", "u", "imperative", false));
        tmp.add(new GrammarObject("v5k", "ke", "ku", "imperative", false));
        tmp.add(new GrammarObject("v5k-s", "ke", "ku", "imperative", false));
        tmp.add(new GrammarObject("v5g", "ge", "gu", "imperative", false));
        tmp.add(new GrammarObject("v5s", "se", "su", "imperative", false));
        tmp.add(new GrammarObject("v5t", "te", "tsu", "imperative", false));
        tmp.add(new GrammarObject("v5n", "ne", "nu", "imperative", false));
        tmp.add(new GrammarObject("v5b", "be", "bu", "imperative", false));
        tmp.add(new GrammarObject("v5m", "me", "mu", "imperative", false));
        tmp.add(new GrammarObject("v5r", "re", "ru", "imperative", false));
        tmp.add(new GrammarObject("v5r-i", "re", "ru", "imperative", false));
        tmp.add(new GrammarObject("v5aru", "re", "i", "imperative", false));
        tmp.add(new GrammarObject("v1", "ro", "ru", "imperative", false));
        tmp.add(new GrammarObject("v1", "iro", "iru", "imperative", false));
        tmp.add(new GrammarObject("v1", "iyo", "iru", "imperative", false));
        tmp.add(new GrammarObject("v1", "ero", "eru", "imperative", false));
        tmp.add(new GrammarObject("v1", "eyo", "eru", "imperative", false));
        tmp.add(new GrammarObject("v1-s", "_", "ru", "imperative", false));

        IMPERATIVE_FORM_LIST = Collections.unmodifiableList(tmp);
    }



    static {
        List<GrammarObject> tmp = new ArrayList<GrammarObject>();

        tmp.add(new GrammarObject("aux", "だろう", "だ", "volitional", true));
        tmp.add(new GrammarObject("aux", "でしょう", "です", "volitional", true));
        tmp.add(new GrammarObject("aux", "ましょう", "ます", "volitional", true));
        tmp.add(new GrammarObject("vs", "しよう", "する", "volitional", true));
        tmp.add(new GrammarObject("vs", "そう", "する", "volitional", false));
        tmp.add(new GrammarObject("vs-i", "しよう", "する", "volitional", true));
        //tmp.add(new GrammarObject("vs-i", "そう", "する", "volitional", false));
        tmp.add(new GrammarObject("vk", "こよう", "くる", "volitional", true));
        tmp.add(new GrammarObject("vk", "来よう", "来る", "volitional", false));
        tmp.add(new GrammarObject("aux", "ましょう", "ます", "volitional", true));
        tmp.add(new GrammarObject("v5u", "おう", "う", "volitional", true));
        tmp.add(new GrammarObject("v5u-s", "おう", "う", "volitional", true));
        tmp.add(new GrammarObject("v5k", "こう", "く", "volitional", true));
        tmp.add(new GrammarObject("v5k-s", "こう", "く", "volitional", true));
        tmp.add(new GrammarObject("v5g", "ごう", "ぐ", "volitional", true));
        tmp.add(new GrammarObject("v5s", "そう", "す", "volitional", true));
        tmp.add(new GrammarObject("v5t", "とう", "つ", "volitional", true));
        tmp.add(new GrammarObject("v5n", "のう", "ぬ", "volitional", true));
        tmp.add(new GrammarObject("v5b", "ぼう", "ぶ", "volitional", true));
        tmp.add(new GrammarObject("v5m", "もう", "む", "volitional", true));
        tmp.add(new GrammarObject("v5r", "ろう", "る", "volitional", true));
        tmp.add(new GrammarObject("v5r-i", "ろう", "る", "volitional", true));
        tmp.add(new GrammarObject("v1", "よう", "る", "volitional", true));
        tmp.add(new GrammarObject("v1-s", "よう", "る", "volitional", true));
        tmp.add(new GrammarObject("adj-i", "かろう", "い", "volitional", true));
        tmp.add(new GrammarObject("adj-na", "だろう", "な", "volitional", true));
        tmp.add(new GrammarObject("aux-adj", "なかろう", "ない", "volitional", true));

        tmp.add(new GrammarObject("aux", "darou", "da", "volitional", false));
        tmp.add(new GrammarObject("aux", "deshou", "desu", "volitional", false));
        tmp.add(new GrammarObject("aux", "mashou", "masu", "volitional", false));
        tmp.add(new GrammarObject("vs", "shiyou", "suru", "volitional", false));
        //tmp.add(new GrammarObject("vs", "sou", "suru", "volitional", false));
        tmp.add(new GrammarObject("vs-i", "shiyou", "suru", "volitional", false));
        tmp.add(new GrammarObject("vs-i", "sou", "suru", "volitional", false));
        tmp.add(new GrammarObject("vk", "koyou", "kuru", "volitional", false));
        tmp.add(new GrammarObject("aux", "mashou", "masu", "volitional", false));
        tmp.add(new GrammarObject("v5u", "ou", "u", "volitional", false));
        tmp.add(new GrammarObject("v5u-s", "ou", "u", "volitional", false));
        tmp.add(new GrammarObject("v5k", "kou", "ku", "volitional", false));
        tmp.add(new GrammarObject("v5k-s", "kou", "ku", "volitional", false));
        tmp.add(new GrammarObject("v5g", "gou", "gu", "volitional", false));
        tmp.add(new GrammarObject("v5s", "sou", "su", "volitional", false));
        tmp.add(new GrammarObject("v5t", "tou", "tsu", "volitional", false));
        tmp.add(new GrammarObject("v5n", "nou", "nu", "volitional", false));
        tmp.add(new GrammarObject("v5b", "bou", "bu", "volitional", false));
        tmp.add(new GrammarObject("v5m", "mou", "mu", "volitional", false));
        tmp.add(new GrammarObject("v5r", "rou", "ru", "volitional", false));
        tmp.add(new GrammarObject("v5r-i", "rou", "ru", "volitional", false));
        tmp.add(new GrammarObject("v1", "you", "ru", "volitional", false));
        tmp.add(new GrammarObject("v1-s", "you", "ru", "volitional", false));
        tmp.add(new GrammarObject("adj-i", "karou", "i", "volitional", false));
        tmp.add(new GrammarObject("adj-na", "darou", "na", "volitional", false));
        tmp.add(new GrammarObject("aux-adj", "nakarou", "nai", "volitional", false));
        VOLITIONAL_FORM_LIST = Collections.unmodifiableList(tmp);
    }

    static {
        Map<String, GrammarObject> tmp = new HashMap<String, GrammarObject>();

        tmp.put("MA", new GrammarObject("MA", "martial arts term", "Martial arts term", "", false));
        tmp.put("X", new GrammarObject("X", "rude or X-rated term (not displayed in educational software)", "X-rated", "", false));
        tmp.put("abbr", new GrammarObject("abbr", "abbreviation", "Abbreviation", "", false));
        tmp.put("adj-i", new GrammarObject("adj-i", "adjective (keiyoushi)", "Adjective", "", false));
        tmp.put("adj-na", new GrammarObject("adj-na", "Adjectival nouns or quasi-adjectives (keiyodoshi)", "Na-adjective", "", false));
        tmp.put("adj-no", new GrammarObject("adj-no", "nouns which may take the genitive case particle `no'", "No-adjective", "", false));
        tmp.put("adj-pn", new GrammarObject("adj-pn", "pre-noun adjectival (rentaishi)", "Pre-noun adjectival", "", false));
        tmp.put("adj-t", new GrammarObject("adj-t", "`taru' adjective", "Taru adjective", "", false));
        tmp.put("adj-f", new GrammarObject("adj-f", "noun or verb acting prenominally", "Noun or verb acting prenominally", "", false));
        tmp.put("adv", new GrammarObject("adv", "adverb (fukushi)", "Adverb", "", false));
        tmp.put("adv-to", new GrammarObject("adv-to", "adverb taking the `to' particle", "Adverb taking the 'to' particle", "", false));
        tmp.put("arch", new GrammarObject("arch", "archaism", "Archaism", "", false));
        tmp.put("ateji", new GrammarObject("ateji", "ateji (phonetic) reading", "Ateji", "", false));
        tmp.put("aux", new GrammarObject("aux", "auxiliary", "Auxiliary", "", false));
        tmp.put("aux-v", new GrammarObject("aux-v", "auxiliary verb", "Auxiliary verb", "", false));
        tmp.put("aux-adj", new GrammarObject("aux-adj", "auxiliary adjective", "Auxiliary adjective", "", false));
        tmp.put("Buddh", new GrammarObject("Buddh", "Buddhist term", "Buddhist term", "", false));
        tmp.put("chem", new GrammarObject("chem", "chemistry term", "Chemistry term", "", false));
        tmp.put("chn", new GrammarObject("chn", "children's language", "Children's language", "", false));
        tmp.put("col", new GrammarObject("col", "colloquialism", "Colloquialism", "", false));
        tmp.put("comp", new GrammarObject("comp", "computer terminology", "Computer terminology", "", false));
        tmp.put("conj", new GrammarObject("conj", "conjunction", "Conjunction", "", false));
        tmp.put("ctr", new GrammarObject("ctr", "counter", "Counter", "", false));
        tmp.put("derog", new GrammarObject("derog", "derogatory", "Derogatory", "", false));
        tmp.put("eK", new GrammarObject("eK", "exclusively kanji", "Exclusively kanji", "", false));
        tmp.put("ek", new GrammarObject("ek", "exclusively kana", "Exclusively kana", "", false));
        tmp.put("exp", new GrammarObject("exp", "expressions (phrases, clauses, etc.)", "Expression", "", false));
        tmp.put("fam", new GrammarObject("fam", "familiar language", "Familiar language", "", false));
        tmp.put("fem", new GrammarObject("fem", "female term or language", "Female term or language", "", false));
        tmp.put("food", new GrammarObject("food", "food term", "Food term", "", false));
        tmp.put("geom", new GrammarObject("geom", "geometry term", "Geometry term", "", false));
        tmp.put("gikun", new GrammarObject("gikun", "gikun (meaning as reading) or jukujikun (special kanji reading)", "Gikun or jukujikun", "", false));
        tmp.put("hon", new GrammarObject("hon", "honorific or respectful (sonkeigo) language", "Honorific language", "", false));
        tmp.put("hum", new GrammarObject("hum", "humble (kenjougo) language", "Humble language", "", false));
        tmp.put("iK", new GrammarObject("iK", "word containing irregular kanji usage", "Irregular kanji usage", "", false));
        tmp.put("id", new GrammarObject("id", "idiomatic expression", "Idiomatic expression", "", false));
        tmp.put("ik", new GrammarObject("ik", "word containing irregular kana usage", "Irregular kana usage", "", false));
        tmp.put("int", new GrammarObject("int", "interjection (kandoushi)", "Interjection", "", false));
        tmp.put("io", new GrammarObject("io", "irregular okurigana usage", "Irregular okurigana usage", "", false));
        tmp.put("iv", new GrammarObject("iv", "irregular verb", "Irregular verb", "", false));
        tmp.put("ling", new GrammarObject("ling", "linguistics terminology", "Linguistics terminology", "", false));
        tmp.put("m-sl", new GrammarObject("m-sl", "manga slang", "Manga slang", "", false));
        tmp.put("male", new GrammarObject("male", "male term or language", "Male term or language", "", false));
        tmp.put("male-sl", new GrammarObject("male-sl", "male slang", "Male slang", "", false));
        tmp.put("math", new GrammarObject("math", "mathematics", "Mathematics", "", false));
        tmp.put("mil", new GrammarObject("mil", "military", "Military", "", false));
        tmp.put("n", new GrammarObject("n", "noun (common) (futsuumeishi)", "Noun", "", false));
        tmp.put("n-adv", new GrammarObject("n-adv", "adverbial noun (fukushitekimeishi)", "Adverbial noun", "", false));
        tmp.put("n-suf", new GrammarObject("n-suf", "noun, used as a suffix", "Noun used as a suffix", "", false));
        tmp.put("n-pref", new GrammarObject("n-pref", "noun, used as a prefix", "Noun used as a prefix", "", false));
        tmp.put("n-t", new GrammarObject("n-t", "noun (temporal) (jisoumeishi)", "Temporal noun", "", false));
        tmp.put("num", new GrammarObject("num", "numeric", "Numeric", "", false));
        tmp.put("oK", new GrammarObject("oK", "word containing out-dated kanji", "Out-dated kanji", "", false));
        tmp.put("obs", new GrammarObject("obs", "obsolete term", "Obsolete term", "", false));
        tmp.put("obsc", new GrammarObject("obsc", "obscure term", "Obscure term", "", false));
        tmp.put("ok", new GrammarObject("ok", "out-dated or obsolete kana usage", "Out-dated kana", "", false));
        tmp.put("oik", new GrammarObject("oik", "old or irregular kana form", "Old or irregular kana", "", false));
        tmp.put("on-mim", new GrammarObject("on-mim", "onomatopoeic or mimetic word", "Onomatopoeic", "", false));
        tmp.put("pn", new GrammarObject("pn", "pronoun", "Pronoun", "", false));
        tmp.put("poet", new GrammarObject("poet", "poetical term", "Poetical term", "", false));
        tmp.put("pol", new GrammarObject("pol", "polite (teineigo) language", "Polite language", "", false));
        tmp.put("pref", new GrammarObject("pref", "prefix", "Prefix", "", false));
        tmp.put("proverb", new GrammarObject("proverb", "proverb", "Proverb", "", false));
        tmp.put("prt", new GrammarObject("prt", "particle", "Particle", "", false));
        tmp.put("physics", new GrammarObject("physics", "physics terminology", "Physics terminology", "", false));
        tmp.put("rare", new GrammarObject("rare", "rare", "Rare", "", false));
        tmp.put("sens", new GrammarObject("sens", "sensitive", "Sensitive", "", false));
        tmp.put("sl", new GrammarObject("sl", "slang", "Slang", "", false));
        tmp.put("suf", new GrammarObject("suf", "suffix", "Suffix", "", false));
        tmp.put("uK", new GrammarObject("uK", "word usually written using kanji alone", "Usually written using kanji alone", "", false));
        tmp.put("uk", new GrammarObject("uk", "word usually written using kana alone", "Usually written using kana alone", "", false));
        tmp.put("unc", new GrammarObject("unc", "unclassified", "Unclassified", "", false));
        tmp.put("yoji", new GrammarObject("yoji", "yojijukugo", "Yojijukugo", "", false));
        tmp.put("v1", new GrammarObject("v1", "Ichidan verb", "Ichidan verb", "", false));
        tmp.put("v2a-s", new GrammarObject("v2a-s", "Nidan verb with 'u' ending (archaic)", "Nidan verb with 'u' ending (archaic)", "", false));
        tmp.put("v4h", new GrammarObject("v4h", "Yodan verb with `hu/fu' ending (archaic)", "Yodan verb with 'hu/fu' ending (archaic)", "", false));
        tmp.put("v4r", new GrammarObject("v4r", "Yodan verb with `ru' ending (archaic)", "Yodan verb with 'ru' ending (archaic)", "", false));
        tmp.put("v5aru", new GrammarObject("v5aru", "Godan verb - aru special class", "Aru special class", "", false));
        tmp.put("v5b", new GrammarObject("v5b", "Godan verb with `bu' ending", "Godan verb", "", false));
        tmp.put("v5g", new GrammarObject("v5g", "Godan verb with `gu' ending", "Godan verb", "", false));
        tmp.put("v5k", new GrammarObject("v5k", "Godan verb with `ku' ending", "Godan verb", "", false));
        tmp.put("v5k-s", new GrammarObject("v5k-s", "Godan verb - Iku/Yuku special class", "Iku/Yuku special class", "", false));
        tmp.put("v5m", new GrammarObject("v5m", "Godan verb with `mu' ending", "Godan verb", "", false));
        tmp.put("v5n", new GrammarObject("v5n", "Godan verb with `nu' ending", "Godan verb", "", false));
        tmp.put("v5r", new GrammarObject("v5r", "Godan verb with `ru' ending", "Godan verb", "", false));
        tmp.put("v5r-i", new GrammarObject("v5r-i", "Godan verb with `ru' ending (irregular verb)", "Godan verb with 'ru' ending (irregular verb)", "", false));
        tmp.put("v5s", new GrammarObject("v5s", "Godan verb with `su' ending", "Godan verb", "", false));
        tmp.put("v5t", new GrammarObject("v5t", "Godan verb with `tsu' ending", "Godan verb", "", false));
        tmp.put("v5u", new GrammarObject("v5u", "Godan verb with `u' ending", "Godan verb", "", false));
        tmp.put("v5u-s", new GrammarObject("v5u-s", "Godan verb with `u' ending (special class)", "Godan verb (special class)", "", false));
        tmp.put("v5uru", new GrammarObject("v5uru", "Godan verb - Uru old class verb (old form of Eru)", "Godan verb - Uru old class verb", "", false));
        tmp.put("vz", new GrammarObject("vz", "Ichidan verb - zuru verb (alternative form of -jiru verbs)", "Ichidan verb - zuru verb", "", false));
        tmp.put("vi", new GrammarObject("vi", "intransitive verb", "Intransitive verb", "", false));
        tmp.put("vk", new GrammarObject("vk", "Kuru verb - special class", "Kuru verb - special class", "", false));
        tmp.put("vn", new GrammarObject("vn", "irregular nu verb", "Irregular nu verb", "", false));
        tmp.put("vr", new GrammarObject("vr", "irregular ru verb, plain form ends with -ri", "Irregular ru verb", "", false));
        tmp.put("vs", new GrammarObject("vs", "noun or participle which takes the aux. verb suru", "Suru verb", "", false));
        tmp.put("vs-c", new GrammarObject("vs-c", "su verb - precursor to the modern suru", "Su verb - precursor to the modern suru", "", false));
        tmp.put("vs-s", new GrammarObject("vs-s", "suru verb - special class", "Suru verb - special class", "", false));
        tmp.put("vs-i", new GrammarObject("vs-i", "suru verb - irregular", "Suru verb - irregular", "", false));
        tmp.put("kyb", new GrammarObject("kyb", "Kyoto-ben", "Kyoto-ben", "", false));
        tmp.put("osb", new GrammarObject("osb", "Osaka-ben", "Osaka-ben", "", false));
        tmp.put("ksb", new GrammarObject("ksb", "Kansai-ben", "Kansai-ben", "", false));
        tmp.put("ktb", new GrammarObject("ktb", "Kantou-ben", "Kantou-ben", "", false));
        tmp.put("tsb", new GrammarObject("tsb", "Tosa-ben", "Tosa-ben", "", false));
        tmp.put("thb", new GrammarObject("thb", "Touhoku-ben", "Touhoku-ben", "", false));
        tmp.put("tsug", new GrammarObject("tsug", "Tsugaru-ben", "Tsugaru-ben", "", false));
        tmp.put("kyu", new GrammarObject("kyu", "Kyuushuu-ben", "Kyuushuu-ben", "", false));
        tmp.put("rkb", new GrammarObject("rkb", "Ryuukyuu-ben", "Ryuukyuu-ben", "", false));
        tmp.put("nab", new GrammarObject("nab", "Nagano-ben", "Nagano-ben", "", false));
        tmp.put("hob", new GrammarObject("hob", "Hokkaido-ben", "Hokkaido-ben", "", false));
        tmp.put("vt", new GrammarObject("vt", "transitive verb", "Transitive verb", "", false));
        tmp.put("vulg", new GrammarObject("vulg", "vulgar expression or word", "Vulgar", "", false));
        tmp.put("adj-kari", new GrammarObject("adj-kari", "`kari' adjective (archaic)", "" + "'kari' adjective (archaic)", "", false));
        tmp.put("adj-ku", new GrammarObject("adj-ku", "`ku' adjective (archaic)", "'ku' adjective (archaic)", "", false));
        tmp.put("adj-shiku", new GrammarObject("adj-shiku", "`shiku' adjective (archaic)", "'shiku' adjective (archaic)", "", false));
        tmp.put("adj-nari", new GrammarObject("adj-nari", "archaic/formal form of na-adjective", "Archaic/formal form of na-adjective", "", false));
        tmp.put("n-pr", new GrammarObject("n-pr", "proper noun", "Proper noun", "", false));
        tmp.put("v-unspec", new GrammarObject("v-unspec", "verb unspecified", "Verb unspecified", "", false));
        tmp.put("v4k", new GrammarObject("v4k", "Yodan verb with `ku' ending (archaic)", "Yodan verb with 'ku' ending (archaic)", "", false));
        tmp.put("v4g", new GrammarObject("v4g", "Yodan verb with `gu' ending (archaic)", "Yodan verb with 'gu' ending (archaic)", "", false));
        tmp.put("v4s", new GrammarObject("v4s", "Yodan verb with `su' ending (archaic)", "Yodan verb with 'su' ending (archaic)", "", false));
        tmp.put("v4t", new GrammarObject("v4t", "Yodan verb with `tsu' ending (archaic)", "Yodan verb with 'tsu' ending (archaic)", "", false));
        tmp.put("v4n", new GrammarObject("v4n", "Yodan verb with `nu' ending (archaic)", "Yodan verb with 'nu' ending (archaic)", "", false));
        tmp.put("v4b", new GrammarObject("v4b", "Yodan verb with `bu' ending (archaic)", "Yodan verb with 'bu' ending (archaic)", "", false));
        tmp.put("v4m", new GrammarObject("v4m", "Yodan verb with `mu' ending (archaic)", "Yodan verb with 'mu' ending (archaic)", "", false));
        tmp.put("v2k-k", new GrammarObject("v2k-k", "Nidan verb (upper class) with `ku' ending (archaic)", "Nidan verb (upper class) with 'ku' ending (archaic)", "", false));
        tmp.put("v2g-k", new GrammarObject("v2g-k", "Nidan verb (upper class) with `gu' ending (archaic)", "Nidan verb (upper class) with 'gu' ending (archaic)", "", false));
        tmp.put("v2t-k", new GrammarObject("v2t-k", "Nidan verb (upper class) with `tsu' ending (archaic)", "Nidan verb (upper class) with 'tsu' ending (archaic)", "", false));
        tmp.put("v2d-k", new GrammarObject("v2d-k", "Nidan verb (upper class) with `dzu' ending (archaic)", "Nidan verb (upper class) with 'dzu' ending (archaic)", "", false));
        tmp.put("v2h-k", new GrammarObject("v2h-k", "Nidan verb (upper class) with `hu/fu' ending (archaic)", "Nidan verb (upper class) with 'hu/fu' ending (archaic)", "", false));
        tmp.put("v2b-k", new GrammarObject("v2b-k", "Nidan verb (upper class) with `bu' ending (archaic)", "Nidan verb (upper class) with 'bu' ending (archaic)", "", false));
        tmp.put("v2m-k", new GrammarObject("v2m-k", "Nidan verb (upper class) with `mu' ending (archaic)", "Nidan verb (upper class) with 'mu' ending (archaic)", "", false));
        tmp.put("v2y-k", new GrammarObject("v2y-k", "Nidan verb (upper class) with `yu' ending (archaic)", "Nidan verb (upper class) with 'yu' ending (archaic)", "", false));
        tmp.put("v2r-k", new GrammarObject("v2r-k", "Nidan verb (upper class) with `ru' ending (archaic)", "Nidan verb (upper class) with 'ru' ending (archaic)", "", false));
        tmp.put("v2k-s", new GrammarObject("v2k-s", "Nidan verb (lower class) with `ku' ending (archaic)", "Nidan verb (lower class) with 'ku' ending (archaic)", "", false));
        tmp.put("v2g-s", new GrammarObject("v2g-s", "Nidan verb (lower class) with `gu' ending (archaic)", "Nidan verb (lower class) with 'gu' ending (archaic)", "", false));
        tmp.put("v2s-s", new GrammarObject("v2s-s", "Nidan verb (lower class) with `su' ending (archaic)", "Nidan verb (lower class) with 'su' ending (archaic)", "", false));
        tmp.put("v2z-s", new GrammarObject("v2z-s", "Nidan verb (lower class) with `zu' ending (archaic)", "Nidan verb (lower class) with 'zu' ending (archaic)", "", false));
        tmp.put("v2t-s", new GrammarObject("v2t-s", "Nidan verb (lower class) with `tsu' ending (archaic)", "Nidan verb (lower class) with 'tsu' ending (archaic)", "", false));
        tmp.put("v2d-s", new GrammarObject("v2d-s", "Nidan verb (lower class) with `dzu' ending (archaic)", "Nidan verb (lower class) with 'dzu' ending (archaic)", "", false));
        tmp.put("v2n-s", new GrammarObject("v2n-s", "Nidan verb (lower class) with `nu' ending (archaic)", "Nidan verb (lower class) with 'nu' ending (archaic)", "", false));
        tmp.put("v2h-s", new GrammarObject("v2h-s", "Nidan verb (lower class) with `hu/fu' ending (archaic)", "Nidan verb (lower class) with 'hu/fu' ending (archaic)", "", false));
        tmp.put("v2b-s", new GrammarObject("v2b-s", "Nidan verb (lower class) with `bu' ending (archaic)", "Nidan verb (lower class) with 'bu' ending (archaic)", "", false));
        tmp.put("v2m-s", new GrammarObject("v2m-s", "Nidan verb (lower class) with `mu' ending (archaic)", "Nidan verb (lower class) with 'mu' ending (archaic)", "", false));
        tmp.put("v2y-s", new GrammarObject("v2y-s", "Nidan verb (lower class) with `yu' ending (archaic)", "Nidan verb (lower class) with 'yu' ending (archaic)", "", false));
        tmp.put("v2r-s", new GrammarObject("v2r-s", "Nidan verb (lower class) with `ru' ending (archaic)", "Nidan verb (lower class) with 'ru' ending (archaic)", "", false));
        tmp.put("v2w-s", new GrammarObject("v2w-s", "Nidan verb (lower class) with `u' ending and `we' conjugation (archaic)", "Nidan verb (lower class) with 'u' ending and 'we' conjugation (archaic)", "", false));
        tmp.put("archit", new GrammarObject("archit", "architecture term", "Architecture term", "", false));
        tmp.put("astron", new GrammarObject("astron", "astronomy, etc. term", "Astronomy, etc. term", "", false));
        tmp.put("baseb", new GrammarObject("baseb", "baseball term", "Baseball term", "", false));
        tmp.put("biol", new GrammarObject("biol", "biology term", "Biology term", "", false));
        tmp.put("bot", new GrammarObject("bot", "botany term", "Botany term", "", false));
        tmp.put("bus", new GrammarObject("bus", "business term", "Business term", "", false));
        tmp.put("econ", new GrammarObject("econ", "economics term", "Economics term", "", false));
        tmp.put("engr", new GrammarObject("engr", "engineering term", "Engineering term", "", false));
        tmp.put("finc", new GrammarObject("finc", "finance term", "Finance term", "", false));
        tmp.put("geol", new GrammarObject("geol", "geology, etc. term", "Geology, etc. term", "", false));
        tmp.put("law", new GrammarObject("law", "law, etc. term", "Law, etc. term", "", false));
        tmp.put("mahj", new GrammarObject("mahj", "mahjong term", "Mahjong term", "", false));
        tmp.put("med", new GrammarObject("med", "medicine, etc. term", "Medicine, etc. term", "", false));
        tmp.put("music", new GrammarObject("music", "music term", "Music term", "", false));
        tmp.put("Shinto", new GrammarObject("Shinto", "Shinto term", "Shinto term", "", false));
        tmp.put("shogi", new GrammarObject("shogi", "shogi term", "Shogi term", "", false));
        tmp.put("sports", new GrammarObject("sports", "sports term", "Sports term", "", false));
        tmp.put("sumo", new GrammarObject("sumo", "sumo term", "Sumo term", "", false));
        tmp.put("zool", new GrammarObject("zool", "zoology term", "Zoology term", "", false));
        tmp.put("joc", new GrammarObject("joc", "jocular, humorous term", "Humorous term", "", false));
        tmp.put("anat", new GrammarObject("anat", "anatomical term", "Anatomical term", "", false));
        ENTITY_TYPE_MAP = Collections.unmodifiableMap(tmp);
    }


    static {
        Map<String, Integer> tmp = new HashMap<String, Integer>();

        tmp.put("", R.drawable.unitedkingdomflag);
        /*
        tmp.put("ger", R.drawable.germanyflag);
        tmp.put("rus", R.drawable.russianflag);
        tmp.put("spa", R.drawable.spanishflag);
        tmp.put("dut", R.drawable.dutchflag);
        tmp.put("hun", R.drawable.hungaryflag);
        tmp.put("swe", R.drawable.swedishflag);
        tmp.put("fre", R.drawable.franceflag);
        tmp.put("slv", R.drawable.slovenianflag);


        // kanji dict
        tmp.put("fr", R.drawable.franceflag);
        tmp.put("pt", R.drawable.portugalflag);
        tmp.put("es", R.drawable.spanishflag);
*/

        LANGUAGES_LIST = Collections.unmodifiableMap(tmp);
    }

    static {
        ArrayList<List<GrammarObject>> grammarList = new ArrayList<List<GrammarObject>>();
        grammarList.add(GrammarDictionaries.NEGATIVE_FORM_LIST);
        grammarList.add(GrammarDictionaries.PASSIVE_FORM_LIST);
        grammarList.add(GrammarDictionaries.POTENTIAL_FORM_LIST);
        grammarList.add(GrammarDictionaries.CAUSATIVE_FORM_LIST);
        grammarList.add(GrammarDictionaries.IMPERATIVE_FORM_LIST);
        grammarList.add(GrammarDictionaries.VOLITIONAL_FORM_LIST);
        grammarList.add(GrammarDictionaries.TEIRU_FORM_LIST);
        grammarList.add(GrammarDictionaries.TE_FORM_LIST);
        grammarList.add(GrammarDictionaries.PASSIVES_LIST);
        grammarList.add(GrammarDictionaries.POLITE_FORM_LIST);
        grammarList.add(GrammarDictionaries.TAI_FORM_LIST);
        grammarList.add(GrammarDictionaries.I_FORM_LIST);
        grammarList.add(GrammarDictionaries.PROVISIONAL_CONDITIONAL_FORM_LIST);
        grammarList.add(GrammarDictionaries.CONDITIONAL_RA_LIST);
        grammarList.add(GrammarDictionaries.TO_ADV_FORM_LIST);
        grammarList.add(GrammarDictionaries.TO_NOUN_FORM_LIST);
        grammarList.add(GrammarDictionaries.TARI_FORM_LIST);
        grammarList.add(GrammarDictionaries.TEOKU_FORM_LIST);

        GRAMMAR_LIST = Collections.unmodifiableList(grammarList);
    }

    static {
        Map<Integer, String> tmp = new HashMap<Integer, String>();

        tmp.put(0, "");
        tmp.put(1, " AND freq is not null ");
        tmp.put(2, " AND jlpt IS NOT NULL ");
        tmp.put(3, " AND jlpt = 5 ");
        tmp.put(4, " AND jlpt = 4 ");
        tmp.put(5, " AND jlpt = 3 ");
        tmp.put(6, " AND jlpt = 2 ");
        tmp.put(7, " AND jlpt = 1 ");
        tmp.put(8, " AND grade <= 6 ");
        tmp.put(9, " AND grade = 1 ");
        tmp.put(10, " AND grade = 2 ");
        tmp.put(11, " AND grade = 3 ");
        tmp.put(12, " AND grade = 4 ");
        tmp.put(13, " AND grade = 5 ");
        tmp.put(14, " AND grade = 6 ");
        tmp.put(15, " AND grade > 6 ");


        KANJI_SPINNER_LIST = Collections.unmodifiableMap(tmp);
    }

    static {
        Map<Integer, String> tmp = new HashMap<Integer, String>();

        tmp.put(0, " AND grade = 1 ");
        tmp.put(1, " AND grade = 2 ");
        tmp.put(2, " AND grade = 3 ");
        tmp.put(3, " AND grade = 4 ");
        tmp.put(4, " AND grade = 5 ");
        tmp.put(5, " AND grade = 6 ");
        tmp.put(6, " AND grade > 6 ");

        KANJI_SCHOOL_GRADES_LIST = Collections.unmodifiableMap(tmp);
    }

    static {
        Map<Integer, String> tmp = new HashMap<Integer, String>();

        tmp.put(0, " AND jlpt = 5 ");
        tmp.put(1, " AND jlpt = 4 ");
        tmp.put(2, " AND jlpt = 3 ");
        tmp.put(3, " AND jlpt = 2 ");
        tmp.put(4, " AND jlpt = 1 ");

        KANJI_JLPT_LIST = Collections.unmodifiableMap(tmp);
    }


    static {
        Map<Integer, String> tmp = new HashMap<Integer, String>();

        tmp.put(0, "");
        tmp.put(1, " AND P.pos like 'v%' ");
        tmp.put(2, " AND P.pos like 'v%' AND P.pos != 'vs' ");
        tmp.put(3, " AND P.pos like 'n%' ");
        tmp.put(4, " AND P.pos like 'adj%' ");
        tmp.put(5, " AND P.pos like 'adv%' ");
        tmp.put(6, " AND P.pos = 'exp' ");
        tmp.put(7, " AND P.pos = 'ctr' ");
        tmp.put(8, " AND misc = 'on-mim' ");

        VOCABULARY_SPINNER_LIST = Collections.unmodifiableMap(tmp);
    }

    static {

        Map<String, String> tmp = new HashMap<String, String>();
        tmp.put("conditional", null);
        tmp.put("past", null);
        tmp.put("polite", "aux");
        tmp.put("negative", "adj-i");
        tmp.put("-tai form", "adj-i");
        tmp.put("-i form", null);
        tmp.put("progressive", "v1");
        tmp.put("-te form", null);
        tmp.put("potential", "v1");
        tmp.put("passive", "v1");
        tmp.put("causative", "v1");
        tmp.put("provisional conditional", null);
        tmp.put("imperative", null);
        tmp.put("volitional", null);
        tmp.put("-tari form", null);
        tmp.put("-teoku short form", "v5k");
        tmp.put("adjective to adverb", null);

        GRAMMAR_WORDTYPE = Collections.unmodifiableMap(tmp);
    }


    static {
        Map<String, List<GrammarObject>> tmp = new HashMap<String, List<GrammarObject>>();

        tmp.put("conditional", GrammarDictionaries.CONDITIONAL_RA_LIST);
        tmp.put("past", GrammarDictionaries.PASSIVES_LIST);
        tmp.put("polite", GrammarDictionaries.POLITE_FORM_LIST);
        tmp.put("negative", GrammarDictionaries.NEGATIVE_FORM_LIST);
        tmp.put("-tai form", GrammarDictionaries.TAI_FORM_LIST);
        tmp.put("-i form", GrammarDictionaries.I_FORM_LIST);
        tmp.put("progressive", GrammarDictionaries.TEIRU_FORM_LIST);
        tmp.put("-teoku short form", GrammarDictionaries.TEOKU_FORM_LIST);
        tmp.put("-te form", GrammarDictionaries.TE_FORM_LIST);
        tmp.put("potential", GrammarDictionaries.POTENTIAL_FORM_LIST);
        tmp.put("passive", GrammarDictionaries.PASSIVE_FORM_LIST);
        tmp.put("causative", GrammarDictionaries.CAUSATIVE_FORM_LIST);
        tmp.put("provisional conditional", GrammarDictionaries.PROVISIONAL_CONDITIONAL_FORM_LIST);
        tmp.put("imperative", GrammarDictionaries.IMPERATIVE_FORM_LIST);
        tmp.put("volitional", GrammarDictionaries.VOLITIONAL_FORM_LIST);
        tmp.put("adjective to adverb", GrammarDictionaries.TO_ADV_FORM_LIST);
        tmp.put("adjective to noun", GrammarDictionaries.TO_NOUN_FORM_LIST);
        tmp.put("-tari form", GrammarDictionaries.TARI_FORM_LIST);

        GRAMMAR_MAP = Collections.unmodifiableMap(tmp);
    }

    static {
        Map<String, List<String>> tmp = new HashMap<String, List<String>>();

        ArrayList<String> ka = new ArrayList<String>();
        ka.add(0, "が");
        tmp.put("か", ka);

        ArrayList<String> ki = new ArrayList<String>();
        ki.add(0, "ぎ");
        tmp.put("き", ki);

        ArrayList<String> ku = new ArrayList<String>();
        ku.add(0, "ぐ");
        tmp.put("く", ku);

        ArrayList<String> ke = new ArrayList<String>();
        ke.add(0, "げ");
        tmp.put("け", ke);

        ArrayList<String> ko = new ArrayList<String>();
        ko.add(0, "ご");
        tmp.put("こ", ko);

        ArrayList<String> sa = new ArrayList<String>();
        sa.add(0, "ざ");
        tmp.put("さ", sa);

        ArrayList<String> shi = new ArrayList<String>();
        shi.add(0, "じ");
        tmp.put("し", shi);

        ArrayList<String> su = new ArrayList<String>();
        su.add(0, "ず");
        tmp.put("す", su);

        ArrayList<String> se = new ArrayList<String>();
        se.add(0, "ぜ");
        tmp.put("せ", se);

        ArrayList<String> so = new ArrayList<String>();
        so.add(0, "ぞ");
        tmp.put("そ", so);

        ArrayList<String> ta = new ArrayList<String>();
        ta.add(0, "だ");
        tmp.put("た", ta);

        ArrayList<String> chi = new ArrayList<String>();
        chi.add(0, "ぢ");
        tmp.put("ち", chi);

        ArrayList<String> tsu = new ArrayList<String>();
        tsu.add(0, "ず");
        tsu.add(1, "づ");
        tmp.put("つ", tsu);

        ArrayList<String> te = new ArrayList<String>();
        te.add(0, "で");
        tmp.put("て", te);

        ArrayList<String> to = new ArrayList<String>();
        to.add(0, "ど");
        tmp.put("と", to);

        ArrayList<String> ha = new ArrayList<String>();
        ha.add(0, "ば");
        ha.add(1, "ぱ");
        tmp.put("は", ha);

        ArrayList<String> hi = new ArrayList<String>();
        hi.add(0, "び");
        hi.add(1, "ぴ");
        tmp.put("ひ", hi);

        ArrayList<String> fu = new ArrayList<String>();
        fu.add(0, "ぶ");
        fu.add(1, "ぷ");
        tmp.put("ふ", fu);

        ArrayList<String> ho = new ArrayList<String>();
        ho.add(0, "ぼ");
        ho.add(1, "ぽ");
        tmp.put("ほ", ho);


        NIG_KANA = Collections.unmodifiableMap(tmp);
    }


    /*
    static {
        Map<String, String> tmp = new HashMap<String, String>();

        tmp.put("conditional", "");
        tmp.put("past", "");
        tmp.put("polite", "aux");
        tmp.put("negative", "adj-i");
        tmp.put("-tai form", "adj-i");
        tmp.put("-i form", "");
        tmp.put("progressive", "v1");
        tmp.put("-te form", "");
        tmp.put("potential", "");
        tmp.put("passive", "");
        tmp.put("causative", "v1");
        tmp.put("provisional conditional", "");
        tmp.put("imperative", "");
        tmp.put("volitional", "");


        WORD_TYPE_AFTER_MAP = Collections.unmodifiableMap(tmp);
    }
    */

    static {

        Map<String, List<String>> canBeInChain = new HashMap<String, List<String>>();

        ArrayList<String> polite = new ArrayList<String>();
        polite.add("past");
        polite.add("negative");
        polite.add("volitional");
        polite.add("-te form");
        canBeInChain.put("polite", polite);

        ArrayList<String> tai = new ArrayList<String>();
        tai.add("past");
        tai.add("negative");
        tai.add("adjective to adverb");
        tai.add("-te form");
        canBeInChain.put("-tai form", tai);

        ArrayList<String> progressive = new ArrayList<String>();
        progressive.add("conditional");
        progressive.add("provisional conditional");
        progressive.add("past");
        progressive.add("polite");
        progressive.add("negative");
        progressive.add("-tai form");
        progressive.add("-te form");
        progressive.add("volitional");
        progressive.add("imperative");
        progressive.add("-tari form");
        canBeInChain.put("progressive", progressive);

        ArrayList<String> te = new ArrayList<String>();
        te.add("progressive");
        canBeInChain.put("-te form", te);

        ArrayList<String> teoku = new ArrayList<String>();
        teoku.add("conditional");
        teoku.add("provisional conditional");
        teoku.add("past");
        teoku.add("polite");
        teoku.add("negative");
        teoku.add("-tai form");
        teoku.add("-te form");
        teoku.add("volitional");
        teoku.add("imperative");
        teoku.add("-tari form");
        canBeInChain.put("-teoku short form", teoku);

        ArrayList<String> potential = new ArrayList<String>();
        potential.add("provisional conditional");
        potential.add("past");
        potential.add("polite");
        potential.add("negative");
        potential.add("-tai form");
        potential.add("-te form");
        potential.add("-tari form");
        canBeInChain.put("potential", potential);

        ArrayList<String> passive = new ArrayList<String>();
        passive.add("past");
        passive.add("polite");
        passive.add("negative");
        passive.add("-tai form");
        passive.add("-i form");
        passive.add("imperative");
        passive.add("progressive");
        passive.add("-te form");
        passive.add("-tari form");
        passive.add("conditional");
        passive.add("provisional conditional");
        canBeInChain.put("passive", passive);

        ArrayList<String> causative = new ArrayList<String>();
        causative.add("past");
        causative.add("negative");
        causative.add("-tai form");
        causative.add("progressive");
        causative.add("imperative");
        causative.add("passive");
        causative.add("-te form");
        causative.add("volitional");
        causative.add("-tari form");
        canBeInChain.put("causative", causative);

        ArrayList<String> negative = new ArrayList<String>();
        negative.add("past");
        negative.add("-te form");
        negative.add("adjective to adverb");

        canBeInChain.put("negative", negative);

        CAN_BE_IN_CHAIN = Collections.unmodifiableMap(canBeInChain);
    }

    static {

        Map<String, List<Pair<String, String>>> haveSameCreationRule = new HashMap<String, List<Pair<String, String>>>();

        ArrayList<Pair<String, String>> v1List = new ArrayList<Pair<String, String>>();
        v1List.add(new Pair<String, String>("passive", "potential"));

        haveSameCreationRule.put("v1", v1List);

        HAVE_SAME_CREATION_RULE = Collections.unmodifiableMap(haveSameCreationRule);
    }
}
