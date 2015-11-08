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

import android.os.Bundle;
import android.os.Handler;

/**
 * Interface to global information about an application environment. This is an
 * abstract class whose implementation is provided by the Android system. It
 * allows access to application-specific resources and classes, as well as
 * up-calls for application-level operations such as launching activities,
 * broadcasting and receiving intents, etc.
 */
public abstract class Context {
    /**
     * File creation mode: the default mode, where the created file can only be
     * accessed by the calling application (or all applications sharing the same
     * user ID).
     * 
     * @see #MODE_WORLD_READABLE
     * @see #MODE_WORLD_WRITEABLE
     */
    public static final int MODE_PRIVATE = 0x0000;
    /**
     * File creation mode: allow all other applications to have read access to
     * the created file.
     * 
     * @see #MODE_PRIVATE
     * @see #MODE_WORLD_WRITEABLE
     */
    public static final int MODE_WORLD_READABLE = 0x0001;
    /**
     * File creation mode: allow all other applications to have write access to
     * the created file.
     * 
     * @see #MODE_PRIVATE
     * @see #MODE_WORLD_READABLE
     */
    public static final int MODE_WORLD_WRITEABLE = 0x0002;
    /**
     * File creation mode: for use with {@link #openFileOutput}, if the file
     * already exists then write data to the end of the existing file instead of
     * erasing it.
     * 
     * @see #openFileOutput
     */
    public static final int MODE_APPEND = 0x8000;

    /**
     * Flag for {@link #bindService}: automatically create the service as long
     * as the binding exists. Note that while this will create the service, its
     * {@link android.app.Service#onStart} method will still only be called due
     * to an explicit call to {@link #startService}. Even without that, though,
     * this still provides you with access to the service object while the
     * service is created.
     * <p>
     * Specifying this flag also tells the system to treat the service as being
     * as important as your own process -- that is, when deciding which process
     * should be killed to free memory, the service will only be considered a
     * candidate as long as the processes of any such bindings is also a
     * candidate to be killed. This is to avoid situations where the service is
     * being continually created and killed due to low memory.
     */
    public static final int BIND_AUTO_CREATE = 0x0001;

    /**
     * Flag for {@link #bindService}: include debugging help for mismatched
     * calls to unbind. When this flag is set, the callstack of the following
     * {@link #unbindService} call is retained, to be printed if a later
     * incorrect unbind call is made. Note that doing this requires retaining
     * information about the binding that was made for the lifetime of the app,
     * resulting in a leak -- this should only be used for debugging.
     */
    public static final int BIND_DEBUG_UNBIND = 0x0002;

    /**
     * Flag for {@link #bindService}: don't allow this binding to raise the
     * target service's process to the foreground scheduling priority. It will
     * still be raised to the at least the same memory priority as the client
     * (so that its process will not be killable in any situation where the
     * client is not killable), but for CPU scheduling purposes it may be left
     * in the background. This only has an impact in the situation where the
     * binding client is a foreground process and the target service is in a
     * background process.
     */
    public static final int BIND_NOT_FOREGROUND = 0x0004;

    /** Return an AssetManager instance for your application's package. */
    // TODO
    // public abstract AssetManager getAssets();

    /** Return a Resources instance for your application's package. */
    // TODO
    // public abstract Resources getResources();

    /** Return PackageManager instance to find global package information. */
    // TODO
    // public abstract PackageManager getPackageManager();

    /** Return a ContentResolver instance for your application's package. */
    // TODO
    // public abstract ContentResolver getContentResolver();

    /**
     * Return the Looper for the main thread of the current process. This is the
     * thread used to dispatch calls to application components (activities,
     * services, etc).
     */
    // TODO
    // public abstract Looper getMainLooper();

    /**
     * Return the context of the single, global Application object of the
     * current process. This generally should only be used if you need a Context
     * whose lifecycle is separate from the current context, that is tied to the
     * lifetime of the process rather than the current component.
     * <p>
     * Consider for example how this interacts with @ *
     * #registerReceiver(BroadcastReceiver, IntentFilter)}:
     * <ul>
     * <li>
     * <p>
     * If used from an Activity context, the receiver is being registered within
     * that activity. This means that you are expected to unregister before the
     * activity is done being destroyed; in fact if you do not do so, the
     * framework will clean up your leaked registration as it removes the
     * activity and log an error. Thus, if you use the Activity context to
     * register a receiver that is static (global to the process, not associated
     * with an Activity instance) then that registration will be removed on you
     * at whatever point the activity you used is destroyed.
     * <li>
     * <p>
     * If used from the Context returned here, the receiver is being registered
     * with the global state associated with your application. Thus it will
     * never be unregistered for you. This is necessary if the receiver is
     * associated with static data, not a particular component. However using
     * the ApplicationContext elsewhere can easily lead to serious leaks if you
     * forget to unregister, unbind, etc.
     * </ul>
     * public abstract Context getApplicationContext(); /** Return a localized,
     * styled CharSequence from the application's package's default string
     * table.
     * 
     * @param resId Resource id for the CharSequence text
     */
    public abstract Context getApplicationContext();
    // TODO
    // public final CharSequence getText(int resId) {
    // return getResources().getText(resId);
    // }

    /**
     * Return a localized string from the application's package's default string
     * table.
     * 
     * @param resId Resource id for the string
     */
    // TODO
    // public final String getString(int resId) {
    // return getResources().getString(resId);
    // }
    /**
     * Return a localized formatted string from the application's package's
     * default string table, substituting the format arguments as defined in
     * {@link java.util.Formatter} and {@link java.lang.String#format}.
     * 
     * @param resId Resource id for the format string
     * @param formatArgs The format arguments that will be used for
     *            substitution.
     */
    // TODO
    // public final String getString(int resId, Object... formatArgs) {
    // return getResources().getString(resId, formatArgs);
    // }
    //public abstract Context getApplicationContext();
    /**
     * Return a class loader you can use to retrieve classes in this package.
     */
    // TODO
    // public abstract ClassLoader getClassLoader();

    /** Return the name of this application's package. */
    public abstract String getPackageName();

    /** Return the full application info for this context's package. */
    // TODO
    // public abstract ApplicationInfo getApplicationInfo();

    /**
     * Launch a new activity. You will not receive any information about when
     * the activity exits.
     * <p>
     * Note that if this method is being called from outside of an
     * {@link android.app.Activity} Context, then the Intent must include the
     * {@link Intent#FLAG_ACTIVITY_NEW_TASK} launch flag. This is because,
     * without being started from an existing Activity, there is no existing
     * task in which to place the new activity and thus it needs to be placed in
     * its own separate task.
     * <p>
     * This method throws {@link ActivityNotFoundException} if there was no
     * Activity found to run the given Intent.
     * 
     * @param intent The description of the activity to start.
     * @throws ActivityNotFoundException
     * @see PackageManager#resolveActivity
     */
    public abstract void startActivity(Intent intent);

    /**
     * Like {@link #startActivity(Intent)}, but taking a IntentSender to start.
     * If the IntentSender is for an activity, that activity will be started as
     * if you had called the regular {@link #startActivity(Intent)} here;
     * otherwise, its associated action will be executed (such as sending a
     * broadcast) as if you had called {@link IntentSender#sendIntent
     * IntentSender.sendIntent} on it.
     * 
     * @param intent The IntentSender to launch.
     * @param fillInIntent If non-null, this will be provided as the intent
     *            parameter to {@link IntentSender#sendIntent}.
     * @param flagsMask Intent flags in the original IntentSender that you would
     *            like to change.
     * @param flagsValues Desired values for any bits set in
     *            <var>flagsMask</var>
     * @param extraFlags Always set to 0.
     */
    public abstract void startIntentSender(IntentSender intent,
            Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags)
            throws IntentSender.SendIntentException;

    /**
     * Broadcast the given intent to all interested BroadcastReceivers. This
     * call is asynchronous; it returns immediately, and you will continue
     * executing while the receivers are run. No results are propagated from
     * receivers and receivers can not abort the broadcast. If you want to allow
     * receivers to propagate results or abort the broadcast, you must send an
     * ordered broadcast using {@link #sendOrderedBroadcast(Intent, String)}.
     * <p>
     * See {@link BroadcastReceiver} for more information on Intent broadcasts.
     * 
     * @param intent The Intent to broadcast; all receivers matching this Intent
     *            will receive the broadcast.
     * @see android.content.BroadcastReceiver
     * @see #registerReceiver
     * @see #sendBroadcast(Intent, String)
     * @see #sendOrderedBroadcast(Intent, String)
     * @see #sendOrderedBroadcast(Intent, String, BroadcastReceiver, Handler,
     *      int, String, Bundle)
     */
    public abstract void sendBroadcast(Intent intent);

    /**
     * Version of {@link #sendBroadcast(Intent)} that allows you to receive data
     * back from the broadcast. This is accomplished by supplying your own
     * BroadcastReceiver when calling, which will be treated as a final receiver
     * at the end of the broadcast -- its {@link BroadcastReceiver#onReceive}
     * method will be called with the result values collected from the other
     * receivers. The broadcast will be serialized in the same way as calling
     * {@link #sendOrderedBroadcast(Intent, String)}.
     * <p>
     * Like {@link #sendBroadcast(Intent)}, this method is asynchronous; it will
     * return before resultReceiver.onReceive() is called.
     * <p>
     * See {@link BroadcastReceiver} for more information on Intent broadcasts.
     * 
     * @param intent The Intent to broadcast; all receivers matching this Intent
     *            will receive the broadcast.
     * @param receiverPermission String naming a permissions that a receiver
     *            must hold in order to receive your broadcast. If null, no
     *            permission is required.
     * @param resultReceiver Your own BroadcastReceiver to treat as the final
     *            receiver of the broadcast.
     * @param scheduler A custom Handler with which to schedule the
     *            resultReceiver callback; if null it will be scheduled in the
     *            Context's main thread.
     * @param initialCode An initial value for the result code. Often
     *            Activity.RESULT_OK.
     * @param initialData An initial value for the result data. Often null.
     * @param initialExtras An initial value for the result extras. Often null.
     * @see #sendBroadcast(Intent)
     * @see #sendBroadcast(Intent, String)
     * @see #sendOrderedBroadcast(Intent, String)
     * @see #sendStickyBroadcast(Intent)
     * @see #sendStickyOrderedBroadcast(Intent, BroadcastReceiver, Handler, int,
     *      String, Bundle)
     * @see android.content.BroadcastReceiver
     * @see #registerReceiver
     * @see android.app.Activity#RESULT_OK
     */
    public abstract void sendOrderedBroadcast(Intent intent,
            String receiverPermission, BroadcastReceiver resultReceiver,
            Handler scheduler, int initialCode, String initialData,
            Bundle initialExtras);

    /**
     * Perform a {@link #sendBroadcast(Intent)} that is "sticky," meaning the
     * Intent you are sending stays around after the broadcast is complete, so
     * that others can quickly retrieve that data through the return value of
     * {@link #registerReceiver(BroadcastReceiver, IntentFilter)}. In all other
     * ways, this behaves the same as {@link #sendBroadcast(Intent)}.
     * <p>
     * You must hold the {@link android.Manifest.permission#BROADCAST_STICKY}
     * permission in order to use this API. If you do not hold that permission,
     * {@link SecurityException} will be thrown.
     * 
     * @param intent The Intent to broadcast; all receivers matching this Intent
     *            will receive the broadcast, and the Intent will be held to be
     *            re-broadcast to future receivers.
     * @see #sendBroadcast(Intent)
     * @see #sendStickyOrderedBroadcast(Intent, BroadcastReceiver, Handler, int,
     *      String, Bundle)
     */
    public abstract void sendStickyBroadcast(Intent intent);

    /**
     * Version of {@link #sendStickyBroadcast} that allows you to receive data
     * back from the broadcast. This is accomplished by supplying your own
     * BroadcastReceiver when calling, which will be treated as a final receiver
     * at the end of the broadcast -- its {@link BroadcastReceiver#onReceive}
     * method will be called with the result values collected from the other
     * receivers. The broadcast will be serialized in the same way as calling
     * {@link #sendOrderedBroadcast(Intent, String)}.
     * <p>
     * Like {@link #sendBroadcast(Intent)}, this method is asynchronous; it will
     * return before resultReceiver.onReceive() is called. Note that the sticky
     * data stored is only the data you initially supply to the broadcast, not
     * the result of any changes made by the receivers.
     * <p>
     * See {@link BroadcastReceiver} for more information on Intent broadcasts.
     * 
     * @param intent The Intent to broadcast; all receivers matching this Intent
     *            will receive the broadcast.
     * @param resultReceiver Your own BroadcastReceiver to treat as the final
     *            receiver of the broadcast.
     * @param scheduler A custom Handler with which to schedule the
     *            resultReceiver callback; if null it will be scheduled in the
     *            Context's main thread.
     * @param initialCode An initial value for the result code. Often
     *            Activity.RESULT_OK.
     * @param initialData An initial value for the result data. Often null.
     * @param initialExtras An initial value for the result extras. Often null.
     * @see #sendBroadcast(Intent)
     * @see #sendBroadcast(Intent, String)
     * @see #sendOrderedBroadcast(Intent, String)
     * @see #sendStickyBroadcast(Intent)
     * @see android.content.BroadcastReceiver
     * @see #registerReceiver
     * @see android.app.Activity#RESULT_OK
     */
    public abstract void sendStickyOrderedBroadcast(Intent intent,
            BroadcastReceiver resultReceiver, Handler scheduler,
            int initialCode, String initialData, Bundle initialExtras);

    /**
     * Remove the data previously sent with {@link #sendStickyBroadcast}, so
     * that it is as if the sticky broadcast had never happened.
     * <p>
     * You must hold the {@link android.Manifest.permission#BROADCAST_STICKY}
     * permission in order to use this API. If you do not hold that permission,
     * {@link SecurityException} will be thrown.
     * 
     * @param intent The Intent that was previously broadcast.
     * @see #sendStickyBroadcast
     */
    public abstract void removeStickyBroadcast(Intent intent);

    /**
     * Register a BroadcastReceiver to be run in the main activity thread. The
     * <var>receiver</var> will be called with any broadcast Intent that matches
     * <var>filter</var>, in the main application thread.
     * <p>
     * The system may broadcast Intents that are "sticky" -- these stay around
     * after the broadcast as finished, to be sent to any later registrations.
     * If your IntentFilter matches one of these sticky Intents, that Intent
     * will be returned by this function <strong>and</strong> sent to your
     * <var>receiver</var> as if it had just been broadcast.
     * <p>
     * There may be multiple sticky Intents that match <var>filter</var>, in
     * which case each of these will be sent to <var>receiver</var>. In this
     * case, only one of these can be returned directly by the function; which
     * of these that is returned is arbitrarily decided by the system.
     * <p>
     * If you know the Intent your are registering for is sticky, you can supply
     * null for your <var>receiver</var>. In this case, no receiver is
     * registered -- the function simply returns the sticky Intent that matches
     * <var>filter</var>. In the case of multiple matches, the same rules as
     * described above apply.
     * <p>
     * See {@link BroadcastReceiver} for more information on Intent broadcasts.
     * <p class="note">
     * Note: this method <em>cannot be called from a
     * {@link BroadcastReceiver} component;</em> that is, from a
     * BroadcastReceiver that is declared in an application's manifest. It is
     * okay, however, to call this method from another BroadcastReceiver that
     * has itself been registered at run time with {@link #registerReceiver},
     * since the lifetime of such a registered BroadcastReceiver is tied to the
     * object that registered it.
     * </p>
     * 
     * @param receiver The BroadcastReceiver to handle the broadcast.
     * @param filter Selects the Intent broadcasts to be received.
     * @return The first sticky intent found that matches <var>filter</var>, or
     *         null if there are none.
     * @see #registerReceiver(BroadcastReceiver, IntentFilter, String, Handler)
     * @see #sendBroadcast
     * @see #unregisterReceiver
     */
    public abstract Intent registerReceiver(BroadcastReceiver receiver,
            IntentFilter filter);

    /**
     * Register to receive intent broadcasts, to run in the context of
     * <var>scheduler</var>. See
     * {@link #registerReceiver(BroadcastReceiver, IntentFilter)} for more
     * information. This allows you to enforce permissions on who can broadcast
     * intents to your receiver, or have the receiver run in a different thread
     * than the main application thread.
     * <p>
     * See {@link BroadcastReceiver} for more information on Intent broadcasts.
     * 
     * @param receiver The BroadcastReceiver to handle the broadcast.
     * @param filter Selects the Intent broadcasts to be received.
     * @param broadcastPermission String naming a permissions that a broadcaster
     *            must hold in order to send an Intent to you. If null, no
     *            permission is required.
     * @param scheduler Handler identifying the thread that will receive the
     *            Intent. If null, the main thread of the process will be used.
     * @return The first sticky intent found that matches <var>filter</var>, or
     *         null if there are none.
     * @see #registerReceiver(BroadcastReceiver, IntentFilter)
     * @see #sendBroadcast
     * @see #unregisterReceiver
     */
    public abstract Intent registerReceiver(BroadcastReceiver receiver,
            IntentFilter filter, String broadcastPermission, Handler scheduler);

    /**
     * Unregister a previously registered BroadcastReceiver. <em>All</em>
     * filters that have been registered for this BroadcastReceiver will be
     * removed.
     * 
     * @param receiver The BroadcastReceiver to unregister.
     * @see #registerReceiver
     */
    public abstract void unregisterReceiver(BroadcastReceiver receiver);

    /**
     * Request that a given application service be started. The Intent can
     * either contain the complete class name of a specific service
     * implementation to start, or an abstract definition through the action and
     * other fields of the kind of service to start. If this service is not
     * already running, it will be instantiated and started (creating a process
     * for it if needed); if it is running then it remains running.
     * <p>
     * Every call to this method will result in a corresponding call to the
     * target service's {@link android.app.Service#onStart} method, with the
     * <var>intent</var> given here. This provides a convenient way to submit
     * jobs to a service without having to bind and call on to its interface.
     * <p>
     * Using startService() overrides the default service lifetime that is
     * managed by {@link #bindService}: it requires the service to remain
     * running until {@link #stopService} is called, regardless of whether any
     * clients are connected to it. Note that calls to startService() are not
     * nesting: no matter how many times you call startService(), a single call
     * to {@link #stopService} will stop it.
     * <p>
     * The system attempts to keep running services around as much as possible.
     * The only time they should be stopped is if the current foreground
     * application is using so many resources that the service needs to be
     * killed. If any errors happen in the service's process, it will
     * automatically be restarted.
     * <p>
     * This function will throw {@link SecurityException} if you do not have
     * permission to start the given service.
     * 
     * @param service Identifies the service to be started. The Intent may
     *            specify either an explicit component name to start, or a
     *            logical description (action, category, etc) to match an
     *            {@link IntentFilter} published by a service. Additional values
     *            may be included in the Intent extras to supply arguments along
     *            with this specific start call.
     * @return If the service is being started or is already running, the
     *         {@link ComponentName} of the actual service that was started is
     *         returned; else if the service does not exist null is returned.
     * @throws SecurityException
     * @see #stopService
     * @see #bindService
     */
    public abstract ComponentName startService(Intent service);

    /**
     * Request that a given application service be stopped. If the service is
     * not running, nothing happens. Otherwise it is stopped. Note that calls to
     * startService() are not counted -- this stops the service no matter how
     * many times it was started.
     * <p>
     * Note that if a stopped service still has {@link ServiceConnection}
     * objects bound to it with the {@link #BIND_AUTO_CREATE} set, it will not
     * be destroyed until all of these bindings are removed. See the
     * {@link android.app.Service} documentation for more details on a service's
     * lifecycle.
     * <p>
     * This function will throw {@link SecurityException} if you do not have
     * permission to stop the given service.
     * 
     * @param service Description of the service to be stopped. The Intent may
     *            specify either an explicit component name to start, or a
     *            logical description (action, category, etc) to match an
     *            {@link IntentFilter} published by a service.
     * @return If there is a service matching the given Intent that is already
     *         running, then it is stopped and true is returned; else false is
     *         returned.
     * @throws SecurityException
     * @see #startService
     */
    public abstract boolean stopService(Intent service);

    /**
     * Connect to an application service, creating it if needed. This defines a
     * dependency between your application and the service. The given
     * <var>conn</var> will receive the service object when its created and be
     * told if it dies and restarts. The service will be considered required by
     * the system only for as long as the calling context exists. For example,
     * if this Context is an Activity that is stopped, the service will not be
     * required to continue running until the Activity is resumed.
     * <p>
     * This function will throw {@link SecurityException} if you do not have
     * permission to bind to the given service.
     * <p class="note">
     * Note: this method <em>can not be called from an
     * {@link BroadcastReceiver} component</em>. A pattern you can use to
     * communicate from an BroadcastReceiver to a Service is to call
     * {@link #startService} with the arguments containing the command to be
     * sent, with the service calling its
     * {@link android.app.Service#stopSelf(int)} method when done executing that
     * command. See the API demo App/Service/Service Start Arguments Controller
     * for an illustration of this. It is okay, however, to use this method from
     * an BroadcastReceiver that has been registered with
     * {@link #registerReceiver}, since the lifetime of this BroadcastReceiver
     * is tied to another object (the one that registered it).
     * </p>
     * 
     * @param service Identifies the service to connect to. The Intent may
     *            specify either an explicit component name, or a logical
     *            description (action, category, etc) to match an
     *            {@link IntentFilter} published by a service.
     * @param conn Receives information as the service is started and stopped.
     * @param flags Operation options for the binding. May be 0 or
     *            {@link #BIND_AUTO_CREATE}.
     * @return If you have successfully bound to the service, true is returned;
     *         false is returned if the connection is not made so you will not
     *         receive the service object.
     * @throws SecurityException
     * @see #unbindService
     * @see #startService
     * @see #BIND_AUTO_CREATE
     */
    public abstract boolean bindService(Intent service, ServiceConnection conn,
            int flags);

    /**
     * Disconnect from an application service. You will no longer receive calls
     * as the service is restarted, and the service is now allowed to stop at
     * any time.
     * 
     * @param conn The connection interface previously supplied to
     *            bindService().
     * @see #bindService
     */
    public abstract void unbindService(ServiceConnection conn);

    /**
     * Start executing an {@link android.app.Instrumentation} class. The given
     * Instrumentation component will be run by killing its target application
     * (if currently running), starting the target process, instantiating the
     * instrumentation component, and then letting it drive the application.
     * <p>
     * This function is not synchronous -- it returns as soon as the
     * instrumentation has started and while it is running.
     * <p>
     * Instrumentation is normally only allowed to run against a package that is
     * either unsigned or signed with a signature that the the instrumentation
     * package is also signed with (ensuring the target trusts the
     * instrumentation).
     * 
     * @param className Name of the Instrumentation component to be run.
     * @param profileFile Optional path to write profiling data as the
     *            instrumentation runs, or null for no profiling.
     * @param arguments Additional optional arguments to pass to the
     *            instrumentation, or null.
     * @return Returns true if the instrumentation was successfully started,
     *         else false if it could not be found.
     */
    // public abstract boolean startInstrumentation(ComponentName className,
    // String profileFile, Bundle arguments);

    /**
     * Return the handle to a system-level service by name. The class of the
     * returned object varies by the requested name. Currently available names
     * are:
     * <dl>
     * <dt> {@link #WINDOW_SERVICE} ("window")
     * <dd>The top-level window manager in which you can place custom windows.
     * The returned object is a {@link android.view.WindowManager}.
     * <dt> {@link #LAYOUT_INFLATER_SERVICE} ("layout_inflater")
     * <dd>A {@link android.view.LayoutInflater} for inflating layout resources
     * in this context.
     * <dt> {@link #ACTIVITY_SERVICE} ("activity")
     * <dd>A {@link android.app.ActivityManager} for interacting with the global
     * activity state of the system.
     * <dt> {@link #POWER_SERVICE} ("power")
     * <dd>A {@link android.os.PowerManager} for controlling power management.
     * <dt> {@link #ALARM_SERVICE} ("alarm")
     * <dd>A {@link android.app.AlarmManager} for receiving intents at the time
     * of your choosing.
     * <dt> {@link #NOTIFICATION_SERVICE} ("notification")
     * <dd>A {@link android.app.NotificationManager} for informing the user of
     * background events.
     * <dt> {@link #KEYGUARD_SERVICE} ("keyguard")
     * <dd>A {@link android.app.KeyguardManager} for controlling keyguard.
     * <dt> {@link #LOCATION_SERVICE} ("location")
     * <dd>A {@link android.location.LocationManager} for controlling location
     * (e.g., GPS) updates.
     * <dt> {@link #SEARCH_SERVICE} ("search")
     * <dd>A {@link android.app.SearchManager} for handling search.
     * <dt> {@link #VIBRATOR_SERVICE} ("vibrator")
     * <dd>A {@link android.os.Vibrator} for interacting with the vibrator
     * hardware.
     * <dt> {@link #CONNECTIVITY_SERVICE} ("connection")
     * <dd>A {@link android.net.ConnectivityManager ConnectivityManager} for
     * handling management of network connections.
     * <dt> {@link #WIFI_SERVICE} ("wifi")
     * <dd>A {@link android.net.wifi.WifiManager WifiManager} for management of
     * Wi-Fi connectivity.
     * <dt> {@link #INPUT_METHOD_SERVICE} ("input_method")
     * <dd>An {@link android.view.inputmethod.InputMethodManager
     * InputMethodManager} for management of input methods.
     * <dt> {@link #UI_MODE_SERVICE} ("uimode")
     * <dd>An {@link android.app.UiModeManager} for controlling UI modes.
     * </dl>
     * <p>
     * Note: System services obtained via this API may be closely associated
     * with the Context in which they are obtained from. In general, do not
     * share the service objects between various different contexts (Activities,
     * Applications, Services, Providers, etc.)
     * 
     * @param name The name of the desired service.
     * @return The service or null if the name does not exist.
     * @see #WINDOW_SERVICE
     * @see android.view.WindowManager
     * @see #LAYOUT_INFLATER_SERVICE
     * @see android.view.LayoutInflater
     * @see #ACTIVITY_SERVICE
     * @see android.app.ActivityManager
     * @see #POWER_SERVICE
     * @see android.os.PowerManager
     * @see #ALARM_SERVICE
     * @see android.app.AlarmManager
     * @see #NOTIFICATION_SERVICE
     * @see android.app.NotificationManager
     * @see #KEYGUARD_SERVICE
     * @see android.app.KeyguardManager
     * @see #LOCATION_SERVICE
     * @see android.location.LocationManager
     * @see #SEARCH_SERVICE
     * @see android.app.SearchManager
     * @see #SENSOR_SERVICE
     * @see android.hardware.SensorManager
     * @see #STORAGE_SERVICE
     * @see android.os.storage.StorageManager
     * @see #VIBRATOR_SERVICE
     * @see android.os.Vibrator
     * @see #CONNECTIVITY_SERVICE
     * @see android.net.ConnectivityManager
     * @see #WIFI_SERVICE
     * @see android.net.wifi.WifiManager
     * @see #AUDIO_SERVICE
     * @see android.media.AudioManager
     * @see #TELEPHONY_SERVICE
     * @see android.telephony.TelephonyManager
     * @see #INPUT_METHOD_SERVICE
     * @see android.view.inputmethod.InputMethodManager
     * @see #UI_MODE_SERVICE
     * @see android.app.UiModeManager
     */
    public abstract Object getSystemService(String name);

    /**
     * Use with {@link #getSystemService} to retrieve a
     * {@link android.os.PowerManager} for controlling power management,
     * including "wake locks," which let you keep the device on while you're
     * running long tasks.
     */
    public static final String POWER_SERVICE = "power";

    /**
     * Use with {@link #getSystemService} to retrieve a
     * {@link android.view.WindowManager} for accessing the system's window
     * manager.
     * 
     * @see #getSystemService
     * @see android.view.WindowManager
     */
    public static final String WINDOW_SERVICE = "window";

    /**
     * Use with {@link #getSystemService} to retrieve a
     * {@link android.view.LayoutInflater} for inflating layout resources in
     * this context.
     * 
     * @see #getSystemService
     * @see android.view.LayoutInflater
     */
    public static final String LAYOUT_INFLATER_SERVICE = "layout_inflater";

    /**
     * Use with {@link #getSystemService} to retrieve a
     * {@link android.accounts.AccountManager} for receiving intents at a time
     * of your choosing.
     * 
     * @see #getSystemService
     * @see android.accounts.AccountManager
     */
    public static final String ACCOUNT_SERVICE = "account";

    /**
     * Use with {@link #getSystemService} to retrieve a
     * {@link android.app.ActivityManager} for interacting with the global
     * system state.
     * 
     * @see #getSystemService
     * @see android.app.ActivityManager
     */
    public static final String ACTIVITY_SERVICE = "activity";

    /**
     * Use with {@link #getSystemService} to retrieve a
     * {@link android.app.AlarmManager} for receiving intents at a time of your
     * choosing.
     * 
     * @see #getSystemService
     * @see android.app.AlarmManager
     */
    public static final String ALARM_SERVICE = "alarm";

    /**
     * Use with {@link #getSystemService} to retrieve a
     * {@link android.app.NotificationManager} for informing the user of
     * background events.
     * 
     * @see #getSystemService
     * @see android.app.NotificationManager
     */
    public static final String NOTIFICATION_SERVICE = "notification";

    /**
     * Use with {@link #getSystemService} to retrieve a
     * {@link android.view.accessibility.AccessibilityManager} for giving the
     * user feedback for UI events through the registered event listeners.
     * 
     * @see #getSystemService
     * @see android.view.accessibility.AccessibilityManager
     */
    public static final String ACCESSIBILITY_SERVICE = "accessibility";

    /**
     * Use with {@link #getSystemService} to retrieve a
     * {@link android.app.NotificationManager} for controlling keyguard.
     * 
     * @see #getSystemService
     * @see android.app.KeyguardManager
     */
    public static final String KEYGUARD_SERVICE = "keyguard";

    /**
     * Use with {@link #getSystemService} to retrieve a
     * {@link android.location.LocationManager} for controlling location
     * updates.
     * 
     * @see #getSystemService
     * @see android.location.LocationManager
     */
    public static final String LOCATION_SERVICE = "location";

    /**
     * Use with {@link #getSystemService} to retrieve a
     * {@link android.app.SearchManager} for handling searches.
     * 
     * @see #getSystemService
     * @see android.app.SearchManager
     */
    public static final String SEARCH_SERVICE = "search";

    /**
     * Use with {@link #getSystemService} to retrieve a
     * {@link android.hardware.SensorManager} for accessing sensors.
     * 
     * @see #getSystemService
     * @see android.hardware.SensorManager
     */
    public static final String SENSOR_SERVICE = "sensor";

    /**
     * @hide Use with {@link #getSystemService} to retrieve a
     *       {@link android.os.storage.StorageManager} for accesssing system
     *       storage functions.
     * @see #getSystemService
     * @see android.os.storage.StorageManager
     */
    public static final String STORAGE_SERVICE = "storage";

    /**
     * Use with {@link #getSystemService} to retrieve a
     * com.android.server.WallpaperService for accessing wallpapers.
     * 
     * @see #getSystemService
     */
    public static final String WALLPAPER_SERVICE = "wallpaper";

    /**
     * Use with {@link #getSystemService} to retrieve a
     * {@link android.os.Vibrator} for interacting with the vibration hardware.
     * 
     * @see #getSystemService
     * @see android.os.Vibrator
     */
    public static final String VIBRATOR_SERVICE = "vibrator";

    /**
     * Use with {@link #getSystemService} to retrieve a
     * {@link android.app.StatusBarManager} for interacting with the status bar.
     * 
     * @see #getSystemService
     * @see android.app.StatusBarManager
     * @hide
     */
    public static final String STATUS_BAR_SERVICE = "statusbar";

    /**
     * Use with {@link #getSystemService} to retrieve a
     * {@link android.net.ConnectivityManager} for handling management of
     * network connections.
     * 
     * @see #getSystemService
     * @see android.net.ConnectivityManager
     */
    public static final String CONNECTIVITY_SERVICE = "connectivity";

    /**
     * Use with {@link #getSystemService} to retrieve a
     * {@link android.net.ThrottleManager} for handling management of
     * throttling.
     * 
     * @hide
     * @see #getSystemService
     * @see android.net.ThrottleManager
     */
    public static final String THROTTLE_SERVICE = "throttle";

    /**
     * Use with {@link #getSystemService} to retrieve a
     * {@link android.net.NetworkManagementService} for handling management of
     * system network services
     * 
     * @hide
     * @see #getSystemService
     * @see android.net.NetworkManagementService
     */
    public static final String NETWORKMANAGEMENT_SERVICE = "network_management";

    /**
     * Use with {@link #getSystemService} to retrieve a
     * {@link android.net.wifi.WifiManager} for handling management of Wi-Fi
     * access.
     * 
     * @see #getSystemService
     * @see android.net.wifi.WifiManager
     */
    public static final String WIFI_SERVICE = "wifi";

    /**
     * Use with {@link #getSystemService} to retrieve a
     * {@link android.media.AudioManager} for handling management of volume,
     * ringer modes and audio routing.
     * 
     * @see #getSystemService
     * @see android.media.AudioManager
     */
    public static final String AUDIO_SERVICE = "audio";

    /**
     * Use with {@link #getSystemService} to retrieve a
     * {@link android.telephony.TelephonyManager} for handling management the
     * telephony features of the device.
     * 
     * @see #getSystemService
     * @see android.telephony.TelephonyManager
     */
    public static final String TELEPHONY_SERVICE = "phone";

    /**
     * Use with {@link #getSystemService} to retrieve a
     * {@link android.text.ClipboardManager} for accessing and modifying the
     * contents of the global clipboard.
     * 
     * @see #getSystemService
     * @see android.text.ClipboardManager
     */
    public static final String CLIPBOARD_SERVICE = "clipboard";

    /**
     * Use with {@link #getSystemService} to retrieve a
     * {@link android.view.inputmethod.InputMethodManager} for accessing input
     * methods.
     * 
     * @see #getSystemService
     */
    public static final String INPUT_METHOD_SERVICE = "input_method";

    /**
     * Use with {@link #getSystemService} to retrieve a
     * {@link android.appwidget.AppWidgetManager} for accessing AppWidgets.
     * 
     * @hide
     * @see #getSystemService
     */
    public static final String APPWIDGET_SERVICE = "appwidget";

    /**
     * Use with {@link #getSystemService} to retrieve an
     * {@link android.app.backup.IBackupManager IBackupManager} for
     * communicating with the backup mechanism.
     * 
     * @hide
     * @see #getSystemService
     */
    public static final String BACKUP_SERVICE = "backup";

    /**
     * Use with {@link #getSystemService} to retrieve a
     * {@link android.os.DropBoxManager} instance for recording diagnostic logs.
     * 
     * @see #getSystemService
     */
    public static final String DROPBOX_SERVICE = "dropbox";

    /**
     * Use with {@link #getSystemService} to retrieve a
     * {@link android.app.admin.DevicePolicyManager} for working with global
     * device policy management.
     * 
     * @see #getSystemService
     */
    public static final String DEVICE_POLICY_SERVICE = "device_policy";

    /**
     * Use with {@link #getSystemService} to retrieve a
     * {@link android.app.UiModeManager} for controlling UI modes.
     * 
     * @see #getSystemService
     */
    public static final String UI_MODE_SERVICE = "uimode";

    public static final int CONTEXT_INCLUDE_CODE = 0x00000001;

    /**
     * Flag for use with {@link #createPackageContext}: ignore any security
     * restrictions on the Context being requested, allowing it to always be
     * loaded. For use with {@link #CONTEXT_INCLUDE_CODE} to allow code to be
     * loaded into a process even when it isn't safe to do so. Use with extreme
     * care!
     */
    public static final int CONTEXT_IGNORE_SECURITY = 0x00000002;

    /**
     * Flag for use with {@link #createPackageContext}: a restricted context may
     * disable specific features. For instance, a View associated with a
     * restricted context would ignore particular XML attributes.
     */
    public static final int CONTEXT_RESTRICTED = 0x00000004;

    /**
     * Return a new Context object for the given application name. This Context
     * is the same as what the named application gets when it is launched,
     * containing the same resources and class loader. Each call to this method
     * returns a new instance of a Context object; Context objects are not
     * shared, however they share common state (Resources, ClassLoader, etc) so
     * the Context instance itself is fairly lightweight.
     * <p>
     * Throws {@link PackageManager.NameNotFoundException} if there is no
     * application with the given package name.
     * <p>
     * Throws {@link java.lang.SecurityException} if the Context requested can
     * not be loaded into the caller's process for security reasons (see
     * {@link #CONTEXT_INCLUDE_CODE} for more information}.
     * 
     * @param packageName Name of the application's package.
     * @param flags Option flags, one of {@link #CONTEXT_INCLUDE_CODE} or
     *            {@link #CONTEXT_IGNORE_SECURITY}.
     * @return A Context for the application.
     * @throws java.lang.SecurityException
     * @throws PackageManager.NameNotFoundException if there is no application
     *             with the given package name
     */
    public abstract Context createPackageContext(String packageName, int flags);

    /**
     * Indicates whether this Context is restricted.
     * 
     * @return True if this Context is restricted, false otherwise.
     * @see #CONTEXT_RESTRICTED
     */
    public boolean isRestricted() {
        return false;
    }
}
