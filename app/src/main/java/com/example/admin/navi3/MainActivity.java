package com.example.admin.navi3;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.File;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private WebView mWebView;
    private ContentLoadingProgressBar progressBar;
    private ProgressBar webbar;
    private ImageButton nav_imageButton;
    private DrawerLayout drawer;

    //접속자 ID
    private String id = null;
    private String cookie;
    private NavigationView navigationView;
    private CloseBackPressed_Handler closeBackPressed_handler;

    private String m_StrCookieData = null;
    private CookieSyncManager m_CookieSyncManager = null;
    private CookieManager m_CookieManager = null;

    boolean mIsTimerPaused = true;

    //웹뷰 상단 새로고침
    private android.support.v4.widget.SwipeRefreshLayout mSwipeRefresh;

    private ValueCallback<Uri> filePathCallbackNormal;
    private ValueCallback<Uri[]> filePathCallbackLollipop;
    private Uri mCapturedImageURI;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onStart() {
        CookieSyncManager.createInstance(this);
        super.onStart();
    }

    @Override
    protected void onResume() {

        CookieSyncManager.getInstance().startSync();

        if(mIsTimerPaused == false){
            mWebView.resumeTimers();
            mWebView.onResume();
            mIsTimerPaused = false;
        }
        super.onResume();

    }

    @Override
    protected void onPause() {

        CookieSyncManager.getInstance().stopSync();

        if(mIsTimerPaused == true){
            mWebView.pauseTimers();
            mWebView.onResume();
            mIsTimerPaused=true;
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mWebView.destroy();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().removeAllCookies(null);
        }

        super.onDestroy();

    }


    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //FCM TOKEN생성
        String token = FirebaseInstanceId.getInstance().getToken();

        //푸시알림전송시 토픽명 그룹 전체에 메세지를 발송할수 있다(이해x)
        FirebaseMessaging.getInstance().subscribeToTopic("notice");

        Log.d("newToken", token);
        new HttpURL().execute(token);

        //로그인 액티비티 종료
//        ((LoginActivity)LoginActivity.loginActivity).finish();
/////////////////////////////////////////////////////////////////////////
        //로그인 메세지
//        Bundle bundle = getIntent().getExtras();
//        id = bundle.getString("id");
//        cookie = bundle.getString("cookie");
//        Toast.makeText(getApplicationContext(), id + " 님 환영합니다.", Toast.LENGTH_SHORT).show();
/////////////////////////////////////////////////////////////////////////
        //툴바 액션 init
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
////////////////////////////////////////////////////////////////////////
        //네비게이션 init
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        //네이게이션 이미지 버튼 닫기
        nav_imageButton = (ImageButton)findViewById(R.id.nav_colse);

        //중간 progress바
        webbar = (ProgressBar)findViewById(R.id.web_progress);
        //상단 progress바
        progressBar = (ContentLoadingProgressBar)findViewById(R.id.progressBar);
        progressBar.setMax(100);

        //상단 새로고침
        mSwipeRefresh = (SwipeRefreshLayout)findViewById(R.id.webview_swipe_refresh);

        
////////////////////////////////////////////////////////////////////////
        //메세지에 지정한 주소로 이동
        String targetUrl = "http://ppuang.co.kr";
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            if(bundle.getString("url") != null && !bundle.getString("url").equalsIgnoreCase("")){
                targetUrl = bundle.getString("url");
                Log.d(TAG, "targetUrl : " +targetUrl);
            }
        }
///////////////////////////////////////////////////////////////////////
        mWebView = (WebView)findViewById(R.id.webView);
        webviewinit();

        if(id != null){
            mWebView.loadUrl("http://ppuang.co.kr?ss_id=" + id);
        }else{
            mWebView.loadUrl(targetUrl);
        }


        mWebView.setWebViewClient(new myWebViewClient());


        // Enable pinch to zoom without the zoom buttons
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            // Hide the zoom controls for HONEYCOMB+
            mWebView.getSettings().setDisplayZoomControls(false);
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            mWebView.getSettings().setTextZoom(100);
        mWebView.setWebChromeClient(new myWebChromeClient());

        //웹뷰 버튼 메세지 연동
        mWebView.addJavascriptInterface(new JavaScriptInterface(this), "Android");

        //웹뷰 버튼 로그인액티비티 연동
        mWebView.addJavascriptInterface(new JavaScriptInterface(this), "loginActivity");

        //Overlay scrollbar on top of Webcontents
        mWebView.setVerticalScrollbarOverlay(true);
        mWebView.setHorizontalScrollbarOverlay(true);

        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        //종료 closebackpressed handler
        closeBackPressed_handler = new CloseBackPressed_Handler(this);

        navigationView.setNavigationItemSelectedListener(this);

        nav_imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawers();
            }
        });

        mSwipeRefresh.setOnRefreshListener(new myOnRefreshListener(this));

    }


    //웹뷰초기화
    public void webviewinit(){

        //improve webview performance
        WebSettings webSettings  = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);

        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setSavePassword(true);
        webSettings.setSaveFormData(true);
        webSettings.setEnableSmoothTransition(true);
        webSettings.setJavaScriptEnabled(true);

        // Enable pinch to zoom without the zoom buttons
        webSettings.setBuiltInZoomControls(true);

        webSettings.setSupportZoom(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        webSettings.setGeolocationEnabled(true);

        //SUPPORT FLASH
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setSupportMultipleWindows(false);

        //HTML5 DOM
        webSettings.setDomStorageEnabled(true);

        //HTML5 Web DB
        webSettings.setDatabaseEnabled(true);
        webSettings.setDatabasePath("/data/data/"+ getApplicationContext().getPackageName()+"/database");

        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        //HTML5 App Cache
        webSettings.setAppCacheEnabled(true);
        webSettings.setAppCacheMaxSize(1024*1024*8);
        webSettings.setAppCachePath("/data/data/" + getApplicationContext().getPackageName()+"/cache");

    }

    private class myOnRefreshListener implements SwipeRefreshLayout.OnRefreshListener {

        private Context mContext;

        public myOnRefreshListener(Context applicationContext) {
            mContext = applicationContext;
        }

        @Override
        public void onRefresh() {
            mWebView.reload();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else if(mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            closeBackPressed_handler.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_login) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
//        else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class myWebViewClient extends WebViewClient{

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {


            //홈페이지 리로딩
            if (request.getUrl().getHost().equals("http://ppuang.co.kr?ss_id="+id)) {

                if(id != null){
                    mWebView.loadUrl("http://ppuang.co.kr?ss_id=" + id);
                }else{
                    mWebView.loadUrl("http://ppuang.co.kr");
                }

                return false;

            } else {

                Intent intent = new Intent(Intent.ACTION_VIEW, request.getUrl());
                startActivity(intent);
//
                 return true;
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            mSwipeRefresh.setRefreshing(false);

            super.onPageFinished(view, url);

        }
    }

    private class myWebChromeClient extends WebChromeClient {


        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);

            progressBar.setProgress(newProgress);
            if (newProgress <= 99) {
                progressBar.setVisibility(View.VISIBLE);
            } else if (newProgress == 100) {
                progressBar.setVisibility(View.GONE);
            }
        }




        @Override
        public void onCloseWindow(WebView w) {
            super.onCloseWindow(w);
            finish();
        }

        @Override
        public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, Message resultMsg) {
            final WebSettings settings = view.getSettings();
            settings.setDomStorageEnabled(true);
            settings.setJavaScriptEnabled(true);
            settings.setAllowFileAccess(true);
            settings.setAllowContentAccess(true);
            view.setWebChromeClient(this);
            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(view);
            resultMsg.sendToTarget();
            return false;
        }

        // For Android < 3.0
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            openFileChooser(uploadMsg, "");
        }

        // For Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            filePathCallbackNormal = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            startActivityForResult(Intent.createChooser(i, "File Chooser"), Common.FILECHOOSER_NORMAL_REQ_CODE);
        }

        // For Android 4.1+
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            openFileChooser(uploadMsg, acceptType);
        }


        // For Android 5.0+
        public boolean onShowFileChooser(
                WebView webView, ValueCallback<Uri[]> filePathCallback,
                WebChromeClient.FileChooserParams fileChooserParams) {
            if (filePathCallbackLollipop != null) {
//                    filePathCallbackLollipop.onReceiveValue(null);
                filePathCallbackLollipop = null;
            }
            filePathCallbackLollipop = filePathCallback;


            // Create AndroidExampleFolder at sdcard
            File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "AndroidExampleFolder");
            if (!imageStorageDir.exists()) {
                // Create AndroidExampleFolder at sdcard
                imageStorageDir.mkdirs();
            }

            // Create camera captured image file path and name
            File file = new File(imageStorageDir + File.separator + "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
            mCapturedImageURI = Uri.fromFile(file);

            Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);

            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");

            // Create file chooser intent
            Intent chooserIntent = Intent.createChooser(i, "작업선택");
            // Set camera intent to file chooser
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{captureIntent});

            // On select image call onActivityResult method of activity
            startActivityForResult(chooserIntent, Common.FILECHOOSER_LOLLIPOP_REQ_CODE);
            return true;
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Common.FILECHOOSER_NORMAL_REQ_CODE) {
            if (filePathCallbackNormal == null) return;
            Uri result = (data == null || resultCode != RESULT_OK) ? null : data.getData();
            filePathCallbackNormal.onReceiveValue(result);
            filePathCallbackNormal = null;
        } else if (requestCode == Common.FILECHOOSER_LOLLIPOP_REQ_CODE) {
            Uri[] result = new Uri[0];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                if(resultCode == RESULT_OK){
                    result = (data == null) ? new Uri[]{mCapturedImageURI} : WebChromeClient.FileChooserParams.parseResult(resultCode, data);
                }
                filePathCallbackLollipop.onReceiveValue(result);
            }
        }
    }



}