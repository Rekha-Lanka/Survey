package com.delhijal.survey.NewSurvey;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.delhijal.survey.R;

public class AboutCompanyActivity  extends AppCompatActivity implements View.OnClickListener {
    ImageView amail,acall,aweb;
    LinearLayout panel1,panel2,panel3;
    TextView text1,text2,text3;
    View openLayout;
   // GoogleMap gmap;
    //String YOUR_API_KEY="AIzaSyDxBBCTnbdc_-7x2gYolw2UD9-k0difgQ8";
    //protected static final String STATIC_MAP_API_ENDPOINT = "http://maps.google.com/maps/api/staticmap?/center=17.49,78.39&maptype=roadmap&zoom=15&size=600x400";
    //http://maps.google.com/maps/api/staticmap?center=53,51&maptype=satellite&zoom=17&size=640x300&sensor=false
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_company);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_aboutus);
//        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
       // toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        amail=(ImageView)findViewById(R.id.aemail);
        acall=(ImageView)findViewById(R.id.acall);
        aweb=(ImageView)findViewById(R.id.awebsite);

        panel2 = (LinearLayout) findViewById(R.id.panel2);
        panel3 = (LinearLayout) findViewById(R.id.panel3);

        text2 = (TextView) findViewById(R.id.text2);
        text3 = (TextView) findViewById(R.id.text3);
      //  text1.setOnClickListener(this);
        text2.setOnClickListener(this);
        text3.setOnClickListener(this);
        amail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(Intent.ACTION_SENDTO);
                    i.setType("message/rfc822");
                    i.setData(Uri.parse("mailto:sateesh.itech@gmail.com"));
                    i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
                    i.putExtra(Intent.EXTRA_TEXT   , "body of email");
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Log.d("exception", "There are no email clients installed.");
                }
            }
        });
        acall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPermissionGranted()){
                    call_action();
                }
            }
        });
        aweb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",Uri.parse("http://www.fratelloinnotech.com/"));
                startActivity(viewIntent);
            }
        });

//        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapaboutusid);
//        mapFragment.getMapAsync(this);

    }
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        gmap = googleMap;
//        // Enabling MyLocation Layer of Google Map
//        googleMap.setMyLocationEnabled(true);
//        addMarkertoMap();
//    }

//    private void addMarkertoMap() {
//        CameraPosition cameraPosition = new CameraPosition.Builder()
//                .target(new LatLng(17.4948,78.3975))
//                .zoom(17)
//                .bearing(0)
//                .tilt(45)
//                .build();
//
//        gmap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//        gmap.addMarker(new MarkerOptions()
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
//                .position(new LatLng(17.4948,78.3975))
//                .title("My Location")
//        );



  //  }


    @Override
    public void onClick(View v)
    {
        hideOthers(v);
    }
    private void hideThemAll() {
        if (openLayout == null) return;
        if (openLayout == panel1)
            panel1.startAnimation(new ScaleAnimToHide(1.0f, 1.0f, 1.0f, 0.0f, 500, panel1, true));
        if (openLayout == panel2)
            panel2.startAnimation(new ScaleAnimToHide(1.0f, 1.0f, 1.0f, 0.0f, 500, panel2, true));
        if (openLayout == panel3)
            panel3.startAnimation(new ScaleAnimToHide(1.0f, 1.0f, 1.0f, 0.0f, 500, panel3, true));
    }
    private void hideOthers(View layoutView) {
        {
            int v;
           if (layoutView.getId() == R.id.text2) {
                v = panel2.getVisibility();
                hideThemAll();
                if (v != View.VISIBLE) {
                    panel2.startAnimation(new ScaleAnimToShow(1.0f, 1.0f, 1.0f, 0.0f, 500, panel2, true));
                }
            } else if (layoutView.getId() == R.id.text3) {
                v = panel3.getVisibility();
                hideThemAll();
                if (v != View.VISIBLE) {
                    panel3.startAnimation(new ScaleAnimToShow(1.0f, 1.0f, 1.0f, 0.0f, 500, panel3, true));
                }
            }
        }
    }

    public class ScaleAnimToHide extends ScaleAnimation
    {

        private View mView;

        private LinearLayout.LayoutParams mLayoutParams;

        private int mMarginBottomFromY, mMarginBottomToY;

        private boolean mVanishAfter = false;

        public ScaleAnimToHide(float fromX, float toX, float fromY, float toY, int duration, View view,boolean vanishAfter)
        {
            super(fromX, toX, fromY, toY);
            setDuration(duration);
            openLayout = null;
            mView = view;
            mVanishAfter = vanishAfter;
            mLayoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
            int height = mView.getHeight();
            mMarginBottomFromY = (int) (height * fromY) + mLayoutParams.bottomMargin - height;
            mMarginBottomToY = (int) (0 - ((height * toY) + mLayoutParams.bottomMargin)) - height;

            Log.v("CZ","height..." + height + " , mMarginBottomFromY...." + mMarginBottomFromY  + " , mMarginBottomToY.." +mMarginBottomToY);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t)
        {
            super.applyTransformation(interpolatedTime, t);
            if (interpolatedTime < 1.0f)
            {
                int newMarginBottom = mMarginBottomFromY + (int) ((mMarginBottomToY - mMarginBottomFromY) * interpolatedTime);
                mLayoutParams.setMargins(mLayoutParams.leftMargin, mLayoutParams.topMargin,mLayoutParams.rightMargin, newMarginBottom);
                mView.getParent().requestLayout();
                //Log.v("CZ","newMarginBottom..." + newMarginBottom + " , mLayoutParams.topMargin..." + mLayoutParams.topMargin);
            }
            else if (mVanishAfter)
            {
                mView.setVisibility(View.GONE);
            }
        }
    }
    public class ScaleAnimToShow extends ScaleAnimation {

        private View mView;

        private LinearLayout.LayoutParams mLayoutParams;

        private int mMarginBottomFromY, mMarginBottomToY;

        private boolean mVanishAfter = false;

        public ScaleAnimToShow(float toX, float fromX, float toY, float fromY, int duration, View view, boolean vanishAfter) {
            super(fromX, toX, fromY, toY);
            openLayout = view;
            setDuration(duration);
            mView = view;
            mVanishAfter = vanishAfter;
            mLayoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
            mView.setVisibility(View.VISIBLE);
            int height = mView.getHeight();
            //mMarginBottomFromY = (int) (height * fromY) + mLayoutParams.bottomMargin + height;
            //mMarginBottomToY = (int) (0 - ((height * toY) + mLayoutParams.bottomMargin)) + height;

            mMarginBottomFromY = 0;
            mMarginBottomToY = height;

            Log.v("CZ", ".................height..." + height + " , mMarginBottomFromY...." + mMarginBottomFromY + " , mMarginBottomToY.." + mMarginBottomToY);
        }
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t)
        {
            super.applyTransformation(interpolatedTime, t);
            if (interpolatedTime < 1.0f)
            {
                int newMarginBottom = (int) ((mMarginBottomToY - mMarginBottomFromY) * interpolatedTime) - mMarginBottomToY;
                mLayoutParams.setMargins(mLayoutParams.leftMargin, mLayoutParams.topMargin,mLayoutParams.rightMargin, newMarginBottom);
                mView.getParent().requestLayout();
                //Log.v("CZ","newMarginBottom..." + newMarginBottom + " , mLayoutParams.topMargin..." + mLayoutParams.topMargin);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public  boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted");
                return true;
            } else {

                Log.v("TAG","Permission is revoked");
                ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted");
            return true;
        }
    }
    public void call_action(){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:9010990285"));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Intent chooser  = Intent.createChooser(intent, "Complete Action using..");
        startActivity(chooser);
        //context.startActivity(intent);
    }
}
