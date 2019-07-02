package com.teamseven.gyroseven;

import android.graphics.Canvas;
import android.graphics.Rect;

import com.teamseven.gameframework.AppManager;
import com.teamseven.gameframework.SpriteAnimation;

public class Score extends SpriteAnimation {

    protected Rect m_boundBox = new Rect();
    protected int m_num;

    public Score() {
        super(AppManager.getInstance().getBitmap(R.drawable.number));
        super.initSpriteData(getBitmap().getWidth() / 10, getBitmap().getHeight(), 1, 1);
    }

    @Override
    public void update(long num) {
        m_num = (int)num;
        m_boundBox.set(m_x, m_y, m_x + getBitmapWidth(), m_y + getBitmapHeight());
        m_rect.left = m_spriteWIdth * (int) num;
        m_rect.right = m_rect.left + m_spriteWIdth;
    }
}
