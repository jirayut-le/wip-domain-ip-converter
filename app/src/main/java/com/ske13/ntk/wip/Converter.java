package com.ske13.ntk.wip;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by macbook on 24/11/2017 AD.
 */


public class Converter  {

    private static ArrayList<String> data = new ArrayList<String>();

    public static ArrayList<String> convert(String input){
        data.clear();
        if(isIP(input))
            convertByIp(input);
        else convertByName(input);
        getLocation(input);
        return data;
    }

    private static boolean isIP(String input){
        long temp = ipToLong(input);
        if(temp > 0)
            return true;
        return false;
    }

    private static void convertByName(String name) {
        String hostAddress;
        try {
            InetAddress inetHost = InetAddress.getByName(name);
            hostAddress = inetHost.getHostAddress();
            data.add(hostAddress);
        } catch(UnknownHostException ex) {
            System.out.println("Unrecognized host");
        }

    }

    private static void convertByIp(String ip){

        try {
            InetAddress host = InetAddress.getByName(ip);
            String hostName = host.getHostName();
            data.add(hostName);
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        }
    }


    public static long ipToLong(String ipAddress) {
        int i, power, ip;
        long result = 0;

        String[] ipAddressInArray = ipAddress.split("\\.");

        for (i = 0; i < ipAddressInArray.length; i++) {
            power = 3 - i;
            try {
                ip = Integer.parseInt(ipAddressInArray[i]);
                result += ip * Math.pow(256, power);
            } catch (NumberFormatException e) {
                return -1;
            }
        }

        return result;

    }

    private static void getLocation(String name){
        Location location = new Location();
        String response = null;
        try {
            response = location.run("https://freegeoip.net/json/" + name);

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            response = response.replace(",", "},{");
            ArrayList<String> arrayList = jsonStringToArray("[" + response +"]");
            addData(arrayList);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private static void addData(ArrayList<String> arrayList){
        data.add(getCountry(arrayList));
        data.add(getLatitude(arrayList));
        data.add(getLongitude(arrayList));
    }

    private static String getCountry(ArrayList<String> text){
        String temp = text.get(2).toString().substring(17);
        temp = temp.substring(0,temp.length()-2);
        return temp;
    }

    private static String getLatitude(ArrayList<String> text){
        String latitude = text.get(8).toString().substring(12);
        latitude = latitude.substring(0,latitude.length()-1);
        return latitude;
    }

    private static String getLongitude(ArrayList<String> text){
        String longitude = text.get(9).toString().substring(13);
        longitude = longitude.substring(0,longitude.length()-1);
        return longitude;
    }

    private static ArrayList<String> jsonStringToArray(String jsonString) throws JSONException {

        ArrayList<String> stringArray = new ArrayList<String>();
        JSONArray jsonArray = new JSONArray(jsonString);

        for (int i = 0; i < jsonArray.length(); i++) {
            stringArray.add(jsonArray.getString(i));
        }

        return stringArray;
    }

}
