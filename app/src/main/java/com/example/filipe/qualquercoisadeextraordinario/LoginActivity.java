package com.example.filipe.qualquercoisadeextraordinario;


import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button SignUpButton;
    Button LoginButton;
    EditText Username1;
    EditText Password1;

    String Username1Str;

    Toast ToastSuccess;
    Toast ToastFailure;

    DatabaseHelper helper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SignUpButton = (Button) findViewById(R.id.SignUpButtonID);
        SignUpButton.setOnClickListener(this);

        LoginButton = (Button) findViewById(R.id.LoginButtonID);
        LoginButton.setOnClickListener(this);

        Username1 = (EditText) findViewById(R.id.UsernameTextID);

        Password1 = (EditText) findViewById(R.id.PasswordTextID);

        ToastFailure = Toast.makeText(this, "Incorrect user or password. Failed to Login", Toast.LENGTH_SHORT);
        ToastSuccess = Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.LoginButtonID:

                Username1Str = Username1.getText().toString();
                String Password1Str = Password1.getText().toString();

                String password = helper.searchPass(Username1Str);

                if (Password1Str.equals(password))
                {
                    //startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    Intent i = new Intent(this, DisplayPoolActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("username",Username1Str);
                    i.putExtras(bundle);
                    System.out.println( bundle.getString("username"));
                    startActivity(i);
                    ToastSuccess.show();
                    finish();
                }
                else
                {
                    ToastFailure.show();
                    Username1.setText("");
                    Password1.setText("");
                }
                break;

            case R.id.SignUpButtonID:
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                break;
        }
    }

}