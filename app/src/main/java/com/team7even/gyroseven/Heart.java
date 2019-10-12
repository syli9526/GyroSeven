package com.team7even.gyroseven;

import com.team7even.gameframework.AppManager;
import com.team7even.gameframework.GraphicObject;
import com.team7even.gyroseven.R;

public class Heart extends GraphicObject {
    // 하트 비트맵 지정
    public Heart() {
        super(AppManager.getInstance().getBitmap(R.drawable.heart));
    }
}