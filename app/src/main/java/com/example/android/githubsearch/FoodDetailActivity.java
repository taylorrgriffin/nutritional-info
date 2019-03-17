package com.example.android.githubsearch;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.githubsearch.data.Food;
import com.example.android.githubsearch.utils.NutUtils;

public class FoodDetailActivity extends AppCompatActivity {
    private TextView mFoodNameTV;
    private ImageView mFoodBookmarkIV;

    private FoodViewModel mGitHubRepoViewModel;
    private Food mFood;
    private boolean mIsSaved = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        mFoodNameTV = findViewById(R.id.tv_repo_name);
        mFoodBookmarkIV = findViewById(R.id.iv_repo_bookmark);

        mGitHubRepoViewModel = ViewModelProviders.of(this).get(FoodViewModel.class);

        mFood = null;
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(NutUtils.FOOD_BUNDLE_ID)) {
            mFood = (Food) intent.getSerializableExtra(NutUtils.FOOD_BUNDLE_ID);
            mFoodNameTV.setText(mFood.name);
            mGitHubRepoViewModel.getFoodByID(mFood.ndbno).observe(this, new Observer<Food>() {
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
        }

        mFoodBookmarkIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFood != null) {
                    if (!mIsSaved) {
                        mGitHubRepoViewModel.insertFoodRepo(mFood);
                    } else {
                        mGitHubRepoViewModel.deleteFoodRepo(mFood);
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
            String shareText = getString(R.string.share_food_text,mFood.name);
            ShareCompat.IntentBuilder.from(this)
                    .setType("text/plain")
                    .setText(shareText)
                    .setChooserTitle(R.string.share_chooser_title)
                    .startChooser();
        }
    }
}
