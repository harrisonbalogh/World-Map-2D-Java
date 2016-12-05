package user_interface.data_components;

import javax.swing.*;

import data_model.DataPointController;

import java.awt.*;

/**
 * Created by Joe on 11/10/2016.
 */
public class DataPointBoundary extends JPanel {
    public JTextField title;
    public JTextField unit;
    public JTextField value;
    public JTextField latitude;
    public JTextField longitude;
    public JTextField altitude;
    public JTextField dateTime;
    JLabel titleLabel;
    JLabel unitLabel;
    JLabel valueLabel;
    JLabel latitudeLabel;
    JLabel longitudeLabel;
    JLabel altitudeLabel;
    JLabel dateTimeLabel;
    GridLayout layout = new GridLayout(7, 2);
    DataPointController dataPointControl;
    public DataPointBoundary(){
        init();

    }
    private void init(){
        title = new JTextField();
        unit = new JTextField();
        value = new JTextField();
        latitude = new JTextField();
        longitude = new JTextField();
        altitude = new JTextField();
        dateTime = new JTextField();
        titleLabel = new JLabel();
        unitLabel = new JLabel();
        valueLabel = new JLabel();
        latitudeLabel = new JLabel();
        longitudeLabel = new JLabel();
        altitudeLabel = new JLabel();
        dateTimeLabel = new JLabel();
        titleLabel.setText("Title of measurement:");
        valueLabel.setText("Value:");
        unitLabel.setText("Unit:");
        latitudeLabel.setText("Latitude in degrees:");
        longitudeLabel.setText("Longitude in degrees:");
        altitudeLabel.setText("Altitude/Depth in meters:");
        dateTimeLabel.setText("Date and time in UTC:");
        unit.setToolTipText("Whatever unit you measured in...");
        altitude.setToolTipText("Positive value for altitude negative value for depth");
        setLayout(layout);
        add(titleLabel);
        add(title);
        add(valueLabel);
        add(value);
        add(unitLabel);
        add(unit);
        add(latitudeLabel);
        add(latitude);
        add(longitudeLabel);
        add(longitude);
        add(altitudeLabel);
        add(altitude);
        add(dateTimeLabel);
        add(dateTime);
    }
    public String[] retrieveValues(){
        String[] values = new String[7];
        values[0] = title.getText();
        values[1] = value.getText();
        values[2] = unit.getText();
        values[3] = latitude.getText();
        values[4] = longitude.getText();
        values[5] = altitude.getText();
        values[6] = dateTime.getText();
        return values;
    }
}
