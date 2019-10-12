package com.team7even.gyroseven;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.team7even.gameframework.AppManager;
import com.team7even.gameframework.SpriteAnimation;

public class Player extends SpriteAnimation {

    // 플레이어 폭발 애니메이션 클래스
    private Player_Die m_playerDie;

    private int m_life;
    private float m_speed;
    private int m_centerX;
    private int m_centerY;
    private float m_degrees;

    protected Rect m_boundBox = new Rect();

    protected int playerState = Constants.STATE_NORMAL;     // 플레이어 상태를 STATE_NORMAL로 초기화

    protected boolean m_canDamaged;                         // 플레이어가 무적상태인지 구분(충돌시 1.5초 무적)
    protected long m_damagedTime;                           // 플레이어가 적과 충돌했을 때 무적 지속시간
    protected long m_dieStartTime;                          // 플레이어가 죽은 시간
    protected long m_twinkleStartTime;                      // 플레이어가 무적상태가 시작 된 시간(깜빡임 시작)
    protected long m_endTime;                               // 플레이어 폭발 애니메이션이 끝난 시간
    protected boolean m_twinkleSet;                         // 플레이어 적과 충돌 시 깜빡임 on, off(투명도 조절)

    Paint m_alpha;                                          // 플레이어의 투명 값(깜빡일 때)

    public Player(Bitmap _bitmap) {
        super(_bitmap);
        super.initSpriteData(getBitmap().getWidth() / 4, getBitmap().getHeight(), 8, 4);
        super.setPosition((AppManager.getInstance().getDeviceSize().x - getBitmapWidth()) / 2, AppManager.getInstance().getDeviceSize().y);

        m_alpha = new Paint();

        m_life = 3;
        m_speed = 8.0f;
        m_canDamaged = true;    // 데미지를 입을 수 있는 상태로 지정
        m_damagedTime = 1500;   // 적 충돌 시 무적 지속시간 설정

        m_twinkleSet = false;   // 적과 충돌하기 전에는 깜빡임 off
    }


    @Override
    public void draw(Canvas _canvas) {
        if (playerState == Constants.STATE_NORMAL) {
            // m_currentFrame 에 따라 현재 프레임에 해당하는 영역을 잘라내고, m_matrix 값에 따라 이미지를 회전시킨다.
            Bitmap rotate = Bitmap.createBitmap(getBitmap(),
                    m_currentFrame * getBitmapWidth(), 0,
                    getBitmapWidth(), getBitmapHeight(), m_matrix, true);

            // 적과 충돌했다면 m_twinkleSet 상태에 따라 플레이어를 깜빡인다.
            if (m_twinkleSet)
                m_alpha.setAlpha(40);
            else
                m_alpha.setAlpha(255);

            if (_canvas != null) _canvas.drawBitmap(rotate, m_x, m_y, m_alpha);
        }

        // 플레이어가 폭발 상태면 폭발 애니메이션 draw
        else if (playerState == Constants.STATE_EXPLODED) {
            m_playerDie.draw(_canvas);
        }
    }

    @Override
    public void update(long gameTime) {
        if (playerState == Constants.STATE_NORMAL) {
            super.update(gameTime);

            // 충돌범위 설정
            m_boundBox.set(m_x, m_y,
                    m_x + getBitmapWidth(),
                    m_y + getBitmapHeight());

            m_centerX = (getX() + m_boundBox.right) / 2;
            m_centerY = (getY() + m_boundBox.bottom) / 2;

            m_dieStartTime = gameTime;

            // 적과 충돌하기 전까지는 m_twinkleStartTime를 현재 게임시간으로 지정
            if (m_canDamaged == true) {
                m_twinkleStartTime = gameTime;
            }
            else { // 충돌 시 0.1초마다 m_twinkleSet을 on, off
                if (gameTime - m_twinkleStartTime >= 100)  {
                    m_twinkleStartTime = gameTime;
                    m_twinkleSet = !m_twinkleSet;
                }
            }
        }

        // 플레이어가 폭발 상태일 때
        if (playerState == Constants.STATE_EXPLODED) {
            // 폭발 애니메이션 업데이트
            m_playerDie.update(gameTime);

            // 폭발시간이 끝나면 플레이어의 상태를 STATE_DEAD로 바꿈
            if (gameTime - m_dieStartTime > m_playerDie.aniTime) {
                m_endTime = gameTime;

                playerState = Constants.STATE_DEAD;

                return;
            }
        }

        // 플레이어 폭발 애니메이션이 끝난 후 실행
        if (playerState == Constants.STATE_DEAD) {
            // 플레이어가 폭발하고 1.5초 뒤 상태를 STATE_ENDED로 바꿔 게임이 종료되었음을 알림
            if (gameTime - m_endTime >= 1500) {
                playerState = Constants.STATE_ENDED;

                return;
            }
        }
    }

    // 플레이어가 파괴됐을 때 한 번 실행
    public void die() {
        // 플레이어 상태변경
        playerState = Constants.STATE_EXPLODED;

        // 플레이어 폭발 애니메이션 생성
        m_playerDie = new Player_Die();
        m_playerDie.setPosition(getCenterX() - m_playerDie.getBitmapWidth() / 2,
                getCenterY() - m_playerDie.getBitmapHeight() / 2);
    }

    // 현재 디바이스의 각도(float)에 따라 매 프레임 변화한다.
    // 매 update마다 현재 pitch, roll 값에 해당하는 float 형의 degrees를 구한다.
    // 해당 degrees를 변수 m_matrix에 적용시켜 유지한다.
    // draw 시에 현재 플레이어 비트맵에 m_matrix를 적용시켜 화면에 출력한다.
    public void move(float pitch, float roll) {
        // pitch : -90(우) ~ 90(좌)
        // roll : -90(상) ~ 90(하)
        m_x -= pitch / 10.0f * m_speed;

        if (m_x < 0) {
            m_x = 0;
        } else if (m_x > AppManager.getInstance().getDeviceSize().x - getBitmapWidth()) {
            m_x = AppManager.getInstance().getDeviceSize().x - getBitmapWidth();
        }

        m_y += roll / 10.0f * m_speed;

        if (m_y < 0) {
            m_y = 0;
        } else if (m_y > AppManager.getInstance().getDeviceSize().y - getBitmap().getHeight()) {
            m_y = AppManager.getInstance().getDeviceSize().y - getBitmap().getHeight();
        }

        // 스마트폰을 기울이는 방향에 따라 player 방향 전환
        double dx = -pitch;
        double dy = -roll;
        m_degrees = (float) Math.toDegrees(Math.atan2(dx, dy));

        m_matrix.setRotate(m_degrees, getCenterX(), getCenterY());
    }

    public float getDegrees() { return m_degrees; }

    public int getLife() {
        return m_life;
    }

    // 플레이어 라이프 1 증가
    public void addLife() {
        m_life++;
        if (m_life > 3) {
            m_life = 3;
        }
    }

    // 플레이어 라이프 1 감소
    public void damagePlayer() {
        m_life--;
        if (m_life < 0) {
            m_life = 0;
        }
    }

    public int getCenterX() {
        return m_centerX;
    }

    public int getCenterY() {
        return m_centerY;
    }

    public void setSpeed(float _speed) {
        m_speed = _speed;
    }

    public float getSpeed() {
        return m_speed;
    }

    public void setCanDamaged(boolean _canDamaged) {
        m_canDamaged = _canDamaged;
    }

    public long getDamagedTime() {
        return m_damagedTime;
    }

    public void setTwinkle(boolean _bTwinckle) {
        m_twinkleSet = _bTwinckle;
    }
}
