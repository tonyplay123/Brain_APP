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
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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

public class PicturePool extends ActionBarActivity {

    public ImageAdapter imageAdapter;

    public GridView imagegrid;
    ShareDialog shareDialog;
    SharePhotoContent content;
    ArrayList<SharePhoto> photos = new ArrayList<>();

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_pool);

        imageAdapter = new ImageAdapter();
        imageAdapter.initialize();
        imagegrid = (GridView) findViewById(R.id.PhoneImageGrid);
        imagegrid.setAdapter(imageAdapter);
        shareDialog = new ShareDialog(this);
        ImageButton camerabtn = (ImageButton) findViewById(R.id.camerabutton);
        camerabtn.setOnClickListener(cameraListenner);

        ImageButton emotionbtn =(ImageButton) findViewById(R.id.emotionbutton);
        emotionbtn.setOnClickListener(emotionListener);

        ImageButton selectBtn = (ImageButton) findViewById(R.id.picturebutton);
        selectBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                final int len =imageAdapter.images.size();
                int cnt = 0;
                String selectImages = "";
                for (int i = 0; i < len; i++) {
                    if (imageAdapter.images.get(i).selection) {
                        cnt++;
                        selectImages = selectImages
                                + imageAdapter.images.get(i).id + ",";
                }
                }
                if (cnt == 0) {
                    Toast.makeText(getApplicationContext(),
                            "Please select at least one image",
                            Toast.LENGTH_LONG).show();
                } else {

                    selectImages = selectImages.substring(0,selectImages.lastIndexOf(","));
                    String[] arrIds =selectImages.split(",");

                    for(int i = 0;i<arrIds.length;i++) {
                        String path = imageAdapter.mFilePath[Integer.parseInt(arrIds[i])];

                        Bitmap bitmap = BitmapFactory.decodeFile(path);
                        SharePhoto photo = new SharePhoto.Builder().setBitmap(bitmap).build();
                        photos.add(photo);


                    }
                    share(photos);

                }

            }
        });





    }

    private ImageButton.OnClickListener cameraListenner = new
            ImageButton.OnClickListener() {
                public void onClick(View v){

                    Intent intent = new Intent();
                    intent.setClass(PicturePool.this,Cameramodel.class);
                    startActivity(intent);
                    // setContentView(R.layout.activity_cameramodel);
                }
            };


    private ImageButton.OnClickListener emotionListener =new
            ImageButton.OnClickListener(){
                @Override
                public void onClick(View v) {

                    Intent intent =new Intent();
                    intent.setClass(PicturePool.this,braininfo.class);
                    startActivity(intent);
                }
            };

    public void share(ArrayList photos){

        if (ShareDialog.canShow(SharePhotoContent.class)) {
            content = new SharePhotoContent.Builder().addPhotos(photos).build();
            shareDialog.show(content);
        }

    }



    public class ImageAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        public ArrayList<ImageItem> images = new ArrayList<ImageItem>();
        File[] listFile;
        String[] mFilePath,mFilename;

        public ImageAdapter() {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void initialize() {
            images.clear();
            File file= new File(android.os.Environment.getExternalStorageDirectory(),"camtest");

            if (file.isDirectory())
            {
                listFile = file.listFiles();
                mFilePath = new String[listFile.length];
                mFilename = new String[listFile.length];
                for (int i = 0; i < listFile.length; i++)
                {
                    mFilePath[i] = listFile[i].getAbsolutePath();
                    mFilename[i] = listFile[i].getName();
                    int id = i;
                    ImageItem imageItem = new ImageItem();
                    imageItem.id = id;
                    imageItem.img = MediaStore.Images.Thumbnails.getThumbnail(
                            getApplicationContext().getContentResolver(), id,
                            MediaStore.Images.Thumbnails.MINI_KIND,null);
                    imageItem.img = BitmapFactory.decodeFile(mFilePath[i]);
                    images.add(imageItem);
                }
            }
        }

        public int getCount() {
            return images.size();
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
                holder.checkbox = (CheckBox) convertView.findViewById(R.id.itemCheckBox);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            ImageItem item = images.get(position);
            holder.checkbox.setId(position);
            holder.imageview.setId(position);

            holder.checkbox.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    CheckBox cb = (CheckBox) v;
                    int id = cb.getId();
                    if (images.get(id).selection) {
                        cb.setChecked(false);
                        images.get(id).selection = false;
                    } else {
                        cb.setChecked(true);
                        images.get(id).selection = true;
                    }

                }
            });


//            holder.imageview.setOnClickListener(new View.OnClickListener() {
//
//                public void onClick(View v) {
//                    // TODO Auto-generated method stub
//                    int id = v.getId();
//                    ImageItem item = images.get(id);
//                    Intent intent = new Intent();
//                    intent.setAction(Intent.ACTION_VIEW);
//                    if (images != null && getCount() > 0) {
//
//                            String path = mFilePath[item.id];
//                            item.img = BitmapFactory.decodeFile(path);
//
//                    }
//                }
//            });

            holder.imageview.setImageBitmap(item.img);
            holder.checkbox.setChecked(item.selection);
            return convertView;
        }
    }
    class ViewHolder {
        ImageView imageview;
        CheckBox checkbox;
    }

    class ImageItem {
        boolean selection;
        int id;
        Bitmap img;
    }

}