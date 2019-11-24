package com.test.sqlitetest;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class MainActivity extends AppCompatActivity
{
    //Переменные для работы с БД
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;

    //RecyclerView
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Часть кода для базы данных
/*      mDBHelper = new DatabaseHelper(this);
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
        }*/

        //Часть кода для RecyclerView
        String[] names = {
                "Word",
                "Excel",
                "PowerPoint",
                "Outlook",
                "Android studio",
                "Visual Studio Code",
                "MySQL WorkBench",
                "Telegram",
                "MathCAD",
                "AutoCAD",
                "Google Chrome",
                "TeamViewer"};

        String[] numbers = {
                "Editeur de texte",
                "Tableur",
                "Logiciel de prГ©sentation",
                "Client de courrier Г©lectronique",
                "Android Development",
                "Code development",
                "Workbench for database",
                "Messenger",
                "Math calculations",
                "Vector redactor",
                "Web browser",
                "Remote Control"};

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyAdapter(names, numbers);
        recyclerView.setAdapter(adapter);
    }
}