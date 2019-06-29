package com.teamseven.gyroseven;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.teamseven.gameframework.AppManager;
import com.teamseven.gameframework.SpriteAnimation;

public class Player extends SpriteAnimation {
    Rect m_boundBox = new Rect();
    private int m_life;

    public Player(Bitmap _bitmap) {
        super(_bitmap);

        super.initSpriteData(getBitmapWidth() / 6, getBitmapHeight(), 10, 6);
        super.setPosition(AppManager.getInstance().getDeviceSize().x / 2, AppManager.getInstance().getDeviceSize().y - getBitmapHeight());

        m_life = 3;
    }

    @Override
    public void update(long gameTime) {
        super.update(gameTime);

        m_boundBox.set(m_x, m_y,
                m_x + getBitmap().getWidth() / m_iFrames,
                m_y + getBitmap().getHeight());
    }

    public int getLife( ) { return m_life; }
    public void addLife( ) { m_life++; }
    public void destroyPlayer( ) { m_life--; }
}
