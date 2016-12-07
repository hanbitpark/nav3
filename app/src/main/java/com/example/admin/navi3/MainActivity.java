package com.example.admin.navi3;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.ContentLoadingProgressBar;
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
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private WebView mWebView;
    private ContentLoadingProgressBar progressBar;
    private ProgressBar webbar;
    private ImageButton nav_imageButton;
    private DrawerLayout drawer;

    //접속자 ID
    private String id;
    private String cookie;
    private NavigationView navigationView;
    private CloseBackPressed_Handler closeBackPressed_handler;

    private String m_StrCookieData = null;
    private CookieSyncManager m_CookieSyncManager = null;
    private CookieManager m_CookieManager = null;

    boolean mIsTimerPaused = true;

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
        super.onDestroy();

    }

//    public void setSyncCookie() {
//        Log.e("surosuro", "token transfer start ---------------------------");
//        try {
//            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
//            nameValuePairs.add(new BasicNameValuePair("token", "TEST");// 넘길 파라메터 값셋팅token=TEST
//
//            HttpParams params = new BasicHttpParams();
//
//            HttpPost post = new HttpPost(domain+/androidToken.jsp");
//            post.setParams(params);
//            HttpResponse response = null;
//            BasicResponseHandler myHandler = new BasicResponseHandler();
//            String endResult = null;
//
//            try {
//                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//
//            try {
//                response = httpclient.execute(post);
//            } catch (ClientProtocolException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            try {
//                endResult = myHandler.handleResponse(response);
//            } catch (HttpResponseException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            List<Cookie> cookies = ((DefaultHttpClient)httpclient).getCookieStore().getCookies();
//
//            if (!cookies.isEmpty()) {
//                for (int i = 0; i < cookies.size(); i++) {
//                    // cookie = cookies.get(i);
//                    String cookieString = cookies.get(i).getName() + "="
//                            + cookies.get(i).getValue();
//                    Log.e("surosuro", cookieString);
//                    cookieManager.setCookie(domain, cookieString);
//                }
//            }
//            Thread.sleep(500);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }



    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        progressBar.setMax(100);


        mWebView.loadUrl("http://ppuang.co.kr?ss_id=" + id);


        mWebView.setWebViewClient(new myWebViewClient());
        mWebView.setWebChromeClient(new myWebChromeClient());

        //웹뷰 버튼 메세지 연동
        mWebView.addJavascriptInterface(new JavaScriptInterface(this), "Android");

        //웹뷰 버튼 로그인액티비티 연동
        mWebView.addJavascriptInterface(new JavaScriptInterface(this), "loginActivity");

        navigationView.setNavigationItemSelectedListener(this);

        nav_imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawers();
            }
        });

    }



    //초기화
    public void init(){

        //로그인 액티비티 종료
        ((LoginActivity)LoginActivity.loginActivity).finish();

        //로그인 메세지
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        cookie = bundle.getString("cookie");
        Toast.makeText(getApplicationContext(), id + " 님 환영합니다.", Toast.LENGTH_SHORT).show();

        //툴바 액션 init
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        //네비게이션 init
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        //네이게이션 이미지 버튼 닫기
        nav_imageButton = (ImageButton)findViewById(R.id.nav_colse);

        //상단 progress바
        progressBar = (ContentLoadingProgressBar) findViewById(R.id.progressBar);

        //중간 progress바
        webbar = (ProgressBar)findViewById(R.id.web_progress);

        mWebView = (WebView)findViewById(R.id.webView);

        //improve webview performance
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);

        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setSavePassword(true);
        webSettings.setSaveFormData(true);
        webSettings.setEnableSmoothTransition(true);
        webSettings.setJavaScriptEnabled(true);
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

        //Overlay scrollbar on top of Webcontents
        mWebView.setVerticalScrollbarOverlay(true);
        mWebView.setHorizontalScrollbarOverlay(true);

        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        //종료 closebackpressed handler
        closeBackPressed_handler = new CloseBackPressed_Handler(this);

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

        if (id == R.id.nav_registor) {
            // Handle the camera action
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


//            if (request.getUrl().getHost().equals("http://ppuang.co.kr?ss_id="+id)) {
//
//                return false;
//
//            } else {
//
//                Intent intent = new Intent(Intent.ACTION_VIEW, request.getUrl());
//                startActivity(intent);
//
                return true;
//            }
        }

        //특정 웹페이지 이동시
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {



//            if(URLUtil.isValidUrl(url) == false){ //URI is not loading is webview
//                Uri uri = Uri.parse(url);
//                if(uri.getScheme().equals("sms") || uri.getScheme().equals("mailto")){
//                    Intent intent = new Intent(Intent.ACTION_SEND);
//                    intent.setData(uri);
//                    view.getContext().startActivity(intent);
//                }else{
//                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                    intent.setData(uri);
//                    view.getContext().startActivity(intent);
//                }
//                return true;
//            } else{
//                Uri uri = Uri.parse(url);
//                if(uri.getHost().equals("play.google.com") == true && uri.getPath().equals("store/apps/details")){
//                    Intent marketIntent = new Intent(Intent.ACTION_VIEW);
//                    marketIntent.setData(Uri.parse("market://deails?id=" + uri.getQueryParameter("id")));
//                    view.getContext().startActivity(marketIntent);
//                    return true;
//                }
//            }

//            view.loadUrl(url);
//            return true;
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

//            CookieSyncManager.getInstance().sync();

            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            CookieSyncManager.getInstance().sync();
            super.onPageFinished(view, url);
        }
    }

    private class myWebChromeClient extends WebChromeClient{

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);

            progressBar.setProgress(newProgress);
            if (newProgress <= 99) {
                progressBar.setVisibility(View.VISIBLE);
            } else if(newProgress == 100){
                progressBar.setVisibility(View.GONE);
            }
        }
    }

    public class avaScriptInterface{
        private Context mContext;

    }



}
