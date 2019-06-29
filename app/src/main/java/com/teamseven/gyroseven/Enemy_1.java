package com.teamseven.gyroseven;

import com.teamseven.gameframework.AppManager;

public class Enemy_1 extends Enemy {

    public Enemy_1() {
        super(AppManager.getInstance().getBitmap(R.drawable.enemy1));
        this.initSpriteData(62*3,104*3,3,6);
        m_hp = 10;
        m_speed = 5.0f;
    }
}
