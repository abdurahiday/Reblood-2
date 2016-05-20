package com.erd.reblood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.erd.reblood.BaseActivity;
import com.erd.reblood.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ILM on 5/20/2016.
 */
public class HistoryDetailActivity extends BaseActivity {
    @BindView(R.id.textMerchantName)
    TextView merchant;
    @BindView(R.id.textStoreName)
    TextView store;
    @BindView(R.id.textViewCustomerIDDetailHistory)
    TextView costId;
    @BindView(R.id.textViewDateDetailHistory)
    TextView transactDate;
    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String mercha = intent.getStringExtra("merch");
        String storea = intent.getStringExtra("store");
        String coId = intent.getStringExtra("coid");
        String trDate = intent.getStringExtra("trDate");

        merchant.setText(mercha);
        store.setText(storea);
        costId.setText(coId);
        transactDate.setText(trDate);

    }

    @OnClick(R.id.buttonDoneDetailHistory)
    public void done() {
        finish();
    }

}
