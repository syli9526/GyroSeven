package com.teamseven.gameframework;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameViewThread extends Thread {

    // 접근을 위한 변수
    private SurfaceHolder m_surfaceHolder;
    private GameView m_gameView;

    // 스레드 실행 상태 멤버 변수
    private boolean m_run = false;

    // 스레드 생성자를 통해 Holder와 Gameview 저장
    public GameViewThread(SurfaceHolder surfaceHolder, GameView gameView) {
        m_surfaceHolder = surfaceHolder;
        m_gameView = gameView;
    }

    // 스레드 동작
    @Override
    public void run() {
        Canvas _canvas; // Canvas 변수 생성
        while (m_run) { // 스레드가 돌아갈동안
            _canvas = null;
            try {
                // 현재를 update 시키고, holder가 canvas에 lock을 걸어 백에서 그려준 후,
                // 다른 변수가 접근을 못하도록 동기화를 통해 GameView에 canvas를 그려줌
                m_gameView.update();
                _canvas = m_surfaceHolder.lockCanvas(null);
                synchronized (m_surfaceHolder) {
                    m_gameView.onDraw(_canvas);
                }
            } finally {
                // canvas 가 null이 아니면 holder의 lock을 해제
                if (_canvas != null) {
                    m_surfaceHolder.unlockCanvasAndPost(_canvas);
                }
            }
        }
        //super.run();
    }

    // 스레드의 실행 여부 결정
    public void setRunning(boolean run) {
        m_run = run;
    }
}
