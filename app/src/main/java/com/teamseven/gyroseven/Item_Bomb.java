package com.teamseven.gyroseven;

import android.graphics.Canvas;
import android.util.Log;

import com.teamseven.gameframework.AppManager;
import com.teamseven.gameframework.SoundManager;

public class Item_Bomb extends Item  {

    Bomb m_bomb;
    private long startTime = 0;

    public Item_Bomb() {
        super(AppManager.getInstance().getBitmap(R.drawable.item_bomb));
        super.initSpriteData(getBitmap().getWidth() / 2, getBitmap().getHeight(),2 , 2);

        ITEM_NUMBER = Constants.ITEM_BOMB;
    }

    @Override
    public void draw(Canvas _canvas) {
        if (itemState == Constants.STATE_ITEM_MADE) {
            super.draw(_canvas);
        }
        else if (itemState == Constants.STATE_ITEM_ACTIONED) {
            m_bomb.draw(_canvas);
        }
    }

    @Override
    public void update(long gameTime, GameState _game) {
        if (itemState == Constants.STATE_ITEM_MADE) {
            super.update(gameTime, _game);

            startTime = gameTime;
        }
        else if (itemState == Constants.STATE_ITEM_ACTIONED) {
            m_bomb.update(gameTime);

            if (gameTime - startTime > m_bomb.itemTime) {
                itemState = Constants.STATE_ITEM_FINISHED;

                return;
            }
        }
    }

    @Override
    public void actionItem(GameState _game) {
        itemState = Constants.STATE_ITEM_ACTIONED;
        SoundManager.getInstance().play(Constants.EFFECT_BOMB);

        m_bomb = new Bomb();
        m_bomb.setPosition(getX() - getBitmapWidth() / 2, getY() - getBitmapHeight());
    }
}