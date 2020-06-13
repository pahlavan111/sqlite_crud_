package com.example.sqlitecrud;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class ActivityShow extends AppCompatActivity {

    DbHandler handler;
    List<Person> personList;
    ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        handler=new DbHandler(getApplicationContext());
        personList=new ArrayList<>();
        personList=handler.getPersonList();

        listView =  findViewById(R.id.list);
        listView.setAdapter(new PersonAdapter(this,R.layout.list_raw,personList,handler));

       // Toast.makeText(this, personList.size()+"", Toast.LENGTH_SHORT).show();


    }
}
