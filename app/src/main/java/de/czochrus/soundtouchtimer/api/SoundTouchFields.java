package de.czochrus.soundtouchtimer.api;

public final class SoundTouchFields {

    protected static final String BOSE_PATH_LIVINGROOM = "http://192.168.0.94:8090";
    protected static final String BOSE_PATH_SLEEPINGROOM = "http://192.168.0.101:8090";

    protected static final String AUTH = "gabbo";
    protected static final int VOLUME_MAX = 100;
    protected static final int VOLUME_MIN = 0;
    protected static final int VOLUME_DEFAULT = 10;
    protected static final String STANDBY = "STANDBY";

    // Paths
    protected static final String PATH_VOLUME = "/volume";
    protected static final String PATH_NOW_PLAYING = "/now_playing";
    protected static final String PATH_KEY = "/key";

    protected static final String TAG_ACTUALVOLUME = "actualvolume";
    protected static final String TAG_NOW_PLAYING = "nowPlaying";
    protected static final String TAG_SOURCE = "source";

    public enum SoundTouchPlayer {
        LIVINGROOM, SLEEPINGROOM;
    }
}
