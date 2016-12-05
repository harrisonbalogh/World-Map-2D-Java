package data_model;

import java.util.ArrayList;

import data_model.entities.DataPoint;

/**
 * Created by Joe on 11/13/2016.
 */
public class DataPointController {
    DataPoint dataPoint;
    String[] values;
    public DataPointController(String title, String unit, double value, double latitude, double longitude, double altitude,
                            String dateTime){
        dataPoint = new DataPoint(title, value, unit, latitude, longitude, altitude, dateTime);
    }


}
