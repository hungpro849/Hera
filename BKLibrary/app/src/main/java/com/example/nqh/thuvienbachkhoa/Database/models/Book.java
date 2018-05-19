package com.example.nqh.thuvienbachkhoa.Database.models;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by hoang on 4/15/2018.
 */
@DatabaseTable(tableName = "book")
public class Book {
    /*==================================
    * DESCRIPTIONS
    * ==================================
    * Fields:
    * id : int
    * name : string
    * author : string
    * subject : string
    * description : string
    * score: float
    * image_link : string
    * voters : int
    * remain : int
    * */

    /*==================================
    * FIELDS
    * ==================================
    * */
    @DatabaseField(columnName = "id",  unique = true)
    private String id;

    @DatabaseField(columnName = "name", index = true, canBeNull = false, unique = true)
    private String name;

    @DatabaseField(columnName = "author")
    private String author;

    @DatabaseField(columnName = "subject")
    private String subject;

    @DatabaseField(columnName = "description")
    private String description;

    @DatabaseField(columnName = "score")
    private float score;

    @DatabaseField(columnName = "image_link")
    private String image_link;

    @DatabaseField(columnName = "voters")
    private int voters;

    @DatabaseField(columnName = "remain")
    private int remain;


    /*==================================
    * GETTERS AND SETTERS
    * ==================================
    * */

    public Book() {

    }

    public Book(String id,String name, String author, String subject, String description,
                float score, String image_link, int voters, int remain) {
        this.id=id;
        this.name = name;
        this.author = author;
        this.subject = subject;
        this.description = description;
        this.score = score;
        this.image_link = image_link;
        this.voters = voters;
        this.remain = remain;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getImage_link() {
        return image_link;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }

    public int getVoters() {
        return voters;
    }

    public void setVoters(int voters) {
        this.voters = voters;
    }

    public int getRemain() {
        return remain;
    }

    public void setRemain(int remain) {
        this.remain = remain;
    }
}

