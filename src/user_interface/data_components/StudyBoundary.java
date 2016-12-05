package user_interface.data_components;

import javax.swing.*;

import data_model.StudyController;
import data_model.entities.DataPoint;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Joe on 11/10/2016.
 */
public class StudyBoundary extends JFrame{
    public static final int WINDOW_WIDTH = 712;
    public static final int WINDOW_HEIGHT = 428;
    JPanel header = new JPanel();
    JScrollPane body;
    JPanel scrollingBody = new JPanel();
    GridLayout headerLayout = new GridLayout(3, 2);
    JLabel titleLable;
    JLabel authorLabel;
    JTextField title;
    JTextField authors;

    GridLayout contentLayout = new GridLayout(2, 1);
    StudyController studyControl;
    GridLayout bodyLayout = new GridLayout(0,1);
    JButton addButton = new JButton("Add Data Point");
    JButton saveButton = new JButton("Save Study");
    ArrayList<DataPointBoundary> dataPoints = new ArrayList<DataPointBoundary>();
    private void initHeader(){
        titleLable= new JLabel("Title: ");
        authorLabel = new JLabel("Authors: ");
        title = new JTextField();
        authors = new JTextField();
        authors.setToolTipText("Separate names with commas");
        header.setLayout(headerLayout);
        header.add(titleLable);
        header.add(title);
        header.add(authorLabel);
        header.add(authors);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                studyControl = new StudyController();
                studyControl.setTitleToStudy(title.getText());
                String authorNames = authors.getText();
                ArrayList<String> authorList = new ArrayList<String>(Arrays.asList(authorNames.split(",[ ]*")));
                for(String author: authorList){
                    studyControl.addAuthorToStudy(author);
                }
                for(DataPointBoundary d:dataPoints) {
                    String[] dpvalues = d.retrieveValues();
                    studyControl.addDataPointToStudy(new DataPoint(dpvalues[0],
                            Double.parseDouble(dpvalues[1]), dpvalues[2],
                            Double.parseDouble(dpvalues[3]), Double.parseDouble(dpvalues[4]),
                            Double.parseDouble(dpvalues[5]), dpvalues[6]));
                }
                studyControl.saveStudy();
            }
        });
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dataPoints.add(new DataPointBoundary());
                addDataPoint();
            }
        });
        header.add(addButton);
        header.add(saveButton);
    }
    private void initBody(){
        scrollingBody.setLayout(bodyLayout);
        body = new JScrollPane(scrollingBody);
        body.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    }
    public void addDataPoint(){
        for(DataPointBoundary dpb : dataPoints){
            System.out.println("Added Datapoint");
            dpb.setBorder(BorderFactory.createLineBorder(Color.red));
            dpb.setVisible(true);
            scrollingBody.add(dpb);
            //body.add(dpb);
        }
        scrollingBody.revalidate();
        scrollingBody.repaint();
        /*DataPointBoundary b = new DataPointBoundary();
        scrollingBody.add(b);*/
        body.validate();
        body.repaint();
    }
    public void initContent(){
        getContentPane().setBackground(Color.WHITE);
        setTitle("Study Editor");
        setBounds(100, 100, WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(contentLayout);
        setMinimumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        setResizable(false);
        initHeader();
        initBody();
        getContentPane().add(header);
        getContentPane().add(body);

    }
    public StudyBoundary(){
        initContent();
    }
    public StudyBoundary(String fileName){
        initContent();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e){
            System.out.println("FILE NOT FOUND");
        }
        String line;
        try {
            line = br.readLine();
            if (line.equals("STUDYTITLE:START")) {
                while (!(line = br.readLine()).equals("STUDYTITLE:END")){
                    title.setText(line);
                }
                line = br.readLine();
                if(line.equals("AUTHORS:START")){
                    while (!(line = br.readLine()).equals("AUTHORS:END")){
                        authors.setText(authors.getText()  + line +", ");
                    }
                }
                line = br.readLine();
                if(line.equals("DATAPOINTS:START")){
                    int dpNum = 0;
                    while (!(line = br.readLine()).equals("DATAPOINTS:END")){
                        String[] tokens = line.split(":");
                        if (tokens.length == 3 && tokens[2].equals("START")){
                            dpNum = Integer.parseInt(tokens[1]) -1;
                            dataPoints.add(new DataPointBoundary());
                            addDataPoint();
                        }
                        else if(line.equals("TITLE:START")){
                            line = br.readLine();
                            DataPointBoundary temp = dataPoints.get(dpNum);
                            temp.title.setText(line);
                            dataPoints.set(dpNum, temp);
                            br.readLine();
                        }
                        else if(line.equals("VALUE:START")){
                            line = br.readLine();
                            DataPointBoundary temp = dataPoints.get(dpNum);
                            temp.value.setText(line);
                            dataPoints.set(dpNum, temp);
                            br.readLine();
                        }
                        else if(line.equals("UNIT:START")){
                            line = br.readLine();
                            DataPointBoundary temp = dataPoints.get(dpNum);
                            temp.unit.setText(line);
                            dataPoints.set(dpNum, temp);
                            br.readLine();
                        }
                        else if(line.equals("LATITUDE:START")){
                            line = br.readLine();
                            DataPointBoundary temp = dataPoints.get(dpNum);
                            temp.latitude.setText(line);
                            dataPoints.set(dpNum, temp);
                            br.readLine();
                        }
                        else if(line.equals("LONGITUDE:START")){
                            line = br.readLine();
                            DataPointBoundary temp = dataPoints.get(dpNum);
                            temp.longitude.setText(line);
                            dataPoints.set(dpNum, temp);
                            br.readLine();
                        }
                        else if(line.equals("ALTITUDE:START")){
                            line = br.readLine();
                            DataPointBoundary temp = dataPoints.get(dpNum);
                            temp.altitude.setText(line);
                            dataPoints.set(dpNum, temp);
                            br.readLine();
                        }
                        else if(line.equals("DATETIME:START")){
                            line = br.readLine();
                            DataPointBoundary temp = dataPoints.get(dpNum);
                            temp.dateTime.setText(line);
                            dataPoints.set(dpNum, temp);
                            br.readLine();
                        }
                    }
                }
            }

        }catch (IOException e){
            System.out.println("SOMETHING WENT WRONG");
        }
    }
}
