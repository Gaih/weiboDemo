package com.liuhuan.demo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;

import java.io.IOException;
import java.io.InputStream;

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

    private Button mReg;
    private Button mLogin;
    private EditText mUsername;
    private EditText mPassword;
    private ProgressDialog mDialog;
    public static String username;


    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.context = Login.this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        init();
        mLogin.setOnClickListener(this);
        mReg.setOnClickListener(this);
        mUsername = (EditText) findViewById(R.id.mUsername);
        mPassword = (EditText) findViewById(R.id.mPassword);

        //信鸽推送注册
        XGPushManager.registerPush(this, "123", new XGIOperateCallback() {
            @Override
            public void onSuccess(Object o, int i) {
                Toast.makeText(context, "TPush" + "注册成功，设备token为：" + o, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail(Object o, int i, String s) {
                Toast.makeText(context, "注册失败，错误码：" + i + ",错误信息：" + s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        mLogin = (Button) findViewById(R.id.mLogin);
        mReg = (Button) findViewById(R.id.mReg);
    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mReg:
                Intent intent = new Intent(Login.this, Regiest.class);
                startActivity(intent);
                finish();

                break;
            case R.id.mLogin:
                if (TextUtils.isEmpty(mUsername.getText())||TextUtils.isEmpty(mPassword.getText())){
                    Snackbar.make(getWindow().getDecorView(), "请输入用户名或密码！", Snackbar.LENGTH_SHORT).show();
                }else {
                    mDialog = new ProgressDialog(Login.this);
                    mDialog.setTitle("登陆");

                    mDialog.setMessage("正在登陆服务器，请稍后...");
                    mDialog.show();
                    new Thread(mRunnable).start();
                }
                break;

        }

    }

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            //创建okHttpClient对象 get方法
            OkHttpClient mOkHttpClient = new OkHttpClient();
//创建一个Request
            RequestBody formBody = new FormBody.Builder()
                    .add("username", mUsername.getText().toString())
                    .add("password", mPassword.getText().toString())
                    .build();

            Log.d("11111", formBody.toString());

            final Request request = new Request.Builder()
                    .url("http://192.168.1.181/php/insert.php")
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
                    InputStream input = response.body().byteStream();
                    int resLen = 0;
                    byte[] res = new byte[1024];
                    StringBuilder sb = new StringBuilder();
                    while ((resLen = input.read(res)) != -1) {
                        sb.append(new String(res, 0, resLen));
                    }
                    Message msg = mHandler.obtainMessage();
                    if (sb.toString().contains("success")) {
                        String[] name = sb.toString().split("。");
                        Log.d("用户名:", name[1]);
                        username= name[1];
                        msg.what = 0;
                        mHandler.sendMessage(msg);
                    } else {
                        msg.what = 1;
                        mHandler.sendMessage(msg);
                    }
                    Log.d("11111", "11111" + sb.toString());

                }
            });
        }
    };
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mDialog.cancel();
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(getApplicationContext(), "登录成功~", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    mDialog.cancel();
                    Toast.makeText(getApplicationContext(), "用户名或密码错误", Toast.LENGTH_SHORT).show();
                    break;

            }

        }
    };

//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getUsername() {
//        return this.username;
//    }
}
