package com.example.android.nutrientInfo.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity(tableName = "food")
public class Food implements Serializable {
    @NonNull
    @PrimaryKey
    public long ndbno;
    public String name;
}
