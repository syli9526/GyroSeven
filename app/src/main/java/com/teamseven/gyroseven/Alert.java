package com.teamseven.gyroseven;

import com.teamseven.gameframework.AppManager;
import com.teamseven.gameframework.GraphicObject;

public class Alert extends GraphicObject {

    public Alert() {
        super(AppManager.getInstance().getBitmap(R.drawable.ready));
        setPosition((AppManager.getInstance().getDeviceSize().x - getBitmap().getWidth()) / 2,
                (AppManager.getInstance().getDeviceSize().y - getBitmap().getHeight()) / 2);
    }

}
