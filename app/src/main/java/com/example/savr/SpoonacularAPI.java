package com.example.savr;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SpoonacularAPI {
    @GET("food/ingredients/search")
    Call<APIResponse> searchIngredients(
            @Query("query") String query,
            @Query("apiKey") String apiKey
    );
}
