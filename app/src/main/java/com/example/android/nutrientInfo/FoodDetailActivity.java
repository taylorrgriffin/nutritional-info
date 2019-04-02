package com.example.android.nutrientInfo;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.nutrientInfo.data.Food;
import com.example.android.nutrientInfo.data.Nutrient;
import com.example.android.nutrientInfo.utils.NutUtils;

import java.util.List;

public class FoodDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private static final String TAG = FoodDetailActivity.class.getSimpleName();
    private static final String SEARCH_URL_KEY = "searchurlkey";

    private List<Nutrient> mNutrients;

    private TextView mFoodNameTV;
    private TextView mFoodEnergyTV;
    private TextView mFoodCarbsTV;
    private TextView mFoodSugarTV;
    private TextView mFoodFatTV;
    private TextView mFoodProteinTV;

    private ImageView mFoodBookmarkIV;
    private TextView mLoadingErrorTV;

    private ProgressBar mLoadingPB;

    private FoodViewModel mFoodViewModel;
    private Food mFood;
    private boolean mIsSaved = false;

    private static final int NUT_SEARCH_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        mFoodNameTV = findViewById(R.id.tv_repo_name);
        mFoodEnergyTV = findViewById(R.id.tv_food_energy);
        mFoodSugarTV = findViewById(R.id.tv_food_sugar);
        mFoodFatTV = findViewById(R.id.tv_food_fat);
        mFoodCarbsTV = findViewById(R.id.tv_food_carbs);
        mFoodProteinTV = findViewById(R.id.tv_food_protein);

        mLoadingErrorTV =  findViewById(R.id.tv_loading_error);
        mLoadingPB = findViewById(R.id.pb_loading);

        mFoodBookmarkIV = findViewById(R.id.iv_repo_bookmark);

        mFoodViewModel = ViewModelProviders.of(this).get(FoodViewModel.class);

        mFood = null;
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(NutUtils.FOOD_BUNDLE_ID)) {
            mFood = (Food) intent.getSerializableExtra(NutUtils.FOOD_BUNDLE_ID);
            mFoodNameTV.setText(mFood.name);
            mFoodViewModel.getFoodByID(mFood.ndbno).observe(this, new Observer<Food>() {
                @Override
                public void onChanged(@Nullable Food food) {
                    if (food != null) {
                        mIsSaved = true;
                        mFoodBookmarkIV.setImageResource(R.drawable.ic_bookmark_black_24dp);
                    }
                    else {
                        mIsSaved = false;
                        mFoodBookmarkIV.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
                    }
                }
            });
            final FoodDetailActivity activity = this;
            mFoodViewModel.getNutrientsByID(mFood.ndbno).observe(this, new Observer<List<Nutrient>>() {
                @Override
                public void onChanged(@Nullable List<Nutrient> nutrients) {
                    if ((nutrients != null) && (!nutrients.isEmpty())) {
                        fillNutrients(nutrients);
                    }
                    else {
                        toggleVisibility(false);
                        String url = NutUtils.buildNutrientSearchURL(mFood.ndbno);
                        Log.d(TAG, "querying search URL: " + url);
                        Bundle args = new Bundle();
                        args.putString(SEARCH_URL_KEY, url);
                        getSupportLoaderManager().restartLoader(NUT_SEARCH_LOADER_ID, args, activity);
                    }
                }
            });

        }

        mFoodBookmarkIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFood != null) {
                    if (!mIsSaved) {
                        mFoodViewModel.insertFood(mFood);
                        for (Nutrient nut : mNutrients) {
                            nut.ndbno = mFood.ndbno;
                            mFoodViewModel.insertNutrient(nut);
                        }
                    }
                    else {
                        mFoodViewModel.deleteFood(mFood);
                        for (Nutrient nut : mNutrients) {
                            mFoodViewModel.deleteNutrient(nut);
                        }
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.repo_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                shareRepo();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void shareRepo() {
        if (mFood != null) {
            String shareText = getString(R.string.share_food_text,mFood.name+", "+mFoodSugarTV.getText()+", "+mFoodEnergyTV.getText()+", "+mFoodFatTV.getText()+", "+mFoodCarbsTV.getText());
            ShareCompat.IntentBuilder.from(this)
                    .setType("text/plain")
                    .setText(shareText)
                    .setChooserTitle(R.string.share_chooser_title)
                    .startChooser();
        }
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int i, @Nullable Bundle bundle) {
        String url = null;
        if (bundle != null) {
            url = bundle.getString(SEARCH_URL_KEY);
        }
        return new NutrientLoader(this, url);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String s) {
        Log.d(TAG, "loader finished loading");
        if (s != null) {
            mLoadingErrorTV.setVisibility(View.INVISIBLE);
            mNutrients = NutUtils.parseNutrientResults(s);
            fillNutrients(mNutrients);
            toggleVisibility(true);
        } else {
            mLoadingErrorTV.setVisibility(View.VISIBLE);
            toggleVisibility(false);
            mLoadingPB.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        // Nothing to do here...
    }

    private void fillNutrients(List<Nutrient> nutrients) {
        if (nutrients == null) {
            return;
        }
        for (Nutrient nut : nutrients) {
            if (nut.name.equals("Sugars, total")) {
                mFoodSugarTV.setText(nut.value+" "+nut.unit);
            }
            else if (nut.name.equals("Energy")) {
                mFoodEnergyTV.setText(nut.value+" "+nut.unit);
            }
            else if (nut.name.equals("Total lipid (fat)")) {
                mFoodFatTV.setText(nut.value+" "+nut.unit);
            }
            else if (nut.name.equals("Carbohydrate, by difference")) {
                mFoodCarbsTV.setText(nut.value+" "+nut.unit);
            }
            else if (nut.name.equals("Protein")) {
                mFoodProteinTV.setText(nut.value+" "+nut.unit);
            }
        }
    }

    private void toggleVisibility(boolean isLoaded) {
        if (isLoaded) {
            mLoadingPB.setVisibility(View.INVISIBLE);

            mFoodNameTV.setVisibility(View.VISIBLE);
            mFoodEnergyTV.setVisibility(View.VISIBLE);
            mFoodCarbsTV.setVisibility(View.VISIBLE);
            mFoodSugarTV.setVisibility(View.VISIBLE);
            mFoodFatTV.setVisibility(View.VISIBLE);
            mFoodProteinTV.setVisibility(View.VISIBLE);

            mFoodBookmarkIV.setVisibility(View.VISIBLE);
        }
        else {
            mLoadingPB.setVisibility(View.VISIBLE);

            mFoodNameTV.setVisibility(View.INVISIBLE);
            mFoodEnergyTV.setVisibility(View.INVISIBLE);
            mFoodCarbsTV.setVisibility(View.INVISIBLE);
            mFoodSugarTV.setVisibility(View.INVISIBLE);
            mFoodFatTV.setVisibility(View.INVISIBLE);
            mFoodProteinTV.setVisibility(View.INVISIBLE);

            mFoodBookmarkIV.setVisibility(View.INVISIBLE);
        }
    }
}
