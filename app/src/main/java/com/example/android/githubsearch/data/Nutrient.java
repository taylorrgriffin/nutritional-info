package com.example.android.githubsearch.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity(tableName = "nutrient")
public class Nutrient implements Serializable {
    @NonNull
    @PrimaryKey
    public long nutrient_id;
    public long ndbno;
    public String name;
    public String unit;
    public float value;
}
