
package android.hardware;

import java.util.ArrayList;
import java.util.Iterator;

import javax.realtime.RealtimeThread;

import edu.buffalo.rtdroid.experimentUtil.RealTimeHelper;

public class SensorManager {
    /** get sensor data as fast as possible */
    public static final int SENSOR_DELAY_FASTEST = 0;
    /** rate suitable for games */
    public static final int SENSOR_DELAY_GAME = 1;
    /** rate suitable for the user interface */
    public static final int SENSOR_DELAY_UI = 2;
    /** rate (default) suitable for screen orientation changes */
    public static final int SENSOR_DELAY_NORMAL = 3;

    /**
     * The values returned by this sensor cannot be trusted, calibration is
     * needed or the environment doesn't allow readings
     */
    public static final int SENSOR_STATUS_UNRELIABLE = 0;

    /**
     * This sensor is reporting data with low accuracy, calibration with the
     * environment is needed
     */
    public static final int SENSOR_STATUS_ACCURACY_LOW = 1;

    /**
     * This sensor is reporting data with an average level of accuracy,
     * calibration with the environment may improve the readings
     */
    public static final int SENSOR_STATUS_ACCURACY_MEDIUM = 2;

    /** This sensor is reporting data with maximum accuracy */
    public static final int SENSOR_STATUS_ACCURACY_HIGH = 3;

    public static final int AXIS_X = 1;
    public static final int AXIS_Y = 2;
    public static final int AXIS_Z = 3;
    public static final int SYN = 0;
    public static final int AXIS_MINUS_X = AXIS_X | 0x80;
    public static final int AXIS_MINUS_Y = AXIS_Y | 0x80;
    public static final int AXIS_MINUS_Z = AXIS_Z | 0x80;
    public static final int DEV_EVENT = 4;
    public static final String EVENT_STRING_KEY = "event_key";

    // handler message type
    private static SensorManager instance = null;

    /* package */Sensor[] sensors;
    /* package */SystemSensorService internalService;

    /*
     * initiate all sensors
     */
    private SensorManager() {
        this.sensors = new Sensor[4];
        sensors[Sensor.TYPE_ACCELEROMETER] = new Sensor(
                Sensor.TYPE_ACCELEROMETER);
        sensors[Sensor.TYPE_GYROSCOPE] = new Sensor(Sensor.TYPE_GYROSCOPE);
        sensors[Sensor.TYPE_LINEAR_ACCELERATION] = new Sensor(
                Sensor.TYPE_LINEAR_ACCELERATION);
        sensors[Sensor.TYPE_IR_SIM] = new Sensor(Sensor.TYPE_IR_SIM);

        // initialize sensor device, and associate the SensorDevice object with
        // Sensor object
        internalService = new SystemSensorService(this);
    }

    public static SensorManager getInstance() {
        if (instance == null) {
            instance = new SensorManager();
        }

        return instance;
    }

    public Boolean registerListener(SensorEventListener listener,
            Sensor sensor, int rate) {
        int sensorId = sensor.sensorId;
        int rawPr = RealtimeThread.currentRealtimeThread().getPriority();
        int priority = RealTimeHelper.getInstance().FijiFIFO2RTSJ(rawPr);
        // System.out.println("priority conversion:" + rawPr + "===>" +
        // priority);
        if (sensors[sensorId].priority < priority) {
            sensors[sensorId].priority = priority;
        }
        return registerListener(listener, sensor, priority, rate);
    }

    public Boolean registerListener(SensorEventListener listener,
            Sensor sensor, int priority, int rate) {
        int sensorId = sensor.sensorId;
        if (sensor.enable) {
            if (sensors[sensorId].listenerList.containsKey(priority)) {
                sensors[sensorId].listenerList.get(priority).add(listener);
            } else {
                ArrayList<SensorEventListener> newList = new ArrayList<SensorEventListener>();
                newList.add(listener);
                sensors[sensorId].listenerList.put(priority, newList);
            }
            return true;
        } else {
            new Exception("sensor is not available.");
            return false;
        }
    }

    public Sensor getDefaultSensor(int type) {
        return sensors[type];
    }

    public SensorBase getSensorDevice(int sensorId) {
        Iterator<SensorBase> it = internalService.getSensorList();
        while (it.hasNext()) {
            SensorBase sensor = it.next();
            if (sensor.getSensorId() == sensorId) {
                return sensor;
            }
        }
        return null;
    }
}
