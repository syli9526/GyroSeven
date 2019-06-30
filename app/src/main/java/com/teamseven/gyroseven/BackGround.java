package com.teamseven.gyroseven;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.teamseven.gameframework.AppManager;
import com.teamseven.gameframework.GraphicObject;

public class BackGround extends GraphicObject {
    //static final float SCROLL_SPEED = 2.0f;
    //private float m_scroll = -2000 + 480;

    public BackGround(int backType) {
        super(null);

        m_bitmap = AppManager.getInstance().getBitmap(R.drawable.background1);
        m_bitmap = Bitmap.createScaledBitmap(m_bitmap, AppManager.getInstance().getDeviceSize().x, AppManager.getInstance().getDeviceSize().y, true);
    }

    void update(long gameTime) {
        //m_scroll = m_scroll + SCROLL_SPEED;
        //if (m_scroll >= 0) m_scroll = -2000 + 480;
        //setPosition(0, (int)m_scroll);
    }

    @Override
    public void draw(Canvas _canvas) {
        _canvas.drawBitmap(m_bitmap, m_x, m_y, null);
    }
}
