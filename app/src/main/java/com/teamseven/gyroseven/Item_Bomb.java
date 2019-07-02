package com.teamseven.gyroseven;

import android.graphics.Bitmap;

import com.teamseven.gameframework.AppManager;

public class Item_Bomb extends Item  {
    public Item_Bomb() {
        super(AppManager.getInstance().getBitmap(R.drawable.item_bomb));
        super.initSpriteData(getBitmap().getWidth() / 2, getBitmap().getHeight(),2 , 2);

        ITEM_NUMBER = Constants.ITEM_BOMB;
    }

    @Override
    public void actionItem(GameState _game) {
        itemState = Constants.STATE_ITEM_ACTIONED;
    }
}
