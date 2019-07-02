package com.teamseven.gyroseven;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.teamseven.gameframework.AppManager;
import com.teamseven.gameframework.SpriteAnimation;

public class BackGround extends SpriteAnimation {

    Bitmap bitmap[] = new Bitmap[4];
    Rect m_BoundBox = new Rect();

    public BackGround(int backType) {
        super(null);
        super.setPosition(0, 0);

        for (int i = 0; i < 4; i++) {
            bitmap[i] = AppManager.getInstance().getBitmap(Constants.BACKGROUND[i]);
            if (i == 0) bitmap[i] = Bitmap.createScaledBitmap(bitmap[i], AppManager.getInstance().getDeviceSize().x * 2, AppManager.getInstance().getDeviceSize().y, true);
            else bitmap[i] = Bitmap.createScaledBitmap(bitmap[i], AppManager.getInstance().getDeviceSize().x, AppManager.getInstance().getDeviceSize().y, true);
        }

        switch (backType) {
            case 0:
                super.m_bitmap = bitmap[0];
                super.initSpriteData(m_bitmap.getWidth()/2, m_bitmap.getHeight(), 10, 2);
                break;
            case 1:
                super.m_bitmap = bitmap[1];
                super.initSpriteData( m_bitmap.getWidth(),  m_bitmap.getHeight(), 1, 1);
                break;
        }
    }

    public void changeBackGround(int back){

        switch (back){
            case 1:
            case 2:
                super.m_bitmap = bitmap[1];
                super.initSpriteData(getBitmapWidth(), getBitmapHeight(), 1, 1);
                break;
            case 3:
            case 4:
                super.m_bitmap = bitmap[2];
                super.initSpriteData(getBitmapWidth(), getBitmapHeight(), 1, 1);
                break;
            case 5:
            case 6:
                super.m_bitmap = bitmap[3];
                super.initSpriteData(getBitmapWidth(), getBitmapHeight(), 1, 1);
                break;
        }
    }

    @Override
    public void update(long gameTime) {
        super.update(gameTime);
        m_BoundBox.set(getX(), getY(), getX() + getBitmapWidth() , getY() + getBitmapHeight());
    }

}