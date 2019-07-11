package com.teamseven.gyroseven;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.teamseven.gameframework.AppManager;
import com.teamseven.gameframework.GameView;
import com.teamseven.gameframework.IState;
import com.teamseven.gameframework.SoundManager;

public class MainActivity extends AppCompatActivity {

    private GameView gameView;
    private long backKeyPressedTime;
    private IState m_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        soundInit();
        m_state = new IntroState();
        gameView.changeGameState(m_state, true);
    }


    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    protected void onPause() {
        SoundManager.getInstance().pauseBackground();
        super.onPause();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        init();
        gameView.changeGameState(m_state, false);
        SoundManager.getInstance().playBackground();
    }

    @Override
    protected void onStop() {
        super.onStop();
        m_state = AppManager.getInstance().getGameView().getState();
        gameView.setThreadRun(false);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        m_state = AppManager.getInstance().getGameView().getState();
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "뒤로가기 버튼을 한 번 더 누르면 종료 됩니다.", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }

    }

    private void soundInit() {
        // Sound Manager에 효과음과 배경음악을 저장
        SoundManager.getInstance().initBackground(this, R.raw.background_game);
        SoundManager.getInstance().init(this);
        SoundManager.getInstance().addSound(Constants.EFFECT_START, R.raw.game_start);
        SoundManager.getInstance().addSound(Constants.EFFECT_LEVELUP, R.raw.level_up);
        SoundManager.getInstance().addSound(Constants.EFFECT_HEART, R.raw.heart);
        SoundManager.getInstance().addSound(Constants.EFFECT_MISSILE, R.raw.missile);
        SoundManager.getInstance().addSound(Constants.EFFECT_SHIELD, R.raw.shield);
        SoundManager.getInstance().addSound(Constants.EFFECT_BOMB, R.raw.bomb);
        SoundManager.getInstance().addSound(Constants.EFFECT_CLICKED, R.raw.clicked);
    }

    private void init() {

        AppManager.getInstance().setContext(this);
        AppManager.getInstance().setGame(true);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        gameView = new GameView(this);
        setContentView(gameView);

    }

}