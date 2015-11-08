package edu.buffalo.rtdroid;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import javax.realtime.PriorityParameters;
import javax.realtime.RealtimeThread;

import android.realtime.alarm.AlarmManager;
import android.realtime.alarm.PendingIntent;

import com.fiji.fivm.Time;
import com.fiji.rtdroid.AndroidApplication;

public class Bootstrap{

	public static void initialize(final AndroidApplication application, String[] args) {
		for(int iter=1; iter<=20; iter++){
			System.out.println("Performing iteration " + iter);
			Runnable alarmThread = new AlarmThread(iter, 10);
			alarmThread.run();
			try {
				Thread.sleep(10000);
				IntentThread.counter = 0;
			} catch (InterruptedException e) {}
		}
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {}
		System.exit(0);
	}

	public static class IntentThread implements Runnable{
		static int counter = 0;
		Object lock = new Object();
		int id;
		String name;
		public IntentThread(String name){
			synchronized(lock){
				this.name = name;
				counter++;
				id = counter;
			}
		}
		@Override
		public void run() {
			File file = new File(name);
			try {
				FileOutputStream fos = new FileOutputStream(file, true);
				fos.write(("Event fire: " + String.valueOf(Time.nanoTime() + "\n")).getBytes());
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("In thread " + id);
		}
		public String toString() {
			return String.valueOf(id);
		}
	} 
	
	public static class IntentHigh implements Runnable{
		static int counter = 0;
		Object lock = new Object();
		int id;
		String name;
		public IntentHigh(String name){
			synchronized(lock){
				this.name = name;
				counter++;
				id = counter;
			}
		}
		@Override
		public void run() {
			File file = new File(name);
			try {
				FileOutputStream fos = new FileOutputStream(file, true);
				fos.write(("Event fire: " + String.valueOf(Time.nanoTime() + "\n")).getBytes());
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("In high priority thread " + id);
		}
		public String toString() {
			return String.valueOf(id);
		}
	} 

	public static class AlarmThread implements Runnable{
		private int threadNo;
		private int iter;
		private int priority;
		private String filename = null;
		
		public AlarmThread(int iter, int priority){
			this.iter = iter;
			this.priority = priority;
		}
		public void run() {
			RealtimeThread root = new RealtimeThread(new Runnable(){
				@Override
				public void run() {
					AlarmManager am = AlarmManager.getInstance();
					for(threadNo=1; threadNo<=iter; threadNo++){
						long timestamp = Time.nanoTime() + Long.valueOf(1000000000L + randPriority(5, 10)*1000000L);
						FileOutputStream fos = null;
						filename = "Thread" + iter + "_"+ threadNo +".time";
						File file = new File( filename );
						try {
							IntentThread intentThread = new IntentThread( filename );
							fos = new FileOutputStream(file);
							fos.write(("FileName: " + filename + "\n").getBytes());
							fos.write(("Priority: " + priority +"\n").getBytes());
							fos.write(("Timestamp: " + timestamp +"\n").getBytes());
							fos.write(("Before set: " + String.valueOf(Time.nanoTime())+"\n").getBytes());
							am.set(timestamp, new PendingIntent(intentThread));
							fos.write(("After set: " + String.valueOf(Time.nanoTime()+"\n")).getBytes());
							fos.close();
						} catch (IOException e) {}
					}
				}
			});
			root.setSchedulingParameters(new PriorityParameters(priority));
			root.start();
			RealtimeThread high = new RealtimeThread(new Runnable(){
				@Override
				public void run() {
					AlarmManager am = AlarmManager.getInstance();
					long timestamp = Time.nanoTime() + Long.valueOf(1000000000L + 11*1000000L);
					FileOutputStream fos = null;
					filename = "Thread" + iter + "_"+ (iter+1) +".time";
					File file = new File( filename );
					try {
						IntentHigh intentThread = new IntentHigh( filename );
						fos = new FileOutputStream(file);
						fos.write(("FileName: " + filename + "\n").getBytes());
						fos.write(("Priority: " + priority +"\n").getBytes());
						fos.write(("Timestamp: " + timestamp +"\n").getBytes());
						fos.write(("Before set: " + String.valueOf(Time.nanoTime())+"\n").getBytes());
						am.set(timestamp, new PendingIntent(intentThread));
						fos.write(("After set: " + String.valueOf(Time.nanoTime()+"\n")).getBytes());
						fos.close();
					} catch (IOException e) {}
				}
			});
			high.setSchedulingParameters(new PriorityParameters(90));
			high.start();
		}
	}

	public static int randPriority( int min, int max ) {
		Random rand = new Random();
		int randNum = rand.nextInt( ( max - min ) + 1 ) + min;
		return randNum;
	}
}
