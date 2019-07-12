package com.example.passvault;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Vector;

public class View extends AppCompatActivity {
    Button exit, clear, show;
    TextView desc, uname, pwd;
    int length;
    DataBase dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        dataBase = new DataBase(View.this);

        exit = findViewById(R.id.view_exit);
        clear = findViewById(R.id.view_clear);
        show = findViewById(R.id.view_show);
        desc = findViewById(R.id.view_desc);
        uname = findViewById(R.id.view_user);
        pwd = findViewById(R.id.view_pwd);

        clear.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                desc.setText("DESCRIPTION : ");
                uname.setText("USERNAME : ");
                pwd.setText("PASSWORD : ");
                Toast.makeText(View.this, "All fields Cleared", Toast.LENGTH_SHORT).show();
                show.setText("View a Password");
            }
        });

        exit.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                Toast.makeText(View.this, "Close", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        show.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(final android.view.View v) {
                PopupMenu popupMenu = new PopupMenu(View.this, show);
                popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());

                {

                    Vector vector = dataBase.getDescription();
                    String string[] = new String[vector.size()];
                    vector.copyInto(string);
                    length = vector.size();
                    if (length == 0)
                        showMessage("No Record Found");
                    else {
                        for (int i = 0; i < length; i++)
                            popupMenu.getMenu().add(0, i, 0, string[i]);
                    }

                }

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(View.this, "You Selected " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        show.setText(item.getTitle());

                        Vector vector = dataBase.recordAt(item.getTitle().toString().trim());

                        desc.setText("DESCRIPTION : " + vector.elementAt(0).toString());
                        uname.setText("USERNAME : " + vector.elementAt(1).toString());
                        pwd.setText("PASSWORD : " + vector.elementAt(2).toString());
                        return true;

                    }
                });
                popupMenu.show();
            }

        });

    }

    public void showMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Error");
        builder.setMessage(message);
        builder.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
