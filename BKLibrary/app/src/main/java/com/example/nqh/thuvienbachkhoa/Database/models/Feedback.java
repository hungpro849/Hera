package com.example.nqh.thuvienbachkhoa.Database.models;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by hoang on 4/24/2018.
 */
@DatabaseTable(tableName = "feedback")
public class Feedback {
    /*==================================
    * DESCRIPTIONS
    * ==================================
    * Fields:
    * id : int
    * content : string
    * time : date
    * user_id : reference to ID of the user who created the feedback
    * */

    /*==================================
    * FIELDS
    * ==================================
    * */
    @DatabaseField(columnName = "id", generatedId = true, unique = true)
    private int id;

    @DatabaseField(columnName = "content")
    private String content;

    @DatabaseField(columnName = "time")
    private long time;

    @DatabaseField(foreign=true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private GeneralUser user;

    /*==================================
    * GETTERS AND SETTERS
    * ==================================
    * */

    public Feedback() {}

    public Feedback(String content, Date time) {
        this.content = content;
        this.time = time.getTime();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public Date getTime() {
        return new Date(time);
    }

    public void setTime(Date time) {
        this.time = time.getTime();
    }

    public GeneralUser getUser() {
        return user;
    }

    public void setUser(GeneralUser user) {
        this.user = user;
    }
}
