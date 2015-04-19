
package android.os;

import java.util.Comparator;

import javax.realtime.RealtimeThread;

import edu.buffalo.rtdroid.SystemConfig;
import edu.buffalo.rtdroid.experimentUtil.RealTimeHelper;
import edu.buffalo.rtdroid.util.ObjectPool;

public class MessagePool extends ObjectPool<Message> {
	private static MessagePool instance = null;
	
	public static MessagePool instance(){
		if( instance == null ){
			System.out.println("how could I get wrong here???");
			instance = new MessagePool(SystemConfig.MESSAGE_DEFAULT_SIZE, new MessageComparator());
			System.out.println("how could I get wrong here???");
		}
		
		return instance;
	}
	
    private void validate() {
        if (!(Thread.currentThread() instanceof RealtimeThread)) {
            throw new IllegalStateException();
        }
    }

    public MessagePool(int capacity, Comparator<Message> comparator) {
        super(capacity, comparator);
    }
    
    @Override
    protected Message createObject() {
        return new Message();
    }

    @Override
    protected void setObjectContext(Message msg) {
    	int p = RealTimeHelper.getInstance().FijiFIFO2RTSJ(Thread.currentThread().getPriority());
    	msg.priority = p;
    }

	@Override
	protected Message dropMessage() {
		System.out.println("Message Dropped");
		Message msg = this.inPorgressQueue.peek();
		Message droppedMessage = msg.msgQueue.dropNextMessage();
		inPorgressQueue.remove(droppedMessage);
		droppedMessage.recycle();
		return droppedMessage;
	}

    public static class MessageComparator implements Comparator<Message> {

        public int compare(Message o1, Message o2) {
            return o1.priority - o2.priority;
        }
    }
    
    public boolean hasAvailable(){
		return getRemainingCapacity() < getTotalCapacity();
    }
}
