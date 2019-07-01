package com.teamseven.gyroseven;

import com.teamseven.gameframework.AppManager;

public class Enemy_2 extends Enemy {
    public Enemy_2() {
        super(AppManager.getInstance().getBitmap(R.drawable.enemy_2));
        speed = 2f;
    }
}
