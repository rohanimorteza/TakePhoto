package com.example.mortrza.mycamera04;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import static android.os.Build.VERSION.SDK_INT;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_PERMISSION = 100;
    ImageView image;
    LinearLayout lin;
    BottomSheetDialog mBottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        lin = findViewById(R.id.lin_select_photo);
        image = findViewById(R.id.img);

        lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openBottomSheet(v);

            }
        });

    }


    public void openBottomSheet(View v) {


        View view = getLayoutInflater().inflate(R.layout.bottomsheet,null);
        CardView gallery = (CardView) view.findViewById(R.id.crd_bs_gallery);
        CardView camera = (CardView) view.findViewById(R.id.crd_bs_camera);


        mBottomSheetDialog = new BottomSheetDialog(MainActivity.this);
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
                Intent intent  = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,1);

            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int currentVersion = SDK_INT;
                if(currentVersion>= Build.VERSION_CODES.M){
                    if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
                        if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.CAMERA)){

                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setCancelable(true);
                            builder.setTitle("دسترسی ضروری به دوربین !!!");
                            builder.setMessage("برای استفاده از دوربین، و گرفتن عکس از دانشجو برنامه به این دسترسی نیاز دارد.");
                            builder.setPositiveButton("موافقم", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(MainActivity.this,new String[] {Manifest.permission.CAMERA},REQUEST_PERMISSION);
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }else {
                            ActivityCompat.requestPermissions(MainActivity.this,new String[] {Manifest.permission.CAMERA},REQUEST_PERMISSION);
                        }
                    }else {
                        //Toast.makeText(getApplicationContext(),"شما قبلا این دسترسی را تائید کرده اید.",Toast.LENGTH_LONG).show();
                        mBottomSheetDialog.dismiss();
                        Intent intent  = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent,2);
                    }
                }



            }
        });


    }

        @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK && data!=null ){

                Uri img = data.getData();
                try{
                    image.setImageURI(img);
                }catch (Exception e){
                    e.printStackTrace();
                }
        }

        if(requestCode==2 && resultCode==RESULT_OK && data!=null ){

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            image.setImageBitmap(photo);

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case REQUEST_PERMISSION :
                if(grantResults.length>=0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    //Toast.makeText(getApplicationContext(),"شما دسترسی را تائید کردید. متشکریم",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(),"شما این دسترسی را تائید نکردید",Toast.LENGTH_LONG).show();
                    finish();
                }
        }

    }


}
