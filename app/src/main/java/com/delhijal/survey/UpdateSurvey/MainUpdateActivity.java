package com.delhijal.survey.UpdateSurvey;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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
     String uniqueid;
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
//        sharedPreferences = getSharedPreferences("persondetails",MODE_PRIVATE);
//        final String ownername = sharedPreferences.getString("personname",null);
//        final String ownerfname = sharedPreferences.getString("pfathername",null);
//        final String ownermno = sharedPreferences.getString("mobilenumber",null);
//        final String owneremail = sharedPreferences.getString("personemail",null);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              // uniqueid=etuniqueid.getText().toString();
               // uniqueid=spinnerbloodgroup.getSelectedItem().toString();
//               if(uniqueid.equals("")||uniqueid.equals(null)){
//                   etuniqueid.setError("Enter valid Unique id");
//                   etuniqueid.setFocusable(true);
//               }
             //  else{
                    uniqueid = spuniqueid.getSelectedItem().toString();
                   sharedpref = getSharedPreferences("personuniqueid", MODE_PRIVATE);
                   editor = sharedpref.edit();
                   editor.putString("uniqueid",uniqueid);
                   editor.commit();
                   new ReloadOwnerDetails().execute();
//                   sharedPreferences = getSharedPreferences("uniquesurveyid",MODE_PRIVATE);
//                   String unique = sharedPreferences.getString("surveyid",null);

                //   }

            }
        });
        new ReloadIdDetails().execute();
        /*spuniqueid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String item=spuniqueid.getSelectedItem().toString();
                Toast.makeText(getApplicationContext(), item,
                        Toast.LENGTH_LONG).show();

                Log.e("Item",item);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/



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

            String jsonStr = sh.makeServiceCall("http://www.globalm.co.in/survey/getowner.php?id="+uniqueid+"&uname="+loginuname+"&umobile="+loginmobileno);
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
                        counts.add(ownername);
                        counts.add(ownerfather);
                        counts.add(ownermobile);
                        counts.add(owneremail);


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
//                    String pusername = ename.getText().toString();
//                    String pfathername=
//                    String pno = emobile.getText().toString();
            editor.putString("personname", ss.get(0));
            editor.putString("pfathername",ss.get(1));
            editor.putString("mobilenumber",ss.get(2));
            editor.putString("personemail",ss.get(3));

//            etoname.setText(counts.get(0));
//            etofname.setText(counts.get(1));
//            etomobileno.setText(counts.get(2));
//            etoemail.setText(counts.get(3));
            editor.commit();
//            today.setText(ss.get(1));
//            total.setText(ss.get(0));
           // loading.dismiss();
            Intent i=new Intent(MainUpdateActivity.this, OwnerDetailsActivity.class);
            startActivity(i);}
            else
            {
                Toast.makeText(getApplicationContext(),"Sorry!,No Data found with these details",Toast.LENGTH_LONG).show();
            }

        }
    }

    private class ReloadIdDetails extends AsyncTask<Void,Void,ArrayList<String>> {
       // ProgressDialog loading;
       // String response = null;

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

            String jsonStr = sh.makeServiceCall("http://www.globalm.co.in/survey/getincompleteids.php?uname="+loginuname+"&umobile="+loginmobileno);
            if (jsonStr != null) {
                try {

                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONArray details= jsonObj.getJSONArray("result");
                    for (int i = 0; i < details.length(); i++) {
                        JSONObject d = details.getJSONObject(i);
                        String myid= d.getString("sid");
                        list.add(myid);



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

}
