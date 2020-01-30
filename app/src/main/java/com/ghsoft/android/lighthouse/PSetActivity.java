package com.ghsoft.android.lighthouse;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PSetActivity extends AppCompatActivity {

    @BindView(R.id.pass)
    EditText pass;
    @BindView(R.id.pass_ok)
    EditText pass_ok;
    @BindView(R.id.id_find)
    Button id_find;
    @BindView(R.id.id_find_ok)
    Button id_find_ok;

    String pass_str = "", pass_ok_str = "";
    String btn_a = "", btn_b = "";
    AQuery aQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pset);
        ButterKnife.bind(this);

        aQuery= new AQuery(this);


        setContent();

        pass.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        pass_ok.setTransformationMethod(new AsteriskPasswordTransformationMethod());

    }

    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));

    }

    public void setContent() {

        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                pass_str = pass.getText().toString().trim();
                pass_ok_str = pass_ok.getText().toString().trim();
                if (pass_ok_str.equals(charSequence.toString())) {
                    btn_a = "ok";
                } else {
                    btn_a = "no";

                }
                if (btn_a.equals("ok") &&  !pass_str.equals("")) {
                    id_find.setVisibility(View.GONE);
                    id_find_ok.setVisibility(View.VISIBLE);
                } else {
                    id_find.setVisibility(View.VISIBLE);
                    id_find_ok.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        pass_ok.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                pass_str = pass.getText().toString().trim();
                pass_ok_str = pass_ok.getText().toString().trim();
                Log.d("비밀번호찾기2", pass_str);
                Log.d("비밀번호찾기3", charSequence.toString());
                if (pass_str.equals(charSequence.toString())) {
                    btn_a = "ok";
                } else {
                    btn_a = "no";

                }
                if (btn_a.equals("ok") && !pass_ok_str.equals("")) {
                    id_find.setVisibility(View.GONE);
                    id_find_ok.setVisibility(View.VISIBLE);
                } else {
                    id_find.setVisibility(View.VISIBLE);
                    id_find_ok.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @OnClick(R.id.id_find)
    public void OnIdClick() {
        pass_str = pass.getText().toString().trim();
        pass_ok_str = pass_ok.getText().toString().trim();

        if (!pass_str.equals("") && !pass_ok_str.equals("")) {
            if (pass_str.equals(pass_ok_str)) {

            } else {
                Toast.makeText(PSetActivity.this, "비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();

            }

        } else {
            Toast.makeText(PSetActivity.this, "정보를 입력해주세요.", Toast.LENGTH_SHORT).show();

        }

    }

    @OnClick(R.id.id_find_ok)
    public void OnIdFindClick() {
        Intent intent=getIntent();
        String id = intent.getExtras().getString("id");
        if (!pass_str.equals("") && !pass_ok_str.equals("")) {
            if (pass_str.equals(pass_ok_str)) {
                String url = LHGlobal.BaseURL + "/member/updatePass";
                final Map<String, Object> params = new HashMap<>();
                params.put("member_id", id);
                params.put("pass", pass_ok_str);
                aQuery.ajax(url, params, String.class, new AjaxCallback<String>() {
                    @Override
                    public void callback(String url, String object, AjaxStatus status) {
                        Log.d("비밀번호바꿈_은진씨", object);
                        Log.d("비밀번호바꿈_예지", params.toString());

                        try {
                            JSONObject jsonObject = new JSONObject(object);
                            String result = jsonObject.getString("return");
                            if (result.equals("true")) {
                                Toast.makeText(PSetActivity.this, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                        } catch (JSONException e) {

                        }
                    }
                }.header("User-Agent", "gh_mobile{}"));

            }
        }

    }


    @OnClick(R.id.back)
    public void OnBackclick() {
        finish();
    }

    public class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new AsteriskPasswordTransformationMethod.PasswordCharSequence(source);
        }

        private class PasswordCharSequence implements CharSequence {
            private CharSequence mSource;
            public PasswordCharSequence(CharSequence source) {
                mSource = source; // Store char sequence
            }
            public char charAt(int index) {
                return '*'; // This is the important part
            }
            public int length() {
                return mSource.length(); // Return default
            }
            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end); // Return default
            }
        }
    };
}
