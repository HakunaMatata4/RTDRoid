
package android.hardware.hal;

import android.hardware.SensorNative;

import com.fiji.fivm.r1.Import;
import com.fiji.fivm.r1.Pointer;

public class SensorNativeRTEMSImpl implements SensorNative {
    // sensor params, it was defined in sensor_params.h
    public static final float GRAVITY_EARTH = 9.80665f;
    public static final float LSB = 64.0f;
    public static final float NUMOFACCDATA = 8.0f;
    public static final float CONVERT_SI = (GRAVITY_EARTH / LSB / NUMOFACCDATA);
    public static final float CONVERT_A_X = CONVERT_SI;
    public static final float CONVERT_A_Y = CONVERT_SI;
    public static final float CONVERT_A_Z = CONVERT_SI;

    @Import
    public static native int openSensor(Pointer dev, int sensorId);

    @Import
    public static native int pollData(Pointer event, int senorId);

    @Import
    public static native void getNativeTimestampImpl(Pointer t);

    public void init() {
        // TODO we need to change the implementations
    }

    public String poll(int sensodId) {
        // TODO we will re-implement the
        return null;
    }
    /*
     * @Import public static native int nativeFunctionRegisterI2C(Pointer
     * event);
     * @Import public static native int nativeFunction(Pointer event);
     * @Import public static native int nativeFunctionCheckI2C(Pointer event);
     * @Import public static native int nativeFunctionOpen();
     * @Import public static native int nativeFunctionClose();
     * @Import public static native int nativeFunctionRead(Pointer Event);
     * @Import public static native int nativeAcclOpen();
     * @Import public static native int nativeAcclRead();
     * @Import public static native int nativeAcclPoll(Pointer event);
     * @Import public static native int nativeAcclClose();
     */
}
