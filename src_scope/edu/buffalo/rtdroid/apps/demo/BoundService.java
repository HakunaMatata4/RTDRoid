
package edu.buffalo.rtdroid.apps.demo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.realtime.RTHandler;

public class BoundService extends Service {
    public static final int DEMO_BOUNDSERVICE_MSG = 1;
    public static final int DEMO_BOUNDSERVICE_HANDLER_MSG = 2;
    private boolean mBound;
    private Object lock;
    private RTHandler handler;
    
    public void onCreate() {
        System.out.println("BoundService Service onCreate()...");
        lock = new Object();
        mBound = false;
        handler = new BoundServiceHadnler("BoundService");
    }

    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("bound service onBind()...");
        return new Messenger(handler).getBinder();
    }

    /**
     * Handler of incoming messages from clients.
     */
    class BoundServiceHadnler extends RTHandler {
        public BoundServiceHadnler(String name) {
            super(name);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DEMO_BOUNDSERVICE_HANDLER_MSG:
                    System.out.println(">>>>>>>>> Recieve msg from other service... ");
            }
        }
    }

    public void onDestroy() {
        System.out.println("BoundService Service onDestroy()...");
        handler.stopAll();
    }
}
