/*
 * Copyright (C) 2007 The Android Open Source Project
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.Collator;
import java.util.Comparator;

/**
 * Information you can retrieve about a particular application. This corresponds
 * to information collected from the AndroidManifest.xml's &lt;application&gt;
 * tag.
 */
public class ApplicationInfo extends PackageItemInfo {
    /**
     * Class implementing the Application object. From the "class" attribute.
     */
    public String className;

    /**
     * Value for {@link #flags}: set to true if this application would like to
     * allow debugging of its code, even when installed on a non-development
     * system. Comes from
     * {@link android.R.styleable#AndroidManifestApplication_debuggable
     * android:debuggable} of the &lt;application&gt; tag.
     */
    public static final int FLAG_DEBUGGABLE = 1 << 1;

    /**
     * Value for {@link #flags}: set to true if this application has code
     * associated with it. Comes from
     * {@link android.R.styleable#AndroidManifestApplication_hasCode
     * android:hasCode} of the &lt;application&gt; tag.
     */
    public static final int FLAG_HAS_CODE = 1 << 2;

    /**
     * Value for {@link #flags}: set to true if this application is persistent.
     * Comes from
     * {@link android.R.styleable#AndroidManifestApplication_persistent
     * android:persistent} of the &lt;application&gt; tag.
     */
    public static final int FLAG_PERSISTENT = 1 << 3;

    /**
     * Value for {@link #flags}: set to true if this application holds the
     * {@link android.Manifest.permission#FACTORY_TEST} permission and the
     * device is running in factory test mode.
     */
    public static final int FLAG_FACTORY_TEST = 1 << 4;

    /**
     * Value for {@link #flags}: default value for the corresponding
     * ActivityInfo flag. Comes from
     * {@link android.R.styleable#AndroidManifestApplication_allowTaskReparenting
     * android:allowTaskReparenting} of the &lt;application&gt; tag.
     */
    public static final int FLAG_ALLOW_TASK_REPARENTING = 1 << 5;

    /**
     * Value for {@link #flags}: default value for the corresponding
     * ActivityInfo flag. Comes from
     * {@link android.R.styleable#AndroidManifestApplication_allowClearUserData
     * android:allowClearUserData} of the &lt;application&gt; tag.
     */
    public static final int FLAG_ALLOW_CLEAR_USER_DATA = 1 << 6;

    /**
     * Value for {@link #flags}: this is set if this application has been
     * install as an update to a built-in system application.
     */
    public static final int FLAG_UPDATED_SYSTEM_APP = 1 << 7;

    /**
     * Value for {@link #flags}: this is set of the application has specified
     * {@link android.R.styleable#AndroidManifestApplication_testOnly
     * android:testOnly} to be true.
     */
    public static final int FLAG_TEST_ONLY = 1 << 8;

    /**
     * Value for {@link #flags}: true when the application's window can be
     * reduced in size for smaller screens. Corresponds to
     * {@link android.R.styleable#AndroidManifestSupportsScreens_smallScreens
     * android:smallScreens}.
     */
    public static final int FLAG_SUPPORTS_SMALL_SCREENS = 1 << 9;

    /**
     * Value for {@link #flags}: true when the application's window can be
     * displayed on normal screens. Corresponds to
     * {@link android.R.styleable#AndroidManifestSupportsScreens_normalScreens
     * android:normalScreens}.
     */
    public static final int FLAG_SUPPORTS_NORMAL_SCREENS = 1 << 10;

    /**
     * Value for {@link #flags}: true when the application's window can be
     * increased in size for larger screens. Corresponds to
     * {@link android.R.styleable#AndroidManifestSupportsScreens_largeScreens
     * android:smallScreens}.
     */
    public static final int FLAG_SUPPORTS_LARGE_SCREENS = 1 << 11;

    /**
     * Value for {@link #flags}: true when the application knows how to adjust
     * its UI for different screen sizes. Corresponds to
     * {@link android.R.styleable#AndroidManifestSupportsScreens_resizeable
     * android:resizeable}.
     */
    public static final int FLAG_RESIZEABLE_FOR_SCREENS = 1 << 12;

    /**
     * Value for {@link #flags}: true when the application knows how to
     * accomodate different screen densities. Corresponds to
     * {@link android.R.styleable#AndroidManifestSupportsScreens_anyDensity
     * android:anyDensity}.
     */
    public static final int FLAG_SUPPORTS_SCREEN_DENSITIES = 1 << 13;

    /**
     * Value for {@link #flags}: set to true if this application would like to
     * request the VM to operate under the safe mode. Comes from
     * {@link android.R.styleable#AndroidManifestApplication_vmSafeMode
     * android:vmSafeMode} of the &lt;application&gt; tag.
     */
    public static final int FLAG_VM_SAFE_MODE = 1 << 14;

    /**
     * Value for {@link #flags}: set to <code>false</code> if the application
     * does not wish to permit any OS-driven backups of its data;
     * <code>true</code> otherwise.
     * <p>
     * Comes from the
     * {@link android.R.styleable#AndroidManifestApplication_allowBackup
     * android:allowBackup} attribute of the &lt;application&gt; tag.
     */
    public static final int FLAG_ALLOW_BACKUP = 1 << 15;

    /**
     * Value for {@link #flags}: set to <code>false</code> if the application
     * must be kept in memory following a full-system restore operation;
     * <code>true</code> otherwise. Ordinarily, during a full system restore
     * operation each application is shut down following execution of its
     * agent's onRestore() method. Setting this attribute to <code>false</code>
     * prevents this. Most applications will not need to set this attribute.
     * <p>
     * If {@link android.R.styleable#AndroidManifestApplication_allowBackup
     * android:allowBackup} is set to <code>false</code> or no
     * {@link android.R.styleable#AndroidManifestApplication_backupAgent
     * android:backupAgent} is specified, this flag will be ignored.
     * <p>
     * Comes from the
     * {@link android.R.styleable#AndroidManifestApplication_killAfterRestore
     * android:killAfterRestore} attribute of the &lt;application&gt; tag.
     */
    public static final int FLAG_KILL_AFTER_RESTORE = 1 << 16;

    /**
     * Value for {@link #flags}: Set to <code>true</code> if the application's
     * backup agent claims to be able to handle restore data even
     * "from the future," i.e. from versions of the application with a
     * versionCode greater than the one currently installed on the device.
     * <i>Use with caution!</i> By default this attribute is <code>false</code>
     * and the Backup Manager will ensure that data from "future" versions of
     * the application are never supplied during a restore operation.
     * <p>
     * If {@link android.R.styleable#AndroidManifestApplication_allowBackup
     * android:allowBackup} is set to <code>false</code> or no
     * {@link android.R.styleable#AndroidManifestApplication_backupAgent
     * android:backupAgent} is specified, this flag will be ignored.
     * <p>
     * Comes from the
     * {@link android.R.styleable#AndroidManifestApplication_restoreAnyVersion
     * android:restoreAnyVersion} attribute of the &lt;application&gt; tag.
     */
    public static final int FLAG_RESTORE_ANY_VERSION = 1 << 17;

    /**
     * Value for {@link #flags}: Set to true if the application is currently
     * installed on external/removable/unprotected storage. Such applications
     * may not be available if their storage is not currently mounted. When the
     * storage it is on is not available, it will look like the application has
     * been uninstalled (its .apk is no longer available) but its persistent
     * data is not removed.
     */
    public static final int FLAG_EXTERNAL_STORAGE = 1 << 18;

    /**
     * Value for {@link #flags}: Set to true if the application has been
     * installed using the forward lock option. {@hide}
     */
    public static final int FLAG_FORWARD_LOCK = 1 << 20;

    /**
     * Value for {@link #flags}: Set to true if the application is
     * native-debuggable, i.e. embeds a gdbserver binary in its .apk {@hide
     * 
     * 
     * }
     */
    public static final int FLAG_NATIVE_DEBUGGABLE = 1 << 21;

    /**
     * Flags associated with the application. Any combination of
     * {@link #FLAG_SYSTEM}, {@link #FLAG_DEBUGGABLE}, {@link #FLAG_HAS_CODE},
     * {@link #FLAG_PERSISTENT}, {@link #FLAG_FACTORY_TEST}, and
     * {@link #FLAG_ALLOW_TASK_REPARENTING} {@link #FLAG_ALLOW_CLEAR_USER_DATA},
     * {@link #FLAG_UPDATED_SYSTEM_APP}, {@link #FLAG_TEST_ONLY},
     * {@link #FLAG_SUPPORTS_SMALL_SCREENS},
     * {@link #FLAG_SUPPORTS_NORMAL_SCREENS},
     * {@link #FLAG_SUPPORTS_LARGE_SCREENS},
     * {@link #FLAG_RESIZEABLE_FOR_SCREENS},
     * {@link #FLAG_SUPPORTS_SCREEN_DENSITIES}, {@link #FLAG_VM_SAFE_MODE}
     */
    public int flags = 0;

    // public static class DisplayNameComparator implements
    // Comparator<ApplicationInfo> {
    // public DisplayNameComparator(PackageManager pm) {
    // mPM = pm;
    // }
    //
    // public final int compare(ApplicationInfo aa, ApplicationInfo ab) {
    // // CharSequence sa = mPM.getApplicationLabel(aa);
    // // if (sa == null) {
    // // sa = aa.packageName;
    // // }
    // // CharSequence sb = mPM.getApplicationLabel(ab);
    // // if (sb == null) {
    // // sb = ab.packageName;
    // // }
    //
    // return
    // }
    //
    // private final Collator sCollator = Collator.getInstance();
    // private PackageManager mPM;
    // }

    public ApplicationInfo() {
    }

    public ApplicationInfo(ApplicationInfo orig) {
        className = orig.className;
        flags = orig.flags;
    }

    public String toString() {
        return "ApplicationInfo{"
                + Integer.toHexString(System.identityHashCode(this)) + " "
                + packageName + "}";
    }

    public int describeContents() {
        return 0;
    }

    private ApplicationInfo(Parcel source) {
        className = source.readString();
        flags = source.readInt();
    }

    public static final Parcelable.Creator<ApplicationInfo> CREATOR = new Parcelable.Creator<ApplicationInfo>() {
        public ApplicationInfo createFromParcel(Parcel source) {
            return new ApplicationInfo(source);
        }

        public ApplicationInfo[] newArray(int size) {
            return new ApplicationInfo[size];
        }
    };

    /**
     * Disable compatibility mode
     * 
     * @hide
     */
    public void disableCompatibilityMode() {
        flags |= (FLAG_SUPPORTS_LARGE_SCREENS | FLAG_SUPPORTS_NORMAL_SCREENS
                | FLAG_SUPPORTS_SMALL_SCREENS | FLAG_RESIZEABLE_FOR_SCREENS | FLAG_SUPPORTS_SCREEN_DENSITIES);
    }

    /**
     * @hide
     */
    @Override
    protected ApplicationInfo getApplicationInfo() {
        return this;
    }
}
