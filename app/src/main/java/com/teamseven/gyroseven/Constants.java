package com.teamseven.gyroseven;

public class Constants {

    // 적 움직임 패턴
    public static final int MOVE_PATTERN_1 = 0, MOVE_PATTERN_2 = 1, MOVE_PATTERN_3 = 2, MOVE_PATTERN_4 = 3;
    public static final int MOVE_PATTERN_5 = 4, MOVE_PATTERN_6 = 5, MOVE_PATTERN_7 = 6, MOVE_PATTERN_8 = 7;

    // 적의 상태
    public static final int STATE_NORMAL = 0;
    public static final int STATE_OUT = 1;

    // 플레이어 상태
    public static final int STATE_EXPLANED = 1;
    public static final int STATE_DEAD = 2;
    public static final int STATE_ENDED = 3;


    public static final int[] BACKGROUND = {R.drawable.intro, R.drawable.background1, R.drawable.background2, R.drawable.background3};

    public static final int ITEM_COUNT = 4;

    // 아이템 번호
    public static final int ITEM_HEART = 0, ITEM_MISSILE = 1, ITEM_SHIELD = 2, ITEM_BOMB = 3;

    // 아이템 상태
    public static final int STATE_ITEM_MADE = 0, STATE_ITEM_ACTIONED = 1, STATE_ITEM_FINISHED = 2;


}
