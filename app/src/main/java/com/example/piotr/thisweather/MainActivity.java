package com.example.piotr.thisweather;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.os.AsyncTask;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    ListView lv;
    private ArrayList<HashMap<String, String>> citiesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getLoaderManager().initLoader(0, null, );

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        citiesList = new ArrayList<>();
        new GetCities().execute();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                String id = sharedPref.getString("ID", "none");
                if(!id.equals("none")) {
                    for (int i = 0; i<citiesList.size(); i++){
                        if(citiesList.get(i).get("ID").equals(id)){
                            startSpecifiedActivity(i);
                        }
                    }
                }
                else Toast.makeText(MainActivity.this, "Home weather not set", Toast.LENGTH_SHORT).show();
            }
        });

        final SwipeRefreshLayout swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetCities getCities = new GetCities();
                getCities.execute();
                swipeRefresh.setRefreshing(false);
            }
        });

        lv = (ListView) findViewById(R.id.list);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startSpecifiedActivity(i);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        findViewById(R.id.loading).setVisibility(View.GONE);
    }

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
            Intent settings = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(settings);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class GetCities extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this, "Refreshing", Toast.LENGTH_LONG).show();

        }
        @Override
        protected Void doInBackground(Void...arg0) {
            refresh();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            listAdapter();
        }
    }

    public final void listAdapter(){
        ListAdapter adapter = new SimpleAdapter(MainActivity.this, citiesList,
                R.layout.list_item, new String[]{"Name", "Temp", "Description"},
                new int[]{R.id.city, R.id.temp, R.id.description});
        lv.setAdapter(adapter);
    }

    public final void refresh(){
        HttpHandler sh = new HttpHandler();
        citiesList.clear();
        String url = "http://api.openweathermap.org/data/2.5/box/city?bbox=13.9,48.8,24.5,54.9,40&cluster=yes&appid=59a58c36edc289243e879bcd9da785e4";
        String jsonStr = sh.makeServiceCall(url);
        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);

                // Getting JSON Array node
                JSONArray cities = jsonObj.getJSONArray("list");

                for (int i = 0; i < cities.length(); i++) {
                    JSONObject c = cities.getJSONObject(i);
                    String city = c.getString("name");
                    String time = c.getString("dt");
                    String id = c.getString("id");

                    //JSONObject sys = c.getJSONObject("sys");
                    //String sunset = sys.getString("sunset");
                    //String sunrise = sys.getString("sunrise");

                    JSONArray weather1 = c.getJSONArray("weather");
                    JSONObject weather2 = weather1.getJSONObject(0);
                    String description = weather2.getString("description");

                    JSONObject wind = c.getJSONObject("wind");
                    String ws = wind.getString("speed");
                    String windSpeed = ws.concat(" m/s");
                    //if (sharedPref.getInt("Speed Unit",0) == 1)
                    //    windSpeed = Double.toString(Double.parseDouble(ws)*3.6).concat(" km/h");
                    JSONObject clouds = c.getJSONObject("clouds");
                    String cloudiness = clouds.getString("all");

                    String windDir = wind.getString("deg");

                    JSONObject main = c.getJSONObject("main");
                    String temp = main.getString("temp");
                    String tempMin = main.getString("temp_min");
                    String tempMax = main.getString("temp_max");
                    String pressure = main.getString("pressure");
                    String humidity = main.getString("humidity");

                    DecimalFormat df = new DecimalFormat("#.#");
                    temp = df.format(Double.parseDouble(temp)).toString().concat("°C");
                    tempMin = df.format(Double.parseDouble(tempMin)).toString().concat("°C");
                    tempMax = df.format(Double.parseDouble(tempMax)).toString().concat("°C");

                    HashMap<String, String> weather = new HashMap<>();

                    weather.put("ID", id);
                    weather.put("Name", city);
                    weather.put("Time", time);
                    weather.put("Temp", temp);
                    weather.put("Temp Min", tempMin);
                    weather.put("Temp Max", tempMax);
                    weather.put("Wind Speed", windSpeed);
                    weather.put("Wind Dir", windDir);
                    //weather.put("Sunrise", sunrise);
                    //weather.put("Sunset", sunset);
                    weather.put("Cloudiness", cloudiness);
                    weather.put("Description", description);
                    weather.put("Pressure", pressure);
                    weather.put("Humidity", humidity);
                    citiesList.add(weather);
                }

            } catch (final JSONException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Json parsing error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
            Collections.sort(citiesList, new Comparator<HashMap<String, String>>() {
                @Override
                public int compare(HashMap<String, String> stringStringHashMap, HashMap<String, String> t1) {
                    return stringStringHashMap.get("Name").compareTo(t1.get("Name"));
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Couldn't get json from server. Check LogCat for possible errors!",
                            Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    public void startSpecifiedActivity(int i){
        findViewById(R.id.loading).setVisibility(View.VISIBLE);
        Intent intent = new Intent(MainActivity.this, SpecifiedWeather.class);
        intent.putExtra("cityName", citiesList.get(i).get("Name"));
        intent.putExtra("id", citiesList.get(i).get("ID"));
        //  intent.putExtra("time", citiesList.get(i).get("Time"));
        intent.putExtra("temp", citiesList.get(i).get("Temp"));
        intent.putExtra("description", citiesList.get(i).get("Description"));
        intent.putExtra("tempMin", citiesList.get(i).get("Temp Min"));
        intent.putExtra("tempMax", citiesList.get(i).get("Temp Max"));
        intent.putExtra("windSpeed", citiesList.get(i).get("Wind Speed"));
        intent.putExtra("windDir", citiesList.get(i).get("Wind Dir"));
        intent.putExtra("sunrise", citiesList.get(i).get("Sunrise"));
        intent.putExtra("sunset", citiesList.get(i).get("Sunset"));
        intent.putExtra("pressure", citiesList.get(i).get("Pressure"));
        intent.putExtra("humidity", citiesList.get(i).get("Humidity"));
        intent.putExtra("cloudiness", citiesList.get(i).get("Cloudiness"));
        startActivity(intent);
    }

}

