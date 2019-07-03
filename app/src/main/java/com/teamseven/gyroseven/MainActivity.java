package com.teamseven.gyroseven;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.teamseven.gameframework.AppManager;
import com.teamseven.gameframework.GameView;

public class MainActivity extends AppCompatActivity {

    private GameView gameView ;
   // private EndDialog endDialog;
    private long backKeyPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init(){

        AppManager.getInstance().setContext(this);
        AppManager.getInstance().setGame(true);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        gameView = new GameView(this);
        setContentView(gameView);
        gameView.changeGameState(new IntroState());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        init();
    }

    @Override
    protected void onStop() {
        super.onStop();
        gameView.setThreadRun(false);
    }

    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis()>backKeyPressedTime+2000){
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "뒤로가기 버튼을 한 번 더 누르면 종료 됩니다.", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }

    }
}