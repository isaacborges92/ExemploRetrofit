package com.example.isaac.exemploretrofit.communication;

import com.example.isaac.exemploretrofit.models.ResultTopRated;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Isaac on 15/08/2017.
 */

public interface RequestApi {

    //https://api.themoviedb.org/3/movie/top_rated?api_key=e087efa69ec3f733e1c3c8cb21c4c442&language=en-US&page=2

    @GET("top_rated")
    Call<ResultTopRated> getTopRated(@Query("api_key") String api_key,
                                     @Query("language") String language,
                                     @Query("page") String page);

}
