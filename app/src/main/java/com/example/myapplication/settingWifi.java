package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.ImageView;
import android.widget.TextView;

public class settingWifi extends BroadcastReceiver
{
    public settingWifi(TextView txtWifi, ImageView img_w) {
        this.txtWifi = txtWifi;
        this.img_w = img_w;
    }

    private TextView txtWifi = null;
    private ImageView img_w;
    public settingWifi() {}
    @Override
    public void onReceive(Context context, Intent intent) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        int wifiState = wifiManager.getWifiState();
        switch (wifiState)
        {
            case WifiManager.WIFI_STATE_ENABLED:
            {
                txtWifi.setText("Wifi is on");
                img_w.setImageResource(R.drawable.wifi_on);
            }
                break;
            case WifiManager.WIFI_STATE_DISABLED:
            {
                txtWifi.setText("Wifi is off");
                img_w.setImageResource(R.drawable.wifi_off);
            }
                break;
            }

    }
}
