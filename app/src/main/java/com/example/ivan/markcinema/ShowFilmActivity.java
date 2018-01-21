package com.example.ivan.markcinema;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ivan.markcinema.MainActivityRecyclerView.Objects.Objects.Film;
import com.example.ivan.markcinema.MainActivityRecyclerView.Objects.Objects.Seance;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ShowFilmActivity extends AppCompatActivity {

    Intent intent;
    int film_id = 0;
    OkHttpClient client = new OkHttpClient();
    RequestBody requestBody;
    Request requestFilm;
    Request requestSeance;
    ProgressDialog progressDialog;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    ArrayList<Seance> seances = new ArrayList<>();
    Film film;
    ImageView imageView;
    OkHttpHandlerFilmLoad okHttpHandlerFilmLoad;
    OkHttpHandlerSeanceLoad okHttpHandlerSeanceLoad;
    LinearLayout linearLayoutContainer;
    int [] urls = {
            R.drawable.thor_ava,
            R.drawable.avangers,
            R.drawable.logan_ava,
            R.drawable.move_up
    };

    int url = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_film);

        imageView = (ImageView) findViewById(R.id.main_backdrop);
        linearLayoutContainer = (LinearLayout) findViewById(R.id.show_film_linear_container);

        intent = getIntent();
        film_id = intent.getIntExtra("film_id", 0);

        url = intent.getIntExtra("pos", 0);

        requestFilm = new Request.Builder()
                .url("http://10.0.3.2:8000/films/" + film_id)
                .get()
                .build();

        requestSeance = new Request.Builder()
                .url("http://10.0.3.2:8000/seances/")
                .get()
                .build();

        okHttpHandlerFilmLoad = new OkHttpHandlerFilmLoad(getApplicationContext());
        okHttpHandlerFilmLoad.execute();

        okHttpHandlerSeanceLoad = new OkHttpHandlerSeanceLoad(getApplicationContext());

    }

    class OkHttpHandlerSeanceLoad extends AsyncTask<String, String, String>{

        Context context;
        int id = 0;
        int[] ids;
        String[] times;
        int cost = 0;
        int[] costs;
        String hall = "";
        String[] halls;
        String mfilm = "";
        String[] mfilms;
        JSONObject reader;

        public OkHttpHandlerSeanceLoad(Context context) {
            this.context = context;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                reader = new JSONObject(s);
                JSONArray data = reader.getJSONArray("data");
                times = new String[data.length()];
                ids = new int[data.length()];
                costs = new int[data.length()];
                halls = new String[data.length()];
                mfilms = new String[data.length()];
                LayoutInflater inflater = getLayoutInflater();
                for(int i = 0; i < data.length(); i++){
                    try{
                        id = data.getJSONObject(i).getInt("id");
                        ids[i] = data.getJSONObject(i).getInt("id");
                        times[i] = data.getJSONObject(i).getString("time");
                        cost = data.getJSONObject(i).getInt("cost");
                        costs[i] = data.getJSONObject(i).getInt("cost");
                        hall = data.getJSONObject(i).getString("hall");
                        halls[i] = data.getJSONObject(i).getString("hall");
                        mfilm = data.getJSONObject(i).getString("film");
                        mfilms[i] = data.getJSONObject(i).getString("film");

                        Log.d("MyTAG", halls[i]);

                        if(film.getName().equals(mfilms[i])) {

                            Log.d("MyTAG", "My HALL == " + halls[i]);

                            seances.add(new Seance(
                                    /*id,
                                    times[i],
                                    cost,
                                    hall,
                                    mfilm*/
                                    ids[i],
                                    times[i],
                                    costs[i],
                                    halls[i],
                                    mfilms[i]
                            ));

                            final int j = i;
                            Log.d("MyTAG", "MY J " + j);
                            Log.d("MyTAG", "MY J HALL " + halls[j]);;

                            View view = inflater.inflate(R.layout.seance_item, linearLayoutContainer, false);
                            view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.d("MyTAG", "seance_id " + ids[j]);
                                    Intent intent = new Intent(getApplicationContext(), ChooseTicketActivity.class);
                                    intent.putExtra("seance_id", ids[j]);
                                    intent.putExtra("hall_name", halls[j]);
                                    intent.putExtra("cost", costs[j]);
                                    intent.putExtra("time", times[j]);
                                    startActivity(intent);
                                }
                            });
                            ((TextView) view.findViewById(R.id.seance_item_film_name)).setText(mfilms[i]);
                            ((TextView) view.findViewById(R.id.seance_item_hall_name)).setText(halls[i]);
                            ((TextView) view.findViewById(R.id.seance_item_cost)).setText(String.valueOf(costs[i]) + " P");
                            ((TextView) view.findViewById(R.id.seance_item_time)).setText(times[i].substring(10, 16));
                            ((TextView) view.findViewById(R.id.seance_item_date)).setText(times[i].substring(0,10));
                            linearLayoutContainer.addView(view);
                        }
                    }catch (JSONException e){
                        ;
                    }
                }

            } catch (JSONException e){
                ;
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try{
                Response response = client.newCall(requestSeance).execute();
                String content = response.body().string();
                return content;
            }catch (Exception e){
                Log.d("MyLOG", e.getMessage());
                return "nope";
            }
        }
    }

    class OkHttpHandlerFilmLoad extends AsyncTask<String, String, String>{

        Context context;
        int id = 0;
        String filmName = "";
        String filmDesciprion = "";
        int age = 0;
        String country = "";
        String producer = "";
        int duration = 0;
        String genre = "";
        String image = "";
        JSONObject reader;

        public OkHttpHandlerFilmLoad(Context context) {
            this.context = context;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try{
                reader = new JSONObject(s);
                JSONArray data = reader.getJSONArray("data");
                try{
                    id = data.getJSONObject(0).getInt("id");
                    filmName = data.getJSONObject(0).getString("name");
                    filmDesciprion = data.getJSONObject(0).getString("description");
                    age = data.getJSONObject(0).getInt("age");
                    country = data.getJSONObject(0).getString("country");
                    producer = data.getJSONObject(0).getString("producer");
                    duration = data.getJSONObject(0).getInt("duration");
                    genre = data.getJSONObject(0).getString("genre");
                    image = data.getJSONObject(0).getString("image");

                    film = new Film(
                            id,
                            filmName,
                            filmDesciprion,
                            age,
                            country,
                            producer,
                            duration,
                            genre,
                            image
                    );

                    okHttpHandlerSeanceLoad.execute();
                    //Picasso.with(context).load("http://10.0.3.2:8000" + image).into(imageView);
                    Picasso.with(context).load(urls[url]).into(imageView);
                } catch (JSONException e){
                    ;
                }
            } catch (JSONException e){
                ;
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try{
                Response response = client.newCall(requestFilm).execute();
                String content = response.body().string();
                return content;
            }catch (Exception e){
                Log.d("MyLOG", e.getMessage());
                return "nope";
            }
        }
    }
}
