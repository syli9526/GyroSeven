package com.teamseven.gyroseven;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import com.teamseven.gameframework.AppManager;
import com.teamseven.gameframework.CollisionManager;

public class Item_Missile extends Item {

    Missile missile[] =  new Missile[8];
    protected int m_outCount;

    public Item_Missile() {
        super(AppManager.getInstance().getBitmap(R.drawable.item_missile));
        super.initSpriteData(getBitmap().getWidth() / 2, getBitmap().getHeight(),2 , 2);

        ITEM_NUMBER = Constants.ITEM_MISSILE;
    }

    @Override
    public void actionItem(GameState _game) {
        itemState = Constants.STATE_ITEM_ACTIONED;

        for (int i = 0; i < 8; i++) {
            missile[i] = new Missile();
            missile[i].moveState = i;
            missile[i].setXY(getX(), getY());
        }
    }

    @Override
    public void draw(Canvas _canvas) {
        if (itemState == Constants.STATE_ITEM_MADE)
            super.draw(_canvas);

        else if (itemState == Constants.STATE_ITEM_ACTIONED) {
            for (int i = 0; i < 8; i++) {
                Matrix matrix = new Matrix();
                matrix.setRotate(45 * i);

                Bitmap rotate = Bitmap.createBitmap(missile[i].getBitmap(), 0, 0,
                        missile[i].getBitmap().getWidth(), missile[i].getBitmap().getHeight(), matrix, true);

                _canvas.drawBitmap(rotate, missile[i].getX(), missile[i].getY(), null);
            }
        }
    }

    @Override
    public void update(long gameTime) {
        if (itemState == Constants.STATE_ITEM_MADE) {
            super.update(gameTime);
        }
        else if (itemState == Constants.STATE_ITEM_ACTIONED) {
            m_outCount = 0;
            for (int i = 0; i < 8; i++) {
                missile[i].update();
                if (missile[i].isOut()) {
                    m_outCount++;
                }
            }

            if (m_outCount >= 8) {
                // missile[8] garbage collector 가 자동으로 지워주나??
                itemState = Constants.STATE_ITEM_FINISHED;
            }
        }
    }
}
