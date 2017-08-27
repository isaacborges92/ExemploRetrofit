package com.example.isaac.exemploretrofit;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.isaac.exemploretrofit.models.Filme;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Isaac on 18/08/2017.
 */

public class FilmeAdapter extends ArrayAdapter<Filme> implements Serializable{

        public FilmeAdapter(@NonNull Context context,
                            @LayoutRes int resource,
                            @NonNull List<Filme> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            Filme filme = getItem(position);
            if (convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_filme, parent, false);
            }

            TextView txtTitle = (TextView)convertView.findViewById(R.id.txtTitle);
            TextView txtYear  = (TextView)convertView.findViewById(R.id.txtYear);
            ImageView imagem  = (ImageView)convertView.findViewById(R.id.imagem);

            txtTitle.setText(convertView.getResources().getString(R.string.titulo) + " " + filme.getTitle());
            txtYear.setText(convertView.getResources().getString(R.string.data) + " " + filme.release_date);

            Picasso.with(getContext())
                    .load("http://image.tmdb.org/t/p/w300/" + filme.getPoster_path())
                    .fit()
                    .centerCrop()
                    .into(imagem);

            return convertView;
        }
}
