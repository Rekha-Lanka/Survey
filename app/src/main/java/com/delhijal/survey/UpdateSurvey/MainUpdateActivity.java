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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.delhijal.survey.NewSurvey.OwnerDetailsActivity;
import com.delhijal.survey.R;
import com.delhijal.survey.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainUpdateActivity extends AppCompatActivity {
     Button submit;
     EditText etuniqueid;
     String uniqueid;
     SharedPreferences sharedpref;
     SharedPreferences.Editor editor;
    private ArrayList<String> counts = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_update);
        etuniqueid=(EditText)findViewById(R.id.etuniqueid);
        submit=(Button)findViewById(R.id.updatesubmitbtn);
//        sharedPreferences = getSharedPreferences("persondetails",MODE_PRIVATE);
//        final String ownername = sharedPreferences.getString("personname",null);
//        final String ownerfname = sharedPreferences.getString("pfathername",null);
//        final String ownermno = sharedPreferences.getString("mobilenumber",null);
//        final String owneremail = sharedPreferences.getString("personemail",null);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               uniqueid=etuniqueid.getText().toString();

               if(uniqueid.equals("")||uniqueid.equals(null)){
                   etuniqueid.setError("Enter valid Unique id");
                   etuniqueid.setFocusable(true);
               }
               else{
                   sharedpref = getSharedPreferences("personuniqueid", MODE_PRIVATE);
                   editor = sharedpref.edit();
                   editor.putString("uniqueid",uniqueid);
                   editor.commit();
                   new ReloadOwnerDetails().execute();
//                   sharedPreferences = getSharedPreferences("uniquesurveyid",MODE_PRIVATE);
//                   String unique = sharedPreferences.getString("surveyid",null);

                   }

            }
        });
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

            String jsonStr = sh.makeServiceCall("http://www.globalm.co.in/survey/getowner.php?id="+uniqueid);
            if (jsonStr != null) {
                try {

                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONArray details= jsonObj.getJSONArray("result");

                    for (int i = 0; i < details.length(); i++) {

                        JSONObject d = details.getJSONObject(i);
                        String ownername = d.getString("name");

                        String ownerfather = d.getString("father");
                        String owneremail=d.getString("mobile");
                        String ownermobile=d.getString("email");
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
            sharedpref = getSharedPreferences("persondetails", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpref.edit();
//                    String pusername = ename.getText().toString();
//                    String pfathername=
//                    String pno = emobile.getText().toString();
            editor.putString("personname", counts.get(0));
            editor.putString("pfathername",counts.get(1));
            editor.putString("mobilenumber",counts.get(2));
            editor.putString("personemail",counts.get(3));

//            etoname.setText(counts.get(0));
//            etofname.setText(counts.get(1));
//            etomobileno.setText(counts.get(2));
//            etoemail.setText(counts.get(3));
            editor.commit();
//            today.setText(ss.get(1));
//            total.setText(ss.get(0));
           // loading.dismiss();
            Intent i=new Intent(MainUpdateActivity.this, OwnerDetailsActivity.class);
            startActivity(i);

        }

    }

}
