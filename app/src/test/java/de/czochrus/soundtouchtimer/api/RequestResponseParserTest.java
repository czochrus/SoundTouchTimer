package de.czochrus.soundtouchtimer.api;

import org.junit.Test;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;
import static de.czochrus.soundtouchtimer.api.SoundTouchFields.*;

public class RequestResponseParserTest {
    private String xml1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><nowPlaying deviceID=\"10CEA99B0C31\" source=\"STANDBY\"><ContentItem source=\"STANDBY\" isPresetable=\"true\" /></nowPlaying>";
    private String xml2 = "<volume deviceID=\"0123456\">\n" + "  <targetvolume>10</targetvolume>\n" + "  <actualvolume>20</actualvolume>\n" + "  <muteenabled>false</muteenabled>\n" + "</volume>";

    @Test
    public void test_parseXml_Attribute_withResult() throws IOException, XmlPullParserException {
        Map<String, String> result = RequestResponseParser.parse3(xml1, Arrays.asList(TAG_NOW_PLAYING, TAG_SOURCE));
        assertThat(result.isEmpty()).isFalse();
        assertThat(result.get(TAG_SOURCE)).isEqualTo("STANDBY");
    }

    @Test
    public void test_parseXml_Attribute_withoutResult() throws IOException, XmlPullParserException {

        Map<String, String> result = RequestResponseParser.parse(xml1, Arrays.asList(TAG_NOW_PLAYING, TAG_SOURCE + "f"));
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    public void test_parseXml_Text_withResult() throws IOException, XmlPullParserException {

        Map<String, String> result = RequestResponseParser.parse(xml1, Arrays.asList(TAG_ACTUALVOLUME));
        assertThat(result.isEmpty()).isFalse();
        assertThat(result.get(TAG_ACTUALVOLUME)).isEqualTo("20");
    }

    @Test
    public void test_parseXml_Text_withoutResult() throws IOException, XmlPullParserException {

        Map<String, String> result = RequestResponseParser.parse(xml1, Arrays.asList(TAG_ACTUALVOLUME));
        assertThat(result.isEmpty()).isTrue();
    }
}
