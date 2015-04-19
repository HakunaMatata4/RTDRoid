
package android.realtime.alarm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.realtime.AsyncEvent;
import javax.realtime.AsyncEventHandler;
import javax.realtime.PriorityParameters;
import javax.realtime.RealtimeThread;

import edu.buffalo.rtdroid.SystemConfig;

public class ThreadPoolScheduler extends AlarmScheduler {
	//private AlarmPool aPool;
    int capacity = SystemConfig.ALARM_CAPACITY;
    int count;
    ExecutionThread thirdExecutionThread;
    ArrayList<ExecutionThread> threadpool;
    TreeSet<RealtimeAlarm> queue;
    Object lock;

    public ThreadPoolScheduler() {
        super();
        count = SystemConfig.ALARM_CAPACITY;
        thirdExecutionThread = null;
        threadpool = new ArrayList<ExecutionThread>();
        for (int i = 0; i < capacity; i++) {
            ExecutionThread thread = new ExecutionThread();
            threadpool.add(thread);
            thread.start();
        }
        Collections.sort(threadpool, new ExecutionThreadComparator());
        queue = new TreeSet<RealtimeAlarm>(new RealTimeAlarmComparator());
        lock = new Object();
    }

    public synchronized void schdulerAlarm(RealtimeAlarm alarm) {
        queue.add(alarm);
        if (count < capacity) {
            count++;
            for (int i = 0; i < capacity; i++) {
                ExecutionThread current = threadpool.get(i);
                if (!current.isAssigned) {

                    synchronized (current.lock) {
                        current.isAssigned = true;
                        current.when = alarm.getTimestamp();
                        current.priority = alarm.getPriority();
                        current.lock.notifyAll();
                    }
                }
            }
        }
    }

    class RealTimeAlarmComparator implements Comparator<RealtimeAlarm> {

        public int compare(RealtimeAlarm alarm1, RealtimeAlarm alarm2) {
            if (alarm1.getTimestamp() > alarm2.getTimestamp()) {
                return 1;
            } else if (alarm1.getTimestamp() < alarm2.getTimestamp()) {
                return -1;
            } else {
                return alarm1.getPriority() >= alarm2.getPriority() ? -1 : 1;
            }
        }

    }

    class ExecutionThreadComparator implements Comparator<ExecutionThread> {

        public int compare(ExecutionThread thread1, ExecutionThread thread2) {
            if (thread1.when > thread2.when) {
                return 1;
            } else if (thread1.when < thread2.when) {
                return -1;
            } else {
                return thread1.getPriority() >= thread2.getPriority() ? -1 : 1;
            }
        }

    }

    class ExecutionThread extends RealtimeThread {
        RealtimeAlarm alarm;
        int priority;
        long when;
        boolean end;
        boolean isAssigned;
        Object lock;

        public ExecutionThread() {
            priority = 10;
            end = false;
            isAssigned = false;
            when = -1L;
            lock = new Object();
        }

        public void run() {
            while (!end) {
                try {
                    synchronized (lock) {
                        if (queue.size() == 0) {
                            isAssigned = false;
                            count--;
                            lock.wait();
                        }

                        Iterator<RealtimeAlarm> iterator = queue.iterator();
                        alarm = iterator.next();
                        priority = alarm.getPriority();
                        when = alarm.getTimestamp();
                        iterator.remove();
                        RealtimeThread currThread = RealtimeThread
                                .currentRealtimeThread();
                        currThread
                                .setSchedulingParameters(new PriorityParameters(
                                        priority));
                        // alarm processing
                        long diff = when - System.currentTimeMillis();

                        if (diff > 0) {
                            System.out.println("scheduling:" + alarm.getPriority()
                                    + " to sleep for " + alarm.getTimestamp());
                            RealtimeThread.sleep(diff);
                        }

                        AsyncEventHandler handler = new AsyncEventHandler(alarm.getActivity().getRunnable());
                        handler.setDaemon(true);
                        AsyncEvent event = new AsyncEvent();
                        event.addHandler(handler);
                        // put the alarm to sleep
                        //alarm.wakeup = System.nanoTime();
                        // worst case execution analysis
                        /*if (DROP_ALARMS) {
                            Map.Entry<Long, TreeMap<Integer, LinkedList<RealtimeAlarm>>> higherEntry = schedulingThread.scheduledAlarmContainer
                                    .higherEntry(alarm.getTimestamp());
                            if (higherEntry != null
                                    && System.currentTimeMillis() > higherEntry
                                            .getKey()
                                    && alarm.getPriority() < higherEntry.getValue()
                                            .firstKey()) {
                                alarm.operation.status = PendingIntent.PENDIND_INTENT_CANCELED;
                                System.out.println("pool:Drop alarm!!");
                                schedulingThread.dropAlarm();
                            }
                        }*/
                        // alarm execution
                        /*if (alarm.operation.status != PendingIntent.PENDIND_INTENT_CANCELED) {
                            alarm.beforeFired = System.nanoTime();
                            event.fire();
                        }*/
                        // reschedule the repeatable alarm
                        if (alarm.isRepeatable()) {
                            alarm.setTimestamp(System.currentTimeMillis()
                                    + alarm.getRepeatInterval());
                            schedulingThread.setAlarm(alarm.getTimestamp(),alarm.getPriority(),alarm.getActivity());
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }// end of while
        }

    }
}
