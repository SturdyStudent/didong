package com.thuchanh.TTD_19DH111156.models;

import com.thuchanh.TTD_19DH111156.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Restaurant implements Serializable {
    public String name;
    public String logo;
    public String cover;
    public String address;
    public String openHours;
    public ArrayList<Food> menu;
    public int rate;
    public String resKey;

    public Restaurant(){
        this.name = "";
        this.logo = "";
        this.cover = "";
        this.address = "";
        this.openHours = "";
        this.menu = new ArrayList<>();
        this.rate = 0;
        this.resKey = "";
    }

    public Restaurant(String name, String logo, String cover, String address, String openHours, ArrayList<Food> menu, int rate, String resKey) {
        this.name = name;
        this.logo = logo;
        this.cover = cover;
        this.address = address;
        this.openHours = openHours;
        this.menu = menu;
        this.rate = rate;
        this.resKey = resKey;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOpenHours() {
        return openHours;
    }

    public void setOpenHours(String openHours) {
        this.openHours = openHours;
    }

    public ArrayList<Food> getMenu() {
        return menu;
    }

    public void setMenu(ArrayList<Food> menu) {
        this.menu = menu;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getResKey() {
        return resKey;
    }

    public void setResKey(String resKey) {
        this.resKey = resKey;
    }


    @Override
    public String toString() {
        return "Restaurant{" +
                "name='" + name + '\'' +
                ", logo='" + logo + '\'' +
                ", cover='" + cover + '\'' +
                ", address='" + address + '\'' +
                ", openHours='" + openHours + '\'' +
                ", menu=" + menu +
                ", rate=" + rate +
                ", resKey='" + resKey + '\'' +
                '}';
    }
}
