package com.delhijal.survey;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.constraint.solver.Cache;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.delhijal.survey.Network.ConnectivityReceiver;
import com.delhijal.survey.Network.MyApplication;
import com.delhijal.survey.NewSurvey.PersonDetailsActivity;
import com.delhijal.survey.UpdateSurvey.MainUpdateActivity;
import com.delhijal.survey.Utils.NetworkChangeReceiver;
import com.delhijal.survey.Utils.NetworkUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainSurveyActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,ConnectivityReceiver.ConnectivityReceiverListener {
    LinearLayout newll,updatell,cmpltll;
    SharedPreferences pref;
    TextView uname,mobileno,today,total;
    private ArrayList<String> counts = new ArrayList<String>();
    RequestHandler rh = new RequestHandler();
    public static final String UPLOAD_URL = "http://www.globalm.co.in/survey/insertsurvey.php";
    public static final String TOTAL_KEY = "total";
    public static final String CURRENT_KEY = "current";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_survey);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header  = navigationView.getHeaderView(0);
        uname = (TextView) header.findViewById(R.id.uname);
        mobileno = (TextView) header.findViewById(R.id.mno);
        newll=(LinearLayout)findViewById(R.id.newsurveyll);
        updatell=(LinearLayout)findViewById(R.id.updatesurveyll);
        cmpltll=(LinearLayout)findViewById(R.id.cmpltsurveyll);
        today=(TextView)findViewById(R.id.todaytv);
        total=(TextView)findViewById(R.id.totaltv);

        pref = getSharedPreferences("userdetails",MODE_PRIVATE);
        String loginuname = pref.getString("username",null);
        String loginmobileno = pref.getString("mobileno",null);
        pref = getSharedPreferences("persondetails",MODE_PRIVATE);
        getApplicationContext().getSharedPreferences("persondetails", 0).edit().clear().commit();
        if(loginuname==""||loginuname==null){
            Intent i=new Intent(this,SigninActivity.class);
            startActivity(i);
        }
        uname.setText(loginuname);
        mobileno.setText(loginmobileno);
        try {
            newll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainSurveyActivity.this, PersonDetailsActivity.class);
                    startActivity(i);
                }
            });

            updatell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(MainSurveyActivity.this, MainUpdateActivity.class);
                    startActivity(i);

                }
            });

            new GetSurvey().execute();

        }catch (Exception e){
            Log.e("ERROR","EXCEPTION");
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
           // super.onBackPressed();
            //@Override
            //public void onBackPressed() {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

           // }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_aboutus) {
            Intent i=new Intent(MainSurveyActivity.this,AboutUs.class);
            startActivity(i);
            // Handle the camera action
        } else if (id == R.id.nav_dashboard) {

        } else if (id == R.id.nav_logout) {
//            Intent intent = new Intent(getApplicationContext(), MainMapActivity.class);
//            startActivity(intent);
            Intent i=new Intent(MainSurveyActivity.this,SigninActivity.class);
            getApplicationContext().getSharedPreferences("userdetails", 0).edit().clear().commit();
            startActivity(i);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private class GetSurvey extends AsyncTask<Void,Void,ArrayList<String>>{
        ProgressDialog loading;
        String response = null;

        @Override

        protected void onPreExecute () {
            super.onPreExecute();
            loading = ProgressDialog.show(MainSurveyActivity.this, "Uploading...", null,true,true);
            checkConnection();
            if(ConnectivityReceiver.isConnected()==false) {
                loading.dismiss();
                displayMobileDataSettingsDialog();
                //refresh();
            }
        }


        @Override

        protected ArrayList<String> doInBackground (Void...voids){
            RequestHandler sh = new RequestHandler();
            String[] res=null;

            // Making a request to url and getting response

        String jsonStr = sh.makeServiceCall("http://www.globalm.co.in/survey/getnosurveys.php");

        if (jsonStr != null) {
            try {

                JSONObject jsonObj = new JSONObject(jsonStr);

                JSONArray contacts = jsonObj.getJSONArray("result");

                for (int i = 0; i < contacts.length(); i++) {

                    JSONObject c = contacts.getJSONObject(i);
                    String totalsurvey = c.getString("total");
                    String currentsurvey = c.getString("current");
                    counts.add(totalsurvey);
                    counts.add(currentsurvey);
                }

            } catch (final JSONException e) {
                Log.e("Error","Exception");
            }
        } else {

           // Log.e("ERROR","EXCEPTION");
          // Toast.makeText(getApplicationContext(), "Couldn't get json from server", Toast.LENGTH_SHORT).show();
        }
        return counts;
    }

        @Override
        protected void onPostExecute(final ArrayList<String> ss){
            super.onPostExecute(ss);
            try {
                today.setText(ss.get(1));
                total.setText(ss.get(0));
                loading.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }


    }
    }
    public AlertDialog displayMobileDataSettingsDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Network Connection");
        //builder.setMessage(message);
        builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
//                startActivity(intent);
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();

            }
        });
        builder.show();
        return builder.create();
    }
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }
    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
        }

        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),message , Snackbar.LENGTH_LONG);
        // snackbar.show();
//        Snackbar snackbar = Snackbar
//                .make(findViewById(R.id.fab), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }
    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }
    public void refresh(){          //refresh is onClick name given to the button
        onRestart();
    }

    @Override
    protected void onRestart() {

        // TODO Auto-generated method stub
        super.onRestart();
        Intent i = new Intent(getApplicationContext(), MainSurveyActivity.class);  //your class
        startActivity(i);
        finish();

    }
}
