
package android.hardware;

public interface SensorNative {
    public void init();

    public String poll(int sensodId);
}
