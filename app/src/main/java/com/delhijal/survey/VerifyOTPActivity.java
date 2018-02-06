package com.delhijal.survey;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class VerifyOTPActivity extends AppCompatActivity {
   EditText etotp;
   String otp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        etotp=(EditText)findViewById(R.id.editotp);
        Button verifyotp=(Button)findViewById(R.id.verifyotp);
        verifyotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otp=etotp.getText().toString();
                if(otp.equals("")|| !otp.equals("83tsU")){
                    etotp.setError("Enter Correct OTP");
                    etotp.setFocusable(true);

                }
                else {
                    Intent intent = new Intent(VerifyOTPActivity.this, MainSurveyActivity.class);
                    startActivity(intent);
                }
            }
        });

    }
}
