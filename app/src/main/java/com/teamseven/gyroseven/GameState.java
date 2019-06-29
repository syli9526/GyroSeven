package com.teamseven.gyroseven;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.SensorEvent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.teamseven.gameframework.AppManager;
import com.teamseven.gameframework.CollisionManager;
import com.teamseven.gameframework.GameView;
import com.teamseven.gameframework.IState;

import java.util.ArrayList;
import java.util.Random;

public class GameState implements IState {
    private Player m_player;
    private BackGround m_background;
    private ArrayList<Enemy> m_enemylist = new ArrayList<Enemy>();

    long lastRegenEnemy = System.currentTimeMillis();
    Random randEnemy = new Random();

    @Override
    public void init() {
        m_player = new Player(AppManager.getInstance().getBitmap(R.drawable.player_sprite));
        m_background = new BackGround(0);
    }

    @Override
    public void destroy() {

    }

    @Override
    public void update() {
        long gameTime = System.currentTimeMillis();
        m_player.update(gameTime);
        m_background.update(gameTime);

        for (int i = m_enemylist.size() - 1; i >= 0; i--) {
            Enemy enemy = m_enemylist.get(i);
            enemy.update(gameTime);
            if (enemy.state == Enemy.STATE_OUT) m_enemylist.remove(i);
        }
        makeEnemy();
        checkCollision();
    }

    @Override
    public void render(Canvas _canvas) {
        m_background.draw(_canvas);
        for (Enemy enemy : m_enemylist) {
            enemy.draw(_canvas);
        }

        m_player.draw(_canvas);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return true;
    }

    public void makeEnemy() {
        if (System.currentTimeMillis() - lastRegenEnemy >= 1000) {
            lastRegenEnemy = System.currentTimeMillis();

            Enemy enemy = new Enemy_1();

            enemy.setPosition(randEnemy.nextInt(
                    GameView.SCREEN_WIDTH - enemy.getBitmap().getWidth() / enemy.getIFrames() / 2),
                    -180);

            m_enemylist.add(enemy);
        }
    }

    public void checkCollision() {
        for (int i = m_enemylist.size()-1; i >= 0; i--) {
            if (CollisionManager.checkBoxToBox(m_player.m_boundBox,
                    m_enemylist.get(i).m_boundBox)) {
                m_enemylist.remove( i);
                m_player.destroyPlayer();
                if (m_player.getLife() <= 0) {
                    //System.exit(0);
                }
            }
        }
    }
}
