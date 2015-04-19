package android.realtime.alarm;

import javax.realtime.RealtimeThread;

import edu.buffalo.rtdroid.experimentUtil.RealTimeHelper;

public class AlarmManager {
    public static final int RTC_WAKEUP = 0;
    public static final int RTC = 1;
   	public static final int ELAPSED_REALTIME_WAKEUP = 2;
   	public static final int ELAPSED_REALTIME = 3;
  
    private static AlarmManager instance = null;
    private AlarmScheduleThread schedulingThread;

    private AlarmManager() {
        schedulingThread = AlarmSchedulerFactory.getInstance().getSchedulingThread();
    }

    public static AlarmManager getInstance() {
        if (instance == null) {
            instance = new AlarmManager();
        }
        return instance;
    }
    
    public AlarmScheduleThread getSchedulingThread(){
    	return schedulingThread;
    }

    public void set(long timestamp, PendingIntent operation) {
	    int priority = RealTimeHelper.getInstance().FijiFIFO2RTSJ(RealtimeThread.currentRealtimeThread().getPriority());
	    schedulingThread.setAlarm(timestamp, priority, operation);
    }
    
    public void setRepeatIng(int type, long triggerAtmMillis,
            long intervalMills, PendingIntent operation) {
    	// alarm.type = type;
        //alarm.repeatInterval = intervalMills;
        //alarm.isRepeatable = true;
        int pr = RealTimeHelper.getInstance().FijiFIFO2RTSJ(
                RealtimeThread.currentRealtimeThread().getPriority());
        
        //schedulingThread.setAlarm(timestamp, priority, operation);

    }
}