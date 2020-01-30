package com.ghsoft.android.lighthouse;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.ghsoft.android.lighthouse.R;
import com.tsengvn.typekit.TypekitContextWrapper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AuthActivity extends AppCompatActivity
{

    @BindView(R.id.edit1)
    EditText edit1;
    @BindView(R.id.edit2)
    EditText edit2;
    @BindView(R.id.edit3)
    EditText edit3;
    @BindView(R.id.edit4)
    EditText edit4;
    @BindView(R.id.time)
    TextView time;

    AQuery aQuery;
    Intent intent_auth;
    String result_auth;


    private long starttime = 180 * 1000;
    private final long interval = 1 * 1000;
    private int secs;
    private int minutes;
    private int seconds;
    private long remainingmillisec;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);
        aQuery = new AQuery(this);

        intent_auth = getIntent();
        result_auth = intent_auth.getExtras().getString("auth");
        setContent();
//        setTime();
    }

    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    public void setContent()
    {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        edit1.setTransformationMethod(new CustomPasswordTransformationMethod());
        edit2.setTransformationMethod(new CustomPasswordTransformationMethod());
        edit3.setTransformationMethod(new CustomPasswordTransformationMethod());
        edit4.setTransformationMethod(new CustomPasswordTransformationMethod());

        edit1.addTextChangedListener(new TextWatcher()
        {
            String strCur;

            public void onTextChanged(final CharSequence s, int start, int before, int count)
            {
                if (s.length() == 1)
                {
                    edit2.requestFocus();
                    edit1.setNextFocusDownId(R.id.edit2);
                    edit1.setSelection(edit1.length());
                }
                else if (s.length() == 0)
                {
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

        edit2.addTextChangedListener(new TextWatcher()
        {
            String strCur;

            public void onTextChanged(final CharSequence s, int start, int before, int count)
            {
                if (s.length() > 12)
                {
                    edit2.setText(strCur);
                    edit2.setSelection(start);
                }
                else
                {
                    if (s.length() == 1)
                    {
                        edit3.requestFocus();
                        edit2.setNextFocusDownId(R.id.edit3);
                        edit2.setSelection(edit2.length());
                    }
                    else if (s.length() == 0)
                    {
                        edit1.requestFocus();
                        edit2.setNextFocusDownId(R.id.edit1);
                    }
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

        edit3.addTextChangedListener(new TextWatcher()
        {
            String strCur;

            public void onTextChanged(final CharSequence s, int start, int before, int count)
            {
                if (s.length() > 12)
                {
                    edit3.setText(strCur);
                    edit3.setSelection(start);
                }
                else
                {
                    if (s.length() == 1)
                    {
                        edit4.requestFocus();
                        edit3.setNextFocusDownId(R.id.edit4);
                        edit3.setSelection(edit3.length());

                    }
                    else if (s.length() == 0)
                    {
                        edit2.requestFocus();
                        edit3.setNextFocusDownId(R.id.edit2);
                    }
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

        edit4.addTextChangedListener(new TextWatcher()
        {
            String strCur;

            public void onTextChanged(final CharSequence s, int start, int before, int count)
            {
                if (s.length() > 12)
                {
                    edit4.setText(strCur);
                    edit4.setSelection(start);
                }
                else
                {
                    if (s.length() == 1)
                    {
                        InputMethodManager immhide = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                        immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        edit4.setSelection(edit4.length());

                        String pass_num = edit1.getText().toString() + edit2.getText().toString() + edit3.getText().toString() + s.toString();

                        if (result_auth.equals("id"))
                        {
                            Intent intent = new Intent();
                            intent.putExtra("num", pass_num);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                        else if (result_auth.equals("pass"))
                        {
                            if (intent_auth.getExtras().getString("num").equals(pass_num))
                            {
                                Intent intent = new Intent(AuthActivity.this, PSetActivity.class);
                                intent.putExtra("id", intent_auth.getExtras().getString("id"));
                                startActivity(intent);
                                finish();

                                ((Activity) PWActivity.context).finish();
                            }
                            else
                            {
                                finish();
                                Toast.makeText(AuthActivity.this, "인증번호가 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    else if (s.length() == 0)
                    {
                        edit3.requestFocus();
                        edit4.setNextFocusDownId(R.id.edit3);
                    }
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

    private static class CustomPasswordTransformationMethod extends PasswordTransformationMethod
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


//    public void setTime() {
//        Log.d("Startime", "KK" + starttime);
//        countDownTimer = new MyCountDownTimer(starttime, interval);
//        secs = (int) (starttime / 1000);
//        minutes = (secs % (60 * 60) / 60);
//        seconds = (secs % (60 * 60) % 60);
//        time.setText("" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds));
//        countDownTimer.start();
//
//    }
//
//    private class MyCountDownTimer extends CountDownTimer {
//        MyCountDownTimer(long starttime, long interval) {
//
//            super(starttime, interval);
//        }
//
//        @Override
//        public void onFinish() {
//            Toast.makeText(AuthActivity.this, "시간이 초과되었습니다.", Toast.LENGTH_SHORT).show();
//            finish();
//        }
//
//        @Override
//        public void onTick(long millisUntilFinished) {
//
//            remainingmillisec = millisUntilFinished;
//            secs = (int) (millisUntilFinished / 1000);
//
//            minutes = (secs % (60 * 60) / 60);
//            seconds = (secs % (60 * 60) % 60);
//
//            time.setText("" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds));
//
//        }
//    }
}
