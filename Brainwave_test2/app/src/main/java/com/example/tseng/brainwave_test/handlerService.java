package com.example.tseng.brainwave_test;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.neurosky.thinkgear.TGDevice;

/**
 * Created by user on 2015/7/7.
 */
public class handlerService extends Service {
    RingBuffer attentionBuf=new RingBuffer<>(6);
    int attentionResult;

    public static final String
            ACTION_LOCATION_BROADCAST = handlerService.class.getName() + "LocationBroadcast",
            EXTRA_STATE = "extra_state",
            EXTRA_ATTENTION="extra_attention",
            EXTRA_MEDITATION="extra_meditation",
            EXTRA_ATTENTION_BUFFER="attentionBuf";


    String state;
    int attention,meditation;


    BluetoothAdapter bluetoothAdapter;
    TGDevice tgDevice;
    final boolean rawEnabled = false;


    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        tgDevice = new TGDevice(bluetoothAdapter, handler);
        if(tgDevice.getState() != TGDevice.STATE_CONNECTING && tgDevice.getState() != TGDevice.STATE_CONNECTED)
            tgDevice.connect(rawEnabled);
    }

    private void sendBroadcastMessage() {
        Intent intent = new Intent(ACTION_LOCATION_BROADCAST);
        intent.putExtra(EXTRA_STATE, state);
        intent.putExtra(EXTRA_ATTENTION, attention);
        intent.putExtra(EXTRA_MEDITATION, meditation);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TGDevice.MSG_STATE_CHANGE:

                    switch (msg.arg1) {
                        case TGDevice.STATE_IDLE:
                            break;
                        case TGDevice.STATE_CONNECTING:
                            state="Connecting...\n";
                            sendBroadcastMessage();
                            break;
                        case TGDevice.STATE_CONNECTED:
                            tgDevice.start();
                            state="Connected.\n";
                            sendBroadcastMessage();
                            break;
                        case TGDevice.STATE_NOT_FOUND:
                            state="Can't find\n";
                            sendBroadcastMessage();
                            break;
                        case TGDevice.STATE_NOT_PAIRED:
                            state="not paired\n";
                            sendBroadcastMessage();
                            break;
                        case TGDevice.STATE_DISCONNECTED:
                            state="Disconnected \n";
                            sendBroadcastMessage();
                    }

                    break;
                case TGDevice.MSG_POOR_SIGNAL:
                    //signal = msg.arg1;
                   state= "PoorSignal: " + msg.arg1 + "\n";
                    sendBroadcastMessage();
                    break;
                case TGDevice.MSG_RAW_DATA:
                    //raw1 = msg.arg1;
                    //tv.append("Got raw: " + msg.arg1 + "\n");
                    break;
                case TGDevice.MSG_HEART_RATE:
                    state="Heart rate: " + msg.arg1 + "\n";
                    sendBroadcastMessage();
                    break;
                case TGDevice.MSG_ATTENTION:
                    //att = msg.arg1;
                    attention=msg.arg1;
                    attentionBuf.push(msg.arg1);
//                    for(int i =0;i<attentionBuf.size();i++){
//
//                    }
                    sendBroadcastMessage();
                    //Log.v("HelloA", "Attention: " + att + "\n");
                    break;
                case TGDevice.MSG_MEDITATION:
                    meditation=msg.arg1;
                    sendBroadcastMessage();
                    break;
                case TGDevice.MSG_BLINK:
                    state="Blink: " + msg.arg1 + "\n";
                    sendBroadcastMessage();
                    break;
                case TGDevice.MSG_RAW_COUNT:
                    //tv.append("Raw Count: " + msg.arg1 + "\n");
                    break;
                case TGDevice.MSG_LOW_BATTERY:
                    Toast.makeText(getApplicationContext(), "Low battery!", Toast.LENGTH_SHORT).show();
                    sendBroadcastMessage();
                    break;
                case TGDevice.MSG_RAW_MULTI:
                    //TGRawMulti rawM = (TGRawMulti)msg.obj;
                    //tv.append("Raw1: " + rawM.ch1 + "\nRaw2: " + rawM.ch2);
                default:
                    break;
            }
        }
    };





}
