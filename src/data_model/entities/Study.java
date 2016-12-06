package data_model.entities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
        this.authors = new ArrayList<>();
        for(String a: authors){
            this.authors.add(a);
        }
        this.dataPoints = new ArrayList<>();

    }
    public Study(File file){
        this.authors = new ArrayList<>();
        this.dataPoints = new ArrayList<>();
        try{
            String fileName = file.getAbsolutePath();
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            line = br.readLine();
            if (line.equals("STUDYTITLE:START")) {
                while (!(line = br.readLine()).equals("STUDYTITLE:END")){
                    setTitle(line);
                }
                line = br.readLine();
                if(line.equals("AUTHORS:START")){
                    while (!(line = br.readLine()).equals("AUTHORS:END")){
                        addAuthor(line);
                    }
                }
                line = br.readLine();
                if(line.equals("DATAPOINTS:START")){
                    int dpNum = 0;
                    while (!(line = br.readLine()).equals("DATAPOINTS:END")){
                        String[] tokens = line.split(":");
                        if (tokens.length == 3 && tokens[2].equals("START")){
                            dpNum = Integer.parseInt(tokens[1]) -1;
                            addDataPoint(new DataPoint());
                        }
                        else if(line.equals("TITLE:START")){
                            line = br.readLine();

                            DataPoint temp = getDataPoints().get(dpNum);
                            temp.setTitle(line);
                            changeDataPoint(dpNum, temp);
                            br.readLine();
                        }
                        else if(line.equals("VALUE:START")){
                            line = br.readLine();
                            DataPoint temp = getDataPoints().get(dpNum);
                            temp.setValue(Double.parseDouble(line));
                            changeDataPoint(dpNum, temp);
                            br.readLine();
                        }
                        else if(line.equals("UNIT:START")){
                            line = br.readLine();

                            DataPoint temp = getDataPoints().get(dpNum);
                            temp.setUnit(line);
                            changeDataPoint(dpNum, temp);
                            br.readLine();
                        }
                        else if(line.equals("LATITUDE:START")){
                            line = br.readLine();

                            DataPoint temp = getDataPoints().get(dpNum);
                            temp.setLatitude(Double.parseDouble(line));
                            changeDataPoint(dpNum, temp);
                            br.readLine();
                        }
                        else if(line.equals("LONGITUDE:START")){
                            line = br.readLine();

                            DataPoint temp = getDataPoints().get(dpNum);
                            temp.setLongitude(Double.parseDouble(line));
                            changeDataPoint(dpNum, temp);
                            br.readLine();
                        }
                        else if(line.equals("ALTITUDE:START")){
                            line = br.readLine();
                            DataPoint temp = getDataPoints().get(dpNum);
                            temp.setAltitude(Double.parseDouble(line));
                            changeDataPoint(dpNum, temp);
                            br.readLine();
                        }
                        else if(line.equals("DATETIME:START")){
                            line = br.readLine();

                            DataPoint temp = getDataPoints().get(dpNum);
                            temp.setDateTime(line);
                            changeDataPoint(dpNum, temp);
                            br.readLine();
                        }
                    }
                }

            }

        } catch (IOException e){
            System.out.println("SOMETHING WENT WRONG");
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
