package com.example.android.nutrientInfo.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface NutrientDao {
    @Insert
    void insert(Nutrient nutrient);

    @Delete
    void delete(Nutrient nutrient);

    @Query("SELECT * FROM nutrient WHERE ndbno = :ndbno")
    LiveData<List<Nutrient>> getNutrientsByID(String ndbno);
}
