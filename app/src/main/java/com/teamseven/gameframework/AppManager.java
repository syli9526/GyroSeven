package com.teamseven.gameframework;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class AppManager {

    private static AppManager s_instance;

    private GameView m_gameView;
    private Resources m_resources;

    private Point size;
    private Display display;
    private Context context;

    public static AppManager getInstance() {
        if (s_instance == null) s_instance = new AppManager();
        return s_instance;
    }

    public Point getDeviceSize() {
        if(size == null){
            display = ((WindowManager) AppManager.getInstance().context.getSystemService(AppManager.getInstance().context.WINDOW_SERVICE)).getDefaultDisplay();
            size = new Point();
            display.getSize(size);
        }
        return size;
    }

    void setContext(Context context){this.context = context;}

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
