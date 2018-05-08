package com.globocom.aureogames_2018.Utilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SMSReaderUtil {



    public static String sendOTP(String mobile){
        String response = "";
        try {
            String message = "OTP PIN" +getRandomInteger(0, 4);
            String smsurl = Constants.SMS_URL.replace("@msisdn",mobile).replace("@message", message);
            URL url = new URL(smsurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            int responseCode = conn.getResponseCode();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer resp = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                resp.append(inputLine);
            }
            in.close();
            // print result
            response = resp.toString();
            //System.out.println("IDEA SMS :  " + response);
        } catch (Exception e) {

        }
        return response;
    }

    public static int getRandomInteger(int maximum, int minimum) {
        return ((int) (Math.random() * (maximum - minimum))) + minimum;
    }


    public static void main(String args[]){
        String resp = sendOTP("919538985200");
        System.out.print(resp);

    }
}
