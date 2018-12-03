package de.czochrus.soundtouchtimer.api;

public interface SoundTouchCallback {

    public void updateVolume(int volume);

    public void updatePowerState(boolean powerOn);
}
