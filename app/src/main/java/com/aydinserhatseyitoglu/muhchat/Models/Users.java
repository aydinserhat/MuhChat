package com.aydinserhatseyitoglu.muhchat.Models;

public class Users {
    private String bolum,cinsiyet,hakkinda,kullaniciismi,okul_numarasi,profile_image,sinif;

    public Users() {
    }

    public Users(String bolum, String cinsiyet, String hakkinda, String kullaniciismi, String okul_numarasi, String profile_image, String sinif) {
        this.bolum = bolum;
        this.cinsiyet = cinsiyet;
        this.hakkinda = hakkinda;
        this.kullaniciismi = kullaniciismi;
        this.okul_numarasi = okul_numarasi;
        this.profile_image = profile_image;
        this.sinif = sinif;
    }

    public String getBolum() {
        return bolum;
    }

    public void setBolum(String bolum) {
        this.bolum = bolum;
    }

    public String getCinsiyet() {
        return cinsiyet;
    }

    public void setCinsiyet(String cinsiyet) {
        this.cinsiyet = cinsiyet;
    }

    public String getHakkinda() {
        return hakkinda;
    }

    public void setHakkinda(String hakkinda) {
        this.hakkinda = hakkinda;
    }

    public String getKullaniciismi() {
        return kullaniciismi;
    }

    public void setKullaniciismi(String kullaniciismi) {
        this.kullaniciismi = kullaniciismi;
    }

    public String getOkul_numarasi() {
        return okul_numarasi;
    }

    public void setOkul_numarasi(String okul_numarasi) {
        this.okul_numarasi = okul_numarasi;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getSinif() {
        return sinif;
    }

    public void setSinif(String sinif) {
        this.sinif = sinif;
    }

    @Override
    public String toString() {
        return "Users{" +
                "bolum='" + bolum + '\'' +
                ", cinsiyet='" + cinsiyet + '\'' +
                ", hakkinda='" + hakkinda + '\'' +
                ", kullaniciismi='" + kullaniciismi + '\'' +
                ", okul_numarasi='" + okul_numarasi + '\'' +
                ", profile_image='" + profile_image + '\'' +
                ", sinif='" + sinif + '\'' +
                '}';
    }
}

