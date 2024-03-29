package com.team7even.gyroseven;

import android.graphics.Canvas;
import android.hardware.SensorEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.team7even.gameframework.AppManager;
import com.team7even.gameframework.IState;
import com.team7even.gameframework.SoundManager;

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
        AppManager.getInstance().getGameView().changeGameState(new GameState());
        SoundManager.getInstance().play(Constants.EFFECT_START);
        return true;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }
}
