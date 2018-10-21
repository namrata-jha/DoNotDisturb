package com.example.android.callblocker;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    ToggleButton isDndOn;
    TextView label;
    BroadcastReceiver incomingCallReceiver;

    private static final int PERMISSION_REQUEST_READ_PHONE_STATE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getRequiredPermissions();
        incomingCallReceiver = new IncomingCallReceiver();
        isDndOn = findViewById(R.id.toggle);
        label = findViewById(R.id.label);

        isDndOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(200);
            }
        });
        isDndOn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    getRequiredPermissions();
                    IntentFilter filter = new IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
                    registerReceiver(incomingCallReceiver, filter);
                    label.setText("Do not disturb mode enabled.");
                } else if (!isChecked) {
                    unregisterReceiver(incomingCallReceiver);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_READ_PHONE_STATE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    isDndOn.setChecked(false);
                    Toast.makeText(this, "DND services cannot function without the required permissions.", Toast.LENGTH_SHORT).show();
                }

                return;
            }
        }
    }


    public void onClickAddContacts(View view) {
        Intent intent = new Intent(this, ContactsActivity.class);
        startActivity(intent);

    }

    private void getRequiredPermissions() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_DENIED) {
                String[] permissions = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS};
                requestPermissions(permissions, PERMISSION_REQUEST_READ_PHONE_STATE);
            }
        }
    }

}
