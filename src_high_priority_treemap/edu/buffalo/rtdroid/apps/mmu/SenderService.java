
package edu.buffalo.rtdroid.apps.mmu;

import javax.realtime.PriorityParameters;
import javax.realtime.RealtimeThread;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.realtime.RTHandler;
import android.util.Log;

public class SenderService extends Service {
    public static final int NUM_MSG = 1000 * 2;
    public static final long INTERVAL_MSG = 20L;
    private static String TAG = "HighService";
    private RTHandler handler = null;
    private boolean isBound = false;
    private int counter = 0;

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            System.out.println("onServiceConnection");
            handler = ((ReceiverService) service).getRTHandler();
            isBound = true;
        }

        public void onServiceDisconnected(ComponentName className) {
            Log.d(TAG, "onServiceDisconnected");
            handler = null;
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        bindService(new Intent(this, ReceiverService.class), mConnection,
                Context.BIND_AUTO_CREATE);

        RealtimeThread thread = new RealtimeThread() {
            public void run() {
                try {
                    RealtimeThread
                            .currentRealtimeThread()
                            .setSchedulingParameters(new PriorityParameters(90));
                    while (!isBound) {
                        RealtimeThread.sleep(10);
                    }
                    Log.i(TAG, "----send message----");
                    while (counter < NUM_MSG) {
                        if (handler == null)
                            System.out.println("mMessage is null");
                        long tmpBeforeAlloc = System.nanoTime();
                        System.out.println("send msg number:" + counter);
                        Message msg = handler
                                .obtainMessage(ReceiverService.PING_MSG);
                        Bundle b = new Bundle();
                        b.putLong(ReceiverService.MSG_BEFORE_MSG_ALLO,
                                tmpBeforeAlloc);
                        b.putLong(ReceiverService.MSG_BEFORE_MSG_SEND,
                                System.nanoTime());
                        msg.setData(b);
                        handler.sendMessage(msg);
                        Thread.sleep(INTERVAL_MSG);
                        counter++;
                    }
                    // send end message
                    Message endMsg = handler
                            .obtainMessage(ReceiverService.MSG_END);
                    handler.sendMessage(endMsg);
                } catch (InterruptedException e) {
                    Log.e(TAG, "RemoteException", e);
                }
            }
        };
        thread.start();
        return 1;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.stopAll();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
}
