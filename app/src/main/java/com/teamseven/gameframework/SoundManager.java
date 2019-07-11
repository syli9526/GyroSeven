package com.teamseven.gameframework;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import java.io.IOException;
import java.util.HashMap;

public class SoundManager {
    static private SoundManager s_instance; // 싱글톤 패턴을 위한 SoundManager 객체 생성

    private MediaPlayer m_mediaPlayer; // 배경음악 저장
    private SoundPool m_soundPool; // 효과음 저장
    private HashMap m_soundPoolMap; // 각 효과음을 Hash Map으로 저장
    private AudioManager m_audioManager; // 안드로이드 소리 설정
    private Context m_activity;

    // 싱글톤 인스턴스 객체를 반환, 이를 통해 SoundManager 클래스에 접근
    public static SoundManager getInstance() {
        if (s_instance == null) return s_instance = new SoundManager();
        return s_instance;
    }

    // 안드로이드 플레이어 설정
    public void initBackground(Context _context, int _soundID) {
        m_mediaPlayer = MediaPlayer.create(_context, _soundID);
        m_mediaPlayer.setVolume(100,100);
    }

    public void setBackgroundVolume(int _volume) {
        m_mediaPlayer.setVolume(_volume,_volume);
    }

    // 각 객체 생성
    public void init(Context _context) {
        m_soundPool = new SoundPool(8, AudioManager.STREAM_MUSIC, 0);
        m_soundPoolMap = new HashMap();
        m_audioManager = (AudioManager) _context.getSystemService(Context.AUDIO_SERVICE);
        m_activity = _context;
    }

    // 효과음들을 받아와 저장하고, 그 id를 해시맵에 저장
    public void addSound(int _index, int _soundID) {
        int id = m_soundPool.load(m_activity, _soundID, 1);
        m_soundPoolMap.put(_index, id);
    }

    // 배경음악 실행
    public void playBackground() {
        m_mediaPlayer.start();

    }

    // 배경음악 중지
    public void stopBackground() {
        m_mediaPlayer.stop();
        try {
            m_mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 배경음악 일시정지
    public void pauseBackground(){
        m_mediaPlayer.pause();
    }

    public boolean isPlayingBackground() {
        return m_mediaPlayer.isPlaying();
    }

    // index를 받아와 HashMap에서 해당하는 효과음을 불러와 SoundPool에서 1번만 재생
    public void play(int _index) {
        float streamVolume = m_audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        streamVolume = streamVolume / m_audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        m_soundPool.play((Integer) m_soundPoolMap.get(_index), streamVolume, streamVolume, 1, 0, 1.0f);
    }

    public void playLooped(int _index) {
        float streamVolume = m_audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        streamVolume = streamVolume / m_audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        m_soundPool.play((Integer) m_soundPoolMap.get(_index), streamVolume, streamVolume, 1, -1, 1.0f);
    }

}
