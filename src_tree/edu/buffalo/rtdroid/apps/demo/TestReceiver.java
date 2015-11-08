package edu.buffalo.rtdroid.apps.demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TestReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) { 
        System.out.println("TestReceiver onReceive here ....");
        System.out.println("Intent int : " + intent.getIntData());
    }
}
