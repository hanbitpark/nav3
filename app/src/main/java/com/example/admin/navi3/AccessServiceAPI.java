package com.example.admin.navi3;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by admin on 2016-12-05.
 */

public class AccessServiceAPI {


    public String getJSONStringFromUrl_GET(String url){

        JSONArray jsonArray = null;
        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader;
        StringBuilder stringBuilder;
        String line;
        String jsonString = "";

        try{
            URL u = new URL(url);
            httpURLConnection = (HttpURLConnection)u.openConnection();
            httpURLConnection.setRequestMethod("GET");
            bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            stringBuilder = new StringBuilder();

            while((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line+'\n');
            }
            jsonString = stringBuilder.toString();

        }catch(MalformedURLException e){
            e.printStackTrace();
        }catch (ProtocolException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }finally {

            httpURLConnection.disconnect();
        }
        return jsonString;
    }

    public JSONObject convertJSONString2Obj(String jsonString){
        JSONObject jObj = null;

        try{

            Log.w("convertJSONString2Obj", "JsonString=" + jsonString);
            jObj = new JSONObject(jsonString);

        }catch(JSONException e){
            e.printStackTrace();
        }
        return jObj;
    }

    public String getJSONStringWithParam_POST(String serviceUrl, Map<String, String> params) throws IOException{
        JSONArray jsonArray = null;
        String jsonString = null;
        HttpURLConnection conn = null;
        String line;

        URL url;

        try{
            url = new URL(serviceUrl);
        }
        catch (MalformedURLException e){
            throw new IllegalArgumentException("invalid url: " + serviceUrl);
        }

        StringBuilder bodyBuilder = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();

        while(iterator.hasNext())
        {
            Map.Entry<String, String> param = iterator.next();
            bodyBuilder.append(param.getKey()).append('=').append(param.getValue());
            if(iterator.hasNext()){
                bodyBuilder.append('&');
            }
        }

        String body = bodyBuilder.toString();
        Log.w("getJSONStringWithParam", "param=>" + body);

        byte[] bytes = body.getBytes();

        try{
            conn = (HttpURLConnection)url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

            OutputStream out = conn.getOutputStream();
            out.write(bytes);
            out.close();

            int status = conn.getResponseCode();

            Log.w("getJSONStringWithParam", "Response Status = " + status);
            if(status != 200){
                throw new IOException("Post failed with error code" + status);
            }

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();

            while((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line + '\n');
            }

            jsonString = stringBuilder.toString();

        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (ProtocolException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }finally {

            conn.disconnect();
        }
        return jsonString;

    }

    public String getJSONStringWithParam_POST(String serviceUrl, String params) throws IOException{
        JSONArray jsonArray = null;
        String jsonString = null;
        HttpURLConnection conn = null;
        String line;

        URL url;
        try{
            url = new URL(serviceUrl);

        }catch(MalformedURLException e){

            throw new IllegalArgumentException("invalid url: " + serviceUrl);

        }

        Log.w("getJSONStringWithParam", "param=>" + params);
        byte[] bytes = params.getBytes();
        try{

            conn = (HttpURLConnection)url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.connect();

            OutputStream out = conn.getOutputStream();
            out.write(bytes);
            out.close();

            int status = conn.getResponseCode();

            Log.w("getJSONStringWithParam", "Response Status = " + status);
            if(status != 200){
                throw new IOException("Post failed with error code" + status);
            }

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();

            while((line = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(line + '\n');
            }

            jsonString = stringBuilder.toString();

        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }

        return jsonString;

    }


}
