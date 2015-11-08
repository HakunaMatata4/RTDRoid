package android.hardware;

import java.util.ArrayList;
import java.util.Map.Entry;

import javax.realtime.AbsoluteTime;
import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.RealtimeThread;
import javax.realtime.RelativeTime;

import edu.buffalo.rtdroid.experimentUtil.RealTimeHelper;
import edu.buffalo.rtdroid.experimentUtil.ResultCollector;
import edu.buffalo.rtdroid.experimentUtil.TimeVal;
import android.os.Bundle;
import android.os.Message;
import android.realtime.RTHandler;

public class ProcessingThread extends RealtimeThread {
    SystemSensorService internalSystem;
    // Object lock;
    SensorBase sensorDev;
    SensorEvent deliverdCopy;

    public ProcessingThread(SystemSensorService service, SensorBase sensorDev,
            String name) {
        this.internalSystem = service;
        this.sensorDev = sensorDev;
        deliverdCopy = new SensorEvent(sensorDev.sensor.sensorId);
    }

    public void run() {
        RealtimeThread currentThreaad = RealtimeThread.currentRealtimeThread();
        currentThreaad.setSchedulingParameters(new PriorityParameters(
                sensorDev.sensor.priority));

        long startTime = System.nanoTime() / (sensorDev.rate * 1000L)
                * (sensorDev.rate * 1000L) + sensorDev.rate * 1000L;
        currentThreaad.setReleaseParameters(new PeriodicParameters(
                new AbsoluteTime(startTime / 1000000,
                        (int) (startTime % 1000000)), new RelativeTime(
                        sensorDev.rate, 0)));

        System.out.println("Sensor Id: before" + sensorDev.sensor.sensorId
                + " with " + sensorDev.rate + "ms...");
        System.out.println("processing thread:"
                + this.getPriority()
                + "==>"
                + RealTimeHelper.getInstance()
                        .FijiFIFO2RTSJ(this.getPriority()));

        try {
            while (true) {
                waitForNextPeriod();
                if (sensorDev.pollEvent()) {
                    sensorDev.process();
                    // System.out.println("[processing thread] " +
                    // sensorDev.sensor.currEvent.toString());
                    /*
                     * every time when we send message, we need to grap the new
                     * handler since the processing thread may change
                     * dynamically according to the priority of his client
                     */
                    TimeVal beforeMessage = ResultCollector.getSystemNano();
                    int sensorId = sensorDev.sensor.sensorId;
                    for (Entry<Integer, ArrayList<SensorEventListener>> entry : internalSystem.sensorMgr.sensors[sensorId].listenerList
                            .entrySet()) {
                        int deliveryPriority = entry.getKey();
                        RTHandler h = internalSystem.getHandler();
                        Message deliverMsg = h.obtainMessage(
                                SystemSensorService.DELIVER, entry.getKey());
                        Bundle b = deliverMsg.getData();
                        b.putInt(SystemSensorService.SENSOR_ID, sensorId);
                        b.putInt(SystemSensorService.DELIVER_PEIORITY,
                                deliveryPriority);
                        for (int i = 0; i < Sensor.VAL.length; i++) {
                            b.putFloat(
                                    Sensor.VAL[i],
                                    internalSystem.sensorMgr.sensors[sensorId].currEvent.values[i]);
                        }
                        b.putLong(
                                Sensor.TV_SEC,
                                internalSystem.sensorMgr.sensors[sensorId].currEvent.second);
                        b.putLong(
                                Sensor.TV_USEC,
                                internalSystem.sensorMgr.sensors[sensorId].currEvent.microSenond);
                        b.putLong(Sensor.TV1_SEC, beforeMessage.tv_sec);
                        b.putLong(Sensor.TV1_USEC, beforeMessage.tv_usec);

                        deliverMsg.setData(b);
                        deliverMsg.sendToTarget();
                    }
                }
            }// end of while loop
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
