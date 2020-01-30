package com.ghsoft.android.lighthouse;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IDActivity extends AppCompatActivity
{
    @BindView(R.id.birth)
    EditText birth;
    @BindView(R.id.number)
    EditText number;
    @BindView(R.id.id_find)
    Button id_find;
    @BindView(R.id.id_find_ok)
    Button id_find_ok;
    @BindView(R.id.result)
    LinearLayout result;
    @BindView(R.id.id_text)
    TextView id_text;
    @BindView(R.id.top)
    LinearLayout top;
    @BindView(R.id.bottom)
    LinearLayout bottom;
    @BindView(R.id.tttt)
    TextView ttt;

    String over_a = "pho";

    Handler handler = new Handler();

    String birth_str, number_str;
    String btn_a = "", btn_b = "";
    private Dialog_Activity mCustomDialog;
    AQuery aQuery;
    String re_num, id_str;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id);
        ButterKnife.bind(this);
        aQuery = new AQuery(this);

        setContent();
    }

    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    public void setContent()
    {
        birth.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                if (checkPass(charSequence.toString()))
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
    }

    @OnClick(R.id.id_find)
    public void OnIdClick()
    {
        birth_str = birth.getText().toString().trim();
        number_str = number.getText().toString().trim();
        if (over_a.equals("pho"))
        {
            if (!birth_str.equals("") && !number_str.equals(""))
            {
                if (checkPass(birth_str))
                {
                    if (checkPhone(number_str))
                    {

                    }
                    else
                    {
                        Toast.makeText(this, "휴대폰번호가 형식에 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(this, "생년월일이 형식에 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(this, "정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            if (!birth_str.equals("") && !number_str.equals(""))
            {
                if (checkPass(birth_str))
                {
                    if (checkEmail(number_str))
                    {

                    }
                    else
                    {
                        Toast.makeText(this, "이메일이 형식에 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(this, "생년월일이 형식에 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(this, "정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @OnClick(R.id.id_find_ok)
    public void OnIdFindClick()
    {
        final int num = new Random().nextInt(9000) + 1000;
        birth_str = birth.getText().toString().trim();
        number_str = number.getText().toString().trim();

        if (over_a.equals("pho"))
        {
            String url = LHGlobal.BaseURL + "/member/findId";
            final Map<String, Object> params = new HashMap<>();
            params.put("member_phone", number_str);
            params.put("member_birth", birth_str);
            aQuery.ajax(url, params, String.class, new AjaxCallback<String>()
            {
                @Override
                public void callback(String url, String object, AjaxStatus status)
                {
                    Log.d("아이디찾기_은진씨", object);
                    Log.d("아이디찾기_예지", params.toString());

                    try
                    {
                        JSONObject jsonObject = new JSONObject(object);
                        String result = jsonObject.getString("return");
                        if (result.equals("true"))
                        {
                            id_str = jsonObject.getString("id");

                            String url_num = LHGlobal.BaseURL + "/member/smsCk";
                            final Map<String, Object> params_num = new HashMap<>();
                            params_num.put("number", num);
                            params_num.put("phone", number_str);
                            aQuery.ajax(url_num, params_num, String.class, new AjaxCallback<String>()
                            {
                                @Override
                                public void callback(String url, String object, AjaxStatus status)
                                {
                                    Log.d("핸드폰_은진씨", object);
                                    Log.d("핸드폰_예지", params_num.toString());

                                    try
                                    {
                                        JSONObject jsonObject = new JSONObject(object);
                                        String result = jsonObject.getString("return");
                                        re_num = jsonObject.getString("phone");
                                        if (result.equals("success"))
                                        {
                                            Intent intent = new Intent(IDActivity.this, AuthActivity.class);
                                            intent.putExtra("auth", "id");
                                            startActivityForResult(intent, 1);
                                            Toast.makeText(IDActivity.this, "인증번호를 전송하였습니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    catch (JSONException e)
                                    {

                                    }
                                }
                            }.header("User-Agent", "gh_mobile{}"));

                        }
                        else
                        {
                            mCustomDialog = new Dialog_Activity(IDActivity.this, "정보가 올바르지 않습니다.", okListener);
                            mCustomDialog.show();
                        }

                    } catch (JSONException e)
                    {

                    }
                }

            }.header("User-Agent", "gh_mobile{}"));
        }
        else
        {
            String url = LHGlobal.BaseURL + "/member/findIdEmail";
            final Map<String, Object> params = new HashMap<>();
            params.put("member_email", number_str);
            params.put("member_birth", birth_str);
            aQuery.ajax(url, params, String.class, new AjaxCallback<String>()
            {
                @Override
                public void callback(String url, String object, AjaxStatus status)
                {
                    Log.d("아이디찾기_은진씨", object);
                    Log.d("아이디찾기_예지", params.toString());

                    try
                    {
                        JSONObject jsonObject = new JSONObject(object);
                        String result = jsonObject.getString("return");
                        if (result.equals("true"))
                        {
                            id_str = jsonObject.getString("id");

                            String url_email = LHGlobal.BaseURL + "/member/email_ck";
                            final Map<String, Object> params_emial = new HashMap<>();
                            params_emial.put("val", num);
                            params_emial.put("email", number_str);
                            aQuery.ajax(url_email, params_emial, String.class, new AjaxCallback<String>()
                            {
                                @Override
                                public void callback(String url, String object, AjaxStatus status)
                                {
                                    Log.d("이메일_은진씨", object);
                                    Log.d("이메일_예지", params_emial.toString());

                                    try
                                    {
                                        JSONObject jsonObject = new JSONObject(object);
                                        String result = jsonObject.getString("result");
                                        Toast.makeText(IDActivity.this, result, Toast.LENGTH_LONG);
                                        if (result.equals("true"))
                                        {
                                            re_num = jsonObject.getString("val");
                                            Intent intent = new Intent(IDActivity.this, AuthActivity.class);
                                            intent.putExtra("auth", "id");
                                            startActivityForResult(intent, 1);
                                            Toast.makeText(IDActivity.this, "인증번호를 전송하였습니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    catch (JSONException e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                            }.header("User-Agent", "gh_mobile{}"));
                        }
                        else
                        {
                            mCustomDialog = new Dialog_Activity(IDActivity.this, "정보가 올바르지 않습니다.", okListener);
                            mCustomDialog.show();
                        }
                    }
                    catch (JSONException e)
                    {

                    }
                }

            }.header("User-Agent", "gh_mobile{}"));
        }
    }

    private View.OnClickListener okListener = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            mCustomDialog.dismiss();
        }
    };

    @OnClick({R.id.bot_btn1, R.id.bot_btn2})
    void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.bot_btn1:
                top.setVisibility(View.GONE);
                bottom.setVisibility(View.VISIBLE);
                over_a = "pho";
                handler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        over_a = "pho";
                        ttt.setText("휴대폰번호");
                        number.setHint("전화 번호를 입력해주세요( - 없이)");
                        number.setInputType(InputType.TYPE_CLASS_NUMBER);
                        number.addTextChangedListener(new TextWatcher()
                        {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
                            {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
                            {
                                if (checkPhone(charSequence.toString()))
                                {
                                    btn_b = "ok";
                                }
                                else
                                {
                                    btn_b = "no";
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
                    }
                });
                break;

            case R.id.bot_btn2:
                top.setVisibility(View.GONE);
                bottom.setVisibility(View.VISIBLE);
                over_a = "em";
                handler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        over_a = "em";
                        ttt.setText("이메일");
                        number.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                        number.setHint("이메일을 입력해주세요");
                        number.addTextChangedListener(new TextWatcher()
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
                                    btn_b = "ok";
                                }
                                else
                                {
                                    btn_b = "no";
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
                    }
                });
                break;
        }
    }

    @OnClick(R.id.back)
    public void OnBackclick()
    {
        finish();
    }

    //생년월일 정규식
    public static boolean checkPass(String pass)
    {
        String passPt = "(19|20)[0-9]{2}(01|02|03|04|05|06|07|08|09|10|11|12)[0-9]{2}";
        Pattern p = Pattern.compile(passPt);
        Matcher m = p.matcher(pass);
        return m.matches();
    }

    //핸드폰 번호 형식 체크(-없이)
    public static boolean checkPhone(String phone)
    {
        String phonePt = "(01[016789])(\\d{4})(\\d{4})+$";
        Pattern p = Pattern.compile(phonePt);
        Matcher m = p.matcher(phone);
        return m.matches();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1)
        {
            if (resultCode == RESULT_OK)
            {
                if (re_num.equals(data.getExtras().getString("num")))
                {
                    id_text.setText(id_str);
                    result.setVisibility(View.VISIBLE);
                }
                else
                {
                    Toast.makeText(IDActivity.this, "인증번로가 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
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
