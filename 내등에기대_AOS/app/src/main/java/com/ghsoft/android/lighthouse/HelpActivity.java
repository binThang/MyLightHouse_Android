package com.ghsoft.android.lighthouse;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bumptech.glide.Glide;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HelpActivity extends AppCompatActivity
{
    AQuery aQuery;

    @BindView(R.id.img)
    ImageView img;
    ProgressDialog pdLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        ButterKnife.bind(this);
        aQuery = new AQuery(this);
        pdLoading = new ProgressDialog(this);
        pdLoading.setMessage("\tLoading...");
        pdLoading.setCancelable(false);
        pdLoading.show();

        String url = LHGlobal.BaseURL + "/customer/select_heppeople";
        aQuery.ajax(url, String.class, new AjaxCallback<String>()
        {
            @Override
            public void callback(String url, String object, AjaxStatus status)
            {
                Log.d("도움_은진씨", object);
                try
                {
                    JSONObject jsonObject = new JSONObject(object);
                    String result = jsonObject.getString("img");
                    Glide.with(HelpActivity.this).load(LHGlobal.BaseURL + "/public/uploads/mobile/image/" + result)
                            .placeholder(R.drawable.border_join)
                            .error(R.drawable.border_join)
                            .into(img);
                    pdLoading.dismiss();
                }
                catch (JSONException e)
                {
                    pdLoading.dismiss();
                }
            }
        }.header("User-Agent", "gh_mobile{}"));
    }

    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @OnClick({R.id.back})
    void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.back:
                finish();
                break;
        }
    }
}
