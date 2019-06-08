package amit.action.myweatherapp;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import amit.action.myweatherapp.databinding.ActivityMainBinding;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public static final String TAG =MainActivity.class.getSimpleName();

    private CurrentWeather currentWeather;
    private ImageView iconImageView;
    TextView darkSky;

    double latitude=27.4956;
    double longitude=77.6856;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getForcast(latitude,longitude);

        darkSky=findViewById(R.id.darkSkyText);
        darkSky.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void getForcast(double latitude,double longitude) {
        final ActivityMainBinding binding= DataBindingUtil.setContentView(MainActivity.this,R.layout.activity_main);


        iconImageView=findViewById(R.id.icon);

        String apiKey="59ddc00736129fdfdb771bed116c73a2";

        String forcastUrl="https://api.darksky.net/forecast/"+apiKey+"/"+latitude+","+longitude;

        if (isNetworkAvailable()) {
            OkHttpClient okHttpClient = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(forcastUrl)
                    .build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            currentWeather=getCurrentDetails(jsonData);

                            final CurrentWeather displayWeather=new CurrentWeather(
                                    currentWeather.getLocationLabel(),
                                    currentWeather.getIcon(),
                                    currentWeather.getTemperature(),
                                    currentWeather.getTime(),
                                    currentWeather.getHumidity(),
                                    currentWeather.getPrecipChance(),
                                    currentWeather.getSummary(),
                                    currentWeather.getTimeZone()
                            );
                            binding.setWeather(displayWeather);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Drawable drawable=getResources().getDrawable(displayWeather.getIconID());
                                    iconImageView.setImageDrawable(drawable);
                                }
                            });

                        } else {
                            alertUserAboutError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "IO Exception caught", e);
                    }catch (JSONException e){
                        Log.e(TAG, "JSON Exception caught", e);
                    }
                }
            });
        }
    }

    private CurrentWeather getCurrentDetails(String jsonData) throws JSONException {
        JSONObject forecast=new JSONObject(jsonData);

        String timezone=forecast.getString("timezone");
        Log.i(TAG,"From JSON: "+timezone);

        JSONObject currently=forecast.getJSONObject("currently");

        CurrentWeather currentWeather =new CurrentWeather();

        currentWeather.setHumidity(currently.getDouble("humidity"));
        currentWeather.setTime(currently.getLong("time"));
        currentWeather.setIcon(currently.getString("icon"));
        currentWeather.setLocationLabel("Bharthiya, Mathura");
        currentWeather.setPrecipChance(currently.getDouble("precipProbability"));
        currentWeather.setSummary(currently.getString("summary"));
        currentWeather.setTemperature(currently.getDouble("temperature"));
        currentWeather.setTimeZone(timezone);

        Log.i(TAG,currentWeather.getFormattedTime());

        return currentWeather;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=manager.getActiveNetworkInfo();

        boolean isAvailable= false;
        if (networkInfo!=null && networkInfo.isConnected()){
            isAvailable=true;
        }else{
            Toast.makeText(this, "Sorry, the network is unavailable.", Toast.LENGTH_LONG).show();
        }
        return isAvailable;
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog =new AlertDialogFragment();
        dialog.show(getFragmentManager(),"error_dialog");
    }

    public void onRefreshActivity(View view) {
        getForcast(latitude,longitude);
        Toast.makeText(this, "getting data...", Toast.LENGTH_SHORT).show();
    }
}
