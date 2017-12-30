package com.delhijal.survey.NewSurvey;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.delhijal.survey.R;

public class PropertyDetailsActivity extends Activity {
   Button pnext,pprevious;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_details);
        pnext=(Button)findViewById(R.id.pnext);
        pprevious=(Button)findViewById(R.id.pprevious);
        pnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(PropertyDetailsActivity.this,ConnectionDetailsActivity.class);
                startActivity(i);
            }
        });

        pprevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(PropertyDetailsActivity.this,OwnerDetailsActivity.class);
                startActivity(i);
            }
        });
    }
}
