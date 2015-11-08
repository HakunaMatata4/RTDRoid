
package android.realtime;

import android.os.Handler;
import android.os.Looper;

public interface HandlerFactory<T extends Handler> {
    public T create(Looper looper);
}
