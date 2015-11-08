
package edu.buffalo.rtdroid.apps.mmu;

import javax.realtime.RealtimeThread;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

public class MainActivity extends Activity {
    private boolean[] array;

    public MainActivity() {
        super();
    }

    public void onStart() {
        System.out.println("MainActivity activity onstart()...");
        array = new boolean[9000 * 1024];
        System.out.println("Start warm-up");
        computePI();
        System.out.println("End warm-up");
        Intent intent = new Intent(this, SenderService.class);
        startService(intent);
    }

    public void onCreate() {
        System.out.println("DemoActivity onCreate()...");
    }

    public void onDestroy() {
        System.out.println("DemoActivity onDestroy()...");
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

    public void computePI() {
        int i = 0;
        float result = 0.0f;
        long startTime = System.currentTimeMillis();
        // compute for 30 seconds, Gregory-Leibniz series
        while (System.currentTimeMillis() - startTime < 80000) {
            if (i % 2 == 0) {
                result = result + (float) 4 / (2 * i + 1);
            } else {
                result = result - (float) 4 / (2 * i + 1);
            }
            i++;
        }
        System.out.println("result:" + result + ", Counter" + i);
    }
}
