package data_model.entities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Joe on 11/13/2016.
 */
public class DataPoint {
    String title;
    String unit;
    double value;
    double latitude;
    double longitude;
    double altitude;
    String dateTime;
    final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date;
    public DataPoint(){}
    public DataPoint(String title, double value, String unit, double latitude, double longitude, double altitude,
                     String dateTime){
        this.title = title;
        this.unit = unit;
        this.value = value;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.dateTime = dateTime;
        setDate();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
        setDate();
    }

    public void setDate(){
        try
        {
            date = sdf.parse(dateTime);
        }catch(ParseException e){
            System.out.println("Date invalid");
        }
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }
}
