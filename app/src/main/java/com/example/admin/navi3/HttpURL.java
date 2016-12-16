package com.example.admin.navi3;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by admin on 2016-12-16.
 */

public class HttpURL extends AsyncTask<String, String, String> {

//    ProgressDialog loading;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d("preexecute", "preexecute실행");
//        loading = ProgressDialog.show(new MainActivity(), "Please Wait", null, true, true);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.d("postexecute", "postexecute실행");
        Log.d("내용", s);
//        loading.dismiss();
//                Toast.makeText(new MainActivity(),s,Toast.LENGTH_LONG).show();
    }

    @Override
    protected String doInBackground(String... params) {

        try{
            Log.d("실행 전 값확인", params[0]);

            String token = (String)params[0];
//                    String address = (String)params[1];

            String link="http://rukusmin.ppuang.co.kr/fcm/register.php";
            String data  = URLEncoder.encode("Token", "UTF-8") + "=" + URLEncoder.encode(token, "UTF-8");
//                    data += "&" + URLEncoder.encode("address", "UTF-8") + "=" + URLEncoder.encode(address, "UTF-8");

            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write( data );
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                sb.append(line);
                break;
            }

            Log.d("실행후 결과값", sb.toString());
            return sb.toString();
        }
        catch(Exception e){
            return new String("Exception: " + e.getMessage());
        }

    }

}
