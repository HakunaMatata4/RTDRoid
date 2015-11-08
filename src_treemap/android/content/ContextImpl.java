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

package android.content;

import java.util.Set;

import javax.realtime.ImmortalMemory;
import javax.realtime.RealtimeThread;
import javax.realtime.ScopedMemory;

import android.app.ScopedActivityDelegator;
import android.app.ScopedServiceDelegator;
import android.app.ServiceDelegator;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.os.Handler;
import android.os.IMessenger;
import android.realtime.ScopedMessengerMediator;
import android.support.v4.content.LocalBroadcastManager;
import edu.buffalo.rtdroid.SystemConfig;

// class ReceiverRestrictedContext extends ContextWrapper {
// ReceiverRestrictedContext(Context base) {
// super(base);
// }
//
// @Override
// public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter
// filter) {
// return registerReceiver(receiver, filter, null, null);
// }
//
// @Override
// public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter
// filter,
// String broadcastPermission, Handler scheduler) {
// throw new ReceiverCallNotAllowedException(
// "IntentReceiver components are not allowed to register to receive intents");
// //ex.fillInStackTrace();
// //Log.e("IntentReceiver", ex.getMessage(), ex);
// //return mContext.registerReceiver(receiver, filter, broadcastPermission,
// // scheduler);
// }

// @Override
// public boolean bindService(Intent service, ServiceConnection conn, int flags)
// {
// throw new ReceiverCallNotAllowedException(
// "IntentReceiver components are not allowed to bind to services");
// //ex.fillInStackTrace();
// //Log.e("IntentReceiver", ex.getMessage(), ex);
// //return mContext.bindService(service, interfaceName, conn, flags);
// }
// }

/**
 * Common implementation of Context API, which provides the base context object
 * for Activity and other application components.
 */
public class ContextImpl extends Context {
    private static long sInstanceCount = 0;
    private ComponentName name;
    private IntentResolver intentReslover;

    public ContextImpl(String _name) {
        this.name = new ComponentName(_name, _name);
        this.intentReslover = IntentResolver.getInstance();
    }

    public static long getInstanceCount() {
        return sInstanceCount;
    }

    @Override
    public String getPackageName() {
        return name.getPackageName();
    }

    @Override
    public void startActivity(Intent intent) {
        try {
            throw new Exception("unsupported function...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startIntentSender(IntentSender intent, Intent fillInIntent,
            int flagsMask, int flagsValues, int extraFlags)
            throws SendIntentException {
        try {
            throw new Exception("unsupported function...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendBroadcast(Intent intent) {
    	//TODO
    }

    @Override
    public void sendOrderedBroadcast(Intent intent, String receiverPermission,
            BroadcastReceiver resultReceiver, Handler scheduler,
            int initialCode, String initialData, Bundle initialExtras) {
        try {
            throw new Exception("unsupported function...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendStickyBroadcast(Intent intent) {
        try {
            throw new Exception("unsupported function...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendStickyOrderedBroadcast(Intent intent,
            BroadcastReceiver resultReceiver, Handler scheduler,
            int initialCode, String initialData, Bundle initialExtras) {
        try {
            throw new Exception("unsupported function...");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void removeStickyBroadcast(Intent intent) {
        try {
            throw new Exception("unsupported function...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public Intent registerReceiver(BroadcastReceiver receiver,
            IntentFilter filter) {
    	try {
            throw new Exception("unsupported function...");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Intent registerReceiver(BroadcastReceiver receiver,
            IntentFilter filter, String broadcastPermission, Handler scheduler) {
        try {
            throw new Exception("unsupported function...");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void unregisterReceiver(BroadcastReceiver receiver) {
    	try {
            throw new Exception("unsupported function...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public ComponentName startService(Intent intent) {
         System.out.println("ContexImpl: start service "
         + intent.getComponentName().getClassName());
         String name = intent.getComponentName().getClassName();
         ServiceDelegator delegator = intentReslover.getServiceDelegator(name);
         delegator.setIntent(intent);
         delegator.onStartCommand(intent, 0, 0);
        return intent.getComponentName();
    }

    @Override
    public boolean stopService(Intent intent) {
    	System.out.println("ContexImpl: stop service "
    	         + intent.getComponentName().getClassName());
    	         String name = intent.getComponentName().getClassName();
    	         ServiceDelegator delegator = intentReslover.getServiceDelegator(name);
    	         delegator.onDestroy(intent);
        return true;
    }
    
    public boolean stopService(Intent intent, String name) {
    	System.out.println("ContexImpl: stop service "
    	         + intent.getComponentName().getClassName());
    	         ServiceDelegator delegator = intentReslover.getServiceDelegator(name);
    	         delegator.onDestroy(null);
        return true;
    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection conn, int flags) {
    	System.out.println("ContexImpl: bind service "
                + intent.getComponentName().getClassName());

        System.out.println("ContexImpl: start service "
                + intent.getComponentName().getClassName());
        
        ServiceDelegator delegator = intentReslover.getServiceDelegator(intent.getComponentName().getClassName());
        String bindingComponent = name.getClassName();
        intentReslover.addBindingRecord(intent.getComponentName().getClassName(), bindingComponent);
        conn.onServiceConnected(intent.getComponentName(), delegator.onBind(intent));
        return true;
    }

    @Override
    public void unbindService(ServiceConnection conn) {
    	String bindingComponent = name.getClassName();
        Set<String> set = intentReslover.removeBindingRecord(bindingComponent);
        for (String item : set) {
            int count = intentReslover.getBindingRecord(item);
            if (count == 1) {
                intentReslover.removeBindingRecord(item);
                stopService(null, item);
            } else {
                intentReslover.updateBindingRecord(item, count - 1);
            }
        }

    }

    @Override
    public Object getSystemService(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Context createPackageContext(String packageName, int flags) {
        return this;
    }
    
    public Context getApplicationContext() {
        return SystemConfig.getApplicationContext();
    }
}
