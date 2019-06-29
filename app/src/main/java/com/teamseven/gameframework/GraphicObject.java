package com.teamseven.gameframework;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class GraphicObject {

    protected Bitmap m_bitmap;
    protected int m_x;
    protected int m_y;

    public GraphicObject(Bitmap bitmap) {
        m_bitmap = bitmap;
    }

    public void draw(Canvas _canvas) {
        _canvas.drawBitmap(m_bitmap, m_x, m_y, null);

    }

    public void setPosition(int _x, int _y) {
        m_x = _x;
        m_y = _y;
    }

    public Bitmap getBitmap() { return m_bitmap; }
    public int getX() { return m_x; }
    public int getY() { return m_y; }
}
