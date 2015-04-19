
package edu.buffalo.rtdroid;

import javax.realtime.LTMemory;
import javax.realtime.RealtimeThread;
import javax.realtime.ScopedMemory;

import android.app.ScopedActivityDelegator;
import android.app.ScopedServiceDelegator;
import android.content.IntentPool;
import android.content.ScopedIntentResolver;
import android.os.Message;
import android.os.MessagePool;
import android.support.v4.content.LocalBroadcastManager;

import com.fiji.rtdroid.ActivityConfiguration;
import com.fiji.rtdroid.AndroidApplication;
import com.fiji.rtdroid.ServiceConfiguration;

import edu.buffalo.rtdroid.apps.demo.TestReceiver;

public class ScopedBootInit implements Runnable {

    AndroidApplication application;

    public ScopedBootInit(AndroidApplication _application) {
        this.application = _application;
    }

    public void run() {
        try {
            System.out.println("initial string buffer for printer in immortal area");

            // Instantiate component objects
            final ActivityConfiguration[] activityConfigArray =
                    application.getActivities();
            final ScopedIntentResolver reslover = ScopedIntentResolver.getInstance();
            reslover.setAndroidConfiguration(application);
            //make sure application context object is allocated in immortal.
            SystemConfig.getApplicationContext();
            //initiate singleton in immortal
            IntentPool.instance();
            MessagePool.instance();
            LocalBroadcastManager.getInstance(SystemConfig.getApplicationContext());
            // initiate activity
            ActivityConfiguration activityConfig = activityConfigArray[0];
            final ScopedMemory activityArea =
                    reslover.registerActivity(activityConfig);
            ServiceConfiguration[] serviceConfigArray =
                    application.getServices();
            for (int i = 0; i < serviceConfigArray.length; i++) {
                final ServiceConfiguration serviceConfig = serviceConfigArray[i];
                ScopedMemory area = reslover.registerService(serviceConfig);
                area.enter(new Runnable() {
                    public void run() {
                        try {
                            ScopedServiceDelegator delegator = (ScopedServiceDelegator) ((ScopedMemory) RealtimeThread.getCurrentMemoryArea()).getPortal();
                            delegator.onCreate();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }   
            
            //register all declared Receiver in manifest ...
            final LTMemory receiverMemory = new LTMemory(SystemConfig.DEMO_ACTIVITY_SIZE);
            receiverMemory.enter(new Runnable() {
                public void run() {
                    try {
                        TestReceiver r = new TestReceiver();
                        ScopedServiceDelegator delegator = new ScopedServiceDelegator(null, r.getClass().getName(), receiverMemory);
                        receiverMemory.setPortal(delegator);
                        LocalBroadcastManager.getInstance(SystemConfig.getApplicationContext()).registerReceiver(r, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            

            activityArea.enter(new Runnable() {
                public void run() {
                    ScopedActivityDelegator delegator = (ScopedActivityDelegator) ((ScopedMemory) RealtimeThread.getCurrentMemoryArea()).getPortal();
                    delegator.onCreate();
                }
            });
            
            
            activityArea.enter(new Runnable() {
                public void run() {
                    ScopedActivityDelegator delegator = (ScopedActivityDelegator) ((ScopedMemory) RealtimeThread.getCurrentMemoryArea()).getPortal();
                    delegator.onDestroy();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
