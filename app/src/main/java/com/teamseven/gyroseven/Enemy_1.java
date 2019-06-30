package com.teamseven.gyroseven;

import com.teamseven.gameframework.AppManager;

public class Enemy_1 extends Enemy {
    public Enemy_1() {
        super(AppManager.getInstance().getBitmap(R.drawable.enemy));
        speed =1.7f;
    }
}
