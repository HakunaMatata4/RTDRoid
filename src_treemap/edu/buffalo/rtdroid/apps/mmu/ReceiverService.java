
package edu.buffalo.rtdroid.apps.mmu;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Messenger;
import android.realtime.RTHandler;

public class ReceiverService extends Service {
    private static String TAG = "ReceiveService";

    public static final int PING_MSG = 1;
    public static final int MSG_END = 2;
    public static final String MSG_BEFORE_MSG_ALLO = "before_msg_alloc";
    public static final String MSG_BEFORE_MSG_SEND = "before_msg_send";

    private RTHandler handler;

    @Override
    public void onCreate() {
        this.handler = new RecieverHandler(this, "ReceiverService");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new Messenger(handler).getBinder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("receive service onDestroy");
    }

    public RTHandler getRTHandler() {
        return handler;
    }
}
