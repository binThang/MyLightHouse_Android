package com.ghsoft.android.lighthouse;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Time_Activity extends Dialog
{

    @BindView(R.id.btn_ok)
    TextView mOkButton;
    @BindView(R.id.text1)
    TextView text1;
    @BindView(R.id.text2)
    TextView text2;
    @BindView(R.id.tt1)
    TextView tt1;


    int nStart = 1;
    int nEnd = 24;

    static int aa = 1, bb = 1;
    AQuery aQuery;
    SharedPreferences pref;
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

        setContentView(R.layout.activity_time);
        ButterKnife.bind(this);
        aQuery = new AQuery(getContext());
        pref = getContext().getSharedPreferences("member", getContext().MODE_PRIVATE);

        tt1.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "komacon.ttf"));
        text1.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "komacon.ttf"));
        text2.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "komacon.ttf"));
        mOkButton.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "komacon.ttf"));

        String url = LHGlobal.BaseURL + "/member/disturbance_show";
        final Map<String, Object> params = new HashMap<>();
        params.put("member_idx", pref.getString("idx", ""));
        aQuery.ajax(url, params, String.class, new AjaxCallback<String>()
        {
            @Override
            public void callback(String url, String object, AjaxStatus status)
            {
                Log.d("방해_예지", params.toString());
                Log.d("방해_은진씨", object);
                try
                {
                    JSONObject jsonObject = new JSONObject(object);

                    String nopushtime1 = jsonObject.getString("nopushtime1");
                    String nopushtime2 = jsonObject.getString("nopushtime2");

                    String time1[] = nopushtime1.split(":");
                    String time2[] = nopushtime2.split(":");

                    if (time1[0].equals("00") && time2[0].equals("00"))
                    {
                        text1.setText("1");
                        text2.setText("1");
                    }
                    else
                    {
                        int time3 = Integer.parseInt(time1[0]);
                        int time4 = Integer.parseInt(time2[0]);
                        Log.d("다이얼로그1", time3 + "");
                        Log.d("다이얼로그2", time4 + "");
                        if (time3 < 10)
                        {
                            String time5[] = time1[0].split("0");
                            text1.setText(time5[1]);
                        }
                        else
                        {
                            text1.setText(time1[0]);
                        }
                        if (time4 < 10)
                        {
                            String time6[] = time2[0].split("0");
                            text2.setText(time6[1]);
                        }
                        else
                        {
                            text2.setText(time2[0]);
                        }
                    }

                } catch (JSONException e)
                {

                }
            }
        }.header("User-Agent", "gh_mobile{}"));

        mOkButton.setOnClickListener(mOkClickListener);
    }

    @OnClick({R.id.btn1, R.id.btn2})
    void onClick(View v)
    {
        String getString = String.valueOf(text1.getText());
        int curent = Integer.parseInt(getString);
        switch (v.getId())
        {
            case R.id.btn1:
                if (curent < nEnd)
                {
                    curent++;
                    text1.setText(String.valueOf(curent));
                }
                else if (curent == nEnd)
                {
                    text1.setText(nStart + "");
                    curent++;
                }
                break;
            case R.id.btn2:
                if (curent > nStart)
                {
                    curent--;
                    text1.setText(String.valueOf(curent));
                }
                else if (curent == nStart)
                {
                    text1.setText(nEnd + "");
                    curent--;
                }
                break;
        }
        aa = Integer.parseInt(text1.getText().toString());
    }

    @OnClick({R.id.btn3, R.id.btn4})
    void onClick2(View v)
    {
        String getString = String.valueOf(text2.getText());
        int curent = Integer.parseInt(getString);
        switch (v.getId())
        {
            case R.id.btn3:
                if (curent < nEnd)
                {
                    curent++;
                    text2.setText(String.valueOf(curent));
                }
                else if (curent == nEnd)
                {
                    text2.setText(nStart + "");
                    curent++;
                }
                break;
            case R.id.btn4:
                if (curent > nStart)
                {
                    curent--;
                    text2.setText(String.valueOf(curent));
                }
                else if (curent == nStart)
                {
                    text2.setText(nEnd + "");
                    curent--;
                }
                break;
        }
        bb = Integer.parseInt(text2.getText().toString());
    }

    // 클릭버튼이 하나일때 생성자 함수로 클릭이벤트를 받는다.
    public Time_Activity(Context context, View.OnClickListener okListener)
    {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mOkClickListener = okListener;
    }
}

