package com.mino.jdict.interfaces;


/**
 * Created by Dominik on 7/26/2015.
 */
public interface IClearable {

    enum ClearanceLevel { None, Soft, Hard }
    void clear(ClearanceLevel inClearanceLevel);
}
