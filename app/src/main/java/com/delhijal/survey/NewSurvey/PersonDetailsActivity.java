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
import android.net.Uri;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PersonDetailsActivity extends AppCompatActivity {
    private String userChoosenTask;
    ImageView propertypic;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    SharedPreferences shre;
    Button next,previous,browse,save;
    LinearLayout imagegalleryll;
    private Uri filePath;
    EditText ename,efname,emobile,eemail;
    String puname,pfname,pmobile,pemail,pcatogery,upload;
    TextView resulttv;
    boolean is_mob_number = false;
    Spinner spinnerDetails;
    public static final String  key = "nameKey";
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public static final String PROVIDEBY_KEY = "provideby";
    public static final String NAME_KEY = "name";
    public static final String FATHERNAME_KEY = "father";
    public static final String MOBILE_KEY = "mobile";
    public static final String EMAIL_KEY = "email";
    public static final String UPLOAD_KEY = "propertyimg";
    RequestHandler rh = new RequestHandler();

    public static final String MyPREFERENCES = "MyPre" ;//file name
    SharedPreferences personpref;
    public static final String UPLOAD_URL = "http://www.globalm.co.in/survey/insertsurvey.php";
    Bitmap bm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_details);
        ename=(EditText)findViewById(R.id.editname);
        efname=(EditText)findViewById(R.id.editfathername);
        emobile=(EditText)findViewById(R.id.editmobileno);
        eemail=(EditText)findViewById(R.id.editemail);
      next=(Button)findViewById(R.id.mainnext);
      save=(Button)findViewById(R.id.mainsave);
    previous=(Button)findViewById(R.id.mainprevious);
        spinnerDetails=(Spinner)findViewById(R.id.signupspinner);

    imagegalleryll=(LinearLayout)findViewById(R.id.picll);
         browse=(Button)findViewById(R.id.browse);
        propertypic=(ImageView)findViewById(R.id.propertypreview);

        pcatogery = spinnerDetails.getSelectedItem().toString();
        puname=ename.getText().toString();
        pfname=efname.getText().toString();
        pmobile=emobile.getText().toString();
        pemail=eemail.getText().toString();


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(puname.equals("")||puname.length()<3){
                    ename.setError("Enter min 3 chars username");
                    ename.setFocusable(true);
                }else if(pfname.equals("")||pfname.length()<3){
                    efname.setError("Enter min 3 chars username");
                    efname.setFocusable(true);
                }else if(pmobile.equals("")|| !ismobileno()) {
                    emobile.setError("Enter valid Mobile no");
                    emobile.setFocusable(true);
                }else if(pemail.equals("")|| !pemail.matches(emailPattern)){
                    eemail.setError("Enter valid email");
                    eemail.setFocusable(true);
                } else if(spinnerDetails.getSelectedItem().toString().trim().equalsIgnoreCase("Data provided by- Tap Here")){
                    TextView errorText = (TextView)spinnerDetails.getSelectedView();
                    errorText.setError("select catogery");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    //errorText.setText("select catogery");//changes the selected item text to this
                }else {
                    personpref = getSharedPreferences("persondetails", MODE_PRIVATE);
                    SharedPreferences.Editor editor = personpref.edit();
//                    String pusername = ename.getText().toString();
//                    String pfathername=
//                    String pno = emobile.getText().toString();
                    editor.putString("personname", puname);
                    editor.putString("pfathername",pfname);
                    editor.putString("mobilenumber",pmobile);
                    editor.putString("personemail",pemail);
                    editor.commit();
                    Intent i=new Intent(PersonDetailsActivity.this,OwnerDetailsActivity.class);
                    startActivity(i);
                }
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(PersonDetailsActivity.this,MainSurveyActivity.class);
                startActivity(i);
            }
        });
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagegalleryll.setVisibility(View.VISIBLE);
              selectImage();
            }
        });

    }
    public boolean ismobileno() {

// mobile number should be 10 digit
        Pattern pattern = Pattern.compile("\\d{10}");
        Matcher matchr = pattern.matcher(pmobile.trim());
        if (matchr.matches()) {
            is_mob_number = true;
        }
        return is_mob_number;
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

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(PersonDetailsActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= CameraUtility.checkPermission(PersonDetailsActivity.this);

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
                        filePath = data.getData();
                        bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), filePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //storeImage(bm);
                    propertypic.setImageBitmap(bm);

                }
            }

            else if (requestCode == REQUEST_CAMERA) {
                //onCaptureImageResult(data);
                Bitmap bm = (Bitmap) data.getExtras().get("data");
                //storeImage(bm);
                propertypic.setImageBitmap(bm);

            }
        }
    }
//    private void storeImage(Bitmap thumbnail) {
//        // Removing image saved earlier in shared prefernces
//       // PreferenceManager.getDefaultSharedPreferences(this).edit().clear().apply();
//        createFolder();
//        // this code is use to generate random number and add to file
//        // name so that each file should be different
//        Random generator = new Random();
//        int n = 10000;
//        n = generator.nextInt(n);
//        String fname = "Image-"+ n +".jpg";
//
//        // set the file path
//        // sdcard/PictureFolder/ is the folder created in create folder method
//        String filePath = "/sdcard/PictureFolder/"+fname;
//        // the rest of the code is for saving the file to filepath mentioned above
//        FileOutputStream fileOutputStream = null;
//        try {
//            fileOutputStream = new FileOutputStream(filePath);
//        } catch (FileNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
//
//        //choose another format if PNG doesn't suit you
//        thumbnail.compress(Bitmap.CompressFormat.PNG, 100, bos);
//        shre =  getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = shre.edit();
//        editor.putString(key,encodeTobase64(thumbnail));
//        editor.commit();
//        try {
//            bos.flush();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        try {
//            bos.close();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//    }
//    public void createFolder()
//    {
//        // here PictureFolder is the folder name you can change it offcourse
//        String RootDir = Environment.getExternalStorageDirectory()
//                + File.separator + "PictureFolder";
//        File RootFile = new File(RootDir);
//        RootFile.mkdir();
//    }

    public String getStringImage(Bitmap bmp)
    {
       // Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encodedImage  = Base64.encodeToString(b, Base64.DEFAULT);

        //Log.d("Image Log:", encodedImage );
        return encodedImage ;

    }
    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
    @Override
    public boolean onSupportNavigateUp() {
//        onBackPressed();
        Intent i=new Intent(PersonDetailsActivity.this,MainSurveyActivity.class);
        startActivity(i);
       return true;
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

    private void upload(){
        class Upload extends AsyncTask<Bitmap,Void,String>  {

            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(PersonDetailsActivity.this, "Uploading...", null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                try {
                       JSONObject jsonObject = new JSONObject(s);
                        String id = jsonObject.getString("result");
                        resulttv=(TextView)findViewById(R.id.result);
                        resulttv.append("Unique id is: "+id);
                        loading.dismiss();

            } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            @Override
            protected String doInBackground(Bitmap... params) {
                pcatogery = spinnerDetails.getSelectedItem().toString();
                puname=ename.getText().toString();
                pfname=efname.getText().toString();
                pmobile=emobile.getText().toString();
                pemail=eemail.getText().toString();
                //Bitmap bitmap = params[5];
               // bm = params[5];
//                shre =  getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//                if (shre.contains(key))
//                {//save required image
//                    String u=shre.getString(key, "");
//                    bm=decodeBase64(u);
//                    //profilepic.setImageBitmap(thumbnail);
//                }
               // upload= getStringImage(bm);
                HashMap<String,String> data = new HashMap<>();
                data.put(PROVIDEBY_KEY,pcatogery);
                data.put(NAME_KEY,puname);
                data.put(FATHERNAME_KEY,pfname);
                data.put(MOBILE_KEY,pmobile);
                data.put(EMAIL_KEY,pemail);
                //data.put(UPLOAD_KEY,upload);
                String result = rh.sendPostRequest(UPLOAD_URL,data);
                return result;

            }


        }

        Upload ui = new Upload();
        ui.execute(bm);
    }

}
