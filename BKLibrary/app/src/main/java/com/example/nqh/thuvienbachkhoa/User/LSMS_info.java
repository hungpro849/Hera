package com.example.nqh.thuvienbachkhoa.User;

public class LSMS_info {
    String name;
    String ngay_muon;
    String ngay_tra;

    public LSMS_info(String name, String ngay_muon, String ngay_tra) {
        this.name = name;
        this.ngay_muon = ngay_muon;
        this.ngay_tra = ngay_tra;
    }

    public void setNgay_muon(String ngay_muon) {
        this.ngay_muon = ngay_muon;
    }

    public void setNgay_tra(String ngay_tra) {
        this.ngay_tra = ngay_tra;
    }
}
