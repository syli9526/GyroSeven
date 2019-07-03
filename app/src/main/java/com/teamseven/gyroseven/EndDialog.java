package com.teamseven.gyroseven;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.w3c.dom.Text;

public class EndDialog extends Dialog {

    private ImageButton m_main_btn;
    private ImageButton m_retry_btn;
    private TextView txt_score;
    private TextView txt_current;
    private int m_best;
    private int m_current;

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
        txt_current = findViewById(R.id.txtCurrentScore);

        //클릭 리스너 셋팅 (클릭버튼이 동작하도록 만들어줌.)
        m_main_btn.setOnClickListener(mMainBtnListener);
        m_retry_btn.setOnClickListener(mRetryBtnListener);

        txt_score.setText(String.valueOf(m_best));
        txt_current.setText(String.valueOf(m_current));
    }

    public void setHighScore(int score){
        txt_score.setText(String.valueOf(score));
    }

    //생성자 생성
    public EndDialog(@NonNull Context context, View.OnClickListener mainListener, View.OnClickListener retryListener,int current, int best) {
        super(context);
        this.mMainBtnListener = mainListener;
        this.mRetryBtnListener = retryListener;
        this.m_current = current;
        this.m_best =best;
    }

    public void showDialog(){
        this.show();
    }
}