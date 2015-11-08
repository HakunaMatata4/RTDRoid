package android.app;

import javax.realtime.AsyncEvent;
import javax.realtime.AsyncEventHandler;

import android.content.pm.ActivityInfo;

import com.fiji.rtdroid.ActivityConfiguration;

/**
 * internal activity delegate
 */
public class ActivityDelegator implements Runnable {
    Activity activity;
    public static final int ACTION_CREATE = 1;
    public static final int ACTION_PAUSE = 2;
    public static final int ACTION_STOP = 3;
    public static final int ACTION_DESTROY = 4;
    public static final int ACTION_RESUME = 5;
    public static final int MODE_INITIATED = 6;
    public static final int MODE_ACTIVE = 7;
    public static final int MODE_INACTIVE = 8;
    public static final int MODE_DESTRYED = 9;

    private int mode;
    private int action;
    private AsyncEvent event;
    private AsyncEventHandler handler;
    private ActivityConfiguration config;
    private ActivityInfo info;

    public ActivityDelegator(Activity activity, ActivityConfiguration config) {
        this.activity = activity;
        this.handler = new AsyncEventHandler(this);
        this.event = new AsyncEvent();
        this.event.setHandler(handler);
        this.config = config;
        this.info = new ActivityInfo(null, config.getName(), config);
        this.mode = MODE_INITIATED;
    }

    public void run() {
        switch (action) {
            case ACTION_CREATE:
                activity.onCreate();
                mode = MODE_ACTIVE;
                break;
            case ACTION_RESUME:
                activity.onResume();
                mode = MODE_ACTIVE;
            case ACTION_PAUSE:
                activity.onPause();
                mode = MODE_INACTIVE;
                break;
            case ACTION_STOP:
                activity.onStop();
                mode = MODE_INACTIVE;
                break;
            case ACTION_DESTROY:
                activity.onDestroy();
                mode = MODE_DESTRYED;
                break;
        }
    }

    public boolean checkMode() {
        switch (mode) {
            case MODE_ACTIVE:
                return (action == ACTION_PAUSE || action == ACTION_STOP || action == ACTION_DESTROY) ? true
                        : false;
            case MODE_INACTIVE:
                return (action == ACTION_RESUME || action == ACTION_CREATE) ? true
                        : false;
            case MODE_INITIATED:
                return (action == ACTION_CREATE) ? true : false;
            case MODE_DESTRYED:
                return (action == ACTION_CREATE) ? true : false;
            default:
                return false;
        }
    }

    public void setAction(int action) {
        this.action = action;
    }

    public AsyncEvent getEvent() {
        return event;
    }

    public AsyncEventHandler getHandler() {
        return handler;
    }

    public ActivityConfiguration getConfig() {
        return config;
    }

    public ActivityInfo getInfo() {
        return info;
    }
}