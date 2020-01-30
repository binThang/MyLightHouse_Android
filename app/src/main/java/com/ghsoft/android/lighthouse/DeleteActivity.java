package com.ghsoft.android.lighthouse;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeleteActivity extends Dialog
{

    @BindView(R.id.down)
    LinearLayout down;
    @BindView(R.id.share)
    LinearLayout share;
    @BindView(R.id.tt1)
    TextView tt1;
    @BindView(R.id.tt2)
    TextView tt2;

    private View.OnClickListener mtopClickListener;
    private View.OnClickListener mbottomClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.activity_delete);
        ButterKnife.bind(this);

        tt1.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "komacon.ttf"));
        tt2.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "komacon.ttf"));


        // 클릭 이벤트 셋팅
        if (mtopClickListener != null && mbottomClickListener != null)
        {
            down.setOnClickListener(mtopClickListener);
            share.setOnClickListener(mbottomClickListener);
        }
    }

    // 클릭버튼이 확인과 취소 두개일때 생성자 함수로 이벤트를 받는다
    public DeleteActivity(Context context, View.OnClickListener topListener, View.OnClickListener bottomListener)
    {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mtopClickListener = topListener;
        this.mbottomClickListener = bottomListener;
    }
}

