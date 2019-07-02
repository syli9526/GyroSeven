package com.teamseven.gyroseven;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;

import com.teamseven.gameframework.AppManager;

public class Item_Shield extends Item {

    Shield m_shield;
    private long startTime = 0;

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

            _canvas.drawBitmap(rotate, m_shield.getX(), m_shield.getY(), null);
        }
    }

    @Override
    public void actionItem(GameState _game) {
        itemState = Constants.STATE_ITEM_ACTIONED;

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

            int x = _game.m_player.getCenterX() - m_shield.getBitmap().getWidth() / 2;
            int y = _game.m_player.getCenterY() - m_shield.getBitmap().getHeight() / 2;
            /*
            float sp[] = new float[2];
            sp[0] = (float)_game.m_player.getCenterX();
            sp[1] = (float)_game.m_player.getCenterY();
            float dp[] = new float[2];

            _game.m_player.getMatrix().mapPoints(dp, sp);
            float fp[] = {dp[0] - sp[0], dp[1] - sp[1]};

            Log.d("myCheck", "px: " + Float.toString(sp[0]));
            Log.d("myCheck", "py : " + Float.toString(sp[1]));
            Log.d("myCheck", "dx : " + Float.toString(dp[0]));
            Log.d("myCheck", "dy : " + Float.toString(dp[1]));

            shield.update((int)sp[0], (int)sp[1], (int)fp[0], (int)fp[1], _game.m_player.getMatrix());
            */

            m_shield.update(x, y, _game.m_player.getMatrix());
        }
    }


}
