package com.teamseven.gyroseven;

import android.graphics.Canvas;
import android.hardware.SensorEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.teamseven.gameframework.AppManager;
import com.teamseven.gameframework.IState;
import com.teamseven.gameframework.SoundManager;

public class IntroState implements IState {

    private BackGround m_background;

    @Override
    public void init() {
        m_background = new BackGround(0);
    }

    @Override
    public void destroy() {

    }

    @Override
    public void update() {
        long gameTime = System.currentTimeMillis();
        m_background.update(gameTime);
    }

    @Override
    public void render(Canvas _canvas) {
        m_background.draw(_canvas);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        GameState gameState = new GameState();
        AppManager.getInstance().getGameView().changeGameState(gameState, true);
        SoundManager.getInstance().play(Constants.EFFECT_START);
        return true;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }
}
