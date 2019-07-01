package com.teamseven.gyroseven;

import com.teamseven.gameframework.AppManager;

public class Item_Heart extends Item {
    public Item_Heart() {
        super(AppManager.getInstance().getBitmap(R.drawable.item_heart));
        super.initSpriteData(getBitmapWidth() / 2, getBitmapHeight(), 2, 2);
    }

    @Override
    public void actionItem(GameState _game) {
        _game.m_player.addLife();
    }
}
