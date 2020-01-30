package com.ghsoft.android.lighthouse;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShareActivity extends Dialog {

    @BindView(R.id.down)
    LinearLayout down;
    @BindView(R.id.share)
    LinearLayout share;

    private View.OnClickListener mtopClickListener;
    private View.OnClickListener mbottomClickListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.activity_share);
        ButterKnife.bind(this);



        // 클릭 이벤트 셋팅
        if (mtopClickListener != null && mbottomClickListener != null) {
            down.setOnClickListener(mtopClickListener);
            share.setOnClickListener(mbottomClickListener);

        }


    }


    // 클릭버튼이 확인과 취소 두개일때 생성자 함수로 이벤트를 받는다
    public ShareActivity(Context context, View.OnClickListener topListener, View.OnClickListener bottomListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mtopClickListener = topListener;
        this.mbottomClickListener = bottomListener;
    }




}

