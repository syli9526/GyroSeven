package com.team7even.gyroseven;

import android.graphics.Rect;

import com.team7even.gameframework.AppManager;
import com.team7even.gameframework.GraphicObject;
import com.team7even.gyroseven.R;

public class Missile extends GraphicObject {

    protected float m_speed;
    protected int moveState;    // 이동 패턴
    protected boolean m_out;    // 화면을 벗어났는지 체크

    protected Rect m_boundBox = new Rect();

    public Missile() {
        super(AppManager.getInstance().getBitmap(R.drawable.missile));

        m_speed = 30.0f;
        m_out = false;
    }

    public boolean isOut() {
        return m_out;
    }

    public void update() {
        // 화면 밖으로 나감을 체크
        if (getY() > AppManager.getInstance().getDeviceSize().y || getX() > AppManager.getInstance().getDeviceSize().x
                || getY() < -getBitmap() .getHeight() || getX() < -getBitmap().getWidth()) {
            m_out = true;
            return;
        }

        // 미사일 충돌범위 설정
        m_boundBox.set(m_x - getBitmap().getWidth()/3, m_y - getBitmap().getWidth()/3,
                m_x + getBitmap().getWidth() + getBitmap().getWidth()/3,
                m_y + getBitmap().getHeight() + getBitmap().getHeight()/3);

        // 패턴에 따라 좌표 이동
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

        setPosition(m_x, m_y);
    }
}
