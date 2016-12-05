package user_interface.data_components;

import data_model.SearchController;
import data_model.StudyController;
import data_model.entities.DataPoint;
import data_model.entities.Study;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Joe on 12/4/2016.
 */
public class SearchBoundary extends JFrame{
    public static final int WINDOW_WIDTH = 712;
    public static final int WINDOW_HEIGHT = 428;
    JPanel header = new JPanel();
    JScrollPane body;
    JPanel scrollingBody = new JPanel();
    GridLayout headerLayout = new GridLayout(1, 3);
    JLabel searchLabel;

    JTextField search;

    GridLayout contentLayout = new GridLayout(2, 1);

    GridLayout bodyLayout = new GridLayout(0,1);
    JButton searchButton = new JButton("Search");
    ArrayList<Study> studies = new ArrayList<Study>();
    SearchController searchController;
    private void initHeader(){
        searchLabel= new JLabel("Search: ");

        search = new JTextField();
        header.setLayout(headerLayout);
        header.add(searchLabel);
        header.add(search);
        searchButton = new JButton("Search");
        header.add(searchButton);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchController = new SearchController();
                studies = searchController.searchStudies(search.getText());
                populateStudies();

            }
        });

        header.add(searchButton);

    }
    private void initBody(){
        scrollingBody.setLayout(bodyLayout);
        body = new JScrollPane(scrollingBody);
        body.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    }
    public void populateStudies(){
        for(Study s: studies){
            JPanel jp = new JPanel();
            jp.setLayout(new GridLayout(2,2));
            jp.add(new JLabel("Title of Study: "));
            jp.add(new JLabel(s.getTitle()));
            jp.add(new JLabel("Authors: "));
            String authors = "";
            for(String a: s.getAuthors()){
                authors = authors + a + ", ";
            }
            jp.add(new JLabel(authors));
            System.out.println("Added Study");
            jp.setBorder(BorderFactory.createLineBorder(Color.red));
            jp.setVisible(true);
            scrollingBody.add(jp);

        }
        scrollingBody.revalidate();
        scrollingBody.repaint();

        body.validate();
        body.repaint();
    }
    public void initContent(){
        getContentPane().setBackground(Color.WHITE);
        setTitle("Search");
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
    public SearchBoundary(){
        initContent();

    }
}
