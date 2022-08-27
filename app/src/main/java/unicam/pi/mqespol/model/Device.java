package unicam.pi.mqespol.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "devices_table")
public class Device implements Serializable {
    private String name;
    private String topic;
    private String message;
    @PrimaryKey(autoGenerate = true)
    private int Id;

    public Device(String name, String topic, String message) {
        this.name = name;
        this.topic = topic;
        this.message = message;
    }

    public int getId() {
        return Id;
    }

    public String getName() {
        return name;
    }

    public String getTopic() {
        return topic;
    }

    public String getMessage() {
        return message;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setId(int id) {
        Id = id;
    }
}
