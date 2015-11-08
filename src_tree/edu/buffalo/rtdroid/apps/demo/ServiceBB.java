
package edu.buffalo.rtdroid.apps.demo;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.MessagePool;
import android.os.Messenger;
import android.os.RTMessage;
import android.os.RemoteException;
import android.realtime.RTHandler;

public class ServiceBB extends Service {
    public static final int DEMO_STANDALONE_MSG = 1;
    boolean mBound;
    private Messenger remoteMessenger;
    
    private ServiceConnection conn = new ServiceConnection() {
        
        public void onServiceDisconnected(ComponentName name) {
            remoteMessenger = null;
            mBound = false;
        }
        
        public void onServiceConnected(ComponentName name, IBinder binder) {
            System.out.println("set remote messenger...");
            remoteMessenger = new Messenger(binder);
        }
    };
    
    public void onCreate() {
        System.out.println("started Service 2 onCreate()...");
    }

    public int onStartCommand(Intent intent, int flag, int startId) {
        System.out.println("started Service 2 onStartCommand()...");
        Intent i = new Intent(this, BoundService.class);
        bindService(i, conn, Context.BIND_AUTO_CREATE);
        
        if( remoteMessenger != null ){
            try {
                System.out.println("send message ...");
                RTMessage rtmsg = new RTMessage() {
					
					public Message initMessage() {
						Message msg = MessagePool.instance().requestObject();
						msg.what = BoundService.DEMO_BOUNDSERVICE_HANDLER_MSG;
						return msg;
					}
				};
                remoteMessenger.send(rtmsg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }else{
            System.out.println("remote messenger is null...");
        }
        
        return 1;
    }

    public void onDestroy() {
        System.out.println("started Service 2 onDestroy()...");
        unbindService(conn);
    }

    public void onStop() {
        System.out.println("started Service 2 onStop()...");
    }

    public IBinder onBind(Intent intent) {
        return null;
    }
}
