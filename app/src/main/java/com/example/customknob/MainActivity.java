package com.example.customknob;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private CustomKnob customKnob;
    private WifiHelper wifiHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customKnob=(CustomKnob)findViewById(R.id.custom_knob);
        customKnob.setRotateDegree(30);
        customKnob.setOnRotateListener(new CustomKnob.OnRotateListener() {
            @Override
            public void changingProgress(int progress) {
                Log.v("test","changingProgress:"+progress);
            }

            @Override
            public void rotatedProgress(int progress) {
                Log.v("test","rotatedProgress:"+progress);
            }
        });
        wifiHelper=new WifiHelper(this);
        byte[] ipByte=wifiHelper.getByteIPAddress();
        for(int i=0;i<ipByte.length;i++){
            Log.v("test",ipByte[i]+"");
        }
    }

}
