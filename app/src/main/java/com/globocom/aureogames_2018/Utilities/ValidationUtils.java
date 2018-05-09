package com.globocom.aureogames_2018.Utilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.Collections;
import java.util.List;

public class ValidationUtils {

    public static boolean checkValidMobileNumber(String mobileNo){

        boolean flag=false;
        String msisdn="";
        // TODO: start with 00 and length is 12
        if(mobileNo.startsWith("00") && mobileNo.length()>12){
            flag=true;
        }
        // TODO: start with 5 and length is 10
        else if(mobileNo.startsWith("5") && mobileNo.length()==10){
            flag=true;
        }
        //TODO: start with 971 and length is 12
        else if(mobileNo.length()==12 && mobileNo.startsWith("971")){
            flag=true;
        }
        //TODO: start with 05 and length is 10
        else if(mobileNo.length()==10 && mobileNo.startsWith("05")){
            flag=true;
        }
        //TODO: start with 5 and length is 9
        else if(mobileNo.length()==9 && mobileNo.startsWith("5")){
            flag=true;
        }
        //TODO: FOR INDIA TESTING
        else if(mobileNo.length()==12 && mobileNo.startsWith("91")){
            msisdn=mobileNo;
        } else{
            flag =false;
        }
        return flag;
    }

    public static String validedMobileNumber(String mobileNo){
        String msisdn="";
        // TODO: start with 00 and length is 12
        if(mobileNo.startsWith("00") && mobileNo.length()>12){
            msisdn=mobileNo.substring(mobileNo.length()-12,mobileNo.length());
        }
        // TODO: start with 5 and length is 10
        else if(mobileNo.startsWith("5") && mobileNo.length()==10){
            msisdn="971"+mobileNo;
        }
        //TODO: start with 971 and length is 12
        else if(mobileNo.length()==12 && mobileNo.startsWith("971")){
            msisdn=mobileNo;
        }
        //TODO: start with 05 and length is 10
        else if(mobileNo.length()==10 && mobileNo.startsWith("05")){
            msisdn="971"+mobileNo.substring(mobileNo.length()-9,mobileNo.length());
        }
        //TODO: start with 5 and length is 9
        else if(mobileNo.length()==9 && mobileNo.startsWith("5")){
            msisdn="971"+mobileNo;
        }
        //TODO: FOR INDIA TESTING
        else if(mobileNo.length()==12 && mobileNo.startsWith("91")){
            msisdn=mobileNo;
        }else{
            msisdn=mobileNo;
        }
        return msisdn;
    }

    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "";
    }




    public static String generatePIN() {
        String pin="";
        //generate a 4 digit integer 1000 <10000
        int randomPIN = (int)(Math.random()*9000)+1000;
        //Store integer in a string
        return ""+randomPIN;
    }

    public static void main(String args[]){

        System.out.println(getOprator("971561114786"));

        System.out.println("IP ADDRESS :"+getIPAddress(true));

        String mobileNo="919538985200";

        System.out.println(checkValidMobileNumber(mobileNo));

        mobileNo="582686851";

        System.out.println(checkValidMobileNumber(mobileNo));

        mobileNo="0582686851";

        System.out.println(checkValidMobileNumber(mobileNo));

        mobileNo="123456666";

        System.out.println(checkValidMobileNumber(mobileNo));

    }

    private static String getOprator(String msisdn){
        String response="";
        HttpURLConnection urlConnection = null;
        String requestUrl= ConstantsValues.GET_OPERATOR.replace("@msisdn",msisdn);
        try {
            URL url = new URL(requestUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */ );
            urlConnection.setConnectTimeout(15000 /* milliseconds */ );
            urlConnection.setDoOutput(true);
            urlConnection.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();

            response = sb.toString();
            System.out.println("Response : "+response);

        }catch (Exception e){

        }
        return response;
    }
}
