package com.ghsoft.android.lighthouse;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.firebase.iid.FirebaseInstanceId;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewPageActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, MainAdapter.AdapterCallback,
        View.OnTouchListener {

    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.top_text)
    TextView top_text;
    @BindView(R.id.toptop)
    LinearLayout toptop;
    @BindView(R.id.dot)
    ImageView dot;
    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.fab_text)
    TextView fab_text;
    @BindView(R.id.fabview)
    LinearLayout fabview;
    @BindView(R.id.spinlist_top)
    LinearLayout spinlist_top;
    @BindView(R.id.spin)
    LinearLayout spin;
    @BindView(R.id.spinlist)
    LinearLayout spinlist;
    @BindView(R.id.search_white_delete)
    LinearLayout search_white_delete;
    @BindView(R.id.spin_text)
    TextView spin_text;
    String url = LHGlobal.BaseURL + "/fun/list/1";
    WebView childView;
    ProgressDialog pdLoading;

    String url_in, name, idx, num;
    Handler handler = new Handler();
    AQuery aQuery;

    String token;
    private ShareActivity mCustomDialog;
    private DeleteActivity deleteDialog;

    private GestureDetectorCompat gestureDetector;
    float X_down, X_up;
    boolean isSwiped;

    String meyou = "", sel = "", type = "";
    int k = 0, j = 0, rere = 0, momo = 0;

    private MainAdapter mAdapter;
    List<MainData> data = new ArrayList<>();
    String board = "1";

    String dat_idx, dada = "";
    static Context context;
    File file;
    String imgPath;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        gestureDetector.onTouchEvent(motionEvent);
        return view.onTouchEvent(motionEvent);
    }

    private class AndroidBridge {
        @JavascriptInterface
        public void uppdate() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.d("은진씨가원하는브릿지","uppdate");
                    rere = 0;
                    k = 0;
                    j = 0;
                    momo = 0;
                    toptop.setVisibility(View.VISIBLE);
                    fabview.setVisibility(View.GONE);
                    spinlist_top.setVisibility(View.GONE);
                    spinlist.setVisibility(View.GONE);
                    search_white_delete.setVisibility(View.GONE);
                }
            });
        }

        //글쓰기 배경 바꾸기
        @JavascriptInterface
        public void out() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    editor.clear();
                    editor.commit();
                    Intent intent = new Intent(NewPageActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    MainActivity act = (MainActivity) MainActivity.activity;
                    act.finish();
                    Log.d("zzzzzzzzzzzz", "outoutoutoutoutoutout");
                }
            });
        }

        @JavascriptInterface
        public void updatee(final String str) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.d("은진씨가원하는브릿지","updatee");
                    dada = "top";
                    Log.d("삭제하기_updatee", str);
                    Log.d("수정하기_updatee", str);
                    dat_idx = str;
                    deleteDialog = new DeleteActivity(NewPageActivity.this, top_dat, bottom_dat);
                    deleteDialog.show();
                }
            });
        }

        @JavascriptInterface
        public void updateee(final String str) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.d("은진씨가원하는브릿지","updateee");
                    dada = "bottom";
                    Log.d("삭제하기_updateee", str);
                    Log.d("수정하기_updateee", str);
                    dat_idx = str;
                    deleteDialog = new DeleteActivity(NewPageActivity.this, top_dat, bottom_dat);
                    deleteDialog.show();
                }
            });
        }

        @JavascriptInterface
        public void keyboardup() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.d("은진씨가원하는브릿지","keyboardup");
                    Log.d("들어옴34", "keyboardup");
                    Activity activity = (Activity) context;
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(activity.getCurrentFocus(), InputMethodManager.SHOW_FORCED);

                }
            });
        }


        @JavascriptInterface
        public void share(final String idx_s) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.d("은진씨가원하는브릿지","share");
                    idx = idx_s;
                    mCustomDialog = new ShareActivity(NewPageActivity.this, top, bottom);
                    mCustomDialog.show();

                }
            });
        }

        @JavascriptInterface
        public void dot(final String str, final String idx_str, final String strstr, final String strstrstr) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.d("은진씨가원하는브릿지","dot");
                    meyou = str;
                    idx = idx_str;
                    sel = strstr;
                    type = strstrstr;
                    Log.d("리스트리스트1", str);
                    Log.d("리스트리스트2", idx_str);
                    Log.d("리스트리스트3", strstr);
                    Log.d("리스트리스트4", strstrstr);
//                    Log.d("삭제하기", idx_str);
                    if (meyou.equals("me")) {
                        dot.setVisibility(View.VISIBLE);
                    } else {
                        dot.setVisibility(View.GONE);

                    }
                }
            });
        }


        //글쓰기 눌렀을때
        @JavascriptInterface
        public void writea(final String str) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.d("은진씨가원하는브릿지","writea");
                    Log.d("들어옴", "write");
                    Log.d("들어옴", str);
                    rere = 1;
                }
            });

        }

        //글쓰기 배경 바꾸기
        @JavascriptInterface
        public void sajin() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.d("은진씨가원하는브릿지","sajin");
                    j = 1;
                    rere = 0;
                    fabview.setVisibility(View.GONE);
                    spinlist_top.setVisibility(View.GONE);
                    spinlist.setVisibility(View.GONE);
                    search_white_delete.setVisibility(View.VISIBLE);

                }
            });

        }

        //글쓰기 사진 클릭했을때
        @JavascriptInterface
        public void selectpic() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.d("은진씨가원하는브릿지","selectpic");
                    j = 0;
                    rere = 1;
                    fabview.setVisibility(View.VISIBLE);
                    spinlist_top.setVisibility(View.GONE);
                    spinlist.setVisibility(View.GONE);
                    search_white_delete.setVisibility(View.GONE);
                }
            });

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NewPageActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_new_page);
        ButterKnife.bind(this);
        token = FirebaseInstanceId.getInstance().getToken();
        aQuery = new AQuery(this);
        pdLoading = new ProgressDialog(this);
        gestureDetector = new GestureDetectorCompat(this, this);
        context = NewPageActivity.this;
        pref = getSharedPreferences("member", MODE_PRIVATE);
        editor = pref.edit();
        setData();

        Intent intent = getIntent();
        url_in = intent.getExtras().getString("url");
        Log.d("Newpage", url_in);

        name = intent.getExtras().getString("name");
        idx = intent.getExtras().getString("idx");
        num = intent.getExtras().getString("num");

        top_text.setText(name);

        setWebView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == RC_FILE_CHOOSE && resultCode == RESULT_OK)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                if (mFilePathCallback == null)
                {
                    super.onActivityResult(requestCode, resultCode, data);
                    return;
                }
                Uri[] results = new Uri[]{getResultUri(data)};

                mFilePathCallback.onReceiveValue(results);
                mFilePathCallback = null;
            }
            else
            {
                if (mUploadMsg == null)
                {
                    super.onActivityResult(requestCode, resultCode, data);
                    return;
                }
                Uri result = getResultUri(data);

                Log.d(getClass().getName(), "openFileChooser : " + result);

                mUploadMsg.onReceiveValue(result);
                mUploadMsg = null;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder
                    .setMessage("사진이 첨부되었습니다")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                    }).show();
        }
        else
        {
            if (mFilePathCallback != null) mFilePathCallback.onReceiveValue(null);
            if (mUploadMsg != null) mUploadMsg.onReceiveValue(null);
            mFilePathCallback = null;
            mUploadMsg = null;
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private Uri getResultUri(Intent data)
    {
        Uri result = null;
        if (data == null || TextUtils.isEmpty(data.getDataString()))
        {
            // If there is not data, then we may have taken a photo
            if (mCameraPhotoPath != null)
            {
                result = Uri.parse(mCameraPhotoPath);
            }
        }
        else
        {
            String filePath = "";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                filePath = data.getDataString();
            }
            else
            {
                filePath = "file:" + RealPathUtil.getRealPath(this, data.getData());
            }
            result = Uri.parse(filePath);
        }

        return result;
    }

    public void onMethodCallback(final int pos, String name, String idx)
    {
        k = 0;
        rere = 1;
        fabview.setVisibility(View.VISIBLE);
        spinlist_top.setVisibility(View.GONE);
        fab_text.setText(name);
        webView.loadUrl("javascript:select_hashtag(" + idx + ")");
        Log.d("리스트리스트3", idx + "");
        board = idx;
        spinlist.setVisibility(View.GONE);
    }


    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));

    }

    public void setData() {

        String url_num = LHGlobal.BaseURL + "/back/select_category";
        aQuery.ajax(url_num, String.class, new AjaxCallback<String>() {
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                try {
                    Log.d("ddddddddddd", object);
                    JSONObject jsonObject = new JSONObject(object);

                    JSONArray caidxArr = new JSONArray(jsonObject.getString("category_idx"));
                    JSONArray caArr = new JSONArray(jsonObject.getString("category"));
                    JSONArray colorArr = new JSONArray(jsonObject.getString("color"));
                    for (int i = 0; caArr.length() > i; i++) {
                        data.add(new MainData(caArr.getString(i), colorArr.getBoolean(i), caidxArr.getString(i)));
                    }
                    board = sel;
                    data.get(0).color = false;
                    data.get(Integer.parseInt(sel) - 1).color = true;
                    fab_text.setText(data.get(Integer.parseInt(sel) - 1).text + "");
                    spin_text.setText(data.get(Integer.parseInt(sel) - 1).text + "");
                    mAdapter = new MainAdapter(NewPageActivity.this, data);
                    list.setAdapter(mAdapter);
                    list.setLayoutManager(new LinearLayoutManager(NewPageActivity.this));
                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {

                }
            }

        }.header("User-Agent", "gh_mobile{}"));
    }

    private static final int RC_FILE_CHOOSE = 2833;
    private ValueCallback<Uri> mUploadMsg = null;
    private ValueCallback<Uri[]> mFilePathCallback = null;
    private String mCameraPhotoPath;

    public void setWebView() {
        webView.getSettings().setTextZoom(100);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setSupportMultipleWindows(true);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);// 웹뷰 성능 개선
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.setOnTouchListener(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        if (18 < Build.VERSION.SDK_INT) {
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        }

        File dir = getCacheDir();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        webView.getSettings().setAppCachePath(dir.getPath());
        webView.getSettings().setAppCacheEnabled(true);

        webView.addJavascriptInterface(new AndroidBridge(), "lighthouse");


        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies(webView, true); // false 설정 시 오류 발생
        }

        String userAgent = new WebView(getBaseContext()).getSettings().getUserAgentString();
        webView.getSettings().setUserAgentString(userAgent + " gh_mobile{" + token + "}");

        webView.loadUrl(LHGlobal.BaseURL + url_in);


        webView.setWebViewClient(new MyWC());
        webView.setWebChromeClient(new WebChromeClient() {
            private View mCustomView;
            private FullscreenHolder mFullscreenContainer;
            private CustomViewCallback mCustomViewCollback;

            @Override
            public void onCloseWindow(WebView window) {
                window.setVisibility(View.GONE);
                window.goBack();
            }

            public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType) {
                openFileChooser(uploadFile);
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                openFileChooser(uploadMsg);
            }

            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                if (mFilePathCallback != null)
                    mFilePathCallback.onReceiveValue(null);
                mFilePathCallback = filePathCallback;

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                NewPageActivity.this.startActivityForResult(Intent.createChooser(intent, "File Chooser"), RC_FILE_CHOOSE);

                return true;
            }

            private void imageChooser() {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                        takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        Log.e(getClass().getName(), "Unable to create Image File", ex);
                    }

                    // Continue only if the File was successfully creat
                    if (photoFile != null) {
                        mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    } else {
                        takePictureIntent = null;
                    }
                }


            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMsg = uploadMsg;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                NewPageActivity.this.startActivityForResult(Intent.createChooser(intent, "File Chooser"), RC_FILE_CHOOSE);
            }

            /**
             * More info this method can be found at
             * http://developer.android.com/training/camera/photobasics.html
             *
             * @return
             * @throws IOException
             */
            private File createImageFile() throws IOException {
                // Create an image file name
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "_";
                File storageDir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES);
                File imageFile = File.createTempFile(
                        imageFileName, /* prefix */
                        ".jpg", /* suffix */
                        storageDir /* directory */
                );
                return imageFile;
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NewPageActivity.this);
                builder.setMessage(message)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm();
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(NewPageActivity.this);
                builder.setMessage(message)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                result.confirm();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                result.cancel();
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
                return true;
            }

            @Override
            public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, Message resultMsg)
            {
                Log.d("eeee", "eeeeeeeeeeeee");
                view.removeAllViews();
                childView = new WebView(view.getContext());
                childView.getSettings().setJavaScriptEnabled(true);
                childView.getSettings().setLoadWithOverviewMode(true);
                childView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                childView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
                childView.getSettings().setUseWideViewPort(true);
                childView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

                childView.setWebChromeClient(this);
                childView.setWebViewClient(new WebViewClient()
                {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url)
                    {
                        if (url.startsWith("http://"))
                            return false;
                        return super.shouldOverrideUrlLoading(view, url);
                    }
                });

                childView.setLayoutParams(new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT));
                childView.findFocus();
                view.addView(childView);

                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                transport.setWebView(childView);
                resultMsg.sendToTarget();
                view.scrollTo(0, 0);

                return true;
            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {

                if (mCustomView != null) {
                    callback.onCustomViewHidden();
                    return;
                }


                FrameLayout decor = (FrameLayout) NewPageActivity.this.getWindow().getDecorView();


                mFullscreenContainer = new FullscreenHolder(NewPageActivity.this);
                mFullscreenContainer.addView(view, ViewGroup.LayoutParams.MATCH_PARENT);
                decor.addView(mFullscreenContainer, ViewGroup.LayoutParams.MATCH_PARENT);
                mCustomView = view;
                mCustomViewCollback = callback;

            }

            @Override
            public void onHideCustomView() {
                if (mCustomView == null) {
                    return;
                }

                FrameLayout decor = (FrameLayout) NewPageActivity.this.getWindow().getDecorView();
                decor.removeView(mFullscreenContainer);
                mFullscreenContainer = null;
                mCustomView = null;
                mCustomViewCollback.onCustomViewHidden();

                NewPageActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }


            class FullscreenHolder extends FrameLayout {

                public FullscreenHolder(Activity ctx) {
                    super(ctx);
                    setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                    SystemClock.sleep(500);
                    NewPageActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    NewPageActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

                }

                @Override
                public boolean onTouchEvent(MotionEvent evt) {
                    return true;
                }
            }

        });
    }

    private class MyWC extends WebViewClient {
        @Override

        public void onPageStarted(WebView view, String url_s, Bitmap favicon) {
            super.onPageStarted(view, url_s, favicon);
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("tel:")) {
                Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                startActivity(i);
                return true;
            }

            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            try {
                pdLoading.dismiss();
            } catch (Exception e) {
                pdLoading.dismiss();

            }
            Log.d("로그인_로그인_로그인_끝", url);

        }

        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            super.doUpdateVisitedHistory(view, url, isReload);
            Log.d("로그인_로그인_로그인_히스토리", url);

        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            Log.d("로그인_로그인_로그인_오류1", errorCode + "");
            Log.d("로그인_로그인_로그인_오류2", description + "");
            Log.d("로그인_로그인_로그인_오류3", failingUrl + "");

        }
    }

    @OnClick(R.id.dot)
    public void OnDot() {
        deleteDialog = new DeleteActivity(NewPageActivity.this, top_del, bottom_del);
        deleteDialog.show();
    }

    @OnClick(R.id.back)
    public void onBack() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", num);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @OnClick({R.id.fab_back, R.id.spin, R.id.del, R.id.save})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.spin:
                k = 1;
                rere = 0;
                fabview.setVisibility(View.GONE);
                spinlist_top.setVisibility(View.VISIBLE);
                spinlist.setVisibility(View.VISIBLE);
                break;
            case R.id.del:
                k = 0;
                rere = 1;
                fabview.setVisibility(View.VISIBLE);
                spinlist_top.setVisibility(View.GONE);
                spinlist.setVisibility(View.GONE);
                break;

            case R.id.save:
                webView.loadUrl("javascript:update_board(" + board + ")");

                break;

            case R.id.fab_back:
                rere = 0;
                toptop.setVisibility(View.VISIBLE);
                fabview.setVisibility(View.GONE);
                webView.loadUrl(LHGlobal.BaseURL + url_in);
                break;
        }
    }


    private View.OnClickListener top = new View.OnClickListener() {
        public void onClick(View v) {

            String url = LHGlobal.BaseURL + "/fun/imgDownload/" + idx;
            aQuery.ajax(url, String.class, new AjaxCallback<String>() {
                @Override
                public void callback(String url, String object, AjaxStatus status) {
                    Log.d("이미지다운로드_은진씨", object);
                    try {
                        JSONObject jsonObject = new JSONObject(object);
                        String image = jsonObject.getString("image");

                        JSONArray jsonArray = new JSONArray(image);

                        for (int i = 0; jsonArray.length() > i; i++) {
                            new DownloadFileFromURL().execute(LHGlobal.BaseURL + "/public/uploads/mobile/image/" + jsonArray.getString(i));
                        }

                    } catch (JSONException e) {

                    }
                }
            }.header("User-Agent", "gh_mobile{}"));


            mCustomDialog.dismiss();
        }
    };


    private View.OnClickListener bottom = new View.OnClickListener() {
        public void onClick(View v) {
            String text = LHGlobal.BaseURL + "/share/index/" + idx;
            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, text);
            Intent chooser = Intent.createChooser(intent, "공유하기");
            startActivity(chooser);
            mCustomDialog.dismiss();

        }
    };

    private View.OnClickListener top_del = new View.OnClickListener() {
        public void onClick(View v) {
            rere = 1;
            momo = 1;
            data = new ArrayList<>();
            setData();
            toptop.setVisibility(View.GONE);
            fabview.setVisibility(View.VISIBLE);
            search_white_delete.setVisibility(View.GONE);
            webView.loadUrl(LHGlobal.BaseURL + "/back/update/" + idx);
            deleteDialog.dismiss();


        }
    };

    private View.OnClickListener bottom_del = new View.OnClickListener() {
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(NewPageActivity.this);
            builder.setTitle("삭제하시겠습니까?")
                    .setCancelable(false)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, int whichButton) {
                            String url = LHGlobal.BaseURL + "/back/del_board";
                            final Map<String, Object> params = new HashMap<>();
                            params.put("board_idx", idx);
                            aQuery.ajax(url, params, String.class, new AjaxCallback<String>() {
                                @Override
                                public void callback(String url, String object, AjaxStatus status) {
                                    Log.d("삭제_은진씨", object);
                                    Log.d("삭제_예지", params.toString());
                                    try {
                                        JSONObject jsonObject = new JSONObject(object);
                                        String result = jsonObject.getString("result");
                                        if (result.equals("true")) {
                                            Intent returnIntent = new Intent();
                                            returnIntent.putExtra("result", "mymym");
                                            Log.d("수정하기12", type);
                                            returnIntent.putExtra("type", type);
                                            setResult(Activity.RESULT_OK, returnIntent);
                                            finish();
                                            dialog.dismiss();
                                        }
                                    } catch (JSONException e) {
                                        dialog.dismiss();

                                    }

                                }
                            }.header("User-Agent", "gh_mobile{}"));
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });

            AlertDialog dialog = builder.create();    // 알림창 객체 생성
            dialog.show();    // 알림창 띄우기
            deleteDialog.dismiss();

        }
    };

    private View.OnClickListener top_dat = new View.OnClickListener() {
        public void onClick(View v) {
            Log.d("댓글수정하기", dat_idx);
            Log.d("수정하기", dada);
            if (dada.equals("top")) {
                webView.loadUrl("javascript:update_reply(" + dat_idx + ")");
                webView.loadUrl("javascript:show_update(" + dat_idx + ")");
                deleteDialog.dismiss();
            } else if (dada.equals("bottom")) {
                webView.loadUrl("javascript:update_reply(" + dat_idx + ")");
                webView.loadUrl("javascript:select_rereply(" + dat_idx + ")");
                webView.loadUrl("javascript:show_update_(" + dat_idx + ")");
                deleteDialog.dismiss();
            }
        }
    };

    private View.OnClickListener bottom_dat = new View.OnClickListener() {
        public void onClick(View v) {
            Log.d("댓글삭제", dat_idx);
            Log.d("삭제하기", dada);
            if (dada.equals("top")) {
                webView.loadUrl("javascript:del_reply(" + dat_idx + ")");
                deleteDialog.dismiss();
            } else if (dada.equals("bottom")) {
                webView.loadUrl("javascript:del_rereply(" + dat_idx + ")");
                deleteDialog.dismiss();
            }


        }
    };


    private class DownloadFileFromURL extends AsyncTask<String, String, String> {

        private ProgressDialog pDialog;

        private File createImageFile() throws IOException {
            File file = new File("Lighthouse", System.currentTimeMillis() + ".png");
            File storageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "Lighthouse" + file);

            // Save a file: path for use with ACTION_VIEW intents
            imgPath = storageDir.getAbsolutePath();

            return storageDir;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("Starting download");

            pDialog = new ProgressDialog(NewPageActivity.this);
            pDialog.setMessage("다운로드중입니다.");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        @Override
        protected String doInBackground(String... f_url) {

            int count;
            try {
//                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
//                    file = createImageFile();
//
//                } else {
                File dir = new File(Environment.getExternalStorageDirectory(), "Lighthouse");
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                file = new File(dir, System.currentTimeMillis() + ".png");
                Log.d("zzzzzzzzzzzzzz0000", file + "");

//                }

                System.out.println("Downloading");
                URL url = new URL(f_url[0]);

                URLConnection conection = url.openConnection();
                conection.connect();

                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                Log.d("zzzzzzzzzzzzzz11111", file + "");

                OutputStream output = new FileOutputStream(file);
                Log.d("zzzzzzzzzzzzzz222222", file + "");

                byte data[] = new byte[1024];

                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;

                    output.write(data, 0, count);

                }
                Log.d("zzzzzzzzzzzzzz3333", file + "");

                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));

                output.flush();

                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }


        @Override
        protected void onPostExecute(String file_url) {
            Log.d("zzzzzzzzzzzzzz444", file_url + "");
            System.out.println("Downloaded");
            pDialog.dismiss();
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.resumeTimers();
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.pauseTimers();
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        X_down = e1.getX();
        X_up = e2.getX();
        if (momo == 1) {

        } else {
            if (isSwiped == false) {
                isSwiped = true;
                if ((X_down - X_up) > 60) {
//                오른쪽에서 왼쪽으로
                    Log.e("guesture", "1");
                    webView.loadUrl("javascript:viewnext('next')");

                }
                if ((X_down - X_up) < -60) {
//                왼쪽에서 오른쪽으로
                    Log.e("guesture", "2");
                    webView.loadUrl("javascript:viewnext('prev')");

                }
            }
        }

        return true;
    }

    @Override
    public void onLongPress(MotionEvent e)
    {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        //        제스처 후 손이 떨어질 때
        Log.e("guesture", "fling");
        isSwiped = false;
        return false;
    }

    @Override
    public void onBackPressed() {
        Log.d("zzzzzz", "zzzzzzzzzz");
        if (rere == 1) {
            rere = 0;
            toptop.setVisibility(View.VISIBLE);
            fabview.setVisibility(View.GONE);
            webView.loadUrl(LHGlobal.BaseURL + url_in);
        } else if (k == 1) {
            k = 0;
            fabview.setVisibility(View.VISIBLE);
            spinlist_top.setVisibility(View.GONE);
            spinlist.setVisibility(View.GONE);
            search_white_delete.setVisibility(View.GONE);
        } else if (j == 1) {
            rere = 1;
            fabview.setVisibility(View.VISIBLE);
            spinlist_top.setVisibility(View.GONE);
            spinlist.setVisibility(View.GONE);
            search_white_delete.setVisibility(View.GONE);
            webView.loadUrl(LHGlobal.BaseURL + "/back/update/" + idx);
        } else {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("result", num);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }
}


