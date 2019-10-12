package com.team7even.gyroseven;

import android.graphics.Matrix;
import android.graphics.Rect;

import com.team7even.gameframework.AppManager;
import com.team7even.gameframework.GraphicObject;
import com.team7even.gyroseven.R;

public class Shield extends GraphicObject {

    // 쉴드 지속 시간
    protected long itemTime = 3000;

    protected Rect m_boundBox = new Rect();
    protected Matrix m_matrix;

    public Shield() {
        super(AppManager.getInstance().getBitmap(R.drawable.shield));

        m_matrix = new Matrix();
    }

    public void update(Player _player) {

        // 쉴드의 기본 위치 설정
        int x = _player.getCenterX() - getBitmap().getWidth() / 2;
        int y = _player.getCenterY() - getBitmap().getHeight() / 2;

        // 플레이어의 회전 각도에 따라서 플레이어 좌표 값의 오차가 일어난다.
        // ex) 플레이어 Bitmap 회전율 0도에서 90도까지 같은 x, y좌표 유지
        // 플레이어 x, y 좌표 오차는 45도(135, 225, 315)에서 가장 크다.
        // 오차 범위만큼 쉴드의 실제 위치도 그에 맞게 조정한 알고리즘, weight로 오차 범위의 가중치를 구한 후
        // 수식을 통해 오차 값을 계산한다.

        float degrees = _player.getDegrees();

        degrees += 180;
        double weight = 0;

        /*
        int i = 0;

        while (degrees > 90) {
            degrees -= 90;
            i++;
        }
        if (0 < degrees && degrees <= 45) {
            weight = degrees - 90 * i;
        } else if (45 < degrees && degrees <= 90) {
            weight = (90 * (i + 1)) - degrees;
        }
        */
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
        // 40은 플레이어의 비트맵 크기
        double value = Math.sqrt(40 * 40 / 2) / 45.0f * weight;

        setPosition(x + (int)value, y + (int)value);

        // 충돌범위 업데이트
        m_boundBox.set(m_x, m_y,
                m_x + getBitmap().getWidth(),
                m_y + getBitmap().getHeight());
    }
}
