package com.teamseven.gyroseven;

import com.teamseven.gameframework.AppManager;
import com.teamseven.gameframework.GameView;

public class Item_Heart extends Item {
    public Item_Heart() {
        super(AppManager.getInstance().getBitmap(R.drawable.item_heart));
        super.initSpriteData(getBitmap().getWidth() / 2, getBitmap().getHeight(),2 , 2);
    }

    @Override
    public void actionItem(GameState _game) {
        _game.m_player.addLife();
    }
}
