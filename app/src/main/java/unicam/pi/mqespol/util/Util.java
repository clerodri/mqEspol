package unicam.pi.mqespol.util;

import android.content.Context;
import android.widget.Toast;

import java.util.Locale;

/**
 * Esta clase implementa dos funciones, formatea el nombre de la red para obtener el Topico
 * la otra funciona es para validar que no ingrese en blanco.
 * */

public class util {
     final static String formated="app_net_";
      static boolean bandera=false;


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
}
