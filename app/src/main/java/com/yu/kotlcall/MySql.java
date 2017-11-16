/*
package com.yu.kotlcall;
*/

/**
 * Created by Yu on 2016/11/11.
 */
import android.location.Location;
/*
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;

public class MySql {
    Connection conn;//connection to database
    String option;
    public MySql(){
        // build a default connection to database
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/test";
        String user = "root";
        String password = "123wq";
        try {
            Class.forName(driver);
            //connect to database
            conn = DriverManager.getConnection(url, user, password);
            if(!conn.isClosed())
                System.out.println("Succeeded connecting to the Database!");
        } catch (SQLException e) {
            System.out.println("MySQL Error");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Connect(String url, String user, String password){
        //build a connection to database according to parameters
        String driver = "com.mysql.jdbc.Driver";
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, password);
            if(!conn.isClosed())
                System.out.println("Succeeded connecting to the Database!");
        } catch (SQLException e) {
            System.out.println("MySQL Error");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResultSet SelectData(String sql){
        //Select data from database, and return the result set
        ResultSet result = null;
        try {
            Statement stmt = conn.createStatement();
            //sql = "select * from students;";
            System.out.println("SQL: "+sql);
            result = stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println("MySQL Select Error");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public int UpdateData(String sql){
        //update database including create, delete, update, return rows affected
        int result = -1;
        try {
            Statement stmt = conn.createStatement();
            //sql = "select * from students;";
            System.out.println("SQL: "+sql);
            result = stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("MySQL Update Error");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void Close() throws SQLException{
        //Close the connection to database
        conn.close();
}
    public static void main(String[] args) throws Exception {
        MySql test = new MySql();
        test.Close();
        String url = "jdbc:mysql://localhost:3306/test";
        String user = "root";
        String password = "123wq";
        test.Connect(url, user, password);
        ResultSet result = test.SelectData("select * from students");
        while (result.next()) {
            System.out.println(result.getInt(1) + "\t"
                    + result.getString(2)+"\t"
                    + result.getString(3)+"\t"
                    + result.getInt(4));//
        }

    }
}*/

