
package android.hardware.hal;

import com.fiji.fivm.r1.Import;
import com.fiji.fivm.r1.Pointer;

public class SensorNativeNexusImpl {
    // sensor params, it was defined in sensor_params.h
    public static final float GRAVITY_EARTH = 9.80665f;
    public static final float LSB = 64.0f;
    public static final float NUMOFACCDATA = 8.0f;
    // public static final float CONVERT_SI = (GRAVITY_EARTH/LSB/NUMOFACCDATA);
    // TODO Changed, needs to be board / accelerometer dependant
    // public static final float CONVERT_SI = (float) 1.0;
    public static final float CONVERT_SI = GRAVITY_EARTH / LSB / NUMOFACCDATA;
    public static final float CONVERT_A_X = CONVERT_SI;
    public static final float CONVERT_A_Y = CONVERT_SI;
    public static final float CONVERT_A_Z = CONVERT_SI;

    @Import
    public static native int openSensor(Pointer dev, int sensorId);

    @Import
    public static native int pollData(Pointer event, int senorId);
}
