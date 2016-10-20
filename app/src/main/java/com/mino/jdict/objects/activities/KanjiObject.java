package com.mino.jdict.objects.activities;

import com.mino.jdict.interfaces.IFactory;

import utils.other.properties.Property;

/**
 * Created by Mino on 2014-12-09.
 */
public class KanjiObject extends ASearchObject {

    public final Property<KanjiBaseObject> Detail = new Property<KanjiBaseObject>();

    public KanjiObject() { }

    public KanjiObject(KanjiBaseObject obj) {
        Detail.set(obj);

        if (obj != null) {
            ID.set(obj.CharacterID.get());
        }
    }

    @Override
    public void setSearchSetting(int i, Object value) {

        switch (i) {
            case 0:

                if (value instanceof Boolean) {

                } else if (value instanceof Integer) {
                    SelectedItemSpinner.set((Integer) value);
                }

                break;
        }
    }

    @Override
    public String getSecond() {
        return Detail.get().Character.get();
    }

    @Override
    public String getFirst() {
        return Detail.get().Meaning.get();
    }


    public static class KanjiObjectFactory implements IFactory<ASearchObject> {
        public KanjiObject factory() {
            return new KanjiObject();
        }
    }
}
