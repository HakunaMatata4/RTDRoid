
package android.content.pm;

import android.os.Bundle;
import java.text.Collator;
import java.util.Comparator;

public class PackageItemInfo {
    /**
     * Public name of this item. From the "android:name" attribute.
     */
    public String name;

    /**
     * Name of the package that this item is in. "manifest.attr("package")
     */
    public String packageName;

    /**
     * Additional meta-data associated with this component. This field will only
     * be filled in if you set the {@link PackageManager#GET_META_DATA} flag
     * when requesting the info. activity.meta-data or service.meta-data
     */
    public Bundle metaData;

    public PackageItemInfo() {
    }

    public PackageItemInfo(String name) {
        this.name = name;
    }

    protected ApplicationInfo getApplicationInfo() {
        return null;
    }
}
