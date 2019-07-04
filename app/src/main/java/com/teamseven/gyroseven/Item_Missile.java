package com.teamseven.gyroseven;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import com.teamseven.gameframework.AppManager;
import com.teamseven.gameframework.SoundManager;

public class Item_Missile extends Item {

    Missile m_missile[] =  new Missile[8];
    protected int m_outCount;

    // 미사일 아이템 비트맵 생성과 초기화
    public Item_Missile() {
        super(AppManager.getInstance().getBitmap(R.drawable.item_missile));
        super.initSpriteData(getBitmap().getWidth() / 2, getBitmap().getHeight(),2 , 2);

        ITEM_NUMBER = Constants.ITEM_MISSILE;
    }

    // 아이템 획득 시 한 번 실행
    @Override
    public void actionItem(GameState _game) {
        // 현재 상태를 바꾸고, 8개 미사일 생성 후 이동 패턴 적용
        itemState = Constants.STATE_ITEM_ACTIONED;
        SoundManager.getInstance().play(Constants.EFFECT_MISSILE);

        for (int i = 0; i < 8; i++) {
            m_missile[i] = new Missile();
            m_missile[i].moveState = i;
            m_missile[i].setPosition(getX(), getY());
        }
    }

    @Override
    public void draw(Canvas _canvas) {
        // 아이템 획득 전에는 아이템 이미지만 그려줌
        if (itemState == Constants.STATE_ITEM_MADE)
            super.draw(_canvas);

        // 아이템이 발동되면 8개의 미사일을 방향에 따라 회전시키고 화면에 그려줌
        else if (itemState == Constants.STATE_ITEM_ACTIONED) {
            for (int i = 0; i < 8; i++) {
                Matrix matrix = new Matrix();
                matrix.setRotate(45 * i);

                Bitmap rotate = Bitmap.createBitmap(m_missile[i].getBitmap(), 0, 0,
                        m_missile[i].getBitmap().getWidth(), m_missile[i].getBitmap().getHeight(), matrix, true);

                if (_canvas != null) _canvas.drawBitmap(rotate, m_missile[i].getX(), m_missile[i].getY(), null);
            }
        }
    }

    @Override
    public void update(long gameTime, GameState _game) {
        // 아이템 획득 전에는 아이템 스프라이트만 업데이트
        if (itemState == Constants.STATE_ITEM_MADE) {
            super.update(gameTime, _game);
        }
        // 아이템이 발동된 후에는 미사일이 모두 화면 밖으로 나갔는지 체크, 모두 나갔으면 상태 변화
        else if (itemState == Constants.STATE_ITEM_ACTIONED) {
            m_outCount = 0;
            for (int i = 0; i < 8; i++) {
                m_missile[i].update();
                if (m_missile[i].isOut()) {
                    m_outCount++;
                }
            }

            if (m_outCount >= 8) {
                // missile[8] garbage collector 가 자동으로 지워주나??
                itemState = Constants.STATE_ITEM_FINISHED;
            }
        }
    }
}
