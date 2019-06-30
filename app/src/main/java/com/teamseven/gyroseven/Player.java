package com.teamseven.gyroseven;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Log;

import com.teamseven.gameframework.AppManager;
import com.teamseven.gameframework.GameView;
import com.teamseven.gameframework.SpriteAnimation;

public class Player extends SpriteAnimation {
    Rect m_boundBox = new Rect();
    private int m_life;
    private float m_speed;

    public Player(Bitmap _bitmap) {
        super(_bitmap);

        super.initSpriteData(getBitmapWidth() / 6, getBitmapHeight(),8 , 4);
        super.setPosition(AppManager.getInstance().getDeviceSize().x / 2, AppManager.getInstance().getDeviceSize().y/2);

        m_life = 3;
        m_speed = 10.0f;
    }

    @Override
    public void draw(Canvas _canvas) {
        // m_currentFrame 에 따라 현재 프레임에 해당하는 영역을 잘라내고, m_matrix 값에 따라 이미지를 회전시킨다.
        Bitmap rotate = Bitmap.createBitmap(getBitmap(),
                m_currentFrame * (getBitmap().getWidth() / getIFrames()), 0,
                getBitmap().getWidth() / getIFrames(), getBitmap().getHeight(), m_matrix, true);

        _canvas.drawBitmap(rotate, m_x, m_y, null);
    }

    @Override
    public void update(long gameTime) {
        super.update(gameTime);

        m_boundBox.set(m_x, m_y,
                m_x + getBitmap().getWidth() / m_iFrames,
                m_y + getBitmap().getHeight());
    }

    public void move(float pitch, float roll) {
        // pitch : -90(우) ~ 90(좌)
        // roll : -90(상) ~ 90(하)
        m_x -= pitch / 10.0f * m_speed;

        if (m_x < 0) {
            m_x = 0;
        }
        else if (m_x > AppManager.getInstance().getDeviceSize().x - getBitmap().getWidth() / getIFrames()) {
            m_x = AppManager.getInstance().getDeviceSize().x  - getBitmap().getWidth() / getIFrames();
        }

        m_y += roll / 10.0f * m_speed;

        if (m_y < 0) {
            m_y = 0;
        }
        else if (m_y > AppManager.getInstance().getDeviceSize().y - getBitmap().getHeight()) {
            m_y =  AppManager.getInstance().getDeviceSize().y - getBitmap().getHeight();
        }

        // 스마트폰을 기울이는 방향에 따라 player 방향 전환
        double dx = -pitch;
        double dy = -roll;
        float degrees = (float)Math.toDegrees(Math.atan2(dx, dy));
        m_matrix.setRotate(degrees);
    }

    public int getLife( ) { return m_life; }
    public void addLife( ) {
        m_life++;
        if (m_life  > 3) { m_life = 3; }
    }
    public void damagePlayer( ) {
        m_life--;
        if (m_life < 0) { m_life = 0; }
    }

    public void setSpeed(int _speed) { m_speed = _speed; }
    public float getSpeed() { return m_speed; }
}
