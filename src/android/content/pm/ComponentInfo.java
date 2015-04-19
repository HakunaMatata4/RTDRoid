
package android.content.pm;

import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityScheduler;
import com.fiji.rtdroid.ActiveConfiguration;

public class ComponentInfo extends PackageItemInfo {
    /**
     * Indicates whether or not this component may be instantiated.
     */
    public boolean enabled;
    public boolean isRealtime;;
    public int priority;
    public boolean repeatable;
    public long periodicityMills;
    public long periodicityNano;
    public boolean exported; /*
                              * Set to true if this component is available for
                              * use by other applications.
                              */

    public ComponentInfo(ApplicationInfo appInfo, String name) {
        super(name);
        this.enabled = true;
        this.exported = true;
        this.isRealtime = false;
        this.repeatable = false;
        this.periodicityMills = 0L;
        this.periodicityNano = 0L;
    }

    public ComponentInfo(ApplicationInfo appInfo, String name,
            ActiveConfiguration config) {
        this(appInfo, name);
        if (this.priority >= PriorityScheduler.instance().getMinPriority()
                && this.priority <= PriorityScheduler.instance()
                        .getMaxPriority()) {
            this.isRealtime = true;
        }
    }

    public ComponentInfo(ApplicationInfo appInfo, String name,
            ActiveConfiguration config, PeriodicParameters period) {
        this(appInfo, name, config);
        if (period != null) {
            this.repeatable = true;
            this.periodicityMills = period.getPeriod().getMilliseconds();
            this.periodicityNano = period.getPeriod().getNanoseconds();
        }
    }
}
