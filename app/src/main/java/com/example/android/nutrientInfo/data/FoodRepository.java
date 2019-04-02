package com.example.android.nutrientInfo.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class FoodRepository {
    private FoodDao mFoodDao;

    public FoodRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mFoodDao = db.FoodDao();
    }

    public void insertFood(Food food) {
        new InsertAsyncTask(mFoodDao).execute(food);
    }

    public void deleteFood(Food food) {
        new DeleteAsyncTask(mFoodDao).execute(food);
    }

    public LiveData<List<Food>> getAllFood() {
        return mFoodDao.getAllFood();
    }

    public LiveData<Food> getFoodByID(long ndbno) {
        return mFoodDao.getFoodByID(Long.toString(ndbno));
    }

    private static class InsertAsyncTask extends AsyncTask<Food, Void, Void> {
        private FoodDao mAsyncTaskDao;
        InsertAsyncTask(FoodDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Food... foods) {
            mAsyncTaskDao.insert(foods[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Food, Void, Void> {
        private FoodDao mAsyncTaskDao;
        DeleteAsyncTask(FoodDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Food... savedFoods) {
            mAsyncTaskDao.delete(savedFoods[0]);
            return null;
        }
    }
}
