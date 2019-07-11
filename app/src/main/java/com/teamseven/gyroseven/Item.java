package com.teamseven.gyroseven;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.teamseven.gameframework.SpriteAnimation;

public class Item extends SpriteAnimation {

    protected Rect m_boundBox = new Rect();
    protected int itemState = Constants.STATE_ITEM_MADE; // 모든 아이템은 생성 시 ITEM_MADE 상태로 초기화
    public int ITEM_NUMBER;                              // 아이템 번호

    public Item(Bitmap _bitmap) {
        super(_bitmap);
    }

    // 아이템 애니메이션을 그려줌
    public void update(long gameTime, GameState _game) {
        super.update(gameTime);

        // 아이템 충돌 범위 지정
        m_boundBox.set(m_x, m_y,
                m_x + getBitmapWidth(),
                m_y + getBitmapHeight());
    }

    public void actionItem(GameState _game) {
    }
}