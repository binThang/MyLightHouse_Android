package com.ghsoft.android.lighthouse;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MainAdapter.AdapterCallback, GestureDetector.OnGestureListener,
        View.OnTouchListener
{

    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.menu)
    ImageView menu;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.search)
    ImageView search;
    @BindView(R.id.mainview)
    LinearLayout mainview;
    @BindView(R.id.search_ok)
    LinearLayout search_ok;
    @BindView(R.id.fabview)
    LinearLayout fabview;
    @BindView(R.id.spin)
    LinearLayout spin;
    @BindView(R.id.spinlist)
    LinearLayout spinlist;
    @BindView(R.id.spinlist_top)
    LinearLayout spinlist_top;
    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.fab_text)
    TextView fab_text;
    @BindView(R.id.spin_text)
    TextView spin_text;
    @BindView(R.id.nickname)
    TextView nickname;
    @BindView(R.id.cate1_img)
    TextView cate1_img;
//    @BindView(R.id.cate2_img)
//    TextView cate2_img;
//    @BindView(R.id.cate3_img)
//    TextView cate3_img;
//    @BindView(R.id.cate4_img)
//    TextView cate4_img;
    @BindView(R.id.search_delete)
    ImageView search_delete;
    @BindView(R.id.search_edit)
    EditText search_edit;
    @BindView(R.id.search_white_delete)
    LinearLayout search_white_delete;
    @BindView(R.id.li_s)
    LinearLayout li_s;
    @BindView(R.id.back_s)
    ImageView back_s;
    @BindView(R.id.text_s)
    TextView text_s;


    private MainAdapter mAdapter;
    AQuery aQuery;
    List<MainData> data = new ArrayList<>();
    String token = "";
    Handler handler = new Handler();

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    //http://ec2-13-124-110-195.ap-northeast-2.compute.amazonaws.com/fun/list2/1
    String url = LHGlobal.BaseURL + "/back/list/1/1";
    ProgressDialog pdLoading;

    ActionBarDrawerToggle toggle;
    WebView childView;
    private long backKeyPressedTime = 0;

    int k = 0, j = 0;
    String writestr = "";

    String board = "1";
    String search_str = "no";

    private static final int RC_FILE_CHOOSE = 2833;
    private ValueCallback<Uri> mUploadMsg = null;

    Context context;

    private GestureDetectorCompat gestureDetector;
    float X_down, X_up;
    boolean isSwiped;

    public static MainActivity activity;

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent)
    {
        gestureDetector.onTouchEvent(motionEvent);
        return view.onTouchEvent(motionEvent);
    }

    private class AndroidBridge
    {
        //리스트클릭했을떄
        @JavascriptInterface
        public void newpage(final String url, final String name, final String num)
        {
            handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    Intent intent = new Intent(MainActivity.this, NewPageActivity.class);
                    intent.putExtra("url", url);
                    intent.putExtra("num", num);
                    if (search_str.equals("no"))
                    {
                        intent.putExtra("name", name);

                    }
                    else
                    {
                        intent.putExtra("name", search_str);

                    }
                    startActivityForResult(intent, 1);
                }
            });
        }

        @JavascriptInterface
        public void out()
        {
            handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    editor.clear();
                    editor.commit();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();

                    Log.d("zzzzzzzzzzzz", "outoutoutoutoutoutout");
                }
            });

        }

        //글쓰기 배경 바꾸기
        @JavascriptInterface
        public void sajin()
        {
            handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    j = 1;
                    Log.d("들어옴", "sajin");
                    fabview.setVisibility(View.GONE);
                    spinlist_top.setVisibility(View.GONE);
                    search_white_delete.setVisibility(View.VISIBLE);
                }
            });

        }

        //검색 후 상단바 바꾸는 부분
        @JavascriptInterface
        public void sname(final String str)
        {
            handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    Log.d("들어옴", "sname");
                    Log.d("들어옴", str);
                    search_str = str;
                    search_edit.setText(null);
                    li_s.setVisibility(View.VISIBLE);
                    fabview.setVisibility(View.GONE);
                    search_white_delete.setVisibility(View.GONE);
                    search_ok.setVisibility(View.GONE);
                    text_s.setText(str);
                }
            });

        }


        //글쓰기 눌렀을때
        @JavascriptInterface
        public void writea(final String str)
        {
            handler.post(new Runnable()
            {
                @SuppressLint("RestrictedApi")
                @Override
                public void run()
                {
                    Log.d("들어옴", "write");
                    Log.d("들어옴", str);
                    if (str.equals("back"))
                    {
                        k = 0;
                        fab.setVisibility(View.GONE);
                        mainview.setVisibility(View.GONE);
                        fabview.setVisibility(View.VISIBLE);
                    }
                    else if (str.equals("tony"))
                    {
                        k = 0;
                        fab.setVisibility(View.GONE);
                        mainview.setVisibility(View.GONE);
                        fabview.setVisibility(View.VISIBLE);
                    }

                }
            });

        }

        //메인
        @JavascriptInterface
        public void funlist(final String str)
        {
            handler.post(new Runnable()
            {
                @SuppressLint("RestrictedApi")
                @Override
                public void run()
                {
                    Log.d("들어옴", "funlist");
                    Log.d("들어옴", str + "");
                    search_str = "no";
                    search_edit.setText(null);
                    writestr = str;

                    // 내 등에 기대 1
                    if (str.equals("1"))
                    {
                        cate1_img.setText("내 등에 기대");
//                        cate2_img.setVisibility(View.GONE);
//                        cate3_img.setVisibility(View.GONE);
//                        cate4_img.setVisibility(View.GONE);
                        fab.setVisibility(View.VISIBLE);
                        search_ok.setVisibility(View.GONE);
                        mainview.setVisibility(View.VISIBLE);
                        search.setVisibility(View.VISIBLE);
                    }
                    // 나도 그래 2
                    else if (str.equals("2"))
                    {
                        cate1_img.setText("나도 그래");
//                        cate2_img.setVisibility(View.GONE);
//                        cate3_img.setVisibility(View.GONE);
//                        cate4_img.setVisibility(View.VISIBLE);
                        fab.setVisibility(View.GONE);
                        search_ok.setVisibility(View.GONE);
                        mainview.setVisibility(View.VISIBLE);
                        search.setVisibility(View.VISIBLE);
                    }
                    // 뻔해도 좋은 3
                    else if (str.equals("3"))
                    {
                        cate1_img.setText("뻔해도 좋은");
////                        cate2_img.setVisibility(View.VISIBLE);
//                        cate3_img.setVisibility(View.GONE);
//                        cate4_img.setVisibility(View.GONE);
                        fab.setVisibility(View.GONE);
                        search_ok.setVisibility(View.GONE);
                        mainview.setVisibility(View.VISIBLE);
                        search.setVisibility(View.INVISIBLE);
                    }
                    // 나의 다이어리 4
                    else if (str.equals("4"))
                    {
                        cate1_img.setText("나의 다이어리");
////                        cate2_img.setVisibility(View.GONE);
//                        cate3_img.setVisibility(View.VISIBLE);
//                        cate4_img.setVisibility(View.GONE);
                        fab.setVisibility(View.GONE);
                        search_ok.setVisibility(View.GONE);
                        mainview.setVisibility(View.VISIBLE);
                        search.setVisibility(View.INVISIBLE);
                    }
                }
            });

        }

        //검색눌렀을때
        @JavascriptInterface
        public void searcha()
        {
            handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    Log.d("들어옴", "search");
                    fab.setVisibility(View.GONE);
                    mainview.setVisibility(View.GONE);
                    search_ok.setVisibility(View.VISIBLE);
                }
            });

        }

        //글쓰기 사진 클릭했을때
        @JavascriptInterface
        public void selectpic()
        {
            handler.post(new Runnable()
            {
                @SuppressLint("RestrictedApi")
                @Override
                public void run()
                {
                    if (writestr.equals("1"))
                    {
                        j = 0;
                        k = 0;
                        Log.d("들어옴", "selectpic");
                        fab.setVisibility(View.GONE);
                        mainview.setVisibility(View.GONE);
                        fabview.setVisibility(View.VISIBLE);
                    }
                    else if (writestr.equals("2"))
                    {
                        j = 0;
                        k = 0;
                        Log.d("들어옴", "selectpic");
                        fab.setVisibility(View.GONE);
                        mainview.setVisibility(View.GONE);
                        fabview.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        token = FirebaseInstanceId.getInstance().getToken();
        aQuery = new AQuery(this);
        pref = getSharedPreferences("member", MODE_PRIVATE);
        editor = pref.edit();
        MarketThread.start();
        context = MainActivity.this;
        activity = MainActivity.this;

        String url_singo = LHGlobal.BaseURL + "/member/singo";
        aQuery.ajax(url_singo, String.class, new AjaxCallback<String>()
        {
            @Override
            public void callback(String url, String object, AjaxStatus status)
            {
                Log.d("신고_은진씨", object);
                try
                {
                    JSONObject jsonObject = new JSONObject(object);

                    String re = jsonObject.getString("result");
                    String te = jsonObject.getString("text");
                    if (re.equals("false"))
                    {
                        AlertDialog.Builder adad = new AlertDialog.Builder(MainActivity.this);
                        adad.setMessage("아래와 같은 이유로 사용하실 수 없습니다.\n\n" + te);
                        adad.setCancelable(false);
                        adad.setPositiveButton("확인", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                finish();
                            }
                        });
                        adad.show();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }.header("User-Agent", "gh_mobile{" + token + "}"));

        gestureDetector = new GestureDetectorCompat(this, this);
        Intent intent_push = getIntent();
        String str = intent_push.getExtras().getString("pushlink");
        String idxxxx = intent_push.getExtras().getString("pushidx");

        if (Intent.ACTION_VIEW.equals(intent_push.getAction()))
        {
            Uri uri = intent_push.getData();
            String temp_post_id = uri.getQueryParameter("post_id");
            Intent intent = new Intent(MainActivity.this, NewPageActivity.class);
            intent.putExtra("url", str);
            intent.putExtra("name", "글");
            intent.putExtra("num", temp_post_id);
            startActivityForResult(intent, 1);
        }
        else if (!str.equals("") && !str.equals("/back/list/1/1"))
        {
            Intent intent = new Intent(MainActivity.this, NewPageActivity.class);
            intent.putExtra("url", str);
            intent.putExtra("name", "글");
            intent.putExtra("num", idxxxx);
            startActivityForResult(intent, 1);
//        } else if (str.equals("/back/list/1/1")) {
//            Intent intent = new Intent(MainActivity.this, NoticeActivity.class);
//            startActivity(intent);
        }


        k = 0;
        j = 0;
        pdLoading = new ProgressDialog(this);


        String url = LHGlobal.BaseURL + "/member/selectInfo";
        final Map<String, Object> params = new HashMap<>();
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

                    String nick = jsonObject.getString("nickname");
                    nickname.setText(nick);

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
        }.header("User-Agent", "gh_mobile{}"));

        setWebView();
        setContent();
    }

    public void onMethodCallback(final int pos, String name, String idx)
    {
        k = 0;
        fabview.setVisibility(View.VISIBLE);
        spinlist_top.setVisibility(View.GONE);
        spinlist.setVisibility(View.GONE);
        fab_text.setText(name);
        spin_text.setText(name);

        webView.loadUrl("javascript:select_hashtag(" + idx + ")");

        board = idx;
    }

    public void setData()
    {
        String url_num = LHGlobal.BaseURL + "/back/select_category";
        aQuery.ajax(url_num, String.class, new AjaxCallback<String>()
        {
            @Override
            public void callback(String url, String object, AjaxStatus status)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(object);

                    JSONArray caidxArr = new JSONArray(jsonObject.getString("category_idx"));
                    JSONArray caArr = new JSONArray(jsonObject.getString("category"));
                    JSONArray colorArr = new JSONArray(jsonObject.getString("color"));
                    for (int i = 0; caArr.length() > i; i++)
                    {
                        data.add(new MainData(caArr.getString(i), colorArr.getBoolean(i), caidxArr.getString(i)));
                    }
                    mAdapter = new MainAdapter(MainActivity.this, data);
                    list.setAdapter(mAdapter);
                    list.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

        }.header("User-Agent", "gh_mobile{}"));
    }

    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    public void setWebView()
    {
        webView.getSettings().setTextZoom(100);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setSupportMultipleWindows(true);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);// 웹뷰 성능 개선
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.setOnTouchListener(this);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        if (18 < Build.VERSION.SDK_INT)
        {
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        }

        File dir = getCacheDir();
        if (!dir.exists())
        {
            dir.mkdirs();
        }
        webView.getSettings().setAppCachePath(dir.getPath());
        webView.getSettings().setAppCacheEnabled(true);

        webView.addJavascriptInterface(new AndroidBridge(), "lighthouse");

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            cookieManager.setAcceptThirdPartyCookies(webView, true); // false 설정 시 오류 발생
        }

        String userAgent = new WebView(getBaseContext()).getSettings().getUserAgentString();
        webView.setWebContentsDebuggingEnabled(true);
        webView.getSettings().setUserAgentString(userAgent + " gh_mobile{" + token + "}");
        webView.loadUrl(url);

        Log.d("메인토큰", token);

        webView.setWebViewClient(new MyWC());
        webView.setWebChromeClient(new WebChromeClient()
        {
            private View mCustomView;
            private int mOriginalOrientation;
            private FullscreenHolder mFullscreenContainer;
            private CustomViewCallback mCustomViewCollback;

            @Override
            public void onCloseWindow(WebView window)
            {
                window.setVisibility(View.GONE);
                window.goBack();
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(message)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
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
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
            public void onShowCustomView(View view, CustomViewCallback callback)
            {

                if (mCustomView != null)
                {
                    callback.onCustomViewHidden();
                    return;
                }

                mOriginalOrientation = MainActivity.this.getRequestedOrientation();

                FrameLayout decor = (FrameLayout) MainActivity.this.getWindow().getDecorView();

                mFullscreenContainer = new FullscreenHolder(MainActivity.this);
                mFullscreenContainer.addView(view, ViewGroup.LayoutParams.MATCH_PARENT);
                decor.addView(mFullscreenContainer, ViewGroup.LayoutParams.MATCH_PARENT);
                mCustomView = view;
                mCustomViewCollback = callback;
                MainActivity.this.setRequestedOrientation(mOriginalOrientation);

            }

            @Override
            public void onHideCustomView()
            {
                if (mCustomView == null)
                {
                    return;
                }

                FrameLayout decor = (FrameLayout) MainActivity.this.getWindow().getDecorView();
                decor.removeView(mFullscreenContainer);
                mFullscreenContainer = null;
                mCustomView = null;
                mCustomViewCollback.onCustomViewHidden();

                MainActivity.this.setRequestedOrientation(mOriginalOrientation);
            }


            class FullscreenHolder extends FrameLayout
            {

                public FullscreenHolder(Context ctx)
                {
                    super(ctx);
                    setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                }

                @Override
                public boolean onTouchEvent(MotionEvent evt)
                {
                    return true;
                }
            }


        });

        toggle = new ActionBarDrawerToggle(MainActivity.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(MainActivity.this);
    }

    private class MyWC extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url_s, Bitmap favicon)
        {
            super.onPageStarted(view, url_s, favicon);
            Log.d("로그인_로그인_로그인_시작", url);
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();
            if (url_s.equals(url))
            {
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }
            else
            {
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            if (url.startsWith("tel:"))
            {
                Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                startActivity(i);
                return true;
            }

            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url)
        {
            super.onPageFinished(view, url);
            try
            {
                pdLoading.dismiss();
            } catch (Exception e)
            {
                pdLoading.dismiss();

            }
            Log.d("로그인_로그인_로그인_끝", url);

        }

        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload)
        {
            super.doUpdateVisitedHistory(view, url, isReload);
            Log.d("로그인_로그인_로그인_히스토리", url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
        {
            super.onReceivedError(view, errorCode, description, failingUrl);
            Log.d("로그인_로그인_로그인_오류1", errorCode + "");
            Log.d("로그인_로그인_로그인_오류2", description + "");
            Log.d("로그인_로그인_로그인_오류3", failingUrl + "");

        }
    }

    public void setContent()
    {
        search_edit.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

                if (charSequence.length() == 0)
                {
                    Log.d("검색하는중", "000000");
                    webView.loadUrl("javascript:nosearch()");
                    search_delete.setVisibility(View.GONE);

                }
                else if (charSequence.length() >= 1)
                {
                    webView.loadUrl("javascript:search(" + "'" + charSequence.toString() + "'" + ")");
                    Log.d("검색하는중", charSequence.toString());
                    search_delete.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });
    }

    @OnClick({R.id.menu, R.id.cate1, R.id.cate3, R.id.cate4, R.id.cate5, R.id.search,
            R.id.search_back, R.id.fab, R.id.fab_back, R.id.spin, R.id.del,
            R.id.search_delete, R.id.white_delete, R.id.save, R.id.back_s})
    void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.menu:
                drawer.openDrawer(Gravity.LEFT);
                break;

            case R.id.cate1:
                webView.loadUrl(url);
                drawer.closeDrawer(GravityCompat.START);
                break;

//            case R.id.cate2:
//                webView.loadUrl(LHGlobal.BaseURL + "/metoo/list/1/1");
//                break;

            case R.id.cate3:
                webView.loadUrl(LHGlobal.BaseURL + "/fun/list2/1");
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.cate4:
                webView.loadUrl(LHGlobal.BaseURL + "/diary/list/1");
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.cate5:
                Intent intent = new Intent(MainActivity.this, SetActivity.class);
                startActivity(intent);
                break;

            case R.id.save:
                Log.d("토니글쓰기", board);
                webView.loadUrl("javascript:insert_board(" + board + ")");
                break;

            case R.id.search:
                webView.loadUrl(LHGlobal.BaseURL + "/search");
                break;

            case R.id.search_back:
                webView.loadUrl(url);
                break;

            case R.id.fab:
                if (writestr.equals("1"))
                {
                    data = new ArrayList<>();
                    setData();
                    webView.loadUrl(LHGlobal.BaseURL + "/back/write");
                    fab_text.setText("친구");
                    spin_text.setText("친구");
                }
                else if (writestr.equals("2"))
                {
                    data = new ArrayList<>();
                    setData();
                    webView.loadUrl(LHGlobal.BaseURL + "/back/tony");
                    fab_text.setText("친구");
                    spin_text.setText("친구");
                }

                break;

            case R.id.fab_back:
                webView.loadUrl(url);
                break;

            case R.id.back_s:
                webView.loadUrl(LHGlobal.BaseURL + "/search");
                break;

            case R.id.spin:
                k = 1;
                fabview.setVisibility(View.GONE);
                spinlist_top.setVisibility(View.VISIBLE);
                spinlist.setVisibility(View.VISIBLE);
                break;


            case R.id.del:
                k = 0;
                fabview.setVisibility(View.VISIBLE);
                spinlist_top.setVisibility(View.GONE);
                spinlist.setVisibility(View.GONE);
                break;

            case R.id.search_delete:
                search_edit.setText(null);
                break;

            case R.id.white_delete:
                if (writestr.equals("1"))
                {
                    webView.loadUrl(LHGlobal.BaseURL + "/back/write");
                }
                else if (writestr.equals("2"))
                {
                    webView.loadUrl(LHGlobal.BaseURL + "/back/tony");
                }
                break;

        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        return false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed()
    {
        if (k == 1)
        {
            k = 0;
            fabview.setVisibility(View.VISIBLE);
            spinlist_top.setVisibility(View.GONE);
            spinlist.setVisibility(View.GONE);
        }
        else if (j == 1)
        {
            if (writestr.equals("1"))
            {
                j = 0;
                webView.loadUrl(LHGlobal.BaseURL + "/back/write");
            }
            else if (writestr.equals("2"))
            {
                j = 0;
                webView.loadUrl(LHGlobal.BaseURL + "/back/tony");
                //
            }

        }
        else
        {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START))
            {
                drawer.closeDrawer(GravityCompat.START);

            }
            else
            {
                if (childView != null)
                {
                    if (childView.isShown())
                    {
                        childView.setVisibility(View.GONE);
                        childView.removeView(childView);
                        childView = null;
                    }

                }
                else
                {
                    String url_now = webView.getUrl();

                    if (url_now.equals(url) || url_now.equals(LHGlobal.BaseURL + "/back/list/1/2") || url_now.equals(LHGlobal.BaseURL + "/back/list/1/3")
                            || url_now.equals(LHGlobal.BaseURL + "/back/list/2/1") || url_now.equals(LHGlobal.BaseURL + "/back/list/2/2") || url_now.equals(LHGlobal.BaseURL + "/back/list/2/3")
                            || url_now.equals(LHGlobal.BaseURL + "/fun/list2/1") || url_now.equals(LHGlobal.BaseURL + "/fun/list2/2") || url_now.equals(LHGlobal.BaseURL + "/fun/list2/3")
                            || url_now.equals(LHGlobal.BaseURL + "/diary/list/1") || url_now.equals(LHGlobal.BaseURL + "/diary/list/2") || url_now.equals(LHGlobal.BaseURL + "/diary/list/3"))
                    {

                        if (System.currentTimeMillis() > backKeyPressedTime + 2000)
                        {
                            backKeyPressedTime = System.currentTimeMillis();
                            return;
                        }
                        else if (System.currentTimeMillis() <= backKeyPressedTime + 2000)
                        {
                            setResult(999);
                            FinishApp();
                        }

                    }
                    else
                    {
                        if (webView.canGoBack())
                        {
                            webView.goBack();
                        }
                        else
                        {
                            if (System.currentTimeMillis() > backKeyPressedTime + 2000)
                            {
                                backKeyPressedTime = System.currentTimeMillis();
                                return;
                            }
                            else if (System.currentTimeMillis() <= backKeyPressedTime + 2000)
                            {
                                setResult(999);
                                FinishApp();
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        webView.resumeTimers();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        webView.pauseTimers();
    }


    @Override
    public boolean onDown(MotionEvent motionEvent)
    {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent)
    {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent)
    {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
    {
        X_down = e1.getX();
        X_up = e2.getX();
        if (isSwiped == false)
        {
            isSwiped = true;
            if ((X_down - X_up) > 60)
            {
//                오른쪽에서 왼쪽으로
                Log.e("guesture", "1");
//                Log.e("fffffffff1111", X_down - X_up + "");
                webView.loadUrl("javascript:next()");

            }
            if ((X_down - X_up) < -60)
            {
//                왼쪽에서 오른쪽으로
                Log.e("guesture", "2");
//                Log.e("fffffffff22222", X_down - X_up + "");
                webView.loadUrl("javascript:prev()");

            }
        }
        return true;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent)
    {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1)
    {
        //        제스처 후 손이 떨어질 때
        Log.e("guesture", "fling");
        isSwiped = false;
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        if (requestCode == 1)
        {
            if (resultCode == RESULT_OK)
            {
                String result = data.getStringExtra("result");
                String type = data.getStringExtra("type");
                if (result.equals("mymym"))
                {
                    Log.d("수정하기_돌아가기", type);

                    if (type.equals("0"))
                    {
                        webView.loadUrl(LHGlobal.BaseURL + "/back/list/1/1");
                    }
                    else if (type.equals("1"))
                    {
                        webView.loadUrl(LHGlobal.BaseURL + "/back/list/2/1");
                    }
                }
                else
                {
                    Log.d("수정하기_돌아가기_me", result);
                    webView.loadUrl("javascript:reload(" + result + ")");
                    webView.loadUrl("javascript:reload_update(" + result + ")");
//                    webView.loadUrl("javascript:reload_list()");
                }
            }
            if (requestCode == RC_FILE_CHOOSE && mUploadMsg != null)
            {
                Uri result = null;
                if (data != null || resultCode == RESULT_OK)
                {
                    result = data.getData();
                }
                mUploadMsg.onReceiveValue(result);
                mUploadMsg = null;
            }
        }
    }

    Thread MarketThread = new Thread(new Runnable()
    {
        @Override
        public void run()
        {
            String store_version = MarketVersionChecker.getMarketVersion(getPackageName());
            String device_version = null;

            try
            {
                device_version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;

                Log.d("버전_마켓", store_version);
                Log.d("버전_디바이스", device_version);

                final int i = ((int) (Float.parseFloat(store_version)) - ((int) Float.parseFloat(device_version)));
                Log.e("버전_i", i + "");
                handler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if (i > 0)
                        {
                            Log.e("버전", "업데이트필요");
                            // 업데이트 필요
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setMessage("새로운 업데이트가 나왔습니다.\n업데이트 하시겠습니까?")
                                    .setPositiveButton("업데이트", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i)
                                        {

                                            Intent intent = new Intent(Intent.ACTION_VIEW);
                                            intent.setData(Uri.parse("market://details?id=" + getPackageName()));
                                            startActivity(intent);
                                        }
                                    })
                                    .setNegativeButton("취소", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i)
                                        {
                                            dialogInterface.dismiss();
                                            finish();
                                        }
                                    })
                                    .setCancelable(false)
                                    .create()
                                    .show();

                        }
                        else
                        {
                            // 업데이트 불필요
                            Log.e("버전", "업데이트불필요");
                        }
                    }
                });
            } catch (PackageManager.NameNotFoundException e)
            {
                Log.e("버전", "오류1");
                Log.e("버전", e + "");
                e.printStackTrace();
            } catch (Exception e)
            {
                Log.e("버전", "오류2");
                Log.e("버전", e + "");
            }
        }
    });

    private void FinishApp()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("종료하시겠습니까?")
                .setPositiveButton("종료", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        finish();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        dialogInterface.cancel();
                    }
                })
                .create()
                .show();
    }
}
