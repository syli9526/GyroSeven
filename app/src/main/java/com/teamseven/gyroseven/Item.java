package com.teamseven.gyroseven;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.teamseven.gameframework.SpriteAnimation;

public class Item extends SpriteAnimation {

    protected Rect m_boundBox = new Rect();
    public Item(Bitmap _bitmap) {
        super(_bitmap);
    }

    public void update(long gameTime) {
        super.update(gameTime);

        m_boundBox.set(m_x, m_y,
                m_x + getBitmapWidth(),
                m_y + getBitmapHeight());
    }

    public void actionItem(GameState _game) { }
}
