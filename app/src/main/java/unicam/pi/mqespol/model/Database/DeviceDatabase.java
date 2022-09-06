package unicam.pi.mqespol.model.Database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import unicam.pi.mqespol.model.Device;
import unicam.pi.mqespol.model.dao.DeviceDao;

@Database(entities = {Device.class},version = 1)
public abstract  class DeviceDatabase extends RoomDatabase {


    private static DeviceDatabase instance;

    public abstract DeviceDao deviceDao();

    public static synchronized DeviceDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), DeviceDatabase.class, "app_database")
                    .fallbackToDestructiveMigration() // ESTO PERMITE LA MIGRACION DE LA BASE DE DATOS.
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsynTask(instance).execute();
        }
    };
    private static class PopulateDbAsynTask extends AsyncTask<Void, Void, Void> {
        private DeviceDao deviceDao;
        private PopulateDbAsynTask(DeviceDatabase db){
            deviceDao = db.deviceDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            deviceDao.insert(new Device("ESP32","TEMPERATURA","40"));
            deviceDao.insert(new Device("ESP31","GAS","44"));
            deviceDao.insert(new Device("ESP30","HUMEDAD","48"));
            return null;
        }
    }



}
