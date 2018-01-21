package com.example.ivan.markcinema;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ivan.markcinema.MainActivityRecyclerView.Objects.Objects.Film;
import com.example.ivan.markcinema.MainActivityRecyclerView.Objects.Objects.Seance;
import com.example.ivan.markcinema.MainActivityRecyclerView.Objects.Objects.Ticket;
import com.example.ivan.markcinema.PayActivity;
import com.example.ivan.markcinema.R;
import com.example.ivan.markcinema.ShowFilmActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.R.attr.path;

public class ChooseTicketActivity extends AppCompatActivity {

    ArrayList<Pair<Integer, Integer>> tickets = new ArrayList<Pair<Integer, Integer>>();
    ArrayList<Integer> places = new ArrayList<>();
    ArrayList<Integer> rows = new ArrayList<>();
    ArrayList<Ticket> mtickets = new ArrayList<>();
    Button button = null;

    int hall_id = 0;
    int seance_id = 0;
    String hall_name = "";
    String film_name = "";
    String time = "";
    int cost = 0;

    Intent transIntent;

    int BOOKSHELF_ROWS = 15;
    int BOOKSHELF_COLUMNS = 15;
    TableLayout tableLayout;

    OkHttpClient client = new OkHttpClient();
    RequestBody requestBody;
    Request requestHall;
    Request requestTicket;
    Request requestSeance;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    ArrayList<Seance> seances = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_ticket);

        transIntent = new Intent(getApplicationContext(), PayActivity.class);

        tableLayout = (TableLayout) findViewById(R.id.tablelayout);
        Log.d("MyTAG", String.valueOf(tableLayout.getChildCount()));
        button = (Button) findViewById(R.id.activity_choose_ticket_next);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transIntent.putExtra("cost", cost);
                transIntent.putIntegerArrayListExtra("rows", rows);
                transIntent.putIntegerArrayListExtra("places", places);
                startActivity(transIntent);
            }
        });

        Intent intent = getIntent();
        seance_id = intent.getIntExtra("seance_id", 0);
        hall_name = intent.getStringExtra("hall_name");
        cost = intent.getIntExtra("cost", 0);
        time = intent.getStringExtra("time");

        Log.d("MyTAG", "http://10.0.3.2:8000/tickets/?seance=" + seance_id);

        requestTicket = new Request.Builder()
                .url("http://10.0.3.2:8000/tickets/?seance=" + seance_id)
                .get()
                .build();
        OkHttpHandlerTicketLoad okHttpHandlerTicketLoad = new OkHttpHandlerTicketLoad(getApplicationContext());
        okHttpHandlerTicketLoad.execute("http://10.0.3.2:8000/tickets/?seance=" + seance_id);
    }


    class OkHttpHandlerTicketLoad extends AsyncTask<String, String, String> {

        Context context;
        int id = 0;
        JSONObject reader;
        int[] mplaces;
        int[] mrows;
        String[] mhalls;
        boolean[] mfrees;
        String[] mfilms;
        String[] mtimes;


        public OkHttpHandlerTicketLoad(Context context) {
            this.context = context;
        }



        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();

            if (s.equals("nope")){
                finish();
                startActivity(getIntent());
            }

            try {
                reader = new JSONObject(s);
                Log.d("MyTAG", s);
                JSONArray data = reader.getJSONArray("data");

                //Toast.makeText(getApplicationContext(), "Total places " + String.valueOf(data.length()), Toast.LENGTH_SHORT).show();

                mplaces = new int[data.length()];
                mrows = new int[data.length()];
                mhalls = new String[data.length()];
                mfrees = new boolean[data.length()];
                mfilms = new String[data.length()];
                mtimes = new String[data.length()];
                for (int i = 0; i < data.length(); i++) {
                    try {
                        id = data.getJSONObject(i).getInt("id");
                        mtimes[i] = data.getJSONObject(i).getString("seance");
                        mhalls[i] = data.getJSONObject(i).getString("hall");
                        mfilms[i] = data.getJSONObject(i).getString("film");
                        mplaces[i] = data.getJSONObject(i).getInt("place");
                        mrows[i] = data.getJSONObject(i).getInt("row");
                        mfrees[i] = data.getJSONObject(i).getBoolean("is_free");

                        if (mhalls[i].equals(hall_name)) {
                            mtickets.add(
                                    new Ticket(
                                            new Seance(
                                                    seance_id,
                                                    mtimes[i],
                                                    cost,
                                                    mhalls[i],
                                                    mfilms[i]
                                            ),
                                            mplaces[i],
                                            mrows[i],
                                            mfrees[i]
                                    )
                            );
                            transIntent.putExtra("seance_id", seance_id);
                            transIntent.putExtra("film", data.getJSONObject(i).getString("film"));
                            transIntent.putExtra("time", time);
                            transIntent.putExtra("hall", data.getJSONObject(i).getString("hall"));
                        }


                    } catch (JSONException e) {
                        Log.d("MyTAG", e.getMessage());
                    }
                }

                //Toast.makeText(getApplicationContext(), "Tickets == " + String.valueOf(mtickets.size()), Toast.LENGTH_SHORT).show();

                BOOKSHELF_ROWS = searchMax(mrows, data.length()) + 1;
                BOOKSHELF_COLUMNS = searchMax(mplaces, data.length()) + 1;

                //Toast.makeText(getApplicationContext(), String.valueOf(BOOKSHELF_COLUMNS) + " " + String.valueOf(BOOKSHELF_ROWS), Toast.LENGTH_SHORT).show();

                int count = 0;

                for (int i = 0; i < BOOKSHELF_ROWS; i++) {

                    TableRow tableRow = new TableRow(getApplicationContext());
                    tableRow.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));
                    tableRow.setBackgroundResource(R.color.backgroundDefault);

                    for (int j = 0; j < BOOKSHELF_COLUMNS; j++) {

                        int[] sets = {R.drawable.chair_free, R.drawable.chair_set};

                        final ImageView imageView = new ImageView(getApplicationContext());

                        if (mtickets.get(count).isFree()) {
                            imageView.setImageResource(R.drawable.chair_free);
                            imageView.setTag(R.drawable.chair_free);
                        } else {
                            imageView.setImageResource(R.drawable.chair_set);
                            //imageView.setTag(R.drawable.chair_set);
                            imageView.setClickable(false);
                        }

                        count++;

                        final int place = j;
                        final int row = i;

                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ImageView imageView = (ImageView) v;
                                Integer integer = (Integer) imageView.getTag();
                                integer = integer == null ? 0 : integer;

                                switch (integer) {
                                    case R.drawable.chair_free:
                                        imageView.setImageResource(R.drawable.chair_set);
                                        imageView.setTag(R.drawable.chair_set);
                                        tickets.add(new Pair<Integer, Integer>(row, place));
                                        rows.add(new Integer(row));
                                        places.add(new Integer(place));
                                        Toast.makeText(getApplicationContext(), String.valueOf(tickets.size()), Toast.LENGTH_SHORT).show();
                                        break;
                                    case R.drawable.chair_set:
                                        imageView.setImageResource(R.drawable.chair_free);
                                        imageView.setTag(R.drawable.chair_free);
                                        for (int k = 0; k < tickets.size(); k++) {
                                            if (tickets.get(k).first == row && tickets.get(k).second == place) {
                                                tickets.remove(k);
                                                rows.remove(k);
                                                places.remove(k);
                                            }
                                        }
                                        Toast.makeText(getApplicationContext(), String.valueOf(tickets.size()), Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        });
                        tableRow.addView(imageView, j);
                    }
                    tableLayout.addView(tableRow, i);
                }

            } catch (JSONException e) {
                ;
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Response response = client.newCall(requestTicket).execute();
                String content = response.body().string();
                return content;



            } catch (Exception e) {
                Log.d("MyLOG", e.getMessage());
                return "nope";
            }

           /* String content;
            try{
                content = getContent(params[0]);
            }
            catch (IOException ex){
                content = ex.getMessage();
            }

            return content;*/
        }

        public int searchMax(int[] a, int n) {
            int max = a[0];
            for (int i = 0; i < n; i++) {
                if (max < a[i]) {
                    max = a[i];
                }
            }
            return max;
        }

        private String getContent(String path) throws IOException {
            BufferedReader mreader = null;
            try {
                URL url = new URL(path);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setReadTimeout(10000);
                c.connect();
                mreader = new BufferedReader(new InputStreamReader(c.getInputStream()));
                StringBuilder buf = new StringBuilder();
                String line = null;
                while ((line = mreader.readLine()) != null) {
                    buf.append(line + "\n");
                }
                return (buf.toString());
            }
            finally {
                if (mreader != null) {
                    mreader.close();
                }
            }
        }
    }
}
