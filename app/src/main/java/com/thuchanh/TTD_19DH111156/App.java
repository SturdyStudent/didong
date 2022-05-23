package com.thuchanh.TTD_19DH111156;

import android.app.Application;
import android.util.Log;

import com.thuchanh.TTD_19DH111156.models.Basket;
import com.thuchanh.TTD_19DH111156.models.OrderFinished;

public class App extends Application {
    private static Basket basket;
    private static OrderFinished order;
    private static App instance;
    private static String resKey;

    public static App getInstance(){
        if(instance == null){
            instance = new App();
            instance.basket = new Basket();
            instance.order = new OrderFinished();
        }
        return instance;
    }
    public static Basket getBasket(){
        return basket;
    }
    public static OrderFinished getOrder(){
        return order;
    }
    public static String getResKey() {return resKey;}
    public void setOrder(OrderFinished order){
        this.order = order;
    }
    public void setResKey(String key){
        resKey = key;
    }
}
