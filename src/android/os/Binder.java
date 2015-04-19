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

import java.io.FileDescriptor;
import java.io.PrintWriter;

/**
 * Base class for a remotable object, the core part of a lightweight remote
 * procedure call mechanism defined by {@link IBinder}. This class is an
 * implementation of IBinder that provides the standard support creating a local
 * implementation of such an object.
 * <p>
 * Most developers will not implement this class directly, instead using the <a
 * href="{@docRoot}guide/developing/tools/aidl.html">aidl</a> tool to describe
 * the desired interface, having it generate the appropriate Binder subclass.
 * You can, however, derive directly from Binder to implement your own custom
 * RPC protocol or simply instantiate a raw Binder object directly to use as a
 * token that can be shared across processes.
 * 
 * @see IBinder
 */
public class Binder implements IBinder {
    private static final String TAG = "Binder";

    private int mObject;
    // owner component
    private IInterface mOwner;
    // class name of owner component
    private String mDescriptor;

    /**
     * Default constructor initializes the object.
     */
    public Binder() {
    }

    /**
     * Convenience method for associating a specific interface with the
     * Binder.
     * After calling, queryLocalInterface() will be implemented for you
     * to return the given owner IInterface when the corresponding
     * descriptor is requested.
     */
    public void attachInterface(IInterface owner, String descriptor) {
        mOwner = owner;
        mDescriptor = descriptor;
    }

    /**
     * Default implementation returns an empty interface name.
     */
    public String getInterfaceDescriptor() {
        return mDescriptor;
    }

    /**
     * Default implementation always returns true -- if you got here,
     * the object is alive.
     */
    public boolean pingBinder() {
        return true;
    }

    /**
     * {@inheritDoc} Note that if you're calling on a local binder, this always
     * returns true
     * because your process is alive if you're calling it.
     */
    public boolean isBinderAlive() {
        return true;
    }

    /**
     * Use information supplied to attachInterface() to return the
     * associated IInterface if it matches the requested
     * descriptor.
     */
    public IInterface queryLocalInterface(String descriptor) {
        if (mDescriptor.equals(descriptor)) {
            return mOwner;
        }
        return null;
    }

    /**
     * Default implementation is a stub that returns false. You will want
     * to override this to do the appropriate unmarshalling of transactions.
     * <p>
     * If you want to call this, call transact().
     */
    protected boolean onTransact(int code, Parcel data, Parcel reply, int flags)
            throws RemoteException {

        return false;
    }

    /**
     * Implemented to call the more convenient version
     * {@link #dump(FileDescriptor, PrintWriter, String[])}.
     */
    public void dump(FileDescriptor fd, String[] args) {
        System.out.println("Owner:" + mOwner + ", class path:" + mDescriptor);
    }

    /**
     * Print the object's state into the given stream.
     * 
     * @param fd The raw file descriptor that the dump is being sent to.
     * @param fout The file to which you should dump your state. This will be
     *            closed for you after you return.
     * @param args additional arguments to the dump request.
     */
    protected void dump(FileDescriptor fd, PrintWriter fout, String[] args) {
    }

    /**
     * Default implementation rewinds the parcels and calls onTransact. On
     * the remote side, transact calls into the binder to do the IPC.
     */
    public final boolean transact(int code, Parcel data, Parcel reply,
            int flags) throws RemoteException {
        
        return false;
    }

    /**
     * Local implementation is a no-op.
     */
    public void linkToDeath(DeathRecipient recipient, int flags) {
    }

    /**
     * Local implementation is a no-op.
     */
    public boolean unlinkToDeath(DeathRecipient recipient, int flags) {
        return true;
    }

    protected void finalize() throws Throwable {
    }

    public boolean execTransact(int code, int dataObj, int replyObj, int flags) {
        return false;
    }
}
