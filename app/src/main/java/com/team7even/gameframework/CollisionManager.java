package com.team7even.gameframework;

import android.graphics.Rect;

public class CollisionManager {

    // 사각 충돌 메소드
    public static boolean checkBoxToBox(Rect _rt1, Rect _rt2) {
        if (_rt1.right > _rt2.left && _rt1.left < _rt2.right &&
                _rt1.top < _rt2.bottom && _rt1.bottom > _rt2.top) {
            return true;
        }
        return false;
    }

    // 원 충돌 메소드
    // Rect의 변 길이를 원의 지름으로 생각해 계산했다.
    // 모든 오브젝트가 정사각형이기 때문에 가능
    public static boolean checkCircleToCircle(Rect _rt1, Rect _rt2) {
        double dis1, dis2;
        double radius1 = (_rt1.right - _rt1.left) / 2;
        double radius2 = (_rt2.right - _rt2.left) / 2;

        dis1 = Math.sqrt((_rt1.centerX() - _rt2.centerX()) * (_rt1.centerX() - _rt2.centerX()) + (_rt1.centerY() - _rt2.centerY()) * (_rt1.centerY() - _rt2.centerY()));
        dis2 = radius1 + radius2;

        if (dis1 > dis2) return false;
        else return true;
    }
}
