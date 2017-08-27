package com.example.isaac.exemploretrofit;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.isaac.exemploretrofit.communication.ParamRequest;
import com.example.isaac.exemploretrofit.communication.RetrofitAsyncTask;
import com.example.isaac.exemploretrofit.communication.RetrofitAsyncTaskDetails;
import com.example.isaac.exemploretrofit.models.Filme;
import com.example.isaac.exemploretrofit.models.Genero;
import com.example.isaac.exemploretrofit.models.MeuOpenHelper;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Informacoes extends AppCompatActivity implements RetrofitAsyncTaskDetails.GetDetailsFilme{

    private ImageView img;
    private TextView txtSinopse, txtDuracao, txtGenero;
    private Button btnAdd;

    private static final String url_base_imagem = "http://image.tmdb.org/t/p/w342/";
    private String posterPath;
    private MeuOpenHelper meuOpenHelper;

    private Filme filmeAux; //Recebe o objeto filme do onDetailsResult para utilizar no saveInstance

    public void configuraActivity(Filme filme){
        getSupportActionBar().setTitle(filme.getTitle());
        posterPath = filme.getPoster_path();

        Picasso.with(getApplicationContext())
                .load(url_base_imagem + posterPath)
                .fit()
                .into(img);

        txtSinopse.setText(filme.getOverview());
        txtDuracao.setText(getResources().getString(R.string.duracao) + " " + filme.getRuntime() + " min");

        for(Genero genero: filme.getGenres()){
            if(txtGenero.getText().toString().trim().length() <= 0) {
                txtGenero.setText(getResources().getString(R.string.genero) + " " + genero.getName());
            }else{
                txtGenero.setText(txtGenero.getText().toString() + ", " + genero.getName());
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacoes);

        img = (ImageView)findViewById(R.id.img);
        txtSinopse = (TextView)findViewById(R.id.sinopse);
        txtDuracao = (TextView)findViewById(R.id.duracao);
        txtGenero  = (TextView)findViewById(R.id.genero);
        btnAdd     = (Button)findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                meuOpenHelper = new MeuOpenHelper(getApplicationContext());
                SQLiteDatabase db = meuOpenHelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();

                contentValues.put("title", filmeAux.getTitle());
                contentValues.put("original_title", filmeAux.getOriginal_title());
                contentValues.put("poster_path", filmeAux.getPoster_path());
                contentValues.put("release_date", filmeAux.getRelease_date());
                contentValues.put("overview", filmeAux.getOverview());
                contentValues.put("runtime", filmeAux.getRuntime());
                contentValues.put("cod_filme", filmeAux.getId());
                contentValues.put("visto", "0");

                long idFilme = db.insert("filmes", null, contentValues);
                db.close();
                Toast.makeText(Informacoes.this, getResources().getString(R.string.adicionado), Toast.LENGTH_SHORT).show();
            }
        });

        if(savedInstanceState == null && Util.isOnline(getApplicationContext())){
            ParamRequest paramRequest = new ParamRequest();
            paramRequest.idFilme = getIntent().getStringExtra("idFilme");
            paramRequest.api_key = "e087efa69ec3f733e1c3c8cb21c4c442";
            paramRequest.language = getResources().getString(R.string.linguagem);

            RetrofitAsyncTaskDetails task = new RetrofitAsyncTaskDetails(this);
            task.execute(paramRequest);
        }
    }

    @Override
    public void onDetailsResult (Filme filme) {
        if(filme != null){
            configuraActivity(filme);
            filmeAux = filme;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("filme", filmeAux);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        filmeAux = (Filme)savedInstanceState.getSerializable("filme");
        configuraActivity(filmeAux);
    }
}
