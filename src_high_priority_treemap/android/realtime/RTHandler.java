package android.realtime;

import java.util.Map;
import java.util.TreeMap;

import javax.realtime.PriorityParameters;
import javax.realtime.RealtimeThread;

import edu.buffalo.rtdroid.SystemConfig;
import edu.buffalo.rtdroid.experimentUtil.RealTimeHelper;
import android.os.Handler;
import android.os.IMessenger;
import android.os.Message;
import android.os.MessagePool;
import android.os.RTMessage;
import android.os.RemoteException;

public abstract class RTHandler {
	private String name;
	private Map<Integer, Handler> handlerContainer;
	private Handler nonRTHandler;
	private IMessenger mMessenger;

	public RTHandler(String name) {
		this.name = name;
		this.handlerContainer = new TreeMap<Integer, Handler>();
		RTLooper nonRTThread = new RTLooper(name, SystemConfig.getMinPriority());
		nonRTThread.start();
		nonRTHandler = new Handler(nonRTThread.getLooper(), this);
	}

	public final boolean sendMessageAtTime(Message msg, long uptimeMillis) {
		boolean r = false;
		int currPriority = RealTimeHelper.getInstance().FijiFIFO2RTSJ(Thread.currentThread().getPriority());

		int msgPriority = msg.priority != 0 ? msg.priority : currPriority;
		// System.out.println("RTHandler>>> recieve message with priority " +
		// msgPriority);
		if (msgPriority <= Thread.MAX_PRIORITY && msgPriority >= Thread.MIN_PRIORITY) {
			r = nonRTHandler.sendMessageAtTime(msg, uptimeMillis);
		} else {
			if (handlerContainer.containsKey(msgPriority)) {
				r = handlerContainer.get(msgPriority).sendMessageAtTime(msg, uptimeMillis);
			} else {
				RTLooper thread = new RTLooper(name, msgPriority);
				thread.start();

				android.os.Handler newHandler = new android.os.Handler(thread.getLooper(), this);
				handlerContainer.put(msgPriority, newHandler);
				r = newHandler.sendMessageAtTime(msg, uptimeMillis);
			}
		}

		return r;
	}

	public final boolean sendMessage(Message msg) {
		return sendMessageAtTime(msg, 0);
	}

	/**
	 * Returns a new {@link android.os.Message Message} from the global message
	 * pool. More efficient than creating and allocating new instances. The
	 * retrieved message has its handler set to this instance (Message.target ==
	 * this). If you don't want that facility, just call Message.obtain()
	 * instead.
	 */
	public final Message obtainMessage() {
		android.os.Handler handler = null;
		int currPriority = RealTimeHelper.getInstance().FijiFIFO2RTSJ(RealtimeThread.currentThread().getPriority());
		System.out.println(Thread.currentThread().getPriority() + "=====>" + currPriority);
		if (currPriority <= Thread.MAX_PRIORITY && currPriority >= Thread.MIN_PRIORITY) {
			// System.out.println("return none real-time handler");
			handler = nonRTHandler;
		} else {
			if (handlerContainer.containsKey(currPriority)) {
				handler = handlerContainer.get(currPriority);
			} else {
				RTLooper thread = new RTLooper(name, currPriority);
				thread.setSchedulingParameters(new PriorityParameters(currPriority));
				thread.start();

				Handler newHandler = new Handler(thread.getLooper(), this);
				handlerContainer.put(SystemConfig.getMinPriority(), newHandler);
				handler = newHandler;
			}
		}

		return Message.obtain(handler);
	}

	/**
	 * Same as {@link #obtainMessage()}, except that it also sets the what
	 * member of the returned Message.
	 * 
	 * @param what
	 *            Value to assign to the returned Message.what field.
	 * @return A Message from the global message pool.
	 */
	public final Message obtainMessage(int what) {
		int currPriority = RealTimeHelper.getInstance().FijiFIFO2RTSJ(Thread.currentThread().getPriority());
		return obtainMessage(what, currPriority);
	}

	/**
	 * Same as {@link #obtainMessage()}, except that it also sets the what
	 * member of the returned Message.
	 * 
	 * @param what
	 *            Value to assign to the returned Message.what field.
	 * @return A Message from the global message pool.
	 */
	public final Message obtainMessage(int what, int priority) {
		android.os.Handler handler = null;
		// System.out.println(Thread.currentThread().getPriority()+"=====>" +
		// currPriority);
		if (priority <= Thread.MAX_PRIORITY && priority >= Thread.MIN_PRIORITY) {
			handler = nonRTHandler;
		} else {
			if (handlerContainer.containsKey(priority)) {
				handler = handlerContainer.get(priority);
			} else {
				RTLooper thread = new RTLooper(name, priority);
				thread.setSchedulingParameters(new PriorityParameters(priority));
				thread.start();

				android.os.Handler newHandler = new android.os.Handler(thread.getLooper(), this);
				handlerContainer.put(priority, newHandler);
				handler = newHandler;
			}
		}

		return Message.obtain(handler, what);
	}

	/**
	 * Terminate all real-time looper associated with real-time handler
	 */
	public void stopAll() {
		nonRTHandler.sendEmptyMessage(0);
		nonRTHandler.getLooper().quit();

		for (Map.Entry<Integer, android.os.Handler> item : handlerContainer.entrySet()) {
			item.getValue().sendEmptyMessage(0);
			item.getValue().getLooper().quit();
		}
	}

	public final synchronized IMessenger getIMessenger() {
		if (mMessenger != null) {
			return mMessenger;
		}
		mMessenger = new MessengerImpl();
		return mMessenger;
	}

	/*
	 * this is the function should be overwrite by sub-class.
	 */
	public abstract void handleMessage(Message msg);

	private final class MessengerImpl extends IMessenger.Stub {
		public void send(Message msg) {
			System.out.println("rthandler messageerImpl send");
			RTHandler.this.sendMessage(msg);
		}

		public void send(RTMessage rtmsg) throws RemoteException {
			Message msg = rtmsg.initMessage();
			RTHandler.this.sendMessage(msg);
		}
	}
}
