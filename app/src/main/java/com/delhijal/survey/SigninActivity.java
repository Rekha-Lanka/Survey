package com.delhijal.survey;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.validation.Validator;

public class SigninActivity extends AppCompatActivity {
    EditText etuname,etmobileno;
    String uname,mobileno;
    SharedPreferences preferences;
    boolean is_mob_number = false;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        Button signin=(Button)findViewById(R.id.signin);
        etuname=(EditText)findViewById(R.id.etname);
        etmobileno=(EditText)findViewById(R.id.etmobileno);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // tv.setText("");
                uname=etuname.getText().toString();
                mobileno=etmobileno.getText().toString();
                if(uname.equals("")||uname.length()<3){
                    etuname.setError("Enter min 3 chars username");
                    etuname.setFocusable(true);

                }else if(mobileno.equals("")|| !ismobileno()) {
                    etmobileno.setError("Enter valid Password");
                    etmobileno.setFocusable(true);

                }
else {
                    preferences = getSharedPreferences("userdetails", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    String username = etuname.getText().toString();
                    String mno = etmobileno.getText().toString();
                    editor.putString("username", username);
                    editor.putString("mobileno", mno);
                    editor.commit();
                    Intent i = new Intent(SigninActivity.this, VerifyOTPActivity.class);
                    startActivity(i);
                }

            }
        });


    }

    public boolean ismobileno() {

// mobile number should be 10 digit
        Pattern pattern = Pattern.compile("\\d{10}");
        Matcher matchr = pattern.matcher(mobileno.trim());
        if (matchr.matches()) {
            is_mob_number = true;
        }
        return is_mob_number;
    }


}
