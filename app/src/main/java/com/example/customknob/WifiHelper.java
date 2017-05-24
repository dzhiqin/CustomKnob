package com.example.customknob;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * Created by Administrator on 2017/5/17.
 */

public class WifiHelper {

    private Context context;
    public WifiHelper(Context context){
        this.context=context;
    }
    public  String getStringIPAddress(){
        WifiManager wifiManager=(WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        // IS  wifi enabled
        if(!wifiManager.isWifiEnabled()){
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo=wifiManager.getConnectionInfo();
        //get the ip address and convert to string, send it out by a udp
        int ipAddress=wifiInfo.getIpAddress();
        String strMes=intToIP(ipAddress);
        return strMes;
    }
    public  byte[] getByteIPAddress(){
        WifiManager wifiManager=(WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        // IS  wifi enabled
        if(!wifiManager.isWifiEnabled()){
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo=wifiManager.getConnectionInfo();
        //get the ip address and convert to string, send it out by a udp
        int ipAddress=wifiInfo.getIpAddress();
        return intToByte(ipAddress);
    }
    private static byte[] intToByte(int i){
        byte[] ipByte=new byte[4];
        ipByte[0]=(byte)(i);
        ipByte[1]=(byte)((i>>8));
        ipByte[2]=(byte)((i>>16));
        ipByte[3]=(byte)((i>>24));
        return ipByte;
    }
    private static String intToIP(int i){
        return (i&0xff)+"."+((i>>8)&0xff)+"."+((i>>16)&0xff)+"."+((i>>24)&0xff);

    }
}
