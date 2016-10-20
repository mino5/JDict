package com.mino.jdict.interfaces;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Mino on 2014-12-15.
 */
public interface IAdapterFactory<T> {
    T factory(Context context, int textViewResourceId, ArrayList<?> objects);
}