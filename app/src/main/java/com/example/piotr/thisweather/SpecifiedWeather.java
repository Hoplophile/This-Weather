package com.example.piotr.thisweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class SpecifiedWeather extends AppCompatActivity {
    SharedPreferences sharedPref;
    Intent in;
    TextView city_name_view;
    TextView temp_view;
    TextView min_temp_view;
    TextView max_temp_view;
    TextView description_view;
    ImageView wind_dir_ic;
    TextView wind_speed_view;
    TextView humidity_view;
    TextView pressure_view;
    TextView cloudiness_view;
    ImageButton homeSetter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specified_weather);

        in = getIntent();

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        String cityName = in.getStringExtra("cityName");
        String temp = in.getStringExtra("temp");
        String description = in.getStringExtra("description");
        String tempMin = in.getStringExtra("tempMin");
        String tempMax = in.getStringExtra("tempMax");
        String windSpeed = in.getStringExtra("windSpeed");
        String windDir = in.getStringExtra("windDir");
        String pressure = in.getStringExtra("pressure");
        String humidity = in.getStringExtra("humidity");
        String cloudiness = in.getStringExtra("cloudiness");

        city_name_view = (TextView)findViewById(R.id.city_name_view);
        temp_view = (TextView)findViewById(R.id.temp_view);
        min_temp_view = (TextView)findViewById(R.id.min_temp_view);
        max_temp_view = (TextView)findViewById(R.id.max_temp_view);
        description_view = (TextView)findViewById(R.id.description_view);
        wind_dir_ic = (ImageView)findViewById(R.id.wind_dir_ic);
        wind_speed_view = (TextView)findViewById(R.id.wind_speed_view);
        humidity_view = (TextView)findViewById(R.id.humidity_view);
        pressure_view = (TextView)findViewById(R.id.pressure_view);
        cloudiness_view = (TextView)findViewById(R.id.cloudiness_view);
        homeSetter = (ImageButton) findViewById(R.id.home_weather);

        if(sharedPref.getString("ID", "0").equals(in.getStringExtra("id")))
            homeSetter.setBackgroundResource(R.drawable.rounded_if_home);

        chooseWindDirectionIcon(windDir, wind_dir_ic);

        city_name_view.setText(cityName);
        temp_view.setText(temp);
        description_view.setText(description);
        min_temp_view.setText("Min temp: "+ tempMin);
        max_temp_view.setText("Max temp: "+ tempMax);
        wind_speed_view.setText(windSpeed);
        humidity_view.setText(humidity + "%");
        pressure_view.setText(pressure + " HPa");
        cloudiness_view.setText(cloudiness + "%");
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
        SharedPreferences.Editor edit = sharedPref.edit();
        edit.remove("ID");
        edit.putString("ID",id);
        edit.apply();
        Toast.makeText(SpecifiedWeather.this,
                "Home weather set to " + in.getStringExtra("cityName"),
                Toast.LENGTH_LONG).show();
        homeSetter.setBackgroundResource(R.drawable.rounded_if_home);
    }
}