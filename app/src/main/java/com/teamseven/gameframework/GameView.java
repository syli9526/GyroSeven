package com.teamseven.gameframework;

import android.content.Context;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.appcompat.app.AppCompatActivity;

import com.teamseven.gyroseven.GameState;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;

    private IState m_state;
    private GameViewThread m_thread;

    public GameView(Context context) {
        super(context);
        setFocusable(true);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        SCREEN_WIDTH = dm.widthPixels;
        SCREEN_HEIGHT = dm.heightPixels;

        AppManager.getInstance().setGameView(this);
        AppManager.getInstance().setResources(getResources());
        AppManager.getInstance().setContext(context);

        getHolder().addCallback(this); // 콜백상태 지정
        m_thread = new GameViewThread(getHolder(), this);

        changeGameState(new GameState());
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
        while(retry) {
            try {
                m_thread.join();
                retry = false;
            } catch (InterruptedException e) {}
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

    public void changeGameState(IState _state) {
        if(m_state != null)
            _state.destroy();
        _state.init();
        m_state = _state;
    }
}
