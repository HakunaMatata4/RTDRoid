package android.app;

import javax.realtime.AsyncEvent;
import javax.realtime.AsyncEventHandler;

import com.fiji.rtdroid.ServiceConfiguration;

import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ServiceInfo;
import android.os.IBinder;

public class ServiceDelegator implements Runnable {
    public static final int ACTION_CREATE = 1;
    public static final int ACTION_START = 2;
    public static final int ACTION_BIND = 3;
    public static final int ACTION_DESTROY = 4;
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
    private AsyncEvent event;
    private AsyncEventHandler handler;
    private ServiceConfiguration config;
    private ServiceInfo info;

    public ServiceDelegator(Service _service, ServiceConfiguration config) {
        this.service = _service;
        this.handler = new AsyncEventHandler(this);
        this.event = new AsyncEvent();
        this.event.setHandler(handler);
        this.config = config;
        this.info = new ServiceInfo(null, config.getName(), config);
        this.mode = MODE_INITIATED;
    }

    public void run() {
        switch (action) {
            case ACTION_START:
                service.onStartCommand(intent, Service.START_NOT_STICKY, 0);
                mode = MODE_ACTIVE;
                break;
            case ACTION_DESTROY:
                service.onDestroy();
                mode = MODE_DESTRYED;
                return;
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

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public Service getServvice() {
        return service;
    }

    public IBinder onBind(Intent intent) {
        return service.onBind(intent);
    }
    
    public void onDestroy(Intent intent){
    	service.onBind(intent);
    }
    
    public void onStartCommand(Intent intent, int flags, int pid){
    	service.onStartCommand(intent, flags, pid);
    }

    public AsyncEvent getEvent() {
        return event;
    }

    public AsyncEventHandler getHandler() {
        return handler;
    }

    public ServiceConfiguration getConfig() {
        return config;
    }

    public ServiceInfo getInfo() {
        return info;
    }
}