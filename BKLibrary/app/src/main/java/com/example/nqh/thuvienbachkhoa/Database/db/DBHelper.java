package com.example.nqh.thuvienbachkhoa.Database.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.nqh.thuvienbachkhoa.Database.models.*;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;

import static com.example.nqh.thuvienbachkhoa.dangkyActivity.computeHash;

/**
 * Created by hoang on 4/15/2018.
 */

public class DBHelper  extends OrmLiteSqliteOpenHelper {
    // Fields

    public static final String DB_NAME = "library_manage.db";
    private static final int DB_VERSION = 1;
    private static String DB_PATH = "";
    private SQLiteDatabase myDataBase;
    private final Context myContext;


    // Public methods

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        this.myContext = context;
//        getWritableDatabase();
        this.createDataBase();
    }

    public void createDataBase(){
        try {
            boolean dbExist = checkDataBase();

            if(dbExist){
                //do nothing - database already exist
                Log.e("Database: ", "Database have already exised!");
            }else{
                //By calling this method and empty database will be created into the default system path
                //of your application so we are gonna be able to overwrite that database with our database.
//                this.getReadableDatabase();
                this.getWritableDatabase();
                createDemoData(this);
//                copyDataBase();
                Log.e("Database: ", "Database was created!");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void copyDataBase(){

        try{
            //Open your local db as the input stream
            InputStream myInput = myContext.getAssets().open(DB_NAME);

            // Path to the just created empty db
            String outFileName = DB_PATH + DB_NAME;

            //Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);

            //transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer))>0){
                myOutput.write(buffer, 0, length);
            }

            //Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
        }
        catch (Exception e) {
            //catch exception
        }
    }

    public SQLiteDatabase openDataBase() throws SQLException{

        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        return myDataBase;

    }

    public void createDemoData(DBHelper db) {
        String mypassword = null;
        try {
             mypassword=computeHash("123456");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            Log.e("createDemoData", "Called");
            // delete before data
            db.deleteAll(Book.class);
            db.deleteAll(Notification.class);
            db.deleteAll(Feedback.class);
            db.deleteAll(Report.class);
            db.deleteAll(GeneralUser.class);

            // create data for table ``Book``
            Book trituenhantao = new Book("Tri tue nhan tao", "Cao Hoang Tru", "KHMT",
                    "Mon chuyen nghanh KHMT", 4.6f, "C:/User/image/ttnt.img", 10, 5);
            Book giaithuat = new Book("PT & TK giai thuat", "Duong Tuan Anh", "KHMT",
                    "Mon chuyen nghanh KHMT", 4.6f, "C:/User/image/pttkgt.img", 10, 5);
            Book songsong = new Book("Tinh toan song song", "Thoai Nam", "KHMT",
                    "Mon chuyen nghanh KHMT", 4.6f, "C:/User/image/ttss.img", 10, 5);

            Book matma = new Book("Mat ma va an ninh mang", "Thay Khuong", "KHMT",
                    "Mon chuyen nghanh KHMT", 4.6f, "C:/User/image/mmanm.img", 10, 5);

            Book dohoa = new Book("Do hoa may tinh", "Thay Son", "KHMT",
                    "Mon chuyen nghanh KHMT", 4.6f, "C:/User/image/dhmt.img", 10, 5);

            db.fillObject(Book.class, trituenhantao);
            db.fillObject(Book.class, giaithuat);
            db.fillObject(Book.class, songsong);
            db.fillObject(Book.class, matma);
            db.fillObject(Book.class, dohoa);

            // create data for table ``general_user``
            GeneralUser hoang = new GeneralUser("Lam Thanh Hoang", "01666584129", "1511120@hcmut.edu.vn", "KTX khu A DHQG TPHCM", mypassword, 0);
            GeneralUser loc = new GeneralUser("Le Phuoc Loc", "0962262545", "1511842@hcmut.edu.vn", "KTX Hoa Hao", mypassword, 1);
            GeneralUser phap = new GeneralUser("Tran Quoc Phap", "01682477933", "1512395@hcmut.edu.vn", "KTX Hoa Hao", mypassword, 0);
            GeneralUser khoi = new GeneralUser("Bui Quang Khoi", "0963261911", "1411862@hcmut.edu.vn", "Thu Duc", mypassword, 1);
            GeneralUser hung = new GeneralUser("Nguyen Quoc Hung", "Khong biet", "Khong biet", "Co so 1", "", 0);

            db.fillObject(GeneralUser.class, hoang);
            db.fillObject(GeneralUser.class, loc);
            db.fillObject(GeneralUser.class, phap);
            db.fillObject(GeneralUser.class, khoi);
            db.fillObject(GeneralUser.class, hung);

            // create borrowedBookList
            db.fillObject(UserBook.class, new UserBook(hoang, songsong, new java.util.Date()));
            db.fillObject(UserBook.class, new UserBook(loc, songsong, new java.util.Date()));
            db.fillObject(UserBook.class, new UserBook(phap, trituenhantao, new java.util.Date()));
            db.fillObject(UserBook.class, new UserBook(phap, matma, new java.util.Date()));
            db.fillObject(UserBook.class, new UserBook(hung, giaithuat, new java.util.Date()));


            // create data for table ``Notification``
            Notification nammoi = new Notification("Chuc mung nam moi", "Chao mung nam moi 2018", new java.util.Date());
            nammoi.setAdmin(hoang);
            db.fillObject(Notification.class, nammoi);

            Notification nghile = new Notification("Nghi le 30/4 - 1/5", "Lich nghi le 30/4 - 1/5", new java.util.Date());
            nghile.setAdmin(hung);
            db.fillObject(Notification.class, nghile);

            Notification mocua = new Notification("Thoi gian mo cua", "Lich mo cua", new java.util.Date());
            mocua.setAdmin(phap);
            db.fillObject(Notification.class, mocua);


            // create data for table ``Feedback``
            Feedback sachmoi = new Feedback("Chung nao co sach moi vay admin?", new java.util.Date());
            sachmoi.setUser(loc);
            db.fillObject(Feedback.class, sachmoi);

            Feedback dangkythanhvien = new Feedback("Dang ky thanh vien nhu the nao?", new java.util.Date());
            dangkythanhvien.setUser(khoi);
            db.fillObject(Feedback.class, dangkythanhvien);

            // create data for table ``Report``
            Report baocaothangnay = new Report("So nguoi dung thang nay", "Hien thi so nguoi dung, so sach ,...", new java.util.Date(), new java.util.Date());
            baocaothangnay.setAdmin(hoang);
            db.fillObject(Report.class, baocaothangnay);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource cs) {
        try {
            // Create Table with given table name with columnNames
            TableUtils.createTable(cs, Book.class);
            TableUtils.createTable(cs, Notification.class);
            TableUtils.createTable(cs, Feedback.class);
            TableUtils.createTable(cs, Report.class);
            TableUtils.createTable(cs, GeneralUser.class);
            TableUtils.createTable(cs, UserBook.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }

    @Override
    public synchronized void close() {

        if(myDataBase != null)
        { myDataBase.close();}

        super.close();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource cs, int oldVersion, int newVersion) {


    }

    public <T> List<T> getAll(Class<T> clazz) throws SQLException {
        Dao<T, ?> dao = getDao(clazz);
        Log.e("Database size" ,Integer.toString(dao.queryForAll().size()));
        return dao.queryForAll();
    }

    public <T> List<T> getAllOrdered(Class<T> clazz, String orderBy, boolean ascending) throws SQLException {
        Dao<T, ?> dao = getDao(clazz);
        return dao.queryBuilder().orderBy(orderBy, ascending).query();
    }

    public <T> void fillObject(Class<T> clazz, T aObj) throws SQLException {
        Dao<T, ?> dao = getDao(clazz);
        dao.createOrUpdate(aObj);
    }

    public <T> void fillObjects(Class<T> clazz, Collection<T> aObjList) throws SQLException {
        Dao<T, ?> dao = getDao(clazz);
        for (T obj : aObjList) {
            dao.createOrUpdate(obj);
        }
    }

    public <T> T getById(Class<T> clazz, Object aId) throws SQLException {
        Dao<T, Object> dao = getDao(clazz);
        return dao.queryForId(aId);
    }

    public <T> List<T> query(Class<T> clazz, Map<String, Object> aMap) throws SQLException {
        Dao<T, ?> dao = getDao(clazz);

        return dao.queryForFieldValues(aMap);
    }

    public <T> List<T> queryLike(Class<T> clazz, String columnName, String value) throws SQLException {
        Dao<T, ?> dao = getDao(clazz);
        return dao.queryBuilder().where().like(columnName, "%" + value + "%").query();
    }


    public <T> List<T> queryZero(Class<T> clazz, String columnName) throws SQLException {
        Dao<T, ?> dao = getDao(clazz);
        return dao.queryBuilder().where().eq(columnName,0).query();
    }

    public <T> List<T> queryZeroDistinct(Class<T> clazz, String columnName, String distinctColumn) throws SQLException {
        Dao<T, ?> dao = getDao(clazz);
        return dao.queryBuilder().distinct().selectColumns(distinctColumn).where().eq(columnName,0).query();
    }

    public <T> List<T> queryEqual(Class<T> clazz, String columnName, Object obj) throws SQLException {
        Dao<T, ?> dao = getDao(clazz);
        return dao.queryBuilder().where().eq(columnName,obj).query();
    }


    public <T> List<T> queryEqualDistinct(Class<T> clazz, String columnName, String distinctColumn, Object obj) throws SQLException {
        Dao<T, ?> dao = getDao(clazz);
        return dao.queryBuilder().distinct().selectColumns(distinctColumn).where().eq(columnName,obj).query();
    }

    public <T> List<T> queryNot(Class<T> clazz, String columnName, int value) throws SQLException {
        Dao<T, ?> dao = getDao(clazz);

        return dao.queryBuilder().where().ne(columnName, value).query();
    }

    public <T> List<T> queryLikeDistinct(Class<T> clazz, String columnName, String value) throws SQLException {
        Dao<T, ?> dao = getDao(clazz);
        return dao.queryBuilder().distinct().where().like(columnName, "%" + value + "%").query();
    }

    public <T> T queryFirst(Class<T> clazz, Map<String, Object> aMap) throws SQLException {
        Dao<T, ?> dao = getDao(clazz);
        List<T> list = dao.queryForFieldValues(aMap);
        if (list.size() > 0)
            return list.get(0);
        else
            return null;
    }




    public <T> Dao.CreateOrUpdateStatus createOrUpdate(T obj) throws SQLException {
        Dao<T, ?> dao = (Dao<T, ?>) getDao(obj.getClass());
        return dao.createOrUpdate(obj);
    }

    public <T> int deleteById(Class<T> clazz, Object aId) throws SQLException {
        Dao<T, Object> dao = getDao(clazz);
        return dao.deleteById(aId);
    }

    public <T> int deleteObjects(Class<T> clazz, Collection<T> aObjList) throws SQLException {
        Dao<T, ?> dao = getDao(clazz);

        return dao.delete(aObjList);
    }

    public <T> void deleteAll(Class<T> clazz) throws SQLException {
        Dao<T, ?> dao = getDao(clazz);
        dao.deleteBuilder().delete();
    }

    public static HashMap<String, Object> where(String aVar, Object aValue) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put(aVar, aValue);
        return result;
    }
}
