package com.ghsoft.android.lighthouse;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ToggleButton;

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

import static com.ghsoft.android.lighthouse.Time_Activity.aa;
import static com.ghsoft.android.lighthouse.Time_Activity.bb;

public class PushActivity extends AppCompatActivity {

    @BindView(R.id.btn1)
    ToggleButton btn1;
    @BindView(R.id.btn2)
    ToggleButton btn2;
    @BindView(R.id.btn3)
    ToggleButton btn3;
    @BindView(R.id.btn4)
    ToggleButton btn4;
    @BindView(R.id.btn5)
    ToggleButton btn5;
    private Time_Activity mCustomDialog;
    SharedPreferences pref, pf;
    SharedPreferences.Editor editor;

    String idx;
    AQuery aQuery;

    String push1, push2, push3, push4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push);
        ButterKnife.bind(this);
        aQuery = new AQuery(this);
        pref = getSharedPreferences("push", MODE_PRIVATE);
        editor = pref.edit();

        pf = getSharedPreferences("member", MODE_PRIVATE);
        idx = pf.getString("idx", "");


        String url = LHGlobal.BaseURL + "/member/select_push";
        final Map<String, Object> params = new HashMap<>();
        params.put("member_idx", idx);
        aQuery.ajax(url, params, String.class, new AjaxCallback<String>() {
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                Log.d("푸시_예지", params.toString());
                Log.d("푸시_은진씨", object);
                try {
                    JSONObject jsonObject = new JSONObject(object);

                    push1 = jsonObject.getString("push1");
                    push2 = jsonObject.getString("push2");
                    push3 = jsonObject.getString("push3");
                    push4 = jsonObject.getString("push4");

                    if (push1.equals("1")) {
                        btn1.setBackgroundResource(R.drawable.push_on);
                        btn1.setChecked(true);
                    } else {
                        btn1.setBackgroundResource(R.drawable.push_off);
                        btn1.setChecked(false);
                    }

                    if (push2.equals("1")) {
                        btn2.setBackgroundResource(R.drawable.push_on);
                        btn2.setChecked(true);
                    } else {
                        btn2.setBackgroundResource(R.drawable.push_off);
                        btn2.setChecked(false);
                    }

                    if (push3.equals("1")) {
                        btn3.setBackgroundResource(R.drawable.push_on);
                        btn3.setChecked(true);
                    } else {
                        btn3.setBackgroundResource(R.drawable.push_off);
                        btn3.setChecked(false);
                    }

                    if (push4.equals("1")) {
                        btn4.setBackgroundResource(R.drawable.push_on);
                        btn4.setChecked(true);
                    } else {
                        btn4.setBackgroundResource(R.drawable.push_off);
                        btn4.setChecked(false);
                    }

                } catch (JSONException e) {

                }
            }
        }.header("User-Agent", "gh_mobile{}"));

        if (pref.getString("onoff", "0").equals("1")) {
            btn5.setBackgroundResource(R.drawable.push_on);
            btn5.setChecked(true);
        } else {
            btn5.setBackgroundResource(R.drawable.push_off);
            btn5.setChecked(false);
        }


    }

    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));

    }


    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.time, R.id.back})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                if (btn1.isChecked()) {
                    String url = LHGlobal.BaseURL + "/member/update_push/1";
                    final Map<String, Object> params = new HashMap<>();
                    params.put("member_idx", idx);
                    params.put("push", "1");
                    aQuery.ajax(url, params, String.class, new AjaxCallback<String>() {
                        @Override
                        public void callback(String url, String object, AjaxStatus status) {
                            Log.d("푸시_예지", params.toString());
                            Log.d("푸시_은진씨", object);
                            try {
                                JSONObject jsonObject = new JSONObject(object);

                                String str = jsonObject.getString("return");

                                if (str.equals("true")) {
                                    btn1.setBackgroundResource(R.drawable.push_on);
                                }
                            } catch (JSONException e) {

                            }
                        }
                    }.header("User-Agent", "gh_mobile{}"));
                } else {
                    String url = LHGlobal.BaseURL + "/member/update_push/1";
                    final Map<String, Object> params = new HashMap<>();
                    params.put("member_idx", idx);
                    params.put("push", "0");
                    aQuery.ajax(url, params, String.class, new AjaxCallback<String>() {
                        @Override
                        public void callback(String url, String object, AjaxStatus status) {
                            Log.d("푸시_예지", params.toString());
                            Log.d("푸시_은진씨", object);
                            try {
                                JSONObject jsonObject = new JSONObject(object);

                                String str = jsonObject.getString("return");

                                if (str.equals("true")) {
                                    btn1.setBackgroundResource(R.drawable.push_off);
                                }
                            } catch (JSONException e) {

                            }
                        }
                    }.header("User-Agent", "gh_mobile{}"));
                }
                break;

            case R.id.btn2:
                if (btn2.isChecked()) {
                    String url = LHGlobal.BaseURL + "/member/update_push/2";
                    final Map<String, Object> params = new HashMap<>();
                    params.put("member_idx", idx);
                    params.put("push", "1");
                    aQuery.ajax(url, params, String.class, new AjaxCallback<String>() {
                        @Override
                        public void callback(String url, String object, AjaxStatus status) {
                            Log.d("푸시_예지", params.toString());
                            Log.d("푸시_은진씨", object);
                            try {
                                JSONObject jsonObject = new JSONObject(object);

                                String str = jsonObject.getString("return");

                                if (str.equals("true")) {
                                    btn2.setBackgroundResource(R.drawable.push_on);
                                }
                            } catch (JSONException e) {

                            }
                        }
                    }.header("User-Agent", "gh_mobile{}"));
                } else {
                    String url = LHGlobal.BaseURL + "/member/update_push/2";
                    final Map<String, Object> params = new HashMap<>();
                    params.put("member_idx", idx);
                    params.put("push", "0");
                    aQuery.ajax(url, params, String.class, new AjaxCallback<String>() {
                        @Override
                        public void callback(String url, String object, AjaxStatus status) {
                            Log.d("푸시_예지", params.toString());
                            Log.d("푸시_은진씨", object);
                            try {
                                JSONObject jsonObject = new JSONObject(object);

                                String str = jsonObject.getString("return");

                                if (str.equals("true")) {
                                    btn2.setBackgroundResource(R.drawable.push_off);

                                }
                            } catch (JSONException e) {

                            }
                        }
                    }.header("User-Agent", "gh_mobile{}"));
                }
                break;

            case R.id.btn3:
                if (btn3.isChecked()) {
                    String url = LHGlobal.BaseURL + "/member/update_push/3";
                    final Map<String, Object> params = new HashMap<>();
                    params.put("member_idx", idx);
                    params.put("push", "1");
                    aQuery.ajax(url, params, String.class, new AjaxCallback<String>() {
                        @Override
                        public void callback(String url, String object, AjaxStatus status) {
                            Log.d("푸시_예지", params.toString());
                            Log.d("푸시_은진씨", object);
                            try {
                                JSONObject jsonObject = new JSONObject(object);

                                String str = jsonObject.getString("return");

                                if (str.equals("true")) {
                                    btn3.setBackgroundResource(R.drawable.push_on);

                                }
                            } catch (JSONException e) {

                            }
                        }
                    }.header("User-Agent", "gh_mobile{}"));
                } else {
                    String url = LHGlobal.BaseURL + "/member/update_push/3";
                    final Map<String, Object> params = new HashMap<>();
                    params.put("member_idx", idx);
                    params.put("push", "0");
                    aQuery.ajax(url, params, String.class, new AjaxCallback<String>() {
                        @Override
                        public void callback(String url, String object, AjaxStatus status) {
                            Log.d("푸시_예지", params.toString());
                            Log.d("푸시_은진씨", object);
                            try {
                                JSONObject jsonObject = new JSONObject(object);

                                String str = jsonObject.getString("return");

                                if (str.equals("true")) {
                                    btn3.setBackgroundResource(R.drawable.push_off);


                                }
                            } catch (JSONException e) {

                            }
                        }
                    }.header("User-Agent", "gh_mobile{}"));
                }
                break;

            case R.id.btn4:
                if (btn4.isChecked()) {
                    String url = LHGlobal.BaseURL + "/member/update_push/4";
                    final Map<String, Object> params = new HashMap<>();
                    params.put("member_idx", idx);
                    params.put("push", "1");
                    aQuery.ajax(url, params, String.class, new AjaxCallback<String>() {
                        @Override
                        public void callback(String url, String object, AjaxStatus status) {
                            Log.d("푸시_예지", params.toString());
                            Log.d("푸시_은진씨", object);
                            try {
                                JSONObject jsonObject = new JSONObject(object);

                                String str = jsonObject.getString("return");

                                if (str.equals("true")) {
                                    btn4.setBackgroundResource(R.drawable.push_on);

                                }
                            } catch (JSONException e) {

                            }
                        }
                    }.header("User-Agent", "gh_mobile{}"));
                } else {
                    String url = LHGlobal.BaseURL + "/member/update_push/4";
                    final Map<String, Object> params = new HashMap<>();
                    params.put("member_idx", idx);
                    params.put("push", "0");
                    aQuery.ajax(url, params, String.class, new AjaxCallback<String>() {
                        @Override
                        public void callback(String url, String object, AjaxStatus status) {
                            Log.d("푸시_예지", params.toString());
                            Log.d("푸시_은진씨", object);
                            try {
                                JSONObject jsonObject = new JSONObject(object);

                                String str = jsonObject.getString("return");

                                if (str.equals("true")) {
                                    btn4.setBackgroundResource(R.drawable.push_off);

                                }
                            } catch (JSONException e) {

                            }
                        }
                    }.header("User-Agent", "gh_mobile{}"));
                }
                break;

            case R.id.btn5:
                if (btn5.isChecked()) {
                    btn5.setBackgroundResource(R.drawable.push_on);
                    editor.putString("onoff", "1");
                    editor.commit();
                } else {
                    btn5.setBackgroundResource(R.drawable.push_off);
                    editor.putString("onoff", "0");
                    editor.commit();
                }
                break;

            case R.id.time:
                mCustomDialog = new Time_Activity(PushActivity.this, okListener);
                mCustomDialog.show();
                break;

            case R.id.back:
                finish();
                break;

        }
    }


    private View.OnClickListener okListener = new View.OnClickListener() {
        public void onClick(View v) {
            String url = LHGlobal.BaseURL + "/member/disturbance";
            final Map<String, Object> params = new HashMap<>();

            if (aa < 10) {
                params.put("nopushtime1", "0" + aa + ":00:00");

            } else {
                params.put("nopushtime1", aa + ":00:00");

            }

            if (bb < 10) {
                params.put("nopushtime2", "0" + bb + ":00:00");

            } else {
                params.put("nopushtime2", bb + ":00:00");

            }
            params.put("member_idx", idx);
            aQuery.ajax(url, params, String.class, new AjaxCallback<String>() {
                @Override
                public void callback(String url, String object, AjaxStatus status) {
                    Log.d("푸시_예지", params.toString());
                    Log.d("푸시_은진씨", object);
                    try {
                        JSONObject jsonObject = new JSONObject(object);

                        String str = jsonObject.getString("return");

                        if (str.equals("true")) {
                            mCustomDialog.dismiss();
                        }
                    } catch (JSONException e) {

                    }
                }
            }.header("User-Agent", "gh_mobile{}"));
        }
    };


}
