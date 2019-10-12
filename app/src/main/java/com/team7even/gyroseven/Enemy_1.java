package com.team7even.gyroseven;

import com.team7even.gameframework.AppManager;
import com.team7even.gyroseven.R;

public class Enemy_1 extends Enemy {

    public Enemy_1() {
        super(AppManager.getInstance().getBitmap(R.drawable.enemy));
        speed = 1.7f;
        grade = 10;
    }

    @Override
    public void move(int x, int y) {

        super.move(x, y);
        switch (movePattern) {

            case Constants.MOVE_PATTERN_1:
                setX(getX() + (int) speed * x_weight);
                setY(getY() + (int) speed * y_weight);
                break;
            case Constants.MOVE_PATTERN_2:
                setX(getX() - (int) speed * x_weight);
                setY(getY() + (int) speed * y_weight);
                break;
            case Constants.MOVE_PATTERN_3:
                setX(getX() - (int) speed * x_weight);
                setY(getY() - (int) speed * y_weight);
                break;
            case Constants.MOVE_PATTERN_4:
                setX(getX() + (int) speed * x_weight);
                setY(getY() - (int) speed * y_weight);
                break;
        }

    }
}
