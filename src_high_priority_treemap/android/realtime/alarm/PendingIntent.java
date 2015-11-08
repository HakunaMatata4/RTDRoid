
package android.realtime.alarm;

public class PendingIntent {
    private Runnable mTarget;
    public static final int FLAG_ONE_SHOT = 0;
    public static final int FLAG_CANCEL_CURRENT = 1;
    public static final int FLAG_NO_CREATE = 2;
    public static final int FLAG_UPDATE_CURRENT = 3;

    public RealtimeAlarm alarm;

    /* package */static final int PENDIND_INTENT_CREATED = 4;
    /* package */static final int PENDIND_INTENT_REQUEST = 5;
    /* package */static final int PENDIND_INTENT_REGISTERED = 6;
    /* package */static final int PENDIND_INTENT_CANCELED = 7;

    /* package */long fireTime;
    /* package */long priority;
    /* package */int status;

    public PendingIntent() {
    }

    public PendingIntent(Runnable target) {
        this.mTarget = target;
        fireTime = 0L;
        priority = 0;
        alarm = null;
    }

    public Runnable getRunnable() {
        return mTarget;
    }
}