package com.example.admin.navi3;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Registor extends AppCompatActivity {

    Button btnReg;
    EditText Id,Pass,ConPass;
    String id,pass,conpass;
    AlertDialog.Builder builder;
    String reg_url = "http://rukusmin.ppuang.co.kr/login/register.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registor);

//        btnReg = (Button)findViewById(R.id.btnRegistor);
//
//        Id = (EditText)findViewById(R.id.regID);
//        Pass = (EditText)findViewById(R.id.regPassword);
//        ConPass = (EditText)findViewById(R.id.confirmPassword);
//
//        builder = new AlertDialog.Builder(this);
//
//        btnReg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                id = Id.getText().toString();
//                pass = Pass.getText().toString();
//                conpass = ConPass.getText().toString();
//
//                }
//        });
//
//    }
//
//    public void displayAlert(final String code){
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if(code.equals("input_error")){
//                    Pass.setText("");
//                    ConPass.setText("");
//                }
//                else if(code.equals("reg_success")){
//                    finish();
//                }
//                else if(code.equals("reg_failed")){
//                    Id.setText("");
//                    Pass.setText("");
//                }
//            }
//        });
//
//        AlertDialog alertDialog = builder.create();
//        alertDialog.show();
//
    }
}
