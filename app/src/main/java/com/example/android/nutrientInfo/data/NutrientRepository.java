package com.example.android.nutrientInfo.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class NutrientRepository {
    private NutrientDao mNutrientDao;

    public NutrientRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mNutrientDao = db.NutrientDao();
    }

    public void insertNutrient(Nutrient nutrient) {
        new InsertAsyncTask(mNutrientDao).execute(nutrient);
    }

    public void deleteNutrient(Nutrient nutrient) {
        new DeleteAsyncTask(mNutrientDao).execute(nutrient);
    }

    public LiveData<List<Nutrient>> getNutrientsByID(long ndbno) {
        return mNutrientDao.getNutrientsByID(Long.toString(ndbno));
    }

    private static class InsertAsyncTask extends AsyncTask<Nutrient, Void, Void> {
        private NutrientDao mAsyncTaskDao;
        InsertAsyncTask(NutrientDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Nutrient... nutrients) {
            mAsyncTaskDao.insert(nutrients[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Nutrient, Void, Void> {
        private NutrientDao mAsyncTaskDao;
        DeleteAsyncTask(NutrientDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Nutrient... savedNutrients) {
            mAsyncTaskDao.delete(savedNutrients[0]);
            return null;
        }
    }
}
