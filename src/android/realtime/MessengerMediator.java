
package android.realtime;

import javax.realtime.ImmortalMemory;
import javax.realtime.RealtimeThread;
import javax.realtime.ScopedMemory;

import android.app.ScopedServiceDelegator;
import android.content.ScopedIntentResolver;
import android.os.Binder;
import android.os.IMessenger;
import android.os.Message;
import android.os.RTMessage;
import android.os.RemoteException;

public class MessengerMediator extends IMessenger.Stub {
    private String targetName;

    public MessengerMediator(String name) {
        this.targetName = name;
    }

    public void send(final Message msg) throws RemoteException {
        ImmortalMemory.instance().executeInArea(new Runnable() {

            public void run() {

                try {
                    System.out.println("IMessenger.Stub.proxy send ..."
                            +
                            targetName);
                    ScopedMemory ma =
                            ScopedIntentResolver.getInstance().getServiceScope(targetName);
                    final Message m = copyMessage(msg);
                    ma.enter(new Runnable() {

                        public void run() {
                            ScopedServiceDelegator delegator = (ScopedServiceDelegator)
                                    ((ScopedMemory) RealtimeThread.
                                            getCurrentMemoryArea()).getPortal();
                            try {
                                delegator.getMessenger().send(m);
                            } catch (RemoteException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void send(final RTMessage rtmsg) throws RemoteException {
        ImmortalMemory.instance().executeInArea(new Runnable() {

            public void run() {

                try {
                    System.out.println("IMessenger.Stub.proxy send ..."
                            +
                            targetName);
                    ScopedMemory ma =
                            ScopedIntentResolver.getInstance().getServiceScope(targetName);
                    ma.enter(new Runnable() {

                        public void run() {
                            ScopedServiceDelegator delegator = (ScopedServiceDelegator)
                                    ((ScopedMemory) RealtimeThread.
                                            getCurrentMemoryArea()).getPortal();
                            try {
                            	Message msg = rtmsg.initMessage();
                                delegator.getMessenger().send(msg);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
	}
    
    /*
     * we will revisit it after discussion the policy
     */
    private Message copyMessage(Message msg) {
        Message newMsg = new Message();
        msg.what = msg.what;
        return newMsg;
    }

	
}
