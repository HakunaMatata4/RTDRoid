
package edu.buffalo.rtdroid;

import android.content.Context;
import android.content.ScopedContextImpl;
import android.util.Log;

public class SystemConfig {
    /* Experimental parameters at Runtime */
    public static int LOG_LEVEL = Log.DEBUG__LEVEL;
    public static int exp_platform = SystemConfig.EXP_RT_LINUX;
    public static int collector_type = 0;
    public static String exp_name = "";
    // flag for android or rtdoird for looper&&handler exp
    public static boolean rtdroid = true;
    // flag for android or rtdoird for alarm exp
    public static int alarm_type = 0; // Default 0
    /* End of experimental parameters */

    /* Temporary scoped configuration in RTdroid manifest XML */
    public static final long IMMORTAL_SIZE = 10000000L; 
    // 10 000 000bytes = 10M
    public static final long DEMO_ACTIVITY_SIZE = 1000000L; 
    // 1 000 000bytes = 1M
    public static final long STANDALONE_SERVICE_SIZE = 1000000L; 
    // 1 000 000bytes = 1M
    public static final long BOUND_SERVICE_SIZE = 1000000L; 
    // 1 000 000bytes = 1M
    /* End of temporary configuration */
    
    /*GC Configuration*/
    public static final boolean SCOPED_MEMORY = false;
    public static final boolean OBJ_POOL_TEST = false;
    public static final int MESSAGE_DEFAULT_SIZE = 50;
    /* Configuration states */
    public static final String ALARM_SCHEDULER_POOL = "pool";
    public static final String ALARM_SCHEDULER_ASYN = "async";
    public static final String JPAPBENCH = "jpapbench";
    public static final int ALARM_CAPACITY = 3;
    public static final int EXP_RT_LINUX = 1;
    public static final int EXP_RTEMS = 2;
    public static final int EXP_RT_PHONE = 3;
    public static int COLLECTOR_TYPE_PRINT = 1;
    public static int COLLECTOR_TYPE_FILE = 2;

    public static final int NONE_REAL_TIME_THREAD_PRIORITY = 5;
    public static final int RTEMS_HIGHEST_PRIORITY = 255;
    public static final int RTEMS_LOWEST_PRIORITY = 11;
    public static final int RT_LINUX_HIGHEST_PRIORITY = 99;
    public static final int RT_LINUX_LOWEST_PRIORITY = 11;

    public static long RTEMS_EXP_DURATION = 160 * 1000L;
    public static long RTEMS_EXP_HARD_STOP = 180 * 1000L;
    public static long RT_LINUX_EXP_DURATION = 40 * 1000L;
    public static long RT_LINUX_EXP_HARD_STOP = 50 * 1000L;

    public static final int RT_LINUX_LOW_THREAD_INTERVAL = 10;
    public static final int RT_LINUX_HIGH_THREAD_INTERVAL = 100;
    public static final int RTEMS_LOW_THREAD_INTERVAL = 30;
    public static final int RTEMS_HIGHT_THREAD_INTERVAL = 400;
    
    public static final long ACTIVITY_SCOPE_SIZE = 100000L;
    public static final long SERVICE_SCOPE_SIZE = 100000L;
    public static final long BOUND_SERVICE_SCOPE_SIZE = 50000L;
    public static Context applicationContext;
    
    /*Object Pool Configuration*/
    public static final int INTENT_POOL_CAPACITY = 50;
    public static final int MESSAGE_POOL_CAPACITY = 50;
    
    public static class TestReceiver{
        public static String name = "edu.buffalo.rtdroid.apps.demo.BroadcastReceiver";
        public static int priority = 50;
        public static String type = "Integer";
    }
    
    /* End of configuration states */
    public static int getMaxPriority() {
        int max = 0;

        switch (SystemConfig.exp_platform) {
            case EXP_RT_LINUX:
            case EXP_RT_PHONE:
                max = RT_LINUX_HIGHEST_PRIORITY;
                break;
            case EXP_RTEMS:
                max = RTEMS_HIGHEST_PRIORITY;
                break;
        }

        return max;
    }

    public static int getMinPriority() {
        int max = 0;

        switch (SystemConfig.exp_platform) {
            case EXP_RT_LINUX:
            case EXP_RT_PHONE:
                max = SystemConfig.RT_LINUX_LOWEST_PRIORITY;
                break;
            case EXP_RTEMS:
                max = SystemConfig.RTEMS_LOWEST_PRIORITY;
                break;
        }

        return max;
    }

    public static long getWorkerDuration() {
        long d = 0;

        switch (SystemConfig.exp_platform) {
            case EXP_RT_LINUX:
            case EXP_RT_PHONE:
                d = SystemConfig.RT_LINUX_EXP_DURATION;
                break;
            case EXP_RTEMS:
                d = SystemConfig.RTEMS_EXP_DURATION;
                break;
        }

        return d;
    }

    public static long getHardStop() {
        long stop = 0;

        switch (SystemConfig.exp_platform) {
            case EXP_RT_LINUX:
            case EXP_RT_PHONE:
                stop = SystemConfig.RT_LINUX_EXP_HARD_STOP;
                break;
            case EXP_RTEMS:
                stop = SystemConfig.RTEMS_EXP_DURATION;
                break;
        }

        return stop;
    }

    public static int getHighPriorityInterval() {
        switch (SystemConfig.exp_platform) {
            case EXP_RT_LINUX:
            case EXP_RT_PHONE:
                return SystemConfig.RT_LINUX_HIGH_THREAD_INTERVAL;
            case EXP_RTEMS:
                return SystemConfig.RTEMS_HIGHT_THREAD_INTERVAL;
            default:
                return SystemConfig.RT_LINUX_HIGH_THREAD_INTERVAL;
        }
    }

    public static int getLowPriorityInterval() {
        int interval = 0;

        switch (SystemConfig.exp_platform) {
            case EXP_RT_LINUX:
            case EXP_RT_PHONE:
                interval = SystemConfig.RT_LINUX_LOW_THREAD_INTERVAL;
                break;
            case EXP_RTEMS:
                interval = SystemConfig.RTEMS_LOW_THREAD_INTERVAL;
                break;
        }
        return interval;
    }

    public static int getAlarmSchedulerType() {
        return SystemConfig.alarm_type;
    }
    
    public static Context getApplicationContext(){
        if(applicationContext==null){
            applicationContext = new ScopedContextImpl("app_name");
        }
        return applicationContext;
    }
}
