package android.realtime.alarm;

import javax.realtime.PriorityParameters;
import javax.realtime.RealtimeThread;

import com.fiji.fivm.Time;

import edu.buffalo.rtdroid.SystemConfig;

public class AlarmScheduleThread extends RealtimeThread {
	private final int MAX_SIZE = 100;
	private AlarmScheduler scheduler;
	private Object lock;

	public void run() {
		RealtimeThread currThread = RealtimeThread.currentRealtimeThread();
		currThread.setSchedulingParameters(new PriorityParameters(SystemConfig.getMaxPriority()));
		while(true) {
			synchronized (lock) {
				try {
					RealtimeAlarm rootAlarm = RealtimeAlarm.getNextTimestamp();
					if(rootAlarm == null){
						lock.wait();
					}
					else{
						long val = (rootAlarm.getTimeStamp() - Time.nanoTime())/1000000;
						if(val > 10){
							RealtimeThread.sleep(val);
						}
						RealtimeAlarm alarm;
						while((alarm = RealtimeAlarm.getNext()) != null){
							while(true){
								RealtimeAlarm releasedAlarm = alarm;
								alarm = alarm.getNextInList();
								scheduler.schdulerAlarm(releasedAlarm);
								if(alarm == null) break;
							}
						}
					}
				} catch (Exception e) {}
			}
		}
	}
	
	public AlarmScheduleThread(AlarmScheduler scheduler) {
		this.scheduler = scheduler;
		lock = new Object();
		RealtimeAlarm.fillPool(MAX_SIZE);
	}

	public void setAlarm(long timestamp, int priority, PendingIntent activity) {
		synchronized (lock) {
			RealtimeAlarm.getRoot().insert(RealtimeAlarm.fetch(timestamp, priority, activity));
			lock.notify();
			RealtimeThread.currentRealtimeThread().interrupt();
		}
	}

	public int getCapacity() {
		return MAX_SIZE;
	}
}