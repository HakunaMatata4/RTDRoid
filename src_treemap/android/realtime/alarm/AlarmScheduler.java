
package android.realtime.alarm;

public abstract class AlarmScheduler {
    protected static final boolean DROP_ALARMS = true;
    protected AlarmScheduleThread schedulingThread;

    public AlarmScheduler() {
    }

    public abstract void schdulerAlarm(RealtimeAlarm alarm);

    public void setSchedulingthread(AlarmScheduleThread thread) {
        this.schedulingThread = thread;
    }
}
