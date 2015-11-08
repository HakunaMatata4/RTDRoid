/*
 * Copyright (C) 2011 The Android Open Source Project
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

package android.support.v4.content;

import java.util.Set;

import javax.realtime.ImmortalMemory;
import javax.realtime.LTMemory;
import javax.realtime.RealtimeThread;
import javax.realtime.ScopedMemory;

import android.app.ScopedServiceDelegator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ScopedIntentResolver;

/**
 * Helper to register for and send broadcasts of Intents to local objects
 * within your process. This is has a number of advantages over sending
 * global broadcasts with {@link android.content.Context#sendBroadcast}:
 * <ul>
 * <li>You know that the data you are broadcasting won't leave your app, so
 * don't need to worry about leaking private data.
 * <li>It is not possible for other applications to send these broadcasts to
 * your app, so you don't need to worry about having security holes they can
 * exploit.
 * <li>It is more efficient than sending a global broadcast through the system.
 * </ul>
 */
public class LocalBroadcastManager {
    private static final String TAG = "LocalBroadcastManager";
    private static final boolean DEBUG = false;

    private final Context mAppContext;

    static final int MSG_EXEC_PENDING_BROADCASTS = 1;
    private static final Object mLock = new Object();
    private static LocalBroadcastManager mInstance;

    public static LocalBroadcastManager getInstance(Context context) {
        synchronized (mLock) {
            if (mInstance == null) {
                mInstance = new LocalBroadcastManager(context);
            }
            return mInstance;
        }
    }

    private LocalBroadcastManager(Context context) {
        mAppContext = context;
    }

    /**
     * Register a receive for any local broadcasts that match the given
     * IntentFilter.
     * 
     * @param receiver The BroadcastReceiver to handle the broadcast.
     * @param filter Selects the Intent broadcasts to be received.
     * @see #unregisterReceiver
     */
    public void registerReceiver(final BroadcastReceiver receiver,
            IntentFilter filter) {
        final LTMemory area = (LTMemory) RealtimeThread.getCurrentMemoryArea();
        ScopedServiceDelegator delegator = (ScopedServiceDelegator) area.getPortal();
        delegator.addReceiver(receiver.getClass().getName(), receiver);
        // map receiver's name to memory area
        ImmortalMemory.instance().executeInArea(new Runnable() {

            public void run() {
                String name = new String(receiver.getClass().getName());
                ScopedIntentResolver.getInstance().addToReceiverList(name, area);
            }
        });
    }

    /**
     * Unregister a previously registered BroadcastReceiver. <em>All</em>
     * filters that have been registered for this BroadcastReceiver will be
     * removed.
     * 
     * @param receiver The BroadcastReceiver to unregister.
     * @see #registerReceiver
     */
    public void unregisterReceiver(final BroadcastReceiver receiver) {
        LTMemory area = (LTMemory) RealtimeThread.getCurrentMemoryArea();
        ScopedServiceDelegator delegator = (ScopedServiceDelegator) area.getPortal();
        delegator.removeReceiver(receiver.getClass().getName(), receiver);
        // remove mapping from receiver's name to memory area
        ImmortalMemory.instance().enter(new Runnable() {

            public void run() {
                String name = new String(receiver.getClass().getName());
                ScopedIntentResolver.getInstance().removeFromReceiverList(name);
            }
        });
    }

    /**
     * Broadcast the given intent to all interested BroadcastReceivers. This
     * call is asynchronous; it returns immediately, and you will continue
     * executing while the receivers are run.
     * 
     * @param intent The Intent to broadcast; all receivers matching this
     *            Intent will receive the broadcast.
     * @see #registerReceiver
     */
    public boolean sendBroadcast(final Intent intent) {
        ImmortalMemory.instance().executeInArea(new Runnable() {

            public void run() {
                try {
                    String name = new String(intent.getComponentName().getClassName());
                    final LTMemory area = ScopedIntentResolver.getInstance().getReceiverByName(name);

                    area.enter(new Runnable() {

                        public void run() {
                            try {
                                ScopedServiceDelegator delegator = (ScopedServiceDelegator) area.getPortal();
                                delegator.onReceive(mAppContext, intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return false;
    }

    /**
     * Like {@link #sendBroadcast(Intent)}, but if there are any receivers for
     * the Intent this function will block and immediately dispatch them before
     * returning.
     */
    public void sendBroadcastSync(Intent intent) {
        try {
            throw new Exception("unsupported function...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void executePendingBroadcasts() {
        try {
            throw new Exception("unsupported function...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
