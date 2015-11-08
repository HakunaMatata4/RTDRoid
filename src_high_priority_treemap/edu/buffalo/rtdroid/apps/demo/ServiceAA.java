
package edu.buffalo.rtdroid.apps.demo;
import android.app.Service;
import android.content.Intent;
import android.content.IntentPool;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

public class ServiceAA extends Service {
    public static final int DEMO_STANDALONE_MSG = 1;
    public static final String HANDLER_KEY = "handler";

    public void onCreate() {
        System.out.println("started Service 1 onCreate()...");
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public int onStartCommand(Intent intent, int flag, int startId) {
        System.out.println("started Service 1 onStartCommand()...");
        //Intent receiverIntent = new Intent(getApplicationContext(), TestReceiver.class);
//        Intent receiverIntent = IntentPool.instance().requestObject();
//        receiverIntent.setAction(TestReceiver.class);
//        receiverIntent.setData(1000);
//        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(receiverIntent);
        return 1;
    }

    public void onDestroy() {
        System.out.println("started Service 1 onDestroy()...");
    }

    public void onStop() {
        System.out.println("started Service 1 onStop()...");
    }
}
