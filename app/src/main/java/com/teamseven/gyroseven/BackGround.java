package com.teamseven.gyroseven;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.teamseven.gameframework.AppManager;
import com.teamseven.gameframework.SpriteAnimation;

public class BackGround extends SpriteAnimation {

    Bitmap bitmap[]= new Bitmap[4];
    Rect m_BoundBox = new Rect();

    public BackGround(int backType) {
        super(null);
        super.setPosition(0, 0);

        if (backType == 0) {
            bitmap[0] = AppManager.getInstance().getBitmap(Constants.BACKGROUND[0]);
            bitmap[0] = Bitmap.createScaledBitmap(bitmap[0], AppManager.getInstance().getDeviceSize().x * 2, AppManager.getInstance().getDeviceSize().y, true);
            super.m_bitmap = bitmap[0];
            super.initSpriteData(m_bitmap.getWidth() / 2, m_bitmap.getHeight(), 4, 2);
        } else {
            // 배경화면 화면(배경 1,2,3) 크기에 맞게 리사이징
            for (int i = 1; i < 4; i++) {
                bitmap[i] = AppManager.getInstance().getBitmap(Constants.BACKGROUND[i]);
                bitmap[i] = Bitmap.createScaledBitmap(bitmap[i], AppManager.getInstance().getDeviceSize().x, AppManager.getInstance().getDeviceSize().y, true);
                super.m_bitmap = bitmap[1];
                super.initSpriteData(m_bitmap.getWidth(), m_bitmap.getHeight(), 1, 1);
            }

        }
    }

    public void changeBackGround(int back) {

        //레벨에 맞춰 배경 변경 해주기
        switch (back) {
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
        m_BoundBox.set(getX(), getY(), getX() + getBitmapWidth(), getY() + getBitmapHeight());
    }

}