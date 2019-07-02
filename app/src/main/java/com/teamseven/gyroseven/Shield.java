package com.teamseven.gyroseven;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Log;

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

    public void update(Player _player) {

        //m_matrix.setRotate(degrees, (getX() * 2 + getBitmap().getWidth()) / 2,
        //        (getY() * 2 + getBitmap().getHeight()) / 2);

        int x = _player.getCenterX() - getBitmap().getWidth() / 2;
        int y = _player.getCenterY() - getBitmap().getHeight() / 2;

        float degrees = _player.getDegrees();

        degrees += 180;
        double weight = 0;
        if (0 < degrees && degrees <= 45) {
            weight = degrees;
        } else if (45 < degrees && degrees <= 90) {
            weight = 90 - degrees;
        } else if (90 < degrees && degrees <= 135) {
            weight = degrees - 90;
        } else if (135 < degrees && degrees <= 180) {
            weight = 180 - degrees;
        } else if (180 < degrees && degrees <= 225) {
            weight = degrees - 180;
        } else if (225 < degrees && degrees <= 270) {
            weight = 270 - degrees;
        } else if (270 < degrees && degrees <= 315) {
            weight = degrees - 270;
        } else if (315 < degrees && degrees <= 360) {
            weight = 360 - degrees;
        }
        double value = Math.sqrt(40 * 40 / 2) / 45.0f * weight;

        setPosition(x + (int)value, y + (int)value);

        m_boundBox.set(m_x, m_y,
                m_x + getBitmap().getWidth(),
                m_y + getBitmap().getHeight());
    }
}
