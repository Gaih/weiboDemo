package com.liuhuan.demo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/3/10.
 */

public class SendMess extends Dialog {



        private Context context;
//        private String title;
        private String confirmButtonText;
        private String cacelButtonText;
        private ClickListenerInterface clickListenerInterface;
    private Handler mHandler;
    private EditText mSendCon;
    private static final int ref =2;//获取图片成功的标识

        public interface ClickListenerInterface {

            public void doConfirm();

            public void doCancel();
        }

        public SendMess(Context context, Handler mhandler, String confirmButtonText, String cacelButtonText) {
            super(context);
            this.context = context;
//            this.title = title;
            this.confirmButtonText = confirmButtonText;
            this.cacelButtonText = cacelButtonText;
            this.mHandler = mhandler;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            super.onCreate(savedInstanceState);

            init();
        }

        public void init() {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.dialog, null);
            setContentView(view);

            mSendCon = (EditText) view.findViewById(R.id.sendCon);
//            TextView tvTitle = (TextView) view.findViewById(R.id.title);
            TextView tvConfirm = (TextView) view.findViewById(R.id.confirm);
            TextView tvCancel = (TextView) view.findViewById(R.id.cancel);

//            tvTitle.setText(title);
            tvConfirm.setText(confirmButtonText);
            tvCancel.setText(cacelButtonText);

            tvConfirm.setOnClickListener(new clickListener());
            tvCancel.setOnClickListener(new clickListener());

            Window dialogWindow = getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
            lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
            dialogWindow.setAttributes(lp);
        }

        public void setClicklistener(ClickListenerInterface clickListenerInterface) {
            this.clickListenerInterface = clickListenerInterface;
        }

        private class clickListener implements View.OnClickListener {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                int id = v.getId();
                switch (id) {
                    case R.id.confirm:
                        clickListenerInterface.doConfirm();
                        break;
                    case R.id.cancel:
                        Log.d("1231231",mSendCon.getText().toString());
                        new Thread(mRunnable).start();
                        SendMess.this.dismiss();
                        break;
                }
            }

        };

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            //创建okHttpClient对象 get方法
            OkHttpClient mOkHttpClient = new OkHttpClient();
//创建一个Request
            RequestBody formBody = new FormBody.Builder()
                    .add("username",Login.username)
                    .add("content", mSendCon.getText().toString())
                    .build();

            Log.d("发送", formBody.toString());

            final Request request = new Request.Builder()
                    .url("http://192.168.1.157:81/php/add.php")
                    .post(formBody)
                    .build();
//new call
            Call call = mOkHttpClient.newCall(request);
//请求加入调度
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("失败", "22222");

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
//                    Message msg = mHandler.obtainMessage();
//                    if (sb.toString().equals("success")){
//                        msg.what = 0;
//                        mHandler.sendMessage(msg);
//                    }else {
//                        msg.what = 1;
//                        mHandler.sendMessage(msg);
//                    }
                    Log.d("返回值", "返回值" + sb.toString());
                    mHandler.obtainMessage(ref).sendToTarget();

                }
            });



        }
    };
}
