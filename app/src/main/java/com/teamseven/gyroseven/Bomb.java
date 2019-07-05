package com.teamseven.gyroseven;

import android.graphics.Rect;

import com.teamseven.gameframework.AppManager;
import com.teamseven.gameframework.SpriteAnimation;

public class Bomb extends SpriteAnimation {

    protected long itemTime = 990;  // 폭발 애니메이션 지속 시간
    protected Rect m_boundBox = new Rect();

    // 폭탄 애니메이션
    public Bomb() {
        super(AppManager.getInstance().getBitmap(R.drawable.bomb));
        super.initSpriteData(getBitmap().getWidth() / 8, getBitmap().getHeight(), 8, 8);

        // 폭발 범위 지정
        m_boundBox.set(m_x , m_y ,
                m_x + getBitmapWidth() ,
                m_y + getBitmapHeight());
    }

    @Override
    public void update(long gameTime) {
        super.update(gameTime);

        // 폭발 스프라이트 출력시 약간의 오차 조정
        m_rect.left = m_currentFrame * m_spriteWIdth + 4;
        m_rect.right = m_rect.left + m_spriteWIdth - 4;
    }
}
