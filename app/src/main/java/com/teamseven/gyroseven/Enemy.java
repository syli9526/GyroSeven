package com.teamseven.gyroseven;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.teamseven.gameframework.AppManager;
import com.teamseven.gameframework.GraphicObject;

public class Enemy extends GraphicObject {
    public int state = Constants.STATE_NORMAL;

    protected int movePattern;

    protected float speed;
    protected int x_weight;
    protected int y_weight;

    Rect m_boundBox = new Rect();

    public Enemy(Bitmap bitmap) {
        super(bitmap);

    }


    public void move(int x, int y) {

        if (getY() > AppManager.getInstance().getDeviceSize().y || getX() > AppManager.getInstance().getDeviceSize().x
                || getY() < -getBitmap().getWidth() || getX() < -getBitmap().getHeight())

            state = Constants.STATE_OUT;

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
            case Constants.MOVE_PATTERN_5:
                if (x < getX()) setX(getX() - (int) speed);
                else setX(getX() + (int) speed);
                if (y < getY()) setY(getY() - (int) speed);
                else setY(getY() + (int) speed);

                break;
        }


    }

    public void update() {
        m_boundBox.set(getX(), getY(), getX() + getBitmap().getWidth(), getY() + getBitmap().getHeight());
    }

    public void speedUp(float plus) {
        this.speed += plus;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawBitmap(m_bitmap, getX(), getY(), null);
    }
}