package com.delhijal.survey.NewSurvey;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.delhijal.survey.MainSurveyActivity;
import com.delhijal.survey.R;
import com.delhijal.survey.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OwnerDetailsActivity extends AppCompatActivity {
Button onext,osubmit,oprevious;
String oname,ofname,omobileno,oemail,catogery;
EditText etoname,etofname,etomobileno,etoemail;
SharedPreferences sharedPreferences;
TextView status2text;
Spinner spinnerDetails;
boolean is_mob_number=false;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public static final String NAME_KEY = "name";
    public static final String FATHERNAME_KEY = "father";
    public static final String MOBILE_KEY = "mobile";
    public static final String EMAIL_KEY = "email";
    public static final String UPLOAD_KEY = "id";
    RequestHandler rh = new RequestHandler();
    private ArrayList<String> counts = new ArrayList<String>();

    public static final String MyPREFERENCES = "MyPre" ;//file name
    SharedPreferences personpref;
    SharedPreferences.Editor editor;
    public static final String UPLOAD_URL = "http://www.globalmrbs.comj/survey/insertowner.php";
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
       osubmit=(Button)findViewById(R.id.osubmit);
       oprevious=(Button)findViewById(R.id.oprevious);
       status2text=findViewById(R.id.status2text);

        sharedPreferences = getSharedPreferences("persondetails",MODE_PRIVATE);
        final String ownername = sharedPreferences.getString("personname",null);
        final String ownerfname = sharedPreferences.getString("pfathername",null);
        final String ownermno = sharedPreferences.getString("mobilenumber",null);
        final String owneremail = sharedPreferences.getString("personemail",null);
        final String status2 = sharedPreferences.getString("personstatus2",null);

       if(status2!=null) {
           if (status2.equalsIgnoreCase("completed")) {
               osubmit.setEnabled(false);
               status2text.setText(status2);

           }
       }
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
                if(position==2){
                    // parent.getItemAtPosition(pos)
                    etoname.setText("");
                    etofname.setText("");
                    etomobileno.setText("");
                    etoemail.setText("");
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
       onext.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i=new Intent(OwnerDetailsActivity.this,PropertyDetailsActivity.class);
               startActivity(i);
               finish();

           }
       });
       osubmit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               catogery = spinnerDetails.getSelectedItem().toString();
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
               }
               else if (spinnerDetails.getSelectedItem().toString().trim().equalsIgnoreCase("Choose- Tap Here")) {
                   TextView errorText = (TextView) spinnerDetails.getSelectedView();
                   errorText.setError("select catogery");
                   errorText.setTextColor(Color.RED);//just to highlight that this is an error
                   //errorText.setText("select catogery");//changes the selected item text to this
               }
               else {
                   uploadOwnerDetails();
               }
           }
       });

        oprevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(OwnerDetailsActivity.this,PersonDetailsActivity.class);
                startActivity(i);
                finish();
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

    private void uploadOwnerDetails(){
        class UploadOwner extends AsyncTask<String,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(OwnerDetailsActivity.this, "Uploading...", null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),"Successfully inserted",Toast.LENGTH_LONG).show();

            }
            @Override
            protected String doInBackground(String...args) {
                //String s=new String();
                //Toast.makeText(getApplicationContext(),"hello do in",Toast.LENGTH_LONG).show();
                oname=etoname.getText().toString();
                ofname=etofname.getText().toString();
                omobileno=etomobileno.getText().toString();
                oemail=etoemail.getText().toString();
                sharedPreferences = getSharedPreferences("personuniqueid",MODE_PRIVATE);
                String unique = sharedPreferences.getString("uniqueid",null);
               HashMap<String,String> data = new HashMap<>();
                data.put(NAME_KEY,oname);
                data.put(FATHERNAME_KEY,ofname);
                data.put(MOBILE_KEY,omobileno);
                data.put(EMAIL_KEY,oemail);
                data.put(UPLOAD_KEY,unique);
               String result = rh.sendPostRequest(UPLOAD_URL,data);
                return result;

            }
      }

        UploadOwner ui = new UploadOwner();
        String name = "hi";
        ui.execute(name);
    }


}
