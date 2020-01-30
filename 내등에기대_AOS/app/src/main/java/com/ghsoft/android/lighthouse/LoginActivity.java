package com.ghsoft.android.lighthouse;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends Activity
{

    AQuery aQuery;
    @BindView(R.id.id)
    EditText id;
    @BindView(R.id.pass)
    EditText pass;
    String id_str, pass_str, token = "";

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ProgressDialog pdLoading;

    Handler handler = new Handler();

    boolean running;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setBackgroundDrawableResource(R.drawable.login_back);
        ButterKnife.bind(this);
        aQuery = new AQuery(this);
        running = false;
        token = FirebaseInstanceId.getInstance().getToken();
        pdLoading = new ProgressDialog(LoginActivity.this);

        pref = getSharedPreferences("member", MODE_PRIVATE);
        editor = pref.edit();

        if (pref.getString("autologin", "0").equals("1"))
        {
            if (!pref.getString("idx", "").equals(""))
            {
                id.setText(pref.getString("id", ""));
                pass.setText(pref.getString("pass", ""));
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("pushlink", "");
                intent.putExtra("pushidx", "");
                startActivity(intent);
                finish();
            }

        }
        else
        {
            try
            {
                if (token.equals(""))
                {
                    handler.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            pdLoading.setMessage("\tLoading...");
                            pdLoading.setCancelable(false);
                            pdLoading.show();
                        }
                    });
                    thread.start();
                }
                Log.d("토로롤로로로로", token);
            } catch (Exception e)
            {
                e.printStackTrace();
//                token = "";
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            pdLoading.setMessage("\tLoading...");
//                            pdLoading.setCancelable(false);
//                            pdLoading.show();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//                thread.start();
            }

        }
        pass.setTransformationMethod(new AsteriskPasswordTransformationMethod());
    }

    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @OnClick({R.id.login, R.id.join, R.id.id_find, R.id.pw_find})
    void onClick(View v)
    {
        switch (v.getId())
        {

            case R.id.login:
                id_str = id.getText().toString().trim();
                pass_str = pass.getText().toString().trim();

                if (!id_str.equals("") && !pass_str.equals(""))
                {
                    String url_num = LHGlobal.BaseURL + "/member/login";
                    final Map<String, Object> params_num = new HashMap<>();
                    params_num.put("member_id", id_str);
                    params_num.put("member_pass", pass_str);
                    aQuery.ajax(url_num, params_num, String.class, new AjaxCallback<String>()
                    {
                        @Override
                        public void callback(String url, String object, AjaxStatus status)
                        {
                            Log.d("로그인_예지", params_num.toString());
                            Log.d("로그인_은진씨", url);

                            try
                            {
                                JSONObject jsonObject = new JSONObject(object);
                                Log.d("로그인 값", object);
//                        Log.d("토큰_로그인", token);
                                String result = jsonObject.getString("return");
                                if (result.equals("true"))
                                {
                                    Log.d("로그인토큰", token);
                                    String idx = jsonObject.getString("member_idx");
                                    editor.putString("id", id_str);
                                    editor.putString("pass", pass_str);
                                    editor.putString("idx", idx);
                                    editor.putString("autologin", "1");
                                    editor.commit();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("pushlink", "");
                                    intent.putExtra("pushidx", "");
                                    startActivity(intent);
                                    finish();
                                }
                                else
                                {
                                    Toast.makeText(LoginActivity.this, "비밀번호 또는 아이디를 확인해주세요.", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e)
                            {

                            }
                        }

                    }.header("User-Agent", "gh_mobile{" + token + "}"));

                }
                else
                {
                    Toast.makeText(this, "정보를 입력해주세요.", Toast.LENGTH_SHORT).show();

                }
                break;

            case R.id.join:
                Intent intent = new Intent(LoginActivity.this, SelectActivity.class);
                startActivity(intent);
                break;

            case R.id.id_find:
                Intent intent1 = new Intent(LoginActivity.this, IDActivity.class);
                startActivity(intent1);
                break;

            case R.id.pw_find:
                Intent intent2 = new Intent(LoginActivity.this, PWActivity.class);
                startActivity(intent2);
                break;

        }
    }

    Thread thread = new Thread(new Runnable()
    {
        @Override
        public void run()
        {
            while (token.equals(""))
            {
                token = FirebaseInstanceId.getInstance().getToken();
                SystemClock.sleep(1500);
                if (!token.equals(""))
                {
                    break;
                }
            }
            handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    Log.d("토로롤로로로로", token);
                    pdLoading.dismiss();
                }
            });
        }
    });

    public class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod
    {
        @Override
        public CharSequence getTransformation(CharSequence source, View view)
        {
            return new PasswordCharSequence(source);
        }

        private class PasswordCharSequence implements CharSequence
        {
            private CharSequence mSource;

            public PasswordCharSequence(CharSequence source)
            {
                mSource = source; // Store char sequence
            }

            public char charAt(int index)
            {
                return '*'; // This is the important part
            }

            public int length()
            {
                return mSource.length(); // Return default
            }

            public CharSequence subSequence(int start, int end)
            {
                return mSource.subSequence(start, end); // Return default
            }
        }
    }
}