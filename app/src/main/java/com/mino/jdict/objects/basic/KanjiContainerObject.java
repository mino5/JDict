package com.mino.jdict.objects.basic;

import com.mino.jdict.objects.activities.KanjiDetailObject;

import java.util.Iterator;

/**
 * Created by Mino on 2015-03-27.
 */
public class KanjiContainerObject implements Iterable<KanjiDetailObject> {

    private KanjiDetailObject mKanji[] = new KanjiDetailObject[5];

    public KanjiContainerObject() {
    }

    public KanjiDetailObject getKanji(int pos) {

        return mKanji[pos - 1];
    }


    public KanjiDetailObject[] getKanjiList() {

        return mKanji;
    }


    public void add(int pos, KanjiDetailObject obj) {

        mKanji[pos - 1] = obj;
    }

    @Override
    public Iterator<KanjiDetailObject> iterator() {
        return new Iterator<KanjiDetailObject>() {

            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < 5 && mKanji[currentIndex] != null;
            }

            @Override
            public KanjiDetailObject next() {
                return mKanji[currentIndex++];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
