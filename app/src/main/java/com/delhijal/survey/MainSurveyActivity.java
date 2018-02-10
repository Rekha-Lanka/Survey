package com.delhijal.survey;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.delhijal.survey.NewSurvey.OwnerDetailsActivity;
import com.delhijal.survey.NewSurvey.PersonDetailsActivity;
import com.delhijal.survey.UpdateSurvey.MainUpdateActivity;
import com.delhijal.survey.Utils.NetworkStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainSurveyActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    LinearLayout newll,updatell,cmpltll;
    SharedPreferences pref;
    TextView uname,mobileno,today,total;
    private ArrayList<String> counts = new ArrayList<String>();
    NetworkStatus ns;
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
        newll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Intent i=new Intent(MainSurveyActivity.this, PersonDetailsActivity.class);
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
        new GetSurvey().execute();//        cmpltll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });


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

    private class GetSurvey extends AsyncTask<Void,Void,ArrayList<String>>{
        ProgressDialog loading;
        String response = null;

        @Override

        protected void onPreExecute () {
            super.onPreExecute();
            loading = ProgressDialog.show(MainSurveyActivity.this, "Uploading...", null,true,true);

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

            Toast.makeText(getApplicationContext(), "Couldn't get json from server", Toast.LENGTH_SHORT).show();
        }
        return counts;
    }

        @Override
        protected void onPostExecute(final ArrayList<String> ss){
            super.onPostExecute(ss);
            today.setText(ss.get(1));
            total.setText(ss.get(0));
            loading.dismiss();

    }

    }
}
