
package android.os;

import javax.realtime.ImmortalMemory;
import javax.realtime.RealtimeThread;
import javax.realtime.ScopedMemory;

import android.app.ScopedServiceDelegator;
import android.content.ScopedIntentResolver;

/*
 * In Android, base/core/java/android/os/IMessenger.aidl define IMessenger
 * and automatically generate its Stub class
 */

/** @hide */
public interface IMessenger extends IInterface {
    /** Local-side IPC implementation stub class. */
    public static abstract class Stub extends Binder implements IMessenger {
        private static final java.lang.String DESCRIPTOR = "android.os.IMessenger";

        public Stub() {
            this.attachInterface(this, DESCRIPTOR);
        }

        /**
         * Cast an IBinder object into an IMessenger interface,
         * generating a proxy if needed.
         */
        public static IMessenger asInterface(IBinder obj) {
            if ((obj == null)) {
                return null;
            }

            IInterface iin = (android.os.IInterface) obj.queryLocalInterface(DESCRIPTOR);
            if (((iin != null) && (iin instanceof android.os.IMessenger))) {
                return ((android.os.IMessenger) iin);
            }
            
            System.out.println("!!!!!!!!!!!!!!!1we should never see this msg!!!!!!!!!!!!");
            return new IMessenger.Stub.Proxy(obj);
        }

        /*
         * In the case of Messenger, its associated Handler/RTHandler's
         * IMessenger Implementation
         */
        public IBinder asBinder() {
            return this;
        }

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags)
                throws RemoteException {
            return false;
        }

        private static class Proxy implements IMessenger {
            private IBinder mRemote;
            private String mName;

            Proxy(IBinder remote) {
                mRemote = remote;
            }

            Proxy(IBinder obj, String name) {
                mRemote = obj;
                mName = name;
            }

            public IBinder asBinder() {
                return mRemote;
            }

            public void send(final Message msg) throws android.os.RemoteException {

            }
            
            public void send(RTMessage msg) throws RemoteException {
			}
        }

        static final int TRANSACTION_send = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    } // end of stub class

    public void send(Message msg) throws android.os.RemoteException;
    public void send(RTMessage msg) throws android.os.RemoteException;
}
