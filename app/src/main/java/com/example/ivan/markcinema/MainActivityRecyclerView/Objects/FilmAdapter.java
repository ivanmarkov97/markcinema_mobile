package com.example.ivan.markcinema.MainActivityRecyclerView.Objects;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ivan.markcinema.MainActivityRecyclerView.Objects.Objects.Film;
import com.example.ivan.markcinema.R;
import com.example.ivan.markcinema.ShowFilmActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.support.v7.recyclerview.R.styleable.RecyclerView;

/**
 * Created by Ivan on 27.12.2017.
 */

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.FilmViewHolder> {

    ArrayList<Film> films;
    Context context;

    int [] urls = {
        R.drawable.thor_ava,
        R.drawable.avangers,
            R.drawable.logan_ava,
            R.drawable.move_up
    };
    public FilmAdapter(ArrayList<Film> films, Context context) {
        this.films = films;
        this.context = context;
    }

    @Override
    public FilmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.film_item, parent, false);
        return new FilmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FilmViewHolder holder, final int position) {
        final Film film = films.get(position);
        holder.filmName.setText(film.getName());
        holder.filmDuration.setText(String.valueOf(film.getDuration()) + " мин.");
        holder.filmAge.setText(String.valueOf(film.getAge()) + "+");
        holder.filmGenre.setText(film.getGenre());

        Picasso.with(context)
                //.load("http://10.0.3.2:8000" + film.getImage())
                .load(urls[position])
                .error(R.mipmap.ic_launcher)
                .into(holder.imageView);



        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShowFilmActivity.class);
                intent.putExtra("film_id", film.getId());
                intent.putExtra("pos", position);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return films.size();
    }

    public class FilmViewHolder extends RecyclerView.ViewHolder {

        public TextView filmName;
        public TextView filmAge;
        public TextView filmDuration;
        public TextView filmGenre;
        public ImageView imageView;

        public FilmViewHolder(View itemView) {
            super(itemView);
            filmName = (TextView) itemView.findViewById(R.id.film_item_name);
            filmDuration = (TextView) itemView.findViewById(R.id.film_item_film_duration);
            imageView = (ImageView) itemView.findViewById(R.id.film_item_film_image);
            filmAge = (TextView) itemView.findViewById(R.id.film_item_film_age);
            filmGenre = (TextView) itemView.findViewById(R.id.film_item_film_genre);
        }
    }
}
