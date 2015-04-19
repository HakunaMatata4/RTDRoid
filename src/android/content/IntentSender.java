
package android.content;

import android.util.AndroidException;

public class IntentSender {
    /**
     * Exception thrown when trying to send through a PendingIntent that has
     * been canceled or is otherwise no longer able to execute the request.
     */
    public static class SendIntentException extends AndroidException {
        public SendIntentException() {
        }

        public SendIntentException(String name) {
            super(name);
        }

        public SendIntentException(Exception cause) {
            super(cause);
        }
    }
}
