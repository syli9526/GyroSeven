package com.team7even.gyroseven;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.team7even.gameframework.AppManager;
import com.team7even.gameframework.SoundManager;
import com.team7even.gyroseven.R;

public class Item_Shield extends Item {

    Shield m_shield;            // 쉴드 이미지를 가진 클래스
    private long startTime = 0; // 쉴드 발동 시장 저장

    public Item_Shield() {
        super(AppManager.getInstance().getBitmap(R.drawable.item_shield));
        super.initSpriteData(getBitmap().getWidth() / 2, getBitmap().getHeight(),2 , 2);

        ITEM_NUMBER = Constants.ITEM_SHIELD;
    }

    @Override
    public void draw(Canvas _canvas) {
        if (itemState == Constants.STATE_ITEM_MADE) {
            super.draw(_canvas);
        }

        else if (itemState == Constants.STATE_ITEM_ACTIONED) {
            Bitmap rotate = Bitmap.createBitmap(m_shield.getBitmap(), 0, 0,
                    m_shield.getBitmap().getWidth(), m_shield.getBitmap().getHeight(), m_shield.m_matrix, true);

            if(_canvas!=null)  _canvas.drawBitmap(rotate, m_shield.getX(), m_shield.getY(), null);
        }
    }

    @Override
    public void actionItem(GameState _game) {
        itemState = Constants.STATE_ITEM_ACTIONED;
        SoundManager.getInstance().play(Constants.EFFECT_SHIELD);

        m_shield = new Shield();
        m_shield.setPosition(_game.m_player.getCenterX() - m_shield.getBitmap().getWidth() / 2,
                _game.m_player.getCenterY() - m_shield.getBitmap().getHeight() / 2);
    }

    @Override
    public void update(long gameTime, GameState _game) {
        if (itemState == Constants.STATE_ITEM_MADE) {
            super.update(gameTime, _game);

            startTime = gameTime;
        }

        else if (itemState == Constants.STATE_ITEM_ACTIONED) {

            if (gameTime - startTime > m_shield.itemTime) {
                itemState = Constants.STATE_ITEM_FINISHED;

                return;
            }



            //m_matrix.reset();
            //m_matrix = _game.m_player.getMatrix();
            //m_matrix.setTranslate((getX() + _game.m_player.getX()) / 2,
             //       (getY() + _game.m_player.getY()) / 2);
            //m_matrix.setTranslate(getX(), getY());

            m_shield.update(_game.m_player);
        }
    }


}
