package com.teamseven.gameframework;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.DisplayMetrics;

public class AppManager {

    private static AppManager s_instance;

    private GameView m_gameView;
    private Resources m_resources;

    private Point size;
    private DisplayMetrics displayMetrics;


    public static AppManager getInstance() {
        if (s_instance == null) s_instance = new AppManager();
        return s_instance;
    }

    public Point getDeviceSize() {

        if (size == null) { //첫실행 한번만 디바이스 사이즈를 구해오기 위함
            displayMetrics = m_resources.getDisplayMetrics();
            size = new Point();
            size.x = displayMetrics.widthPixels;
            size.y = displayMetrics.heightPixels;

        }
        return size;
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
