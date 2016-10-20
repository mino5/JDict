package utils.other.properties;

/**
 * Created by Dominik on 10/15/2016.
 */

public class Property<T>  {

    T val;

    public Property() { }
    public Property(T inVal) { val = inVal; }

    public Runnable onSet = null;
    public void set(T inValue) { val = inValue; if (onSet != null) onSet.run();}
    public T get() { return val; }
}


