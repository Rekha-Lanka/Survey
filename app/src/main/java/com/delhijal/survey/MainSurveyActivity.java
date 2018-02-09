package com.delhijal.survey;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import com.delhijal.survey.NewSurvey.PersonDetailsActivity;
import com.delhijal.survey.UpdateSurvey.MainUpdateActivity;

public class MainSurveyActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    LinearLayout newll,updatell,cmpltll;
    SharedPreferences pref;
    TextView uname,mobileno;

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
                Intent i=new Intent(MainSurveyActivity.this, MainUpdateActivity.class);
                startActivity(i);
            }
        });
        cmpltll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


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
}
