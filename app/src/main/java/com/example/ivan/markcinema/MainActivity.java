package com.example.ivan.markcinema;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.example.ivan.markcinema.MainActivityRecyclerView.Objects.Objects.Film;
import com.example.ivan.markcinema.MainActivityRecyclerView.Objects.FilmAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    FilmAdapter filmAdapter;
    ArrayList<Film> films = new ArrayList<>();
    OkHttpClient client = new OkHttpClient();
    //RequestBody requestBody;
    Request request;
    ProgressDialog progressDialog;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
        setTitle("Все фильмы");
        recyclerView = (RecyclerView) findViewById(R.id.activity_main_recycler_view_films);
        filmAdapter = new FilmAdapter(films, getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(filmAdapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Подождите");
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        request = new Request.Builder()
                .url("http://10.0.3.2:8000/films/")
                .get()
                .build();

        OkHttpHandler okHttpHandler = new OkHttpHandler(getApplicationContext());
        okHttpHandler.execute();
    }

    public class OkHttpHandler extends AsyncTask<String, Void, String> {

        private Context context;
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

        public OkHttpHandler(Context context){
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.hide();
            //Toast.makeText(context, s, Toast.LENGTH_LONG).show();
            try{
                reader = new JSONObject(s);
                JSONArray data = reader.getJSONArray("data");
                //Toast.makeText(context, String.valueOf(data.length()), Toast.LENGTH_LONG).show();
                for(int i = 0; i < data.length(); i++) {
                    try {
                        id = data.getJSONObject(i).getInt("id");
                        filmName = data.getJSONObject(i).getString("name");
                        filmDesciprion = data.getJSONObject(i).getString("description");
                        age = data.getJSONObject(i).getInt("age");
                        country = data.getJSONObject(i).getString("country");
                        producer = data.getJSONObject(i).getString("producer");
                        duration = data.getJSONObject(i).getInt("duration");
                        genre = data.getJSONObject(i).getString("genre");
                        image = data.getJSONObject(i).getString("image");

                        films.add(new Film(
                                id,
                                filmName,
                                filmDesciprion,
                                age,
                                country,
                                producer,
                                duration,
                                genre,
                                image
                        ));
                        filmAdapter.notifyItemInserted(i);
                        //Toast.makeText(getApplicationContext(), films.get(i).getImage(), Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {
                        //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            } catch (JSONException e){
                //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try{
                Response response = client.newCall(request).execute();
                String content = response.body().string();
                return content;
            }catch (Exception e){
                Log.d("MyLOG", e.getMessage());
                return "nope";
            }
        }
    };
}
