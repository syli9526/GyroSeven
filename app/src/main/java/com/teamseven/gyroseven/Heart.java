package com.teamseven.gyroseven;

import com.teamseven.gameframework.AppManager;
import com.teamseven.gameframework.GraphicObject;

public class Heart extends GraphicObject {
    public Heart() {
        super(AppManager.getInstance().getBitmap(R.drawable.heart));
    }
}