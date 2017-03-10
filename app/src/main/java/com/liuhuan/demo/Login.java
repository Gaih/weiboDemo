package com.liuhuan.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/3/10.
 */

public class Login extends AppCompatActivity implements View.OnClickListener {
    Context context;

    Button mReg;
    Button mLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.context = Login.this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        init();
        mLogin.setOnClickListener(this);
        mReg.setOnClickListener(this);
    }

    private void init() {
        mLogin = (Button) findViewById(R.id.mLogin);
        mReg = (Button) findViewById(R.id.mReg);
    }
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.mReg:

                //创建okHttpClient对象 get方法
                OkHttpClient mOkHttpClient = new OkHttpClient();
//创建一个Request
                RequestBody formBody = new FormBody.Builder()
                                .add("account", "android")
                                .add("password", "bug")
                                .add("username", "XXXX")
                                .add("pic", "XXXXX")
                                .add("sex", "XXXXX")
                                .build();

                Log.d("11111",formBody.toString());

                final Request request = new Request.Builder()
                        .url("http://192.168.1.157:81/php/insert.php")
                        .post(formBody)
                        .build();
//new call
                Call call = mOkHttpClient.newCall(request);
//请求加入调度
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("22222", "22222");

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        // 输出返回结果
                        Log.d("11111", "11111");

                    }
                });


                break;
            case R.id.mLogin:
                Intent intent = new Intent(Login.this,MainActivity.class);
                startActivity(intent);
                break;
        }

    }
}
