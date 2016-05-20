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
import com.android.volley.toolbox.StringRequest;
import com.erd.reblood.BaseActivity;
import com.erd.reblood.R;
import com.erd.reblood.adapter.HistoryAdapter;
import com.erd.reblood.app.AppController;
import com.erd.reblood.model.History;
import com.erd.reblood.utils.Constants;
import com.erd.reblood.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by E.R.D on 4/2/2016.
 */
public class HistoryActivity extends BaseActivity {
    private String TAG = HistoryActivity.class.getSimpleName();
    private static final String url = Constants.BASE_URL + "/users/history";
    private String jsonResponse;
    private JSONArray data;
    private List<History> historyList;
    private HistoryAdapter adapter;
    private SessionManager session;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.dummyfrag_scrollableview) RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        historyList = new ArrayList<>();

        adapter = new HistoryAdapter(historyList, new HistoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(History model) {
                String merch = model.getMerchantName();
                String store = model.getStoreName();
                String costId = model.getCostId();
                String transDate = model.getTransactDate();

                Intent intent = new Intent(HistoryActivity.this, HistoryDetailActivity.class);

                intent.putExtra("merch", merch);
                intent.putExtra("store", store);
                intent.putExtra("coid", costId);
                intent.putExtra("trDate", transDate);

                startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapter);
        getHistoryFromAPI();

    }

    private void getHistoryFromAPI() {
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        final String cid = user.get(SessionManager.KEY_CID);
        Log.d("TAGING", cid);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        Date date = new Date();
        String currentDate = dateFormat.format(date);

        StringRequest histReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                try {
                    jsonResponse = "";
                    JSONObject job = new JSONObject(response);
                    data = job.getJSONArray("history");

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject person = data.getJSONObject(i);

                        String sid = person.getString("store_id");
                        String mname = person.getString("merchant_name");
                        String sname = person.getString("store_name");
                        String date = person.getString("transaction_date");
                        String costID = person.getString("customer_id");

                        History history = new History(sid, mname, sname, costID, date);
                        historyList.add(history);

//                        jsonResponse += "ID: "      + id        + "\n";
//                        jsonResponse += "MID: "     + mid       + "\n";
//                        jsonResponse += "SID: "     + sid       + "\n";
//                        jsonResponse += "Name: "    + sname     + "\n";
//                        jsonResponse += "Desc: "    + desc      + "\n";
//                        jsonResponse += "Street: "  + street    + "\n";
//                        jsonResponse += "City: "    + city      + "\n";
//                        jsonResponse += "Country: " + country   + "\n";
//                        jsonResponse += "Phone: "   + phone     + "\n";
//
//                        Log.d(TAG, jsonResponse);

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
        }){
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("cid", cid);
                //Log.d("cidkey", "cidkey" + cid);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(histReq);
    }

}
