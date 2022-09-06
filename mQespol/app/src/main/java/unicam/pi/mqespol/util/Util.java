package unicam.pi.mqespol.util;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.util.Log;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;
import java.util.Locale;

public class Util {
    final static String formated = "app_net_";
    static boolean bandera = false;
    public static String TCP = "tcp://";
    public static final String CLIENT_ID = "mQespol";


    public static String getFormated(String network) {
        String[] separated = network.split("_");
        if (separated.length >= 2) {
            String topic = separated[separated.length - 1];
            return topic.toUpperCase(Locale.ROOT);
        } else {
            return null;
        }
    }

    public static Boolean isValid(String text, int position) {
        if (position == -1 || text.trim().isEmpty()) {
            bandera = false;
        } else {
            bandera = true;
        }
        return bandera;
    }

    public static String getBrokerURL(Context paramContext) {
        return Formatter.formatIpAddress(((WifiManager) paramContext.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo().getIpAddress());
    }

    public static String getIpLocal() {
        String ip = "";
        try {
            for (Enumeration<NetworkInterface> listNetwork = NetworkInterface.getNetworkInterfaces();
                 listNetwork.hasMoreElements(); ) {
                NetworkInterface networkInterface = listNetwork.nextElement();
                for (Enumeration<InetAddress> enuInet = networkInterface.getInetAddresses();
                     enuInet.hasMoreElements(); ) {
                    InetAddress inetAddress = enuInet.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        ip = inetAddress.getHostAddress();
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "Turn On Internet";
    }
}

