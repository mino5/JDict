package com.mino.jdict.objects.activities;

import com.mino.jdict.interfaces.IFactory;

import utils.other.properties.Property;

/**
 * Created by Mino on 2014-12-09.
 */
public class ExampleObject extends ASearchObject {

    public final Property<ItemDetailObject> Detail = new Property<ItemDetailObject>();

    public ExampleObject() {
    }

    public ExampleObject(ItemDetailObject obj) {
        Detail.set(obj);

        if (obj != null) {
            ID.set(obj.SentenceSeq.get());
        }
    }

    @Override
    public String getSecond() {
        return Detail.get().getReb(true);
    }

    @Override
    public String getFirst() {
        return "";
    }


    public static class ExampleObjectFactory implements IFactory<ASearchObject> {
        public ExampleObject factory() {
            return new ExampleObject();
        }
    }
}
