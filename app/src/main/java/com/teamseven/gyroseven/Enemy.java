package com.teamseven.gyroseven;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.teamseven.gameframework.AppManager;
import com.teamseven.gameframework.GraphicObject;

public class Enemy extends GraphicObject {
    public int state = Constants.STATE_NORMAL; //적의 상태
    protected int movePattern; // 적의 움직임 패턴 종류
    protected int grade; // 적 처치시 받을 점수
    protected float speed; // 적 스피드
    protected int x_weight; // x방향으로 랜덤 가중치
    protected int y_weight;// y방향으로 랜덤 가중치

    Rect m_boundBox = new Rect();

    public Enemy(Bitmap bitmap) {
        super(bitmap);

    }


    public void move(int x, int y){
        // 디바이스 밖으로 나가면 상태를 STATE_OUT으로 변경
        if (getY() > AppManager.getInstance().getDeviceSize().y || getX() > AppManager.getInstance().getDeviceSize().x
                || getY() < -getBitmap().getWidth() || getX() < -getBitmap().getHeight())
            state = Constants.STATE_OUT;
    }


    public void update() {
        //적의 위치 업데이트 (충돌 체크를 위한)
        m_boundBox.set(getX(), getY(), getX() + getBitmap().getWidth(), getY() + getBitmap().getHeight());
    }


    public void speedUp(float plus) {
        this.speed += plus;
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }
}