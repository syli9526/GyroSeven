package com.teamseven.gyroseven;

import android.graphics.Matrix;
import android.graphics.Rect;

import com.teamseven.gameframework.AppManager;
import com.teamseven.gameframework.GraphicObject;

public class Shield extends GraphicObject {

    protected long itemTime = 3000;

    protected Rect m_boundBox = new Rect();
    protected Matrix m_matrix;

    public Shield() {
        super(AppManager.getInstance().getBitmap(R.drawable.shield));

        m_matrix = new Matrix();
    }

    public void update(int px, int py, Matrix _matrix) {
        setPosition(px, py);

        m_matrix = _matrix;

        m_boundBox.set(m_x, m_y,
                m_x + getBitmap().getWidth(),
                m_y + getBitmap().getHeight());
    }
}
