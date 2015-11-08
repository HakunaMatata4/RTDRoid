package android.content;

import java.util.Comparator;

import javax.realtime.RealtimeThread;

import android.os.Message;
import edu.buffalo.rtdroid.SystemConfig;
import edu.buffalo.rtdroid.experimentUtil.RealTimeHelper;
import edu.buffalo.rtdroid.util.ObjectPool;



public class IntentPool extends ObjectPool<Intent> {
    private static IntentPool instance = null;
    
    public static IntentPool instance(){
        validate();
        if( instance == null ){
            IntentComparator comparator = new IntentComparator();
            instance = new IntentPool(SystemConfig.INTENT_POOL_CAPACITY, comparator);
        }
        
        return instance;
    }
    
    private static void validate(){
        if( !(Thread.currentThread() instanceof RealtimeThread) ){
            throw new IllegalStateException();
        }
    }
    
	public IntentPool(int capacity, Comparator<Intent> comparator) {
		super(capacity, comparator);
	}

	@Override
	protected Intent createObject() {
		return new Intent();
	}
	
	@Override
    protected void setObjectContext(Intent t) {
        int priority = RealTimeHelper.getInstance().FijiFIFO2RTSJ(RealtimeThread.currentRealtimeThread().getPriority());
        t.priority = priority;
        t.sender = RealtimeThread.currentRealtimeThread().getMemoryArea().toString();
    }
	
	@Override
	protected Intent dropMessage() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static class IntentComparator implements Comparator<Intent>{
		
		public int compare(Intent o1, Intent o2) {
			return o1.priority - o2.priority;
		}
	}




}
