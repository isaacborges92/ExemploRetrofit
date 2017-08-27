package com.example.isaac.exemploretrofit.communication;

import android.os.AsyncTask;

import com.example.isaac.exemploretrofit.models.Filme;

import java.io.IOException;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Isaac on 18/08/2017.
 */
public class RetrofitAsyncTaskDetails extends AsyncTask<ParamRequest, Void, Filme>{

    private GetDetailsFilme getDetailsFilme;

    public RetrofitAsyncTaskDetails(RetrofitAsyncTaskDetails.GetDetailsFilme listener){
        this.getDetailsFilme = listener;
    }

    @Override
    protected Filme doInBackground(ParamRequest... param) {

        ParamRequest parameter = param[0];
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/movie/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestDetails api = retrofit.create(RequestDetails.class);

        try {
            Response<Filme> response =
                    api.getDetails( parameter.idFilme,
                                    parameter.api_key,
                                    parameter.language).execute();

            if (response.body() != null) {
                return response.body();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Filme filme) {
        super.onPostExecute(filme);
        //Retornar a lista de artigos
        if (getDetailsFilme != null) {
            getDetailsFilme.onDetailsResult(filme);
        }
    }

    public interface GetDetailsFilme{
        void onDetailsResult(Filme filme);
    }
}
