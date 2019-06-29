package com.teamseven.gyroseven;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Log;

import com.teamseven.gameframework.GameView;
import com.teamseven.gameframework.SpriteAnimation;

public class Player extends SpriteAnimation {
    Rect m_boundBox = new Rect();
    private int m_life;
    private float m_speed;

    public Player(Bitmap _bitmap) {
        super(_bitmap);

        this.initSpriteData(300, 300,5,4);
        this.setPosition(GameView.SCREEN_WIDTH / 2 - (m_bitmap.getWidth() / m_iFrames / 2),
                (int)(GameView.SCREEN_HEIGHT * 0.6));

        m_life = 3;
        m_speed = 10.0f;
    }

    @Override
    public void update(long gameTime) {
        super.update(gameTime);

        m_boundBox.set(m_x, m_y,
                m_x + getBitmap().getWidth() / m_iFrames,
                m_y + getBitmap().getHeight());
    }

    public void move(float pitch, float roll) {
        m_x -= pitch / 10.0f * m_speed;

        if (m_x < 0) {
            m_x = 0;
        }
        else if (m_x > GameView.SCREEN_WIDTH - getBitmap().getWidth() / getIFrames()) {
            m_x = GameView.SCREEN_WIDTH - getBitmap().getWidth() / getIFrames();
        }

        m_y += roll / 10.0f * m_speed;

        if (m_y < 0) {
            m_y = 0;
        }
        else if (m_y > GameView.SCREEN_HEIGHT - getBitmap().getHeight()) {
            m_y = GameView.SCREEN_HEIGHT - getBitmap().getHeight();
        }

        double dx = -pitch;
        double dy = -roll;

        float degrees = (float)Math.toDegrees(Math.atan2(dx, dy));
        Log.d("myCheck", Double.toString(degrees));
        m_matrix.setRotate(degrees);
    }

    public int getLife( ) { return m_life; }
    public void addLife( ) { m_life++; }
    public void destroyPlayer( ) { m_life--; }

    public void setSpeed(int _speed) { m_speed = _speed; }
    public float getSpeed() { return m_speed; }
}
