package com.bnr.android.beatbox;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BeatBox {
    private List<Sound> mSounds = new ArrayList<>();
    private SoundPool mSoundPool;
    private AssetManager mAssets;
    private static final String TAG = "BeatBox";
    private static final String SOUNDS_FOLDER = "sample_sounds";
    private static final int MAX_SOUNDS = 5;

    public BeatBox(Context context){
        mAssets = context.getAssets();
        mSoundPool = new SoundPool(MAX_SOUNDS,AudioManager.STREAM_MUSIC,0);
        loadSounds();
    }

    private void loadSounds(){
        String[] soundNames;

        try{
            soundNames = mAssets.list(SOUNDS_FOLDER);
            Log.wtf(TAG,"------- FOUND "+soundNames.length + " sounds");
        } catch(IOException e){
            e.printStackTrace();
            return;
        }

        for (String fileName : soundNames) {
            try {
                String assetPath = SOUNDS_FOLDER + "/" + fileName;
                Sound sound = new Sound(assetPath);
                load(sound);
                mSounds.add(sound);

            } catch (IOException ioe) {
                ioe.printStackTrace();
                Log.wtf(TAG, "------COULD NOT LOAD SOUND" + fileName, ioe);
            }
        }
    }

    public void play(Sound sound){
        Integer soundId = sound.getSoundId();
        if (soundId == null){
            return;
        }
        mSoundPool.play(soundId,1.0f,1.0f,1,0,1.0f);
    }

    public void release(){
        mSoundPool.release();
    }

    private void load(Sound sound) throws IOException{
        AssetFileDescriptor afd = mAssets.openFd(sound.getAssetPath());
        int soundId = mSoundPool.load(afd,1);
        sound.setSoundId(soundId);

    }

    public List<Sound> getSounds(){
        return mSounds;
    }




}
