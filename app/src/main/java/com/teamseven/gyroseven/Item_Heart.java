package com.teamseven.gyroseven;

import com.teamseven.gameframework.AppManager;
import com.teamseven.gameframework.SoundManager;

public class Item_Heart extends Item {
    public Item_Heart() {
        super(AppManager.getInstance().getBitmap(R.drawable.item_heart));
        super.initSpriteData(getBitmap().getWidth() / 2, getBitmap().getHeight(),2 , 2);

        ITEM_NUMBER = Constants.ITEM_HEART;
    }

    @Override
    public void actionItem(GameState _game) {
        itemState = Constants.STATE_ITEM_ACTIONED;
        SoundManager.getInstance().play(Constants.EFFECT_HEART);

        _game.m_player.addLife();
        itemState = Constants.STATE_ITEM_FINISHED;
    }
}
