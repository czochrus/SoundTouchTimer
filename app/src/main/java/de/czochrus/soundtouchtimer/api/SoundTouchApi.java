package de.czochrus.soundtouchtimer.api;

import android.util.Log;

import com.apptakk.http_request.HttpRequest;
import com.apptakk.http_request.HttpRequestTask;
import com.apptakk.http_request.HttpResponse;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import static de.czochrus.soundtouchtimer.api.SoundTouchFields.*;

public class SoundTouchApi {
    private static final String TAG = "SoundTouchAPI";
    private static String BOSE_PATH_ACTUAL = BOSE_PATH_SLEEPINGROOM;
    private static int volume = VOLUME_DEFAULT;

    private static boolean power_on = false;

    SoundTouchCallback callBack = null;

    public SoundTouchApi(SoundTouchCallback callBack) {
        this.callBack = callBack;
    }

    public void changePlayerTo(SoundTouchPlayer player) {
        switch (player) {
            case LIVINGROOM:
                BOSE_PATH_ACTUAL = BOSE_PATH_LIVINGROOM;
                reloadVolume();
                break;
            case SLEEPINGROOM:
                BOSE_PATH_ACTUAL = BOSE_PATH_SLEEPINGROOM;
                reloadVolume();
                break;
        }

    }

    //<key state="$KEY_STATE" sender="$KEY_SENDER">$KEY_VALUE</key>
    public void powerOn() {
        final String requestPress = "<key state=\"press\" sender="+"Gabbo"+">POWER</key>";
        final String requestRelease = "<key state=\"release\" sender="+"Gabbo"+">POWER</key>";

        new HttpRequestTask(new HttpRequest(BOSE_PATH_ACTUAL + PATH_KEY, HttpRequest.POST, requestPress, AUTH), new HttpRequest.Handler() {
            @Override
            public void response(HttpResponse response) {
                if (response.code == 200) {
                    new HttpRequestTask(new HttpRequest(BOSE_PATH_ACTUAL + PATH_KEY, HttpRequest.POST, requestRelease, AUTH), new HttpRequest.Handler() {
                        @Override
                        public void response(HttpResponse response) {
                            if (response.code == 200) {

                                Log.d(TAG, "Request successful! release");
                            } else {
                                Log.e(TAG, "Request unsuccessful: release" + response);
                            }
                        }
                    }).execute();
                    Log.d(TAG, "Request successful! press");
                } else {
                    Log.e(TAG, "PowerOn Request unsuccessful: press" + response);
                }
            }
        }).execute();
    }

    public void volumeDown() {
        volume = Math.max(VOLUME_MIN, --volume);
        String request = RequestResponseParser.writeXml(createVolumeRequest(volume));
        new HttpRequestTask(new HttpRequest(BOSE_PATH_ACTUAL + PATH_VOLUME, HttpRequest.POST, request, AUTH), new HttpRequest.Handler() {
            @Override
            public void response(HttpResponse response) {
                if (response.code == 200) {
                    callBack.updateVolume(volume);
                    Log.d(TAG, "Request successful!");
                } else {
                    Log.e(TAG, "VolumeDown Request unsuccessful: " + response);
                }
            }
        }).execute();
    }

    public void volumeUp() {
        volume = Math.min(VOLUME_MAX, ++volume);
        String request = RequestResponseParser.writeXml(createVolumeRequest(volume));
        new HttpRequestTask(new HttpRequest(BOSE_PATH_ACTUAL + PATH_VOLUME, HttpRequest.POST, request, AUTH), new HttpRequest.Handler() {
            @Override
            public void response(HttpResponse response) {
                if (response.code == 200) {
                    callBack.updateVolume(volume);
                    Log.d(TAG, "Request successful!");
                } else {
                    Log.e(TAG, "VolumeUp Request unsuccessful: " + response);
                }
            }
        }).execute();
    }

    public void setVolumeTo(int vol) {
        volume = vol;
        String request = RequestResponseParser.writeXml(createVolumeRequest(volume));
        new HttpRequestTask(new HttpRequest(BOSE_PATH_ACTUAL + PATH_VOLUME, HttpRequest.POST, request, AUTH), new HttpRequest.Handler() {
            @Override
            public void response(HttpResponse response) {
                if (response.code == 200) {
                    callBack.updateVolume(volume);
                    Log.d(TAG, "Request successful!");
                } else {
                    Log.e(TAG, "SetVolumeTo Request unsuccessful: " + response);
                }
            }
        }).execute();
    }

    public void reloadVolume() {
        new HttpRequestTask(new HttpRequest(BOSE_PATH_ACTUAL + PATH_VOLUME, HttpRequest.GET, null, "gabbo"), new HttpRequest.Handler() {
            @Override
            public void response(HttpResponse response) {
                if (response.code == 200) {
                    try {
                        Log.i(TAG,response.body);
                        Map<String, String> parse = RequestResponseParser.parse(response.body, Arrays.asList(TAG_ACTUALVOLUME));
                        volume = Integer.parseInt(parse.get(TAG_ACTUALVOLUME));
                        callBack.updateVolume(volume);
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e(TAG, "ReloadVolume Request unsuccessful: " + BOSE_PATH_ACTUAL+ response);
                }
            }
        }).execute();
    }

    public void readNowPlaying() {
        new HttpRequestTask(new HttpRequest(BOSE_PATH_ACTUAL + PATH_NOW_PLAYING, HttpRequest.GET, null, "gabbo"), new HttpRequest.Handler() {
            @Override
            public void response(HttpResponse response) {
                if (response.code == 200) {
                    try {
                        Map<String, String> parse = RequestResponseParser.parse(response.body, Arrays.asList(TAG_NOW_PLAYING, TAG_SOURCE));
                        if (STANDBY.equals(parse.get(TAG_SOURCE))) {
                            setPowerTo(false);
                        } else {
                            setPowerTo(true);
                        }
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e(TAG, "readNowPlaying Request unsuccessful: "+BOSE_PATH_ACTUAL + response);
                }
            }
        }).execute();
    }

    private Map<String, String> createVolumeRequest(int volume) {
        Map<String, String> request = new LinkedHashMap<String, String>();
        request.put(TAG_ACTUALVOLUME, Integer.toString(volume));
        return request;
    }

    public void setPowerTo(boolean powerOn) {
        callBack.updatePowerState(powerOn);
    }
}
