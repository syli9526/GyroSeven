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
    protected int width = m_bitmap.getWidth();
    protected int height = m_bitmap.getHeight();
    Rect m_boundBox = new Rect();

    public Enemy(Bitmap bitmap) {
        super(bitmap);
    }


    public void move(int x, int y) {

        if (getY() > AppManager.getInstance().getDeviceSize().y || getX() > AppManager.getInstance().getDeviceSize().x
                || getY() < -this.width || getX() < -this.height)
            state = Constants.STATE_OUT;

        switch (movePattern) {

            case Constants.MOVE_PATTERN_1:
                setX(getX() + (int) speed);
                setY(getY() + (int) speed);
                break;
            case Constants.MOVE_PATTERN_2:
                setX(getX() - (int) speed);
                setY(getY() + (int) speed);
                break;
            case Constants.MOVE_PATTERN_3:
                setX(getX() - (int) speed);
                setY(getY() - (int) speed);
                break;
            case Constants.MOVE_PATTERN_4:
                setX(getX() + (int) speed);
                setY(getY() - (int) speed);
                break;
            case Constants.MOVE_PATTERN_5:
                if (x < getX())
                    setX(getX() - (int) speed);
                else
                    setX(getX() + (int) speed);
                if (y < getY())
                    setY(getY() - (int) speed);
                else
                    setY(getY() + (int) speed);
                break;
        }


    }

    public void update() {
        m_boundBox.set(getX(), getY(), getX() + this.width, getY() + this.height);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawBitmap(m_bitmap, getX(), getY(), null);
    }
}
