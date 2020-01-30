package com.ghsoft.android.lighthouse;

/**
 * Created by YJhong on 2016-11-28.
 */

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class FirebaseInstanceIDService extends FirebaseInstanceIdService
{

    private static final String TAG = "MyFirebaseIIDService";

    // [START refresh_token]
    @Override
    public void onTokenRefresh()
    {
        // Get updated InstanceID token.
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + token);

        // 생성등록된 토큰을 개인 앱서버에 보내 저장해 두었다가 추가 뭔가를 하고 싶으면 할 수 있도록 한다.
        SharedPreferences pref = FirebaseInstanceIDService.this.getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor prefEd = pref.edit();
        prefEd.clear();
        prefEd.putString("Token", token);
        prefEd.commit();

        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token)
    {
        // Add custom implementation, as needed.

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                //어플 실행시 토큰값과 package명을 함께 전송
                .add("Token", token)
                .add("package", "lighthouse")
                .build();

        //request
        Request request = new Request.Builder()
                .url("http://push.globalhumanism.kr/push/?type=action&value=reg")
                .post(body)
                .build();
        try
        {
            client.newCall(request).execute();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}


