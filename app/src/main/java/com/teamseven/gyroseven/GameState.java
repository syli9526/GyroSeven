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
    String m_level = "";

    private Player m_player;
    private BackGround m_background;
    private ArrayList<Enemy> m_enemylist = new ArrayList<Enemy>();
    private ArrayList<Enemy> m_enemylist_2 = new ArrayList<Enemy>();
    private Heart m_heart;

    private int frequency = 3000;
    private int level = 1;
    long lastRegenEnemy = System.currentTimeMillis();
    long lastLevelUp = System.currentTimeMillis();


    Random randEnemy = new Random();

    float m_roll;
    float m_pitch;

    @Override
    public void init() {
        m_player = new Player(AppManager.getInstance().getBitmap(R.drawable.player_sprite));
        m_background = new BackGround(0);
        m_heart = new Heart();
    }

    @Override
    public void destroy() {

    }

    @Override
    public void update() {
        long gameTime = System.currentTimeMillis();

        m_player.update(gameTime);
        m_player.move(m_pitch, m_roll);

        if (level < 6) {
            if (gameTime - lastLevelUp >= 20000) {
                lastLevelUp = gameTime;
                level++;
                frequency -= 500;
                for (Enemy enemy : m_enemylist) enemy.speedUp(1f);
            }
        } else {
            if (gameTime - lastLevelUp >= 1000) {
                lastLevelUp = gameTime;
                for (Enemy enemy : m_enemylist) enemy.speedUp(0.1f);
            }

        }

        for (int i = m_enemylist.size() - 1; i >= 0; i--) {
            Enemy enemy = m_enemylist.get(i);
            enemy.move(m_player.getX(), m_player.getY());
            if (enemy.state == Constants.STATE_OUT) m_enemylist.remove(i);
            enemy.update();
        }

        for (int i = m_enemylist_2.size() - 1; i >= 0; i--) {
            Enemy enemy = m_enemylist_2.get(i);
            enemy.move(m_player.getX(), m_player.getY());
            if (enemy.state == Constants.STATE_OUT) m_enemylist_2.remove(i);
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
        for (Enemy enemy : m_enemylist_2) {
            enemy.draw(_canvas);
        }
        m_player.draw(_canvas);

        for (int i = 0; i < m_player.getLife(); i++) {
            m_heart.setPosition(5 + m_heart.getBitmap().getWidth() * i, 5);
            m_heart.draw(_canvas);
        }


        Paint p = new Paint();
        p.setTextSize(40);
        p.setColor(Color.BLACK);
        m_level = "Level :" + Integer.toString(level);
        _canvas.drawText(m_str1, 0, 140, p);
        _canvas.drawText(m_str2, 0, 180, p);
        _canvas.drawText(m_level, 0, 220, p);
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
            Enemy crw[] = new Enemy_1[4];

            for (int i = 0; i < crw.length; i++) {
                crw[i] = new Enemy_1();
                crw[i].x_weight = randEnemy.nextInt(3 + level);
                crw[i].y_weight = randEnemy.nextInt(3 + level);
                crw[i].movePattern = i + randEnemy.nextInt(1);

                if (posX[i] == 0) crw[i].setPosition(-crw[i].getBitmap().getWidth(), posY[i]);
                else if (posY[i] == 0) crw[i].setPosition(posX[i], -crw[i].getBitmap().getHeight());
                else crw[i].setPosition(posX[i], posY[i]);

                m_enemylist.add(crw[i]);
            }

            while (m_enemylist_2.size() < level) {
                Enemy enemy = new Enemy_2();
                int idx = randEnemy.nextInt(3);
                enemy.movePattern = 4;
                enemy.speedUp(randEnemy.nextInt(4));
                enemy.setPosition(posX[idx], posY[idx]);
                m_enemylist_2.add(enemy);
            }
        }

    }

    public void checkCollision() {
        for (int i = m_enemylist.size() - 1; i >= 0; i--) {
            if (CollisionManager.checkCircleToCircle(m_player.m_boundBox,
                    m_enemylist.get(i).m_boundBox)) {
                m_enemylist.remove(i);
                m_player.damagePlayer();
                if (m_player.getLife() <= 0) {
                    //System.exit(0);
                }
            }
        }

        for (int i = m_enemylist_2.size() - 1; i >= 0; i--) {
            if (CollisionManager.checkCircleToCircle(m_player.m_boundBox,
                    m_enemylist_2.get(i).m_boundBox)) {
                m_enemylist_2.remove(i);
                m_player.damagePlayer();
                if (m_player.getLife() <= 0) {
                    //System.exit(0);
                }
            }
        }
    }
}
