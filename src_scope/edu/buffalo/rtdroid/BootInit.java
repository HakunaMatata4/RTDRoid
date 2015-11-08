
package edu.buffalo.rtdroid;

import android.app.Activity;
import android.app.ActivityDelegator;
import android.app.Service;
import android.app.ServiceDelegator;
import android.content.ContextImpl;
import android.content.IntentResolver;
import com.fiji.rtdroid.ActivityConfiguration;
import com.fiji.rtdroid.AndroidApplication;
import com.fiji.rtdroid.ServiceConfiguration;
import edu.buffalo.rtdroid.apps.demo.DemoActivity;


public class BootInit implements Runnable {

    AndroidApplication application;

    public BootInit(AndroidApplication _application) {
        this.application = _application;
    }

    public void run() {
    	// Instantiate component objects
        final ActivityConfiguration[] activityConfigArray = application
                .getActivities();
        IntentResolver reslover = IntentResolver.getInstance();
        // initiate activity
        ActivityConfiguration activityConfig = activityConfigArray[0];
        Activity activity = new DemoActivity();
        activity.attachBaseContext(new ContextImpl(activityConfig.getName()));
        ActivityDelegator delegator = new ActivityDelegator(activity,
                activityConfig);

        reslover.registerActivity(activityConfig.getName(), delegator);
        // initiate all services
        final ServiceConfiguration[] serviceConfigArray = application
                .getServices();
        for (int i = 0; i < serviceConfigArray.length; i++) {
            Service service = application
                    .instantiateService(serviceConfigArray[i]);
            service.attachBaseContext(new ContextImpl(serviceConfigArray[i]
                    .getName()));
            ServiceDelegator serviceDelegator = new ServiceDelegator(service,
                    serviceConfigArray[i]);
            reslover.registerService(serviceConfigArray[i].getName(),
                    serviceDelegator);
            service.onCreate();
        }
        activity.onCreate();
        activity.onStart();
        activity.onDestroy();
    }
}
