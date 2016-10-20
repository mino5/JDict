package com.mino.jdict.objects.activities;

import com.mino.jdict.activities.ItemDetailsActivity;
import com.mino.jdict.activities.KanjiDetailsActivity;
import com.mino.jdict.interfaces.IDetails;
import com.mino.jdict.interfaces.IFactory;

import utils.other.properties.BoolProperty;
import utils.other.properties.Property;

/**
 * Created by Dominik on 10/5/2015.
 */
public class ExtractObject extends ASearchObject implements IDetails {

    public final Property<ListObject> Word = new Property<ListObject>();
    public final Property<KanjiObject> Kanji = new Property<KanjiObject>();
    public final Property<ExampleDetailObjects> Example = new Property<ExampleDetailObjects>();
    public final BoolProperty IsSeparator = new BoolProperty();

    public ExtractObject() {
    }

    public ExtractObject(ListObject obj) {
        Word.set(obj);
        ID.set(obj.ID.get());
    }

    public ExtractObject(KanjiObject obj) {
        Kanji.set(obj);
        ID.set(obj.ID.get());
    }

    public ExtractObject(ExampleDetailObjects obj) {
        Example.set(obj);
    }

    public Object getObjectForContainer() {

        if (Word.get() != null) return Word.get();
        if (Kanji.get() != null) return Kanji.get();
        if (Example.get() != null) return Example.get();

        return null;
    }

    public java.lang.Class<?> getDetailsActivityClass() {

        if (Word.get() != null) return ItemDetailsActivity.class;
        if (Kanji.get() != null) return KanjiDetailsActivity.class;
        if (Example.get() != null) return ItemDetailsActivity.class;

        return null;
    }

    public IFactory<ARecentObject> getRTypeFactory() {
        if (Word.get() != null) return new RecentObject.RecentObjectFactory();
        if (Kanji.get() != null) return new KanjiRecentObject.KanjiRecentObjectFactory();
        if (Example.get() != null) return new RecentObject.RecentObjectFactory();

        return null;
    }

    @Override
    public void setSearchSetting(int i, Object value) {

        switch (i) {
            case 0:
                if (Word.get() != null) {
                    Word.get().setSearchSetting(i, value);
                } else if (Kanji.get() != null) {
                    Kanji.get().setSearchSetting(i, value);
                }

                break;
        }
    }

    @Override
    public String getSecond() {
        if (Word.get() != null)
            return Word.get().getSecond();
        else if (Kanji.get() != null)
            return Kanji.get().getSecond();
        else if (Example.get() == null || !Example.get().ListObject.notNull())
            return Example.get().ListObject.get().getSecond();
        else return "";
    }

    @Override
    public String getFirst() {
        if (Word.get() != null)
            return Word.get().getFirst();
        else if (Kanji.get() != null)
            return Kanji.get().getFirst();
        else if (Example.get() != null && Example.get().ListObject.notNull())
            return Example.get().ListObject.get().getFirst();
        else return "";
    }

    public static class ExtractObjectFactory implements IFactory<ASearchObject> {
        public ExtractObject factory() {
            return new ExtractObject();
        }
    }
}
