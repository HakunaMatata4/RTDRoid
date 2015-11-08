/*
 * Copyright (C) 2006 The Android Open Source Project
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.os;
import android.util.AndroidRuntimeException;
import android.util.Config;
import android.util.Log;

// import com.android.internal.os.RuntimeInit;

/**
 * Low-level class holding the list of messages to be dispatched by a
 * {@link Looper}. Messages are not added directly to a MessageQueue, but rather
 * through {@link Handler} objects associated with the Looper.
 * <p>
 * You can retrieve the MessageQueue for the current thread with
 * {@link Looper#myQueue() Looper.myQueue()}.
 */
public class MessageQueue {
	protected Message mMessages = null;
	protected boolean mQuiting = false;
	protected boolean mQuitAllowed = true;
	protected int size = 0;

	public MessageQueue() {
	}

	public final Message next() {
		while (true) {
			long now;

			// Try to retrieve the next message, returning if found.
			// long wakeUp = System.nanoTime();
			synchronized (this) {
				// TODO
				now = System.nanoTime();
				Message msg = pullNextLocked(now);

				if (msg == null) {
					try {
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					return msg;
				}
			}
		}
	}

	protected Message pullNextLocked(long now) {
		Message msg = mMessages;
		if (msg != null) {
			if (now >= msg.when) {
				size--;
				mMessages = msg.next;
				if (Config.LOGV)
					Log.v("MessageQueue", "Returning message: " + msg);
				return msg;
			}
		}

		return null;
	}

	protected boolean enqueueMessage(Message msg, long when) {
		if (msg.when != 0) {
			throw new AndroidRuntimeException(msg + " This message is already in use.");
		}
		if (msg.target == null && !mQuitAllowed) {
			throw new RuntimeException("Main thread not allowed to quit");
		}
		synchronized (this) {
			if (mQuiting) {
				RuntimeException e = new RuntimeException(msg.target + " sending message to a Handler on a dead thread");
				Log.w("MessageQueue", e.getMessage(), e);
				return false;
			} else if (msg.target == null) {
				mQuiting = true;
			}

			msg.when = when;
			// System.out.println("MessageQueue Enqueing: " + msg.toString());
			Message p = mMessages;
			size++;
			if (p == null || when == 0 || when < p.when) {
				msg.next = p;
				mMessages = msg;
				notify();
			} else {
				Message prev = null;
				while (p != null && p.when <= when) {
					prev = p;
					p = p.next;
				}
				msg.next = prev.next;
				prev.next = msg;
				msg.msgQueue = this;
				// msg.beforeNotify = System.nanoTime();
				notify();
			}
		}// end of sync
		return true;
	}

	protected boolean removeMessages(Handler h, int what, Object object, boolean doRemove) {
		synchronized (this) {
			Message p = mMessages;
			boolean found = false;

			// Remove all messages at front.
			while (p != null && p.target == h && p.what == what && (object == null || p.obj == object)) {
				if (!doRemove)
					return true;
				found = true;
				Message n = p.next;
				mMessages = n;
				p.recycle();
				p = n;
				size--;
			}

			// Remove all messages after front.
			while (p != null) {
				Message n = p.next;
				if (n != null) {
					if (n.target == h && n.what == what && (object == null || n.obj == object)) {
						if (!doRemove)
							return true;
						found = true;
						Message nn = n.next;
						n.recycle();
						p.next = nn;
						size--;
						continue;
					}
				}
				p = n;
				size--;
			}

			// Remove all messages after front.
			while (p != null) {
				Message n = p.next;
				if (n != null) {
					if (n.target == h && n.what == what && (object == null || n.obj == object)) {
						if (!doRemove)
							return true;
						found = true;
						Message nn = n.next;
						n.recycle();
						p.next = nn;
						size--;
						continue;
					}
				}
				p = n;
			}

			return found;
		}
	}

	protected void removeMessages(Handler h, Runnable r, Object object) {
		if (r == null) {
			return;
		}

		synchronized (this) {
			Message p = mMessages;

			// Remove all messages at front.
			while (p != null && p.target == h && p.callback == r && (object == null || p.obj == object)) {
				Message n = p.next;
				mMessages = n;
				p.recycle();
				p = n;
				size--;
			}

			// Remove all messages after front.
			while (p != null) {
				Message n = p.next;
				if (n != null) {
					if (n.target == h && n.callback == r && (object == null || n.obj == object)) {
						Message nn = n.next;
						n.recycle();
						p.next = nn;
						size--;
						continue;
					}
				}
				p = n;
			}
		}
	}

	final void removeCallbacksAndMessages(Handler h, Object object) {
		synchronized (this) {
			Message p = mMessages;

			// Remove all messages at front.
			while (p != null && p.target == h && (object == null || p.obj == object)) {
				Message n = p.next;
				mMessages = n;
				p.recycle();
				p = n;
				size--;
			}

			// Remove all messages after front.
			while (p != null) {
				Message n = p.next;
				if (n != null) {
					if (n.target == h && (object == null || n.obj == object)) {
						Message nn = n.next;
						n.recycle();
						p.next = nn;
						size--;
						continue;
					}
				}
				p = n;
			}
		}
	}

	public int size() {
		return size;
	}

	/*
	 * private void dumpQueue_l() { Message p = mMessages;
	 * System.out.println(this + "  queue is:"); while (p != null) {
	 * System.out.println("            " + p); p = p.next; } }
	 */
	
	public Message dropNextMessage(){
		Message msg;
		synchronized (this) {
			size--;
			msg = mMessages;
			mMessages = msg.next;
		}
		return msg;
	}

	void poke() {
		synchronized (this) {
			this.notify();
		}
	}
}
