
package edu.buffalo.rtdroid.experimentUtil;

import com.fiji.fivm.ThreadPriority;

public class RealTimeHelper {
    private static RealTimeHelper instance = null;

    public static RealTimeHelper getInstance() {
        if (instance == null) {
            instance = new RealTimeHelper();
        }

        return instance;
    }

    private RealTimeHelper() {
    }

    /*
     * convert the low level priority to programming priority value there is
     * some operation performed on the priority in the thread class, this
     * reverts the priority back to a more expected form
     */
    public int FijiFIFO2RTSJ(int vmPriotiy) {
        return ThreadPriority.priority(vmPriotiy) + 9;
    }

    // @Import
    // static native void debugPrint(Pointer p);
    //
    // public void printk(String str){
    // Pointer p = fivmRuntime.getBuffer(str.length());
    // debugPrint(p);
    // }
}
