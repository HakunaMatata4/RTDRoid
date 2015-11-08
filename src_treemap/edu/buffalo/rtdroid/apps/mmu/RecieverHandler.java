
package edu.buffalo.rtdroid.apps.mmu;

import android.os.Message;
import android.realtime.RTHandler;
import android.util.Log;

public class RecieverHandler extends RTHandler {
    private static String TAG = "RecieverHandler";
    long[][] dataBag;
    int numMsg;
    ReceiverService recieveService;

    public RecieverHandler(ReceiverService service, String name) {
        super(name);
        this.recieveService = service;
        dataBag = new long[SenderService.NUM_MSG][4];
        numMsg = 0;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case ReceiverService.PING_MSG:
                if (numMsg < SenderService.NUM_MSG) {
                    dataBag[numMsg][2] = System.nanoTime();
                    // shall we do some computation here???/
                    for (int i = 0; i < 12; i++) {
                        boolean[] array = new boolean[50 * 1024];
                    }
                    dataBag[numMsg][3] = System.nanoTime();
                    dataBag[numMsg][0] = (long) msg.getData().getLong(
                            ReceiverService.MSG_BEFORE_MSG_ALLO);
                    dataBag[numMsg][1] = (long) msg.getData().getLong(
                            ReceiverService.MSG_BEFORE_MSG_SEND);
                    numMsg++;
                }
                break;
            case ReceiverService.MSG_END:
                Log.i(TAG, "/----Dump result----/");
                for (long[] item : dataBag) {
                    Log.i(TAG, item[0] + " " + item[1] + " " + item[2] + " "
                            + item[3]);
                }
                Log.i(TAG, "finish dump");
                recieveService.stopSelf();
                break;
            default:
                break;
        }
    }

}
