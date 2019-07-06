package com.teamseven.gyroseven;

import com.teamseven.gameframework.AppManager;
import com.teamseven.gameframework.SoundManager;

public class Item_Heart extends Item {
    // 하트 아이템 비트맵 생성과 초기화
    public Item_Heart() {
        super(AppManager.getInstance().getBitmap(R.drawable.item_heart));
        super.initSpriteData(getBitmap().getWidth() / 2, getBitmap().getHeight(),2 , 2);

        ITEM_NUMBER = Constants.ITEM_HEART;
    }

    // 아이템 획득 시 한 번 실행
    @Override
    public void actionItem(GameState _game) {
        itemState = Constants.STATE_ITEM_ACTIONED;
        SoundManager.getInstance().play(Constants.EFFECT_HEART);

        _game.m_player.addLife(); // 플레이어 하트 1 증가
        itemState = Constants.STATE_ITEM_FINISHED;
    }
}
