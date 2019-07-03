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

    private static AppManager s_instance;

    private GameView m_gameView;
    private Resources m_resources;
    private EndDialog m_dialog;
    private Context m_context;
    private View.OnClickListener m_main;
    private View.OnClickListener m_retry;

    private boolean game = false;
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

    public View.OnClickListener getMainListener() {
        return m_main;
    }
    public void setMainListener(View.OnClickListener m_main) {
        this.m_main = m_main;
    }

    public View.OnClickListener getRetryListener() {
        return m_retry;
    }

    public void setRetryListener(View.OnClickListener m_retry) {
        this.m_retry = m_retry;
    }

    public EndDialog getDialog() {
        Log.v("Dialog","get dialog");
        return m_dialog;
    }

    public void setDialog(EndDialog m_dialog) {
        this.m_dialog = m_dialog;
        Log.v("Dialog","set dialog");
    }


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