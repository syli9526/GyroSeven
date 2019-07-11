package com.teamseven.gameframework;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class GraphicObject {

    protected Bitmap m_bitmap; // 이미지
    // 이미지 위치 좌표
    protected int m_x;
    protected int m_y;

    // 생성자를 통해 이미지 저장
    public GraphicObject(Bitmap bitmap) {
        m_bitmap = bitmap;
    }

    // 이미지를 canvas에 그려줌
    public void draw(Canvas _canvas) {
        if(_canvas!=null) _canvas.drawBitmap(m_bitmap, m_x, m_y, null);
    }

    /**
     *
     * 각 변수의 get/set 메소드
     */

    public void setPosition(int _x, int _y) {
        m_x = _x;
        m_y = _y;
    }

    public Bitmap getBitmap() {
        return m_bitmap;
    }

    public void setBitmap(Bitmap _bitmap) {
        m_bitmap = _bitmap;
    }

    public int getX() {
        return m_x;
    }

    public int getY() {
        return m_y;
    }

    public void setX(int x) {
        this.m_x = x;
    }

    public void setY(int y) {
        this.m_y = y;
    }
}
