package com.liuhuan.demo;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/2.
 */

public class demo {
    public static void main(String[] args) {
        try {
            URL url = new URL("http://192.168.1.157:81/index.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            // 输出返回结果
            InputStream input = conn.getInputStream();
            int resLen = 0;
            byte[] res = new byte[1024];
            StringBuilder sb = new StringBuilder();
            while ((resLen = input.read(res)) != -1) {
                sb.append(new String(res, 0, resLen));
            }
            String jsonStr = sb.toString();
            //Json的解析类对象
            JsonParser parser = new JsonParser();
            //将JSON的String 转成一个JsonArray对象
            JsonArray jsonArray = parser.parse(jsonStr).getAsJsonArray();

            Gson gson = new Gson();
            ArrayList<userInfo> userBeanList = new ArrayList<>();

            //加强for循环遍历JsonArray
            for (JsonElement user : jsonArray) {
                //使用GSON，直接转成Bean对象
                userInfo userBean = gson.fromJson(user, userInfo.class);
                userBeanList.add(userBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
