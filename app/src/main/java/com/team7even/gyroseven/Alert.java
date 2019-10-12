package com.team7even.gyroseven;

import com.team7even.gameframework.AppManager;
import com.team7even.gameframework.GraphicObject;
import com.team7even.gyroseven.R;

public class Alert extends GraphicObject {

    public Alert() {
        super(AppManager.getInstance().getBitmap(R.drawable.ready));
        setPosition((AppManager.getInstance().getDeviceSize().x - getBitmap().getWidth()) / 2,
                (AppManager.getInstance().getDeviceSize().y - getBitmap().getHeight()) / 2);
    }

}
