
package android.app;

import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.ContextWrapper;

public abstract class Activity extends ContextWrapper implements
        ComponentCallbacks {

    public Activity() {
        super();
        System.out.println("Activity default abstract constructor");
    }

    public Activity(Context base) {
        super(base);
    }

    public void onCreate() {
    }

    public void onStart() {

    }

    public void onDestroy() {

    }

    public void onResume() {

    }

    public void onPause() {

    }

    public void onStop() {

    }

    public void onLowMemory() {

    }
}
