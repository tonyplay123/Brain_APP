package com.example.tseng.brainwave_test;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.neurosky.thinkgear.TGDevice;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.Buffer;


public class Cameramodel extends Activity {

    private static final String TAG = "CamTestActivity";
    camera preview;
    Button buttonClick;
    Camera camera;
    Activity act;
    Context ctx;
    SurfaceView focus;
    ImageView AttentionTen,AttentionOne;
    RingBuffer attentionBuf=new RingBuffer<>(6);
    int attentionResult;
//    BluetoothAdapter bluetoothAdapter;
//    TGDevice tgDevice;
//    final boolean rawEnabled = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        act = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_cameramodel);
        AttentionTen =(ImageView)findViewById(R.id.attTen);
        AttentionOne =(ImageView)findViewById(R.id.attOne);

        preview = new camera(this, (SurfaceView)findViewById(R.id.surfaceView));
        preview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ((FrameLayout) findViewById(R.id.layout)).addView(preview);
        preview.setKeepScreenOn(true);



        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        String state = intent.getStringExtra(handlerService.EXTRA_STATE);

                        int att = intent.getIntExtra(handlerService.EXTRA_ATTENTION,0);
                        attnumberone(att,AttentionOne);
                        attnumberten(att,AttentionTen);
                        if(att>=60){
                            Toast.makeText(Cameramodel.this, "已拍照並儲存相片" , Toast.LENGTH_SHORT).show();
                                         camera.takePicture(shutterCallback, rawCallback, jpegCallback);//拍照
                        }
                        int med = intent.getIntExtra(handlerService.EXTRA_MEDITATION,0);

                    }
                }, new IntentFilter(handlerService.ACTION_LOCATION_BROADCAST)
        );


//        tgDevice = new TGDevice(bluetoothAdapter, handler);
//        if(tgDevice.getState() != TGDevice.STATE_CONNECTING && tgDevice.getState() != TGDevice.STATE_CONNECTED)
//            tgDevice.connect(rawEnabled);

//        preview.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                camera.takePicture(shutterCallback, rawCallback, jpegCallback);
//            }
//        });
        focus =(SurfaceView)findViewById(R.id.surfaceView);
        focus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                camera.autoFocus(new Camera.AutoFocusCallback(){
                    @Override
                    public void onAutoFocus(boolean arg0, Camera arg1) {
                        //camera.takePicture(shutterCallback, rawCallback, jpegCallback);
                    }
                });

            }
        });;

        buttonClick = (Button) findViewById(R.id.button);

        buttonClick.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                camera.stopPreview();
                // 拍照提示訊息
                AlertDialog.Builder dialog1 = new AlertDialog.Builder(Cameramodel.this);
                dialog1.setTitle("訊息");
                dialog1.setMessage("你確定是這張照片嗎?");

                //取消按鈕
                dialog1.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        camera.startPreview();
                    }
                });
                //OK按鈕
                dialog1.setPositiveButton(R.string.ok_label,//
                        new DialogInterface.OnClickListener(){
                            public void onClick(
                                    DialogInterface dialoginterface, int i){
                                camera.startPreview();
                                //preview.camera.takePicture(shutterCallback, rawCallback, jpegCallback);
                                camera.takePicture(shutterCallback, rawCallback, jpegCallback);//拍照
                            }
                        });
                dialog1.show();

            }
        });

        buttonClick.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View arg0) {
                camera.autoFocus(new Camera.AutoFocusCallback(){
                    @Override
                    public void onAutoFocus(boolean arg0, Camera arg1) {
                        //camera.takePicture(shutterCallback, rawCallback, jpegCallback);
                    }
                });
                return true;
            }
        });

        		buttonClick = (Button) findViewById(R.id.button);

        		buttonClick.setOnClickListener(new View.OnClickListener() {
        			public void onClick(View v) {
        //				preview.camera.takePicture(shutterCallback, rawCallback, jpegCallback);
        				camera.takePicture(shutterCallback, rawCallback, jpegCallback);
        			}
        		});

        		buttonClick.setOnLongClickListener(new View.OnLongClickListener(){
        			@Override
        			public boolean onLongClick(View arg0) {
        				camera.autoFocus(new Camera.AutoFocusCallback(){
        					@Override
        					public void onAutoFocus(boolean arg0, Camera arg1) {
        						//camera.takePicture(shutterCallback, rawCallback, jpegCallback);
        					}
        				});
        				return true;
        			}
        		});

    }

    @Override
    protected void onResume() {
        super.onResume();
        int numCams = Camera.getNumberOfCameras();
        if(numCams > 0){
            try{
                camera = Camera.open(0);
                camera.startPreview();
                preview.setCamera(camera);
            } catch (RuntimeException ex){
                Toast.makeText(ctx, getString(R.string.camera_not_found), Toast.LENGTH_LONG).show();
            }
        }
        startService(new Intent(this, handlerService.class));
    }

    @Override
    protected void onPause() {
        if(camera != null) {
            camera.stopPreview();
            preview.setCamera(null);
            camera.release();
            camera = null;
        }
        super.onPause();
        stopService(new Intent(this, handlerService.class));
    }

    private void resetCam() {
        camera.startPreview();
        preview.setCamera(camera);
    }

    private void refreshGallery(File file) {
        Intent mediaScanIntent = new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(Uri.fromFile(file));
        sendBroadcast(mediaScanIntent);
    }

    Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        public void onShutter() {
            //			 Log.d(TAG, "onShutter'd");
        }
    };

    Camera.PictureCallback rawCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            //			 Log.d(TAG, "onPictureTaken - raw");
        }
    };

    Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            new SaveImageTask().execute(data);
            resetCam();
            Log.d(TAG, "onPictureTaken - jpeg");
        }
    };

    private class SaveImageTask extends AsyncTask<byte[], Void, Void> {

        @Override
        protected Void doInBackground(byte[]... data) {
            FileOutputStream outStream = null;

            // Write to SD Card
            try {
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File (sdCard.getAbsolutePath() + "/camtest");
                dir.mkdirs();

                String fileName = String.format("%d.jpg", System.currentTimeMillis());
                File outFile = new File(dir, fileName);

                outStream = new FileOutputStream(outFile);
                outStream.write(data[0]);
                outStream.flush();
                outStream.close();

                Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length + " to " + outFile.getAbsolutePath());

                refreshGallery(outFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            }
            return null;
        }

    }
    private void attnumberone(int num, ImageView i){//專注個位數

        num=num%10;
        switch (num){
            case 0:
                i.setImageResource(R.drawable.num0);
                break;
            case 1:
                i.setImageResource(R.drawable.num1);
                break;
            case 2:
                i.setImageResource(R.drawable.num2);
                break;
            case 3:
                i.setImageResource(R.drawable.num3);
                break;
            case 4:
                i.setImageResource(R.drawable.num4);
                break;
            case 5:
                i.setImageResource(R.drawable.num5);
                break;
            case 6:
                i.setImageResource(R.drawable.num6);
                break;
            case 7:
                i.setImageResource(R.drawable.num7);
                break;
            case 8:
                i.setImageResource(R.drawable.num8);
                break;
            case 9:
                i.setImageResource(R.drawable.num9);
                break;

        }
    }
    private void attnumberten(int num,ImageView i) {//專注十位數
        num=num/10;
        switch (num) {
            case 0:
                i.setImageResource(R.drawable.num0);
                break;
            case 1:
                i.setImageResource(R.drawable.num1);
                break;
            case 2:
                i.setImageResource(R.drawable.num2);
                break;
            case 3:
                i.setImageResource(R.drawable.num3);
                break;
            case 4:
                i.setImageResource(R.drawable.num4);
                break;
            case 5:
                i.setImageResource(R.drawable.num5);
                break;
            case 6:
                i.setImageResource(R.drawable.num6);
                break;
            case 7:
                i.setImageResource(R.drawable.num7);
                break;
            case 8:
                i.setImageResource(R.drawable.num8);
                break;
            case 9:
                i.setImageResource(R.drawable.num9);
                break;
        }
    }


}
