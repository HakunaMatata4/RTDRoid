package android.realtime.alarm;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.realtime.PriorityParameters;
import javax.realtime.RealtimeThread;

import com.fiji.fivm.Time;

import edu.buffalo.rtdroid.SystemConfig;

public class AlarmScheduleThread extends RealtimeThread {
	public static TreeMap<Long, TreeMap<Integer, LinkedList<RealtimeAlarm>>> scheduleTree = new TreeMap<Long, TreeMap<Integer, LinkedList<RealtimeAlarm>>>();
	public static TreeMap<Integer, TreeMap<Long, LinkedList<RealtimeAlarm>>> aux = new TreeMap<Integer, TreeMap<Long, LinkedList<RealtimeAlarm>>>();
	private final int MAX_SIZE = 5;
	private AlarmScheduler schedler;
	private Object lock;

    public void run() {
        RealtimeThread currThread = RealtimeThread.currentRealtimeThread();
        currThread.setSchedulingParameters(new PriorityParameters(SystemConfig.getMaxPriority()));
        while(true) {
            synchronized (lock) {
                try {
                    Entry<Long, TreeMap<Integer, LinkedList<RealtimeAlarm>>> entry = getNextTimestamp();
                    if (entry == null) {
                        lock.wait();
                    } else if((entry.getKey() - Time.nanoTime()) > 15000000){
                    	Thread.sleep(15);        //TODO: make this thread sleep for 15 milliseconds.
                    } else {
                        long timestamp = entry.getKey();
                        TreeMap<Integer, LinkedList<RealtimeAlarm>> node = entry.getValue();
                        Iterator<LinkedList<RealtimeAlarm>> listIt = node
                                .values().iterator();
                        while (listIt.hasNext()) {
                            LinkedList<RealtimeAlarm> alarmList = listIt.next();
                            Iterator<RealtimeAlarm> it = alarmList.iterator();
                            while (it.hasNext()) {
                                RealtimeAlarm alarm = it.next();
                                schedler.schdulerAlarm(alarm);
                            }
                        }
                        scheduleTree.remove(timestamp);
                    }
                } catch (InterruptedException e) {}
            }
        }
    }

	public AlarmScheduleThread(AlarmScheduler scheduler) {
		TreeMap<Integer, LinkedList<RealtimeAlarm>> map = new TreeMap<Integer, LinkedList<RealtimeAlarm>>();
		LinkedList<RealtimeAlarm> list = new LinkedList<RealtimeAlarm>();
		for(int i=0; i<MAX_SIZE; i++){
			list.add(new RealtimeAlarm());
		}
		map.put(1, list);
		scheduleTree.put(-1l, map);
		this.schedler = scheduler;
		lock = new Object();
	}

	private Entry<Long, TreeMap<Integer, LinkedList<RealtimeAlarm>>> getNextTimestamp(){
		Iterator<Map.Entry<Long, TreeMap<Integer, LinkedList<RealtimeAlarm>>>> iteratorUpper = null;
		iteratorUpper = scheduleTree.entrySet().iterator();
		iteratorUpper.next();
		if(iteratorUpper.hasNext()) return iteratorUpper.next();
		return null;
	}

	public void setAlarm(long timestamp, int priority, PendingIntent activity) {
		RealtimeAlarm alarm = requestObject(priority);
		alarm.setParameters(timestamp, priority, activity);
		TreeMap<Integer, LinkedList<RealtimeAlarm>> subTreeTime = null;
		TreeMap<Long, LinkedList<RealtimeAlarm>> subTreePriority = null;
		LinkedList<RealtimeAlarm> alarmsTime = null;
		LinkedList<RealtimeAlarm> alarmsPriority = null;
		if(scheduleTree.containsKey(timestamp)){
			subTreeTime = scheduleTree.get(timestamp);
			if(subTreeTime.containsKey(priority)) {
				alarmsTime = subTreeTime.get(priority);
				alarmsTime.add(alarm);
			}
			else{
				alarmsTime = new LinkedList<RealtimeAlarm>();
				alarmsTime.add(alarm);
				subTreeTime.put(priority, alarmsTime);
			}
		}
		else{
			subTreeTime = new TreeMap<Integer, LinkedList<RealtimeAlarm>>();
			alarmsTime = new LinkedList<RealtimeAlarm>();
			alarmsTime.add(alarm);
			subTreeTime.put(priority, alarmsTime);
		}
		scheduleTree.put(timestamp, subTreeTime);
		if(aux.containsKey(priority)){
			subTreePriority = aux.get(priority);
			if(subTreePriority.containsKey(timestamp)) {
				alarmsPriority = subTreePriority.get(timestamp);
				alarmsPriority.add(alarm);
			}
			else{
				alarmsPriority = new LinkedList<RealtimeAlarm>();
				alarmsPriority.add(alarm);
				subTreePriority.put(timestamp, alarmsPriority);
			}
		}
		else{
			subTreePriority = new TreeMap<Long, LinkedList<RealtimeAlarm>>();
			alarmsPriority = new LinkedList<RealtimeAlarm>();
			alarmsPriority.add(alarm);
			subTreePriority.put(timestamp, alarmsPriority);
		}
		aux.put(priority, subTreePriority);
		//lock.notifyAll();  //TODO: throws java.lang.IllegalMonitorStateException
	}

	public void cancel(PendingIntent activity) {
		Iterator<Map.Entry<Long, TreeMap<Integer, LinkedList<RealtimeAlarm>>>> iteratorUpper = null;
		Iterator<Map.Entry<Integer, LinkedList<RealtimeAlarm>>> iteratorInner = null;
		if (activity == null) {
			throw new NullPointerException();
		} 
		else {
			boolean flag = false;
			RealtimeAlarm targetAlarm = null;
			iteratorUpper = scheduleTree.entrySet().iterator();
			while(iteratorUpper.hasNext()){
				iteratorInner = iteratorUpper.next().getValue().entrySet().iterator();
				while(iteratorInner.hasNext()){
					Iterator<RealtimeAlarm> alarmIterator = iteratorInner.next().getValue().iterator();
					LinkedList<RealtimeAlarm> list = iteratorInner.next().getValue();
					while(alarmIterator.hasNext()){
						targetAlarm = alarmIterator.next();
						if(targetAlarm.getActivity().equals(activity)){
							targetAlarm.setParameters(-1, 1, null);
							list.remove(targetAlarm);
							scheduleTree.get(-1l).get(1).add(targetAlarm);
							flag = true;
						}
						if(flag) break;
					}
					if(flag) break;
				}
				if(flag) break;
			}
		}
	}

	private RealtimeAlarm requestObject(int priority){
		LinkedList<RealtimeAlarm> list = scheduleTree.get(-1l).get(1);
		RealtimeAlarm alarm = list.poll();
		if(alarm == null) {
			if(aux.lastKey() > priority){
				alarm = aux.lastEntry().getValue().lastEntry().getValue().poll();
				long tempTime = alarm.getTimestamp();
				int tempPriority = alarm.getPriority();
				if(aux.get(tempPriority).get(tempTime).size() == 0) aux.get(tempPriority).remove(tempTime);
				if(aux.get(tempPriority).size() == 0) aux.remove(tempPriority);
				scheduleTree.get(tempTime).get(tempPriority).remove(alarm);
				if(scheduleTree.get(tempTime).get(tempPriority).size() == 0) scheduleTree.get(tempTime).remove(tempPriority);
				if(scheduleTree.get(tempTime).size() == 0) scheduleTree.remove(tempTime);
				alarm.setParameters(-1, 1, null);
			}
			else throw new IllegalArgumentException("Exception");
		}
		return alarm;
	}

	@SuppressWarnings("unused")
	private void recycleObject(Entry<Long, TreeMap<Integer, LinkedList<RealtimeAlarm>>> tree){
		long key = tree.getKey();
		LinkedList<RealtimeAlarm> list = scheduleTree.get(-1l).get(1);
		for(Entry<Integer, LinkedList<RealtimeAlarm>> set : tree.getValue().entrySet()){
			for(RealtimeAlarm alarm : set.getValue()){
				long tempTime = alarm.getTimestamp();
				int tempPriority = alarm.getPriority();
				alarm.setParameters(-1, 1, null);
				list.add(alarm);
				aux.get(tempPriority).get(tempTime).remove(alarm);
				if(aux.get(tempPriority).get(tempTime).size() == 0) aux.get(tempPriority).remove(tempTime);
				if(aux.get(tempPriority).size() == 0) aux.remove(tempPriority);
			}
		}
		scheduleTree.remove(key);
	}

	public int getCapacity() {
		return MAX_SIZE;
	}
}