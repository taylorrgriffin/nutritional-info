package com.example.android.githubsearch;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.android.githubsearch.data.Food;
import com.example.android.githubsearch.data.FoodRepository;

import java.util.ArrayList;
import java.util.List;

public class FoodViewModel extends AndroidViewModel {
    private FoodRepository mFoodRepoRepository;

    public FoodViewModel(Application application) {
        super(application);
        mFoodRepoRepository = new FoodRepository(application);
    }

    public void insertFoodRepo(Food food) {
        mFoodRepoRepository.insertFood(food);
    }

    public void deleteFoodRepo(Food food) {
        mFoodRepoRepository.deleteFood(food);
    }

    public LiveData<List<Food>> getAllFood() {
        return mFoodRepoRepository.getAllFood();
    }

    public LiveData<Food> getFoodByID(long ndbno) {
        return mFoodRepoRepository.getFoodByID(ndbno);
    }
}
