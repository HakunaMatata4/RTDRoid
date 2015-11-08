
package android.hardware;

import edu.buffalo.rtdroid.experimentUtil.TimeVal;

public class IRSimSensor extends SensorBase {
    private String attitude;
    boolean isNew = false;
    private TimeVal bufferedTime;
    final static int DEFAULT_IR_RATE = 50;

    public IRSimSensor(Sensor s) {
        super(DEFAULT_IR_RATE, s);
        this.attitude = "";
        this.bufferedTime = new TimeVal();
    }

    @Override
    public void init() {
        // do nothing
    }

    @Override
    boolean pollEvent() {
        // do nothing. the data is set by FlightSimultorTaskHandler...
        return isNew;
    }

    @Override
    void process() {
        String attString = getAltitude();
        // System.out.println("process::att::" + attString);

        if (attString.length() == 0)
            return;

        int currIndex = 0;
        int i = 0;
        while (attString.charAt(currIndex) != ';') {
            int lastIndex = currIndex;
            currIndex = findIndexOf(attString, ',', currIndex);
            // System.out.println("===========>" + currIndex);
            if (currIndex > lastIndex) {
                switch (i) {
                    case 0:
                    case 1:
                    case 2:
                        sensor.currEvent.values[i] = Float.parseFloat(attString
                                .substring(lastIndex, currIndex));
                        break;
                    case 3:
                        sensor.currEvent.second = Long.parseLong(attString
                                .substring(lastIndex, currIndex));
                        break;
                    case 4:
                        sensor.currEvent.microSenond = Long.parseLong(attString
                                .substring(lastIndex, currIndex));
                        break;
                }
                currIndex = currIndex + 1;
                i++;
            }
        }
        isNew = false;
    }

    public String getAltitude() {
        return attitude;
    }

    public void setAltitude(String altitude) {
        this.attitude = altitude;
        this.isNew = true;
    }

    /* experimental interface */
    public void setAltitude(String altitude, TimeVal t) {
        // System.out.println("fligh sim set value" + altitude);
        this.attitude = altitude;
        this.bufferedTime.tv_sec = t.tv_sec;
        this.bufferedTime.tv_usec = t.tv_usec;
        this.isNew = true;
    }
}
