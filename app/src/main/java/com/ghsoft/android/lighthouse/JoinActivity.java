package com.ghsoft.android.lighthouse;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.firebase.iid.FirebaseInstanceId;
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

public class JoinActivity extends AppCompatActivity
{
    @BindView(R.id.id_over)
    Button id_over;
    @BindView(R.id.id_over_ok)
    Button id_over_ok;
    @BindView(R.id.id_edit)
    EditText id_edit;
    @BindView(R.id.nick_over)
    Button nick_over;
    @BindView(R.id.nick_over_ok)
    Button nick_over_ok;
    @BindView(R.id.nick_edit)
    EditText nick_edit;
    @BindView(R.id.pass)
    EditText pass;
    @BindView(R.id.pass_ok)

    EditText pass_ok;
    @BindView(R.id.pass_text)
    TextView pass_text;
    @BindView(R.id.gender_m)
    ToggleButton gender_m;
    @BindView(R.id.gender_f)
    ToggleButton gender_f;
    @BindView(R.id.check)
    ToggleButton check;
    @BindView(R.id.check2)
    ToggleButton check2;
    @BindView(R.id.birth)
    EditText birth;

    @BindView(R.id.bot_btn1)
    ToggleButton bot_btn1;
    @BindView(R.id.bot_btn2)
    ToggleButton bot_btn2;
    @BindView(R.id.bot_btn3)
    ToggleButton bot_btn3;
    @BindView(R.id.bot_btn4)
    ToggleButton bot_btn4;

    @BindView(R.id.top)
    ScrollView top;
    @BindView(R.id.bottom)
    ScrollView bottom;
    @BindView(R.id.okokok)
    Button okokok;


    private Dialog_Activity mCustomDialog;

    AQuery aQuery;
    String id_str = "", nick_str = "", pass_str = "", pass_ok_str = "", birth_str = "", gender_str = "", number_str = "", email_str = "";
    String re_id, re_nick;
    Handler handler = new Handler();
    String btn_a = "", token = "";

    String over_a = "no", over_b = "no";

    String sel, sel_over;

    int a = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        ButterKnife.bind(this);
        aQuery = new AQuery(this);
        token = FirebaseInstanceId.getInstance().getToken();
        a = 0;
        Intent intent = getIntent();
        sel = intent.getExtras().getString("select");
        sel_over = intent.getExtras().getString("over");

        Log.d("인증1", sel);
        Log.d("인증2", sel_over);


        InputFilter[] filters = new InputFilter[]{new InputFilter.LengthFilter(15), filter};
        id_edit.setFilters(filters);


        setContent();
        pass.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        pass_ok.setTransformationMethod(new AsteriskPasswordTransformationMethod());

    }

    @Override
    protected void attachBaseContext(Context newBase)
    {

        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));

    }

    public void setContent()
    {
        id_edit.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

                if (!charSequence.equals(re_id))
                {
                    over_a = "no";
                    id_over.setVisibility(View.VISIBLE);
                    id_over_ok.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });

        nick_edit.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                if (!charSequence.equals(re_nick))
                {
                    over_b = "no";
                    nick_over.setVisibility(View.VISIBLE);
                    nick_over_ok.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });


        pass.addTextChangedListener(new TextWatcher()
        {
            String strCur;

            public void onTextChanged(final CharSequence s, int start, int before, int count)
            {
                pass_str = pass.getText().toString().trim();
                pass_ok_str = pass_ok.getText().toString().trim();

                if (pass_ok_str.equals(s.toString()))
                {
                    btn_a = "ok";
                }
                else
                {
                    btn_a = "no";

                }

                if (btn_a.equals("ok") && !pass_str.equals(""))
                {
                    handler.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            pass_text.setText("비밀번호가 일치합니다.");
                        }
                    });
                }
                else if (pass_str.equals("") && pass_ok_str.equals(""))
                {
                    handler.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            pass_text.setText("");
                        }
                    });

                }
                else
                {
                    handler.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            pass_text.setText("비밀번호가 일치하지않습니다.");
                        }
                    });
                }

            }


            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                strCur = s.toString();

            }

            public void afterTextChanged(Editable s)
            {

            }
        });

        pass_ok.addTextChangedListener(new TextWatcher()
        {
            String strCur;

            public void onTextChanged(final CharSequence s, int start, int before, int count)
            {
                pass_str = pass.getText().toString().trim();
                pass_ok_str = pass_ok.getText().toString().trim();

                if (pass_str.equals(s.toString()))
                {
                    btn_a = "ok";
                }
                else
                {
                    btn_a = "no";

                }

                if (btn_a.equals("ok") && !pass_ok_str.equals(""))
                {
                    handler.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            pass_text.setText("비밀번호가 일치합니다.");
                        }
                    });
                }
                else if (pass_str.equals("") && pass_ok_str.equals(""))
                {
                    handler.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            pass_text.setText("");
                        }
                    });
                }
                else
                {
                    handler.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            pass_text.setText("비밀번호가 일치하지않습니다.");
                        }
                    });
                }


            }


            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                strCur = s.toString();

            }

            public void afterTextChanged(Editable s)
            {

            }
        });


    }

    @OnClick(R.id.id_over)
    public void OnIdOver()
    {
        id_str = id_edit.getText().toString().trim();
        String url = LHGlobal.BaseURL + "/member/selectId";
        final Map<String, Object> params = new HashMap<>();
        params.put("member_id", id_str);
        aQuery.ajax(url, params, String.class, new AjaxCallback<String>()
        {
            @Override
            public void callback(String url, String object, AjaxStatus status)
            {
                Log.d("아이디중복검사_은진씨", object);
                Log.d("아이디중복검사_예지", params.toString());

                try
                {
                    JSONObject jsonObject = new JSONObject(object);
                    String result = jsonObject.getString("return");
                    if (!id_str.equals(""))
                    {
                        if (id_str.length() >= 4 && id_str.length() < 16)
                        {
                            if (result.equals("true"))
                            {
                                re_id = jsonObject.getString("id");

                                id_over.setVisibility(View.GONE);
                                id_over_ok.setVisibility(View.VISIBLE);
                                over_a = "ok";

                                Toast.makeText(JoinActivity.this, "사용가능한 ID입니다.", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(JoinActivity.this, "ID 중복입니다.", Toast.LENGTH_SHORT).show();
                                over_a = "no";

                            }
                        }
                        else
                        {
                            Toast.makeText(JoinActivity.this, "ID는 4자 이상 15자 이하 입니다.", Toast.LENGTH_SHORT).show();
                            over_a = "no";

                        }
                    }
                    else
                    {
                        Toast.makeText(JoinActivity.this, "정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        over_a = "no";

                    }
                } catch (JSONException e)
                {

                }
            }
        }.header("User-Agent", "gh_mobile{}"));

    }

    @OnClick(R.id.nick_over)
    public void OnNickOver()
    {
        nick_str = nick_edit.getText().toString().trim();
        String url = LHGlobal.BaseURL + "/member/selectNick";
        final Map<String, Object> params = new HashMap<>();
        params.put("member_name", nick_str);
        aQuery.ajax(url, params, String.class, new AjaxCallback<String>()
        {
            @Override
            public void callback(String url, String object, AjaxStatus status)
            {
                Log.d("닉네임중복검사_은진씨", object);
                Log.d("닉네임중복검사_예지", params.toString());

                try
                {
                    JSONObject jsonObject = new JSONObject(object);
                    String result = jsonObject.getString("return");
                    if (!nick_str.equals(""))
                    {
                        if (nick_str.length() >= 2 && nick_str.length() < 11)
                        {
                            if (result.equals("true"))
                            {
                                re_nick = jsonObject.getString("id");

                                nick_over.setVisibility(View.GONE);
                                nick_over_ok.setVisibility(View.VISIBLE);
                                over_b = "ok";
                                Toast.makeText(JoinActivity.this, "사용가능한 닉네임입니다.", Toast.LENGTH_SHORT).show();

                            }
                            else
                            {
                                Toast.makeText(JoinActivity.this, "닉네임 중복입니다.", Toast.LENGTH_SHORT).show();
                                over_b = "no";

                            }
                        }
                        else
                        {
                            Toast.makeText(JoinActivity.this, "닉네임은 2자 이상 10자 이하 입니다.", Toast.LENGTH_SHORT).show();
                            over_b = "no";

                        }
                    }
                    else
                    {
                        Toast.makeText(JoinActivity.this, "정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        over_b = "no";

                    }
                } catch (JSONException e)
                {

                }
            }
        }.header("User-Agent", "gh_mobile{}"));

    }

    @OnClick(R.id.gender_m)
    public void OnGenderM()
    {
        if (gender_m.isChecked())
        {
            gender_m.setBackgroundResource(R.drawable.man);
            gender_f.setBackgroundResource(R.drawable.n);
            gender_f.setChecked(false);
            gender_str = "남자";
        }
        else
        {
            gender_m.setBackgroundResource(R.drawable.n);
        }
    }

    @OnClick(R.id.gender_f)
    public void OnGenderF()
    {
        if (gender_f.isChecked())
        {
            gender_f.setBackgroundResource(R.drawable.woman);
            gender_m.setBackgroundResource(R.drawable.n);
            gender_m.setChecked(false);
            gender_str = "여자";
        }
        else
        {
            gender_f.setBackgroundResource(R.drawable.n);
        }
    }

    @OnClick(R.id.check)
    public void OnCheck()
    {
        if (check.isChecked())
        {
            check.setBackgroundResource(R.drawable.m_p);
            check.setChecked(true);

        }
        else
        {
            check.setBackgroundResource(R.drawable.m);
            check.setChecked(false);
        }
    }

    @OnClick(R.id.check2)
    public void OnCheck2()
    {
        if (check2.isChecked())
        {
            check2.setBackgroundResource(R.drawable.m_p);
            check2.setChecked(true);

        }
        else
        {
            check2.setBackgroundResource(R.drawable.m);
            check2.setChecked(false);
        }
    }

    @OnClick(R.id.okokok)
    public void OnOKOK()
    {
        top.setVisibility(View.VISIBLE);
        bottom.setVisibility(View.GONE);
        a = 0;
    }

    @OnClick(R.id.swear)
    public void OnSwear()
    {
        top.setVisibility(View.GONE);
        bottom.setVisibility(View.VISIBLE);
        a = 1;
    }

    @OnClick({R.id.bot_btn1, R.id.bot_btn2, R.id.bot_btn3, R.id.bot_btn4})
    void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.bot_btn1:
                if (bot_btn1.isChecked())
                {
                    bot_btn1.setBackgroundResource(R.drawable.m_p);
                    bot_btn1.setChecked(true);

                }
                else
                {
                    bot_btn1.setBackgroundResource(R.drawable.m);
                    bot_btn1.setChecked(false);
                }
                break;

            case R.id.bot_btn2:
                if (bot_btn2.isChecked())
                {
                    bot_btn2.setBackgroundResource(R.drawable.m_p);
                    bot_btn2.setChecked(true);

                }
                else
                {
                    bot_btn2.setBackgroundResource(R.drawable.m);
                    bot_btn2.setChecked(false);
                }
                break;

            case R.id.bot_btn3:
                if (bot_btn3.isChecked())
                {
                    bot_btn3.setBackgroundResource(R.drawable.m_p);
                    bot_btn3.setChecked(true);

                }
                else
                {
                    bot_btn3.setBackgroundResource(R.drawable.m);
                    bot_btn3.setChecked(false);
                }
                break;

            case R.id.bot_btn4:
                if (bot_btn4.isChecked())
                {
                    bot_btn4.setBackgroundResource(R.drawable.m_p);
                    bot_btn4.setChecked(true);

                }
                else
                {
                    bot_btn4.setBackgroundResource(R.drawable.m);
                    bot_btn4.setChecked(false);
                }
                break;
        }
    }


    protected InputFilter filter = new InputFilter()
    {
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend)
        {
            Pattern ps = Pattern.compile("^[a-zA-Z0-9]+$");

            if (!ps.matcher(source).matches())
            {
                return "";
            }
            return null;
        }
    };

    //생년월일 정규식
    public static boolean checkPass(String pass)
    {
        String passPt = "(19|20)[0-9]{2}(01|02|03|04|05|06|07|08|09|10|11|12)[0-9]{2}";
        Pattern p = Pattern.compile(passPt);
        Matcher m = p.matcher(pass);
        return m.matches();
    }


    @OnClick(R.id.back)
    public void OnBackClick()
    {
        finish();
    }

    @Override
    public void onBackPressed()
    {
        if (a == 1)
        {
            top.setVisibility(View.VISIBLE);
            bottom.setVisibility(View.GONE);
            a = 0;
        }
        else if (a == 0)
        {
            finish();
        }
    }

    public class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod
    {
        @Override
        public CharSequence getTransformation(CharSequence source, View view)
        {
            return new AsteriskPasswordTransformationMethod.PasswordCharSequence(source);
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

    @OnClick(R.id.next_btn)
    public void OnNext()
    {
        id_str = id_edit.getText().toString().trim();
        nick_str = nick_edit.getText().toString().trim();
        pass_str = pass.getText().toString().trim();
        pass_ok_str = pass_ok.getText().toString().trim();
        birth_str = birth.getText().toString().trim();
        if (!id_str.equals("") && !nick_str.equals("") && !pass_str.equals("") && !pass_ok_str.equals("") && gender_m.isChecked() || gender_f.isChecked() && !birth_str.equals(""))
        {
            if (check.isChecked() && check2.isChecked())
            {
                if (bot_btn1.isChecked() && bot_btn2.isChecked() && bot_btn3.isChecked() && bot_btn4.isChecked())
                {
                    if (!over_a.equals("no") && !over_b.equals("no"))
                    {
                        if (checkPass(birth_str))
                        {
                            if (pass_text.getText().toString().contains("일치합니다"))
                            {
                                if (pass.getText().toString().length() >= 4)
                                {

                                    String url = LHGlobal.BaseURL + "/member/insert";
                                    final Map<String, Object> params = new HashMap<>();
                                    params.put("member_id", id_str);
                                    params.put("member_name", nick_str);
                                    params.put("member_pass", pass_str);
                                    params.put("member_gender", gender_str);
                                    params.put("member_birth", birth_str);
                                    if (sel_over.equals("pho"))
                                    {
                                        params.put("member_email", "");
                                        params.put("member_phone", sel);
                                    }
                                    else
                                    {
                                        params.put("member_email", sel);
                                        params.put("member_phone", "");
                                    }
                                    params.put("member_device", "0");
                                    aQuery.ajax(url, params, String.class, new AjaxCallback<String>()
                                    {
                                        @Override
                                        public void callback(String url, String object, AjaxStatus status)
                                        {
                                            Log.d("회원가입_은진씨", params.toString());
                                            Log.d("회원가입_예지", object);
                                            try
                                            {
                                                JSONObject jsonObject = new JSONObject(object);
                                                String result = jsonObject.getString("return");

                                                if (result.equals("true"))
                                                {
                                                    finish();
                                                    Toast.makeText(JoinActivity.this, "회원가입이 되었습니다.", Toast.LENGTH_SHORT).show();
                                                }
                                                else
                                                {
//                                                    Toast.makeText(JoinActivity.this, "이미 회원가입한 번호 또는 이메일 입니다.", Toast.LENGTH_SHORT).show();

                                                }

                                            } catch (JSONException e)
                                            {

                                            }
                                        }
                                    }.header("User-Agent", "gh_mobile{" + token + "}"));
                                }
                                else
                                {
                                    Toast.makeText(JoinActivity.this, "비밀번호는 4자 이상입니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(JoinActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(JoinActivity.this, "생년월일이 형식에 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(JoinActivity.this, "중복검사를 해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(JoinActivity.this, "서약 동의를 해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(JoinActivity.this, "약관동의를 해주세요.", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(JoinActivity.this, "정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
        }
    }
}
