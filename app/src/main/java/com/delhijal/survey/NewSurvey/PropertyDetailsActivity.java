package com.delhijal.survey.NewSurvey;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.delhijal.survey.MainSurveyActivity;
import com.delhijal.survey.R;

import java.util.List;
import java.util.Locale;

public class PropertyDetailsActivity extends AppCompatActivity implements LocationListener {
   Button pnext,pprevious;
   EditText etdno,etfloorno,etpropertyname,etstreet,etarea,etlandmark,etpin;
   String dno,floorno,propertyname,street,area,landmark,pin;
   TextView locationtv;
    LocationManager locationManager;
    Handler h = new Handler();
    int delay =500; //1 second=1000 milisecond, 15*1000=15seconds
    Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_details);
        etdno=(EditText)findViewById(R.id.edithno);
        etfloorno=(EditText)findViewById(R.id.editFloorno);
        etpropertyname=(EditText)findViewById(R.id.editpname);
        etstreet=(EditText)findViewById(R.id.editstreetname);
        etarea=(EditText)findViewById(R.id.editarea);
        etlandmark=(EditText)findViewById(R.id.editlandmark);
        etpin=(EditText)findViewById(R.id.editpin);
        locationtv=(TextView)findViewById(R.id.locationtv);
        pnext=(Button)findViewById(R.id.pnext);
        pprevious=(Button)findViewById(R.id.pprevious);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }
        getLocation();
        pnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dno=etdno.getText().toString();
                floorno=etfloorno.getText().toString();
                propertyname=etpropertyname.getText().toString();
                street=etstreet.getText().toString();
                area=etarea.getText().toString();
                landmark=etlandmark.getText().toString();
                pin=etpin.getText().toString();
                if(dno.equals("")||dno.length()<2){
                    etdno.setError("Enter min 2 chars");
                    etdno.setFocusable(true);
                }else if(floorno.equals("")){
                    etfloorno.setError("Enter the floor no");
                    etfloorno.setFocusable(true);
                }else if(propertyname.equals("")||propertyname.length()<3) {
                    etpropertyname.setError("Enter min 3 chars");
                    etpropertyname.setFocusable(true);
                }else if(street.equals("")||street.length()<3) {
                    etstreet.setError("Enter min 3 chars");
                    etstreet.setFocusable(true);
                }else if(area.equals("")||area.length()<3) {
                    etarea.setError("Enter min 3 chars");
                    etarea.setFocusable(true);
                }else if(landmark.equals("")||landmark.length()<3) {
                    etlandmark.setError("Enter min 3 chars");
                    etlandmark.setFocusable(true);
                }else if(pin.equals("")) {
                    etpin.setError("Enter pin number");
                    etpin.setFocusable(true);
                }else {
                    Intent i = new Intent(PropertyDetailsActivity.this, ConnectionDetailsActivity.class);
                    startActivity(i);
                }
            }
        });

        pprevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(PropertyDetailsActivity.this,OwnerDetailsActivity.class);
                startActivity(i);
            }
        });
    }
    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
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


    @Override
    protected void onResume() {
        //start handler as activity become visible
        h.postDelayed(new Runnable() {
            public void run() {
                //do something
                getLocation();
                runnable=this;
                h.postDelayed(runnable, delay);
            }
        }, delay);

        super.onResume();
    }
    @Override
    protected void onPause() {
        h.removeCallbacks(runnable); //stop handler when activity not visible
        super.onPause();
    }


    @Override
    public void onLocationChanged(Location location) {
        locationtv.setText(location.getLatitude()+","+location.getLongitude());
//        locationtv.setText("Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude());
//
//        try {
//            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//            locationtv.setText(locationtv.getText() + "\n"+addresses.get(0).getAddressLine(0)+", "+
//                    addresses.get(0).getAddressLine(1)+", "+addresses.get(0).getAddressLine(2));
//        }catch(Exception e)
//        {
//
//        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(PropertyDetailsActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }
}
