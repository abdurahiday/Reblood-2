package com.erd.reblood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.erd.reblood.BaseActivity;
import com.erd.reblood.R;
import com.erd.reblood.adapter.EventRecyclerAdapter;
import com.erd.reblood.app.AppController;
import com.erd.reblood.model.Event;
import com.erd.reblood.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ILM on 5/12/2016.
 */
public class EventActivity extends BaseActivity{
    private String TAG = EventActivity.class.getSimpleName();
    private static final String url = Constants.BASE_URL + "/transaction/event";
    private String jsonResponse;
    private JSONArray data;
    private List<Event> restaurantList;
    private EventRecyclerAdapter adapter;


    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.dummyfrag_scrollableview)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        restaurantList = new ArrayList<>();

        adapter = new EventRecyclerAdapter(restaurantList, new EventRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Event model) {
            String title = model.getStore_name();
            String desc = model.getDescription();
            String street = model.getStreet();

            Intent intent = new Intent(EventActivity.this, EventDetailActivity.class);
            intent.putExtra("judul", title);
            intent.putExtra("desc", desc);
            intent.putExtra("street", street);
            startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
        getEventFromAPI();
    }

    private void getEventFromAPI() {
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String rs = response.toString();
                Log.d(TAG, response.toString());
                try {
                    jsonResponse = "";
                    JSONObject job = new JSONObject(rs);
                    data = job.getJSONArray("store");

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject person = data.getJSONObject(i);

                        String id = person.getString("idstore");
                        String mid = person.getString("merchant_id");
                        String sid = person.getString("store_id");
                        String sname = person.getString("store_name");
                        String desc = person.getString("description");
                        String street = person.getString("street");
                        String city = person.getString("city");
                        String country = person.getString("country");
                        String phone = person.getString("phone");

                        Event event = new Event(id, mid, sid, sname, desc, street, city, country, phone);
                        restaurantList.add(event);

                        jsonResponse += "ID: "      + id        + "\n";
                        jsonResponse += "MID: "     + mid       + "\n";
                        jsonResponse += "SID: "     + sid       + "\n";
                        jsonResponse += "Name: "    + sname     + "\n";
                        jsonResponse += "Desc: "    + desc      + "\n";
                        jsonResponse += "Street: "  + street    + "\n";
                        jsonResponse += "City: "    + city      + "\n";
                        jsonResponse += "Country: " + country   + "\n";
                        jsonResponse += "Phone: "   + phone     + "\n";

                        Log.d(TAG, jsonResponse);

                    }
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(req);
    }

}
