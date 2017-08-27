package com.example.isaac.exemploretrofit.communication;

import com.example.isaac.exemploretrofit.models.Filme;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Isaac on 18/08/2017.
 */

public interface RequestDetails {

    @GET("{id}")
    Call<Filme> getDetails (@Path("id") String idFilme,
                            @Query("api_key") String api_key,
                            @Query("language") String language);

}
