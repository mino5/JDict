package com.mino.jdict.interfaces;

import com.mino.jdict.objects.activities.ARecentObject;

/**
 * Created by Dominik on 10/7/2015.
 */
public interface IDetails {
    java.lang.Class<?> getDetailsActivityClass();

    IFactory<ARecentObject> getRTypeFactory();

    Object getObjectForContainer();
}
