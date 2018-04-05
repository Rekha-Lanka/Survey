package com.delhijal.survey.NewSurvey;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.delhijal.survey.MainSurveyActivity;
import com.delhijal.survey.R;
import com.delhijal.survey.RequestHandler;
import com.delhijal.survey.Utils.CameraUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

public class SubmitDetailsActivity extends AppCompatActivity {
 Button sprevious,submit;
    private String userChoosenTask;
    ImageView nameplatepreview,meterpreview;
    Button  nameplatebtn,meterbtn;
    Spinner sewerspinner,djbspinner;
    EditText etkitchens,etwashrooms;
    String sewerspinn,djbspinn,kitchens,washrooms,nameplateimg,meterimg;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    RequestHandler rh = new RequestHandler();
    SharedPreferences shre;
    int flag=0;
    LinearLayout imagell;
    Bitmap bm,bm2,bm1;
    public static final String UPLOAD_URL = "http://www.globalmrbs.com/survey/insertfinal.php";

    public static final String SAWER_KEY = "sawer";
    public static final String KITCHEN_KEY = "kitch";
    public static final String WASHROOM_KEY = "wash";
    public static final String DJBSAWER_KEY = "djbsawer";
    public static final String NAMEPLATEIMG_KEY = "nameplateimg";
    public static final String METERIMG_KEY = "meterimg";
    public static final String UPLOAD_KEY = "id";
    SharedPreferences sharedPreferences;
    TextView statustext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_details);
        sprevious=(Button)findViewById(R.id.sprevious);
        submit=(Button)findViewById(R.id.submit);
        etkitchens=(EditText)findViewById(R.id.editkitchens);
        etwashrooms=(EditText)findViewById(R.id.editwashrooms);
        sewerspinner=(Spinner)findViewById(R.id.sewarspinner);
        djbspinner=(Spinner)findViewById(R.id.djbconnspinner);
        nameplatepreview=(ImageView)findViewById(R.id.nameplatepreview);
        nameplatebtn=(Button)findViewById(R.id.nameplatebtn);
        imagell=(LinearLayout)findViewById(R.id.imagegalleryll);
        meterbtn=(Button)findViewById(R.id.meterbtn);
        meterpreview=(ImageView)findViewById(R.id.meterpreview);
        statustext=findViewById(R.id.statustext);

        sharedPreferences = getSharedPreferences("persondetails",MODE_PRIVATE);
        final String status = sharedPreferences.getString("personstatus",null);
        if(status!=null) {
            if (status.equalsIgnoreCase("completed")) {
                submit.setEnabled(false);
                submit.setBackgroundColor(Color.GRAY);

                statustext.setText("Already Details Submitted");

            }
        }
        sprevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SubmitDetailsActivity.this,ConnectionDetailsActivity.class);
                startActivity(i);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i=new Intent(SubmitDetailsActivity.this,OwnerDetailsActivity.class);
//                startActivity(i);
               // imagell.setVisibility(View.GONE);

                sewerspinn = sewerspinner.getSelectedItem().toString();
                djbspinn = djbspinner.getSelectedItem().toString();
                kitchens=etkitchens.getText().toString();
                washrooms=etwashrooms.getText().toString();
                if (kitchens.equals("")) {
                    etkitchens.setError("Enter kitchen details");
                    etkitchens.setFocusable(true);
                } else if (washrooms.equals("")){
                    etwashrooms.setError("Enter no of washrooms");
                    etwashrooms.setFocusable(true);
                } else if((nameplatepreview.getDrawable() == null)||(meterpreview.getDrawable() == null)){
                   Toast.makeText(getApplicationContext(),"images shouldn't be empty",Toast.LENGTH_LONG).show();
                }else if (sewerspinner.getSelectedItem().toString().trim().equalsIgnoreCase("Choose- Tap Here")) {
                    TextView errorText = (TextView) sewerspinner.getSelectedView();
                    errorText.setError("select catogery");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    //errorText.setText("select catogery");//changes the selected item text to this
                }else if (djbspinner.getSelectedItem().toString().trim().equalsIgnoreCase("Choose- Tap Here")) {
                    TextView errorText = (TextView) djbspinner.getSelectedView();
                    errorText.setError("select catogery");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    //errorText.setText("select catogery");//changes the selected item text to this
                }
                else {
                    uploadfinaldetails();

                }

            }
        });
        nameplatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagell.setVisibility(View.VISIBLE);
                flag=0;
                selectImage();
            }
        });
        meterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagell.setVisibility(View.VISIBLE);
                flag=1;
                selectImage();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case CameraUtility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }

        return;
    }

    // other 'case' lines to check for other permissions this application might request.
    // You can add here other case statements according to your requirement.



    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }


    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SubmitDetailsActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= CameraUtility.checkPermission(SubmitDetailsActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    if(result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);


    }
    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE){
                //onSelectFromGalleryResult(data);
                 bm=null;
                if (data != null) {
                    try {
                        bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //storeImage(bm);
                    if(flag==0) {
                        bm1 = bm;
                        nameplatepreview.setImageBitmap(bm);
                    }
                    else{
                        meterpreview.setImageBitmap(bm);
                        bm2 = bm;
                    }

                }
            }

            else if (requestCode == REQUEST_CAMERA) {
                //onCaptureImageResult(data);
               bm = (Bitmap) data.getExtras().get("data");
                //storeImage(bm);
                if(flag==0){
                    nameplatepreview.setImageBitmap(bm);
                    bm1= bm;
                }
                else{
                    meterpreview.setImageBitmap(bm);
                    bm2 =bm;
                }


            }
        }
    }
    public static String getStringImage(Bitmap image)
    {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.d("Image Log:", imageEncoded);
        return imageEncoded;

    }
//    public static Bitmap decodeBase64(String input)
//    {
//        byte[] decodedByte = Base64.decode(input, 0);
//        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
//    }

    private void uploadfinaldetails(){
        class UploadDetails extends AsyncTask<Bitmap,Void,String> {

            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(SubmitDetailsActivity.this, "Uploading...", null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),"Successfully inserted",Toast.LENGTH_LONG).show();
                Intent i=new Intent(SubmitDetailsActivity.this,PersonDetailsActivity.class);
                startActivity(i);

            }
            @Override
            protected String doInBackground(Bitmap... params) {
                //Toast.makeText(getApplicationContext(),"hello do in",Toast.LENGTH_LONG).show();
                //String sewerspinn,djbspinn,kitchens,washrooms,nameplateimg,meterimg;
                sewerspinn = sewerspinner.getSelectedItem().toString();
                djbspinn = djbspinner.getSelectedItem().toString();
                kitchens=etkitchens.getText().toString();
                washrooms=etwashrooms.getText().toString();
                Bitmap bitmap1 = params[0];
                Bitmap bitmap2=params[1];

                nameplateimg= getStringImage(bitmap1);
                meterimg=getStringImage(bitmap2);
                shre = getSharedPreferences("personuniqueid",MODE_PRIVATE);
                String unique = shre.getString("uniqueid",null);
                HashMap<String,String> data = new HashMap<>();
                data.put(KITCHEN_KEY,kitchens);
                data.put(WASHROOM_KEY,washrooms);
                data.put(SAWER_KEY,sewerspinn);
                data.put(DJBSAWER_KEY,djbspinn);
                data.put(NAMEPLATEIMG_KEY,nameplateimg);
                data.put(METERIMG_KEY,meterimg);
                data.put(UPLOAD_KEY,unique);
                String result = rh.sendPostRequest(UPLOAD_URL,data);
                return result;

            }

        }

        UploadDetails ui = new UploadDetails();
        ui.execute(bm1,bm2);

    }

}


