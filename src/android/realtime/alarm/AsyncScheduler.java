package android.realtime.alarm;

import javax.realtime.AsyncEvent;
import javax.realtime.AsyncEventHandler;
import javax.realtime.PriorityParameters;
import javax.realtime.RealtimeThread;

import com.fiji.fivm.Time;

public class AsyncScheduler extends AlarmScheduler {

	private AlarmRunnableWrapper run;
	
    public AsyncScheduler() {
        super();
    }

    public void schdulerAlarm(RealtimeAlarm alarm) {
    	run = new AlarmRunnableWrapper(alarm);
        RealtimeThread rt = new RealtimeThread(run);
        rt.start();
    }

    public class AlarmRunnableWrapper implements Runnable {
    	private  RealtimeAlarm alarm;
        private boolean PENDIND_INTENT_CANCELED = true;
        
        public AlarmRunnableWrapper(RealtimeAlarm alarm) {
            this.alarm = alarm;
        }
        
        public void cancel(){
        	PENDIND_INTENT_CANCELED = false;
        }

        public void run() {
            RealtimeThread currThread = RealtimeThread.currentRealtimeThread();
            currThread.setSchedulingParameters(new PriorityParameters(alarm.getPriority()));
            AsyncEventHandler handler = new AsyncEventHandler(alarm.getActivity().getRunnable());
            handler.setSchedulingParameters(new PriorityParameters(alarm.getPriority()));
            handler.setDaemon(false);
            AsyncEvent event = new AsyncEvent();
            event.addHandler(handler);
            Time.sleepAbsolute(alarm.getTimestamp());
            /* try {
				RealtimeThread.sleep(alarm.getTimestamp());
			} catch (InterruptedException e) {}*/
            if (PENDIND_INTENT_CANCELED) {
                event.fire();
            }
            
            if (alarm.isRepeatable()) {
                alarm.setTimestamp(System.currentTimeMillis() + alarm.getRepeatInterval());
                schedulingThread.setAlarm(alarm.getTimestamp(),alarm.getPriority(),alarm.getActivity());
            }
        }    
        
    }
}