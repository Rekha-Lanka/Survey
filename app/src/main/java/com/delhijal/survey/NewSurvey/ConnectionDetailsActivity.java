package com.delhijal.survey.NewSurvey;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.delhijal.survey.R;

public class ConnectionDetailsActivity extends Activity {
    Button cnext,cprevious;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_details);
        cnext=(Button)findViewById(R.id.cnext);
        cprevious=(Button)findViewById(R.id.cprevious);
        cnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ConnectionDetailsActivity.this,SubmitDetailsActivity.class);
                startActivity(i);
            }
        });

        cprevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ConnectionDetailsActivity.this,PropertyDetailsActivity.class);
                startActivity(i);
            }
        });
    }
}
