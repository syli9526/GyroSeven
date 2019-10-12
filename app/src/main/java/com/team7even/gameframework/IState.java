package com.team7even.gameframework;

import android.graphics.Canvas;
import android.hardware.SensorEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;

public interface IState {

    public void init();

    public void destroy();

    public void update();

    public void render(Canvas _canvas);

    public boolean onKeyDown(int keyCode, KeyEvent event);

    public boolean onTouchEvent(MotionEvent event);

    public void onSensorChanged(SensorEvent event);
}
