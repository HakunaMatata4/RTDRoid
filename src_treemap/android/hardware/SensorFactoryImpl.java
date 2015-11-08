
package android.hardware;

import java.util.List;

import edu.buffalo.rtdroid.SystemConfig;

public class SensorFactoryImpl implements SensorFactory {

    public void createSensors(List<SensorBase> devList,
            SystemSensorService service) {
        if (SystemConfig.exp_name.equals(SystemConfig.JPAPBENCH)) {
            IRSimSensor irSensor = new IRSimSensor(
                    service.sensorMgr.sensors[Sensor.TYPE_IR_SIM]);
            devList.add(irSensor);
            ProcessingThread irProThread = new ProcessingThread(service,
                    irSensor, "ir_sim_thread");
            irProThread.start();
            System.out.println("finish ir_sim init");
        } else {
            // real sensors
            switch (SystemConfig.exp_platform) {
                case SystemConfig.EXP_RT_LINUX:
                case SystemConfig.EXP_RT_PHONE: {
                    /****** initiate accelerometer sensor ******/
                    SensorBase accl = new AccelerometerSensor(
                            service.sensorMgr.sensors[Sensor.TYPE_ACCELEROMETER]);
                    accl.init();
                    devList.add(accl);
                    ProcessingThread acclProThread = new ProcessingThread(
                            service, accl, "accl_thread");
                    acclProThread.start();
                    System.out.println("finish accelerometer init");

                    /****** initiate gyroscope sensor ******/
                    SensorBase gyroscope = new GyroscopeSensor(
                            service.sensorMgr.sensors[Sensor.TYPE_GYROSCOPE]);
                    gyroscope.init();
                    devList.add(gyroscope);
                    ProcessingThread gyroProThread = new ProcessingThread(
                            service, gyroscope, "gtro_thread");
                    gyroProThread.start();
                    System.out.println("finish gyroscope init");

                    break;
                }
                case SystemConfig.EXP_RTEMS:
                    break;
                default: {
                    try {
                        throw new Exception("system configuration error.");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

        }
    }
}
