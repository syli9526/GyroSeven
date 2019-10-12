package com.team7even.gameframework;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

public class SpriteAnimation extends GraphicObject {

    protected Rect m_rect;
    protected int m_fps;
    protected int m_iFrames;
    protected int m_currentFrame;
    protected int m_spriteWIdth;
    protected int m_spriteHeight;
    protected long m_frameTimer;
    protected Matrix m_matrix;

    public SpriteAnimation(Bitmap _bitmap) {
        super(_bitmap);

        m_rect = new Rect(0, 0, 0, 0);
        m_matrix = new Matrix();
        m_frameTimer = 0;
        m_currentFrame = 0;
    }

    public int getBitmapWidth() { return m_spriteWIdth;}
    public int getBitmapHeight() { return m_spriteHeight;}

    public void initSpriteData(int _width, int _height, int _fps, int _iFrames) {
        m_spriteWIdth = _width;
        m_spriteHeight = _height;
        m_fps = 1000 / _fps;
        m_iFrames = _iFrames;

        m_rect.top = 0;
        m_rect.bottom = m_spriteHeight;
        m_rect.left = 0;
        m_rect.right = m_spriteWIdth;
    }

    @Override
    public void draw(Canvas _canvas) {


        Rect dest = new Rect(getX(), getY(),
                getX() + m_spriteWIdth, getY() + m_spriteHeight);

        if(_canvas!=null) _canvas.drawBitmap(getBitmap(), m_rect, dest, null);
    }

    public void update(long gameTime) {

        if (gameTime > m_frameTimer + m_fps) {
            m_frameTimer = gameTime;
            m_currentFrame += 1;

            if (m_currentFrame >= m_iFrames) m_currentFrame = 0;
        }
        m_rect.left = m_currentFrame * m_spriteWIdth;
        m_rect.right = m_rect.left + m_spriteWIdth;
    }

    public int getIFrames() {
        return m_iFrames;
    }
    public Matrix getMatrix() { return m_matrix; }
}
