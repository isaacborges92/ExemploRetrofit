package com.example.isaac.exemploretrofit;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.isaac.exemploretrofit.models.Filme;
import com.example.isaac.exemploretrofit.models.MeuOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class MinhaLista extends AppCompatActivity {

    private MeuOpenHelper meuOpenHelper;
    private ListView listView;
    private FilmeAdapter filmeAdapter;

    private void setAdapter(FilmeAdapter filmeAdapter){
        listView.setAdapter(filmeAdapter);
    }

    private void consulta(){
        meuOpenHelper = new MeuOpenHelper(getApplicationContext());
        SQLiteDatabase db = meuOpenHelper.getWritableDatabase();

        Cursor c = db.rawQuery("select * from filmes", null);
        c.moveToFirst();

        List<Filme> lstFilmes = new ArrayList();
        do{
            Filme filme = new Filme(c.getString(c.getColumnIndex("cod_filme")),
                    c.getString(c.getColumnIndex("title")),
                    c.getString(c.getColumnIndex("original_title")),
                    c.getString(c.getColumnIndex("poster_path")),
                    c.getString(c.getColumnIndex("release_date")),
                    c.getString(c.getColumnIndex("overview")),
                    c.getString(c.getColumnIndex("runtime")));
            lstFilmes.add(filme);
        }while(c.moveToNext());
        filmeAdapter = new FilmeAdapter(getApplicationContext(), R.layout.item_filme, lstFilmes);
        setAdapter(filmeAdapter);
    }

    private void excluirItem(Filme filme){
        SQLiteDatabase db = meuOpenHelper.getWritableDatabase();
        String whereClause = "cod_filme = ?";
        String cod = filme.getId();
        String[] whereArgs = {cod};

        db.delete("filmes", whereClause, whereArgs);
        consulta();
    }

    private void atualizarItem(Filme filme){
        SQLiteDatabase db = meuOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("visto", "1");

        String where = "cod_filme = ? ";
        String[] whereArg = {filme.getId()};

        db.update("filmes", contentValues, where, whereArg);
        consulta();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minha_lista);

        getSupportActionBar().setTitle(R.string.lista);
        listView = (ListView)findViewById(R.id.lstMeusFilmes);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Filme filme = (Filme)parent.getItemAtPosition(position);
                atualizarItem(filme);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                final Filme filme   = (Filme) parent.getItemAtPosition(position);

                AlertDialog alert = new AlertDialog.Builder(MinhaLista.this)
                        .setTitle(getResources().getString(R.string.excluir))
                        .setMessage(getResources().getString(R.string.excluir_msg))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                excluirItem(filme);
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
                return false;
            }
        });

        consulta();
    }
}
