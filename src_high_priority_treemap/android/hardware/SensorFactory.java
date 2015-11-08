
package android.hardware;

import java.util.List;

public interface SensorFactory {
    public void createSensors(List<SensorBase> devList,
            SystemSensorService service);
}
