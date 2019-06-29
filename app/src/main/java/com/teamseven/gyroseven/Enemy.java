package com.teamseven.gyroseven;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.teamseven.gameframework.GameView;
import com.teamseven.gameframework.SpriteAnimation;

public class Enemy extends SpriteAnimation {
    public static final int STATE_NORMAL= 0;
    public static final int STATE_OUT= 1;
    public int state = STATE_NORMAL;

    protected int m_hp;
    protected float m_speed;

    protected Rect m_boundBox = new Rect();

    public Enemy(Bitmap _bitmap) {
        super(_bitmap);
    }

    void move() {
        m_y += m_speed;

        if ( m_y > GameView.SCREEN_HEIGHT) state = STATE_OUT;
    }

    @Override
    public void update(long gameTime) {
        super.update(gameTime);
        move();

        m_boundBox.set(m_x, m_y,
                m_x + (getBitmap().getWidth() / getIFrames()),
                m_y + getBitmap().getHeight());
    }

    void attack() {

    }
}
