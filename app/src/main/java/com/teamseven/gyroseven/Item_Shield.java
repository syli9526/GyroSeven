package com.teamseven.gyroseven;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.teamseven.gameframework.AppManager;

public class Item_Shield extends Item {

    Shield shield;
    private long shieldTime = 3000;
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
            Bitmap rotate = Bitmap.createBitmap(shield.getBitmap(), 0, 0,
                    shield.getBitmap().getWidth(), shield.getBitmap().getHeight(), shield.m_matrix, true);

            _canvas.drawBitmap(rotate, shield.getX(), shield.getY(), null);
        }
    }

    @Override
    public void actionItem(GameState _game) {
        itemState = Constants.STATE_ITEM_ACTIONED;

        shield = new Shield();
        shield.setXY(_game.m_player.getCenterX() - shield.getBitmap().getWidth() / 2,
                _game.m_player.getCenterY() - shield.getBitmap().getHeight() / 2);
    }

    @Override
    public void update(long gameTime, GameState _game) {
        if (itemState == Constants.STATE_ITEM_MADE) {
            super.update(gameTime, _game);

            startTime = gameTime;
        }

        else if (itemState == Constants.STATE_ITEM_ACTIONED) {

            if (gameTime - startTime > shieldTime) {
                itemState = Constants.STATE_ITEM_FINISHED;

                return;
            }

            int x = _game.m_player.getCenterX() - shield.getBitmap().getWidth() / 2;
            int y = _game.m_player.getCenterY() - shield.getBitmap().getHeight() / 2;

            shield.update(x, y, _game.m_player.getMatrix());
        }
    }


}
