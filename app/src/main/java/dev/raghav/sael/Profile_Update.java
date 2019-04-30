package dev.raghav.sael;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import dev.raghav.sael.Connectivity.Connectivity;
import dev.raghav.sael.Connectivity.Utilities;
import dev.raghav.sael.Connectivity.Utility;

public class Profile_Update extends AppCompatActivity {
    ImageView profile_image, edit_pf_image;
    EditText et_name,et_email,et_mobile,et_pass;
    String Et_Name,Et_Email,Et_Mobile,Et_Pass;
    Button button_Update;

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    String userChoosenTask;
    String output;
    File destination;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__update);
        getSupportActionBar().hide();

        button_Update=findViewById(R.id.button_update);
        profile_image=findViewById(R.id.profile_image);
        edit_pf_image=findViewById(R.id.change_profile_image);
        et_email=findViewById(R.id.et_email);
        et_name=findViewById(R.id.et_fullname);
        et_mobile=findViewById(R.id.et_mobile);
        et_pass=findViewById(R.id.et_pw);

        button_Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(Profile_Update.this,Main2Activity.class);
//                startActivity(intent);

                Et_Name=et_name.getText().toString();
                Et_Pass=et_pass.getText().toString();
                Et_Email=et_email.getText().toString();
                Et_Mobile=et_mobile.getText().toString();
                if (Connectivity.isNetworkAvailable(Profile_Update.this)){

                    if (!Et_Name.isEmpty() && !Et_Mobile.isEmpty() && !Et_Pass.isEmpty() && !Et_Email.isEmpty()){
                        new Profile_Update_Excute().execute();
                    }else {
                        Toast.makeText(Profile_Update.this, "All Field Are Required", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(Profile_Update.this, "Please Check Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //*********************************************
        edit_pf_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();

            }
        });


    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Profile_Update.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(Profile_Update.this);

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

    private void galleryIntent() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start new activity with the LOAD_IMAGE_RESULTS to handle back the results when image is picked from the Image Gallery.
        startActivityForResult(i, SELECT_FILE);

    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                try {
                    onSelectFromGalleryResult(data);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            if(destination !=null)
            {
                // Toast.makeText(this, "path is"+destination.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "something wrong", Toast.LENGTH_SHORT).show();
            }
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        profile_image.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) throws URISyntaxException {

        Bitmap bm=null;
        if (data != null) {

            Uri pickedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            destination = new File(cursor.getString(cursor.getColumnIndex(filePath[0])));
            cursor.close();

            //Toast.makeText(this, ""+destination, Toast.LENGTH_SHORT).show();
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        profile_image.setImageBitmap(bm);
    }
//*************************************************************************

    private class Profile_Update_Excute extends AsyncTask<Void, Long, String> {


        String result = "";

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(Profile_Update.this);
            dialog.setMessage("Processing");
            // dialog.setMax(100);
            dialog.show();
            dialog.setCancelable(false);
            // dialog.setProgress(0);
//            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                org.apache.http.entity.mime.MultipartEntity entity = new MultipartEntity(
                        HttpMultipartMode.BROWSER_COMPATIBLE);
               // String id= AppPreference.getUserid(MainLocation.this);
               if(destination !=null)
                {
                    entity.addPart("file", new FileBody(destination));
                }
                entity.addPart("name", new StringBody(Et_Name));
                entity.addPart("mobile",new StringBody(Et_Mobile));
                entity.addPart("password",new StringBody(Et_Pass));
                entity.addPart("email",new StringBody(Et_Email));

                result = Utilities.postEntityAndFindJson("http://ihisaab.in/vets/Api/*****", entity);

                return result;

            } catch (Exception e) {
                // something went wrong. connection with the server error
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // Toast.makeText(MainLocation.this, "res is "+result, Toast.LENGTH_LONG).show();

            //String result1 = result;
            if (result != null) {


                Log.e("result_Image", result);
                try {
                    JSONObject object = new JSONObject(result);
                    String responce = object.getString("responce");
                    // String img = object.getString("img");
                    if (responce.equals("true")) {

                        Toast.makeText(Profile_Update.this, "Success", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(Profile_Update.this,Main2Activity.class);
                        startActivity(intent);

                        finish();


                    } else {
                        Toast.makeText(Profile_Update.this, "Some Problem", Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    dialog.dismiss();
                    Toast.makeText(Profile_Update.this, e.toString(), Toast.LENGTH_SHORT).show();
                }

            } else {
                dialog.dismiss();
                //  Toast.makeText(Registration.this, "No Response From Server", Toast.LENGTH_LONG).show();
            }

        }
    }
}
