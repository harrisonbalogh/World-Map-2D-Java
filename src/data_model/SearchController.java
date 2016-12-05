package data_model;

import data_model.entities.DataPoint;
import data_model.entities.Study;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created by Joe on 12/4/2016.
 */
public class SearchController {
    File folder;
    File[] files;
    Path currentRelativePath = Paths.get("");
    String s = currentRelativePath.toAbsolutePath().toString();
    public SearchController(){
        folder = new File(s);
        files = folder.listFiles();
    }
    private boolean isRelevant(String line, String searchQuery){
        if (line.contains(searchQuery)){
            return true;
        }
        return false;
    }
    public ArrayList<Study> searchStudies(String searchQuery){
        ArrayList<Study> relevantStudies = new ArrayList<>();
        for(File f: files){
            try{
                BufferedReader br = new BufferedReader(new FileReader(f.getName()));
                String line;
                boolean relevant = false;
                Study study = new Study();
                line = br.readLine();
                if (line.equals("STUDYTITLE:START")) {
                    while (!(line = br.readLine()).equals("STUDYTITLE:END")){
                        relevant = isRelevant(line, searchQuery);
                        study.setTitle(line);
                    }
                    line = br.readLine();
                    if(line.equals("AUTHORS:START")){
                        while (!(line = br.readLine()).equals("AUTHORS:END")){
                            relevant = isRelevant(line, searchQuery);
                            study.addAuthor(line);
                        }
                    }
                    line = br.readLine();
                    if(line.equals("DATAPOINTS:START")){
                        int dpNum = 0;
                        while (!(line = br.readLine()).equals("DATAPOINTS:END")){
                            String[] tokens = line.split(":");
                            if (tokens.length == 3 && tokens[2].equals("START")){
                                dpNum = Integer.parseInt(tokens[1]) -1;
                                study.addDataPoint(new DataPoint());
                            }
                            else if(line.equals("TITLE:START")){
                                line = br.readLine();
                                relevant = isRelevant(line,searchQuery);
                                DataPoint temp = study.getDataPoints().get(dpNum);
                                temp.setTitle(line);
                                study.changeDataPoint(dpNum, temp);
                                br.readLine();
                            }
                            else if(line.equals("VALUE:START")){
                                line = br.readLine();
                                relevant = isRelevant(line,searchQuery);
                                DataPoint temp = study.getDataPoints().get(dpNum);
                                temp.setValue(Double.parseDouble(line));
                                study.changeDataPoint(dpNum, temp);
                                br.readLine();
                            }
                            else if(line.equals("UNIT:START")){
                                line = br.readLine();
                                relevant =isRelevant(line, searchQuery);
                                DataPoint temp = study.getDataPoints().get(dpNum);
                                temp.setUnit(line);
                                study.changeDataPoint(dpNum, temp);
                                br.readLine();
                            }
                            else if(line.equals("LATITUDE:START")){
                                line = br.readLine();
                                relevant = isRelevant(line, searchQuery);
                                DataPoint temp = study.getDataPoints().get(dpNum);
                                temp.setLatitude(Double.parseDouble(line));
                                study.changeDataPoint(dpNum, temp);
                                br.readLine();
                            }
                            else if(line.equals("LONGITUDE:START")){
                                line = br.readLine();
                                relevant = isRelevant(line, searchQuery);
                                DataPoint temp = study.getDataPoints().get(dpNum);
                                temp.setLongitude(Double.parseDouble(line));
                                study.changeDataPoint(dpNum, temp);
                                br.readLine();
                            }
                            else if(line.equals("ALTITUDE:START")){
                                line = br.readLine();
                                relevant = isRelevant(line, searchQuery);
                                DataPoint temp = study.getDataPoints().get(dpNum);
                                temp.setAltitude(Double.parseDouble(line));
                                study.changeDataPoint(dpNum, temp);
                                br.readLine();
                            }
                            else if(line.equals("DATETIME:START")){
                                line = br.readLine();
                                relevant = isRelevant(line, searchQuery);
                                DataPoint temp = study.getDataPoints().get(dpNum);
                                temp.setDateTime(line);
                                study.changeDataPoint(dpNum, temp);
                                br.readLine();
                            }
                        }
                    }
                }
                if(relevant){
                    relevantStudies.add(study);
                }
            } catch (IOException e){
                System.out.println("SOMETHING WENT WRONG");
            }
        }
        return relevantStudies;
    }
}
