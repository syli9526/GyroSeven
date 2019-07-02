package com.teamseven.gyroseven;

import com.teamseven.gameframework.AppManager;
import com.teamseven.gameframework.GraphicObject;

public class SpeedUp extends GraphicObject {

    public SpeedUp() {
        super(AppManager.getInstance().getBitmap(R.drawable.speed_up));
        setPosition((AppManager.getInstance().getDeviceSize().x - getBitmap().getWidth()) / 2,
                AppManager.getInstance().getDeviceSize().y - getBitmap().getHeight() - 10);
    }

}
