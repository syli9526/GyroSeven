package com.teamseven.gameframework;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.teamseven.gyroseven.EndDialog;

public class AppManager {

    private static AppManager s_instance; // 싱글톤을 위한 instance 객체 선언
    private GameView m_gameView; // GameView에 접근을 위한 객체 선언
    private Resources m_resources; // Resource 클래스 접근을 위한 객체 선언
    private Context m_context; // Context 클래스 접근을 위한 객체 선언

    private boolean game = false; // 게임이 종료된것을 flag로 관리
    private Point size; // 실행 화면의 크기를 저장하는 Point 변수
    private DisplayMetrics displayMetrics; // 화면크기를 받아오기 위한 변수


    // 싱글톤 인스턴스 객체를 반환, 이를 통해 AppManager 클래스에 접근
    public static AppManager getInstance() {
        if (s_instance == null) s_instance = new AppManager();
        return s_instance;
    }

    // 디바이스 화면 크기에 대한 값을 계산하고 크키 값을 return
    public Point getDeviceSize() {

        if (size == null) { //첫실행 한번만 디바이스 사이즈를 구해오기 위함
            displayMetrics = m_resources.getDisplayMetrics();
            size = new Point();
            size.x = displayMetrics.widthPixels;
            size.y = displayMetrics.heightPixels;

        }
        return size;
    }

    /**
     *
     * 각 변수의 get/set 메소드
     */
    public Context getContext() {
        return m_context;
    }

    public void setContext(Context m_context) {
        this.m_context = m_context;
    }

    public boolean isGame() {
        return game;
    }

    public void setGame(boolean game) {
        this.game = game;
    }
    void setGameView(GameView _gameView) {
        m_gameView = _gameView;
    }

    void setResources(Resources _resources) {
        m_resources = _resources;
    }

    public GameView getGameView() {
        return m_gameView;
    }

    public Resources getResources() {
        return m_resources;
    }

    public Bitmap getBitmap(int r) {
        return BitmapFactory.decodeResource(m_resources, r);
    }
}