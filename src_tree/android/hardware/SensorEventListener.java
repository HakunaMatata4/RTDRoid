
package android.hardware;

public interface SensorEventListener {
    public void onSensorChanged(SensorEvent event);

    public void onAccuracyChanged(Sensor sensor, int accuracy);
}
