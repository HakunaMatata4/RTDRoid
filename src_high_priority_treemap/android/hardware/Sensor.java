
package android.hardware;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Sensor {
    public static final int TYPE_ACCELEROMETER = 0;
    public static final int TYPE_GYROSCOPE = 1;
    public static final int TYPE_LINEAR_ACCELERATION = 2;
    public static final int TYPE_IR_SIM = 3;
    public static final int TYPE_MAGNETIC_FIELD = 4;
    @Deprecated
    public static final int TYPE_ORIENTATION = 14;
    public static final int TYPE_LIGHT = 5;
    public static final int TYPE_PRESSURE = 6;
    @Deprecated
    public static final int TYPE_TEMPERATURE = 7;
    public static final int TYPE_PROXIMITY = 8;
    public static final int TYPE_GRAVITY = 9;

    public static final int TYPE_ROTATION_VECTOR = 11;
    public static final int TYPE_RELATIVE_HUMIDITY = 12;
    public static final int TYPE_AMBIENT_TEMPERATURE = 13;

    public static final int TYPE_ALL = -1;

    static final int SENSOE_BASE_VALUE = 28;

    /* input event configuration */
    // Event types
    static final int EV_SYN = 0x00;
    static final int EV_REL = 0x02;
    // event syn
    static final int SYN_REPORT = 0;
    static final int SYN_CONFIG = 1;
    // Relative axes
    static final int REL_X = 0x00;
    static final int REL_Y = 0x01;
    static final int REL_Z = 0x02;
    static final int REL_RX = 0x03;
    static final int REL_RY = 0x04;
    static final int REL_RZ = 0x05;
    // for message passing
    static final String[] VAL = new String[] {
            "VAL_0", "VAL_1", "VAL_2"
    };
    static final String TV_SEC = "TV_SEC";
    static final String TV_USEC = "TV_USEC";
    // experimental purpose
    static final String TV1_SEC = "TV1_SEC";
    static final String TV1_USEC = "TV1_USEC";

    int sensorId;
    int priority;
    boolean enable;
    SensorEvent currEvent;
    Map<Integer, ArrayList<SensorEventListener>> listenerList;

    public Sensor(int id) {
        sensorId = id;
        priority = SENSOE_BASE_VALUE;
        enable = true;
        currEvent = new SensorEvent(id, this);
        listenerList = new TreeMap<Integer, ArrayList<SensorEventListener>>();
    }

    public int getType() {
        return sensorId;
    }
}
