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

import com.fiji.rtdroid.ActivityConfiguration;

/**
 * Information you can retrieve about a particular application activity or
 * receiver. This corresponds to information collected from the
 * AndroidManifest.xml's &lt;activity&gt; and &lt;receiver&gt; tags.
 */
// TODO
public class ActivityInfo extends ComponentInfo {
    /**
     * Constant corresponding to <code>standard</code> in the
     * {@link android.R.attr#launchMode} attribute.
     */
    public static final int LAUNCH_MULTIPLE = 0;
    /**
     * Constant corresponding to <code>singleTop</code> in the
     * {@link android.R.attr#launchMode} attribute.
     */
    public static final int LAUNCH_SINGLE_TOP = 1;
    /**
     * Constant corresponding to <code>singleTask</code> in the
     * {@link android.R.attr#launchMode} attribute.
     */
    public static final int LAUNCH_SINGLE_TASK = 2;
    /**
     * Constant corresponding to <code>singleInstance</code> in the
     * {@link android.R.attr#launchMode} attribute.
     */
    public static final int LAUNCH_SINGLE_INSTANCE = 3;
    /**
     * The launch mode style requested by the activity. From the
     * {@link android.R.attr#launchMode} attribute, one of
     * {@link #LAUNCH_MULTIPLE}, {@link #LAUNCH_SINGLE_TOP},
     * {@link #LAUNCH_SINGLE_TASK}, or {@link #LAUNCH_SINGLE_INSTANCE}.
     */
    public int launchMode;

    public int describeContents() {
        return 0;
    }

    public ActivityInfo(ApplicationInfo appInfo, String name,
            ActivityConfiguration config) {
        super(appInfo, name, config);
    }

    public ActivityInfo(ApplicationInfo appInfo, String name) {
        super(appInfo, name);
    }
}
