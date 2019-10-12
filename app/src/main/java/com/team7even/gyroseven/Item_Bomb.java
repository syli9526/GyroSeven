package com.team7even.gyroseven;

import android.graphics.Canvas;

import com.team7even.gameframework.AppManager;
import com.team7even.gameframework.SoundManager;
import com.team7even.gyroseven.R;

public class Item_Bomb extends Item  {

    Bomb m_bomb;                // 폭탄 애니메이션을 가지고 있는 클래스
    private long startTime = 0; // 폭탄 아이템 발동시간

    // 폭탄 아이템 비트맵 생성과 초기화
    public Item_Bomb() {
        super(AppManager.getInstance().getBitmap(R.drawable.item_bomb));
        super.initSpriteData(getBitmap().getWidth() / 2, getBitmap().getHeight(),2 , 2);

        ITEM_NUMBER = Constants.ITEM_BOMB;
    }

    // 아이템 획득 시 한 번 실행
    @Override
    public void actionItem(GameState _game) {
        itemState = Constants.STATE_ITEM_ACTIONED;
        SoundManager.getInstance().play(Constants.EFFECT_BOMB);

        // 폭발 애니메이션 객체 생성 및 위치 설정
        m_bomb = new Bomb();
        m_bomb.setPosition(getX() + getBitmapWidth() / 2 - m_bomb.getBitmapWidth() / 2,
                getY() + getBitmapWidth() / 2 - m_bomb.getBitmapHeight() / 2);
    }

    @Override
    public void draw(Canvas _canvas) {
        // 아이템 획득 전에는 아이템 이미지만 그려줌
        if (itemState == Constants.STATE_ITEM_MADE) {
            super.draw(_canvas);
        }
        // 아이템이 발동되면 폭발 애니메이션을 그려줌
        else if (itemState == Constants.STATE_ITEM_ACTIONED) {
            m_bomb.draw(_canvas);
        }
    }

    @Override
    public void update(long gameTime, GameState _game) {
        // 아이템 획득 전에는 아이템 스프라이트만 업데이트
        if (itemState == Constants.STATE_ITEM_MADE) {
            super.update(gameTime, _game);

            startTime = gameTime;
        }
        // 아이템이 발동된 후에는 아이템 발동 시간동안 폭발 애니메이션을 업데이트 하고, 상태 변화
        else if (itemState == Constants.STATE_ITEM_ACTIONED) {
            m_bomb.update(gameTime);

            if (gameTime - startTime > m_bomb.itemTime) {
                itemState = Constants.STATE_ITEM_FINISHED;

                return;
            }
        }
    }
}