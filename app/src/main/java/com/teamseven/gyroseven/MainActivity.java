package com.teamseven.gyroseven;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.teamseven.gameframework.AppManager;
import com.teamseven.gameframework.GameView;

public class MainActivity extends AppCompatActivity {

    private GameView gameView ;
    private EndDialog endDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        endDialog = new EndDialog(this,positiveListener,negativeListener);
        AppManager.getInstance().setDialog(endDialog);
        AppManager.getInstance().setGame(true);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        gameView = new GameView(this);
        setContentView(gameView);
        gameView.changeGameState(new IntroState());

    }

    private View.OnClickListener positiveListener = new View.OnClickListener() {
        public void onClick(View v) {
            endDialog.dismiss();
            gameView.changeGameState(new IntroState());
        }
    };

    private View.OnClickListener negativeListener = new View.OnClickListener() {
        public void onClick(View v) {
            endDialog.dismiss();
            gameView.changeGameState(new GameState());
        }
    };

}