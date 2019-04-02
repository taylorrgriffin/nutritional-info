package com.example.android.nutrientInfo;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.android.nutrientInfo.data.Food;
import com.example.android.nutrientInfo.utils.NutUtils;

import java.util.ArrayList;
import java.util.List;

public class SavedFoodsActivity extends AppCompatActivity implements FoodSearchAdapter.OnSearchItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_food_results);

        RecyclerView savedFoodRV = findViewById(R.id.rv_saved_foods);
        savedFoodRV.setLayoutManager(new LinearLayoutManager(this));
        savedFoodRV.setHasFixedSize(true);

        final FoodSearchAdapter adapter = new FoodSearchAdapter(this);
        savedFoodRV.setAdapter(adapter);

        FoodViewModel viewModel = ViewModelProviders.of(this).get(FoodViewModel.class);
        viewModel.getAllFood().observe(this, new Observer<List<Food>>() {
            @Override
            public void onChanged(@Nullable List<Food> foods) {
                adapter.updateSearchResults(new ArrayList<>(foods));
            }
        });
    }

    @Override
    public void onSearchItemClick(Food food) {
        Intent intent = new Intent(this, FoodDetailActivity.class);
        intent.putExtra(NutUtils.FOOD_BUNDLE_ID, food);
        startActivity(intent);
    }
}
