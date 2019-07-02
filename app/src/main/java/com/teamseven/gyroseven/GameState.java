package com.teamseven.gyroseven;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.teamseven.gameframework.AppManager;
import com.teamseven.gameframework.CollisionManager;
import com.teamseven.gameframework.IState;

import java.util.ArrayList;
import java.util.Random;

public class GameState implements IState {

    protected Player m_player;
    protected BackGround m_background;
    protected ArrayList<Enemy> m_enemylist = new ArrayList<Enemy>();
    protected ArrayList<Item> m_itemlist = new ArrayList<Item>();
    protected Heart m_heart;
    protected Alert m_alert;
    protected ArrayList<Score> m_score = new ArrayList<>();

    private boolean event = false;
    private int frequency = 3000;
    private int level = 1;
    private float timeWeight = 0.0f;
    private int numOfEmemy_2 = 0;
    private long lastScore = 0;
    private boolean intro = true;

    // 각 마지막 갱신한 시간
    private long lastEvent;
    private long lastUpdateScore = System.currentTimeMillis();
    private long lastRegenEnemy = System.currentTimeMillis();
    private long lastLevelUp = System.currentTimeMillis();
    private long lastReagenItem = System.currentTimeMillis();
    private long lastPlayerDamage = System.currentTimeMillis();

    private Random randItem = new Random();
    private Random randEnemy = new Random();

    private float m_roll;
    private float m_pitch;

    @Override
    public void init() {
        m_player = new Player(AppManager.getInstance().getBitmap(R.drawable.player_sprite));
        m_background = new BackGround(level);
        m_heart = new Heart();
        m_alert = new Alert();
        Score s = new Score();
        s.update(0);
        m_score.add(s);
    }

    @Override
    public void destroy() {

    }

    @Override
    public void update() {
        long gameTime = System.currentTimeMillis();

        if (intro) {
            m_player.setSpeed(0.5f);
            m_player.update(gameTime);
            m_player.move(0, -90);
            if (m_player.getCenterY() >= AppManager.getInstance().getDeviceSize().y / 4 * 3) {
                event = true;
            } else if (m_player.getCenterY() > AppManager.getInstance().getDeviceSize().y / 2) {
                event = true;
                m_alert.setBitmap(AppManager.getInstance().getBitmap(R.drawable.start));
            } else if (m_player.getCenterY() <= AppManager.getInstance().getDeviceSize().y / 2) {
                intro = false;
                m_alert.setBitmap(AppManager.getInstance().getBitmap(R.drawable.speed_up));
                m_alert.setPosition((AppManager.getInstance().getDeviceSize().x - m_alert.getBitmap().getWidth()) / 2,
                        (AppManager.getInstance().getDeviceSize().y - m_alert.getBitmap().getHeight()) - 10);
                m_player.setSpeed(8);
            }

        } else {
            int cnt = 0;
            m_background.changeBackGround(level);
            m_player.update(gameTime);

            for (int i = m_enemylist.size() - 1; i >= 0; i--) {
                Enemy enemy = m_enemylist.get(i);
                enemy.move(m_player.getCenterX(), m_player.getCenterY());
                if (enemy.grade == 20 && enemy.state != Constants.STATE_OUT) cnt++;
                if (enemy.state == Constants.STATE_OUT) m_enemylist.remove(i);

                enemy.update();
            }

            for (int i = m_itemlist.size() - 1; i >= 0; i--) {
                Item item = m_itemlist.get(i);
                if (item.itemState != Constants.STATE_ITEM_FINISHED) {
                    item.update(gameTime, this);
                } else {
                    m_itemlist.remove(i);
                }
            }

            if (m_player.playerState == Constants.STATE_NORMAL) {
                m_player.move(m_pitch, m_roll);

                m_background.changeBackGround(level);

                if (gameTime - lastUpdateScore >= 500) {
                    lastUpdateScore = gameTime;
                    lastScore += level;
                }

                if (level < 6) {
                    if (gameTime - lastLevelUp >= 20000) {
                        lastEvent = System.currentTimeMillis();
                        event = true;
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

                numOfEmemy_2 = cnt;

                updateScore();
                makeEnemy();
                makeItem(gameTime);
                calculateLevel(gameTime);
            }
            checkCollision();
        }
    }

    @Override
    public void render(Canvas _canvas) {

        m_background.draw(_canvas);
        for (Enemy enemy : m_enemylist) {
            enemy.draw(_canvas);
        }
        for (Item item : m_itemlist) {
            item.draw(_canvas);
        }
        for (int i = m_score.size() - 1; i >= 0; i--) {
            m_score.get(i).setPosition(AppManager.getInstance().getDeviceSize().x - m_score.get(i).getBitmapWidth() * (i + 1) - 10, 5);
            m_score.get(i).draw(_canvas);
        }
        for (int i = 0; i < m_player.getLife(); i++) {
            m_heart.setPosition(5 + m_heart.getBitmap().getWidth() * i, 5);
            m_heart.draw(_canvas);
        }

        m_player.draw(_canvas);

        if (event) {
            m_alert.draw(_canvas);
            if (System.currentTimeMillis() - lastEvent >= 1000)
                event = false;
        }

        Paint p = new Paint();
        p.setTextSize(40);
        p.setColor(Color.WHITE);
        String m_level = "Level : " + Integer.toString(level);
        String m_score = "Number : " + Long.toString(lastScore);
        _canvas.drawText(m_level, 0, 220, p);
        _canvas.drawText(m_score, 0, 260, p);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        synchronized (this) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_ORIENTATION:
                    m_pitch = event.values[1];
                    m_roll = event.values[2];

                    if (m_pitch > 90.0f) {
                        m_pitch = 90.0f;
                    } else if (m_pitch < -90.0f) {
                        m_pitch = -90.0f;
                    }

                    m_roll -= 18.0f; // 센서 기준을 약간 기울이게 조정
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

    public void calculateLevel(long gameTime) {

        // 스코어 계산
        if (gameTime - lastUpdateScore >= 500) {
            lastUpdateScore = gameTime;
            lastScore += level;
        }

        // 레벨 계산
        if (level < 6) {
            if (gameTime - lastLevelUp >= 20000) {
                lastEvent = System.currentTimeMillis();
                lastLevelUp = gameTime;
                event = true;
                level++;
                frequency -= 500;
                for (Enemy enemy : m_enemylist) enemy.speedUp(1f);
            }
        } else {
            if (gameTime - lastLevelUp >= 1000) {
                lastLevelUp = gameTime;
                timeWeight += 0.002f;
                for (Enemy enemy : m_enemylist) enemy.speedUp(timeWeight);
            }
        }
    }

    public void makeItem(long gameTime) {
        int itemNumber = randItem.nextInt(Constants.ITEM_COUNT);
        //int itemNumber = 3;
        if (gameTime - lastReagenItem >= 2000) {
            lastReagenItem = gameTime;

            Item newItem = new Item(null);

            if (itemNumber == Constants.ITEM_HEART) {
                newItem = new Item_Heart();
            } else if (itemNumber == Constants.ITEM_MISSILE) {
                newItem = new Item_Missile();
            } else if (itemNumber == Constants.ITEM_SHIELD) {
                newItem = new Item_Shield();
            } else if (itemNumber == Constants.ITEM_BOMB) {
                newItem = new Item_Bomb();
            }

            newItem.setPosition(randEnemy.nextInt((AppManager.getInstance().getDeviceSize().x) - newItem.getBitmapWidth()),
                    randEnemy.nextInt((AppManager.getInstance().getDeviceSize().y) - newItem.getBitmapHeight()));
            m_itemlist.add(newItem);
        }
    }

    public void updateScore() {

        long num = lastScore;
        int idx = 0;
        while (num / 10 != 0) {
            if (idx >= m_score.size()) makeScore(num % 10);
            else m_score.get(idx).update(num % 10);
            num = num / 10;
            idx++;

        }
        if (idx >= m_score.size()) makeScore(num % 10);
        else m_score.get(idx).update(num % 10);

    }

    public void makeScore(long num) {
        Score s = new Score();
        s.update(num);
        m_score.add(s);
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

            while (numOfEmemy_2 < level) {
                Enemy enemy = new Enemy_2();
                int idx = randEnemy.nextInt(4);
                enemy.movePattern = 4;
                enemy.speedUp(randEnemy.nextInt(4));
                enemy.setPosition(posX[idx], posY[idx]);
                m_enemylist.add(enemy);
                numOfEmemy_2++;
            }
        }

    }

    public void checkCollision() {
        long gameTime = System.currentTimeMillis();

        if (m_player.playerState == Constants.STATE_NORMAL) {
            if (gameTime - lastPlayerDamage >= m_player.getDamagedTime()) {
                m_player.setTwinkle(false);
                m_player.setCanDamaged(true);
                for (int i = m_enemylist.size() - 1; i >= 0; i--) {
                    Rect resize = new Rect();
                    resize.set(m_player.m_boundBox.left + 10,
                            m_player.m_boundBox.top + 10,
                            m_player.m_boundBox.right - 10,
                            m_player.m_boundBox.bottom - 10);
                    if (CollisionManager.checkCircleToCircle(
                            resize, m_enemylist.get(i).m_boundBox)) {
                        m_enemylist.remove(i);
                        m_player.damagePlayer();
                        if (m_player.getLife() <= 0) {
                            m_player.die();
                            AppManager.getInstance().getGameView().getVibrator().vibrate(300);
                        } else
                            AppManager.getInstance().getGameView().getVibrator().vibrate(100);

                        lastPlayerDamage = gameTime;
                        m_player.setCanDamaged(false);
                    }
                }
            }
        }

        for (int i = m_itemlist.size() - 1; i >= 0; i--) {
            if (m_itemlist.get(i).itemState == Constants.STATE_ITEM_MADE) {
                if (CollisionManager.checkCircleToCircle(
                        m_player.m_boundBox, m_itemlist.get(i).m_boundBox)) {
                    m_itemlist.get(i).actionItem(this);
                }
            }
        }

        for (int i = m_enemylist.size() - 1; i >= 0; i--) {
            for (int j = m_itemlist.size() - 1; j >= 0; j--) {

                if (m_itemlist.get(j).ITEM_NUMBER == Constants.ITEM_MISSILE &&
                        m_itemlist.get(j).itemState == Constants.STATE_ITEM_ACTIONED) {
                    Item_Missile missile = (Item_Missile)m_itemlist.get(j);
                    for (int k = 0; k < 8; k++) {
                        if (CollisionManager.checkBoxToBox(
                                missile.m_missile[k].m_boundBox, m_enemylist.get(i).m_boundBox)) {
                            lastScore += m_enemylist.get(i).grade;
                            m_enemylist.remove(i);
                            return;
                        }
                    }
                }

                else if (m_itemlist.get(j).ITEM_NUMBER == Constants.ITEM_SHIELD &&
                        m_itemlist.get(j).itemState == Constants.STATE_ITEM_ACTIONED) {
                    Item_Shield shield = (Item_Shield) m_itemlist.get(j);
                    if (CollisionManager.checkBoxToBox(
                            shield.m_shield.m_boundBox, m_enemylist.get(i).m_boundBox)) {
                        lastScore += m_enemylist.get(i).grade;
                        m_enemylist.remove(i);
                        return;
                    }
                }

                else if (m_itemlist.get(j).ITEM_NUMBER == Constants.ITEM_BOMB &&
                        m_itemlist.get(j).itemState == Constants.STATE_ITEM_ACTIONED) {
                    Item_Bomb bomb = (Item_Bomb) m_itemlist.get(j);
                    if (CollisionManager.checkBoxToBox(
                            bomb.m_bomb.m_boundBox, m_enemylist.get(i).m_boundBox)) {
                        lastScore += m_enemylist.get(i).grade;
                        m_enemylist.remove(i);
                        return;
                    }
                }
            }
        }
    }
}
