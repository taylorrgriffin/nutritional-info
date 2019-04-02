package com.example.android.nutrientInfo.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity(tableName = "nutrient", primaryKeys= {"nutrient_id","ndbno"})
public class Nutrient implements Serializable {
    @NonNull
    public long nutrient_id;
    @NonNull
    public long ndbno;
    public String name;
    public String unit;
    public float value;
}
