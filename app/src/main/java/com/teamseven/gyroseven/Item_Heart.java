package com.teamseven.gyroseven;

import com.teamseven.gameframework.AppManager;
import com.teamseven.gameframework.SpriteAnimation;

public class Item_Heart extends SpriteAnimation implements Item {
    public Item_Heart() {
        super(AppManager.getInstance().getBitmap(R.drawable.heart));
    }

    @Override
    public void actionItem() {

    }
}
