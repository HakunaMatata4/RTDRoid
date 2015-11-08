
package android.app;

import javax.realtime.RealtimeThread;

public class RepeatableRunnable implements Runnable {
    Service service;
    String className;

    public RepeatableRunnable(Service s, String cn) {
        this.service = s;
        this.className = cn;
    }

    public void run() {
        // while(ComponentsCollector.getInstance().getIsRepeat(className)){
        // RealtimeThread.currentRealtimeThread().waitForNextPeriod();
        // service.onRepeat();
        // }
    }

}
