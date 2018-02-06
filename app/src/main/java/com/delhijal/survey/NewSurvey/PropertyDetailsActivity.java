package com.delhijal.survey.NewSurvey;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.delhijal.survey.MainSurveyActivity;
import com.delhijal.survey.R;

public class PropertyDetailsActivity extends AppCompatActivity {
   Button pnext,pprevious;
   EditText etdno,etfloorno,etpropertyname,etstreet,etarea,etlandmark,etpin;
   String dno,floorno,propertyname,street,area,landmark,pin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_details);
        etdno=(EditText)findViewById(R.id.edithno);
        etfloorno=(EditText)findViewById(R.id.editFloorno);
        etpropertyname=(EditText)findViewById(R.id.editpname);
        etstreet=(EditText)findViewById(R.id.editstreetname);
        etarea=(EditText)findViewById(R.id.editarea);
        etlandmark=(EditText)findViewById(R.id.editlandmark);
        etpin=(EditText)findViewById(R.id.editpin);
        pnext=(Button)findViewById(R.id.pnext);
        pprevious=(Button)findViewById(R.id.pprevious);
        pnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dno=etdno.getText().toString();
                floorno=etfloorno.getText().toString();
                propertyname=etpropertyname.getText().toString();
                street=etstreet.getText().toString();
                area=etarea.getText().toString();
                landmark=etlandmark.getText().toString();
                pin=etpin.getText().toString();
                if(dno.equals("")||dno.length()<2){
                    etdno.setError("Enter min 2 chars");
                    etdno.setFocusable(true);
                }else if(floorno.equals("")){
                    etfloorno.setError("Enter the floor no");
                    etfloorno.setFocusable(true);
                }else if(propertyname.equals("")||propertyname.length()<3) {
                    etpropertyname.setError("Enter min 3 chars");
                    etpropertyname.setFocusable(true);
                }else if(street.equals("")||street.length()<3) {
                    etstreet.setError("Enter min 3 chars");
                    etstreet.setFocusable(true);
                }else if(area.equals("")||area.length()<3) {
                    etarea.setError("Enter min 3 chars");
                    etarea.setFocusable(true);
                }else if(landmark.equals("")||landmark.length()<3) {
                    etlandmark.setError("Enter min 3 chars");
                    etlandmark.setFocusable(true);
                }else if(pin.equals("")) {
                    etpin.setError("Enter pin number");
                    etpin.setFocusable(true);
                }else {
                    Intent i = new Intent(PropertyDetailsActivity.this, ConnectionDetailsActivity.class);
                    startActivity(i);
                }
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            Intent i=new Intent(this,MainSurveyActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
