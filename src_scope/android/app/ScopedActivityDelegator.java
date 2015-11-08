
package android.app;

import javax.realtime.AsyncEvent;
import javax.realtime.AsyncEventHandler;
import javax.realtime.ScopedMemory;

import android.content.ContextImpl;
import android.content.ScopedContextImpl;
import android.content.pm.ActivityInfo;
import android.realtime.MemoryAreaController;

import com.fiji.fivm.Settings;
import com.fiji.rtdroid.ActivityConfiguration;
import com.fiji.rtdroid.AndroidApplication;

import edu.buffalo.rtdroid.apps.demo.DemoActivity;

/**
 * internal activity delegate
 */
public class ScopedActivityDelegator implements Runnable {
    Activity activity;
    public static final int ACTION_CREATE = 1;
    public static final int ACTION_START = 2;
    public static final int ACTION_PAUSE = 3;
    public static final int ACTION_STOP = 4;
    public static final int ACTION_DESTROY = 5;
    public static final int ACTION_RESUME = 6;
    public static final int MODE_INITIATED = 7;
    public static final int MODE_ACTIVE = 8;
    public static final int MODE_INACTIVE = 9;
    public static final int MODE_DESTRYED = 10;

    private int mode;
    private int action;
    private AsyncEvent event;
    private AsyncEventHandler handler;
    private ActivityConfiguration config;
    private ActivityInfo info;
    private ScopedMemory area;
    private MemoryAreaController controller;

    public ScopedActivityDelegator(final AndroidApplication appConfig,
            final ActivityConfiguration config, ScopedMemory area) {
        this.area = area;
        this.config = config;
        this.info = new ActivityInfo(null, config.getName(), config);
        this.mode = MODE_INITIATED;
        this.controller = new MemoryAreaController(this.area);
        controller.waitForExit();
        controller.syncRequest();
        final String name = config.getName();
        activity = new DemoActivity();
        //TODO it is better to use factory ;)
        activity.attachBaseContext(new ScopedContextImpl(name));
        handler = new AsyncEventHandler(this);
        event = new AsyncEvent();
        event.setHandler(handler);
        action = ACTION_CREATE;
    }

    public void run() {
        try {
            switch (action) {
                case ACTION_CREATE:
                    activity.onCreate();
                    mode = MODE_INACTIVE;
                    break;
                case ACTION_START:
                    activity.onStart();
                    mode = MODE_ACTIVE;
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
                    return;
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    public void onStart() {
        action = ACTION_START;
        run();
    }

    public void onCreate() {
        action = ACTION_CREATE;
        run();
    }

    public void onDestroy() {
        action = ACTION_DESTROY;
        run();
        controller.exit();
    }
}
