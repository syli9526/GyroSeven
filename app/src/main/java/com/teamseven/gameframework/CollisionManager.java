package com.teamseven.gameframework;

import android.graphics.Rect;
import android.util.Log;

public class CollisionManager {

    public static boolean checkBoxToBox(Rect _rt1, Rect _rt2) {
        if (_rt1.right > _rt2.left && _rt1.left < _rt2.right &&
                _rt1.top < _rt2.bottom && _rt1.bottom > _rt2.top) {
            return true;
        }
        return false;
    }

    public static boolean checkCircleToCircle(Rect _rt1, Rect _rt2) {
        double dis1, dis2;
        double radius1 = (_rt1.right - _rt1.left) / 5 * 2;
        double radius2 = (_rt2.right - _rt2.left) / 2;

        dis1 = Math.sqrt((_rt1.centerX() - _rt2.centerX()) * (_rt1.centerX() - _rt2.centerX()) + (_rt1.centerY() - _rt2.centerY()) * (_rt1.centerY() - _rt2.centerY()));
        dis2 = radius1 + radius2;

        if (dis1 > dis2) return false;
        else return true;
    }
}
