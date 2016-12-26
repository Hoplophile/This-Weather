package com.example.piotr.thisweather;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class SpecifiedWeather extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specified_weather);

        Intent in = getIntent();
        String cityName = in.getStringExtra("cityName");
        String temp = in.getStringExtra("temp");
        String description = in.getStringExtra("description");
        String tempMin = in.getStringExtra("tempMin");
        String tempMax = in.getStringExtra("tempMax");
        String windSpeed = in.getStringExtra("windSpeed");
        String windDir = in.getStringExtra("windDir");
        String sunrise = in.getStringExtra("sunrise");
        String sunset = in.getStringExtra("sunset");
        String pressure = in.getStringExtra("pressure");
        String humidity = in.getStringExtra("humidity");
        String cloudiness = in.getStringExtra("cloudiness");

        final TextView city_name_view = (TextView)findViewById(R.id.city_name_view);
        final TextView temp_view = (TextView)findViewById(R.id.temp_view);
        final TextView min_temp_view = (TextView)findViewById(R.id.min_temp_view);
        final TextView max_temp_view = (TextView)findViewById(R.id.max_temp_view);
        final TextView description_view = (TextView)findViewById(R.id.description_view);
        final ImageView wind_dir_ic = (ImageView)findViewById(R.id.wind_dir_ic);
        final TextView wind_speed_view = (TextView)findViewById(R.id.wind_speed_view);
        final TextView humidity_view = (TextView)findViewById(R.id.humidity_view);
        final TextView pressure_view = (TextView)findViewById(R.id.pressure_view);
        final TextView cloudiness_view = (TextView)findViewById(R.id.cloudiness_view);
        //final TextView sunrise_view = (TextView)findViewById(R.id.sunrise_view);
        //final TextView sunset_view = (TextView)findViewById(R.id.sunset_view);
        //long sunrise2 = Long.parseLong(sunrise);
        //long sunset2 = Long.parseLong(sunset);

        //sunrise = getDateCurrentTimeZone(sunrise2);
        //sunset = getDateCurrentTimeZone(sunset2);

        chooseWindDirectionIcon(windDir, wind_dir_ic);

        city_name_view.setText(cityName);
        temp_view.setText(temp);
        description_view.setText(description);
        min_temp_view.setText("Min temp: "+ tempMin);
        max_temp_view.setText("Max temp: "+ tempMax);
        //wind_dir_view.setText(directionSymbol);
        wind_speed_view.setText(windSpeed);
        humidity_view.setText(humidity + "%");
        pressure_view.setText(pressure + " HPa");
        cloudiness_view.setText(cloudiness + "%");
        //sunrise_view.setText(sunrise);
        //sunset_view.setText(sunset);
    }

    public void chooseWindDirectionIcon(String direction, ImageView ic){
        Double angle = Double.parseDouble(direction);
        if(angle>335 || angle<25) ic.setBackgroundResource(R.drawable.ic_n);
        else if(angle>=25 && angle<=65) ic.setBackgroundResource(R.drawable.ic_ne);
        else if(angle>65 && angle<115) ic.setBackgroundResource(R.drawable.ic_e);
        else if(angle>=115 && angle<=155) ic.setBackgroundResource(R.drawable.ic_se);
        else if(angle>155 && angle<=205) ic.setBackgroundResource(R.drawable.ic_s);
        else if(angle>=205 && angle<=245) ic.setBackgroundResource(R.drawable.ic_sw);
        else if(angle>245 && angle<=295) ic.setBackgroundResource(R.drawable.ic_w);
        else if(angle>=295 && angle<=335) ic.setBackgroundResource(R.drawable.ic_nw);
    }

    public String getDateCurrentTimeZone(long timestamp) {
        try{
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getTimeZone("GMT+01:00");
            calendar.setTimeInMillis(timestamp * 1000);
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Date currentTimeZone = calendar.getTime();
            return sdf.format(currentTimeZone);
        } catch (Exception e) {
        }
        return "";
    }

    public void setHome(View view){
        Intent in = getIntent();
        String id = in.getStringExtra("id");
     //   Toast.makeText(SpecifiedWeather.this, id, Toast.LENGTH_SHORT).show();
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = sharedPref.edit();
        edit.remove("ID");
        edit.putString("ID",id);
        edit.apply();
        Toast.makeText(SpecifiedWeather.this, "Home weather set to " + in.getStringExtra("cityName"),Toast.LENGTH_LONG).show();
    }
}