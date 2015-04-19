
package android.hardware;

import java.io.FileDescriptor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.os.Bundle;
import android.os.IInterface;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.realtime.RTHandler;

public class SystemSensorService {
    SensorManager sensorMgr;
    List<SensorBase> deviceList;
    // Deliver handler
    private RTHandler handler;
    public static final int DELIVER = 0;
    public static final String SENSOR_ID = "sensor_id";
    public static final String DELIVER_PEIORITY = "delivery_priority";

    public SystemSensorService(SensorManager mgr) {
        deviceList = new ArrayList<SensorBase>();
        this.sensorMgr = mgr;

        handler = new DeliverHandler("sensor_delivery_handler");
        // initialize polling thread
        SensorFactoryImpl factoryImpl = new SensorFactoryImpl();
        // poll thread
        factoryImpl.createSensors(deviceList, this);
    }

    public Iterator<SensorBase> getSensorList() {
        return deviceList.iterator();
    }

    public RTHandler getHandler() {
        return handler;
    }

    public class DeliverHandler extends RTHandler {
        public static final int MSG_INC = 1;
        public static final int MSG_END = 2;
        public static final int MSG_EXP1 = 3;
        public static final int MSG_EXP2_RT = 4;

        public DeliverHandler(String name) {
            super(name);
        }

        public void handleMessage(Message msg) {
            // RealtimeThread rtThread = RealtimeThread.currentRealtimeThread();
            switch (msg.what) {
                case DELIVER:
                    // System.out.println("deliver message handler...");
                    Bundle bundle = msg.getData();
                    int sensorId = bundle.getInt(SENSOR_ID);
                    int deliveryPriority = bundle.getInt(DELIVER_PEIORITY);
                    // System.out.println("msg priority:" + deliveryPriority);
                    // System.out.println("delivery thread:" +
                    // rtThread.getPriority() + "==>"
                    // +RealTimeHelper.getInstance().convertToAppPriority(rtThread.getPriority()));
                    // System.out.println("[DEBUG] : sensor id:" + sensorId +
                    // ", priority " + deliveryPriority);
                    // populate sensor data
                    SensorEvent sensorData = new SensorEvent(sensorId,
                            sensorMgr.sensors[sensorId]);
                    for (int i = 0; i < Sensor.VAL.length; i++) {
                        sensorData.values[i] = bundle.getFloat(Sensor.VAL[i]);
                    }
                    sensorData.second = bundle.getLong(Sensor.TV_SEC);
                    sensorData.microSenond = bundle.getLong(Sensor.TV_USEC);
                    // experimental purpose
                    sensorData.readSecond = bundle.getLong(Sensor.TV1_SEC);
                    sensorData.readMicroSecond = bundle
                            .getLong(Sensor.TV1_USEC);

                    // call the onchange() function
                    List<SensorEventListener> subscriptors = sensorMgr.sensors[sensorId].listenerList
                            .get(deliveryPriority);
                    for (SensorEventListener listener : subscriptors) {
                        listener.onSensorChanged(sensorData);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
