package com.atmproofofconcept;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListActivity extends AppCompatActivity {
    @BindView(R.id.recyclerView_list)
    RecyclerView recyclerView_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);

        recyclerView_list.setLayoutManager(new LinearLayoutManager(this));
        AdapterList adapterList = new AdapterList();
        recyclerView_list.setAdapter(adapterList);
    }
}