package edu.buffalo.rtdroid;

import javax.realtime.PriorityParameters;
import javax.realtime.RealtimeThread;

import com.fiji.fivm.Time;
import com.fiji.rtdroid.AndroidApplication;

import android.realtime.alarm.AlarmManager;
import android.realtime.alarm.PendingIntent;

public class Bootstrap{

    public static void initialize(final AndroidApplication application, String[] args) {	
    	RealtimeThread root = new RealtimeThread(new Runnable() {
    		public void run() {
    			AlarmManager am = AlarmManager.getInstance();
    			long currentTime = Time.nanoTime();
    			Runnable r1 = new Thread1();
    			Runnable r2 = new Thread2();
    			PendingIntent intent1 = new PendingIntent(r1);
            	PendingIntent intent2 = new PendingIntent(r2);
            	am.set(currentTime + (5 * 1000000000L), intent1);
            	am.set(currentTime + (10 * 1000000000L), intent2);
    		}
    	});
    	root.setSchedulingParameters(new PriorityParameters(90));
    	root.start();
    }
    
    public static class Thread1 implements Runnable{
		@Override
		public void run() {
			System.out.println("In thread 1");
		}
    }
    
    public static class Thread2 implements Runnable{
		@Override
		public void run() {
			System.out.println("In thread 2");
		}
    }
}