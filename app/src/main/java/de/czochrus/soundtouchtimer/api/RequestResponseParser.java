package de.czochrus.soundtouchtimer.api;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestResponseParser {

    public static Map<String, String> parse(String body, List<String> fields) throws XmlPullParserException, IOException {
        Map<String, String> result = new HashMap<String, String>();
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(false);

        XmlPullParser xpp = factory.newPullParser();
        xpp.setInput(new StringReader(body));
        int eventType = xpp.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_DOCUMENT) {
            } else if (eventType == XmlPullParser.START_TAG) {
                if (fields.contains(xpp.getName())) {
                    String actual = xpp.getName();
                    int attr = xpp.getAttributeCount();
                    if (attr > 0) {
                        for (int i = 0; i < attr; i++) {
                            if (fields.contains(xpp.getAttributeName(i))) {
                                result.put(xpp.getAttributeName(i), xpp.getAttributeValue(i));
                            }
                        }
                    }
                    xpp.next();
                    result.put(actual, xpp.getText());
                }
            }
            eventType = xpp.next();
        }
        return result;
    }

    public static Map<String, String> parse2(String body, List<String> fields) throws XmlPullParserException, IOException {
        Map<String, String> result = new HashMap<String, String>();
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(false);

        XmlPullParser xpp = factory.newPullParser();
        xpp.setInput(new StringReader(body));
        int eventType = xpp.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_DOCUMENT) {
                System.out.println("Start document");
            } else if (eventType == XmlPullParser.START_TAG) {
                System.out.println("Start tag " + xpp.getName());
            } else if (eventType == XmlPullParser.END_TAG) {
                System.out.println("End tag " + xpp.getName());
            } else if (eventType == XmlPullParser.TEXT) {
                System.out.println("Text " + xpp.getText());
            }
            eventType = xpp.next();
        }
        return result;
    }

    public static String writeXml(Map<String, String> fields) {
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        try {
            serializer.setOutput(writer);
            serializer.startDocument("UTF-8", true);
            serializer.startTag("", "messages");
            for (String key : fields.keySet()) {
                serializer.startTag("", key);
                serializer.text(fields.get(key));
                serializer.endTag("", key);
            }
            serializer.endTag("", "messages");
            serializer.endDocument();
            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
