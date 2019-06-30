package com.teamseven.gameframework;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.util.HashMap;

public class SoundManager {
    static private SoundManager s_instance;

    private SoundPool m_soundPool;
    private HashMap m_soundPoolMap;
    private AudioManager m_audioManager;
    private Context m_activity;

    public static SoundManager getInstance() {
        if(s_instance == null) return s_instance = new SoundManager();
        return s_instance;
    }

    public void init(Context _context) {
        m_soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC,0);
        m_soundPoolMap = new HashMap();
        m_audioManager = (AudioManager)_context.getSystemService(Context.AUDIO_SERVICE);
        m_activity = _context;
    }

    public void addSound(int _index, int _soundID) {
        int id = m_soundPool.load(m_activity, _soundID, 1);
        m_soundPoolMap.put(_index, id);
    }

    public void play(int _index) {
        float streamVolume = m_audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        streamVolume = streamVolume / m_audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        m_soundPool.play((Integer)m_soundPoolMap.get(_index), streamVolume, streamVolume, 1, 0, .1f);
    }

    public void playLooped(int _index) {
        float streamVolume = m_audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        streamVolume = streamVolume / m_audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        m_soundPool.play((Integer)m_soundPoolMap.get(_index), streamVolume, streamVolume, 1, -1, .1f);
    }


}