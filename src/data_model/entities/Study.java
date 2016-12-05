package data_model.entities;

import java.util.ArrayList;

/**
 * Created by Joe on 11/13/2016.
 */
public class Study {
    String title;
    ArrayList<String> authors;
    ArrayList<DataPoint> dataPoints;
    public Study(){
        authors = new ArrayList<String>();
        dataPoints = new ArrayList<DataPoint>();
    }
    public Study(String title, ArrayList<String> authors, ArrayList<DataPoint> dataPoints){
        this.title = title;
        this.authors = authors;
        this.dataPoints = dataPoints;
    }
    public Study(String studyTitle, String[] authors){
        title = studyTitle;
        for(String a: authors){
            this.authors.add(a);
        }
    }
    public String getTitle() {
        return title;
    }

    public ArrayList<String> getAuthors() {
        return authors;
    }

    public ArrayList<DataPoint> getDataPoints() {
        return dataPoints;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthors(ArrayList<String> authors) {
        this.authors = authors;
    }

    public void setDataPoints(ArrayList<DataPoint> dataPoints) {
        this.dataPoints = dataPoints;
    }
    public void setDataPoints(DataPoint [] dataPoints){
        for(DataPoint d: dataPoints){
            this.dataPoints.add(d);
        }
    }
    public void addDataPoint(DataPoint dp){
        dataPoints.add(dp);
    }
    public void addAuthor(String author){
        authors.add(author);
    }
    public void removeAuthor(String author){
        authors.remove(author);
    }
    public void removeDataPoint(DataPoint dp){
        dataPoints.remove(dp);
    }
    public void changeDataPoint(int index, DataPoint dp){
        dataPoints.set(index, dp);
    }
}
