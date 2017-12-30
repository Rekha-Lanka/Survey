package com.delhijal.survey.NewSurvey;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.delhijal.survey.R;

public class SubmitDetailsActivity extends Activity {
 Button sprevious,submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_details);
        sprevious=(Button)findViewById(R.id.sprevious);
        submit=(Button)findViewById(R.id.submit);
        sprevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SubmitDetailsActivity.this,ConnectionDetailsActivity.class);
                startActivity(i);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i=new Intent(SubmitDetailsActivity.this,OwnerDetailsActivity.class);
//                startActivity(i);
            }
        });
    }
}
