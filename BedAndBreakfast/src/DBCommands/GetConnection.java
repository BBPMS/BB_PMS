/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBCommands;

import java.util.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import static javax.swing.JOptionPane.showMessageDialog;

/**
 * GetConnection.java
 * @author Kevin
 * @modification Prasanna
 * creates a DB Connection for the specific database in the String 
 * URL.  This connection string must be manually changed as of right now
 */
public class GetConnection{
    private Connection conn;
//    private Properties connectionProps;
    private String username;
    private String password;
    private String schema;
    private String server;
    private int port;
    private String URL;
    private Statement stmt;
    private ResultSet rs;
    
    
    //constructors
    public GetConnection(String username, String password, String schema, String server, int port) {
        this.username = username;
        this.password = password;
        this.schema = schema;
        this.server = server;
        this.port = port;
//        connectionProps = new Properties();
//        connectionProps.put("user", username);
//        connectionProps.put("password", password);
    }
    //constructor to call kevins db connection
    public GetConnection(int x){
        if(x == 5) {
            this.username = "system";
            this.password = "fail1982";
            this.schema = "xe";
            this.server = "localhost";
            this.port = 1521;
        }
        //you can add an if interger statement for yours
    }
    //end constructors
    
    //getters
    public Connection getConn() {
        return this.conn;
    }
    
    public ResultSet getRS() {
        return this.rs;
    }
    
    public Statement getStmt() {
        return this.stmt;
    }
    //end getters
    
    //setters
    public void setRS(ResultSet rs) {
        this.rs = rs;
    }
    
    public void setStmt(Statement stmt) {
        this.stmt = stmt;
    }
    //end setters
    
    
    //method getDBConnection(), 
    public void getDBConnection(){
        //this string is broke into these "jdbc:oracle:thin:username:passowrd@location:port:databasename
        //jdbc:oracle:thin:@server:port:schema
        URL = "jdbc:oracle:thin:" + "@" + server + ":" + port +
                ":" + schema;
        //System.out.println(URL);
        try {
            this.conn = DriverManager.getConnection(URL, username, password);
            System.out.println("DB Connection Made");
        } catch(SQLException ex) {
            //System.out.println(ex.getMessage());
            showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println("did not connect to DB");
        }
    }
    
    //method validateUser()
    public Boolean validateUser(String username, String password) {
        
        Boolean access = false;
        try {
            String sql = "SELECT * FROM employees WHERE user_name='" + username + 
                "' and password='" + password +"'";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if(rs.next()){
                access = true;
                rs.close();
            } else 
                access = false;
                rs.close();
        } catch(SQLException ex) {
            System.out.println(ex);
        }
        return access;        
    }
    
    
    //method getResults(), Prasana returning DB info to ArrayList
    public ArrayList getresults(String query, int column_size){
        ArrayList<String> records = new ArrayList<String>();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()){
                for (int i = 1; i < column_size; i++){
                    records.add(rs.getString(i));
                }                
            }
            rs.close();
        } catch (SQLException ex) {
            showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (stmt!=null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                }
            }
        }
        return records;
    }//getResults()
    
    public ArrayList getresults(String query){
        ArrayList <ArrayList<String>> records = new ArrayList<ArrayList<String>>();
        ArrayList <String>record = new ArrayList<String>();
        try (Statement stmt = conn.createStatement()){
            rs = stmt.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            int column_size = rsmd.getColumnCount();
            System.out.println(column_size);
            while (rs.next()){
                for (int i = 1; i <= column_size; i++){
                    record.add(rs.getString(i));
                }
                records.add(record);
                record = new ArrayList<String>();
            }
            rs.close();
        } catch (SQLException ex) {
            showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } 
        return records;
    }//getResults()
    
}
