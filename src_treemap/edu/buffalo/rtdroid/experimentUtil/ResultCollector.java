
package edu.buffalo.rtdroid.experimentUtil;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fiji.fivm.r1.Import;
import com.fiji.fivm.r1.Pointer;
import com.fiji.fivm.r1.fivmRuntime;

import edu.buffalo.rtdroid.SystemConfig;

public class ResultCollector {
    private static List<String> items = new ArrayList<String>(3000);
    static boolean isFinish = false;
    public static String outputFile = "";
    static int counter = 0;

    public static void addItem(String str) {
        // System.out.println("Result collector add:" + str );
        counter++;
        if (!isFinish && counter % 2 == 0) {
            items.add(str);
        }
    }

    public static void dumpResult() {
        isFinish = true;
        if (SystemConfig.collector_type == SystemConfig.COLLECTOR_TYPE_PRINT) {
            Iterator<String> it = items.iterator();
            while (it.hasNext()) {
                String tmp = it.next();
                System.out.println(tmp);
            }
            // for (String item : items) {
            // st.append(item+"/n");
            // //System.out.println(item);
            // }
            // System.out.println(st.toString());
        } else {
            try {
                System.out.println("result collector print:" + outputFile);
                PrintWriter printer = new PrintWriter(outputFile);
                for (String item : items) {
                    printer.println(item);
                }
                printer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Import
    static native TimeVal getNativeTimestamp(Pointer p);

    public static TimeVal getSystemNano() {
        TimeVal tVal = new TimeVal();
        if (SystemConfig.exp_platform == SystemConfig.EXP_RTEMS) {
            long time = System.nanoTime();
            tVal.tv_sec = time / 1000000000L;
            tVal.tv_usec = time % 1000000000L;
        } else {
            Pointer nowPr = fivmRuntime.getBuffer(255);
            ResultCollector.getNativeTimestamp(nowPr);
            String nowStr = fivmRuntime.fromCStringFull(nowPr);
            // System.out.println("now--->" + nowStr);vim

            int splitPoint = findIndexOf(nowStr, ',', 0);

            tVal.tv_sec = Long.parseLong(nowStr.substring(0, splitPoint));
            tVal.tv_usec = Long.parseLong(nowStr.substring(splitPoint + 1,
                    nowStr.length()));

        }

        return tVal;
    }

    public static int findIndexOf(String target, char x, int start) {
        for (int i = start; i < target.length(); i++) {
            if (target.charAt(i) == x)
                return i;
        }

        return -1;
    }
}
