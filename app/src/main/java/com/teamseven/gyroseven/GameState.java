package com.teamseven.gyroseven;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.teamseven.gameframework.AppManager;
import com.teamseven.gameframework.CollisionManager;
import com.teamseven.gameframework.IState;
import com.teamseven.gameframework.SoundManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GameState implements IState {

    protected Player m_player;                                          // 플레이어 클래스
    protected BackGround m_background;                                  // 배경 이미지를 관리하는 클래스
    protected ArrayList<Enemy> m_enemylist = new ArrayList<Enemy>();    // 적을 관리하는 ArrayList
    protected ArrayList<Item> m_itemlist = new ArrayList<Item>();       // 아이템을 관리하는 ArrayList
    protected Heart m_heart;                                            // 플레이어 생명 표시 비트맵
    protected Alert m_alert;                                            // ready, start, speedup을 알려 줄 이미지를 관리
    protected ArrayList<Score> m_score = new ArrayList<>();             // 현재 스코어를 알려줄 이미지를 관리할 arraylist

    private boolean event = false;                                      // ready, start, speedup가 나타날 때 true로 변경해 알려줌
    private int frequency = 3000;                                       // 적이 나타나는 빈도
    private int level = 1;                                              // 현재 레벨
    private int timeWeight = 0;                                         // 6레벨 이상일때 시간에 따라 점수를 증가시키기 위한 가중치
    private int numOfEmemy_2 = 0;                                       // 화면에 나온 enemy2의 수
    private long currentScore = 0;                                      // 현재 스코어
    private boolean subIntro = true;                                    // gameState의 인트로를 부를 flage

    // 각 마지막 갱신한 시간
    private long lastEvent;                                             // 이벤트가 발생한 시간
    private long lastUpdateScore = System.currentTimeMillis();          // 마지막 스코어 업데이트 시간
    private long lastRegenEnemy = System.currentTimeMillis();           // 마지막 적 생성 시간
    private long lastLevelUp = System.currentTimeMillis();              // 마지막 레벨 업 시간
    private long lastReagenItem = System.currentTimeMillis();           // 마지막 아이템 생성 시간
    private long lastPlayerDamage = System.currentTimeMillis();         // 플레이어가 피해를 입은 시간

    private Random randItem = new Random();                             // 아이템 랜덤 생성 변수
    private Random randEnemy = new Random();                            // 적 랜덤 생성 변수

    private float m_roll;                                               // 현재 디바이스의 roll 값
    private float m_pitch;                                              // 현재 디바이스의 pitch 값

    private DBHelper mHelper;

    @Override
    public void init() {
        // 초기화
        m_player = new Player(AppManager.getInstance().getBitmap(R.drawable.player_sprite));
        m_background = new BackGround(level);
        m_heart = new Heart();
        m_alert = new Alert();
        Score s = new Score();
        s.update(0);
        m_score.add(s);
        mHelper = new DBHelper(AppManager.getInstance().getContext());
        SoundManager.getInstance().playBackground(); // 배경음악 재생
    }

    @Override
    public void destroy() {

    }

    @Override
    public void update() {
        long gameTime = System.currentTimeMillis();

        // 게임이 시작되면 실제 게임의 인트로 실행
        if (subIntro) {

            m_player.setSpeed(0.5f);
            m_player.update(gameTime);
            m_player.move(0, -90);

            // 위치에 따라 이벤트를 발생시키고(ready, start),
            // 플레이어 위치가 중앙으로 가면 실제 게임 상태로 넘어감
            if (m_player.getCenterY() >= AppManager.getInstance().getDeviceSize().y / 4 * 3) {
                event = true;
            } else if (m_player.getCenterY() > AppManager.getInstance().getDeviceSize().y / 2) {
                event = true;
                m_alert.setBitmap(AppManager.getInstance().getBitmap(R.drawable.start));
            } else if (m_player.getCenterY() <= AppManager.getInstance().getDeviceSize().y / 2) {
                AppManager.getInstance().setGame(true);
                subIntro = false;
                m_alert.setBitmap(AppManager.getInstance().getBitmap(R.drawable.speed_up));
                m_alert.setPosition((AppManager.getInstance().getDeviceSize().x - m_alert.getBitmap().getWidth()) / 2,
                        (AppManager.getInstance().getDeviceSize().y - m_alert.getBitmap().getHeight()) - 10);
                m_player.setSpeed(8);
            }

        } else { // 실제 게임 상태
            int cnt = 0;

            m_player.update(gameTime); // 플레이어 업데이트

            updateShield(); // 쉴드 아이템 업데이트

            // Enemy 업데이트
            for (int i = m_enemylist.size() - 1; i >= 0; i--) {
                Enemy enemy = m_enemylist.get(i);
                enemy.move(m_player.getCenterX(), m_player.getCenterY());
                if (enemy.grade == 20 && enemy.state != Constants.STATE_OUT) cnt++;
                if (enemy.state == Constants.STATE_OUT) m_enemylist.remove(i);

                enemy.update();
            }

            // Item 업데이트
            for (int i = m_itemlist.size() - 1; i >= 0; i--) {
                Item item = m_itemlist.get(i);
                // 아이템 상태가 ITEM_FINISHED가 아니면 update, 맞으면 삭제
                if (item.itemState != Constants.STATE_ITEM_FINISHED) {
                    item.update(gameTime, this);
                } else {
                    m_itemlist.remove(i);
                }
            }

            // Player가 살아있을 때만 실행
            if (m_player.playerState == Constants.STATE_NORMAL) {
                m_player.move(m_pitch, m_roll);         // 플레이어 이동

                m_background.changeBackGround(level);   // 레벨에 따른 배경 변화
                numOfEmemy_2 = cnt;                     // 현재 Enemy2의 수를 저장
                updateScore();                          // 점수 업데이트
                makeEnemy();                            // 적 생성
                calculateLevel(gameTime);               // 레벨 계산
                if (m_itemlist.size() < 5)              // 아이템이 5개 미만이면 아이템 생성
                    makeItem(gameTime);

                checkCollision();                       // 충돌체크
            }
        }
    }

    private void viewDialog() {
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 다이얼 로그 생성
                EndDialog endDialog = new EndDialog(AppManager.getInstance().getContext(), (int) currentScore, mHelper.compareDBScore((int) currentScore));
                endDialog.setCancelable(false);
                endDialog.show();
            }

        }, 0);
    }

    @Override
    public void render(Canvas _canvas) {

        // 플에이어 상태가 STATE_ENDED라면 게임이 종료됐음으로 viewDialog를 화면에 띄어줌
        if (m_player.playerState == Constants.STATE_ENDED && AppManager.getInstance().isGame()) {
            viewDialog();
            AppManager.getInstance().setGame(false);
        } else {
            m_background.draw(_canvas);         // 배경 draw
            for (Enemy enemy : m_enemylist) {   // 적 draw
                enemy.draw(_canvas);
            }
            for (Item item : m_itemlist) {      // 아이템 draw
                item.draw(_canvas);
            }
            // score 자릿수 별로 위치 조정하고 draw
            for (int i = m_score.size() - 1; i >= 0; i--) {
                m_score.get(i).setPosition(AppManager.getInstance().getDeviceSize().x - m_score.get(i).getBitmapWidth() * (i + 1) - 10, 5);
                m_score.get(i).draw(_canvas);
            }

            // 플레이어 하트 draw
            for (int i = 0; i < m_player.getLife(); i++) {
                m_heart.setPosition(5 + m_heart.getBitmap().getWidth() * i, 5);
                m_heart.draw(_canvas);
            }

            m_player.draw(_canvas); // 플레이어 draw

            if (event) { // 이벤트가 true일때 1초동안 ready, start, speedup등을 보여줌
                m_alert.draw(_canvas);
                if (System.currentTimeMillis() - lastEvent >= 1000)
                    event = false;
            }
        }
    }

    // 방향 센서에서 이벤트를 받아옴
    @Override
    public void onSensorChanged(SensorEvent event) {
        synchronized (this) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_ORIENTATION:
                    m_pitch = event.values[1];
                    m_roll = event.values[2];

                    // pitch의 최대값, 최솟값 조政
                    if (m_pitch > 90.0f) {
                        m_pitch = 90.0f;
                    } else if (m_pitch < -90.0f) {
                        m_pitch = -90.0f;
                    }

                    m_roll -= 18.0f; // 센서의 기준을 디바이스를 약간 기울인 상태로 조정
                    // roll의 최대값, 최솟값 조政
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
            currentScore += (level + timeWeight);
        }

        // 레벨 계산
        if (level < 6) {
            if (gameTime - lastLevelUp >= 20000) {
                SoundManager.getInstance().play(Constants.EFFECT_LEVELUP);
                m_background.changeBackGround(level);
                lastEvent = System.currentTimeMillis();
                lastLevelUp = gameTime;
                event = true;
                level++;
                frequency -= 500;
                for (Enemy enemy : m_enemylist) enemy.speedUp(1f);
            }
        } else {
            // 레벨 6 이상일 경우 시간 가중치를 증가시켜 스코어를 더 높일 수 있도록 함
            if (gameTime - lastLevelUp >= 1000) {
                lastLevelUp = gameTime;
                timeWeight += 1;
                for (Enemy enemy : m_enemylist) enemy.speedUp(1f);
            }
        }
    }

    public void makeItem(long gameTime) {
        int itemNumber = randItem.nextInt(Constants.ITEM_NUMBER); // 아이템 번호 랜덤 지정
        if (gameTime - lastReagenItem >= 5000) { // 5초에 한번 생성
            lastReagenItem = gameTime;

            Item newItem = new Item(null);

            // 아이템 번호에 따라 아이템 생성
            if (itemNumber == Constants.ITEM_HEART) {
                newItem = new Item_Heart();
            } else if (itemNumber == Constants.ITEM_MISSILE) {
                newItem = new Item_Missile();
            } else if (itemNumber == Constants.ITEM_SHIELD) {
                newItem = new Item_Shield();
            } else if (itemNumber == Constants.ITEM_BOMB) {
                newItem = new Item_Bomb();
            }

            // 위치 지정하고 ArrayList에 추가
            newItem.setPosition(randEnemy.nextInt((AppManager.getInstance().getDeviceSize().x) - newItem.getBitmapWidth()),
                    randEnemy.nextInt((AppManager.getInstance().getDeviceSize().y) - newItem.getBitmapHeight()));
            m_itemlist.add(newItem);
        }
    }

    public void updateShield() {
        int shieldCnt = -1;

        // 쉴드가 겹쳐있을 시, 먼저 획득한 쉴드 삭제
        for (int i = 0; i < m_itemlist.size() - 1; i++) {
            if (m_itemlist.get(i).ITEM_NUMBER == Constants.ITEM_SHIELD &&
                    m_itemlist.get(i).itemState == Constants.STATE_ITEM_ACTIONED) {
                if (shieldCnt == -1)
                    shieldCnt = i;
                else {
                    m_itemlist.remove(shieldCnt);
                    return;
                }
            }
        }

        shieldCnt = 0;
        // 쉴드 발동 시, 해당 아이템들을 리스트의 맨 앞으로 정렬
        for (int i = 0; i < m_itemlist.size() - 1; i++) {
            if (m_itemlist.get(i).ITEM_NUMBER == Constants.ITEM_SHIELD &&
                    m_itemlist.get(i).itemState == Constants.STATE_ITEM_ACTIONED) {
                Collections.swap(m_itemlist, i, shieldCnt);
                shieldCnt++;
            }
        }
    }

    // 화면에 보여줄 스코어 이미지를 업데이트
    public void updateScore() {
        long num = currentScore;
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

    // 자리수가 넘어가면 이미지 객체 하나 더 생성
    public void makeScore(long num) {
        Score s = new Score();
        s.update(num);
        m_score.add(s);
    }

    public void makeEnemy() {

        // 적 생성 위치 결정
        int posX[] = {randEnemy.nextInt(AppManager.getInstance().getDeviceSize().x), AppManager.getInstance().getDeviceSize().x, randEnemy.nextInt(AppManager.getInstance().getDeviceSize().x), 0, randEnemy.nextInt(AppManager.getInstance().getDeviceSize().x)};
        int posY[] = {0, randEnemy.nextInt(AppManager.getInstance().getDeviceSize().y), AppManager.getInstance().getDeviceSize().y, randEnemy.nextInt(AppManager.getInstance().getDeviceSize().y), 0};

        if (System.currentTimeMillis() - lastRegenEnemy >= frequency) {
            lastRegenEnemy = System.currentTimeMillis();
            Enemy crw[] = new Enemy_1[4];

            // enemy1은 가중치를 랜덤으로 결정해 적 마다 속도와 방향이 달라질 수 있도록 함.
            for (int i = 0; i < crw.length; i++) {
                crw[i] = new Enemy_1();
                crw[i].x_weight = randEnemy.nextInt(3 + level) + 1;
                crw[i].y_weight = randEnemy.nextInt(3 + level);
                crw[i].movePattern = i + randEnemy.nextInt(1);

                if (posX[i] == 0) crw[i].setPosition(-crw[i].getBitmap().getWidth(), posY[i]);
                else if (posY[i] == 0) crw[i].setPosition(posX[i], -crw[i].getBitmap().getHeight());
                else crw[i].setPosition(posX[i], posY[i]);

                m_enemylist.add(crw[i]);
            }

            // enemy2의 수와 레벨과 같은 수 만큼 생성 
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

    // 충돌 처리
    public void checkCollision() {
        long gameTime = System.currentTimeMillis();

        // 플레이어가 살아있는 상태에서만 실행되는 충돌처리들
        if (m_player.playerState == Constants.STATE_NORMAL) {

            // 플레이어가 무적상태가 아니라면
            if (gameTime - lastPlayerDamage >= m_player.getDamagedTime()) {
                m_player.setTwinkle(false);     // 깜빡임 off
                m_player.setCanDamaged(true);   // 데이미를 입을 수 있는 상태로 지정

                // 플레이어와 적과의 충돌처리
                for (int i = m_enemylist.size() - 1; i >= 0; i--) {
                    Rect resize = new Rect();

                    // 적과 부딛히는 범위를 줄여 난이도 조정
                    resize.set(m_player.m_boundBox.left + 10,
                            m_player.m_boundBox.top + 10,
                            m_player.m_boundBox.right - 10,
                            m_player.m_boundBox.bottom - 10);

                    // 원충돌 실행
                    if (CollisionManager.checkCircleToCircle(
                            resize, m_enemylist.get(i).m_boundBox)) {
                        lastPlayerDamage = gameTime;
                        m_player.setCanDamaged(false);  // 무적 상태로 변화

                        m_enemylist.remove(i);          // 해당 적 삭제
                        m_player.damagePlayer();        // 생명 1 감소

                        // 적과 충돌 시 진동, 생명이 0이 되면 플레이어 사망
                        if (m_player.getLife() <= 0) {
                            m_player.die();
                            AppManager.getInstance().getGameView().getVibrator().vibrate(300);
                            SoundManager.getInstance().stopBackground();
                        } else {
                            AppManager.getInstance().getGameView().getVibrator().vibrate(100);
                        }
                        return;
                    }
                }
            }
        }

        // 플레이어와 아이템 충돌처리
        for (int i = m_itemlist.size() - 1; i >= 0; i--) {
            if (m_itemlist.get(i).itemState == Constants.STATE_ITEM_MADE) {
                if (CollisionManager.checkCircleToCircle(
                        m_player.m_boundBox, m_itemlist.get(i).m_boundBox)) {
                    // 충돌시 해당 아이템의 actionItem 메소드 실행
                    m_itemlist.get(i).actionItem(this);
                }
            }
        }

        // 적과 발동된 아이템들의 충돌처리
        for (int i = m_enemylist.size() - 1; i >= 0; i--) {
            for (int j = m_itemlist.size() - 1; j >= 0; j--) {

                // 1. 미사일과 적 충돌처리
                // 충돌한 아이템이 미사일이고, 아이템이 발동된 상태라면 충돌 확인
                if (m_itemlist.get(j).ITEM_NUMBER == Constants.ITEM_MISSILE &&
                        m_itemlist.get(j).itemState == Constants.STATE_ITEM_ACTIONED) {
                    Item_Missile missile = (Item_Missile) m_itemlist.get(j);
                    // 8개 미사일을 반복문을 통해 모두 체크
                    for (int k = 0; k < 8; k++) {
                        // 충돌이 잘 되도록 사각 충돌 실행
                        if (CollisionManager.checkBoxToBox(
                                missile.m_missile[k].m_boundBox, m_enemylist.get(i).m_boundBox)) {
                            currentScore += m_enemylist.get(i).grade; // 적에 따라 점수 증가
                            m_enemylist.remove(i); // 해당 적 삭제
                            return;
                        }
                    }
                }

                // 2. 쉴드와 적 충돌처리
                // 충돌한 아이템이 쉴드이고, 아이템이 발동된 상태라면 충돌 확인
                else if (m_itemlist.get(j).ITEM_NUMBER == Constants.ITEM_SHIELD &&
                        m_itemlist.get(j).itemState == Constants.STATE_ITEM_ACTIONED) {
                    Item_Shield shield = (Item_Shield) m_itemlist.get(j);
                    if (CollisionManager.checkBoxToBox(
                            shield.m_shield.m_boundBox, m_enemylist.get(i).m_boundBox)) {
                        currentScore += m_enemylist.get(i).grade;
                        m_enemylist.remove(i);
                        return;
                    }
                }

                // 3. 폭탄과 적 충돌처리
                // 충돌한 아이템이 폭탄이고, 아이템이 발동된 상태라면 충돌 확인
                else if (m_itemlist.get(j).ITEM_NUMBER == Constants.ITEM_BOMB &&
                        m_itemlist.get(j).itemState == Constants.STATE_ITEM_ACTIONED) {
                    Item_Bomb bomb = (Item_Bomb) m_itemlist.get(j);
                    if (CollisionManager.checkBoxToBox(
                            bomb.m_bomb.m_boundBox, m_enemylist.get(i).m_boundBox)) {
                        currentScore += m_enemylist.get(i).grade;
                        m_enemylist.remove(i);
                        return;
                    }
                }
            }
        }
    }
}