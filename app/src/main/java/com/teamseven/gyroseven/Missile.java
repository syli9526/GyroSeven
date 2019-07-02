package com.teamseven.gyroseven;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.teamseven.gameframework.AppManager;
import com.teamseven.gameframework.GraphicObject;

public class Missile extends GraphicObject {

    protected float m_speed;
    protected int moveState;
    protected boolean m_out;

    protected Rect m_boundBox = new Rect();

    public Missile() {
        super(AppManager.getInstance().getBitmap(R.drawable.missile));

        m_speed = 30.0f;
        m_out = false;
    }

    public void setXY(int _x, int _y) {
        m_x = _x;
        m_y = _y;
    }

    public boolean isOut() {
        return m_out;
    }

    public void update() {
        if (getY() > AppManager.getInstance().getDeviceSize().y || getX() > AppManager.getInstance().getDeviceSize().x
                || getY() < -getBitmap() .getHeight() || getX() < -getBitmap().getWidth()) {
            m_out = true;
            return;
        }

        m_boundBox.set(m_x - getBitmap().getWidth()/3, m_y - getBitmap().getWidth()/3,
                m_x + getBitmap().getWidth() + getBitmap().getWidth()/3,
                m_y + getBitmap().getHeight() + getBitmap().getHeight()/3);

        if (moveState == Constants.MOVE_PATTERN_1) {
            m_y -= m_speed;
        }
        else if (moveState == Constants.MOVE_PATTERN_2) {
            m_x += m_speed;
            m_y -= m_speed;
        }
        else if (moveState == Constants.MOVE_PATTERN_3) {
            m_x += m_speed;
        }
        else if (moveState == Constants.MOVE_PATTERN_4) {
            m_x += m_speed;
            m_y += m_speed;
        }
        else if (moveState == Constants.MOVE_PATTERN_5) {
            m_y += m_speed;
        }
        else if (moveState == Constants.MOVE_PATTERN_6) {
            m_x -= m_speed;
            m_y += m_speed;
        }
        else if (moveState == Constants.MOVE_PATTERN_7) {
            m_x -= m_speed;
        }
        else if (moveState == Constants.MOVE_PATTERN_8) {
            m_x -= m_speed;
            m_y -= m_speed;
        }

        setXY(m_x, m_y);
    }
}
