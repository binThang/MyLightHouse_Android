package com.ghsoft.android.lighthouse;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.firebase.iid.FirebaseInstanceId;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        ButterKnife.bind(this);
    }

    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @OnClick({R.id.push, R.id.prog, R.id.req, R.id.notice, R.id.back, R.id.help, R.id.logout, R.id.withdrawal})
    void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.push:
                Intent intent = new Intent(SetActivity.this, PushActivity.class);
                startActivity(intent);
                break;

            case R.id.prog:
                Intent intent1 = new Intent(SetActivity.this, ProgramActivity.class);
                startActivity(intent1);
                break;

            case R.id.req:
                Intent intent2 = new Intent(SetActivity.this, PostboxActivity.class);
                startActivity(intent2);
                break;

            case R.id.notice:
                Intent intent3 = new Intent(SetActivity.this, NoticeActivity.class);
                startActivity(intent3);
                break;

            case R.id.help:
                Intent intent4 = new Intent(SetActivity.this, HelpActivity.class);
                startActivity(intent4);
                break;

            case R.id.back:
                finish();
                break;

            case R.id.logout:
            {
                final String token = FirebaseInstanceId.getInstance().getToken();
                final AQuery aQuery = new AQuery(this);
                final SharedPreferences pref = getSharedPreferences("member", MODE_PRIVATE);
                final SharedPreferences.Editor editor = pref.edit();

                AlertDialog.Builder alert_logot = new AlertDialog.Builder(this);
                alert_logot.setPositiveButton("확인", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(final DialogInterface dialog, int which)
                    {
                        String url = LHGlobal.BaseURL + "/member/logout";
                        aQuery.ajax(url, String.class, new AjaxCallback<String>()
                        {
                            @Override
                            public void callback(String url, String object, AjaxStatus status)
                            {
                                Log.d("로그아웃_은진씨", object);
                                Log.d("토큰_로그아웃", token);

                                try
                                {
                                    JSONObject jsonObject = new JSONObject(object);
                                    String result = jsonObject.getString("return");

                                    if (result.equals("true"))
                                    {
                                        editor.clear();
                                        editor.commit();
                                        Intent intent = new Intent(SetActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                        dialog.dismiss();
                                        Toast.makeText(SetActivity.this, "로그아웃되었습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e)
                                {
                                    dialog.dismiss();

                                }
                            }
                        }.header("User-Agent", "gh_mobile{" + token + "}"));

                    }
                });
                alert_logot.setNegativeButton("취소", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });
                alert_logot.setMessage("로그아웃하시겠습니까?");
                alert_logot.setCancelable(false);
                alert_logot.show();
                break;
            }

            case R.id.withdrawal:
                final String token = FirebaseInstanceId.getInstance().getToken();
                final AQuery aQuery = new AQuery(this);
                final SharedPreferences pref = getSharedPreferences("member", MODE_PRIVATE);
                final SharedPreferences.Editor editor = pref.edit();

                AlertDialog.Builder alert_withdrawal = new AlertDialog.Builder(this);
                alert_withdrawal.setPositiveButton("확인", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(final DialogInterface maindialog, int which)
                    {
                        maindialog.dismiss();
                        AlertDialog.Builder alert = new AlertDialog.Builder(SetActivity.this);
                        alert.setPositiveButton("탈퇴하기", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(final DialogInterface dialog, int which)
                            {
                                String url = LHGlobal.BaseURL + "/member/withdrawal";
                                final Map<String, Object> params = new HashMap<>();
                                params.put("member_idx", pref.getString("idx", ""));
                                aQuery.ajax(url, params, String.class, new AjaxCallback<String>()
                                {
                                    @Override
                                    public void callback(String url, String object, AjaxStatus status)
                                    {
                                        Log.d("정보_예지", params.toString());
                                        Log.d("정보_은진씨", object);
                                        try
                                        {
                                            JSONObject jsonObject = new JSONObject(object);
                                            String result = jsonObject.getString("return");

                                            if (result.equals("true"))
                                            {
                                                editor.clear();
                                                editor.commit();
                                                Intent intent = new Intent(SetActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                                finish();
                                                dialog.dismiss();
                                                Toast.makeText(SetActivity.this, "탈퇴되었습니다.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        catch (JSONException e)
                                        {
                                            maindialog.dismiss();
                                        }
                                    }
                                }.header("User-Agent", "gh_mobile{}"));
                            }
                        });
                        alert.setNegativeButton("좀 더 머무르기", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                            }
                        });
                        alert.setMessage("언제든 힘이 들면 잠시 쉬어가세요");
                        alert.setCancelable(false);
                        alert.show();
                    }
                });
                alert_withdrawal.setNegativeButton("취소", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });
                alert_withdrawal.setMessage("탈퇴하시겠습니까?");
                alert_withdrawal.setCancelable(false);
                alert_withdrawal.show();

                break;
        }
    }
}
