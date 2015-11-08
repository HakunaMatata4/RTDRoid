
package edu.buffalo.rtdroid.apps.demo;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentPool;

public class DemoActivity extends Activity {
    public DemoActivity() {
        super();
    }

    public static void main(String[] a) {
        System.out.println("demo activity onstart()...");
    }

    public void onStart() {
        System.out.println("demo activity onstart()...");
    }

    public void onCreate() {
        System.out.println("DemoActivity onCreate()...");
        Intent intent1 = IntentPool.instance().requestObject();
        intent1.setAction(ServiceAA.class);
        startService(intent1);
        Intent intent2 = new Intent(this, ServiceBB.class);
        intent2.setAction(ServiceBB.class);
        startService(intent2);
    }

    public void onDestroy() {
        System.out.println("DemoActivity onDestroy()...");
        Intent intent1 = IntentPool.instance().requestObject();
        intent1.setAction(ServiceAA.class);
        stopService(intent1);
        Intent intent2 = new Intent(this, ServiceBB.class);
        intent2.setAction(ServiceBB.class);
        stopService(intent2);
    }

    public void onResume() {
        System.out.println("DemoActivity onResume()...");
    }

    public void onPause() {
        System.out.println("DemoActivity onPause()...");
    }

    public void onStop() {
        System.out.println("DemoActivity onStop()...");
    }
}
