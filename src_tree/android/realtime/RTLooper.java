
package android.realtime;

import javax.realtime.PriorityParameters;
import javax.realtime.PriorityScheduler;
import javax.realtime.RealtimeThread;

import android.os.Looper;

public class RTLooper extends RealtimeThread {
    private String name;
    private int mPriority;
    private long mTid = -1L;
    private Looper mLooper;

    public RTLooper(String name) {
        super();
        this.name = name;
        // mPriority = Process.THREAD_PRIORITY_DEFAULT;
        mPriority = PriorityScheduler.MIN_PRIORITY;
    }

    /**
     * Constructs a HandlerThread.
     * 
     * @param name
     * @param priority The priority to run the RealtimeThread at. The value
     *            supplied must be from {@link android.os.Process} and not from
     *            java.lang.Thread.
     */
    public RTLooper(String name, int priority) {
        super();
        this.name = name;
        mPriority = priority;
    }

    /**
     * Call back method that can be explicitly over ridden if needed to execute
     * some setup before Looper loops.
     */
    protected void onLooperPrepared() {
    }

    public void run() {
        System.out.println("RTLooper>>start new looper:" + name);
        // mTid = Process.myTid();
        RealtimeThread curThread = RealtimeThread.currentRealtimeThread();
        curThread.setSchedulingParameters(new PriorityParameters(mPriority));
        mTid = curThread.getId();
        Looper.prepare();
        synchronized (this) {
            mLooper = Looper.myLooper();
            notifyAll();
        }
        onLooperPrepared();
        Looper.loop();
        mTid = -1L;
    }

    /**
     * This method returns the Looper associated with this RealtimeThread. If
     * this RealtimeThread not been started or for any reason is isAlive()
     * returns false, this method will return null. If this RealtimeThread has
     * been started, this method will block until the looper has been
     * initialized.
     * 
     * @return The looper.
     */
    public Looper getLooper() {
        if (!isAlive()) {
            return null;
        }

        // If the RealtimeThread has been started, wait until the looper has
        // been created.
        synchronized (this) {
            while (isAlive() && mLooper == null) {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }
        }

        return mLooper;
    }

    /**
     * Ask the currently running looper to quit. If the RealtimeThread has not
     * been started or has finished (that is if {@link #getLooper} returns
     * null), then false is returned. Otherwise the looper is asked to quit and
     * true is returned.
     */
    public boolean quit() {
        Looper looper = getLooper();
        if (looper != null) {
            looper.quit();
            return true;
        }
        return false;
    }

    /**
     * Returns the identifier of this RealtimeThread. See Process.myTid().
     */
    public long getThreadId() {
        return mTid;
    }

    public String getThreadName() {
        return name;
    }
}
