package de.czochrus.soundtouchtimer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.ToggleButton;

import de.czochrus.soundtouchtimer.api.SoundTouchApi;
import de.czochrus.soundtouchtimer.api.SoundTouchCallback;
import de.czochrus.soundtouchtimer.api.SoundTouchFields;

public class MainActivity extends AppCompatActivity implements SoundTouchCallback {
    private SoundTouchApi soundTouchApi;
    private SeekBar sb_volume;
    private Switch s_power;
    VolumeSeekBarListener volumeSeekBarListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        soundTouchApi = new SoundTouchApi(this);
        instanceFields();
    }

    private void instanceFields() {
        sb_volume = findViewById(R.id.sB_volume);
        volumeSeekBarListener = new VolumeSeekBarListener();
        sb_volume.setOnSeekBarChangeListener(volumeSeekBarListener);
        s_power = findViewById(R.id.s_power);
        setFieldValues();
    }

    private void setFieldValues() {
        soundTouchApi.reloadVolume();
        soundTouchApi.readNowPlaying();

    }

    public void volumeDown(final View view) {
        soundTouchApi.volumeDown();
    }

    public void readNowPlaying(final View view) {
        soundTouchApi.readNowPlaying();
    }

    public void volumeUp(final View view) {
        soundTouchApi.volumeUp();
    }

    public void powerOnOff(final View view) {

        soundTouchApi.powerOn();
    }

    public void switchSoundtouch(final View view) {
        boolean sleepingroom = ((ToggleButton) view).isChecked();
        if (sleepingroom) {
            soundTouchApi.changePlayerTo(SoundTouchFields.SoundTouchPlayer.SLEEPINGROOM);
        } else {
            soundTouchApi.changePlayerTo(SoundTouchFields.SoundTouchPlayer.LIVINGROOM);
        }
        setFieldValues();
    }

    @Override
    public void updateVolume(int volume) {
        sb_volume.setProgress(volume);
    }

    @Override
    public void updatePowerState(boolean powerOn) {
        s_power.setChecked(powerOn);
    }

    class VolumeSeekBarListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            soundTouchApi.setVolumeTo(seekBar.getProgress());
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            soundTouchApi.setVolumeTo(seekBar.getProgress());
        }
    }
}
