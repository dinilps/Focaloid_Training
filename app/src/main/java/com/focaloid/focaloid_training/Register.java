package com.focaloid.focaloid_training;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by dinil on 10-08-2016.
 */
public class Register extends AppCompatActivity {

    Button btnuserreg;
    EditText editTextUserName, editTextPassword, editTextConfirmPassword;

    DbAdapter databaseAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        final TextInputLayout usernameWrapper = (TextInputLayout) findViewById(R.id.usernameWrapper);
        final TextInputLayout password1Wrapper = (TextInputLayout) findViewById(R.id.password1Wrapper);
        final TextInputLayout password2Wrapper = (TextInputLayout) findViewById(R.id.password2Wrapper);

        usernameWrapper.setHint("Username");
        password1Wrapper.setHint("Password");
        password2Wrapper.setHint("Confirm Password");

        databaseAdapter = new DbAdapter(this);
        databaseAdapter = databaseAdapter.open();
        editTextUserName = (EditText) findViewById(R.id.usernamelog);
        editTextPassword = (EditText) findViewById(R.id.pass1);
        editTextConfirmPassword = (EditText) findViewById(R.id.pass2);

        btnuserreg = (Button) findViewById(R.id.btnsubmit);

        btnuserreg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                String userName = editTextUserName.getText().toString();
                String password = editTextPassword.getText().toString();
                String confirmPassword = editTextConfirmPassword.getText().toString();


                if (userName.equals("") || password.equals("") || confirmPassword.equals("")) {
                    Toast.makeText(getApplicationContext(), "Field Vaccant", Toast.LENGTH_LONG).show();
                    return;
                }
                // check if both password matches
                if (!password.equals(confirmPassword)) {
                    editTextConfirmPassword.setError("Password does not match");
                    //Toast.makeText(getApplicationContext(), "Password does not match", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    // Save the Data in Database
                    databaseAdapter.RegsterUser(userName, password);
                    Toast.makeText(getApplicationContext(), "Account Successfully Created ", Toast.LENGTH_LONG).show();
                    Intent mainIntent = new Intent(Register.this, MainActivity.class);
                    Register.this.finish();
                    startActivity(mainIntent);


                }

            }
        });


    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        databaseAdapter.close();

    }
}
