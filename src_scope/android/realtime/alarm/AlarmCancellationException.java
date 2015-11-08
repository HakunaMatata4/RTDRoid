
package android.realtime.alarm;

public class AlarmCancellationException extends Exception {
    private static final long serialVersionUID = 1L;

    public AlarmCancellationException() {
    }

    public AlarmCancellationException(String name) {
        super(name);
    }

    public AlarmCancellationException(Exception cause) {
        super(cause);
    }
}
