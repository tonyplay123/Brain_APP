package com.example.tseng.brainwave_test;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.FacebookSdk;
import com.neurosky.thinkgear.TGDevice;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MainActivity extends Activity {
    BluetoothAdapter bluetoothAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button testbtn = (Button) findViewById(R.id.testbutton);
        testbtn.setOnClickListener(buttonListener);
        final ImageView imageView =(ImageView)findViewById(R.id.power);
        //藍芽設定
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            // Alert user that Bluetooth is not available
            Toast.makeText(this, "Bluetooth 不支援", Toast.LENGTH_LONG).show();

            return;
        } else if(bluetoothAdapter.isEnabled()){
        /* create the TGDevice */
            LocalBroadcastManager.getInstance(this).registerReceiver(
                    new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {
                            String state = intent.getStringExtra(handlerService.EXTRA_STATE);
                            //tv.setText(state);
//                            if(state=="Connected.\n"){
//                                Toast.makeText(MainActivity.this,"腦波儀已接上！",Toast.LENGTH_LONG).show();
//                                imageView.setImageResource(R.drawable.on);
//
//
//                            }else if(state=="Can't find\n"){
//                                imageView.setImageResource(R.drawable.off);
//                            }
                            int att = intent.getIntExtra(handlerService.EXTRA_ATTENTION,0);

                            int med = intent.getIntExtra(handlerService.EXTRA_MEDITATION,0);

                        }
                    }, new IntentFilter(handlerService.ACTION_LOCATION_BROADCAST)
            );


        }



    }



    private Button.OnClickListener buttonListener = new
            Button.OnClickListener() {
                public void onClick(View v) {
                    //setContentView(R.layout.activity_braininfo);
                    finish();
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, braininfo.class);
                    startActivity(intent);
                }

            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void onDestroy() {
        super.onDestroy();
    }


    public BluetoothAdapter getBluetoothAdapter() {
        return bluetoothAdapter;
    }

    public void setBluetoothAdapter(BluetoothAdapter bluetoothAdapter) {
        this.bluetoothAdapter = bluetoothAdapter;
    }
}
