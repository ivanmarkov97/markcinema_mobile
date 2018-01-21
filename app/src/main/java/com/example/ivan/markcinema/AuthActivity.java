package com.example.ivan.markcinema;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AuthActivity extends AppCompatActivity implements View.OnClickListener{

    EditText login;
    EditText password;
    Button enter;
    TextView register;
    OkHttpClient client = new OkHttpClient();
    RequestBody requestBody;
    Request request;
    ProgressDialog progressDialog;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        login = (EditText) findViewById(R.id.activity_auth_login);
        password = (EditText) findViewById(R.id.activity_auth_pass);
        enter = (Button) findViewById(R.id.activity_auth_enter);
        register = (TextView) findViewById(R.id.activity_auth_register);

        register.setText(Html.fromHtml("<u>" + register.getText().toString() + "</u>"));

        enter.setOnClickListener(this);
        register.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Подождите");
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    @Override
    public void onClick(View v) {
        String loginStr = login.getText().toString();
        String passStr = password.getText().toString();
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject
                    .put("username", loginStr)
                    .put("password", passStr);
        } catch (JSONException e){}
        requestBody = RequestBody.create(JSON, jsonObject.toString());
        //Toast.makeText(v.getContext(), requestBody.toString(), Toast.LENGTH_SHORT).show();
        switch (v.getId()){
            case R.id.activity_auth_enter:
                request = new Request.Builder()
                        .url("http://10.0.3.2:8000/auth/login/")
                        .post(requestBody)
                        .build();
                OkHttpHandler  okHttpHandlerLogin = new OkHttpHandler(v.getContext());
                progressDialog.show();
                okHttpHandlerLogin.execute();
                break;
            case R.id.activity_auth_register:
                request = new Request.Builder()
                        .url("http://10.0.3.2:8000/auth/register/")
                        .post(requestBody)
                        .build();
                OkHttpHandler okHttpHandlerRegister = new OkHttpHandler(v.getContext());
                progressDialog.show();
                okHttpHandlerRegister.execute();
                break;
            default:
                break;
        }
    }

    public class OkHttpHandler extends AsyncTask<String, Void, String>{

        private Context context;

        public OkHttpHandler(Context context){
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.hide();
            Toast.makeText(context, s, Toast.LENGTH_LONG).show();
            if(!s.equals("ERROR auth/login")){
                startActivity(new Intent(context, MainActivity.class));
                finish();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try{
                Response response = client.newCall(request).execute();
                return response.body().string();
            }catch (Exception e){
                e.printStackTrace();
            }
            return "nope";
        }
    };
}
