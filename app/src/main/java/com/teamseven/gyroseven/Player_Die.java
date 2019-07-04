package com.teamseven.gyroseven;

import com.teamseven.gameframework.AppManager;
import com.teamseven.gameframework.SpriteAnimation;

public class Player_Die extends SpriteAnimation {

    // 플레이어 폭발 애니메이션 지속시간
    protected long aniTime = 700;

    // 플레이어 폭발 애니메이션 비트맵 생성과 초기화
    public Player_Die() {
        super(AppManager.getInstance().getBitmap(R.drawable.player_die));
        super.initSpriteData(getBitmap().getWidth() / 6, getBitmap().getHeight(),8, 6);
    }
}
