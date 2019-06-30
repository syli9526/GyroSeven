package com.teamseven.gyroseven;

import com.teamseven.gameframework.AppManager;

import java.util.Random;

public class Enemy_2 extends Enemy {

    Random rand = new Random();

    public Enemy_2() {
        super(AppManager.getInstance().getBitmap(R.drawable.enemy_2));

        speed = rand.nextInt(4) + 1;
    }
}
