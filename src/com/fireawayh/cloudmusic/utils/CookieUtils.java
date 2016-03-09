package com.fireawayh.cloudmusic.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by FireAwayH on 15/01/2016. XD
 */
public class CookieUtils {

    private final String USER_AGENT = "Mozilla/5.0";


    // HTTP GET request
    public void sendGet() throws Exception {

        String url = "http://yun.baidu.com/";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
//        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        InputStreamReader in = new InputStreamReader((InputStream) con.getContent());
        BufferedReader buff = new BufferedReader(in);

        StringBuilder text = new StringBuilder();

        String line;
        do {
            line = buff.readLine();
            text.append(line).append("\n");
        } while (line != null);


        String res = text.toString();
        if(res.contains("bdstoken=")){
            String xx = text.toString().split("bdstoken=")[1];
            String token = xx.substring(1,33);
            System.out.println(token);
        }


//        System.out.println(text.toString());


       }
}
