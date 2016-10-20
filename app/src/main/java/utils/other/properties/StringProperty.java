package utils.other.properties;

/**
 * Created by Dominik on 10/15/2016.
 */

public class StringProperty extends Property<String> {

    public StringProperty() {}
    public StringProperty(String inVal) { val = inVal; }
    public StringProperty(String inVal, Runnable inOnSet) { val = inVal; onSet = inOnSet; }
    public boolean notEmpty() { return get() != null && !get().isEmpty(); }
}
