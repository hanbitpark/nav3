package com.example.admin.navi3;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private Button ButtonLogin;
    private EditText EditId, EditPass;
    private ProgressBar progressBar;

    //로그인액티비티
    public static LoginActivity loginActivity;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginActivity = LoginActivity.this;

//        textView = (TextView)findViewById(R.id.btnRegistor);
//        textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this, Registor.class);
//                startActivity(intent);
//            }
//        });

//        builder = new AlertDialog.Builder(LoginActivity.this);

        ButtonLogin = (Button)findViewById(R.id.btnLogin);
        EditId = (EditText)findViewById(R.id.loginID);
        EditPass = (EditText)findViewById(R.id.loginPassword);



        ButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if("".equals(EditId.getText().toString())){
                    EditId.setError("이름을 입력하세요");
                    return;
                }
                if("".equals(EditPass.getText().toString())){
                    EditPass.setError("비밀번호를 입력하세요");
                    return;
                }

                // async task
                new TaskLogin().execute(EditId.getText().toString(), EditPass.getText().toString());

            }
        });



        progressBar = (ProgressBar)findViewById(R.id.progressBar_login);

    }

    // 로그인 실패했을때 기존 값 불러오기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            EditId.setText(data.getStringExtra("id"));
            EditPass.setText(data.getStringExtra("pass"));
        }
    }

    public class TaskLogin extends AsyncTask<String, Void, String>{

        private String id,pass;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //open progress dialog during login
            mProgressDialog = ProgressDialog.show(LoginActivity.this, "기다려주세요...", "진행중입니다...", true);
            id = EditId.getText().toString();
            pass = EditPass.getText().toString();

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                StringBuilder outputBuilder = new StringBuilder();

                URL url = new URL(Common.SERVICE_API_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=euc-kr");
//                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                //쿠키생성
                String getCookie = CookieManager.getInstance().getCookie(Common.SERVICE_API_URL.toString());
                if(getCookie != null){
                    Log.i("ddddddddddddddd", getCookie);
                    conn.setRequestProperty("Cookie", getCookie);
                }

//                String setCookie = conn.getHeaderField("Set-Cookie");
//                if(setCookie != null){
////                    Log.i("getHeaderField", setCookie);
//                    CookieManager.getInstance().setCookie(Common.SERVICE_API_URL.toString(), setCookie);
//                }

                StringBuffer sb = new StringBuffer();
                sb.append("id=").append(id).append("&");
                sb.append("pass=").append(pass);

                PrintWriter pw  = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "euc-kr"));
                pw.write(sb.toString());
                pw.flush();

//                OutputStream os = conn.getOutputStream();
//                os.write(sb.getChars("euc-kr"););

                //스트림 버퍼 비우기
//                os.flush();
                // 스트림 닫기기
//               os.close();

                int resCode = conn.getResponseCode();
                if(resCode == conn.HTTP_OK){
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "EUC-KR"));

                    String line = null;
                    while(true){
                        line = reader.readLine();
                        if(line == null){
                            break;
                        }

                        outputBuilder.append(line + "\n");
                    }

                    pw.close();
                    reader.close();
                    conn.disconnect();

                }

                String output = outputBuilder.toString();


                return output;

            }catch(Exception e){
                return null;
            }

        }

        @Override
        protected void onPostExecute(String json) {

            super.onPostExecute(json);
            mProgressDialog.dismiss();

            try {
                JSONArray jsonArray = new JSONArray(json);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                String result = jsonObject.getString("result");
                String jsonId = jsonObject.getString("id");

                Log.i("bbbbbbbbbbbbbbbbb", result);
                Log.i("ccccccccccccccccc", jsonId);

                if(result.equals("login_success"))
                {
//                Toast.makeText(getApplicationContext(), json, Toast.LENGTH_LONG).show();
//                Log.i("aaaaaaaaaaaaaaaaa", json);
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    i.putExtra("id", EditId.getText().toString());
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Login Fail", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }



        }
    }

}
