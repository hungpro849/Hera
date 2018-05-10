package com.example.nqh.thuvienbachkhoa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.nqh.thuvienbachkhoa.Admin.AdminActivity;
import com.example.nqh.thuvienbachkhoa.Database.db.DBHelper;


import com.example.nqh.thuvienbachkhoa.Database.models.*;
import com.example.nqh.thuvienbachkhoa.User.UserActivity;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    DBHelper db;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DBHelper(this);
//        createDemoData(db);

        boolean hasUserSignedOut = false;
        int isNormalUser = 1;

        SharedPreferences session;
        session = this.getSharedPreferences("sessionUser", Context.MODE_PRIVATE);
        String currentEmail = session.getString("email","");

        if (currentEmail.isEmpty()) {
            hasUserSignedOut = true;
        } else {
            Map<String, Object> condition = new HashMap<String, Object>();
            condition.put("email", currentEmail);
            List<GeneralUser> currentUsers = Collections.emptyList();
            try {
                currentUsers = db.query(GeneralUser.class, condition);
                if (currentUsers.size() > 0) {
                    hasUserSignedOut = false;
                    isNormalUser = currentUsers.get(0).getIsuser();
                }
                else hasUserSignedOut = true;
            } catch (Exception e) {
                //if cannot query current user, consider them to have signed out
                hasUserSignedOut = true;
            }
        }

        //load activity
        if (!hasUserSignedOut) {
            if (isNormalUser == 1) {
                Intent userActivity = new Intent(this, UserActivity.class);
                startActivity(userActivity);
            } else {
                Intent adminActivity = new Intent(this, AdminActivity.class);
                startActivity(adminActivity);
            }
        } else {
            Intent dangNhapActivity = new Intent(this, DangNhapActivity.class);
            startActivity(dangNhapActivity);
        }
    }


    public void createDemoData(DBHelper db) {
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
            GeneralUser hoang = new GeneralUser("Lam Thanh Hoang", "01666584129", "1511120@hcmut.edu.vn", "KTX khu A DHQG TPHCM", "1511120", 0);
            GeneralUser loc = new GeneralUser("Le Phuoc Loc", "0962262545", "1511842@hcmut.edu.vn", "KTX Hoa Hao", "1511842", 1);
            GeneralUser phap = new GeneralUser("Tran Quoc Phap", "01682477933", "1512395@hcmut.edu.vn", "KTX Hoa Hao", "1512395", 0);
            GeneralUser khoi = new GeneralUser("Bui Quang Khoi", "0963261911", "1411862@hcmut.edu.vn", "Thu Duc", "1411862", 1);
            GeneralUser hung = new GeneralUser("Nguyen Quoc Hung", "Khong biet", "Khong biet", "Co so 1", "", 0);

            db.fillObject(GeneralUser.class, hoang);
            db.fillObject(GeneralUser.class, loc);
            db.fillObject(GeneralUser.class, phap);
            db.fillObject(GeneralUser.class, khoi);
            db.fillObject(GeneralUser.class, hung);

            // create borrowedBookList
            db.fillObject(UserBook.class, new UserBook(hoang, songsong, new Date()));
            db.fillObject(UserBook.class, new UserBook(loc, songsong, new Date()));
            db.fillObject(UserBook.class, new UserBook(phap, trituenhantao, new Date()));
            db.fillObject(UserBook.class, new UserBook(phap, matma, new Date()));
            db.fillObject(UserBook.class, new UserBook(hung, giaithuat, new Date()));


            // create data for table ``Notification``
            Notification nammoi = new Notification("Chuc mung nam moi", "Chao mung nam moi 2018", new Date());
            nammoi.setAdmin(hoang);
            db.fillObject(Notification.class, nammoi);

            Notification nghile = new Notification("Nghi le 30/4 - 1/5", "Lich nghi le 30/4 - 1/5", new Date());
            nghile.setAdmin(hung);
            db.fillObject(Notification.class, nghile);

            Notification mocua = new Notification("Thoi gian mo cua", "Lich mo cua", new Date());
            mocua.setAdmin(phap);
            db.fillObject(Notification.class, mocua);


            // create data for table ``Feedback``
            Feedback sachmoi = new Feedback("Chung nao co sach moi vay admin?", new Date());
            sachmoi.setUser(loc);
            db.fillObject(Feedback.class, sachmoi);

            Feedback dangkythanhvien = new Feedback("Dang ky thanh vien nhu the nao?", new Date());
            dangkythanhvien.setUser(khoi);
            db.fillObject(Feedback.class, dangkythanhvien);

            // create data for table ``Report``
            Report baocaothangnay = new Report("So nguoi dung thang nay", "Hien thi so nguoi dung, so sach ,...", new Date(), new Date());
            baocaothangnay.setAdmin(hoang);
            db.fillObject(Report.class, baocaothangnay);

        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }


    public void setUpDatabase() {

//        createDemoData(db);


        try {
            // test the function create a new book
//            db.fillObject(Book.class, new Book("Machine Learning", "AndrewNg", "ML/AI",
//                    "Material for ML", 4.6f, "C:/User/image/ml.img", 10, 5));
//            db.fillObject(Book.class, new Book("Machine Learning", "AndrewNg", "ML/AI",
//                    "Material for ML", 4.6f, "C:/User/image/ml.img", 10, 5));
//            db.fillObject(Book.class, new Book("ABC", "Fuck you", "ML/AI",
//                    "Material for ML", 4.6f, "C:/User/image/m2.img", 10, 5));
//
//            // delete a book
//            Book deletedBook = new Book("Deleted Book", "Fuck you", "ML/AI",
//                    "Material for ML", 4.6f, "C:/User/image/m2.img", 10, 5);
//            db.fillObject(Book.class, deletedBook);
//            db.deleteById(Book.class, deletedBook.getId());
//
//            // update an existed book
//            Book updatedBook = new Book("Updated Book", "Fuck you", "ML/AI",
//                    "Material for ML", 4.6f, "C:/User/image/m2.img", 10, 5);
//            db.fillObject(Book.class, updatedBook);
//            updatedBook.setName("After updated book");
//            db.createOrUpdate(updatedBook);
//            Log.d("Id of a book: ", Integer.toString(updatedBook.getId()));

            // search a book
//            Map<String, Object> condition = new HashMap<String, Object>();
//            condition.put("name", "Machine Learning");
//            List<Book> foundBook = db.query(Book.class, condition);
//            for (Book b: foundBook) {
//                Log.d("Found: ", b.getName());
//            }

            // get all books
            List<Book> all_books = db.getAll(Book.class);
            for (Book b: all_books) {
                Log.d("Book :", b.getName());
            }
            // query like %name%
            List<Book> books = db.queryLike(Book.class, "name", "hoa");
            for (Book b: books) {
                Log.d("Found :", b.getName());
            }

            // get list user borrowed book ``song song`` - result should be hoang and loc
            // and test join two table
            Map<String, Object> condition1 = new HashMap<String, Object>();
            condition1.put("name", "Tinh toan song song");
            Book songsong = db.queryFirst(Book.class, condition1);
            Map<String, Object> condition2 = new HashMap<String, Object>();
            condition2.put("book_id", songsong.getId());
            List<UserBook> user_book = db.query(UserBook.class, condition2);
            for (UserBook userBook : user_book) {
                Log.d("Name of book borrower:", userBook.getUser().getName());
            }
        }
        catch (SQLException e) {
            Log.e("Exception", e.getMessage());
        }
    }


    public List<Book> bookQuery(String query) {
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("name", query);
        List<Book> foundBook = Collections.emptyList();
        try {
            foundBook = db.query(Book.class, condition);
        } catch (Exception e) {
            Log.e("Book Query Exception", e.getMessage());
        }
        return foundBook;
    }

    //on returning to this activity, which means from any other activity, on back press, finish this
    // since this is just to load and check data.
    @Override
    protected void onResume() {
        super.onResume();
        finish();
    }
}
