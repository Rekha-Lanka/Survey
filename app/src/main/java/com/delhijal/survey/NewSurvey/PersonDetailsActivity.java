package com.delhijal.survey.NewSurvey;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.delhijal.survey.MainSurveyActivity;
import com.delhijal.survey.R;

public class PersonDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_details);
        Button next=(Button)findViewById(R.id.mainnext);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(PersonDetailsActivity.this,OwnerDetailsActivity.class);
                startActivity(i);
            }
        });

    }
    @Override
    public boolean onSupportNavigateUp() {
//        onBackPressed();
        Intent i=new Intent(PersonDetailsActivity.this,MainSurveyActivity.class);
        startActivity(i);
       return true;
    }
}
