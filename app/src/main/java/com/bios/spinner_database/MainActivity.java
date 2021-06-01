package com.bios.spinner_database;

import androidx.appcompat.app.AppCompatActivity;

import android.app.VoiceInteractor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner spinnerElectronic,spinnerElectype;
    ArrayList<String> electronicList = new ArrayList<>();
    ArrayList<String> electList = new ArrayList<>();
    ArrayAdapter<String> electronicAdapter;
    ArrayAdapter<String> electAdapter;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(this);
        spinnerElectronic = findViewById(R.id.spinnerElectronic);
        spinnerElectype = findViewById(R.id.spinnerElect_type);
        String url = "http://192.168.43.225/Android_spinner_lab/electronic.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("electronic");
                    for (int i=0; i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String electronicName = jsonObject.optString("electronic_name");
                        electronicList.add(electronicName);
                        electronicAdapter = new ArrayAdapter<>(MainActivity.this,
                                android.R.layout.simple_spinner_item, electronicList);
                        electronicAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                        spinnerElectronic.setAdapter(electronicAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

requestQueue.add(jsonObjectRequest);
spinnerElectronic.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        if(adapterView.getId()== R.id.spinnerElectronic){
            electList.clear();
            String selectelectronic = adapterView.getSelectedItem().toString();
            String url = "http://192.168.43.225/Android_spinner_lab/elect_type.php?electronic_name="+selectelectronic;
            requestQueue = Volley.newRequestQueue(this);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("elect_type");
                        for (int i=0; i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String elect_Name = jsonObject.optString("elect_name");
                            electList.add(elect_Name);
                            electAdapter = new ArrayAdapter<>(MainActivity.this,
                                    android.R.layout.simple_spinner_dropdown_item, electList);
                            electronicAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerElectype.setAdapter(electAdapter);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            requestQueue.add(jsonObjectRequest);
//
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}