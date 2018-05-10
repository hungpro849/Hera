package com.example.nqh.thuvienbachkhoa.Database.models;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by hoang on 4/24/2018.
 */
@DatabaseTable(tableName = "report")
public class Report {
    /*==================================
    * DESCRIPTIONS
    * ==================================
    * Fields:
    * id : int
    * topic : string
    * content : string
    * start_date : date
    * end_date : date
    * admin_id : foreign key to admin
    * */

    /*==================================
    * FIELDS
    * ==================================
    * */
    @DatabaseField(columnName = "id", generatedId = true, unique = true)
    private int id;

    @DatabaseField(columnName = "topic")
    private  String topic;

    @DatabaseField(columnName = "content")
    private String content;

    @DatabaseField(columnName = "start_date")
    private long start_date;


    @DatabaseField(columnName = "end_date")
    private long end_date;

    @DatabaseField(foreign=true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private GeneralUser admin;
    /*==================================
    * GETTERS AND SETTERS
    * ==================================
    * */

    public Report() {}

    public Report(String topic, String content, Date start_date, Date end_date) {
        this.topic = topic;
        this.content = content;
        this.start_date = start_date.getTime();
        this.end_date = end_date.getTime();
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

    public Date getStart_date() {
        return new Date(start_date);
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date.getTime();
    }

    public Date getEnd_date() {
        return new Date(end_date);
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date.getTime();
    }

    public void setAdmin(GeneralUser admin) {
        this.admin = admin;
    }

    public GeneralUser getAdmin() {
        return admin;
    }
}
