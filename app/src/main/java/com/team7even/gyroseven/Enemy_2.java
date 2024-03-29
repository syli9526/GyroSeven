package com.team7even.gyroseven;

import com.team7even.gameframework.AppManager;
import com.team7even.gyroseven.R;

public class Enemy_2 extends Enemy {
    public Enemy_2() {
        super(AppManager.getInstance().getBitmap(R.drawable.enemy_2));
        speed = 2f;
        grade = 20;

    }

    @Override
    public void move(int x, int y) {
        super.move(x, y);
        if (x < getX()) setX(getX() - (int) speed);
        else setX(getX() + (int) speed);
        if (y < getY()) setY(getY() - (int) speed);
        else setY(getY() + (int) speed);

    }
}