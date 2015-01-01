/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
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

package net.wrolf.android.outages.net;

import android.util.Xml;

import net.wrolf.android.common.logger.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class parses generic Atom feeds.
 *
 * <p>Given an InputStream representation of a feed, it returns a List of outages,
 * where each list element represents a single outage (post) in the XML feed.
 *
 * <p>An example of an Atom feed can be found at:
 * http://en.wikipedia.org/w/index.php?title=Atom_(standard)&oldid=560239173#Example_of_an_Atom_1.0_feed
 */
public class FeedParser {

    // Constants indicting XML element names that we're interested in
    private static final int TAG_ID = 1;
    private static final int TAG_NODELABEL = 2;
    private static final int TAG_TIME = 3;
    private static final int TAG_LOGMESSAGE = 4;
    private static final int TAG_LINK = 5;

    // <outage>
    // <outage id="201238">
    // <ifLostService>
    // ...
    // <logMessage>Interface 24.93.73.62 is down.</logMessage>
    // ...
    // <time>2014-10-24T15:59:32-04:00</time>
    // ...
    // <nodeLabel>twc-rr-nc-2-la-ca</nodeLabel>
    // </outage>

    private enum outageTag { none, nodeTag, logMessageTag, nodeLabelTag, timeTag }

    // We don't use XML namespaces
    private static final String ns = null;

    /** Parse an Outage feed, returning a collection of Outage objects.
     *
     * @param inputStream Outages feed, as a stream.
     * @return List of {@link net.wrolf.android.outages.net.FeedParser.Outage} objects.
     * @throws org.xmlpull.v1.XmlPullParserException on error parsing feed.
     * @throws java.io.IOException on I/O error.
     */
    public List<Outage> getOutageDataFromXML(InputStream inputStream)
        throws XmlPullParserException, IOException, ParseException {
        final String LOG_TAG = "getOutageDataFromXML";

        outageTag currentTag = outageTag.none;

        List<Outage> outages = new ArrayList<>();

        Outage o = new Outage();

        try {
            // XmlPullParser xpp = XmlPullParserFactory.newInstance().newPullParser();
            XmlPullParser xpp = Xml.newPullParser();
            xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xpp.setInput(inputStream, null);

            for (int eventType = xpp.getEventType();
                 eventType != XmlPullParser.END_DOCUMENT;
                 eventType = xpp.next())
            {
                if(eventType == XmlPullParser.START_DOCUMENT) {
                    Log.d(LOG_TAG, "In start document");
                } else if(eventType == XmlPullParser.START_TAG) {
//                    Log.d(LOG_TAG, "In start tag = "+xpp.getName());
                    // TODO outage id=
                    String tagName = xpp.getName();
                    if (tagName.equalsIgnoreCase("outage")) {
                        currentTag = outageTag.nodeTag;
                        o.outageId = xpp.getAttributeValue(null, "id");
                    } else if (tagName.equalsIgnoreCase("logMessage"))
                        currentTag = outageTag.logMessageTag;
                    else if (tagName.equalsIgnoreCase("nodeLabel"))
                        currentTag = outageTag.nodeLabelTag;
                    else if (tagName.equalsIgnoreCase("time"))
                        currentTag = outageTag.timeTag;
                    else
                        currentTag = outageTag.none;

                } else if(eventType == XmlPullParser.END_TAG) {
//                    Log.d(LOG_TAG, "In end tag = "+xpp.getName());
                    if (xpp.getName().contentEquals("outage")) {
                        Log.d(LOG_TAG, "o={" + o.outageId + o.time + "," + o.nodeLabel + "," + o.logMessage + "}");
                        outages.add(o);
                    }
                } else if(eventType == XmlPullParser.TEXT) {
                    switch (currentTag) {
                        case logMessageTag:
//                            Log.d(LOG_TAG, "logMessage = " + xpp.getText());
                            o.logMessage = xpp.getText();
                            break;

                        case nodeLabelTag:
//                            Log.d(LOG_TAG, "nodeLabel = " + xpp.getText());
                            o.nodeLabel = xpp.getText();
                            break;

                        case timeTag:
                            o.time = xpp.getText();
                            // TODO parse time
                            // o.time = Long.parseLong(xpp.getText());
                            break;
                    }
                }
            }

        } catch (XmlPullParserException|IOException e) {
            e.printStackTrace();
        } finally {
            inputStream.close();
        }

        return outages;
    }

    /**
     * This class represents a single outage in the XML feed.
     */
    public static class Outage {
        public String outageId  = null;
        public String logMessage = null;
        public String time = null;
        // TODO parse time public long time = 0;
        public String nodeLabel = null;

    }
}
