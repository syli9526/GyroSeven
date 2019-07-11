package com.teamseven.gameframework;

import android.content.Context;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.teamseven.gyroseven.Constants;
import com.teamseven.gyroseven.R;

public class GameView extends SurfaceView implements SurfaceHolder.Callback, SensorEventListener {

    private SensorManager m_sensorManager; // player 이동을 위한 센서 사용
    private Vibrator m_vibrator; // 플레이어 사망시 진동을 위한 변수
    private IState m_state; // 현재 View를 보여주는 State (State 패턴)
    private GameViewThread m_thread; // sufaceView의 백그라운드에서 동작하는 스레드

    public GameView(Context context) {
        super(context);
        setFocusable(true); // 키 이벤트 포커스를 이 뷰로 맞춤

        // 센서 변수 초기화
        // 화면 기울임에 따라서 센서를 입력받음
        m_sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        m_sensorManager.registerListener(this, m_sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);

        // 진동 변수 초기화
        m_vibrator = (Vibrator) context.getSystemService
                (Context.VIBRATOR_SERVICE);

        // AppManager에 view와 resorce를 저장
        AppManager.getInstance().setGameView(this);
        AppManager.getInstance().setResources(getResources());

        getHolder().addCallback(this); // 콜백상태 지정
        m_thread = new GameViewThread(getHolder(), this); // 스레드 생성

    }

    // View가 생성되면 스레드를 실행시킴
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        m_thread.setRunning(true);
        m_thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    // 뷰가 사라질때, 스레드를 실행을 중지시키고 join을 통해 스레드 실행이후, 종료시킴
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

    // 현재 State의 rander함수를 호출하여 canvas를 통해 화면에 그려줌
    @Override
    public void onDraw(Canvas canvas) {
        m_state.render(canvas);
    }

    // 스레드를 통해 현재 State의 update함수를 호출, 게임을 지속적으로 update시킴
    public void update() {
        m_state.update();
    }

    // 화면 터치 이벤트, intro에서 Game 으로 State 전환시 사용
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        m_state.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    // 키보드 이벤트
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        m_state.onKeyDown(keyCode, event);

        return super.onKeyDown(keyCode, event);
    }

    // 센서가 변화되었을때 이벤트를 받아 State에 넘겨줌
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        m_state.onSensorChanged(sensorEvent);
        invalidate();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    // 이 함수를 통해 현재 State를 변화, 매개변수 State를 초기화하고 현 State로 설정
    public void changeGameState(IState _state, boolean flag) {
        if (m_state != null)
            _state.destroy();
        if (flag)
            _state.init();
        m_state = _state;
    }

    // 현재 State를 받아옴
    public IState getState() {
        return m_state;
    }

    // 진동 센서를 넘겨줌
    public Vibrator getVibrator() {
        return m_vibrator;
    }

    // Thread의 동작을 제어, 스레드를 run 혹은 중단 시킴
    public void setThreadRun(boolean flag) {
        m_thread.setRunning(flag);
    }
}
