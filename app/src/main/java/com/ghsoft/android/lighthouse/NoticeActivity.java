package com.ghsoft.android.lighthouse;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NoticeActivity extends AppCompatActivity {

    @BindView(R.id.recy)
    RecyclerView mRVFishPrice;
    private NoticeAdapter mAdapter;
    AQuery aQuery;
    List<NoticeData> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        ButterKnife.bind(this);
        aQuery = new AQuery(this);
        setConn();



    }

    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));

    }


    public void setConn() {
        String url = LHGlobal.BaseURL + "/customer/select_notice";
        aQuery.ajax(url, String.class, new AjaxCallback<String>() {
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                Log.d("공지사항_은진씨", object);
                try {
                    JSONArray jsonArray = new JSONArray(object);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String title = jsonObject.getString("title");
                        String content = jsonObject.getString("content");
                        String date = jsonObject.getString("wdate");

                        String[] date_s = date.split(" ");

                        NoticeData noticeData = new NoticeData();
                        noticeData.title = title;
                        noticeData.date = date_s[0];
                        noticeData.con = content;

                        data.add(noticeData);

                    }
                    mAdapter = new NoticeAdapter(NoticeActivity.this, data);
                    mRVFishPrice.setAdapter(mAdapter);
                    mRVFishPrice.setLayoutManager(new LinearLayoutManager(NoticeActivity.this));

                } catch (JSONException e) {

                }
            }
        }.header("User-Agent", "gh_mobile{}"));

    }

    @OnClick(R.id.back)
    public void OnBackclick() {
        finish();
    }
}
