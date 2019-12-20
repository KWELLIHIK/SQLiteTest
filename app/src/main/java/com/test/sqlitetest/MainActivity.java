package com.test.sqlitetest;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener
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
        mDb = mDBHelper.getWritableDatabase();

        //Часть кода для RecyclerView
        //Наполнение списка данными
        ArrayList<Abonent> abonents = new ArrayList<>();

        //Показывает всех абонентов
        //Cursor cursor = mDb.rawQuery("SELECT user_profile._id, user_profile.user_name, abonent.mobile_number FROM  user_profile LEFT JOIN abonent ON abonent.user_id = user_profile._id;", null);

        //Показывает только тех, у кого есть номер
        final Cursor cursor = mDb.rawQuery("SELECT user_profile._id, user_profile.user_name, abonent.mobile_number, abonent.balance, plans.plan_name FROM  abonent LEFT JOIN user_profile ON user_profile._id=abonent.user_id join plans on abonent.plan_id=plans._id;", null);

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
            if(cursor.isNull(3))
            {
                abonent.setBalance("Нет информации");
            }else
            {
                abonent.setBalance(cursor.getString(3));
            }
            if(cursor.isNull(4))
            {
                abonent.setPlan("Нет информации");
            }else
            {
                abonent.setPlan(cursor.getString(4));
            }
            abonents.add(abonent);
            cursor.moveToNext();
        }
        cursor.close();

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyAdapter(abonents, new MyAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View v, final int position, boolean isLongClick, final ArrayList<Abonent> abonents)
            {
                Toast.makeText(MainActivity.this, "Position - " + position, Toast.LENGTH_SHORT).show();
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setTitle("Диалог");
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_edit);

                //Инициализация EditText
                final EditText et_dialog_name = dialog.findViewById(R.id.et_dialog_name);
                final EditText et_dialog_number = dialog.findViewById(R.id.et_dialog_number);
                final EditText et_dialog_balance = dialog.findViewById(R.id.et_dialog_balance);
                final EditText et_dialog_plan = dialog.findViewById(R.id.et_dialog_plan);
                et_dialog_name.setText(abonents.get(position).getName());
                et_dialog_number.setText(abonents.get(position).getNumber());
                et_dialog_balance.setText(abonents.get(position).getBalance());
                et_dialog_plan.setText(abonents.get(position).getPlan());

                //Получение user_id
                Cursor idCursor = mDb.rawQuery("SELECT user_profile._id,user_profile.user_name,abonent.mobile_number,abonent._id " +
                        "FROM abonent LEFT JOIN user_profile ON user_profile._id = abonent.user_id WHERE user_profile.user_name='" +
                        abonents.get(position).getName().trim() +
                        "' AND abonent.mobile_number='" + abonents.get(position).getNumber().trim() + "';", null);
                idCursor.moveToFirst();
                final int user_id = idCursor.getInt(0);
                final int abonent_id = idCursor.getInt(3);
                idCursor.close();

                //Кнопка Отмена
                Button dialog_btn_cancel = dialog.findViewById(R.id.dialog_btn_cancel);
                dialog_btn_cancel.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        dialog.dismiss();
                    }
                });
                dialog.show();

                //Кнопка Ок
                Button dialog_btn_ok = dialog.findViewById(R.id.dialog_btn_ok);
                dialog_btn_ok.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        //Обновление номера
                        ContentValues cvUpdateAbonent = new ContentValues();
                        cvUpdateAbonent.put("mobile_number", et_dialog_number.getText().toString().trim());
                        mDb.update("abonent", cvUpdateAbonent, "user_id='" + user_id + "'", null);
                        cvUpdateAbonent.clear();

                        //Обновление имени
                        ContentValues cvUpdateProfile = new ContentValues();
                        cvUpdateProfile.put("user_name", et_dialog_name.getText().toString().trim());
                        mDb.update("user_profile", cvUpdateProfile, "_id='" + user_id + "'", null);
                        cvUpdateProfile.clear();

                        //Обновление баланса
                        ContentValues cvUpdateBalance = new ContentValues();
                        cvUpdateBalance.put("balance",et_dialog_balance.getText().toString().trim());
                        mDb.update("abonent", cvUpdateBalance, "user_id='" + user_id + "'", null);
                        cvUpdateBalance.clear();

                        Toast.makeText(MainActivity.this, "Обновлено", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

                //Кнопка Удалить
                Button dialog_btn_delete = dialog.findViewById(R.id.dialog_btn_delete);
                dialog_btn_delete.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        mDb.delete("abonent", "user_id='" + user_id + "'", null);
                        mDb.delete("user_profile", "_id='" + user_id + "'", null);
                        mDb.delete("additional_service", "abonent_id='" + abonent_id + "'", null);

                        Toast.makeText(MainActivity.this, "Удалено", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
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

        //getting the search view
        final SearchView searchView = (SearchView) searchViewItem.getActionView();

        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(this);
        return true;
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_settings:
                Toast.makeText(this, "Вы нажали Settings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_add:
                Toast.makeText(this, "Нажата кнопка добавить", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_logout:
                finish();
                break;
        }
        return true;
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

    @Override
    protected void onDestroy()
    {
        mDBHelper.close();
        mDb.close();
        super.onDestroy();
    }

    //----------------------------------------------------------------------------------------------
}