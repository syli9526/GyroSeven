package com.teamseven.gameframework;

import android.content.Context;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback, SensorEventListener {

    private SensorManager m_sensorManager;
    private Vibrator m_vibrator;
    private IState m_state;
    private GameViewThread m_thread;

    public GameView(Context context) {
        super(context);
        setFocusable(true);

        m_sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        m_sensorManager.registerListener(this, m_sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);

        m_vibrator= (Vibrator) context.getSystemService
                (Context. VIBRATOR_SERVICE);

        AppManager.getInstance().setGameView(this);
        AppManager.getInstance().setResources(getResources());

        getHolder().addCallback(this); // 콜백상태 지정
        m_thread = new GameViewThread(getHolder(), this);

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        m_thread.setRunning(true);
        m_thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        m_thread.setRunning(false);
        while (retry) {
            try {
                m_thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        m_state.render(canvas);
    }

    public void update() {
        m_state.update();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        m_state.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        m_state.onKeyDown(keyCode, event);

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        m_state.onSensorChanged(sensorEvent);
        invalidate();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void changeGameState(IState _state) {
        if (m_state != null)
            _state.destroy();
        _state.init();
        m_state = _state;
    }

    public Vibrator getVibrator() {
        return m_vibrator;
    }
}
