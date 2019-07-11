package com.teamseven.gyroseven;

import com.teamseven.gameframework.AppManager;
import com.teamseven.gameframework.GraphicObject;

public class Alert extends GraphicObject {


    public Alert() {
        // ready이미지로 초기화, 위치 초기화화
       super(AppManager.getInstance().getBitmap(R.drawable.ready));
        setPosition((AppManager.getInstance().getDeviceSize().x - getBitmap().getWidth()) / 2,
                (AppManager.getInstance().getDeviceSize().y - getBitmap().getHeight()) / 2);
    }

}
