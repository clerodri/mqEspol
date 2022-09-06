package unicam.pi.mqespol.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import unicam.pi.mqespol.model.Device;

@Dao
public interface DeviceDao {
        @Insert
        void insert(Device device);
        @Update
        void update(Device device);
        @Delete
        void delete(Device device);
        @Query("DELETE FROM devices_table")
        void deleteAllDevice();
        @Query("SELECT * FROM devices_table ORDER BY ID DESC ")
        LiveData<List<Device>> getAllDevices();


}
