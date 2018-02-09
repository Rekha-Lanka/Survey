package com.delhijal.survey.UpdateSurvey;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.delhijal.survey.NewSurvey.OwnerDetailsActivity;
import com.delhijal.survey.R;

public class MainUpdateActivity extends AppCompatActivity {
     Button submit;
     EditText etuniqueid;
     String uniqueid;
     SharedPreferences sharedpref;
     SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_update);
        etuniqueid=(EditText)findViewById(R.id.etuniqueid);
        submit=(Button)findViewById(R.id.updatesubmitbtn);
//        sharedPreferences = getSharedPreferences("persondetails",MODE_PRIVATE);
//        final String ownername = sharedPreferences.getString("personname",null);
//        final String ownerfname = sharedPreferences.getString("pfathername",null);
//        final String ownermno = sharedPreferences.getString("mobilenumber",null);
//        final String owneremail = sharedPreferences.getString("personemail",null);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               uniqueid=etuniqueid.getText().toString();

               if(uniqueid.equals("")||uniqueid.equals(null)){
                   etuniqueid.setError("Enter valid Unique id");
                   etuniqueid.setFocusable(true);
               }
               else{
                   sharedpref = getSharedPreferences("personuniqueid", MODE_PRIVATE);
                   editor = sharedpref.edit();
                   editor.putString("uniqueid",uniqueid);
                   editor.commit();
//                   sharedPreferences = getSharedPreferences("uniquesurveyid",MODE_PRIVATE);
//                   String unique = sharedPreferences.getString("surveyid",null);
                   Intent i=new Intent(MainUpdateActivity.this, OwnerDetailsActivity.class);
                   startActivity(i);
                   }
            }
        });
    }
}
