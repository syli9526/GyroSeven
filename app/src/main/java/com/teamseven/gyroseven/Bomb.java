package com.teamseven.gyroseven;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.teamseven.gameframework.AppManager;
import com.teamseven.gameframework.SpriteAnimation;

public class Bomb extends SpriteAnimation {

    protected long itemTime = 990;

    protected Rect m_boundBox = new Rect();

    public Bomb() {
        super(AppManager.getInstance().getBitmap(R.drawable.bomb));
        super.initSpriteData(getBitmap().getWidth() / 8, getBitmap().getHeight(), 8, 8);
    }

    @Override
    public void update(long gameTime) {
        super.update(gameTime);

        m_rect.left = m_currentFrame * m_spriteWIdth + 4;
        m_rect.right = m_rect.left + m_spriteWIdth - 4;

        m_boundBox.set(m_x , m_y ,
                m_x + getBitmapWidth() ,
                m_y + getBitmapHeight());
    }
}
