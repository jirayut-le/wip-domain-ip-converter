package com.ske13.ntk.wip;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by macbook on 24/11/2017 AD.
 */


public class Converter {

    public static String convert(String input){
        return (isIP(input)) ? convertByIp(input) : convertByName(input);
    }

    private static boolean isIP(String input){
        long temp = ipToLong(input);
        if(temp > 0)
            return true;
        return false;
    }


    private static String convertByName(String name) {
        String hostAddress;
        try {
            InetAddress inetHost = InetAddress.getByName(name);
            hostAddress = inetHost.getHostAddress();
            System.out.println("The host IP address is: " + hostAddress);
            System.out.println();

        } catch(UnknownHostException ex) {
            System.out.println("Unrecognized host");
            return "Unrecognized host";
        }
        return hostAddress;

    }

    private static String convertByIp(String ip){

        try {
            InetAddress host = InetAddress.getByName(ip);
            String hostName = host.getHostName();
            System.out.println("Domain name is: "+ hostName);
            return hostName;
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
            return "Error";
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

    public static String longToIp(long ip) {
        return ((ip >> 24) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + (ip & 0xFF);
    }
}
