package com.jeimandei.nasabah;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONObject;

public class NasabahDetailAvtivity extends AppCompatActivity {
    EditText et_name, et_nat, et_prio, et_card, et_bal;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasabah_detail_avtivity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Employee Detailed Data");

        et_name = findViewById(R.id.view_name);
        et_nat = findViewById(R.id.view_nationality);
        et_prio = findViewById(R.id.view_priority);
        et_card = findViewById(R.id.view_cardtype);
        et_bal = findViewById(R.id.view_balance);

        Intent recIntent = getIntent();
        id = recIntent.getStringExtra(Config.ID);

        getJSON();
    }

    private void getJSON() {
        class GetJSON extends AsyncTask<Void, Void, String> {
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(NasabahDetailAvtivity.this, "Getting Data", "Please wait...", false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                HTTPHandler handler = new HTTPHandler();
                String result = handler.sendGetResp(Config.URL_GET_DETAIL, id);
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
                displayDetailData(s);

            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    private void displayDetailData(String json) {
        try{
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject object = jsonArray.getJSONObject(0);

            String name = object.getString(Config.TAG_JSON_NAME);
            String nat = object.getString(Config.TAG_JSON_NATIONALITY);
            String prio = object.getString(Config.TAG_JSON_PRIORITY);
            String card = object.getString(Config.TAG_JSON_CARD_TYPE);
            String bal = object.getString(Config.TAG_JSON_BALANCE);

            et_name.setText(name);
            et_nat.setText(nat);
            et_prio.setText(prio);
            et_card.setText(card);
            et_bal.setText(bal);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return true;
    }
}