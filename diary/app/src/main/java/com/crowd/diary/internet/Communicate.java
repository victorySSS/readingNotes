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
    String myUrl = "http://www.ludics.cn/app/";
    

    /**
     * @作用 注册账号
     * @param username
     * @param password
     * @return "Username existed, register failed."
     * @return "Register success."
     * @throws IOException
     */
    public String registerToServer(String username,String password) throws IOException{
        OutputStreamWriter out = null;
        BufferedReader reader = null;
        String response="";
        try {
            URL httpUrl = null; //HTTP URL类 用这个类来创建连接
            //创建URL
            httpUrl = new URL(myUrl+"signup.php");
            //建立连接
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Charset", "utf-8");
            conn.setUseCaches(false);//设置不要缓存
            conn.setInstanceFollowRedirects(true);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(3000);
            String data = "&userName=" + username + "&userPassword=" + password;
            conn.setRequestProperty("Content-Length",String.valueOf(data.getBytes().length));
            conn.connect();
            //POST请求
            OutputStream os = conn.getOutputStream();
            os.write(data.getBytes());
            os.flush();
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
            // System.out.println("发送 POST 请求出现异常！"+e);
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
    /**
     * @作用 登录
     * @param username
     * @param password
     * @return "Login success."
     * @return "Password error."
     * @return "User not exist."
     * @throws IOException
     */
    public String loginToServer(String username, String password) throws IOException{
        OutputStreamWriter out = null;
        BufferedReader reader = null;
        String response="";
        try {

            URL httpUrl = null; //HTTP URL类 用这个类来创建连接
            //创建URL
            httpUrl = new URL(myUrl+"user_login.php");
            //建立连接
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Charset", "utf-8");
            conn.setUseCaches(false);//设置不要缓存
            conn.setInstanceFollowRedirects(true);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(3000);
            String data = "&userName=" + username + "&userPassword=" + password;
            conn.setRequestProperty("Content-Length",String.valueOf(data.getBytes().length));
            conn.connect();
            //POST请求
            OutputStream os = conn.getOutputStream();
            os.write(data.getBytes());
            os.flush();
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
            // System.out.println("发送 POST 请求出现异常！"+e);
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
