package com.example.nqh.findjobs;

/**
 * Created by NQH on 23/05/2018.
 */

public class job {


    String tittle;
    String salary;
    public job(String tittle, String salary) {
        this.tittle = tittle;
        this.salary = salary;
    }
    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }


}
