
package android.hardware;

public class SensorEvent {
    int sensorId;
    long second;
    long microSenond;
    long readSecond;// the time of reading from kernel buffer...
    long readMicroSecond;// the time of reading from kernel buffer...

    public float values[];
    public Sensor sensor;

    public SensorEvent(int sid) {
        this.sensorId = sid;
        sensor = null;
        values = new float[3];
    }

    public SensorEvent(int sid, Sensor s) {
        this.sensorId = sid;
        sensor = s;
        values = new float[3];
    }

    public String toString() {
        return "{ id:" + sensorId + ", time: " + second + "\'" + microSenond
                + "\", " + "x: " + values[0] + ", y: " + values[1] + ", z:"
                + values[2] + ", priority :" + sensor.priority + " }";
    }

    public long getSeconds() {
        return second;
    }

    public long getMicrosecond() {
        return microSenond;
    }

    public long getReadSecond() {
        return readSecond;
    }

    public long getReadMicroSecond() {
        return readMicroSecond;
    }
}
