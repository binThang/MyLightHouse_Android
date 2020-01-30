package com.ghsoft.android.lighthouse;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tsengvn.typekit.TypekitContextWrapper;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by baeyongbin on 2018. 1. 25..
 */

public class PostboxActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postbox);
        ButterKnife.bind(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));

    }


    @OnClick({R.id.postbox_request, R.id.postbox_request_book, R.id.back})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.postbox_request:
                Intent intent = new Intent(PostboxActivity.this, RequestActivity.class);
                startActivity(intent);
                break;

            case R.id.postbox_request_book:
                Intent intent1 = new Intent(PostboxActivity.this, RequestBookActivity.class);
                startActivity(intent1);
                break;

            case R.id.back:
                finish();
                break;
        }

    }
}
