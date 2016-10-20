package utils.other;

public class ClassObject {
    private Object mObject1;
    private Object mObject2;

    public ClassObject(Object object1, Object object2) {
        this.mObject1 = object1;
        this.mObject2 = object2;
    }

    public ClassObject(Object obj) {
        this.mObject1 = obj;
        this.mObject2 = null;
    }


    public Object getObject1() {
        return mObject1;
    }

    public void setObject1(Object inObj) {
        mObject1 = inObj;
    }

    public Object getObject2() {
        return mObject2;
    }

    public void setObject2(Object inObj) {
        mObject2 = inObj;
    }

}
