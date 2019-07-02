package com.teamseven.gyroseven;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.teamseven.gameframework.AppManager;
import com.teamseven.gameframework.SpriteAnimation;

public class Player extends SpriteAnimation {

    private Player_Die m_playerDie;

    private int m_life;
    private float m_speed;
    private int m_centerX;
    private int m_centerY;

    protected Rect m_boundBox = new Rect();

    protected int playerState = Constants.STATE_NORMAL;
    protected boolean m_canDamaged;
    protected long m_damagedTime;
    protected long m_dieStartTime;
    protected long m_twinkleStartTime;
    protected long m_endTime;
    protected boolean m_twinkleSet;

    Paint alpha;

    public Player(Bitmap _bitmap) {
        super(_bitmap);
        super.initSpriteData(getBitmap().getWidth() / 4, getBitmap().getHeight(), 8, 4);
        super.setPosition((AppManager.getInstance().getDeviceSize().x - getBitmapWidth()) / 2, AppManager.getInstance().getDeviceSize().y);

        alpha = new Paint();

        m_life = 3;
        m_speed = 8.0f;
        m_canDamaged = true;
        m_damagedTime = 1500;

        m_twinkleSet = false;
    }


    @Override
    public void draw(Canvas _canvas) {
        if (playerState == Constants.STATE_NORMAL) {
            // m_currentFrame 에 따라 현재 프레임에 해당하는 영역을 잘라내고, m_matrix 값에 따라 이미지를 회전시킨다.
            Bitmap rotate = Bitmap.createBitmap(getBitmap(),
                    m_currentFrame * getBitmapWidth(), 0,
                    getBitmapWidth(), getBitmapHeight(), m_matrix, true);

            if (m_twinkleSet)
                alpha.setAlpha(40);
            else
                alpha.setAlpha(255);

            _canvas.drawBitmap(rotate, m_x, m_y, alpha);
        }

        else if (playerState == Constants.STATE_EXPLODED) {
            m_playerDie.draw(_canvas);
        }
    }

    @Override
    public void update(long gameTime) {
        if (playerState == Constants.STATE_NORMAL) {
            super.update(gameTime);

            m_boundBox.set(m_x, m_y,
                    m_x + getBitmapWidth(),
                    m_y + getBitmapHeight());

            m_centerX = (getX() + m_boundBox.right) / 2;
            m_centerY = (getY() + m_boundBox.bottom) / 2;

            m_dieStartTime = gameTime;

            if (m_canDamaged == true) {
                m_twinkleStartTime = gameTime;
            }
            else {
                if (gameTime - m_twinkleStartTime >= 100)  {
                    m_twinkleStartTime = gameTime;
                    m_twinkleSet = !m_twinkleSet;
                }
            }
        }

        if (playerState == Constants.STATE_EXPLODED) {
            m_playerDie.update(gameTime);

            if (gameTime - m_dieStartTime > m_playerDie.aniTime) {
                m_endTime = gameTime;

                playerState = Constants.STATE_DEAD;

                return;
            }
        }

        if (playerState == Constants.STATE_DEAD) {
            if (gameTime - m_endTime >= 2000) {
                playerState = Constants.STATE_ENDED;
                Log.d("myCheck","Game Ended");

                return;
            }
        }
    }

    public void die() {
        playerState = Constants.STATE_EXPLODED;

        m_playerDie = new Player_Die();
        m_playerDie.setPosition(getCenterX() - m_playerDie.getBitmapWidth() / 2,
                getCenterY() - m_playerDie.getBitmapHeight() / 2);
    }

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
        float degrees = (float) Math.toDegrees(Math.atan2(dx, dy));
        //m_matrix.setRotate(degrees, getBitmapWidth() / 2, getBitmapHeight() / 2);

        m_matrix.reset();
        //m_matrix.setTranslate(getX(), getY());
        m_matrix.setRotate(degrees, getCenterX(), getCenterY());
    }

    public int getLife() {
        return m_life;
    }

    public void addLife() {
        m_life++;
        if (m_life > 3) {
            m_life = 3;
        }
    }

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
