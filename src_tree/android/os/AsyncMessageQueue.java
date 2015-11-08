
package android.os;

import android.util.AndroidRuntimeException;
import android.util.Log;

/*
 * This class is for the asynchronous scheduler to hold all of the coming
 * message
 * the data structure is like a linked list and order by the priority of the
 * message
 */
public class AsyncMessageQueue extends MessageQueue {

    public AsyncMessageQueue() {
        super();
    }

    public boolean enqueueMessage(Message msg, long when) {
        if (msg.when != 0) {
            throw new AndroidRuntimeException(msg
                    + " This message is already in use.");
        }
        if (msg.target == null && !mQuitAllowed) {
            throw new RuntimeException("Main thread not allowed to quit");
        }
        synchronized (this) {
            if (mQuiting) {
                RuntimeException e = new RuntimeException(msg.target
                        + " sending message to a Handler on a dead thread");
                Log.w("MessageQueue", e.getMessage(), e);
                return false;
            } else if (msg.target == null) {
                mQuiting = true;
            }

            msg.when = when;
            // Log.d("MessageQueue", "Enqueing: " + msg);
            // p is the current next message that is going to be processed
            Message p = mMessages;
            size++;
            // insert the message based on msg.when && msg.priority
            if (p == null) {
                msg.next = p;
                mMessages = msg;
                this.notify();
            } else {
                Message prev = null;
                while (p != null && p.when <= when
                        && p.priority >= msg.priority) {
                    prev = p;
                    p = p.next;
                }
                msg.next = prev.next;
                prev.next = msg;
                this.notify();
            }
        }
        return true;
    }

}
