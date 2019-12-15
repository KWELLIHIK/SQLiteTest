package com.test.sqlitetest;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class MainActivity extends AppCompatActivity implements MyAdapter.MyAdapterListener
{
    //Переменные для работы с БД
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;

    //RecyclerView
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    MyAdapter adapter;


    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar); //getting the toolbar
        setSupportActionBar(toolbar); //placing toolbar in place of actionbar

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

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyAdapter(abonents, this);
        recyclerView.setAdapter(adapter);
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        //getting the search view from the menu
        MenuItem searchViewItem = menu.findItem(R.id.menu_search);
        //getting search manager from systemservice
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        //getting the search view
        final SearchView searchView = (SearchView) searchViewItem.getActionView();
        searchView.setIconifiedByDefault(true);
        //here we will get the search query
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                //do the search here
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query)
            {
                adapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_about:
                Toast.makeText(this, "Вы нажали About", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_settings:
                Toast.makeText(this, "Вы нажали Settings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_logout:
                Toast.makeText(this, "Вы нажали Выход", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public void onAbonentSelected(Abonent abonent)
    {
        Toast.makeText(getApplicationContext(), "Выбран: " + abonent.getName() + ", " + abonent.getNumber(), Toast.LENGTH_LONG).show();
    }
}