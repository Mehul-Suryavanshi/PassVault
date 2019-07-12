package com.example.passvault;

import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.multidots.fingerprintauth.AuthErrorCodes;
import com.multidots.fingerprintauth.FingerPrintAuthCallback;
import com.multidots.fingerprintauth.FingerPrintAuthHelper;

public class MainActivity extends AppCompatActivity implements FingerPrintAuthCallback {
    Button loginbtn;
    EditText editText_user, editText_pwd;
    LinearLayout layout;
    FingerPrintAuthHelper fingerPrintAuthHelper;
    TextView textView_try, textView_finger, textView_or, textView_try2;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fingerPrintAuthHelper = FingerPrintAuthHelper.getHelper(this, this);

        imageView = findViewById(R.id.imageView);
        textView_or = findViewById(R.id.txtview_or);
        textView_finger = findViewById(R.id.tv_finger);
        layout = findViewById(R.id.layout);
        loginbtn = findViewById(R.id.login_btn);
        editText_user = findViewById(R.id.login_user);
        editText_pwd = findViewById(R.id.login_pwd);
        textView_try = findViewById(R.id.textview_try);
        textView_try2 = findViewById(R.id.textview_try2);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user, pwd;
                user = editText_user.getText().toString().trim();
                pwd = editText_pwd.getText().toString().trim();
                //in case user name of password is not entered
                if ((user.length() == 0) || (pwd.length() == 0))
                    showerror("Please enter both username and password");
                else if (user.equals("passvault") && pwd.equals("passvault")) {
                    startActivity(new Intent(MainActivity.this, Info.class));
                    finish();
                } else
                    showerror("Invalid username or password");
            }
        });

        textView_try.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                makeinvisible();
                fingerPrintAuthHelper.startAuth();
            }
        });

        textView_try2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makevisible();
                fingerPrintAuthHelper.stopAuth();
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        //  fingerPrintAuthHelper.startAuth();
    }

    @Override
    protected void onPause() {
        super.onPause();
        fingerPrintAuthHelper.stopAuth();
    }

    @Override
    public void onNoFingerPrintHardwareFound() {
        //Device does not have finger print scanner.
        Toast.makeText(this, "No hardware for fingerprint scanning.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNoFingerPrintRegistered() {
        Toast.makeText(this, "Your device has no registered Fingerprints. First, please add a fingerprint to your device.", Toast.LENGTH_SHORT).show();
        //There are no finger prints registered on this device.
    }

    @Override
    public void onBelowMarshmallow() {
        //Device running below API 23 version of android that does not support finger print authentication.
        Toast.makeText(this, "API level below 23 doesnt support finger print scanning", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAuthSuccess(FingerprintManager.CryptoObject cryptoObject) {
        //Authentication sucessful.
        startActivity(new Intent(MainActivity.this, Info.class));
        finish();
    }

    @Override
    public void onAuthFailed(int errorCode, String errorMessage) {
        switch (errorCode) {    //Parse the error code for recoverable/non recoverable error.
            case AuthErrorCodes.CANNOT_RECOGNIZE_ERROR:
                //Cannot recognize the fingerprint scanned.
                Toast.makeText(this, "cannot recognise", Toast.LENGTH_SHORT).show();
                break;
            case AuthErrorCodes.NON_RECOVERABLE_ERROR:
                //This is not recoverable error. Try other options for user authentication. like pin, password.
                break;
            case AuthErrorCodes.RECOVERABLE_ERROR:
                //Any recoverable error. Display message to the user.
                break;
        }
    }

    private void makeinvisible() {
        layout.setVisibility(View.GONE);
        loginbtn.setVisibility(View.GONE);
        textView_or.setVisibility(View.GONE);
        textView_try.setVisibility(View.GONE);
        imageView.setImageResource(R.drawable.fingerprint);


        textView_finger.setVisibility(View.VISIBLE);
        textView_try2.setVisibility(View.VISIBLE);
    }


    private void makevisible() {
        layout.setVisibility(View.VISIBLE);
        loginbtn.setVisibility(View.VISIBLE);
        textView_or.setVisibility(View.VISIBLE);
        textView_try.setVisibility(View.VISIBLE);
        imageView.setImageResource(R.drawable.icon_pv_crop);

        textView_finger.setVisibility(View.GONE);
        textView_try2.setVisibility(View.GONE);
    }

    private void showerror(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Error");
        builder.setIcon(R.drawable.ic_error);
        builder.setMessage(s);
        builder.setCancelable(false);
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editText_pwd.getText().clear();
                editText_user.getText().clear();
            }
        });
        builder.show();
    }

}
