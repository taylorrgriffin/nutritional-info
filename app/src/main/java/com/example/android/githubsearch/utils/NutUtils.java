package com.example.android.githubsearch.utils;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.example.android.githubsearch.data.Food;
import com.example.android.githubsearch.data.Nutrient;
import com.google.gson.Gson;

import java.util.ArrayList;

public class NutUtils {
    private final static String NUT_SEARCHLIST_BASE_URL = "https://api.nal.usda.gov/ndb/search";
    private final static String NUT_SEARCHLIST_QUERY_PARAM = "q";
    private final static String NUT_SEARCHLIST_SORT_PARAM = "sort";
    private final static String NUT_SEARCHLIST_SORT_VALUE = "n";
    private final static String NUT_SEARCHLIST_MAX_PARAM = "max";
    private final static String NUT_SEARCHLIST_MAX_VALUE = "25";
    private final static String NUT_SEARCHLIST_OFFSET_PARAM = "offset";
    private final static String NUT_SEARCHLIST_OFFSET_VALUE = "0";

    private final static String NUT_SEARCHDETAIL_BASE_URL = "https://api.nal.usda.gov/ndb/V2/reports";
    private final static String NUT_SEARCHDETAIl_ID_PARAM = "ndbno";
    private final static String NUT_SEARCHDETAIL_TYPE_PARAM = "type";
    private final static String NUT_SEARCHDETAIL_TYPE_VALUE = "b";

    private final static String NUT_SEARCH_FORMAT_PARAM = "format";
    private final static String NUT_SEARCH_FORMAT_VALUE = "json";
    private final static String NUT_SEARCH_APIKEY_PARAM = "api_key";
    private final static String NUT_SEARCH_APIKEY_VALUE = "GwQKRnpJWfjSPP9Gtp0mjGPuAuvVWpsmmkj9A2qv";

    public final static String FOOD_BUNDLE_ID = "food_id";

    public static class FoodSearchResults {
        public FoodSearchResult list;
    }

    public static class FoodSearchResult {
        public ArrayList<Food> item;
    }

    public static class NutrientSearchResults {
        public ArrayList<NutrientSearchResult> foods;
    }

    public static class NutrientSearchResult {
        public ArrayList<NutrientSearchResultFood> food;
    }

    public static class NutrientSearchResultFood {
        public ArrayList<Nutrient> nutrients;
    }

    public static String buildFoodSearchURL(String foodName) {
        return Uri.parse(NUT_SEARCHLIST_BASE_URL).buildUpon()
                .appendQueryParameter(NUT_SEARCH_FORMAT_PARAM, NUT_SEARCH_FORMAT_VALUE)
                .appendQueryParameter(NUT_SEARCHLIST_QUERY_PARAM, foodName)
                .appendQueryParameter(NUT_SEARCHLIST_SORT_PARAM, NUT_SEARCHLIST_SORT_VALUE)
                .appendQueryParameter(NUT_SEARCHLIST_MAX_PARAM, NUT_SEARCHLIST_MAX_VALUE)
                .appendQueryParameter(NUT_SEARCHLIST_OFFSET_PARAM, NUT_SEARCHLIST_OFFSET_VALUE)
                .appendQueryParameter(NUT_SEARCH_APIKEY_PARAM,NUT_SEARCH_APIKEY_VALUE)
                .build()
                .toString();
    }

    public static String buildNutrientSearchURL(long ndbno) {
        return Uri.parse(NUT_SEARCHDETAIL_BASE_URL).buildUpon()
                .appendQueryParameter(NUT_SEARCHDETAIl_ID_PARAM, Long.toString(ndbno))
                .appendQueryParameter(NUT_SEARCHDETAIL_TYPE_PARAM, NUT_SEARCHDETAIL_TYPE_VALUE)
                .appendQueryParameter(NUT_SEARCH_FORMAT_PARAM, NUT_SEARCH_FORMAT_VALUE)
                .appendQueryParameter(NUT_SEARCH_APIKEY_PARAM,NUT_SEARCH_APIKEY_VALUE)
                .build()
                .toString();
    }

    public static ArrayList<Food> parseFoodResults(String json) {
        Gson gson = new Gson();
        FoodSearchResults results = gson.fromJson(json, FoodSearchResults.class);
        if (results != null && results.list != null && results.list.item != null) {
            for(Food food : results.list.item) {
                food.name = food.name.substring(0,food.name.indexOf(", UPC"));
            }
            return results.list.item;
        }
        else {
            return null;
        }
    }

    public static ArrayList<Nutrient> parseNutrientResults(String json) {
        Gson gson = new Gson();
        NutrientSearchResults results = gson.fromJson(json, NutrientSearchResults.class);
        if(results != null
                && results.foods != null
                && !results.foods.isEmpty()
                && !results.foods.get(0).food.isEmpty())
        {
            ArrayList<Nutrient> listNut=new ArrayList<Nutrient>();
            for(Nutrient nut : results.foods.get(0).food.get(0).nutrients) {
                if(nut.name.equals("Sugars, total")
                    || nut.name.equals("Energy")
                    || nut.name.equals("Total lipid (fat)")
                    || nut.name.equals("Carbohydrate, by difference"))
                {
                    listNut.add((nut));
                }
            }
            return listNut;
        }
        else {
            return null;
        }
    }
}
