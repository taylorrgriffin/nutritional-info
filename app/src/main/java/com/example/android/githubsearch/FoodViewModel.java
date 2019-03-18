package com.example.android.githubsearch;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.android.githubsearch.data.Food;
import com.example.android.githubsearch.data.FoodRepository;
import com.example.android.githubsearch.data.Nutrient;
import com.example.android.githubsearch.data.NutrientRepository;

import java.util.ArrayList;
import java.util.List;

public class FoodViewModel extends AndroidViewModel {
    private FoodRepository mFoodRepoRepository;
    public NutrientRepository mNutRepository;

    public FoodViewModel(Application application) {
        super(application);
        mFoodRepoRepository = new FoodRepository(application);
        mNutRepository = new NutrientRepository(application);
    }

    public void insertFood(Food food) {
        mFoodRepoRepository.insertFood(food);
    }

    public void deleteFood(Food food) {
        mFoodRepoRepository.deleteFood(food);
    }

    public LiveData<List<Food>> getAllFood() {
        return mFoodRepoRepository.getAllFood();
    }

    public LiveData<Food> getFoodByID(long ndbno) {
        return mFoodRepoRepository.getFoodByID(ndbno);
    }

    public LiveData<List<Nutrient>> getNutrientsByID(long ndbno) { return mNutRepository.getNutrientsByID(ndbno);}

    public void insertNutrient(Nutrient nut) {
        mNutRepository.insertNutrient(nut);
    }

    public void deleteNutrient(Nutrient nut) {
        mNutRepository.deleteNutrient(nut);
    }
}
