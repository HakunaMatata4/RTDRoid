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

import com.fiji.rtdroid.ActiveConfiguration;

/**
 * Information you can retrieve about a particular application service. This
 * corresponds to information collected from the AndroidManifest.xml's
 * &lt;service&gt; tags.
 */
public class ServiceInfo extends ComponentInfo {
    /**
     * Bit in {@link #flags}: If set, the service will automatically be stopped
     * by the system if the user removes a task that is rooted in one of the
     * application's activities. Set from the
     * {@link android.R.attr#stopWithTask} attribute.
     */
    public static final int FLAG_STOP_WITH_TASK = 0x0001;

    /**
     * Bit in {@link #flags}: If set, the service will run in its own isolated
     * process. Set from the {@link android.R.attr#isolatedProcess} attribute.
     */
    public static final int FLAG_ISOLATED_PROCESS = 0x0002;

    /**
     * Options that have been set in the service declaration in the manifest.
     * These include: {@link #FLAG_STOP_WITH_TASK},
     * {@link #FLAG_ISOLATED_PROCESS}.
     */
    public int flags;

    public ServiceInfo(ApplicationInfo appInfo, String name) {
        super(appInfo, name);
    }

    public ServiceInfo(ApplicationInfo appInfo, String name,
            ActiveConfiguration config) {
        super(appInfo, name, config);
    }
}
