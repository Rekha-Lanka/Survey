package com.delhijal.survey.NewSurvey;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.delhijal.survey.R;

public class OwnerDetailsActivity extends Activity {
Button onext,oprevious;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_details);
       onext=(Button)findViewById(R.id.onext);
       oprevious=(Button)findViewById(R.id.oprevious);
       onext.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i=new Intent(OwnerDetailsActivity.this,PropertyDetailsActivity.class);
               startActivity(i);
           }
       });

        oprevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(OwnerDetailsActivity.this,PersonDetailsActivity.class);
                startActivity(i);
            }
        });
    }
}
