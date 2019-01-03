package com.example.mortrza.mycamera04;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

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
                mBottomSheetDialog.dismiss();
                Intent intent  = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,2);

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
}
