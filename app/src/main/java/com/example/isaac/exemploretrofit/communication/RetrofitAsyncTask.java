package com.example.isaac.exemploretrofit.communication;

import android.os.AsyncTask;

import com.example.isaac.exemploretrofit.MainActivity;
import com.example.isaac.exemploretrofit.models.Filme;
import com.example.isaac.exemploretrofit.models.ResultTopRated;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Isaac on 18/08/2017.
 */

public class RetrofitAsyncTask extends AsyncTask<ParamRequest, Void, List<Filme>> {

    private GetTopRatedListener getTopRatedListener;

    public RetrofitAsyncTask(GetTopRatedListener listener) {
        this.getTopRatedListener = listener;
    }

    @Override
    protected List<Filme> doInBackground(ParamRequest... param) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/movie/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestApi api = retrofit.create(RequestApi.class);
        ParamRequest parameter = param[0];

        try {
            Response<ResultTopRated> response =
                    api.getTopRated(parameter.api_key,
                                    parameter.language,
                                    parameter.page).execute();

            if (response.body() != null) {
                MainActivity.total_paginas = response.body().getTotal_pages();
                return response.body().getFilmes();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Filme> filmes){
        super.onPostExecute(filmes);
        //Retornar a lista de artigos
        if (getTopRatedListener != null) {
            getTopRatedListener.onTopRatedResult(filmes);
        }
    }

    public interface GetTopRatedListener {
        void onTopRatedResult(List<Filme> filmes);
    }
}
