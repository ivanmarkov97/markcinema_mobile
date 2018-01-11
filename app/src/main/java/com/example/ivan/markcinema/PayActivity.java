package com.example.ivan.markcinema;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ivan.markcinema.MainActivityRecyclerView.Objects.Objects.Seance;
import com.example.ivan.markcinema.MainActivityRecyclerView.Objects.Objects.Ticket;
import com.example.ivan.markcinema.PayActivityRecyclerView.TicketAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PayActivity extends AppCompatActivity {

    Toolbar toolbar;
    Button buyTickets;
    RecyclerView recyclerView;
    TicketAdapter ticketAdapter;
    ArrayList<Ticket> tickets = new ArrayList<>();
    ArrayList<Integer> rows = new ArrayList<>();
    ArrayList<Integer> places = new ArrayList<>();
    String film = "";
    String hall = "";
    int cost = 0;
    int seance_id = 0;
    String time = "";

    RequestBody requestBody;
    Request requestTickets;
    OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        Intent intent = getIntent();
        rows = intent.getIntegerArrayListExtra("rows");
        places = intent.getIntegerArrayListExtra("places");
        seance_id = intent.getIntExtra("seance_id", 0);
        film = intent.getStringExtra("film");
        hall = intent.getStringExtra("hall");
        cost = intent.getIntExtra("cost", 0);
        time = intent.getStringExtra("time");

        Toast.makeText(getApplicationContext(), String.valueOf(rows.size()), Toast.LENGTH_SHORT).show();

        toolbar = (Toolbar) findViewById(R.id.activity_pay_toolbar);
        setSupportActionBar(toolbar);
        setTitle("Билеты");

        buyTickets = (Button) findViewById(R.id.activity_pay_pay);
        buyTickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();
                JSONArray jsonArray = new JSONArray();
                try{
                    jsonObject.put("seance_id", seance_id);
                    jsonObject.put("film", film);
                    jsonObject.put("hall", hall);
                    jsonObject.put("cost", cost);
                    jsonObject.put("time", time);
                    jsonObject.put("places", jsonArray);
                    for(int i = 0; i < rows.size(); i++) {
                        JSONObject arrayItem = new JSONObject();
                        arrayItem
                                .put("row", rows.get(i))
                                .put("place", places.get(i));
                        jsonArray.put(arrayItem);
                    }
                } catch (JSONException e){}
                Toast.makeText(getApplicationContext(), jsonObject.toString(), Toast.LENGTH_SHORT).show();
                requestBody = RequestBody.create(JSON, jsonObject.toString());
                requestTickets = new Request.Builder()
                        .url("http://10.0.3.2:8000/tickets/sell/")
                        .post(requestBody)
                        .build();
                PayHandler payHandler = new PayHandler(getApplicationContext());
                payHandler.execute();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.activity_pay_tickets);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ticketAdapter = new TicketAdapter(tickets, getApplicationContext());
        recyclerView.setAdapter(ticketAdapter);

        for(int i = 0; i < rows.size(); i++) {
            tickets.add(new Ticket(
                            new Seance(
                                    seance_id,
                                    time,
                                    cost,
                                    hall,
                                    film
                            ),
                            places.get(i) + 1,
                            rows.get(i) + 1,
                            false
                    )
            );
        }
    }

    class PayHandler extends AsyncTask<String, String, String>{

        private Context context;

        public PayHandler(Context context) {
            this.context = context;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Response response = client.newCall(requestTickets).execute();
                return response.body().string();
            } catch (Exception e){
                return "nope";
            }
        }
    }
}
