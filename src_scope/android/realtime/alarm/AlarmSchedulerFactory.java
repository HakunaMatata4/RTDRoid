
package android.realtime.alarm;

import edu.buffalo.rtdroid.SystemConfig;

public class AlarmSchedulerFactory {
    private static AlarmSchedulerFactory instance = null;
    public final static int ALARM_SCHEDULER_TYPE_POOL = 1;
    public final static int ALARM_SCHEDULER_TYPE_ASYNC = 2;
    private AlarmScheduler scheduler;
    private AlarmScheduleThread schedulingThread;

    private AlarmSchedulerFactory() {
        switch (SystemConfig.getAlarmSchedulerType()) {
            case AlarmSchedulerFactory.ALARM_SCHEDULER_TYPE_ASYNC:
            default:
                scheduler = new AsyncScheduler();
                break;
        }
        schedulingThread = new AlarmScheduleThread(scheduler);
        scheduler.setSchedulingthread(schedulingThread);
        schedulingThread.start();
    }

    public static AlarmSchedulerFactory getInstance() {
        if (instance == null) {
            instance = new AlarmSchedulerFactory();
        }
        return instance;
    }

    public AlarmScheduler getShcedulerImpl() {
        return scheduler;
    }

    public AlarmScheduleThread getSchedulingThread() {
        return schedulingThread;
    }
}
