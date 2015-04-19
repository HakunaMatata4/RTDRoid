package android.realtime.alarm;

import android.realtime.alarm.AsyncScheduler.AlarmRunnableWrapper;

public class RealtimeAlarm{
	
	public int type;
    /** count for the execution times for repeating alarm */
    public int count;
	private long timestamp = -1;
	private int priority = 1;
	private PendingIntent activity = null;
	private boolean isRepeatable;
	private long repeatInterval;
	//private AlarmRunnableWrapper handler = null;   //TODO: to be used
	
	public void setParameters(long timestamp, int priority, PendingIntent activity){
		this.count = 0;
		this.timestamp = timestamp;
		this.priority = priority;
		this.activity = activity;
		this.isRepeatable = false;
	}
	
	public String toString(){
		String str = "";
		str += "Timestamp = " + timestamp + "\n";
		str += "Priority = " + priority + "\n";
		return str;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public PendingIntent getActivity() {
		return activity;
	}

	public void setActivity(PendingIntent activity) {
		this.activity = activity;
	}

	public boolean isRepeatable() {
		return isRepeatable;
	}

	public void setRepeatable(boolean isRepeatable) {
		this.isRepeatable = isRepeatable;
	}
	
	public long getRepeatInterval() {
		return repeatInterval;
	}

	public void setRepeatInterval(long repeatInterval) {
		this.repeatInterval = repeatInterval;
	}
	
}