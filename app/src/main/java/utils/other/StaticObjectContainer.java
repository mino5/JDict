package utils.other;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Mino on 2014-10-14.
 */


public class StaticObjectContainer {

    // do przesylania obiektow podczas Intent, zeby nie ladowac na nowo
    public static Object StaticObject = null;

    // Pause-resume
    public static HashMap<Integer, ClassObject> ClassStaticObjectMap = new LinkedHashMap<Integer, ClassObject>();

    public static ClassObject GetClassObject(int hashCode) {
        if (ClassStaticObjectMap.containsKey(hashCode)) {
            return ClassStaticObjectMap.get(hashCode);
        } else return null;
    }

    public static void SetClassObject(int hashCode, ClassObject inObj) {

        ClassStaticObjectMap.put(hashCode, inObj);
    }
}

