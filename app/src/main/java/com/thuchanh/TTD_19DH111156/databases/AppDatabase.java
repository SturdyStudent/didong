package com.thuchanh.TTD_19DH111156.databases;

import android.content.Context;
import android.hardware.input.InputManager;
import android.view.inputmethod.InputMethodManager;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.thuchanh.TTD_19DH111156.databases.DAOs.Cart;
import com.thuchanh.TTD_19DH111156.databases.DAOs.CartDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database( entities = {Cart.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;
    public abstract CartDao cartDao();
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(1);

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class , "CartDB")
                    .build();
        }
        return INSTANCE;
    }
    public static void destroyInstance() {
        INSTANCE = null;
    }
}