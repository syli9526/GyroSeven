package com.teamseven.gyroseven;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.teamseven.gameframework.AppManager;
import com.teamseven.gameframework.SpriteAnimation;

public class Player_Die extends SpriteAnimation {

    protected long aniTime = 700;

    public Player_Die() {
        super(AppManager.getInstance().getBitmap(R.drawable.player_die));
        super.initSpriteData(getBitmap().getWidth() / 6, getBitmap().getHeight(),8, 6);
    }
}
