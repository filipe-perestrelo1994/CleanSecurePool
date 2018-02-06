package com.example.filipe.qualquercoisadeextraordinario;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    DatabaseHelper helper = new DatabaseHelper(this);

    Button CreateButton;
    Button BackToLogin;
    EditText Username;
    EditText Pass;
    EditText PassConfirmed;
    EditText PoolName;

    String NullString = "";




    Toast ErrorToastGeneral;

    Toast RegisterToast;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        CreateButton = (Button)findViewById(R.id.CreateUserID);
        CreateButton.setOnClickListener(this);

        BackToLogin = (Button)findViewById(R.id.ButtonBackToLogin);
        BackToLogin.setOnClickListener(this);

        Username = (EditText) findViewById(R.id.UserNameCreateTextID);
        Pass = (EditText) findViewById(R.id.PasswordCreateTextID);
        PassConfirmed = (EditText)findViewById(R.id.ConfirmPasswordTextID);
        PoolName = (EditText)findViewById(R.id.PoolNameTextID);


    }

    @Override
    public void onClick(View v)
    {
        //User = Username.getText();
        //Pass1 = Pass.getText().toString();
        //Pass2 = PassConfirmed.getText().toString();

        String UserStr = Username.getText().toString();
        String PasswordStr = Pass.getText().toString();
        String PoolNameStr = PoolName.getText().toString();


        switch(v.getId())
        {
            case R.id.CreateUserID:
                if (isEmpty(Username) || isEmpty(Pass) || isEmpty(PassConfirmed) || isEmpty(PoolName) || !Pass.getText().toString().equals(PassConfirmed.getText().toString())) {
                    ErrorToastGeneral = Toast.makeText(this, "Invalid settings", Toast.LENGTH_SHORT);
                    ErrorToastGeneral.show();
                    Username.setText("");
                    Pass.setText("");
                    PassConfirmed.setText("");
                    PoolName.setText("");
                }
                else
                {
                    Contact U = new Contact();
                    U.setName(UserStr);
                    U.setPassword(PasswordStr);
                    U.setPoolName(PoolNameStr);

                    String UserNameAux = helper.searchUser(U.getName());

                    if (UserNameAux.equals(U.getName())) //Verifica se já existe um utilizador com o mesmo nome
                    {
                        Toast Failure = Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT);
                        Failure.show();
                    }
                    else
                    {
                        helper.insertUser(U);

                        RegisterToast = Toast.makeText(this, "Register successfull", Toast.LENGTH_SHORT);
                        RegisterToast.show();
                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));

                        System.out.println(helper.getReadableDatabase().toString());

                        Username.setText("");
                        Pass.setText("");
                        PassConfirmed.setText("");
                        PoolName.setText("");
                        finish();
                    }
                }
            break;

            case R.id.ButtonBackToLogin:
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
                break;
        }
    }
    private boolean isEmpty(EditText etText)
    {
        if (etText.getText().toString().trim().length() > 0) {
            return false;
        } else {
            return true;
        }
    }
}
