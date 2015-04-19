
package edu.buffalo.rtdroid;

import javax.realtime.ImmortalMemory;
import javax.realtime.MemoryArea;
import javax.realtime.PriorityParameters;
import javax.realtime.RealtimeThread;

import android.realtime.alarm.AlarmManager;

import com.fiji.rtdroid.AndroidApplication;

public class Bak_Bootstrap {
    public static void initialize(final AndroidApplication application, String[] args) {
/*        if (SystemConfig.SCOPED_MEMORY) {
            // TODO We should fix this in fijiVM, currently the memory area
            // assigned in constructor are not reflected at runtime ;(
            RealtimeThread root = new RealtimeThread(new Runnable() {
                public void run() {
                    MemoryArea immortal = ImmortalMemory.instance();
                    immortal.enter(new ScopedBootInit(application));
                }
            });
            root.setSchedulingParameters(new PriorityParameters(SystemConfig.getMaxPriority()));
            root.start();
        } else {
        	RealtimeThread root = new RealtimeThread(new BootInit(application));
            root.setSchedulingParameters(new PriorityParameters(SystemConfig.getMaxPriority()));
            root.start();
        }*/
    	System.out.println(AlarmManager.getInstance().toString());
    }
}
