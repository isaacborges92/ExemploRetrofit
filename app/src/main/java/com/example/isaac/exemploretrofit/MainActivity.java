package com.example.isaac.exemploretrofit;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.isaac.exemploretrofit.communication.ParamRequest;
import com.example.isaac.exemploretrofit.communication.RequestApi;
import com.example.isaac.exemploretrofit.communication.RetrofitAsyncTask;
import com.example.isaac.exemploretrofit.models.Filme;
import com.example.isaac.exemploretrofit.models.ResultTopRated;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements RetrofitAsyncTask.GetTopRatedListener{

    private static final int PAGINA_INICIAL = 1;
    public static String total_paginas;

    private List<Filme> filmeList;
    private FilmeAdapter filmeAdapter;
    private ListView listView;
    private ImageButton btnProximo, btnAnterior;
    private TextView txtPagina;
    private int pag;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            MainActivity.this.receivedBroadcast(intent);
        }
    };

    private void receivedBroadcast(Intent i) {
        enviaRequisicao(pag);
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter iff = new IntentFilter();
        iff.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        this.registerReceiver(this.mBroadcastReceiver,iff);
    }

    @Override
    public void onPause() {
        super.onPause();
        this.unregisterReceiver(this.mBroadcastReceiver);
    }

    private void setAdapter(FilmeAdapter filmeAdapter){
        listView.setAdapter(filmeAdapter);
        txtPagina.setText(getResources().getString(R.string.pag) + " " + pag + " " +
                          getResources().getString(R.string.de) + " " + total_paginas);
    }

    private void exibeLoad(final ProgressDialog dialog, final int pagina){
            Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    super.run();
                    ParamRequest paramRequest = new ParamRequest();
                    paramRequest.page = String.valueOf(pagina);
                    paramRequest.language = getResources().getString(R.string.linguagem);
                    paramRequest.api_key = "e087efa69ec3f733e1c3c8cb21c4c442";
                    RetrofitAsyncTask task = new RetrofitAsyncTask(MainActivity.this);
                    task.execute(paramRequest);
                    dialog.dismiss();
                } catch (Exception e) {

                }
            }
        };
        thread.start();
    }

    private void enviaRequisicao(int pagina){
        if(Util.isOnline(MainActivity.this)) {
            ProgressDialog dialog = Util.showDialog(MainActivity.this);
            dialog.show();
            exibeLoad(dialog, pagina);
        }else{
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.conectado), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.lstFilmes);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(Util.isOnline(MainActivity.this)) {
                    Filme filme = (Filme) parent.getItemAtPosition(position); //retornaItem(position);
                    Intent intent = new Intent(MainActivity.this, Informacoes.class);
                    intent.putExtra("idFilme", filme.getId());

                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }else{
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.conectado), Toast.LENGTH_SHORT).show();
                }
            }
        });

        txtPagina = (TextView)findViewById(R.id.txtPagina);
        btnProximo = (ImageButton)findViewById(R.id.btnProximo);
        btnProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pag < Integer.parseInt(total_paginas)){
                    pag += 1;
                    enviaRequisicao(pag);
                }
            }
        });

        btnAnterior = (ImageButton)findViewById(R.id.btnAnterior);
        btnAnterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pag > PAGINA_INICIAL) {
                    pag -= 1;
                    enviaRequisicao(pag);
                }
            }
        });

        if(savedInstanceState == null) {
            pag = PAGINA_INICIAL;
            enviaRequisicao(pag);
        }
    }

    @Override
    public void onTopRatedResult(List<Filme> filmes) {
        if (filmes != null) {
            filmeList = new ArrayList<>();
            for (Filme filme : filmes) {
                filmeList.add(filme);
            }

            filmeAdapter = new FilmeAdapter(getApplicationContext(), R.layout.item_filme, filmeList);
            setAdapter(filmeAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_minha_lista:
                Intent intent = new Intent(MainActivity.this, MinhaLista.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("lista", (Serializable)filmeList);
        outState.putSerializable("adapter", (Serializable)filmeAdapter);
        outState.putInt("pagina", pag);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        filmeList = (List<Filme>) savedInstanceState.getSerializable("lista");
        filmeAdapter = (FilmeAdapter) savedInstanceState.getSerializable("adapter");
        pag = savedInstanceState.getInt("pagina");
        setAdapter(filmeAdapter);
    }
}
