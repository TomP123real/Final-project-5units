package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.widget.ImageView;
import android.widget.TextView;

public class settingFlightMode extends BroadcastReceiver
{
    private TextView airplane = null;
    private ImageView img_a;

    public settingFlightMode(TextView airplane, ImageView img_a) {
        this.airplane = airplane;
        this.img_a = img_a;
    }
    public settingFlightMode() {}

    private static boolean isAirplaneModeOn(Context context)
    {
        return Settings.System.getInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (isAirplaneModeOn(context.getApplicationContext()))
        {
            airplane.setText("AirPlane mode is on");
            img_a.setImageResource(R.drawable.plane_on);
        }

        else
        {
            airplane.setText("AirPlane mode is off");
            img_a.setImageResource(R.drawable.plane_off);
        }

    }
}
