package utils.other.properties;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dominik on 10/15/2016.
 */

public class ArrayListProperty<ElemType> extends Property<ArrayList<ElemType>> {
    public ArrayListProperty() {
          val = new ArrayList<ElemType>();
    }

    public void add(ElemType inElem) {
        val.add(inElem);
    }

}
