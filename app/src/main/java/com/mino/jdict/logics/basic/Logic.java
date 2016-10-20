package com.mino.jdict.logics.basic;

import android.os.Bundle;

import com.mino.jdict.activities.BaseActivity;
import com.mino.jdict.interfaces.IActivitySection;
import com.mino.jdict.interfaces.IClearable;
import com.mino.jdict.models.basic.LogicModel;
import com.mino.jdict.models.basic.SimpleLogicModel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mino on 2014-12-18.
 */
public class Logic {

    protected BaseActivity mActivity;
    protected Bundle mBundle;

    // activity, logic -> trzymamy logiki - dodanie przy Create, usuwanie przy Close, Back
    private static Map<BaseActivity, Logic> activities = new HashMap<BaseActivity, Logic>();

    // refresh listy w Logice jeśli taką posiada -> ogólnie na typ Activity
    private static Map<Class, IClearable.ClearanceLevel> activityTypeRefresh = new HashMap<Class, IClearable.ClearanceLevel>();

    // które Activity z danego typu zostały już zrefreshowane, a które jeszcze nie
    private static Map<BaseActivity, Boolean> activityRefresh = new HashMap<BaseActivity, Boolean>();

    public static Logic get(BaseActivity inActivity) {
        if (activities.containsKey(inActivity)) {

            return activities.get(inActivity);
        }

        return null;
    }

    public static void refreshActivity(Class inActivity, IClearable.ClearanceLevel inClearanceLevel) {
        activityTypeRefresh.put(inActivity, inClearanceLevel);
    }

    public static void resumeActivity(BaseActivity inActivity) {

        Class activityClass = inActivity.getClass();
        boolean isInActivityRefresh = activityRefresh.containsKey(inActivity) && activityRefresh.get(inActivity).equals(true);
        IClearable.ClearanceLevel clearanceLevel = activityTypeRefresh.get(activityClass);
        boolean isInTypeRefresh = activityTypeRefresh.containsKey(activityClass) && clearanceLevel != IClearable.ClearanceLevel.None;

        if (isInActivityRefresh || isInTypeRefresh) {

            if (isInTypeRefresh) {
                for (BaseActivity m : activityRefresh.keySet()) {
                    if (m != inActivity) {
                        activityRefresh.put(m, true);
                    }
                }

                activityTypeRefresh.put(inActivity.getClass(), IClearable.ClearanceLevel.None);
            }

            activityRefresh.put(inActivity, false);

            Logic logic = activities.get(inActivity);

            if (logic instanceof IClearable && !logic.isCreating()) {
                ((IClearable) logic).clear(clearanceLevel);
            }
        }

        if (inActivity instanceof IActivitySection) {
            inActivity.SetTitles(((IActivitySection) inActivity).getSectionNumber());
        }

        activities.get(inActivity).resume();
    }

    public boolean isCreating() {
        return mIsCreating;
    }

    public void initialize() {
    }

    public void resume() {

        mIsCreating = false;
    }

    private boolean mIsCreating = true;

    public void init(LogicModel inModel) {

        if (!activities.containsKey(inModel.getActivity())) {
            activities.put(inModel.getActivity(), this);
            activityRefresh.put(inModel.getActivity(), false);
        }


        if (!activityTypeRefresh.containsKey(inModel.getActivity().getClass())) {
            activityTypeRefresh.put(inModel.getActivity().getClass(), IClearable.ClearanceLevel.None);
        }

        if (inModel instanceof SimpleLogicModel) {
            mActivity = inModel.getActivity();
            mBundle = ((SimpleLogicModel)inModel).getBundle();

            initialize();
        }
    }
}
