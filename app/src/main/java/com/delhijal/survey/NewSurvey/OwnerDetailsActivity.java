package com.delhijal.survey.NewSurvey;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.delhijal.survey.MainSurveyActivity;
import com.delhijal.survey.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OwnerDetailsActivity extends AppCompatActivity {
Button onext,oprevious;
String oname,ofname,omobileno,oemail,catogery;
EditText etoname,etofname,etomobileno,etoemail;
SharedPreferences sharedPreferences;
    Spinner spinnerDetails;
boolean is_mob_number=false;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_details);
        etoname=(EditText)findViewById(R.id.editOname);
        etofname=(EditText)findViewById(R.id.editOfathername);
        etomobileno=(EditText)findViewById(R.id.editOmobileno);
        etoemail=(EditText)findViewById(R.id.editOemail);
        spinnerDetails=(Spinner)findViewById(R.id.ownerspinner);
        catogery = spinnerDetails.getSelectedItem().toString();
       onext=(Button)findViewById(R.id.onext);
       oprevious=(Button)findViewById(R.id.oprevious);
        sharedPreferences = getSharedPreferences("persondetails",MODE_PRIVATE);
        final String ownername = sharedPreferences.getString("personname",null);
        final String ownerfname = sharedPreferences.getString("pfathername",null);
        final String ownermno = sharedPreferences.getString("mobilenumber",null);
        final String owneremail = sharedPreferences.getString("personemail",null);
        spinnerDetails.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position==1){
                    // parent.getItemAtPosition(pos)
                    etoname.setText(ownername);
                    etofname.setText(ownerfname);
                    etomobileno.setText(ownermno);
                    etoemail.setText(owneremail);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
       onext.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               oname=etoname.getText().toString();
               ofname=etofname.getText().toString();
               omobileno=etomobileno.getText().toString();
               oemail=etoemail.getText().toString();
               if(oname.equals("")||oname.length()<3){
                   etoname.setError("Enter min 3 chars username");
                   etoname.setFocusable(true);
               }else if(ofname.equals("")||ofname.length()<3){
                   etofname.setError("Enter min 3 chars username");
                   etofname.setFocusable(true);
               }else if(omobileno.equals("")|| !ismobileno()) {
                   etomobileno.setError("Enter valid Mobile no");
                   etomobileno.setFocusable(true);
               }else if(oemail.equals("")|| !oemail.matches(emailPattern)) {
                   etoemail.setError("Enter valid email");
                   etoemail.setFocusable(true);
               }else if(spinnerDetails.getSelectedItem().toString().trim().equalsIgnoreCase("choose- Tap Here")){
                       TextView errorText = (TextView)spinnerDetails.getSelectedView();
                       errorText.setError("select catogery");
                       errorText.setTextColor(Color.RED);//just to highlight that this is an error
                       // errorText.setText("select catogery");//changes the selected item text to this
               }
//               }else if(spinnerDetails.getSelectedItem().toString().trim().equalsIgnoreCase("choose- Tap Here")){
//                   TextView errorText = (TextView)spinnerDetails.getSelectedView();
//                   errorText.setError("");
//                   errorText.setTextColor(Color.RED);//just to highlight that this is an error
//                   errorText.setText("select catogery");//changes the selected item text to this
//
//               }
               //if(spinnerDetails.getSelectedItem().toString().trim().equalsIgnoreCase("yes"))
                  else {

                   Intent i=new Intent(OwnerDetailsActivity.this,PropertyDetailsActivity.class);
                   startActivity(i);
               }

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
    public boolean ismobileno() {

// mobile number should be 10 digit
    Pattern pattern = Pattern.compile("\\d{10}");
    Matcher matchr = pattern.matcher(omobileno.trim());
    if (matchr.matches()) {
        is_mob_number = true;
    }
    return is_mob_number;
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
