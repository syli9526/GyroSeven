package com.teamseven.gyroseven;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class EndDialog extends Dialog {

    private ImageButton m_main_btn;
    private ImageButton m_retry_btn;
    private TextView txt_score;

    private View.OnClickListener mMainBtnListener;
    private View.OnClickListener mRetryBtnListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //다이얼로그 밖의 화면은 흐리게 만들어줌
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        setContentView(R.layout.activity_main);

        //셋팅
        m_main_btn = (ImageButton) findViewById(R.id.mainButton);
        m_retry_btn = (ImageButton) findViewById(R.id.retryButton);
        txt_score = findViewById(R.id.txtScore);

        //클릭 리스너 셋팅 (클릭버튼이 동작하도록 만들어줌.)
        m_main_btn.setOnClickListener(mMainBtnListener);
        m_retry_btn.setOnClickListener(mRetryBtnListener);
    }

    public void setHighScore(int score){
        txt_score.setText(String.valueOf(score));
    }

    //생성자 생성
    public EndDialog(@NonNull Context context, View.OnClickListener mainListener, View.OnClickListener retryListener) {
        super(context);
        this.mMainBtnListener = mainListener;
        this.mRetryBtnListener = retryListener;
    }

    public void showDialog(){
        this.show();
    }
}