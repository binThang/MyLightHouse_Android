package com.ghsoft.android.lighthouse;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Dialog_Activity extends Dialog
{
    private TextView mTitleView;
    private TextView mLeftButton;
    private TextView mRightButton;
    private TextView mOkButton;
    private String mTitle;
    private String mContent;
    private String mleft;
    private String mright;
    private LinearLayout okLin;
    private LinearLayout twoLin;

    private View.OnClickListener mLeftClickListener;
    private View.OnClickListener mRightClickListener;
    private View.OnClickListener mOkClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.activity_dialog);

        mTitleView = (TextView) findViewById(R.id.txt_title);
        mLeftButton = (TextView) findViewById(R.id.btn_left);
        mRightButton = (TextView) findViewById(R.id.btn_right);
        mOkButton = (TextView) findViewById(R.id.btn_ok);
        okLin = (LinearLayout) findViewById(R.id.okLin);
        twoLin = (LinearLayout) findViewById(R.id.twoLin);

        mTitleView.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "komacon.ttf"));
        mLeftButton.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "komacon.ttf"));
        mRightButton.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "komacon.ttf"));
        mOkButton.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "komacon.ttf"));

        // 제목과 내용을 생성자에서 셋팅한다.
        mTitleView.setText(mTitle);
        mLeftButton.setText(mleft);
        mRightButton.setText(mright);

        // 클릭 이벤트 셋팅
        if (mLeftClickListener != null && mRightClickListener != null)
        {
            mLeftButton.setOnClickListener(mLeftClickListener);
            mRightButton.setOnClickListener(mRightClickListener);
        }
        else if (mLeftClickListener == null && mRightClickListener == null && mOkClickListener != null)
        {
            mOkButton.setOnClickListener(mOkClickListener);

        }
    }

    // 클릭버튼이 확인과 취소 두개일때 생성자 함수로 이벤트를 받는다
    public Dialog_Activity(Context context, String title, View.OnClickListener leftListener, View.OnClickListener rightListener)
    {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mTitle = title;
        this.mLeftClickListener = leftListener;
        this.mRightClickListener = rightListener;
    }

    // 클릭버튼이 하나일때 생성자 함수로 클릭이벤트를 받는다.
    public Dialog_Activity(Context context, String title, View.OnClickListener okListener)
    {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mTitle = title;
        this.mOkClickListener = okListener;
    }
}

