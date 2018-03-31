package com.delhijal.survey.UpdateSurvey;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.delhijal.survey.MainSurveyActivity;
import com.delhijal.survey.NewSurvey.OwnerDetailsActivity;
import com.delhijal.survey.R;
import com.delhijal.survey.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainUpdateActivity extends AppCompatActivity {
     Button submit;
     Spinner spuniqueid;
     String uniqueid1,uniqueid;
     SharedPreferences sharedpref,sharedpref1;
     SharedPreferences.Editor editor;
    private ArrayList<String> counts = new ArrayList<String>();
    ArrayList<String> list = new ArrayList<String>();
    ArrayAdapter<String> dataAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_update);
        spuniqueid=(Spinner)findViewById(R.id.spuniqueid);
        submit=(Button)findViewById(R.id.updatesubmitbtn);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                submit.setEnabled(false);
                uniqueid1 = spuniqueid.getSelectedItem().toString();
                //Splitting the values
                String[] parts = uniqueid1.split("-");
                uniqueid= parts[0].trim();
                   sharedpref = getSharedPreferences("personuniqueid", MODE_PRIVATE);
                   editor = sharedpref.edit();
                   editor.putString("uniqueid",uniqueid);
                   editor.commit();
                   new ReloadOwnerDetails().execute();

            }
        });
        new ReloadIdDetails().execute();

    }

    private class ReloadOwnerDetails extends AsyncTask<Void,Void,ArrayList<String>> {
        ProgressDialog loading;
        String response = null;

        @Override

        protected void onPreExecute () {
            super.onPreExecute();
           // loading = ProgressDialog.show(MainUpdateActivity.this, "Updating...", null,true,true);

        }


        @Override

        protected ArrayList<String> doInBackground (Void...voids){
            RequestHandler sh = new RequestHandler();
            String[] res=null;
            sharedpref = getSharedPreferences("personuniqueid",MODE_PRIVATE);
            String unique = sharedpref.getString("uniqueid",null);
            // Making a request to url and getting response

            sharedpref1 = getSharedPreferences("userdetails",MODE_PRIVATE);
            String loginuname = sharedpref1.getString("username",null);
            String loginmobileno = sharedpref1.getString("mobileno",null);

            String jsonStr = sh.makeServiceCall("http://www.globalmrbs.com/survey/getowner.php?id="+uniqueid+"&uname="+loginuname+"&umobile="+loginmobileno);
            if (jsonStr != null) {
                try {

                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONArray details= jsonObj.getJSONArray("result");

                    for (int i = 0; i < details.length(); i++) {

                        JSONObject d = details.getJSONObject(i);
                        String ownername = d.getString("name");
                        String ownerfather = d.getString("father");
                        String owneremail=d.getString("email");
                        String ownermobile=d.getString("mobile");
                        String status=d.getString("status");
                        String status2=d.getString("status2");
                        String status3=d.getString("status3");
                        String status4=d.getString("status4");
                        counts.add(ownername);
                        counts.add(ownerfather);
                        counts.add(ownermobile);
                        counts.add(owneremail);
                        counts.add(status);
                        counts.add(status2);
                        counts.add(status3);
                        counts.add(status4);


                    }

                } catch (final JSONException e) {
                    Log.e("Error","Exception");
                }
            } else {
                Toast.makeText(getApplicationContext(), "Couldn't get json from server", Toast.LENGTH_SHORT).show();
            }
            return counts;
        }

        @Override
        protected void onPostExecute(final ArrayList<String> ss){
            super.onPostExecute(ss);
            if(ss.size()>0){
            sharedpref = getSharedPreferences("persondetails", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpref.edit();

            editor.putString("personname", ss.get(0));
            editor.putString("pfathername",ss.get(1));
            editor.putString("mobilenumber",ss.get(2));
            editor.putString("personemail",ss.get(3));
                editor.putString("personstatus", ss.get(4));
                editor.putString("personstatus2", ss.get(5));
                editor.putString("personstatus3", ss.get(6));
                editor.putString("personstatus4", ss.get(7));
             editor.commit();
            Intent i=new Intent(MainUpdateActivity.this, OwnerDetailsActivity.class);
            startActivity(i);
            finish();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Sorry!,No Data found with these details",Toast.LENGTH_LONG).show();
            }

        }
    }

    private class ReloadIdDetails extends AsyncTask<Void,Void,ArrayList<String>> {

        @Override

        protected void onPreExecute () {
            super.onPreExecute();

        }


        @Override

        protected ArrayList<String> doInBackground (Void...voids){
            RequestHandler sh = new RequestHandler();
            sharedpref1 = getSharedPreferences("userdetails",MODE_PRIVATE);
            String loginuname = sharedpref1.getString("username",null);
            String loginmobileno = sharedpref1.getString("mobileno",null);

            String jsonStr = sh.makeServiceCall("http://www.globalmrbs.com/survey/getincompleteids.php?uname="+loginuname+"&umobile="+loginmobileno);
            if (jsonStr != null) {
                try {

                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONArray details= jsonObj.getJSONArray("result");
                    for (int i = 0; i < details.length(); i++) {
                        JSONObject d = details.getJSONObject(i);
                        String myid= d.getString("sid");
                        String name=d.getString("name");
                        list.add(myid+" - "+name);

                    }

                } catch (final JSONException e) {
                    Log.e("ERROR","EXCEPTION");
                }
            }
            else {
               // Toast.makeText(getApplicationContext(), "Couldn't get json from server", Toast.LENGTH_SHORT).show();
            }
            return list ;
        }

        @Override
        protected void onPostExecute(final ArrayList<String> list){
            super.onPostExecute(list);

            Collections.sort(list);
            //Toast.makeText(getApplicationContext(),list.toString(),Toast.LENGTH_LONG).show();
            dataAdapter = new ArrayAdapter<String>
                    (getApplicationContext(), android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spuniqueid.setAdapter(dataAdapter);
        }
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
