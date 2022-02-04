package com.jeimandei.nasabah;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ListNasabahActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private String JSON_STRING;
    private ListView lv_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_nasabah);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lv_data = findViewById(R.id.lv_data);

        lv_data.setOnItemClickListener(this);

        getJSON();
    }

    private void getJSON() {
        class GetJSON extends AsyncTask<Void, Void, String> {
            ProgressDialog progressDialog;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(ListNasabahActivity.this, "Getting Data", "Please wait...", false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                HTTPHandler handler = new HTTPHandler();
                String result = handler.sendGetResp(Config.URL_GET_ALL);
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                    }
                }, 1000);

                JSON_STRING = s;
                Log.d("Data_JSON", JSON_STRING);


                displayAllData();

            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    private void displayAllData() {
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();

        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray jsonArray = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            Log.d("Data_JSON_LIST: ", JSON_STRING);


            for (int i=0;i<jsonArray.length(); i++){
                JSONObject object = jsonArray.getJSONObject(i);
                String id = object.getString(Config.TAG_JSON_ID);
                String name = object.getString(Config.TAG_JSON_NAME);
                String nationality = object.getString(Config.TAG_JSON_NATIONALITY);
                String priority = object.getString(Config.TAG_JSON_PRIORITY);

                HashMap<String, String> emp = new HashMap<>();
                emp.put(Config.TAG_JSON_ID, id);
                emp.put(Config.TAG_JSON_NAME, name);
                emp.put(Config.TAG_JSON_NATIONALITY, nationality);
                emp.put(Config.TAG_JSON_PRIORITY, priority.toUpperCase());

                arrayList.add(emp);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                getApplicationContext(), arrayList, R.layout.nasabah_data_layout,
                new String[] {Config.TAG_JSON_NAME, Config.TAG_JSON_NATIONALITY, Config.TAG_JSON_PRIORITY},
                new int[] {R.id.detail_name, R.id.detail_nationality, R.id.detail_priority}
        );
        lv_data.setAdapter(adapter);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(ListNasabahActivity.this, NasabahDetailAvtivity.class);
        HashMap<String, String> map = (HashMap) adapterView.getItemAtPosition(i);
        String custid = map.get(Config.TAG_JSON_ID).toString();
        intent.putExtra(Config.ID, custid);
        startActivity(intent);
    }
}