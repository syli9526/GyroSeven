package com.teamseven.gyroseven;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import com.teamseven.gameframework.AppManager;

public class Item_Missile extends Item {

    Missile m_missile[] =  new Missile[8];
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
            m_missile[i] = new Missile();
            m_missile[i].moveState = i;
            m_missile[i].setPosition(getX(), getY());
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

                Bitmap rotate = Bitmap.createBitmap(m_missile[i].getBitmap(), 0, 0,
                        m_missile[i].getBitmap().getWidth(), m_missile[i].getBitmap().getHeight(), matrix, true);

                _canvas.drawBitmap(rotate, m_missile[i].getX(), m_missile[i].getY(), null);
            }
        }
    }

    @Override
    public void update(long gameTime, GameState _game) {
        if (itemState == Constants.STATE_ITEM_MADE) {
            super.update(gameTime, _game);
        }
        else if (itemState == Constants.STATE_ITEM_ACTIONED) {
            m_outCount = 0;
            for (int i = 0; i < 8; i++) {
                m_missile[i].update();
                if (m_missile[i].isOut()) {
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
