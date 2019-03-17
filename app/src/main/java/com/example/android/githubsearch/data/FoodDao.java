package com.example.android.githubsearch.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface FoodDao {
    @Insert
    void insert(Food food);

    @Delete
    void delete(Food food);

    @Query("SELECT * FROM food")
    LiveData<List<Food>> getAllFood();

    @Query("SELECT * FROM food WHERE ndbno = :ndbno LIMIT 1")
    LiveData<Food> getFoodByID(String ndbno);
}
