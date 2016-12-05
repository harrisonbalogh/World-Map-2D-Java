package data_model;


import javax.swing.*;

import data_model.entities.DataPoint;
import data_model.entities.Study;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Joe on 11/13/2016.
 */
public class StudyController {
    Study study;
    //opening a local study
    public StudyController(String study){

    }
    //making a new study
    public StudyController(){
        study = new Study();
    }
    public void addDataPointToStudy(DataPoint dataPoint){
        study.addDataPoint(dataPoint);
    }
    public void addAuthorToStudy(String author){
        study.addAuthor(author);
    }
    public void setTitleToStudy(String title){
        study.setTitle(title);
    }
    public void saveStudy(){
        try {
            PrintWriter pw = new PrintWriter("studies/" + study.getTitle() + ".txt", "UTF-8");
            pw.write("STUDYTITLE:START\r\n");
            pw.write(study.getTitle());
            pw.write("\r\n");
            pw.write("STUDYTITLE:END\r\n");
            pw.write("AUTHORS:START\r\n");
            for(String author: study.getAuthors()){
                pw.write(author);
                pw.write("\r\n");
            }
            pw.write("AUTHORS:END\r\n");
            pw.write("DATAPOINTS:START\r\n");

            int dpNum = 1;
            try {
                for (DataPoint d : study.getDataPoints()) {
                    pw.write("DATAPOINT:"+dpNum+":START\r\n");
                    pw.write("TITLE:START\r\n");
                    pw.write(d.getTitle());
                    pw.write("\r\n");
                    pw.write("TITLE:END\r\n");
                    pw.write("VALUE:START\r\n");
                    pw.write(Double.toString(d.getValue()));
                    pw.write("\r\n");
                    pw.write("VALUE:END\r\n");
                    pw.write("UNIT:START\r\n");
                    pw.write(d.getUnit());
                    pw.write("\r\n");
                    pw.write("UNIT:END\r\n");
                    pw.write("LATITUDE:START\r\n");
                    pw.write(Double.toString(d.getLatitude()));
                    pw.write("\r\n");
                    pw.write("LATITUDE:END\r\n");
                    pw.write("LONGITUDE:START\r\n");
                    pw.write(Double.toString(d.getLongitude()));
                    pw.write("\r\n");
                    pw.write("LONGITUDE:END\r\n");
                    pw.write("ALTITUDE:START\r\n");
                    pw.write(Double.toString(d.getAltitude()));
                    pw.write("\r\n");
                    pw.write("ALTITUDE:END\r\n");
                    pw.write("DATETIME:START\r\n");
                    pw.write(d.getDateTime());
                    pw.write("\r\n");
                    pw.write("DATETIME:END\r\n");
                    pw.write("DATAPOINT:"+dpNum+":END\r\n");
                    dpNum++;
                }
            } catch(NumberFormatException e){
                JFrame f = new JFrame("FILL OUT YOUR FIELDS");
                JOptionPane.showMessageDialog(f,"FILL OUT YOUR NUMERICAL FIELDS");
            }
            pw.write("DATAPOINTS:END");
            pw.close();
        } catch(IOException e){
            System.out.println("Something went wrong");
        }
    }

}
