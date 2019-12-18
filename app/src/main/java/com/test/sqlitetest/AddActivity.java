package com.test.sqlitetest;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.io.IOException;
import java.util.ArrayList;

public class AddActivity extends AppCompatActivity
{
    //Переменные для работы с БД
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;

    //Объявление EditText
    TextInputEditText name_tet;
    TextInputEditText number_tet;
    TextInputEditText address_tet;
    TextInputEditText age_tet;
    TextInputEditText date_start_additional_service_tet;
    TextInputEditText date_end_additional_service_tet;
    TextInputEditText balance_tet;
    TextInputEditText registration_date_tet;
    TextInputEditText debit_date_tet;

    //Объявление TextInputLayout
    TextInputLayout name_til;
    TextInputLayout number_til;
    TextInputLayout address_til;
    TextInputLayout age_til;
    TextInputLayout date_start_additional_service_til;
    TextInputLayout date_end_additional_service_til;
    TextInputLayout balance_til;
    TextInputLayout registration_date_til;
    TextInputLayout debit_date_til;

    //Индексы для таблиц
    int plan_index;
    int employer_index;
    int addService_index;
    int user_index;
    int abonent_index;

    //Объявление Spinner
    Spinner planSpinner;
    Spinner employerSpinner;
    Spinner addServiceSpinner;
    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        //Инициализация Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Инициализация EditText
        name_tet = findViewById(R.id.name_tet);
        number_tet = findViewById(R.id.number_tet);
        address_tet = findViewById(R.id.address_tet);
        age_tet = findViewById(R.id.age_tet);
        date_start_additional_service_tet = findViewById(R.id.date_start_additional_service_tet);
        date_end_additional_service_tet = findViewById(R.id.date_end_additional_service_tet);
        balance_tet = findViewById(R.id.balance_tet);
        registration_date_tet = findViewById(R.id.registration_date_tet);
        debit_date_tet = findViewById(R.id.debit_date_tet);

        //Инициализация TextInputLayout
        name_til = findViewById(R.id.name_til);
        number_til = findViewById(R.id.number_til);
        address_til = findViewById(R.id.address_til);
        age_til = findViewById(R.id.age_til);
        date_start_additional_service_til = findViewById(R.id.date_start_additional_service_til);
        date_end_additional_service_til = findViewById(R.id.date_end_additional_service_til);
        balance_til = findViewById(R.id.balance_til);
        registration_date_til = findViewById(R.id.registration_date_til);
        debit_date_til = findViewById(R.id.debit_date_til);

        //Инициализация Spinner
        planSpinner = findViewById(R.id.plan_spinner);
        employerSpinner = findViewById(R.id.employer_spinner);
        addServiceSpinner = findViewById(R.id.additional_service_spinner);

        //Часть кода для базы данных
        mDBHelper = new DatabaseHelper(this);
        try
        {
            mDBHelper.updateDataBase();
        }catch (IOException ex)
        {
            throw new Error("UnableToUpdateDatabase");
        }
        mDb = mDBHelper.getWritableDatabase();
        Cursor spinnerCursor;

        //Списки для Spinner
        final ArrayList<String> plans = new ArrayList<>();
        final ArrayList<String> employers = new ArrayList<>();
        final ArrayList<String> addServices = new ArrayList<>();
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        spinnerCursor = mDb.rawQuery("select plan_name from plans;",null);
        spinnerCursor.moveToFirst();
        while(!spinnerCursor.isAfterLast())
        {
            if(spinnerCursor.isNull(0))
            {
                plans.add("Нет тарифных планов");
            }else
            {
                plans.add(spinnerCursor.getString(0));
            }
            spinnerCursor.moveToNext();
        }
        spinnerCursor.close();
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        spinnerCursor = mDb.rawQuery("select empl_name from employer;",null);
        spinnerCursor.moveToFirst();
        while(!spinnerCursor.isAfterLast())
        {
            if(spinnerCursor.isNull(0))
            {
                employers.add("Нет сотрудников");
            }else
            {
                employers.add(spinnerCursor.getString(0));
            }
            spinnerCursor.moveToNext();
        }
        spinnerCursor.close();
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        spinnerCursor = mDb.rawQuery("select service_name from service;",null);
        spinnerCursor.moveToFirst();
        while(!spinnerCursor.isAfterLast())
        {
            if(spinnerCursor.isNull(0))
            {
                addServices.add("Нет дополнительных услуг");
            }else
            {
                addServices.add(spinnerCursor.getString(0));
            }
            spinnerCursor.moveToNext();
        }
        spinnerCursor.close();
        addServices.add("Нет");
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        //Присвоение адаптера Spinner
        ArrayAdapter<String> planSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, plans);
        planSpinner.setAdapter(planSpinnerAdapter);
        planSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final ArrayAdapter<String> emplSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, employers);
        employerSpinner.setAdapter(emplSpinnerAdapter);
        emplSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> addServiceSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, addServices);
        addServiceSpinner.setAdapter(addServiceSpinnerAdapter);
        addServiceSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Слушатель для Spinner
        AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                switch (parent.getId())
                {
                    case R.id.plan_spinner:
                        plan_index = position + 1;
                        //Snackbar.make(planSpinner, "Selected plan : " + plan_index, Snackbar.LENGTH_SHORT).show();
                        break;
                    case R.id.employer_spinner:
                        employer_index = position + 1;
                        //Snackbar.make(employerSpinner, "Selected employer : " + employer_index, Snackbar.LENGTH_SHORT).show();
                        break;
                    case R.id.additional_service_spinner:
                        addService_index = position + 1;
                        //Snackbar.make(addServiceSpinner, "Selected add service : " + addService_index, Snackbar.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                Snackbar.make(employerSpinner, "Nothing selected", Snackbar.LENGTH_SHORT).show();
            }
        };

        //Присваиваем слушателей Spinner
        planSpinner.setOnItemSelectedListener(onItemSelectedListener);
        employerSpinner.setOnItemSelectedListener(onItemSelectedListener);
        addServiceSpinner.setOnItemSelectedListener(onItemSelectedListener);

        //Инициализация FAB и присвоение ей обработчика
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //Объявление строк
                String name = "";
                String number = "";
                String address = "";
                String age = "";
                String date_start = "";
                String date_end = "";
                String balance = "";
                String create_date = "";
                String update_date = "";
                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                if(name_tet.getText().toString().trim().equals(""))
                {
                    name_til.setError("Введите имя");
                }else
                {
                    name_til.setError(null);
                    name = name_tet.getText().toString().trim();
                }
                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                if(number_tet.getText().toString().trim().equals(""))
                {
                    number_tet.setError("Введите номер");
                }else
                {
                    number_tet.setError(null);
                    number = number_tet.getText().toString().trim();
                }
                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                if(address_tet.getText().toString().trim().equals(""))
                {
                    address_tet.setError("Введите адрес");
                }else
                {
                    address_tet.setError(null);
                    address = address_tet.getText().toString().trim();
                }
                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                if(age_tet.getText().toString().trim().equals(""))
                {
                    age_tet.setError("Введите возраст");
                }else
                {
                    age_tet.setError(null);
                    age = age_tet.getText().toString().trim();
                }
                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                if(date_start_additional_service_tet.getText().toString().trim().equals("") && !addServiceSpinner.getSelectedItem().equals("Нет"))
                {
                    date_start_additional_service_tet.setError("Введите дату начала");
                }else
                {
                    date_start_additional_service_tet.setError(null);
                    date_start = date_start_additional_service_tet.getText().toString().trim();
                }
                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                if(date_end_additional_service_tet.getText().toString().trim().equals("") && !addServiceSpinner.getSelectedItem().equals("Нет"))
                {
                    date_end_additional_service_tet.setError("Введите дату окончания");
                }else
                {
                    date_end_additional_service_tet.setError(null);
                    date_end = date_end_additional_service_tet.getText().toString().trim();
                }
                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                if(balance_tet.getText().toString().trim().equals(""))
                {
                    balance_tet.setError("Введите баланс");
                }else
                {
                    balance_tet.setError(null);
                    balance = balance_tet.getText().toString().trim();
                }
                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                if(registration_date_tet.getText().toString().trim().equals(""))
                {
                    registration_date_tet.setError("Введите дату регистрации");
                }else
                {
                    registration_date_tet.setError(null);
                    create_date = registration_date_tet.getText().toString().trim();
                }
                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                if(debit_date_tet.getText().toString().trim().equals(""))
                {
                    debit_date_tet.setError("Введите дату списания средств");
                    return;
                }else
                {
                    debit_date_tet.setError(null);
                    update_date = debit_date_tet.getText().toString().trim();
                }
                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                ContentValues user_profile = new ContentValues();
                user_profile.put("user_name", name);
                user_profile.put("age", age);
                user_profile.put("address", address);
                long userID = mDb.insert("user_profile", null, user_profile);
                user_index = (int) userID;

                ContentValues abonent = new ContentValues();
                abonent.put("mobile_number", number);
                abonent.put("plan_id", plan_index);
                abonent.put("user_id", user_index);
                abonent.put("empl_id", employer_index);
                abonent.put("balance", balance);
                abonent.put("create_date", create_date);
                abonent.put("update_date", update_date);
                long abonentID = mDb.insert("abonent", null, abonent);
                abonent_index = (int) abonentID;

                if(addServiceSpinner.getSelectedItem().equals("Нет")) {}
                else
                {
                    ContentValues additional = new ContentValues();
                    additional.put("service_id", addService_index);
                    additional.put("abonent_id", abonent_index);
                    additional.put("date_start", date_start);
                    additional.put("date_end", date_end);
                    long additionalID = mDb.insert("additional_service", null, additional);
                }
                mDb.close();
                Snackbar.make(view, "user_id = " + user_index + ", abonent_id = " + abonent_index, Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                Intent intent = new Intent(AddActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
    }
    //----------------------------------------------------------------------------------------------
}