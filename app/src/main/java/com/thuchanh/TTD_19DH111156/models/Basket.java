package com.thuchanh.TTD_19DH111156.models;
import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;
import java.util.HashMap;

public class Basket implements Serializable {
    public HashMap<String, FoodBasket> foods;
    public double totalPrice;
    public int totalItem;

    public Basket() { // mỗi lần tạo mới khởi tạo lại hashmap
        foods = new HashMap<>();
        totalPrice = 0;
        totalItem = 0;
    }

    public void deleteFood(){
        foods.clear();
    }
    public void addFood(FoodBasket food) {
        foods.put(food.getFoodKey(), food);
    }
//    public Food getFirstFood(){
//        return foods.get
//    }
    public FoodBasket getFood(String key) {
        return foods.get(key);
    }

    public void calculateBasket() {
        totalPrice = 0;
        totalItem = 0;
        for (FoodBasket foodBasket : foods.values()) {//không có cái mới vào
            Log.d("QUANTITY", String.valueOf(foodBasket.getQuantity()) + " SO FOOD " + foods.values().size());
            totalPrice += (foodBasket.price * foodBasket.quantity);
            totalItem += foodBasket.quantity;
        }
    }

    public String getTotalPrice() {
        return totalPrice + " VND";
    }

    public int getTotalItem() {
        return totalItem;
    }

    @Override
    public String toString() {
        return "Basket{" +
                "foods=" + foods +
                ", totalPrice=" + totalPrice +
                ", totalItem=" + totalItem +
                '}';
    }
}
