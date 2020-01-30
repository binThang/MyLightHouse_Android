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

public class SelectActivity extends AppCompatActivity {
    @BindView(R.id.number_btn)
    Button number_btn;
    @BindView(R.id.number_btn_ok)
    Button number_btn_ok;
    @BindView(R.id.number)
    EditText number;
    @BindView(R.id.auth)
    EditText auth;
    @BindView(R.id.auth_btn)
    Button auth_btn;
    @BindView(R.id.auth_btn_ok)
    Button auth_btn_ok;
    @BindView(R.id.te)
    TextView te;

    String number_str = "", auth_str = "", re_num;
    String over_a = "no", over_b = "no", over_c = "pho";
    Handler handler = new Handler();

    AQuery aQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        ButterKnife.bind(this);
        aQuery = new AQuery(this);

        setContent();


    }

    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));

    }

    public void setContent() {

        te.setText("이메일로 인증하기");
        number.setHint("전화 번호를 입력해주세요( - 없이)");
        number.setInputType(InputType.TYPE_CLASS_NUMBER);

        number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (!charSequence.equals(re_num)) {
                    number_btn.setVisibility(View.VISIBLE);
                    number_btn_ok.setVisibility(View.GONE);
                    over_a = "no";
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        auth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.equals(re_num)) {
                    auth_btn.setVisibility(View.VISIBLE);
                    auth_btn_ok.setVisibility(View.GONE);
                    over_b = "no";
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @OnClick(R.id.number_btn)
    public void OnNunber() {
        number_str = number.getText().toString().trim();
        final int num = new Random().nextInt(9000) + 1000;

        if (over_c.equals("pho")) {
            if (!number_str.equals("")) {
                if (checkPhone(number_str)) {
                    //http://ec2-13-124-110-195.ap-northeast-2.compute.amazonaws.com/member/checkPhone
                    String url_num = LHGlobal.BaseURL + "/member/checkPhone";
                    final Map<String, Object> params_num = new HashMap<>();
                    params_num.put("member_phone", number_str);
                    aQuery.ajax(url_num, params_num, String.class, new AjaxCallback<String>() {
                        @Override
                        public void callback(String url_num, String object_num, AjaxStatus status) {
                            Log.d("핸드폰중복_예지", params_num.toString());
                            Log.d("핸드폰중복_은진씨", object_num);

                            try {
                                JSONObject jsonObject = new JSONObject(object_num);
                                if (jsonObject.getString("return").equals("true")) {
                                    String url = LHGlobal.BaseURL + "/member/smsCk";
                                    final Map<String, Object> params = new HashMap<>();
                                    params.put("number", num);
                                    params.put("phone", number_str);
                                    aQuery.ajax(url, params, String.class, new AjaxCallback<String>() {
                                        @Override
                                        public void callback(String url, String object, AjaxStatus status) {
                                            Log.d("핸드폰_은진씨", object);
                                            Log.d("핸드폰_예지", params.toString());

                                            try {
                                                JSONObject jsonObject = new JSONObject(object);
                                                String result = jsonObject.getString("return");
                                                if (result.equals("success")) {
                                                    re_num = jsonObject.getString("phone");

                                                    number_btn.setVisibility(View.GONE);
                                                    number_btn_ok.setVisibility(View.VISIBLE);
                                                    over_a = "ok";
                                                    Toast.makeText(SelectActivity.this, "인증번호를 전송하였습니다.", Toast.LENGTH_SHORT).show();

                                                }

                                            } catch (JSONException e) {

                                            }
                                        }
                                    }.header("User-Agent", "gh_mobile{}"));
                                } else {
                                    Toast.makeText(SelectActivity.this, "휴대폰번호가 중복입니다.", Toast.LENGTH_SHORT).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }

                    }.header("User-Agent", "gh_mobile{}"));
                } else {
                    Toast.makeText(SelectActivity.this, "휴대폰번호가 형식에 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                    over_a = "no";

                }
            } else {
                Toast.makeText(SelectActivity.this, "정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
                over_a = "no";

            }

        } else {
            if (!number_str.equals("")) {
                if (checkEmail(number_str)) {
                    String url_num = LHGlobal.BaseURL + "/member/checkEmail";
                    final Map<String, Object> params_num = new HashMap<>();
                    params_num.put("member_email", number_str);
                    aQuery.ajax(url_num, params_num, String.class, new AjaxCallback<String>() {
                        @Override
                        public void callback(String url_num, String object_num, AjaxStatus status) {
                            Log.d("핸드폰중복_예지", params_num.toString());
                            Log.d("핸드폰중복_은진씨", object_num);

                            try {
                                JSONObject jsonObject = new JSONObject(object_num);
                                if (jsonObject.getString("return").equals("true")) {
                                    String url = LHGlobal.BaseURL + "/member/email_ck";
                                    final Map<String, Object> params = new HashMap<>();
                                    params.put("val", num);
                                    params.put("email", number_str);
                                    aQuery.ajax(url, params, String.class, new AjaxCallback<String>() {
                                        @Override
                                        public void callback(String url, String object, AjaxStatus status) {
                                            Log.d("이메일_은진씨", object);
                                            Log.d("이메일_예지", params.toString());

                                            try {
                                                JSONObject jsonObject = new JSONObject(object);
                                                String result = jsonObject.getString("result");
                                                if (result.equals("true")) {
                                                    re_num = jsonObject.getString("val");

                                                    number_btn.setVisibility(View.GONE);
                                                    number_btn_ok.setVisibility(View.VISIBLE);
                                                    over_a = "ok";
                                                    Toast.makeText(SelectActivity.this, "인증번호를 전송하였습니다.", Toast.LENGTH_SHORT).show();
                                                }

                                            } catch (JSONException e) {

                                            }
                                        }
                                    }.header("User-Agent", "gh_mobile{}"));
                                } else {
                                    Toast.makeText(SelectActivity.this, "이메일이 중복입니다.", Toast.LENGTH_SHORT).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }

                    }.header("User-Agent", "gh_mobile{}"));
                } else {
                    Toast.makeText(SelectActivity.this, "휴대폰번호가 형식에 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                    over_a = "no";

                }
            } else {
                Toast.makeText(SelectActivity.this, "정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
                over_a = "no";

            }
        }

    }

    @OnClick(R.id.okok)
    public void onOK() {
        number_str = number.getText().toString().trim();
        auth_str = auth.getText().toString().trim();

        if (!number_str.equals("") && !auth_str.equals("") && over_a.equals("ok") && over_b.equals("ok")) {
            Intent intent = new Intent(SelectActivity.this, JoinActivity.class);
            intent.putExtra("select", number_str);
            intent.putExtra("over", over_c);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(SelectActivity.this, "정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.auth_btn)
    public void OnAuth() {
        auth_str = auth.getText().toString().trim();
        number_str = number.getText().toString().trim();
        if (!auth_str.equals("")) {
            if (auth_str.equals(re_num)) {
                auth_btn.setVisibility(View.GONE);
                auth_btn_ok.setVisibility(View.VISIBLE);
                over_b = "ok";
                Toast.makeText(SelectActivity.this, "인증번호가 확인되었습니다.", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(SelectActivity.this, "인증번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                over_b = "no";
            }
        } else {
            Toast.makeText(SelectActivity.this, "정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
            over_b = "no";

        }
    }


    @OnClick(R.id.te)
    public void OnTe() {

        handler.post(new Runnable() {
            @Override
            public void run() {
                if (te.getText().toString().contains("이메일")) {
                    te.setText("전화 번호로 인증하기");
                    number.setHint("이메일을 입력해주세요");
                    over_c = "em";
                    over_a = "no";
                    over_b = "no";
                    number.setText(null);
                    auth.setText(null);
                    number.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                } else {
                    te.setText("이메일로 인증하기");
                    number.setHint("전화 번호를 입력해주세요( - 없이)");
                    over_c = "pho";
                    over_a = "no";
                    over_b = "no";
                    number.setText(null);
                    auth.setText(null);
                    number.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
            }
        });
    }


    @OnClick({R.id.back})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

    //핸드폰 번호 형식 체크(-없이)
    public static boolean checkPhone(String phone) {
        String phonePt = "(01[016789])(\\d{4})(\\d{4})+$";
        Pattern p = Pattern.compile(phonePt);
        Matcher m = p.matcher(phone);
        return m.matches();
    }

    //이메일 형식 체크
    public static boolean checkEmail(String email) {
        String mail = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(mail);
        Matcher m = p.matcher(email);
        return m.matches();
    }

}
