package com.teamseven.gyroseven;

import android.graphics.Canvas;

import android.hardware.SensorEvent;

import android.view.KeyEvent;
import android.view.MotionEvent;

import com.teamseven.gameframework.AppManager;
import com.teamseven.gameframework.CollisionManager;
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
        for (int i = m_enemylist.size() - 1; i >= 0; i--){
            Enemy enemy = m_enemylist.get(i);
            enemy.move(m_player.getX(), m_player.getY());
            if(enemy.state == Constants.STATE_OUT) m_enemylist.remove(i);
            enemy.update();
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

        int posX[] = {randEnemy.nextInt(AppManager.getInstance().getDeviceSize().x), AppManager.getInstance().getDeviceSize().x, randEnemy.nextInt(AppManager.getInstance().getDeviceSize().x), 0,randEnemy.nextInt(AppManager.getInstance().getDeviceSize().x)};
        int posY[] = {0, randEnemy.nextInt(AppManager.getInstance().getDeviceSize().y), AppManager.getInstance().getDeviceSize().y, randEnemy.nextInt(AppManager.getInstance().getDeviceSize().y),0};

        if (System.currentTimeMillis() - lastRegenEnemy >= 500) {
            lastRegenEnemy = System.currentTimeMillis();

            Enemy crw[] = new Enemy_1[4];

            for (int i = 0; i < crw.length; i++) {
                crw[i] = new Enemy_1();
                crw[i].movePattern = i + randEnemy.nextInt(1);
                if (posX[i] == 0)
                    crw[i].setPosition(-crw[i].width, posY[i]);
                else if (posY[i] == 0)
                    crw[i].setPosition(posX[i], -crw[i].height);
                else
                    crw[i].setPosition(posX[i], posY[i]);
                m_enemylist.add(crw[i]);
            }


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
