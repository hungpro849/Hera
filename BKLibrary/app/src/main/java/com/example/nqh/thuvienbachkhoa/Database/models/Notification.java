package com.example.nqh.thuvienbachkhoa.Database.models;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by hoang on 4/24/2018.
 */

@DatabaseTable(tableName = "notification")
public class Notification {
    /*==================================
    * DESCRIPTIONS
    * ==================================
    * Fields:
    * id : int
    * topic : string
    * content : string
    * create_date : long
    * admin : foreign key to admin - a field ``admin_id`` is created in DB
    * */

     /*==================================
    * FIELDS
    * ==================================
    * */
    @DatabaseField(columnName = "id", generatedId = true, unique = true)
    private int id;

    @DatabaseField(columnName = "topic")
    private String topic;

    @DatabaseField(columnName = "content")
    private String content;

    @DatabaseField(columnName = "create_date")
    private long create_date;

    @DatabaseField(foreign=true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private GeneralUser admin;
    /*==================================
    * GETTERS AND SETTERS
    * ==================================
    * */

    public Notification() {

    }

    public Notification(String topic, String content, Date create_date) {
        this.topic = topic;
        this.content = content;
        this.create_date = create_date.getTime();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public Date getCreate_date() {
        return new Date(create_date);
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date.getTime();
    }

    public GeneralUser getAdmin() {
        return admin;
    }

    public void setAdmin(GeneralUser admin) {
        this.admin = admin;
    }
}
