package com.mino.jdict.adapters;

import android.database.Cursor;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.mino.jdict.R;
import com.mino.jdict.interfaces.IRebKebObject;
import com.mino.jdict.objects.activities.KanjiBaseObject;
import com.mino.jdict.objects.activities.ListObject;
import com.mino.jdict.objects.basic.SenseObject;

import utils.grammar.InputUtils;
import utils.other.ExTextView;

/**
 * Created by Mino on 2014-10-19.
 */
public class AdapterHelper {

    public static void DecompositionItem(KanjiBaseObject i, LayoutInflater inflater, TextView kanji, TextView kanjiMeaning, TextView kanjiReading, View showKanji, View v) {

        if (i.ShowArrow.get()) {
            showKanji.setVisibility(View.VISIBLE);
        }

        if (kanji != null) {

            kanji.setText(i.Character.get());

            if (i.CharacterID.get().equals(0)) {

                ((ExTextView)kanji).setSpecialFont();
                kanji.setText(i.Character.get());
                showKanji.setVisibility(View.GONE);
            }
        }
        if (kanjiReading != null) {

            SpannableStringBuilder WordtoSpan;

            if (i.getKunyomi().isEmpty() && i.getOnyomi().isEmpty() && !i.getNanori().isEmpty()) {

                WordtoSpan = new SpannableStringBuilder(i.getNanori());
                WordtoSpan.setSpan(new ForegroundColorSpan(v.getResources().getColor(R.color.imired_light)), 0, i.getNanori().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                kanjiReading.setText(WordtoSpan);
            } else {

                if (!i.getKunyomi().isEmpty() && !i.getOnyomi().isEmpty()) {
                    WordtoSpan = new SpannableStringBuilder(i.getOnyomi() + " / " + i.getKunyomi());
                } else if (!i.getOnyomi().isEmpty()) {
                    WordtoSpan = new SpannableStringBuilder(i.getOnyomi());
                } else {
                    WordtoSpan = new SpannableStringBuilder(i.getKunyomi());
                }

                Boolean isDot = false;

                int len = WordtoSpan.length();
                for (int j = 0; j < len; j++) {

                    if (WordtoSpan.charAt(j) == '.') {
                        isDot = true;
                        WordtoSpan.delete(j, j + 1);
                        WordtoSpan.setSpan(new ForegroundColorSpan(v.getResources().getColor(R.color.wordending)), j, j + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        len--;
                    } else if (WordtoSpan.charAt(j) != ',' && WordtoSpan.charAt(j) != '/') {

                        if (isDot) {
                            WordtoSpan.setSpan(new ForegroundColorSpan(v.getResources().getColor(R.color.wordending)), j, j + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        } else {
                            WordtoSpan.setSpan(new ForegroundColorSpan(v.getResources().getColor(R.color.imired_light)), j, j + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    } else {
                        isDot = false;
                    }
                }

                kanjiReading.setText(WordtoSpan);
            }
        }
        if (kanjiMeaning != null) {
            kanjiMeaning.setText(i.Meaning.get());
        }
    }

    public static void GetKanjiBasicInfo(Cursor cursorKanji, KanjiBasicInfoInterface callback, String inComponent) {

        String onyomi = "", kunyomi = "", nanori = "", meaning = "", literal = "";
        int characterSeq = 0;

        if (cursorKanji != null && cursorKanji.getCount() > 0) {

            int characterSeqIndex = cursorKanji.getColumnIndex("character_seq");
            int readIndex = cursorKanji.getColumnIndex("read");
            int r_typeIndex = cursorKanji.getColumnIndex("r_type");
            int meaningIndex = cursorKanji.getColumnIndex("meaning");
            int literalIndex = cursorKanji.getColumnIndex("literal");
            int strokeSvgIndex = cursorKanji.getColumnIndex("stroke_svg");

            int cSeq;

            do {

                cSeq = cursorKanji.getInt(characterSeqIndex);
                if (characterSeq != cSeq) {

                    if (characterSeq != 0) {
                        callback.onGotData(new KanjiBaseObject(characterSeq, kunyomi, onyomi, nanori, meaning, literal));
                    }

                    characterSeq = cSeq;
                    onyomi = "";
                    kunyomi = "";
                    nanori = "";
                }

                if (cursorKanji.getString(meaningIndex) != null && !cursorKanji.getString(meaningIndex).isEmpty()) {
                    meaning = cursorKanji.getString(meaningIndex);
                }

                literal = cursorKanji.getString(literalIndex);

                String r_type = cursorKanji.getString(r_typeIndex);

                if (r_type.contentEquals("ja_kun")) {
                    if (!kunyomi.isEmpty()) kunyomi += ", ";
                    kunyomi += cursorKanji.getString(readIndex);
                } else if (r_type.contentEquals("ja_on")) {
                    if (!onyomi.isEmpty()) onyomi += ", ";
                    onyomi += cursorKanji.getString(readIndex);
                } else if (r_type.contentEquals("nanori")) {
                    if (!nanori.isEmpty()) nanori += ", ";
                    nanori += cursorKanji.getString(readIndex);
                }

            } while (cursorKanji.moveToNext());
        }

        if (inComponent != null && characterSeq == 0) {
            literal = inComponent;
        }

        if (cursorKanji != null) {
            cursorKanji.close();
        }

        if (characterSeq > 0 || inComponent != null) {
            callback.onGotData(new KanjiBaseObject(characterSeq, kunyomi, onyomi, nanori, meaning, literal));
        }
    }

    public static void AddEntryToView(ListObject i, View v) {

        TextView up = (TextView) v.findViewById(R.id.toptext);
        TextView upadd = (TextView) v.findViewById(R.id.toptextadd);
        TextView bt = (TextView) v.findViewById(R.id.bottomtext);
        TextView common = (TextView) v.findViewById(R.id.commonWord);
        TextView additional = (TextView) v.findViewById(R.id.additionalInfos);
        TextView certified = (TextView) v.findViewById(R.id.certifiedWord);

        certified.setVisibility(View.GONE);

        if (up != null) {
            up.setText(i.getKeb());
        }
        if (upadd != null) {
            upadd.setText(i.getReb());
        }
        if (bt != null) {
            if (i.SenseID.get() > 0) {
                for (SenseObject sense : i.getSenseList()) {
                    if (sense.getSenseID() == i.SenseID.get()) {

                        bt.setText(sense.getGloss());
                        break;
                    }
                }

            } else {
                bt.setText(i.Gloss.get());
            }
        }

        if (common != null) {
            if (i.IsCommon.get()) {
                common.setVisibility(View.VISIBLE);
            } else {
                common.setVisibility(View.GONE);
            }
        }

        if (additional != null) {

            String text = "";

            if (i.IsCertified.get()) {
                certified.setVisibility(View.VISIBLE);
            }

            if (i.IsConjugated.get() && i.GrammarChain.get() != null && !i.GrammarChain.get().isEmpty()) {
                text += "Conjugated (" + i.GrammarChain.get() + "), ";
            }

            if (i.JLPTLevel.get() != 0) {
                text += "JLPT N" + String.valueOf(i.JLPTLevel.get()) + ", ";
            }

            text += InputUtils.getWordTypesString(i.WordType.get());

            additional.setText(text);
        }
    }

    public static boolean CheckKebEmpty(IRebKebObject i) {
        boolean kanjiEmpty = true;
        for (Pair<String, String> pair : i.getRebKebList()) {
            if (!pair.first.contentEquals("")) {
                kanjiEmpty = false;
                break;
            }
        }
        return kanjiEmpty;
    }

    public static String getRomajiFromRebKebList(IRebKebObject i, int j) {

        String reading = "";
        reading = GetReadingFromRebKebObject(i, j);

        if (InputUtils.isStringAllKana(reading)) {
            String romaji = InputUtils.getRomaji(reading, true);

            // biore nastepny znak i patrze czy po złączeniu czytanie w romaji sie nie różni (przez np małe tsu)
            // być może trzeba jednak rozpatrywać, czy ten nastepny znak na pewno należy do tego słowa co obecnie rozpatrywany
            if (!reading.isEmpty() && j + 1 < i.getRebKebList().size()) {

                String nextReading = GetReadingFromRebKebObject(i, j + 1);
                String nextRomaji = InputUtils.getRomaji(nextReading, true);

                String twoCharRomaji = romaji + nextRomaji;
                String twoCharRomaji2 = InputUtils.getRomaji(reading + nextReading, true);

                if (!twoCharRomaji.contentEquals(twoCharRomaji2)) {

                    romaji = twoCharRomaji2.substring(0, twoCharRomaji2.length() - nextRomaji.length());
                }

            }

            return romaji;
        } else return "";
    }

    private static String GetReadingFromRebKebObject(IRebKebObject i, int j) {
        String reading;
        if (i.getRebKebList().get(j).first.isEmpty()) {
            reading = i.getRebKebList().get(j).second;
        } else {
            reading = i.getRebKebList().get(j).first;
        }
        return reading;
    }

    public interface KanjiBasicInfoInterface {

        void onGotData(KanjiBaseObject result);
    }
}
