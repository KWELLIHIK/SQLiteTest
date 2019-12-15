package com.test.sqlitetest;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import java.io.IOException;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {


    //RecyclerView
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    MyAdapter adapter;

    //SearchView
    SearchView editSearch;
    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //Getting ToolBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); //placing toolbar in place of actionbar

        //Переменные для работы с БД
        DatabaseHelper mDBHelper;
        SQLiteDatabase mDb;

        //Часть кода для базы данных
        mDBHelper = new DatabaseHelper(this);
        try
        {
            mDBHelper.updateDataBase();
        }catch (IOException ex)
        {
            throw new Error("UnableToUpdateDatabase");
        }
        try
        {
            mDb = mDBHelper.getWritableDatabase();
        }catch (SQLException sqlex)
        {
            throw sqlex;
        }
        //Часть кода для RecyclerView
        //Наполнение списка данными
        ArrayList<Abonent> abonents = new ArrayList<>();

        //Показывает всех абонентов
        //Cursor cursor = mDb.rawQuery("SELECT user_profile._id, user_profile.user_name, abonent.mobile_number FROM  user_profile LEFT JOIN abonent ON abonent.user_id = user_profile._id;", null);

        //Показывает только тех, у кого есть номер
        Cursor cursor = mDb.rawQuery("SELECT user_profile._id, user_profile.user_name, abonent.mobile_number FROM  abonent LEFT JOIN user_profile ON user_profile._id = abonent.user_id;", null);

        cursor.moveToFirst();

        //Добавление из базы в ArrayList
        while(!cursor.isAfterLast())
        {
            Abonent abonent = new Abonent();
            if(cursor.isNull(1))
            {
                abonent.setName("Имя отсутствует");
            }else
            {
                abonent.setName(cursor.getString(1));
            }
            if(cursor.isNull(2))
            {
                abonent.setNumber("Номер остутствует");
            }else
            {
                abonent.setNumber(cursor.getString(2));
            }
            abonents.add(abonent);
            cursor.moveToNext();
        }
        cursor.close();

        recyclerView = findViewById(R.id.searchRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyAdapter(abonents);
        recyclerView.setAdapter(adapter);

        editSearch = (SearchView) findViewById(R.id.search);
        editSearch.setOnQueryTextListener(this);
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public boolean onQueryTextSubmit(String query)
    {
        return false;
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public boolean onQueryTextChange(String newText)
    {
        String text = newText;
        adapter.filter(text);
        return false;
    }
    //----------------------------------------------------------------------------------------------

}
