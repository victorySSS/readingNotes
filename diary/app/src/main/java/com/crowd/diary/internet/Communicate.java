package com.crowd.diary.internet;

/**
 * Created by leon on 17-12-12.
 */
import java.util.HashMap;
import java.util.Map;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

public class Communicate {
    String httpUrl = null;
    public void login(String username, String password) throws JSONException{
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("username", username);
        jsonObj.put("password", password);
        Communicate comm = new Communicate();
        try {
            String re = comm.sendPost(httpUrl, jsonObj.toString());
        } catch (IOException e){
            e.printStackTrace();
        }

    }


    public void signup(String username, String password) throws JSONException {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("username", username);
        jsonObj.put("password", password);
        Communicate comm = new Communicate();
        try {
            String re = comm.sendPost(httpUrl, jsonObj.toString());
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    private String sendPost(String url,String Params)throws IOException{
        OutputStreamWriter out = null;
        BufferedReader reader = null;
        String response="";
        try {
            URL httpUrl = null; //HTTP URL类 用这个类来创建连接
            //创建URL
            httpUrl = new URL(url);
            //建立连接
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("connection", "keep-alive");
            conn.setUseCaches(false);//设置不要缓存
            conn.setInstanceFollowRedirects(true);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();
            //POST请求
            out = new OutputStreamWriter(
                    conn.getOutputStream());
            out.write(Params);
            out.flush();
            //读取响应
            reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String lines;
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                response+=lines;
            }
            reader.close();
            // 断开连接
            conn.disconnect();
            // System.out.println(response.toString());
            // log.info(response.toString());
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(reader!=null){
                    reader.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }

        return response;
    }
}
