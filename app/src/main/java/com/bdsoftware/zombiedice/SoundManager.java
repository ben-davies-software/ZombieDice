package com.bdsoftware.zombiedice;

import android.content.Context;
import android.media.MediaPlayer;

public class SoundManager {

    private MediaPlayer diceRollPlayer;
    private MediaPlayer brainCollectPlayer;
    private MediaPlayer shotgunBlastPlayer;

    public SoundManager(Context context) {
        diceRollPlayer = MediaPlayer.create(context, R.raw.dice_roll);
        brainCollectPlayer = MediaPlayer.create(context, R.raw.brain_collect);
        shotgunBlastPlayer = MediaPlayer.create(context, R.raw.shotgun_blast);
    }

    public void playDiceRoll() {
        playSound(diceRollPlayer);
    }

    public void playBrainCollect() {
        playSound(brainCollectPlayer);
    }

    public void playShotgunBlast() {
        playSound(shotgunBlastPlayer);
    }

    private void playSound(MediaPlayer player) {
        if (player != null) {
            if (player.isPlaying()) {
                player.seekTo(0);
            }
            player.start();
        }
    }

    // Call this when the activity is destroyed to free up memory
    public void release() {
        if (diceRollPlayer != null) {
            diceRollPlayer.release();
            diceRollPlayer = null;
        }
        if (brainCollectPlayer != null) {
            brainCollectPlayer.release();
            brainCollectPlayer = null;
        }
        if (shotgunBlastPlayer != null) {
            shotgunBlastPlayer.release();
            shotgunBlastPlayer = null;
        }
    }
}