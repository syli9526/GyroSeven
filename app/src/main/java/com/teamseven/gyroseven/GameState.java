package com.teamseven.gyroseven;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.teamseven.gameframework.AppManager;
import com.teamseven.gameframework.CollisionManager;
import com.teamseven.gameframework.GameView;
import com.teamseven.gameframework.IState;

import java.util.ArrayList;
import java.util.Random;

public class GameState implements IState {
    String m_str1 = "";
    String m_str2 = "";

    private Player m_player;
    private BackGround m_background;
    private ArrayList<Enemy> m_enemylist = new ArrayList<Enemy>();

    long lastRegenEnemy = System.currentTimeMillis();
    Random randEnemy = new Random();

    private int cnt = 0;
    private int frequency = 1000;

    float m_roll;
    float m_pitch;

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
        m_player.move(m_pitch, m_roll);
        //m_background.update(gameTime);

        for (int i = m_enemylist.size() - 1; i >= 0; i--) {
            Enemy enemy = m_enemylist.get(i);
            enemy.move(m_player.getX(), m_player.getY());
            if (enemy.state == Constants.STATE_OUT) m_enemylist.remove(i);
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

        Paint p = new Paint();
        p.setTextSize(40);
        p.setColor(Color.BLACK);
        _canvas.drawText(m_str1, 0, 140, p);
        _canvas.drawText(m_str2, 0, 180, p);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        synchronized (this) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_ORIENTATION:
                    m_pitch = event.values[1];
                    m_roll = event.values[2];
                    m_str1 = "Pitch   : " + Integer.toString((int) m_pitch);
                    m_str2 = "Roll    : " + Integer.toString((int) m_roll);

                    if (m_pitch > 90.0f) {
                        m_pitch = 90.0f;
                    } else if (m_pitch < -90.0f) {
                        m_pitch = -90.0f;
                    }

                    if (m_roll > 90.0f) {
                        m_roll = 90.0f;
                    } else if (m_roll < -90.0f) {
                        m_roll = -90.0f;
                    }
            }
        }
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
        int posX[] = {randEnemy.nextInt(AppManager.getInstance().getDeviceSize().x), AppManager.getInstance().getDeviceSize().x, randEnemy.nextInt(AppManager.getInstance().getDeviceSize().x), 0, randEnemy.nextInt(AppManager.getInstance().getDeviceSize().x)};
        int posY[] = {0, randEnemy.nextInt(AppManager.getInstance().getDeviceSize().y), AppManager.getInstance().getDeviceSize().y, randEnemy.nextInt(AppManager.getInstance().getDeviceSize().y), 0};

        if (System.currentTimeMillis() - lastRegenEnemy >= frequency) {
            lastRegenEnemy = System.currentTimeMillis();

            cnt++;
            Enemy crw[] = new Enemy_1[4];
            for (int i = 0; i < crw.length; i++) {
                crw[i] = new Enemy_1();

                crw[i].x_weight = randEnemy.nextInt(3);
                crw[i].y_weight = randEnemy.nextInt(3);
                crw[i].movePattern = i + randEnemy.nextInt(1);
                if (cnt % 5 == 0){
                    frequency --;
                    crw[i].speed = crw[i].speed +1f;
                }

                if (posX[i] == 0) crw[i].setPosition(-crw[i].getBitmap().getWidth(), posY[i]);
                else if (posY[i] == 0) crw[i].setPosition(posX[i], -crw[i].getBitmap().getHeight());
                else crw[i].setPosition(posX[i], posY[i]);
                m_enemylist.add(crw[i]);
            }

            if (cnt % 5 == 0) {
                Enemy enemy = new Enemy_2();
                int idx = randEnemy.nextInt(3);
                enemy.movePattern = 4;
                enemy.setPosition(posX[idx], posY[idx]);
                m_enemylist.add(enemy);
            }
        }

    }

    public void checkCollision() {
        for (int i = m_enemylist.size() - 1; i >= 0; i--) {
            if (CollisionManager.checkCircleToCircle(m_player.m_boundBox,
                    m_enemylist.get(i).m_boundBox)) {
                m_enemylist.remove(i);
                m_player.destroyPlayer();
                if (m_player.getLife() <= 0) {
                    //System.exit(0);
                }
            }
        }
    }
}
