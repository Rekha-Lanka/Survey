package com.delhijal.survey;

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
import com.delhijal.survey.NewSurvey.PersonDetailsActivity;
import com.delhijal.survey.UpdateSurvey.MainUpdateActivity;
import com.delhijal.survey.Utils.NetworkStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainSurveyActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    LinearLayout newll,updatell,cmpltll;
    SharedPreferences pref;
    TextView uname,mobileno,today,total;
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
        getcompletesurvey();
//        cmpltll.setOnClickListener(new View.OnClickListener() {
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.home_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_home) {
//            Intent i=new Intent(this,MainSurveyActivity.class);
//            startActivity(i);
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

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

    private void getcompletesurvey() {
        //String urmail="loginuname";
        // donorprogress.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        //String uri = "http://" + address + "/myFolder/page.php?" + value1 + "=" + value2;
        //String uri = String.format("http://somesite.com/some_endpoint.php?param1=%1$s&param2=%2$s", num1, num2);
        String serverURL = String.format("http://www.globalm.co.in/survey/insertsurvey.php");
        final StringRequest getRequest = new StringRequest(Request.Method.GET, serverURL,
                new com.android.volley.Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            //JSONArray contacts = jsonObj.getJSONArray("result");
                            //HashMap<String,String> data = new HashMap<>();
                            String totalsurvey=jsonObj.getString(TOTAL_KEY);
                            String currentsurvey=jsonObj.getString(CURRENT_KEY);
                            today.setText(totalsurvey);
                            total.setText(totalsurvey);
                           // for (int i = 0; i < contacts.length(); i++) {
                                //columns:  fullname,email,password,mobile,gender,address,bloodgroup,age,city,status
//                                JSONObject c = contacts.getJSONObject(i);
//                                String fullname = c.getString("fullname");
//                                String email = c.getString("email");
//                                String password = c.getString("password");
//                                String mobile = c.getString("mobile");
//                                String gender = c.getString("gender");
//                                String address = c.getString("address");
//                                String bloodgroup = c.getString("bloodgroup");
//                                String age=c.getString("age");
//                                String city=c.getString("city");
//                                String status=c.getString("status");
                                //String status=c.getString("status");
//                                etViewName.setText(fullname);
//                                textViewEmail.setText(email);
//                                etPhoneno.setText(mobile);
//                                etAddress.setText(address);
//                                etAge.setText(age);
//                                etcity.setText(city);
//                                etbloodgroup.setText(bloodgroup);
//                                etgender.setText(gender);
//                                etstatus.setText(status);
//                        validation(name,pass);
//                                donordata.add(new DonorBean(id,fullname,email,password,mobile,gender,address,bloodgroup,age,city));
//                                donorListAdapter = new DonorListAdapter(DonarActivity.this,donordata);
//                                donorrecycle.setAdapter(donorListAdapter);
//                                donorrecycle.setLayoutManager(new LinearLayoutManager(DonarActivity.this));
//                                donorprogress.setVisibility(View.GONE);
                            } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
//                    } catch (final JSONException e) {
//                            Log.e(TAG, "Json parsing error: " + e.getMessage());
//                            e.printStackTrace();
//                        }

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(getApplicationContext(), "" + error, Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() {
                Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_SHORT).show();
                return null;
            }
        };
        queue.add(getRequest);
    }
}
