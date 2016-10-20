package utils.other.properties;

import com.mino.jdict.objects.activities.ListObject;

/**
 * Created by Dominik on 10/15/2016.
 */

public class ListObjectProperty extends Property<ListObject> {

    public boolean notNull() { return val != null; }

}
