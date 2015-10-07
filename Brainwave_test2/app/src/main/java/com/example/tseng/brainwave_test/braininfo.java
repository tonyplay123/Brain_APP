package com.example.tseng.brainwave_test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class braininfo extends ActionBarActivity {

//    BluetoothAdapter bluetoothAdapter;
//    TextView tv;
    TextView relax;
    String attention;
    String medititon;
    ImageView relaxten,relaxone,attentionten,attentionone;
    int temprelax,tempattention;


//    TGDevice tgDevice;
//    final boolean rawEnabled = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_braininfo);
//        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();

        final TextView tv =(TextView)findViewById(R.id.statevalue);
        final ImageView imageView =(ImageView)findViewById(R.id.power);

        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        String state = intent.getStringExtra(handlerService.EXTRA_STATE);
                        //tv.setText(state);
                        if(state=="Connected.\n"){
                            Toast.makeText(braininfo.this, "腦波儀已成功接上！", Toast.LENGTH_SHORT).show();
                            imageView.setImageResource(R.drawable.on);
                        }
//                        else if(state=="Disconnected \n"|state=="Can't find\n"){
//                            Toast.makeText(braininfo.this, "請重新確認是否連接腦波儀！", Toast.LENGTH_SHORT).show();
//                            imageView.setImageResource(R.drawable.off);
//                        }
                        int att = intent.getIntExtra(handlerService.EXTRA_ATTENTION,0);
                        attnumberone(att,attentionone);
                        attnumberten(att,attentionten);
                        int med = intent.getIntExtra(handlerService.EXTRA_MEDITATION,0);
                        mednumberone(med,relaxone);
                        mednumberten(med,relaxten);
                    }
                }, new IntentFilter(handlerService.ACTION_LOCATION_BROADCAST)
        );


        ImageButton camerabtn = (ImageButton) findViewById(R.id.camerabutton);
        camerabtn.setOnClickListener(cameraListenner);

        ImageButton picturebtn =(ImageButton) findViewById(R.id.picturebutton);
        picturebtn.setOnClickListener(pictureListener);




        relaxten= (ImageView) findViewById(R.id.relaxten);
        relaxone= (ImageView) findViewById(R.id.relaxone);
        attentionten= (ImageView) findViewById(R.id.attentionten);
        attentionone= (ImageView) findViewById(R.id.attentionone);



//        tgDevice = new TGDevice(bluetoothAdapter, handler);
//        if(tgDevice.getState() != TGDevice.STATE_CONNECTING && tgDevice.getState() != TGDevice.STATE_CONNECTED)
//            tgDevice.connect(rawEnabled);

    }

    @Override
    protected void onResume() {
        super.onResume();
        startService(new Intent(this, handlerService.class));
    }

    @Override
    protected void onPause() {
        super.onStop();
        stopService(new Intent(this, handlerService.class));
    }

    private ImageButton.OnClickListener cameraListenner = new
            ImageButton.OnClickListener() {
                public void onClick(View v){

                    Intent intent = new Intent();
                    intent.setClass(braininfo.this,Cameramodel.class);
                    startActivity(intent);
                   // setContentView(R.layout.activity_cameramodel);
                }
            };


    private ImageButton.OnClickListener pictureListener =new
            ImageButton.OnClickListener(){
                @Override
                public void onClick(View v) {

                    Intent intent =new Intent();
                    intent.setClass(braininfo.this,PicturePool.class);
                    startActivity(intent);
                }
            };

    public void attnumberone(int num, ImageView i){//專注個位數

        num=num%10;
        switch (num){
            case 0:
                i.setImageResource(R.drawable.r0_r);
                break;
            case 1:
                i.setImageResource(R.drawable.r1_r);
                break;
            case 2:
                i.setImageResource(R.drawable.r2_r);
                break;
            case 3:
                i.setImageResource(R.drawable.r3_r);
                break;
            case 4:
                i.setImageResource(R.drawable.r4_r);
                break;
            case 5:
                i.setImageResource(R.drawable.r5_r);
                break;
            case 6:
                i.setImageResource(R.drawable.r6_r);
                break;
            case 7:
                i.setImageResource(R.drawable.r7_r);
                break;
            case 8:
                i.setImageResource(R.drawable.r8_r);
                break;
            case 9:
                i.setImageResource(R.drawable.r9_r);
                break;

        }
    }
    public void attnumberten(int num,ImageView i) {//專注十位數
        num=num/10;
        switch (num) {
            case 0:
                i.setImageResource(R.drawable.r0_l);
                break;
            case 1:
                i.setImageResource(R.drawable.r1_l);
                break;
            case 2:
                i.setImageResource(R.drawable.r2_l);
                break;
            case 3:
                i.setImageResource(R.drawable.r3_l);
                break;
            case 4:
                i.setImageResource(R.drawable.r4_l);
                break;
            case 5:
                i.setImageResource(R.drawable.r5_l);
                break;
            case 6:
                i.setImageResource(R.drawable.r6_l);
                break;
            case 7:
                i.setImageResource(R.drawable.r7_l);
                break;
            case 8:
                i.setImageResource(R.drawable.r8_l);
                break;
            case 9:
                i.setImageResource(R.drawable.r9_l);
                break;
        }
    }
    public void mednumberone(int num, ImageView i){//放鬆個位數

        num=num%10;
        switch (num){
            case 0:
                i.setImageResource(R.drawable.b0_r);
                break;
            case 1:
                i.setImageResource(R.drawable.b1_r);
                break;
            case 2:
                i.setImageResource(R.drawable.b2_r);
                break;
            case 3:
                i.setImageResource(R.drawable.b3_r);
                break;
            case 4:
                i.setImageResource(R.drawable.b4_r);
                break;
            case 5:
                i.setImageResource(R.drawable.b5_r);
                break;
            case 6:
                i.setImageResource(R.drawable.b6_r);
                break;
            case 7:
                i.setImageResource(R.drawable.b7_r);
                break;
            case 8:
                i.setImageResource(R.drawable.b8_r);
                break;
            case 9:
                i.setImageResource(R.drawable.b9_r);
                break;

        }
    }
    public void mednumberten(int num,ImageView i) {//放鬆十位數
        num=num/10;
        switch (num) {
            case 0:
                i.setImageResource(R.drawable.b0_l);
                break;
            case 1:
                i.setImageResource(R.drawable.b1_l);
                break;
            case 2:
                i.setImageResource(R.drawable.b2_l);
                break;
            case 3:
                i.setImageResource(R.drawable.b3_l);
                break;
            case 4:
                i.setImageResource(R.drawable.b4_l);
                break;
            case 5:
                i.setImageResource(R.drawable.b5_l);
                break;
            case 6:
                i.setImageResource(R.drawable.b6_l);
                break;
            case 7:
                i.setImageResource(R.drawable.b7_l);
                break;
            case 8:
                i.setImageResource(R.drawable.b8_l);
                break;
            case 9:
                i.setImageResource(R.drawable.b9_l);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_braininfo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
