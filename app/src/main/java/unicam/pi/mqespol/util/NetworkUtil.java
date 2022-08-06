package unicam.pi.mqespol.util;

import android.util.Log;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;
public class NetworkUtil {
    private NetworkUtil(){
        //Singlenton
    }
    private static final String TAG = "NetworkUtil";
    private static final NetworkUtil INSTANCE=new NetworkUtil();
    private String address;
    private List<InetAddress> list_addrs;

    public static NetworkUtil getInstance(){
        return INSTANCE;
    }
    public String getIP(){

        address = "";
        try{
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for(NetworkInterface intf : interfaces){
                list_addrs = Collections.list(intf.getInetAddresses());
                for(InetAddress addr : list_addrs){
                    if(!addr.isLoopbackAddress() && addr instanceof Inet4Address){
                        address = addr.getHostAddress().toUpperCase(new Locale("es", "MX"));
                    }
                }
            }
        }catch (SocketException e){
            Log.w(TAG, "Ex getting IP value " + e.getMessage());
        }
        return address;
    }
}
