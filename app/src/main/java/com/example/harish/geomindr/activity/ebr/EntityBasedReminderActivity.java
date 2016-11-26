package com.example.harish.geomindr.activity.ebr;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.harish.geomindr.R;
import com.example.harish.geomindr.database.DatabaseHelper;
import com.example.harish.geomindr.service.main.ReminderService;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EntityBasedReminderActivity extends AppCompatActivity {

    Button atm,food,hospital,police,mall,pharmacy,gym,bank,poffice,bar,lib,movie,books,gov,gas;

    // checking if button is clicked or not
    // of 0 then not clicked and if 1 then clicked
    public int atmCheck=0,foodCheck=0,hospitalCheck=0,policeCheck=0,mallCheck=0,pharmacyCheck=0,gymCheck=0,bankCheck=0,pofficeCheck=0,barCheck=0,libCheck=0,movieCheck=0,booksCheck=0,govCheck=0,gasCheck=0;

    DatabaseHelper myDb;
    public static Boolean active=false;

    public int mHour,mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entity_based_reminder);

        atm = (Button) findViewById(R.id.btn_atm);
        food = (Button) findViewById(R.id.btn_food);
        hospital = (Button) findViewById(R.id.btn_hospital);
        police = (Button) findViewById(R.id.btn_police);
        mall = (Button) findViewById(R.id.btn_mall);
        pharmacy = (Button) findViewById(R.id.btn_pharmacy);
        gym = (Button) findViewById(R.id.btn_gym);
        bank = (Button) findViewById(R.id.btn_bank);
        poffice = (Button) findViewById(R.id.btn_poffice);
        bar = (Button) findViewById(R.id.btn_bar);
        lib = (Button) findViewById(R.id.btn_lib);
        movie = (Button) findViewById(R.id.btn_movie);
        books = (Button) findViewById(R.id.btn_books);
        gov = (Button) findViewById(R.id.btn_gov);
        gas = (Button) findViewById(R.id.btn_gas);

        myDb = DatabaseHelper.getInstance(EntityBasedReminderActivity.this);

        Cursor res = myDb.getAllRecordsEBR();
        while(res.moveToNext()){
            String r = res.getString(0);
            if(r.equals("atm")){
                atmCheck=1;
                atm.setBackgroundColor(getResources().getColor(R.color.button_clicked));
            }
            else if(r.equals("food")){
                foodCheck=1;
                food.setBackgroundColor(getResources().getColor(R.color.button_clicked));
            }
            else if(r.equals("hospital")){
                hospitalCheck=1;
                hospital.setBackgroundColor(getResources().getColor(R.color.button_clicked));
            }
            else if(r.equals("police")){
                policeCheck=1;
                police.setBackgroundColor(getResources().getColor(R.color.button_clicked));
            }
            else if(r.equals("shopping_mall")){
                mallCheck=1;
                mall.setBackgroundColor(getResources().getColor(R.color.button_clicked));
            }
            else if(r.equals("pharmacy")){
                pharmacyCheck=1;
                pharmacy.setBackgroundColor(getResources().getColor(R.color.button_clicked));
            }
            else if(r.equals("gym")){
                gymCheck=1;
                gym.setBackgroundColor(getResources().getColor(R.color.button_clicked));
            }
            else if(r.equals("bank")){
                bankCheck=1;
                bank.setBackgroundColor(getResources().getColor(R.color.button_clicked));
            }
            else if(r.equals("post_office")){
                pofficeCheck=1;
                poffice.setBackgroundColor(getResources().getColor(R.color.button_clicked));
            }
            else if(r.equals("bar")){
                barCheck=1;
                bar.setBackgroundColor(getResources().getColor(R.color.button_clicked));
            }
            else if(r.equals("library")){
                libCheck=1;
                lib.setBackgroundColor(getResources().getColor(R.color.button_clicked));
            }
            else if(r.equals("movie_theater")){
                movieCheck=1;
                movie.setBackgroundColor(getResources().getColor(R.color.button_clicked));
            }
            else if(r.equals("book_store")){
                booksCheck=1;
                books.setBackgroundColor(getResources().getColor(R.color.button_clicked));
            }
            else if(r.equals("local_government_office")){
                govCheck=1;
                gov.setBackgroundColor(getResources().getColor(R.color.button_clicked));
            }
            else if(r.equals("gas_station")){
                gasCheck=1;
                gas.setBackgroundColor(getResources().getColor(R.color.button_clicked));
            }
        }

        atm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(atmCheck==0){
                    atmCheck=1;
                    atm.setBackgroundColor(getResources().getColor(R.color.button_clicked));
                    //sending data below
                    saveData("atm","");
                    Toast.makeText(EntityBasedReminderActivity.this, "Data will be added to database", Toast.LENGTH_SHORT).show();
                }
                else{
                    atmCheck=0;
                    atm.setBackgroundColor(getResources().getColor(R.color.button_unclicked));
                    deleteData("atm");
                    Toast.makeText(EntityBasedReminderActivity.this, "Data will be deleted from database", Toast.LENGTH_SHORT).show();
                }
            }
        });

        pharmacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pharmacyCheck==0){
                    pharmacyCheck=1;
                    pharmacy.setBackgroundColor(getResources().getColor(R.color.button_clicked));
                    saveData("pharmacy","");
                    Toast.makeText(EntityBasedReminderActivity.this, "Data will be added to database", Toast.LENGTH_SHORT).show();
                }
                else{
                    pharmacyCheck=0;
                    pharmacy.setBackgroundColor(getResources().getColor(R.color.button_unclicked));
                    deleteData("pharmacy");
                    Toast.makeText(EntityBasedReminderActivity.this, "Data will be deleted from database", Toast.LENGTH_SHORT).show();
                }
            }
        });

        poffice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pofficeCheck==0){
                    pofficeCheck=1;
                    poffice.setBackgroundColor(getResources().getColor(R.color.button_clicked));
                    saveData("post_office","");
                    Toast.makeText(EntityBasedReminderActivity.this, "Data will be added to database", Toast.LENGTH_SHORT).show();
                }
                else{
                    pofficeCheck=0;
                    poffice.setBackgroundColor(getResources().getColor(R.color.button_unclicked));
                    deleteData("post_office");
                    Toast.makeText(EntityBasedReminderActivity.this, "Data will be deleted from database", Toast.LENGTH_SHORT).show();
                }
            }
        });

        books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(booksCheck==0){
                    booksCheck=1;
                    books.setBackgroundColor(getResources().getColor(R.color.button_clicked));
                    saveData("book_store","");
                    Toast.makeText(EntityBasedReminderActivity.this, "Data will be added to database", Toast.LENGTH_SHORT).show();
                }
                else{
                    booksCheck=0;
                    books.setBackgroundColor(getResources().getColor(R.color.button_unclicked));
                    deleteData("book_store");
                    Toast.makeText(EntityBasedReminderActivity.this, "Data will be deleted from database", Toast.LENGTH_SHORT).show();
                }
            }
        });


        gas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gasCheck==0){
                    gasCheck=1;
                    gas.setBackgroundColor(getResources().getColor(R.color.button_clicked));
                    saveData("gas_station","");
                    Toast.makeText(EntityBasedReminderActivity.this, "Data will be added to database", Toast.LENGTH_SHORT).show();
                }
                else{
                    gasCheck=0;
                    gas.setBackgroundColor(getResources().getColor(R.color.button_unclicked));
                    deleteData("gas_station");
                    Toast.makeText(EntityBasedReminderActivity.this, "Data will be deleted from database", Toast.LENGTH_SHORT).show();
                }
            }
        });

        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(foodCheck==0){
                    //opens dialog box
                    final Dialog dialog = new Dialog(EntityBasedReminderActivity.this);
                    dialog.setContentView(R.layout.dialog);
                    dialog.setTitle("Set Condition");
                    final CheckBox check1 = (CheckBox) dialog.findViewById(R.id.check1);
                    final CheckBox check2 = (CheckBox) dialog.findViewById(R.id.check2);
                    final EditText name = (EditText) dialog.findViewById(R.id.entity_name);
                    final Button save = (Button) dialog.findViewById(R.id.save);
                    final Button cancel = (Button) dialog.findViewById(R.id.cancel);

                    dialog.show();

                    check2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if(b) {
                                name.setVisibility(View.VISIBLE);
                                check1.setEnabled(false);
                            }
                            else{
                                name.setVisibility(View.GONE);
                                check1.setEnabled(true);
                            }
                        }
                    });

                    check1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if(b) {
                                name.setVisibility(View.GONE);
                                check2.setEnabled(false);
                            }
                            else{
                                name.setVisibility(View.GONE);
                                check2.setEnabled(true);
                            }
                        }
                    });

                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(!check1.isChecked() && !check2.isChecked()){
                                save.setBackgroundColor(Color.RED);
                            }
                            else if(check1.isChecked() && check2.isChecked()){
                                save.setBackgroundColor(Color.RED);
                            }
                            else if(check1.isChecked()){
                                //save data
                                foodCheck=1;
                                food.setBackgroundColor(getResources().getColor(R.color.button_clicked));
                                saveData("food","");
                                Toast.makeText(EntityBasedReminderActivity.this, "Data will be added to database", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                            else if(check2.isChecked() && !name.getText().toString().equals("")) {
                                String data = name.getText().toString();
                                //save data
                                foodCheck=1;
                                food.setBackgroundColor(getResources().getColor(R.color.button_clicked));
                                saveData("food",name.getText().toString());
                                Toast.makeText(EntityBasedReminderActivity.this, "Data will be added to database", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                            else{
                                save.setBackgroundColor(Color.RED);
                            }
                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                }
                else{
                    foodCheck=0;
                    food.setBackgroundColor(getResources().getColor(R.color.button_unclicked));
                    deleteData("food");
                    Toast.makeText(EntityBasedReminderActivity.this, "Data will be deleted from database", Toast.LENGTH_SHORT).show();
                }
            }
        });

        hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hospitalCheck==0){
                    //opens dialog box
                    final Dialog dialog = new Dialog(EntityBasedReminderActivity.this);
                    dialog.setContentView(R.layout.dialog);
                    dialog.setTitle("Set Condition");
                    final CheckBox check1 = (CheckBox) dialog.findViewById(R.id.check1);
                    final CheckBox check2 = (CheckBox) dialog.findViewById(R.id.check2);
                    final EditText name = (EditText) dialog.findViewById(R.id.entity_name);
                    final Button save = (Button) dialog.findViewById(R.id.save);
                    final Button cancel = (Button) dialog.findViewById(R.id.cancel);

                    dialog.show();

                    check2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if(b) {
                                name.setVisibility(View.VISIBLE);
                                check1.setEnabled(false);
                            }
                            else{
                                name.setVisibility(View.GONE);
                                check1.setEnabled(true);
                            }
                        }
                    });

                    check1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if(b) {
                                name.setVisibility(View.GONE);
                                check2.setEnabled(false);
                            }
                            else{
                                name.setVisibility(View.GONE);
                                check2.setEnabled(true);
                            }
                        }
                    });

                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(!check1.isChecked() && !check2.isChecked()){
                                save.setBackgroundColor(Color.RED);
                            }
                            else if(check1.isChecked() && check2.isChecked()){
                                save.setBackgroundColor(Color.RED);
                            }
                            else if(check1.isChecked()){
                                //save data
                                hospitalCheck=1;
                                hospital.setBackgroundColor(getResources().getColor(R.color.button_clicked));
                                saveData("hospital","");
                                Toast.makeText(EntityBasedReminderActivity.this, "Data will be added to database", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                            else if(check2.isChecked() && !name.getText().toString().equals("")) {
                                String data = name.getText().toString();
                                //save data
                                hospitalCheck=1;
                                hospital.setBackgroundColor(getResources().getColor(R.color.button_clicked));
                                saveData("hospital",name.getText().toString());
                                Toast.makeText(EntityBasedReminderActivity.this, "Data will be added to database", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                            else{
                                save.setBackgroundColor(Color.RED);
                            }
                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                }
                else
                {
                    hospitalCheck=0;
                    hospital.setBackgroundColor(getResources().getColor(R.color.button_unclicked));
                    deleteData("hospital");
                    Toast.makeText(EntityBasedReminderActivity.this, "Data will be deleted from database", Toast.LENGTH_SHORT).show();
                }
            }
        });

        police.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(policeCheck==0){
                    // dialog box opens
                    final Dialog dialog = new Dialog(EntityBasedReminderActivity.this);
                    dialog.setContentView(R.layout.dialog);
                    dialog.setTitle("Set Condition");
                    final CheckBox check1 = (CheckBox) dialog.findViewById(R.id.check1);
                    final CheckBox check2 = (CheckBox) dialog.findViewById(R.id.check2);
                    final EditText name = (EditText) dialog.findViewById(R.id.entity_name);
                    final Button save = (Button) dialog.findViewById(R.id.save);
                    final Button cancel = (Button) dialog.findViewById(R.id.cancel);

                    dialog.show();

                    check2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if(b) {
                                name.setVisibility(View.VISIBLE);
                                check1.setEnabled(false);
                            }
                            else{
                                name.setVisibility(View.GONE);
                                check1.setEnabled(true);
                            }
                        }
                    });

                    check1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if(b) {
                                name.setVisibility(View.GONE);
                                check2.setEnabled(false);
                            }
                            else{
                                name.setVisibility(View.GONE);
                                check2.setEnabled(true);
                            }
                        }
                    });

                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(!check1.isChecked() && !check2.isChecked()){
                                save.setBackgroundColor(Color.RED);
                            }
                            else if(check1.isChecked() && check2.isChecked()){
                                save.setBackgroundColor(Color.RED);
                            }
                            else if(check1.isChecked()){
                                //save data
                                policeCheck=1;
                                police.setBackgroundColor(getResources().getColor(R.color.button_clicked));
                                saveData("police","");
                                Toast.makeText(EntityBasedReminderActivity.this, "Data will be added to database", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                            else if(check2.isChecked() && !name.getText().toString().equals("")) {
                                String data = name.getText().toString();
                                //save data
                                policeCheck=1;
                                police.setBackgroundColor(getResources().getColor(R.color.button_clicked));
                                saveData("police",name.getText().toString());
                                Toast.makeText(EntityBasedReminderActivity.this, "Data will be added to database", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                            else{
                                save.setBackgroundColor(Color.RED);
                            }
                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                }
                else{
                    policeCheck=0;
                    police.setBackgroundColor(getResources().getColor(R.color.button_unclicked));
                    deleteData("police");
                    Toast.makeText(EntityBasedReminderActivity.this, "Data will be deleted from database", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mallCheck==0){
                    //dialog box opens
                    final Dialog dialog = new Dialog(EntityBasedReminderActivity.this);
                    dialog.setContentView(R.layout.dialog);
                    dialog.setTitle("Set Condition");
                    final CheckBox check1 = (CheckBox) dialog.findViewById(R.id.check1);
                    final CheckBox check2 = (CheckBox) dialog.findViewById(R.id.check2);
                    final EditText name = (EditText) dialog.findViewById(R.id.entity_name);
                    final Button save = (Button) dialog.findViewById(R.id.save);
                    final Button cancel = (Button) dialog.findViewById(R.id.cancel);

                    dialog.show();

                    check2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if(b) {
                                name.setVisibility(View.VISIBLE);
                                check1.setEnabled(false);
                            }
                            else{
                                name.setVisibility(View.GONE);
                                check1.setEnabled(true);
                            }
                        }
                    });

                    check1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if(b) {
                                name.setVisibility(View.GONE);
                                check2.setEnabled(false);
                            }
                            else{
                                name.setVisibility(View.GONE);
                                check2.setEnabled(true);
                            }
                        }
                    });

                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(!check1.isChecked() && !check2.isChecked()){
                                save.setBackgroundColor(Color.RED);
                            }
                            else if(check1.isChecked() && check2.isChecked()){
                                save.setBackgroundColor(Color.RED);
                            }
                            else if(check1.isChecked()){
                                //save data
                                mallCheck=1;
                                mall.setBackgroundColor(getResources().getColor(R.color.button_clicked));
                                saveData("shopping_mall","");
                                Toast.makeText(EntityBasedReminderActivity.this, "Data will be added to database", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                            else if(check2.isChecked() && !name.getText().toString().equals("")) {
                                String data = name.getText().toString();
                                //save data
                                mallCheck=1;
                                mall.setBackgroundColor(getResources().getColor(R.color.button_clicked));
                                saveData("shopping_mall",name.getText().toString());
                                Toast.makeText(EntityBasedReminderActivity.this, "Data will be added to database", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                            else{
                                save.setBackgroundColor(Color.RED);
                            }
                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                }
                else{
                    mallCheck=0;
                    mall.setBackgroundColor(getResources().getColor(R.color.button_unclicked));
                    deleteData("shopping_mall");
                    Toast.makeText(EntityBasedReminderActivity.this, "Data will be deleted from database", Toast.LENGTH_SHORT).show();
                }
            }
        });

        gym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gymCheck==0){
                    //dialog box opens
                    final Dialog dialog = new Dialog(EntityBasedReminderActivity.this);
                    dialog.setContentView(R.layout.dialog);
                    dialog.setTitle("Set Condition");
                    final CheckBox check1 = (CheckBox) dialog.findViewById(R.id.check1);
                    final CheckBox check2 = (CheckBox) dialog.findViewById(R.id.check2);
                    final EditText name = (EditText) dialog.findViewById(R.id.entity_name);
                    final Button save = (Button) dialog.findViewById(R.id.save);
                    final Button cancel = (Button) dialog.findViewById(R.id.cancel);

                    dialog.show();

                    check2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if(b) {
                                name.setVisibility(View.VISIBLE);
                                check1.setEnabled(false);
                            }
                            else{
                                name.setVisibility(View.GONE);
                                check1.setEnabled(true);
                            }
                        }
                    });

                    check1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if(b) {
                                name.setVisibility(View.GONE);
                                check2.setEnabled(false);
                            }
                            else{
                                name.setVisibility(View.GONE);
                                check2.setEnabled(true);
                            }
                        }
                    });

                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(!check1.isChecked() && !check2.isChecked()){
                                save.setBackgroundColor(Color.RED);
                            }
                            else if(check1.isChecked() && check2.isChecked()){
                                save.setBackgroundColor(Color.RED);
                            }
                            else if(check1.isChecked()){
                                //save data
                                gymCheck=1;
                                gym.setBackgroundColor(getResources().getColor(R.color.button_clicked));
                                saveData("gym","");
                                Toast.makeText(EntityBasedReminderActivity.this, "Data will be added to database", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                            else if(check2.isChecked() && !name.getText().toString().equals("")) {
                                String data = name.getText().toString();
                                //save data
                                gymCheck=1;
                                gym.setBackgroundColor(getResources().getColor(R.color.button_clicked));
                                saveData("gym",name.getText().toString());
                                Toast.makeText(EntityBasedReminderActivity.this, "Data will be added to database", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                            else{
                                save.setBackgroundColor(Color.RED);
                            }
                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                }
                else{
                    gymCheck=0;
                    gym.setBackgroundColor(getResources().getColor(R.color.button_unclicked));
                    deleteData("gym");
                    Toast.makeText(EntityBasedReminderActivity.this, "Data will be deleted from database", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bankCheck==0){
                    //opens dialog box
                    final Dialog dialog = new Dialog(EntityBasedReminderActivity.this);
                    dialog.setContentView(R.layout.dialog);
                    dialog.setTitle("Set Condition");
                    final CheckBox check1 = (CheckBox) dialog.findViewById(R.id.check1);
                    final CheckBox check2 = (CheckBox) dialog.findViewById(R.id.check2);
                    final EditText name = (EditText) dialog.findViewById(R.id.entity_name);
                    final Button save = (Button) dialog.findViewById(R.id.save);
                    final Button cancel = (Button) dialog.findViewById(R.id.cancel);

                    dialog.show();

                    check2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if(b) {
                                name.setVisibility(View.VISIBLE);
                                check1.setEnabled(false);
                            }
                            else{
                                name.setVisibility(View.GONE);
                                check1.setEnabled(true);
                            }
                        }
                    });

                    check1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if(b) {
                                name.setVisibility(View.GONE);
                                check2.setEnabled(false);
                            }
                            else{
                                name.setVisibility(View.GONE);
                                check2.setEnabled(true);
                            }
                        }
                    });

                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(!check1.isChecked() && !check2.isChecked()){
                                save.setBackgroundColor(Color.RED);
                            }
                            else if(check1.isChecked() && check2.isChecked()){
                                save.setBackgroundColor(Color.RED);
                            }
                            else if(check1.isChecked()){
                                //save data
                                bankCheck=1;
                                bank.setBackgroundColor(getResources().getColor(R.color.button_clicked));
                                saveData("bank","");
                                Toast.makeText(EntityBasedReminderActivity.this, "Data will be added to database", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                            else if(check2.isChecked() && !name.getText().toString().equals("")) {
                                String data = name.getText().toString();
                                //save data
                                bankCheck=1;
                                bank.setBackgroundColor(getResources().getColor(R.color.button_clicked));
                                saveData("bank",name.getText().toString());
                                Toast.makeText(EntityBasedReminderActivity.this, "Data will be added to database", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                            else{
                                save.setBackgroundColor(Color.RED);
                            }
                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                }
                else{
                    bankCheck=0;
                    bank.setBackgroundColor(getResources().getColor(R.color.button_unclicked));
                    deleteData("bank");
                    Toast.makeText(EntityBasedReminderActivity.this, "Data will be deleted from database", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(barCheck==0){
                    //opens dialog box
                    final Dialog dialog = new Dialog(EntityBasedReminderActivity.this);
                    dialog.setContentView(R.layout.dialog);
                    dialog.setTitle("Set Condition");
                    final CheckBox check1 = (CheckBox) dialog.findViewById(R.id.check1);
                    final CheckBox check2 = (CheckBox) dialog.findViewById(R.id.check2);
                    final EditText name = (EditText) dialog.findViewById(R.id.entity_name);
                    final Button save = (Button) dialog.findViewById(R.id.save);
                    final Button cancel = (Button) dialog.findViewById(R.id.cancel);

                    dialog.show();

                    check2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if(b) {
                                name.setVisibility(View.VISIBLE);
                                check1.setEnabled(false);
                            }
                            else{
                                name.setVisibility(View.GONE);
                                check1.setEnabled(true);
                            }
                        }
                    });

                    check1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if(b) {
                                name.setVisibility(View.GONE);
                                check2.setEnabled(false);
                            }
                            else{
                                name.setVisibility(View.GONE);
                                check2.setEnabled(true);
                            }
                        }
                    });

                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(!check1.isChecked() && !check2.isChecked()){
                                save.setBackgroundColor(Color.RED);
                            }
                            else if(check1.isChecked() && check2.isChecked()){
                                save.setBackgroundColor(Color.RED);
                            }
                            else if(check1.isChecked()){
                                //save data
                                barCheck=1;
                                bar.setBackgroundColor(getResources().getColor(R.color.button_clicked));
                                saveData("bar","");
                                Toast.makeText(EntityBasedReminderActivity.this, "Data will be added to database", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                            else if(check2.isChecked() && !name.getText().toString().equals("")) {
                                String data = name.getText().toString();
                                //save data
                                barCheck=1;
                                bar.setBackgroundColor(getResources().getColor(R.color.button_clicked));
                                saveData("bar",name.getText().toString());
                                Toast.makeText(EntityBasedReminderActivity.this, "Data will be added to database", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                            else{
                                save.setBackgroundColor(Color.RED);
                            }
                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                }
                else{
                    barCheck=0;
                    bar.setBackgroundColor(getResources().getColor(R.color.button_unclicked));
                    deleteData("bar");
                    Toast.makeText(EntityBasedReminderActivity.this, "Data will be deleted from database", Toast.LENGTH_SHORT).show();
                }
            }
        });

        lib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(libCheck==0){
                    // opens dialog box
                    final Dialog dialog = new Dialog(EntityBasedReminderActivity.this);
                    dialog.setContentView(R.layout.dialog);
                    dialog.setTitle("Set Condition");
                    final CheckBox check1 = (CheckBox) dialog.findViewById(R.id.check1);
                    final CheckBox check2 = (CheckBox) dialog.findViewById(R.id.check2);
                    final EditText name = (EditText) dialog.findViewById(R.id.entity_name);
                    final Button save = (Button) dialog.findViewById(R.id.save);
                    final Button cancel = (Button) dialog.findViewById(R.id.cancel);

                    dialog.show();

                    check2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if(b) {
                                name.setVisibility(View.VISIBLE);
                                check1.setEnabled(false);
                            }
                            else{
                                name.setVisibility(View.GONE);
                                check1.setEnabled(true);
                            }
                        }
                    });

                    check1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if(b) {
                                name.setVisibility(View.GONE);
                                check2.setEnabled(false);
                            }
                            else{
                                name.setVisibility(View.GONE);
                                check2.setEnabled(true);
                            }
                        }
                    });

                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(!check1.isChecked() && !check2.isChecked()){
                                save.setBackgroundColor(Color.RED);
                            }
                            else if(check1.isChecked() && check2.isChecked()){
                                save.setBackgroundColor(Color.RED);
                            }
                            else if(check1.isChecked()){
                                //save data
                                libCheck=1;
                                lib.setBackgroundColor(getResources().getColor(R.color.button_clicked));
                                saveData("library","");
                                Toast.makeText(EntityBasedReminderActivity.this, "Data will be added to database", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                            else if(check2.isChecked() && !name.getText().toString().equals("")) {
                                String data = name.getText().toString();
                                //save data
                                libCheck=1;
                                lib.setBackgroundColor(getResources().getColor(R.color.button_clicked));
                                saveData("library",name.getText().toString());
                                Toast.makeText(EntityBasedReminderActivity.this, "Data will be added to database", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                            else{
                                save.setBackgroundColor(Color.RED);
                            }
                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                }
                else{
                    libCheck=0;
                    lib.setBackgroundColor(getResources().getColor(R.color.button_unclicked));
                    deleteData("library");
                    Toast.makeText(EntityBasedReminderActivity.this, "Data will be deleted from database", Toast.LENGTH_SHORT).show();
                }
            }
        });

        movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(movieCheck==0){
                    //opens dialog box
                    final Dialog dialog = new Dialog(EntityBasedReminderActivity.this);
                    dialog.setContentView(R.layout.dialog);
                    dialog.setTitle("Set Condition");
                    final CheckBox check1 = (CheckBox) dialog.findViewById(R.id.check1);
                    final CheckBox check2 = (CheckBox) dialog.findViewById(R.id.check2);
                    final EditText name = (EditText) dialog.findViewById(R.id.entity_name);
                    final Button save = (Button) dialog.findViewById(R.id.save);
                    final Button cancel = (Button) dialog.findViewById(R.id.cancel);

                    dialog.show();

                    check2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if(b) {
                                name.setVisibility(View.VISIBLE);
                                check1.setEnabled(false);
                            }
                            else{
                                name.setVisibility(View.GONE);
                                check1.setEnabled(true);
                            }
                        }
                    });

                    check1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if(b) {
                                name.setVisibility(View.GONE);
                                check2.setEnabled(false);
                            }
                            else{
                                name.setVisibility(View.GONE);
                                check2.setEnabled(true);
                            }
                        }
                    });

                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(!check1.isChecked() && !check2.isChecked()){
                                save.setBackgroundColor(Color.RED);
                            }
                            else if(check1.isChecked() && check2.isChecked()){
                                save.setBackgroundColor(Color.RED);
                            }
                            else if(check1.isChecked()){
                                //save data
                                movieCheck=1;
                                movie.setBackgroundColor(getResources().getColor(R.color.button_clicked));
                                saveData("movie_theater","");
                                Toast.makeText(EntityBasedReminderActivity.this, "Data will be added to database", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                            else if(check2.isChecked() && !name.getText().toString().equals("")) {
                                String data = name.getText().toString();
                                //save data
                                movieCheck=1;
                                movie.setBackgroundColor(getResources().getColor(R.color.button_clicked));
                                saveData("movie_theater",name.getText().toString());
                                Toast.makeText(EntityBasedReminderActivity.this, "Data will be added to database", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                            else{
                                save.setBackgroundColor(Color.RED);
                            }
                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                }
                else{
                    movieCheck=0;
                    movie.setBackgroundColor(getResources().getColor(R.color.button_unclicked));
                    deleteData("movie_theater");
                    Toast.makeText(EntityBasedReminderActivity.this, "Data will be deleted from database", Toast.LENGTH_SHORT).show();
                }
            }
        });

        gov.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(govCheck==0){
                    //opens dialog box
                    final Dialog dialog = new Dialog(EntityBasedReminderActivity.this);
                    dialog.setContentView(R.layout.dialog);
                    dialog.setTitle("Set Condition");
                    final CheckBox check1 = (CheckBox) dialog.findViewById(R.id.check1);
                    final CheckBox check2 = (CheckBox) dialog.findViewById(R.id.check2);
                    final EditText name = (EditText) dialog.findViewById(R.id.entity_name);
                    final Button save = (Button) dialog.findViewById(R.id.save);
                    final Button cancel = (Button) dialog.findViewById(R.id.cancel);

                    dialog.show();

                    check2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if(b) {
                                name.setVisibility(View.VISIBLE);
                                check1.setEnabled(false);
                            }
                            else{
                                name.setVisibility(View.GONE);
                                check1.setEnabled(true);
                            }
                        }
                    });

                    check1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if(b) {
                                name.setVisibility(View.GONE);
                                check2.setEnabled(false);
                            }
                            else{
                                name.setVisibility(View.GONE);
                                check2.setEnabled(true);
                            }
                        }
                    });

                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(!check1.isChecked() && !check2.isChecked()){
                                save.setBackgroundColor(Color.RED);
                            }
                            else if(check1.isChecked() && check2.isChecked()){
                                save.setBackgroundColor(Color.RED);
                            }
                            else if(check1.isChecked()){
                                //save data
                                govCheck=1;
                                gov.setBackgroundColor(getResources().getColor(R.color.button_clicked));
                                saveData("local_government_office","");
                                Toast.makeText(EntityBasedReminderActivity.this, "Data will be added to database", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                            else if(check2.isChecked() && !name.getText().toString().equals("")) {
                                String data = name.getText().toString();
                                //save data
                                govCheck=1;
                                gov.setBackgroundColor(getResources().getColor(R.color.button_clicked));
                                saveData("local_government_office",name.getText().toString());
                                Toast.makeText(EntityBasedReminderActivity.this, "Data will be added to database", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                            else{
                                save.setBackgroundColor(Color.RED);
                            }
                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                }
                else{
                    govCheck=0;
                    gov.setBackgroundColor(getResources().getColor(R.color.button_unclicked));
                    deleteData("local_government_office");
                    Toast.makeText(EntityBasedReminderActivity.this, "Data will be deleted from database", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void saveData(String entity,String name){
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String currentDateTime = dateFormat.format(new Date()); // Find current time
        String indian_time[] = currentDateTime.split(":");
        int currTime = Integer.parseInt(indian_time[0]);
        if (currTime > 12)
            currTime = currTime - 12;
        currentDateTime = Integer.toString(currTime) + ":" + indian_time[1];
        Boolean isInserted = myDb.insertRecordEBR(entity,currentDateTime,name,"0.0","0.0");
        if (isInserted) {
            Toast.makeText(EntityBasedReminderActivity.this, "Inserted", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteData(String entity){
        Integer deleted = myDb.deleteData(entity);
        if(deleted<=0)
            Toast.makeText(this, "Nothing is deleted", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        active=true;
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        active=false;
        Toast.makeText(this, "Service will start", Toast.LENGTH_SHORT).show();
        Intent a1 = new Intent(getApplicationContext(), ReminderService.class);
        startService(a1);
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}