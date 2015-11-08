
package android.hardware;

import android.hardware.hal.SensorNativeNexusImpl;

import com.fiji.fivm.r1.Pointer;
import com.fiji.fivm.r1.fivmRuntime;

public class AccelerometerSensor extends SensorBase {
    private boolean isUpdate;
    private String devicePath;
    final static int DEFAULT_ACCEL_RATE = 25;

    public AccelerometerSensor(Sensor s) {
        super(DEFAULT_ACCEL_RATE, s);
        devicePath = "event6";
    }

    public String getDevicePath() {
        return devicePath;
    }

    @Override
    public void init() {
        Pointer pointer = fivmRuntime.getCStringFull(devicePath);
        System.out.println("open and syn with " + devicePath);
        int isOpen = SensorNativeNexusImpl.openSensor(pointer, sensor.sensorId);
        if (isOpen == 0) {
            try {
                throw new Exception("fail to open " + devicePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    boolean pollEvent() {
        Pointer event = fivmRuntime.getBuffer(255);
        int numEvent = 0;
        // poll 4 raw events every time.
        while (numEvent < 4) {
            int rc = SensorNativeNexusImpl.pollData(event,
                    Sensor.TYPE_ACCELEROMETER);
            if (rc == 1) {
                eventBuffer[numEvent] = fivmRuntime.fromCStringFull(event);
                // System.out.println(eventBuffer[numEvent]);
                numEvent++;
            }
        }

        if (numEvent == 3)
            isUpdate = true;

        return isUpdate;
    }

    @Override
    void process() {
        isUpdate = false;
        for (String e : eventBuffer) {
            // System.out.println("process() :: " + e);
            int eventType = Integer.parseInt(e.substring(2, 3), 16);
            int eventCode = Integer.parseInt(e.substring(4, 5), 16);

            // System.out.println("process() :: eventType " + eventType);
            // System.out.println("process() :: eventCode " + eventCode);

            if (eventType == Sensor.EV_SYN) {
                int cur = 15;
                int pCur = 15;
                long time[] = new long[4];
                int i = 0;
                while (e.charAt(pCur) != ';') {
                    cur = findIndexOf(e, ',', pCur + 1);
                    if (cur > pCur) {
                        // System.out.println("process() :: substring " +
                        // e.substring(pCur, cur));
                        time[i] = Long.parseLong(e.substring(pCur, cur));
                        pCur = cur + 1;
                        i++;
                    }
                }

                sensor.currEvent.readSecond = time[0];
                sensor.currEvent.readMicroSecond = time[1];
                sensor.currEvent.second = time[2];
                sensor.currEvent.microSenond = time[3];
            } else {
                // read value
                int index = findIndexOf(e, ',', 6);
                float val = 0.0f;

                // System.out.println("process() :: foat index " + index);

                if (index > 0) {
                    // System.out.println("process() :: substring " +
                    // e.substring(6, index));
                    val = Float.parseFloat(e.substring(6, index));
                    // System.out.println("process() :: val " + val);
                    switch (eventCode) {
                        case Sensor.REL_X:
                            sensor.currEvent.values[0] = val
                                    * SensorNativeNexusImpl.CONVERT_SI;
                            break;
                        case Sensor.REL_Y:
                            sensor.currEvent.values[1] = val
                                    * SensorNativeNexusImpl.CONVERT_SI;
                            break;
                        case Sensor.REL_Z:
                            sensor.currEvent.values[2] = val
                                    * SensorNativeNexusImpl.CONVERT_SI;
                            break;
                    }
                }

            }
        }

    }
}
