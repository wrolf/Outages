/*
 * Copyright (c) Wrolf Courtney <wrolf@wrolf.net> 2015.
 *
 * Portions copyright 2013 The Android Open Source Project
 *
 * Said portions licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.wrolf.android.outages.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Field and table name constants for
 * {@link net.wrolf.android.outages.provider.FeedProvider}.
 */
public class FeedContract {
    private FeedContract() {
    }

    /**
     * Content provider authority.
     */
    public static final String CONTENT_AUTHORITY = "net.wrolf.android.outages";

    /**
     * Base URI. (content://net.wrolf.android.outages)
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Path component for "outage"-type resources..
     */
    private static final String PATH_ENTRIES = "outages";

    /**
     * Columns supported by "outages" records.
     */
    public static class OutageColumns implements BaseColumns {
        /**
         * MIME type for lists of outages.
         */
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.outages.outages";
        /**
         * MIME type for individual outages.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.outages.outage";

        /**
         * Fully qualified URI for "outage" resources.
         */
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ENTRIES).build();

        /**
         * Table name where records are stored for "outage" resources.
         */
        public static final String TABLE_NAME = "outage";
        public static final String COLUMN_NAME_OUTAGE_ID = "outage_id";
        public static final String COLUMN_NAME_LOG_MESSAGE = "logmessage";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_NODE_LABEL = "nodelabel";
    }
}