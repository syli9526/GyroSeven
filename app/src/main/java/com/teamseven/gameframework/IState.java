package com.teamseven.gameframework;

import android.graphics.Canvas;
import android.hardware.SensorEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;

public interface IState {
    // State 패턴을 위한 인터페이스

    public void init(); // 초기화

    public void destroy(); // 종료(제거)

    public void update(); //update

    public void render(Canvas _canvas); // canvas 그리기

    public boolean onKeyDown(int keyCode, KeyEvent event); // 키보드 이벤트

    public boolean onTouchEvent(MotionEvent event); // 터치 이벤트

    public void onSensorChanged(SensorEvent event); // 센서 이벤트
}
