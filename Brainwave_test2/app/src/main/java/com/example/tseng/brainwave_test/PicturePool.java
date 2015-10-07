package com.example.tseng.brainwave_test;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tseng.brainwave_test.adapter.GridViewImageAdapter;
import com.example.tseng.brainwave_test.helper.AppConstant;
import com.example.tseng.brainwave_test.helper.Utils;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

public class PicturePool extends Activity {

    private int count;
    private Bitmap[] thumbnails;
    private boolean[] thumbnailsselection;
    private String[] arrPath;
    private ImageAdapter imageAdapter;
    ArrayList<String> f = new ArrayList<String>();// list of file paths
    File[] listFile;

    ShareDialog shareDialog;
    String path= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()+"/Camera/1444032498686.jpg";//要鎖定路徑


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_pool);
        getFromSdcard();
        GridView imagegrid = (GridView) findViewById(R.id.PhoneImageGrid);
        imageAdapter = new ImageAdapter();
        imagegrid.setAdapter(imageAdapter);
        ImageButton emotionbtn = (ImageButton) findViewById(R.id.emotionbutton);
        emotionbtn.setOnClickListener(emotionListenner);

        ImageButton camerabtn =(ImageButton) findViewById(R.id.camerabutton);
        camerabtn.setOnClickListener(cameraListener);

        ImageButton sharebtn =(ImageButton) findViewById(R.id.sharebutton);
        shareDialog=new ShareDialog(this);
        sharebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap image = BitmapFactory.decodeFile(path);
                SharePhoto photo = new SharePhoto.Builder().setBitmap(image).build();
                SharePhotoContent content = new SharePhotoContent.Builder().addPhoto(photo).build();
                shareDialog.show(content);
            }
        });

    }
    private ImageButton.OnClickListener emotionListenner = new
            ImageButton.OnClickListener() {
                public void onClick(View v){
                    finish();//轉跳頁面不佔存原來的Activity
                    Intent intent = new Intent();
                    intent.setClass(PicturePool.this,braininfo.class);
                    startActivity(intent);
                    // setContentView(R.layout.activity_cameramodel);
                }
            };


    private ImageButton.OnClickListener cameraListener =new
            ImageButton.OnClickListener(){
                @Override
                public void onClick(View v) {
                    finish();
                    Intent intent =new Intent();
                    intent.setClass(PicturePool.this,Cameramodel.class);
                    startActivity(intent);
                }
            };

    private ImageButton.OnClickListener shareListener =new
            ImageButton.OnClickListener(){
                @Override
                public void onClick(View v) {
            //放分享的程式碼

                }
            };
    public void getFromSdcard()
    {
        File file= new File(android.os.Environment.getExternalStorageDirectory(),"camtest");

        if (file.isDirectory())
        {
            listFile = file.listFiles();


            for (int i = 0; i < listFile.length; i++)
            {

                f.add(listFile[i].getAbsolutePath());

            }
        }
    }

    public class ImageAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public ImageAdapter() {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return f.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;

        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(
                        R.layout.gelleryitem, null);
                holder.imageview = (ImageView) convertView.findViewById(R.id.thumbImage);

                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }


            Bitmap myBitmap = BitmapFactory.decodeFile(f.get(position));
            holder.imageview.setImageBitmap(myBitmap);
            return convertView;
        }
    }
    class ViewHolder {
        ImageView imageview;


    }

}