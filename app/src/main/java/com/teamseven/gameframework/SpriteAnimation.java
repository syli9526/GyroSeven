package com.teamseven.gameframework;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Log;

public class SpriteAnimation extends GraphicObject {

    protected Rect m_rect; // 이미지 내부 한 프레임의 위치 및 크기
    protected int m_fps; // 초당 프레임
    protected int m_iFrames; // 프레임 수
    protected int m_currentFrame; // 현재 프레임
    protected int m_spriteWIdth; // 한 프레임의 가로길이
    protected int m_spriteHeight; // 한 프레임의 세로길이
    protected long m_frameTimer; // 프레임 타이머
    protected Matrix m_matrix; // 실드 이동을 위한 Matrix

    // 생성자를 통해 각 변수 초기화
    public SpriteAnimation(Bitmap _bitmap) {
        super(_bitmap);

        m_rect = new Rect(0, 0, 0, 0);
        m_matrix = new Matrix();
        m_frameTimer = 0;
        m_currentFrame = 0;
    }

    public int getBitmapWidth() { return m_spriteWIdth;}
    public int getBitmapHeight() { return m_spriteHeight;}

    // 이미지 프레임 1개의 가로, 세로, 초당프레임, 프레임 개수를 받아와 지정
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

    // 이미지 내에서 프레임의 위치와 canvas에서 그려줄 위치를 지정하여 canvas에 그려줌
    @Override
    public void draw(Canvas _canvas) {
        Rect dest = new Rect(getX(), getY(),
                getX() + m_spriteWIdth, getY() + m_spriteHeight);

        if(_canvas!=null) _canvas.drawBitmap(getBitmap(), m_rect, dest, null);
    }

    public void update(long gameTime) {

        // 초당프레임 마다 프레임을 변화시킴
        if (gameTime > m_frameTimer + m_fps) {
            m_frameTimer = gameTime;
            m_currentFrame += 1;

            // 프ㅏ레임이 최대가 되면 다시 0으로 돌아가 반복 수햄
            if (m_currentFrame >= m_iFrames) m_currentFrame = 0;
        }
        // 이미지 내에서 프레임의 위치값 설정
        m_rect.left = m_currentFrame * m_spriteWIdth;
        m_rect.right = m_rect.left + m_spriteWIdth;
    }

    public int getIFrames() {
        return m_iFrames;
    }
    public Matrix getMatrix() { return m_matrix; }
}
