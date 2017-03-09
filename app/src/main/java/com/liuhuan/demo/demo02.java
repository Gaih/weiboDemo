package com.liuhuan.demo;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;

/**
 * 用GSON解析Json数组
 */
public class demo02 {
    public static void main(String[] args) {
        // Json数组最外层要加"[]"
        String jsonData =
"[{\"id\":\"1\",\"account\":\"gai\",\"password\":\"123\",\"username\":\"盖小同学\",\"pic\":\"..\\/..\\/..\\/image\\/7.jpg\",\"sex\":\"男\"}," +
        "{\"id\":\"3\",\"account\":\"Mary\",\"password\":\"123\",\"username\":\"Mary\",\"pic\":\"..\\/..\\/..\\/image\\/1.jpg\",\"sex\":\"女\"}," +
        "{\"id\":\"5\",\"account\":\"Jack\",\"password\":\"123\",\"username\":\"Jack\",\"pic\":\"..\\/..\\/..\\/image\\/2.jpg\",\"sex\":\"男\"}," +
        "{\"id\":\"6\",\"account\":\"Bob\",\"password\":\"123\",\"username\":\"Bob\",\"pic\":\"..\\/..\\/..\\/image\\/3.jpg\",\"sex\":\"男\"}," +
        "{\"id\":\"7\",\"account\":\"Baga\",\"password\":\"123\",\"username\":\"Gaga\",\"pic\":\"..\\/..\\/..\\/image\\/7.jpg\",\"sex\":\"女\"}," +
        "{\"id\":\"8\",\"account\":\"123456\",\"password\":\"123456\",\"username\":\"小明\",\"pic\":\"..\\/..\\/..\\/image\\/4.jpg\",\"sex\":\"男\"}," +
        "{\"id\":\"19\",\"account\":\"Jim\",\"password\":\"123\",\"username\":\"Jim\",\"pic\":\"..\\/..\\/..\\/image\\/9.jpg\",\"sex\":\"男\"}," +
        "{\"id\":\"20\",\"account\":\"HH\",\"password\":\"123\",\"username\":\"HH\",\"pic\":\"..\\/..\\/..\\/image\\/title.jpg\",\"sex\":\"女\"}," +
        "{\"id\":\"21\",\"account\":\"hhh\",\"password\":\"123\",\"username\":\"hhh\",\"pic\":\"..\\/..\\/..\\/image\\/9.jpg\",\"sex\":\"女\"}," +
        "{\"id\":\"22\",\"account\":\"admin\",\"password\":\"123456\",\"username\":\"admin\",\"pic\":\"..\\/..\\/..\\/image\\/8.jpg\",\"sex\":\"男\"}," +
        "{\"id\":\"23\",\"account\":\"test\",\"password\":\"123\",\"username\":\"123\",\"pic\":\"..\\/..\\/..\\/image\\/7.jpg\",\"sex\":\"男\"}]";

        //Json的解析类对象
        JsonParser parser = new JsonParser();
        //将JSON的String 转成一个JsonArray对象
        JsonArray jsonArray = parser.parse(jsonData).getAsJsonArray();

        Gson gson = new Gson();
        ArrayList<userInfo> userBeanList = new ArrayList<>();

        //加强for循环遍历JsonArray
        for (JsonElement user : jsonArray) {
            //使用GSON，直接转成Bean对象
            userInfo userBean = gson.fromJson(user, userInfo.class);
            System.out.println(userBean.getUsername());
            userBeanList.add(userBean);
        }
    }
}
class userInfo{
    private String id;
    private String account;
    private String username;
    private String password;
    private String sex;

    public void setId(String id) {
        this.id = id;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getId() {

        return id;
    }

    public String getAccount() {
        return account;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getSex() {
        return sex;
    }

    public String getPic() {
        return pic;
    }

    private String pic;

}