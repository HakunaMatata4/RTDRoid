
package android.hardware;

public abstract class SensorBase {
    protected String[] eventBuffer;
    protected Sensor sensor;
    protected int rate;

    /* package */SensorNative sensorNaiveImpl;

    public SensorBase(int rate, Sensor sensor) {
        this.eventBuffer = new String[4];
        this.sensorNaiveImpl = null; /*
                                      * we will initialize the sensor native
                                      * implementation in children
                                      */
        this.sensor = sensor; /*
                               * we will initialize the sensor instance in
                               * children
                               */
        this.rate = rate;
    }

    int findIndexOf(String target, char x, int start) {
        for (int i = start; i < target.length(); i++) {
            if (target.charAt(i) == x)
                return i;
        }

        return -1;
    }

    public int getSensorId() {
        return this.sensor.sensorId;
    }

    /**** the function that need to be implemented by children ****/
    abstract boolean pollEvent();

    abstract void process();

    abstract void init();
}
