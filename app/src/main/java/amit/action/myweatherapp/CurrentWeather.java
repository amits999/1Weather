package amit.action.myweatherapp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class CurrentWeather {
    private String locationLabel;
    private String icon;
    private double temperature;
    private long time;
    private double humidity;
    private double precipChance;
    private String summary;
    private String timeZone;

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public CurrentWeather() {
    }

    public CurrentWeather(String locationLabel, String icon, double temperature, long time, double humidity, double precipChance, String summary, String timeZone) {
        this.locationLabel = locationLabel;
        this.icon = icon;
        this.temperature = temperature;
        this.time = time;
        this.humidity = humidity;
        this.precipChance = precipChance;
        this.summary = summary;
        this.timeZone = timeZone;
    }

    public String getLocationLabel() {
        return locationLabel;
    }

    public void setLocationLabel(String locationLabel) {
        this.locationLabel = locationLabel;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getIconID(){
        int iconId=R.drawable.clear_day;

        switch(icon){
            case "clear-day":
                iconId=R.drawable.clear_day;
                break;
            case "clear-night":
                iconId=R.drawable.clear_night;
                break;
            case "rain":
                iconId=R.drawable.rain;
                break;
            case "snow":
                iconId=R.drawable.snow;
                break;
            case "sleet":
                iconId=R.drawable.sleet;
                break;
            case "wind":
                iconId=R.drawable.wind;
                break;
            case "fog":
                iconId=R.drawable.fog;
                break;
            case "cloudy":
                iconId=R.drawable.cloudy;
                break;
            case "partly-cloudy-day":
                iconId=R.drawable.partly_cloudy;
                break;
            case "partly-cloudy-night":
                iconId=R.drawable.cloudy_night;
                break;
        }
        return iconId;

    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public long getTime() {
        return time;
    }

    public String getFormattedTime(){
        SimpleDateFormat formatter =new SimpleDateFormat("h:mm a");
        formatter.setTimeZone(TimeZone.getTimeZone(timeZone));

        Date dateTime=new Date(time * 100);
        return formatter.format(dateTime);
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getPrecipChance() {
        return precipChance;
    }

    public void setPrecipChance(double precipChance) {
        this.precipChance = precipChance;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
