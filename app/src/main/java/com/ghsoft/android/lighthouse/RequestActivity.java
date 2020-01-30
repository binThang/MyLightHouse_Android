package com.ghsoft.android.lighthouse;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RequestActivity extends AppCompatActivity
{
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.con)
    EditText con;
    @BindView(R.id.id_find)
    Button id_find;
    @BindView(R.id.id_find_ok)
    Button id_find_ok;

    String email_str, con_str, btn_a = "no", btn_b = "no";

    AQuery aQuery;
    SharedPreferences pref;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        ButterKnife.bind(this);
        aQuery = new AQuery(this);
        pref = getSharedPreferences("member", MODE_PRIVATE);
        setContent();
    }

    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    public void setContent()
    {
        email.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                if (checkEmail(charSequence.toString()))
                {
                    btn_a = "ok";
                }
                else
                {
                    btn_a = "no";

                }
                if (btn_a.equals("ok") && btn_b.equals("ok"))
                {
                    id_find.setVisibility(View.GONE);
                    id_find_ok.setVisibility(View.VISIBLE);
                }
                else
                {
                    id_find.setVisibility(View.VISIBLE);
                    id_find_ok.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });

        con.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                if (charSequence.length() >= 1)
                    btn_b = "ok";
                else
                    btn_b = "no";

                if (btn_a.equals("ok") && btn_b.equals("ok"))
                {
                    id_find.setVisibility(View.GONE);
                    id_find_ok.setVisibility(View.VISIBLE);
                }
                else
                {
                    id_find.setVisibility(View.VISIBLE);
                    id_find_ok.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });
    }

    @OnClick({R.id.id_find, R.id.id_find_ok, R.id.back})
    void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.id_find:
                email_str = email.getText().toString().trim();
                con_str = con.getText().toString().trim();

                if (!email_str.equals("") && !con_str.equals(""))
                {
                    if (checkEmail(email_str))
                        ;
                    else
                        Toast.makeText(this, "이메일이 형식에 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(this, "정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
                break;

            case R.id.id_find_ok:
                email_str = email.getText().toString().trim();
                con_str = con.getText().toString().trim();
                String url = LHGlobal.BaseURL + "/customer/mail";
                final Map<String, Object> params = new HashMap<>();
                params.put("email", email_str);
                params.put("content", con_str);
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

                            String str = jsonObject.getString("return");

                            if (str.equals("success"))
                            {
                                finish();
                                Toast.makeText(RequestActivity.this, "접수되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (JSONException e)
                        {

                        }
//
                    }
                }.header("User-Agent", "gh_mobile{}"));
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    //이메일 형식 체크
    public static boolean checkEmail(String email)
    {
        String mail = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(mail);
        Matcher m = p.matcher(email);
        return m.matches();
    }
}
