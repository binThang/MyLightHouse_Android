package com.ghsoft.android.lighthouse;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProgramActivity extends AppCompatActivity
{

    @BindView(R.id.li_1)
    LinearLayout li_1;
    @BindView(R.id.btn1)
    ToggleButton btn1;
    @BindView(R.id.li_2)
    LinearLayout li_2;
    @BindView(R.id.btn2)
    ToggleButton btn2;
    @BindView(R.id.ver)
    TextView ver;

    @BindView(R.id.te_1)
    TextView te_1;
    @BindView(R.id.te_2)
    TextView te_2;
    Handler handler = new Handler();
    ProgressDialog pdLoading;

    @BindView(R.id.topview1)
    LinearLayout topview1;
    @BindView(R.id.topview2)
    LinearLayout topview2;

    AQuery aQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program);
        ButterKnife.bind(this);
        aQuery = new AQuery(this);
        pdLoading = new ProgressDialog(this);
        pdLoading.setMessage("\tLoading...");
        pdLoading.setCancelable(false);
        pdLoading.show();

        MarketThread.start();

        String url = LHGlobal.BaseURL + "/agree/agree1";
        aQuery.ajax(url, String.class, new AjaxCallback<String>()
        {
            @Override
            public void callback(String url, String object, AjaxStatus status)
            {
                Log.d("마지막_은진씨", object);

                try
                {
                    JSONObject jsonObject = new JSONObject(object);
                    String str1 = jsonObject.getString("agree");
                    te_1.setText(str1);

                } catch (JSONException e)
                {

                }
            }
        }.header("User-Agent", "gh_mobile{}"));

        String url2 = LHGlobal.BaseURL + "/agree/agree2";
        aQuery.ajax(url2, String.class, new AjaxCallback<String>()
        {
            @Override
            public void callback(String url, String object, AjaxStatus status)
            {
                Log.d("마지막_은진씨", object);

                try
                {
                    JSONObject jsonObject = new JSONObject(object);
                    String str1 = jsonObject.getString("agree");
                    te_2.setText(str1);

                } catch (JSONException e)
                {

                }
            }
        }.header("User-Agent", "gh_mobile{}"));


        topview1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                btn1.performClick();
            }
        });

        topview2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                btn2.performClick();
            }
        });


    }

    @Override
    protected void attachBaseContext(Context newBase)
    {

        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));

    }

    @OnClick({R.id.btn1, R.id.btn2, R.id.back})
    void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn1:
                if (btn1.isChecked())
                {
                    btn1.setBackgroundResource(R.drawable.up);
                    li_1.setVisibility(View.VISIBLE);
                }
                else
                {
                    btn1.setBackgroundResource(R.drawable.down);
                    li_1.setVisibility(View.GONE);

                }
                break;

            case R.id.btn2:
                if (btn2.isChecked())
                {
                    btn2.setBackgroundResource(R.drawable.up);
                    li_2.setVisibility(View.VISIBLE);
                }
                else
                {
                    btn2.setBackgroundResource(R.drawable.down);
                    li_2.setVisibility(View.GONE);

                }
                break;

            case R.id.back:
                finish();
                break;
        }
    }

    Thread MarketThread = new Thread(new Runnable()
    {
        @Override
        public void run()
        {
            String store_version = MarketVersionChecker.getMarketVersion(getPackageName());
            String device_version = null;

            try
            {
                device_version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;

                final double i = Double.parseDouble(store_version) - Double.parseDouble(device_version);
                final String finalDevice_version = device_version;
                handler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if (i >= 0)
                        {
                            ver.setText(finalDevice_version + " Ver");
                        }
                        pdLoading.dismiss();
                    }
                });
            } catch (PackageManager.NameNotFoundException e)
            {
                Log.e("버전", "오류1");
                Log.e("버전", e + "");
                e.printStackTrace();
                pdLoading.dismiss();

            } catch (Exception e)
            {
                Log.e("버전", "오류2");
                Log.e("버전", e + "");
                pdLoading.dismiss();
            }
        }
    });
}
