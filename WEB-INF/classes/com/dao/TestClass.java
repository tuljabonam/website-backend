package com.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//Tulja
//Meant for Testing the Database connection
public class TestClass {

	public static void main(String[] args) {
        String url_c = "jdbc:oracle:thin:@artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu";
        String uname = "stekula";
        String pass = "efoampug";
        
        
        try {
     	   Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection(url_c, uname, pass);
            PreparedStatement pstmt = con.prepareStatement("select * from Students where StudentID=?");
            
            pstmt.setString(1, "G01238290");
            ResultSet results = pstmt.executeQuery();
            System.out.println(results);
        
	        pstmt.close();
	        con.close();
	        System.out.println("Finished");
        }
        catch (Exception e) {
            e.printStackTrace();
            return;

	}

}
}
