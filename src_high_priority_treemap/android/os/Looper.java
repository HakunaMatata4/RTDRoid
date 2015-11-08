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

import edu.buffalo.rtdroid.SystemConfig;
import android.realtime.RTHandler;
import android.util.Config;
import android.util.Printer;

/**
 * Class used to run a message loop for a thread. Threads by default do not have
 * a message loop associated with them; to create one, call {@link #prepare} in
 * the thread that is to run the loop, and then {@link #loop} to have it process
 * messages until the loop is stopped.
 * <p>
 * Most interaction with a message loop is through the {@link Handler} class.
 * <p>
 * This is a typical example of the implementation of a Looper thread, using the
 * separation of {@link #prepare} and {@link #loop} to create an initial Handler
 * to communicate with the Looper.
 * 
 * <pre>
 * class LooperThread extends Thread {
 * 	public Handler mHandler;
 * 
 * 	public void run() {
 * 		Looper.prepare();
 * 
 * 		mHandler = new Handler() {
 * 			public void handleMessage(Message msg) {
 * 				// process incoming messages here
 * 			}
 * 		};
 * 
 * 		Looper.loop();
 * 	}
 * }
 * </pre>
 */
public class Looper {
	private static final boolean DEBUG = false;

	// sThreadLocal.get() will return null unless you've called prepare().
	private static final ThreadLocal<Looper> sThreadLocal = new ThreadLocal<Looper>();

	final MessageQueue mQueue;
	volatile boolean mRun;
	Thread mThread;
	private static Looper mMainLooper = null;

	/**
	 * Initialize the current thread as a looper. This gives you a chance to
	 * create handlers that then reference this looper, before actually starting
	 * the loop. Be sure to call {@link #loop()} after calling this method, and
	 * end it by calling {@link #quit()}.
	 */
	public static final void prepare() {
		if (sThreadLocal.get() != null) {
			throw new RuntimeException("Only one Looper may be created per thread");
		}
		sThreadLocal.set(new Looper());
	}

	/**
	 * Initialize the current thread as a looper, marking it as an application's
	 * main looper. The main looper for your application is created by the
	 * Android environment, so you should never need to call this function
	 * yourself. {@link #prepare()}
	 */

	public static final void prepareMainLooper() {
		prepare();
		setMainLooper(myLooper());
	}

	private synchronized static void setMainLooper(Looper looper) {
		mMainLooper = looper;
	}

	/**
	 * Returns the application's main looper, which lives in the main thread of
	 * the application.
	 */
	public synchronized static final Looper getMainLooper() {
		return mMainLooper;
	}

	/**
	 * Run the message queue in this thread. Be sure to call {@link #quit()} to
	 * end the loop.
	 */
	public static final void loop() {
		Looper me = myLooper();
		MessageQueue queue = me.mQueue;
		while (true) {
			Message msg;
			msg = queue.next();
			if (!me.mRun) {
				break;
			}
			if (msg != null) {
				if (msg.target == null) {
					// No target is a magic identifier for the quit message.
					return;
				}
				msg.target.dispatchMessage(msg);
				msg.recycle();
				if (SystemConfig.SCOPED_MEMORY) {
					MessagePool.instance().recycleObject(msg);
				}
			}
		}
	}

	/**
	 * Return the Looper object associated with the current thread. Returns null
	 * if the calling thread is not associated with a Looper.
	 */
	public static final Looper myLooper() {
		return (Looper) sThreadLocal.get();
	}

	/**
	 * Return the {@link MessageQueue} object associated with the current
	 * thread. This must be called from a thread running a Looper, or a
	 * NullPointerException will be thrown.
	 */
	public static final MessageQueue myQueue() {
		return myLooper().mQueue;
	}

	private Looper() {
		mQueue = new MessageQueue();
		mRun = true;
		mThread = Thread.currentThread();
	}

	public void quit() {
		Message msg = Message.obtain();
		// NOTE: By enqueueing directly into the message queue, the
		// message is left with a null target. This is how we know it is
		// a quit message.
		mQueue.enqueueMessage(msg, 0);
	}

	/**
	 * Return the Thread associated with this Looper.
	 */
	public Thread getThread() {
		return mThread;
	}

	/*
	 * return the message queue associated with this Looper. it has to be call
	 * after the looper started, so call getLooper() first!
	 */
	public MessageQueue getMessageQueue() {
		return mQueue;
	}

	public void dump(Printer pw, String prefix) {
		pw.println(prefix + this);
		pw.println(prefix + "mRun=" + mRun);
		pw.println(prefix + "mThread=" + mThread);
		pw.println(prefix + "mQueue=" + ((mQueue != null) ? mQueue : "(null"));
		if (mQueue != null) {
			synchronized (mQueue) {
				Message msg = mQueue.mMessages;
				int n = 0;
				while (msg != null) {
					pw.println(prefix + "  Message " + n + ": " + msg);
					n++;
					msg = msg.next;
				}
				pw.println(prefix + "(Total messages: " + n + ")");
			}
		}
	}

	public String toString() {
		return "Looper{" + Integer.toHexString(System.identityHashCode(this)) + "}";
	}

	static class HandlerException extends Exception {
		private static final long serialVersionUID = 1L;

		HandlerException(Message message, Throwable cause) {
			super(createMessage(cause), cause);
		}

		static String createMessage(Throwable cause) {
			String causeMsg = cause.getMessage();
			if (causeMsg == null) {
				causeMsg = cause.toString();
			}
			return causeMsg;
		}
	}
}
