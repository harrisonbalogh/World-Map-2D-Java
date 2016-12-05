package data_model;

import data_model.entities.DataPoint;
import data_model.entities.Study;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.util.Date;
/**
 * Created by Joe on 12/4/2016.
 */

public class UploadController extends HttpServlet {
    // database connection settings
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/StudyDB/studydb";
    private static final String USER = "root";
    private static final String PASS = "nihon";
    private String sql;
    private Connection conn = null;
    private Statement stmt = null;
    // represents connection to database
    public boolean isConnected = false;

    public UploadController() {
        try {
            Class.forName(JDBC_DRIVER);                                // connects to jdbc driver
            conn = DriverManager.getConnection(DB_URL, USER, PASS);    // creates connection to the database

            if (conn != null)
                isConnected = true;
            stmt = conn.createStatement();
            //System.out.println("Connection Successful\n");		// prints statement if connection is successful
        } catch (SQLException sqle) {
            //Handle errors for JDBC
            System.out.println("ERROR: " + sqle);
        } catch (Exception e) {
            // handles exception for Class.forName
            System.out.println("ERROR: " + e);
        }
    }


    public void closeConnection() {
        try {
            if (stmt != null)
                stmt.close();
        } catch (SQLException sqle1) {
        }// nothing we can do
        try {
            if (conn != null)
                conn.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            isConnected = false;
        }
    }

    public void uploadStudy(Study study) {
        if (isConnected) {
            try {
                String title = study.getTitle();
                String description = null;

                if (description == null) {
                    description = "  ";
                }
                sql = "INSERT INTO Study (studyTitle, studyDescription) VALUES('" + title + "', '" + description + "')"
                        + " ON DUPLICATE KEY UPDATE studyTitle = studyTitle";
                stmt.execute(sql);

                // Adds all authors who wrote the study

                String[] authors = new String[study.getAuthors().size()];
                int v = 0;
                for (String a: study.getAuthors()){
                    authors[v] = a;
                    v++;
                }
                for (int i = 0; i < authors.length; i++) {

                    if (authors[i] != null) {
                        // Code for adding new author to database attached to a study
                        sql = "INSERT INTO Author (authorName) VALUES('" + authors[i] + "') "
                                + "ON DUPLICATE KEY UPDATE authorName = authorName";
                        stmt.execute(sql);
                        sql = " INSERT INTO StudyAuthor(authorName, studyTitle) VALUES('" + authors[i] + "', '" + title + "') "
                                + "ON DUPLICATE KEY UPDATE authorName = authorName";
                        stmt.execute(sql);
                    }
                }

                DataPoint[] dataPoints = new DataPoint[study.getDataPoints().size()];
                v = 0;
                for(DataPoint dp : study.getDataPoints()){
                    dataPoints[v] = dp;
                    v++;
                }
                if (dataPoints != null) {
                    for (int i = 0; i < dataPoints.length; i++) {
                        double measurement = dataPoints[i].getValue();
                        String unit = dataPoints[i].getUnit();
                        String date = dataPoints[i].getDateTime();
                        double latitude = dataPoints[i].getLatitude();
                        double longitude = dataPoints[i].getLongitude();
                        double altitude = dataPoints[i].getAltitude();
                        //String	warning		=	dataPoints[i].getWarning();

                        // Code for adding a new data points to the database attached to the study
                        sql = "INSERT INTO DataPoint (studyTitle, dpMeasurment, dpUnit, dpDate, dpLatitude, dpLongitude, dpAltitude) "
                                + "VALUES('" + title + "'," + measurement + ", '" + unit + "', STR_TO_DATE('" + date + "', '%Y,%m,%d')," + latitude + ", " + longitude + "," + altitude + ")";
                        stmt.execute(sql);
                    }
                }
            } catch (SQLException sqle) {
                //Handle errors for JDBC
                System.out.println("ERROR: " + sqle);
            } catch (Exception e) {
                // handles exception for
                System.out.println("ERROR: " + e);
            }
        }
    }

    public Study[] getAllStudies() {
        String _sql = "Study";
        return executeQuery(_sql);
    }


    public Study[] getStudiesByTitle(String _title) {
        String _sql = "Study WHERE studyTitle = '" + _title + "'";    //
        return executeQuery(_sql);
    }


    public Study[] getStudiesByAuthor(String _author) {
        String _sql = "StudyAuthor WHERE authorName = '" + _author + "'";
        return executeQuery(_sql);
    }


    public Study[] getStudyByMeasurement(String _unit) {
        String _sql = "FROM DataPoint WHERE dpUnit = '" + _unit + "'";
        return executeQuery(_sql);
    }


    public Study[] getStudiesByDateRange(Date beginDate, Date endDate) {

        String _sql = "FIGURE QUERY OUT!!!!!";

        return executeQuery(_sql);
    }


    public Study[] getStudiesByLocation(double _latitude, double _longitude) {

        String _sql = "FIGURE QUERY OUT!!!!!";

        return executeQuery(_sql);
    }


    public Study[] executeQuery(String _sql) {
        Study[] temp = null;
        if (isConnected) {
            try {

                sql = "SELECT COUNT(DISTINCT studyTitle) FROM " + _sql;        // get number of rows in table
                ResultSet count = stmt.executeQuery(sql);
                if (count.next()) {
                    temp = new Study[count.getInt(1)];
                }
                if (temp != null) {

                    sql = "SELECT * FROM " + _sql;                // creates statement to be executed in query
                    ResultSet data = stmt.executeQuery(sql);

                    for (int i = 0; i < temp.length; i++) {        // Adds all data from every song into song array
                        if (data.next()) {

                            String studyTitle = data.getString("studyTitle");
                            String[] Authors = null;
                            DataPoint[] DataPoints = null;


                            sql = "SELECT COUNT(authorName) FROM StudyAuthor "
                                    + "WHERE studyTitle = '" + studyTitle + "'";
                            ResultSet x = stmt.executeQuery(sql);
                            if (x.next()) {
                                Authors = new String[x.getInt(1)];

                            }

                            sql = "SELECT authorName FROM StudyAuthor "
                                    + "WHERE studyTitle = '" + studyTitle + "'";
                            ResultSet authors = stmt.executeQuery(sql);

                            for (int j = 0; j < Authors.length; j++) {
                                if (authors.next()) {
                                    Authors[j] = authors.getString("authorName");
                                }
                            }

                            sql = "SELECT COUNT(*) FROM DataPoint "
                                    + "WHERE studyTitle = '" + studyTitle + "'";
                            ResultSet y = stmt.executeQuery(sql);

                            if (y.next()) {
                                DataPoints = new DataPoint[y.getInt(1)];
                            }

                            sql = "SELECT * FROM DataPoint "
                                    + "WHERE studyTitle = '" + studyTitle + "'";
                            ResultSet dataPoints = stmt.executeQuery(sql);

                            for(int k = 0; k < DataPoints.length; k++){
                                if(dataPoints.next()){
                                    System.out.println(dataPoints.getDate("dpDate"));
                                    DataPoints[k] = new DataPoint(studyTitle,
                                            dataPoints.getInt("dpMeasurment"),
                                            dataPoints.getString("dpUnit"),
                                            dataPoints.getDouble("dpLatitude"),
                                            dataPoints.getDouble("dpLongitude"),
                                            dataPoints.getDouble("dpAltitude"),
                                            dataPoints.getDate("dpDate").toString());

                                }
                            }



                            temp[i] = new Study(studyTitle, Authors);
                            temp[i].setDataPoints(DataPoints);
                        }
                    }
                }
            } catch (SQLException e) {
                //handles SQL Exception
                System.out.println(e);
                //e.printStackTrace();
            }
        }
        return temp;
    }
}