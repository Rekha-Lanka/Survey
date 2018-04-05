package com.delhijal.survey.NewSurvey;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.delhijal.survey.MainSurveyActivity;
import com.delhijal.survey.R;
import com.delhijal.survey.RequestHandler;

import java.util.HashMap;

public class ConnectionDetailsActivity extends AppCompatActivity {
    Button cnext,cprevious,csubmit;
    EditText etconn,etmno,etmmake,etmreading,etcount,etraisedbill,etpaidbill,etborewellsno;
    Spinner sourcespinner,djbspinner,billspinner,chargespinner;
    String conn,mno,mmake,mreading,conncount,raisedbill,paidbill,borewellsno,sourceespinn,djbspinn,billspinn,chargespinn;
    SharedPreferences pref;
    Handler h = new Handler();
    RequestHandler rh = new RequestHandler();
    public static final String UPLOAD_URL = "http://www.globalmrbs.com/survey/insertwater.php";

    public static final String WATERSRC_KEY = "watersrc";
    public static final String DJBSTATUS_KEY = "djbstat";
    public static final String KNO_KEY = "kno";
    public static final String BILLDEL_KEY = "billdel";
    public static final String METERNO_KEY = "meterno";
    public static final String METERMAKE_KEY = "metermake";
    public static final String METERREAD_KEY = "meterread";
    public static final String UNAUTHCON_KEY = "unauthcon";
    public static final String BILLRAISE_KEY = "billraise";
    public static final String BILLPAID_KEY = "billpaid";
    public static final String BOREWELLS_KEY = "borewells";
    public static final String SWERAGEPAID_KEY = "sweragepaid";
    public static final String UPLOAD_KEY = "id";
    SharedPreferences sharedPreferences;
    TextView status4text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_details);

        cnext=(Button)findViewById(R.id.cnext);
        csubmit=(Button)findViewById(R.id.csubmit);
        cprevious=(Button)findViewById(R.id.cprevious);
        //EditText etconn,etmno,etmmake,etmreading,etcount,etraisedbill,etpaidbill,etborewellsno;
        etconn=(EditText)findViewById(R.id.editconnno);
        etmno=(EditText)findViewById(R.id.editmno);
        etmmake=(EditText)findViewById(R.id.editmmake);
        etmreading=(EditText)findViewById(R.id.editmread);
        etcount=(EditText)findViewById(R.id.editcount);
        etraisedbill=(EditText)findViewById(R.id.editbillraised);
        etpaidbill=(EditText)findViewById(R.id.editbillpaid);
        etborewellsno=(EditText)findViewById(R.id.editborewells);
        //Spinner sourcespinner,djbspinner,billspinner,chargespinner;
        sourcespinner=(Spinner) findViewById(R.id.sourcespinner);
        djbspinner=(Spinner) findViewById(R.id.djbspinner);
        chargespinner=(Spinner) findViewById(R.id.spinnercharge);
        billspinner=(Spinner)findViewById(R.id.billdelvryspinner);
        status4text=findViewById(R.id.status4text);

        sharedPreferences = getSharedPreferences("persondetails",MODE_PRIVATE);
        final String status4 = sharedPreferences.getString("personstatus4",null);
        if(status4!=null) {
            if (status4.equalsIgnoreCase("completed")) {
                csubmit.setEnabled(false);
                csubmit.setBackgroundColor(Color.GRAY);

                status4text.setText("Already Water Details Submitted");

            }
        }

        cnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ConnectionDetailsActivity.this,SubmitDetailsActivity.class);
                startActivity(i);
            }
        });
         csubmit.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 conn = etconn.getText().toString();
                 mno = etmno.getText().toString();
                 mmake = etmmake.getText().toString();
                 mreading = etmreading.getText().toString();
                 conncount = etcount.getText().toString();
                 raisedbill = etraisedbill.getText().toString();
                 paidbill = etpaidbill.getText().toString();
                 borewellsno = etborewellsno.getText().toString();
                 sourceespinn = sourcespinner.getSelectedItem().toString();
                 djbspinn = djbspinner.getSelectedItem().toString();
                 billspinn = billspinner.getSelectedItem().toString();
                 chargespinn = chargespinner.getSelectedItem().toString();
                 if (conn.equals("") || conn.length() < 2) {
                     etconn.setError("Enter connection details");
                     etconn.setFocusable(true);
                 } else if (mno.equals("")) {
                     etmno.setError("Enter meter no");
                     etmno.setFocusable(true);
                 } else if (mmake.equals("")){
                     etmmake.setError("Enter meter details");
                     etmmake.setFocusable(true);
                 } else if (mreading.equals("")) {
                     etmreading.setError("Enter meter reading");
                     etmreading.setFocusable(true);
                 } else if (conncount.equals("")) {
                     etcount.setError("Enter connection count");
                     etcount.setFocusable(true);
                 } else if (raisedbill.equals("")) {
                     etraisedbill.setError("Enter raised bill");
                     etraisedbill.setFocusable(true);
                 } else if (paidbill.equals("")) {
                     etpaidbill.setError("Enter pin number");
                     etpaidbill.setFocusable(true);
                 }else if(borewellsno.equals("")){
                     etborewellsno.setError("Enter no.of borewells");
                     etborewellsno.setFocusable(true);
                 }else if (sourcespinner.getSelectedItem().toString().trim().equalsIgnoreCase("Choose - Tap Here")) {
                     TextView errorText = (TextView) sourcespinner.getSelectedView();
                     errorText.setError("select catogery");
                     errorText.setTextColor(Color.RED);//just to highlight that this is an error
                     //errorText.setText("select catogery");//changes the selected item text to this
                 }else if (djbspinner.getSelectedItem().toString().trim().equalsIgnoreCase("Choose - Tap Here")) {
                     TextView errorText = (TextView) djbspinner.getSelectedView();
                     errorText.setError("select catogery");
                     errorText.setTextColor(Color.RED);//just to highlight that this is an error
                     //errorText.setText("select catogery");//changes the selected item text to this
                 }else if (billspinner.getSelectedItem().toString().trim().equalsIgnoreCase("Choose- Tap Here")) {
                     TextView errorText = (TextView) billspinner.getSelectedView();
                     errorText.setError("select catogery");
                     errorText.setTextColor(Color.RED);//just to highlight that this is an error
                     //errorText.setText("select catogery");//changes the selected item text to this
                 }else if (chargespinner.getSelectedItem().toString().trim().equalsIgnoreCase("Choose- Tap Here")) {
                     TextView errorText = (TextView) chargespinner.getSelectedView();
                     errorText.setError("select catogery");
                     errorText.setTextColor(Color.RED);//just to highlight that this is an error
                     //errorText.setText("select catogery");//changes the selected item text to this
                 }
                 else {
                     uploadwaterdetails();
                 }
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



    private void uploadwaterdetails() {
        class UploadWaterConn extends AsyncTask<String,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ConnectionDetailsActivity.this, "Uploading...", null,true,true);
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
                conn = etconn.getText().toString();
                mno=etmno.getText().toString();
                mmake=etmmake.getText().toString();
                mreading=etmreading.getText().toString();
                conncount=etcount.getText().toString();
                raisedbill=etraisedbill.getText().toString();
                paidbill=etpaidbill.getText().toString();
                borewellsno=etborewellsno.getText().toString();
                sourceespinn=sourcespinner.getSelectedItem().toString();
                djbspinn=djbspinner.getSelectedItem().toString();
                billspinn=billspinner.getSelectedItem().toString();
                chargespinn=chargespinner.getSelectedItem().toString();

                pref = getSharedPreferences("personuniqueid",MODE_PRIVATE);
                String unique = pref.getString("uniqueid",null);
                HashMap<String,String> data = new HashMap<>();
                // String conn,mno,mmake,mreading,conncount,raisedbill,paidbill,borewellsno,sourceespinn,djbspinn,billspinn,chargespinn;

                data.put(WATERSRC_KEY,sourceespinn);
                data.put(DJBSTATUS_KEY,djbspinn);
                data.put(KNO_KEY,conn);
                data.put(BILLDEL_KEY,billspinn);
                data.put(METERNO_KEY,mno);
                data.put(METERMAKE_KEY,mmake);
                data.put(METERREAD_KEY,mreading);
                data.put(UNAUTHCON_KEY,conncount);
                data.put(BILLRAISE_KEY,raisedbill);
                data.put(BILLPAID_KEY,paidbill);
                data.put(BOREWELLS_KEY,borewellsno);
                data.put(SWERAGEPAID_KEY,chargespinn);
                data.put(UPLOAD_KEY,unique);
                String result = rh.sendPostRequest(UPLOAD_URL,data);
                return result;
            }
        }
        UploadWaterConn ui = new UploadWaterConn();
        String name = "hi";
        ui.execute(name);
    }
}
