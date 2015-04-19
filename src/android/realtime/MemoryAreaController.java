
package android.realtime;

import javax.realtime.PriorityParameters;
import javax.realtime.RealtimeThread;
import javax.realtime.ScopedMemory;

public class MemoryAreaController implements Runnable {
    Object lock;
    Object notifier;
    ScopedMemory scopedArea;
    RealtimeThread thread;

    public MemoryAreaController(ScopedMemory scopedArea) {
        this.lock = new Object();
        this.notifier = new Object();
        this.scopedArea = scopedArea;
        this.thread = new RealtimeThread(this);
        this.thread.setSchedulingParameters(new PriorityParameters(98));
    }

    public void waitForExit() {
        thread.start();
    }

    public void run() {
        try {
            synchronized (notifier) {
                notifier.notifyAll();
            }
            // hold the area as long as it wants
            synchronized (lock) {
                lock.wait();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exit() {
        synchronized (lock) {
            lock.notify();
        }
    }

    public void syncRequest() {
        synchronized (notifier) {
            try {
                notifier.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
