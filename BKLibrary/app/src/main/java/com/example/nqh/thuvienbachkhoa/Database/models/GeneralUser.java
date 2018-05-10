package com.example.nqh.thuvienbachkhoa.Database.models;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.FileOutputStream;
import java.util.Collection;
import java.util.SimpleTimeZone;

/**
 * Created by hoang on 4/24/2018.
 */
@DatabaseTable(tableName = "general_user")
public class GeneralUser {
    /*==================================
    * DESCRIPTIONS
    * ==================================
    * Fields:
    * id : int
    * name : string
    * phone : string
    * email : string
    * address : string
    * password: string
    * isuser: int 1 - user, 0 - admin
    * */

    /*==================================
    * FIELDS
    * ==================================
    * */

    @DatabaseField(columnName = "id", generatedId = true, unique = true)
    protected int id;

    @DatabaseField(columnName = "name")
    protected String name;

    @DatabaseField(columnName = "phone")
    protected String phone;

    @DatabaseField(columnName = "email", unique = true)

    protected String email;

    @DatabaseField(columnName = "address")
    protected String address;

    @DatabaseField(columnName = "password")
    protected String password;

    @DatabaseField(columnName = "isuser")
    protected int isuser;

    @DatabaseField(columnName = "islocked")
    protected boolean islocked;





    /*==================================
    * GETTERS AND SETTERS
    * ==================================
    * */

    public GeneralUser() {}

    public GeneralUser(String name, String phone, String email, String address, String password, int isuser) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.password = password;
        this.isuser = isuser;
        islocked = false;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getIsuser() {
        return isuser;
    }

    public void setIsuser(int isuser) {
        this.isuser = isuser;
    }

    public boolean isIslocked() { return islocked; }

    public void setIslocked() { islocked = !islocked; }
}
