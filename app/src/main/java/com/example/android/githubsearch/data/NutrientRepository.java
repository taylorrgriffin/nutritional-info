package com.example.android.githubsearch.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.ArrayList;
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

    public LiveData<ArrayList<Nutrient>> getNutrientsByID(long ndbno) {
        return mNutrientDao.getNutrientsByID(Long.toString(ndbno));
    }

    private static class InsertAsyncTask extends AsyncTask<Nutrient, Void, Void> {
        private NutrientDao mAsyncTaskDao;
        InsertAsyncTask(NutrientDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Nutrient... gitHubRepos) {
            mAsyncTaskDao.insert(gitHubRepos[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Nutrient, Void, Void> {
        private NutrientDao mAsyncTaskDao;
        DeleteAsyncTask(NutrientDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Nutrient... gitHubRepos) {
            mAsyncTaskDao.delete(gitHubRepos[0]);
            return null;
        }
    }
}
