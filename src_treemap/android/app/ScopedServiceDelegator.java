
package android.app;

import java.util.Map;
import java.util.TreeMap;

import javax.realtime.AsyncEvent;
import javax.realtime.AsyncEventHandler;
import javax.realtime.LTMemory;

import com.fiji.rtdroid.AndroidApplication;
import com.fiji.rtdroid.ServiceConfiguration;

import edu.buffalo.rtdroid.SystemConfig;
import edu.buffalo.rtdroid.apps.demo.ServiceAA;
import edu.buffalo.rtdroid.apps.demo.ServiceBB;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextImpl;
import android.content.Intent;
import android.content.IntentPool;
import android.content.ScopedContextImpl;
import android.content.ServiceConnection;
import android.content.pm.ServiceInfo;
import android.os.IBinder;
import android.os.IMessenger;
import android.os.Messenger;
import android.realtime.MemoryAreaController;

public class ScopedServiceDelegator implements Runnable {
    public static final int ACTION_CREATE = 1;
    public static final int ACTION_START = 2;
    public static final int ACTION_BIND = 3;
    public static final int ACTION_DESTROY = 4;
    public static final int ACTION_BROADCAST = 12;
    public static final int ACTION_DENIMATION = 13;
    public static final int MODE_INITIATED = 6;
    public static final int MODE_ACTIVE = 7;
    public static final int MODE_INACTIVE = 8;
    public static final int MODE_BOUND = 9;
    public static final int MODE_UNBOUND = 10;
    public static final int MODE_DESTRYED = 11;

    private int mode;
    private int action;
    Service service;
    private Intent intent;
    private ServiceConnection conn;
    private AsyncEvent event;
    private AsyncEventHandler handler;
    private ServiceConfiguration config;
    private ServiceInfo info;
    private LTMemory area;
    private MemoryAreaController controller;
    private Messenger messenger;
    //class name => receiver
    private Map<String, BroadcastReceiver> receiverList;
    //action name => receiver
    private Map<String, BroadcastReceiver> actionToReceiver;

    public ScopedServiceDelegator(final AndroidApplication appConfig,
            final ServiceConfiguration config, LTMemory area) {
        this.area = area;
        this.config = config;
        this.conn = null;
        this.info = new ServiceInfo(null, config.getName(), config);
        this.mode = MODE_INITIATED;
        this.controller = new MemoryAreaController(area);
        controller.waitForExit();
        controller.syncRequest();
        service = appConfig.instantiateService(config);
        //TODO it is better to use factory ;)
        service.attachBaseContext(new ScopedContextImpl(config.getName()));
        handler = new AsyncEventHandler(this);
        event = new AsyncEvent();
        event.setHandler(handler);
        messenger =  null;
        receiverList = new TreeMap<String, BroadcastReceiver>();
        actionToReceiver = new TreeMap<String, BroadcastReceiver>();
    }
    
    public ScopedServiceDelegator(final AndroidApplication appConfig,
            final String receiverName, LTMemory area){
        this.area = area;
        this.config = null;
        this.conn = null;
        this.info = null;
        this.mode = MODE_INITIATED;
        this.controller = new MemoryAreaController(area);
        controller.waitForExit();
        controller.syncRequest();
        service = null;
        handler = new AsyncEventHandler(this);
        event = new AsyncEvent();
        event.setHandler(handler);
        messenger =  null;
        receiverList = new TreeMap<String, BroadcastReceiver>();
        actionToReceiver = new TreeMap<String, BroadcastReceiver>();
    }

    public void run() {
        try {
            switch (action) {
                case ACTION_BROADCAST:
                    String recieverName = intent.getComponentName().getClassName();
                    receiverList.get(recieverName).onReceive(SystemConfig.getApplicationContext(), intent);
                    break;
                /*cases for service*/
                case ACTION_CREATE:
                    service.onCreate();
                    break;
                case ACTION_START:
                    service.onStartCommand(intent, Service.START_NOT_STICKY, 0);
                    mode = MODE_ACTIVE;
                    break;
                case ACTION_BIND:
                    service.onBind(intent);
                    mode = MODE_ACTIVE;
                    break;
                case ACTION_DENIMATION:
                    return;
                case ACTION_DESTROY:
                    System.out.println(service.toString());
                    service.onDestroy();
                    mode = MODE_DESTRYED;
                    return;
            }
            if( intent != null ){
                IntentPool.instance().recycleObject(intent);
                intent = null;
            }    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkMode() {
        switch (mode) {
            case MODE_ACTIVE:
                return (action == MODE_INITIATED) ? true : false;
            case MODE_INACTIVE:
                return (action == MODE_INACTIVE) ? true : false;
            case MODE_INITIATED:
                return (action == ACTION_START) ? true : false;
            case MODE_DESTRYED:
                return (action == ACTION_CREATE) ? true : false;
            default:
                return false;
        }
    }

    public void onCreate() {
        action = ACTION_CREATE;
        run();
    }
    
    public void onDestroty() {
        action = ACTION_DESTROY;
        event.fire();
        controller.exit();
    }

    public void onStartCommand(Intent intent, int flag) {
        action = ACTION_START;
        event.fire();
    }

    public Messenger onBind(Intent _intent) {
        if( messenger ==  null ) { 
            messenger = new Messenger(service.onBind(intent));
        }
        
        if( intent != null ){
            IntentPool.instance().recycleObject(intent);
            intent = null;
        } 
        
        return messenger;
    }
    
    public void onReceive(Context context, Intent intent){
        this.intent = intent;
        action = ACTION_BROADCAST;
        event.fire();
    }
    
    public void terminate(){
        action = ACTION_DENIMATION;
        event.fire();
        controller.exit();
    }
    
    public Messenger getMessenger(){
        return messenger;
    }
    
    public void addReceiver(String name, BroadcastReceiver r){
        receiverList.put(name, r);
    }
    public void removeReceiver(String name, BroadcastReceiver r){
        receiverList.remove(name);
    }
    /*
     * getters and setters
     */
    public void setIntnet(Intent intent){
        this.intent = intent;
    }
    public void setServiceConnection(ServiceConnection conn) {
        this.conn = conn;
    }
}
