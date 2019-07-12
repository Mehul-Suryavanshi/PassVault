package com.example.passvault;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Add extends AppCompatActivity {
    Button clear, save;
    EditText desc, user, pwd;
    DataBase dataBase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        dataBase = new DataBase(Add.this);
        clear = findViewById(R.id.add_clear);
        save = findViewById(R.id.add_save);
        desc = findViewById(R.id.add_desc);
        user = findViewById(R.id.add_user);
        pwd = findViewById(R.id.add_pwd);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desc.setText("");
                user.setText("");
                pwd.setText("");
                Toast.makeText(Add.this, "Fields Cleared", Toast.LENGTH_SHORT).show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String d, u, p;
                d = desc.getText().toString().trim();
                u = user.getText().toString().trim();
                p = pwd.getText().toString().trim();
                if ((d.length() == 0) && (u.length() == 0) && (p.length() == 0))
                    showerror("Above Fields cannot be empty.");
                else {
                    dataBase.insertRecord(d,u,p);
                    Toast.makeText(Add.this, "Password Saved", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private void showerror(String s) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Add.this);
        builder.setTitle("Error");
        builder.setIcon(R.drawable.ic_error);
        builder.setMessage(s);
        builder.setCancelable(true);
        builder.show();
    }
}
