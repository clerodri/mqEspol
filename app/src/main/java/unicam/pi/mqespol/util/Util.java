package unicam.pi.mqespol.util;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.widget.Toast;

import java.util.Locale;

/**
 * Esta clase implementa dos funciones, formatea el nombre de la red para obtener el Topico
 * la otra funciona es para validar que no ingrese en blanco.
 * */

public class Util {
     final static String formated="app_net_";
      static boolean bandera=false;
     public static String BROKER_CONF_FILE= "moqette.conf";
     public static String ALLOW_ANONYMOUS = "allow_anonymous";
     public static String PASSWORD_FILE = "pwd.conf";
     public static String TCP = "tcp://";
     public static  final String CLIENT_ID="mQespol";

     public static String getFormated(String network){
          String[] separated= network.split("_");
          if(separated.length>=2) {
               String topic = separated[separated.length - 1];
               return topic.toUpperCase(Locale.ROOT);
          }else{
               return  null;
          }
     }

     public static Boolean isValid(String text,int position){
          bandera= position != -1 && !text.trim().isEmpty();
          return bandera;
     }

     public static void toast(String msg, Context context){
          Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
     }
     public static String getBrokerURL(Context paramContext) {
          return Formatter.formatIpAddress(((WifiManager)paramContext.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo().getIpAddress());
     }
}
