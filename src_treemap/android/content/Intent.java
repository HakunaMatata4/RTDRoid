
package android.content;

import java.nio.ByteBuffer;

public class Intent{
    public static int INTENT_DATA_TYPE_NONE = 0;
    public static int INTENT_DATA_TYPE_INT = 1;
    public static int INTENT_DATA_TYPE_DOUBLE = 2;
    public static int INTENT_DATA_TYPE_STRING = 3;
    public static int INTENR_DATA_TYPE_LONG = 4;
    public static int INTENT_DATA_TYPE_BYTES = 5;
    public static int INTENT_CAPACITY_BYTEBUFFER = 50;
    
    private String action;
    private ComponentName mComponent;
    private int type;
    private ByteBuffer byteBufferData;
    /*package*/ int priority;
    /*package*/ String sender;
    
    public Intent() {
    	this.priority = 0;
    	this.sender = "";
        this.action = "";
        this.type = INTENT_DATA_TYPE_NONE;
        this.byteBufferData = ByteBuffer.allocate(INTENT_CAPACITY_BYTEBUFFER);
        this.mComponent = new ComponentName();
    }
    
    public Intent(Intent intent) {
    	this.priority = intent.priority;
    	this.sender = intent.sender;
        this.action = intent.getAction();
        this.type = intent.getType();
        this.byteBufferData = ByteBuffer.allocate(INTENT_CAPACITY_BYTEBUFFER);
        this.mComponent = new ComponentName(intent.getComponentName());
    }

    public Intent(String action) {
        this.action = action;
    	this.priority = 0;
    	this.sender = "";
        this.type = INTENT_DATA_TYPE_NONE;
        this.byteBufferData = ByteBuffer.allocate(INTENT_CAPACITY_BYTEBUFFER);
    }

    public Intent(Context packageContext, Class<?> cls) {
        this.mComponent = new ComponentName(packageContext, cls);
    }
    
    /*getter and setter*/
    public void setAction(String action) {
        this.action=action;
    }

    public void setAction(Class<?> cls) {
        this.mComponent.setClassName(cls.getName());
        this.mComponent.setPackageName(cls.getName());
    }    
    
    public void setType(int type){
        this.type = type;
    }
    
    public String getAction() {
        return action;
    }

    public ComponentName getComponentName() {
        return mComponent;
    }

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}
	
	public int getType() {
        return type;
    }

    public int getIntData() {
    	this.byteBufferData.flip();
        return this.byteBufferData.asIntBuffer().get();
    }

    public double getDoubleData() {
    	this.byteBufferData.flip();
        return this.byteBufferData.asDoubleBuffer().get();
    }

    public long getLongData() {
    	this.byteBufferData.flip();
        return this.byteBufferData.asLongBuffer().get();
    }

    public String getStringData() {
    	this.byteBufferData.flip();
        return new String(this.byteBufferData.array());
    }

    public byte[] getByteArray() {
    	this.byteBufferData.flip();
        return byteBufferData.array();
    }
    public void setData(int data){
        this.byteBufferData.putInt(data);
    }
    public void setData(double data){
        this.byteBufferData.putDouble(data);
    }
    public void setData(long data){
        this.byteBufferData.putLong(data);
    }
    public void setData(String data){
        this.byteBufferData.put(data.getBytes());
    }
    public void setData(byte[] data){
        if( data.length > byteBufferData.capacity() ){
            byteBufferData.put(data);
        }
    }
}
