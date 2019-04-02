package com.example.android.nutrientInfo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.nutrientInfo.data.Food;

import java.util.ArrayList;

public class FoodSearchAdapter extends RecyclerView.Adapter<FoodSearchAdapter.SearchResultViewHolder> {
    private ArrayList<Food> mFoods;
    OnSearchItemClickListener mSeachItemClickListener;

    public interface OnSearchItemClickListener {
        void onSearchItemClick(Food food);
    }

    FoodSearchAdapter(OnSearchItemClickListener searchItemClickListener) {
        mSeachItemClickListener = searchItemClickListener;
    }

    public void updateSearchResults(ArrayList<Food> foods) {
        mFoods = foods;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mFoods != null) {
            return mFoods.size();
        } else {
            return 0;
        }
    }

    @NonNull
    @Override
    public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.search_result_item, parent, false);
        return new SearchResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultViewHolder holder, int position) {
        holder.bind(mFoods.get(position));
    }

    class SearchResultViewHolder extends RecyclerView.ViewHolder {
        private TextView mSearchResultTV;

        public SearchResultViewHolder(View itemView) {
            super(itemView);
            mSearchResultTV = itemView.findViewById(R.id.tv_search_result);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Food searchResult = mFoods.get(getAdapterPosition());
                    mSeachItemClickListener.onSearchItemClick(searchResult);
                }
            });
        }

        public void bind(Food food) {
            mSearchResultTV.setText(food.name);
        }
    }
}