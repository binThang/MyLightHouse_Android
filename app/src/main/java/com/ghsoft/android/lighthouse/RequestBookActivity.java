package com.ghsoft.android.lighthouse;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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

public class RequestBookActivity extends AppCompatActivity
{
    @BindView(R.id.book_tony)
    EditText etTony;
    @BindView(R.id.book_address)
    EditText etAddress;
    @BindView(R.id.id_find)
    Button id_find;
    @BindView(R.id.id_find_ok)
    Button id_find_ok;

    String tony_str, address_str;

    boolean isTonyInserted = false;
    boolean isAddressInserted = false;

    AQuery aQuery;
    SharedPreferences pref;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_book);
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
        etTony.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                isTonyInserted = !charSequence.toString().isEmpty();

                if (isTonyInserted && isAddressInserted)
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

        etAddress.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                isAddressInserted = !charSequence.toString().isEmpty();

                if (isAddressInserted && isTonyInserted)
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
                tony_str = etTony.getText().toString().trim();
                address_str = etAddress.getText().toString().trim();

                if (!tony_str.equals("") && !address_str.equals(""))
                    Toast.makeText(this, "정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
                break;

            case R.id.id_find_ok:
                tony_str = etTony.getText().toString().trim();
                address_str = etAddress.getText().toString().trim();
                String givemebook_url = getString(R.string.server_base_url) + "/customer/give_me_book";

                // Parameters
                final Map<String, Object> params = new HashMap<>();
                params.put("member_idx", pref.getString("idx", ""));
                params.put("tony_name", tony_str);
                params.put("address", address_str);

                aQuery.ajax(givemebook_url, params, String.class, new AjaxCallback<String>()
                {
                    @Override
                    public void callback(String url, String object, AjaxStatus status)
                    {
                        try
                        {
                            JSONObject jsonObject = new JSONObject(object);

                            String str = jsonObject.getString("return");

                            if (str.equals("success"))
                            {
                                finish();
                                Toast.makeText(RequestBookActivity.this, "접수되었습니다. " + jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(RequestBookActivity.this, jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (JSONException e)
                        {
                            Toast.makeText(RequestBookActivity.this, e.getMessage(), Toast.LENGTH_SHORT);
                        }
                    }
                }.header("User-Agent", "gh_mobile{}"));
                break;
            case R.id.back:
                finish();
                break;
        }
    }
}
