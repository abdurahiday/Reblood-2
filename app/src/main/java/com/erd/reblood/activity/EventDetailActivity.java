package com.erd.reblood.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.erd.reblood.BaseActivity;
import com.erd.reblood.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ILM on 5/12/2016.
 */
public class EventDetailActivity extends BaseActivity {
    private String TAG = EventDetailActivity.class.getSimpleName();

    @BindView(R.id.title_event) TextView txtTitle;
    @BindView(R.id.street_event) TextView txtStreet;
    @BindView(R.id.desc_event) TextView txtDesc;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_layout) CollapsingToolbarLayout a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String name = intent.getStringExtra("judul");
        String desc = intent.getStringExtra("desc");
        String street = intent.getStringExtra("street");

        a.setExpandedTitleColor(Color.TRANSPARENT);

        txtTitle.setText(name);
        txtStreet.setText(street);
        txtDesc.setText(desc);
    }

}
