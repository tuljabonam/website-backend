package Test;

import org.json.JSONException;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
//Rachana Thota
//Main file where the survey form is submitted
@Path("api")
public class Hello {
	
	  public double calcStd(String data)
	    {
		  double sum = 0.0, standardDeviation = 0.0;
		  double length = 10.0;
	
		  String[] tokens = data.split(",");
		    for (String token : tokens) {
			      Double num = Double.parseDouble(token);
			      sum += num;      
		    }

	        double mean = sum/length;
		    for (String token : tokens) {
			      Double num = Double.parseDouble(token);
			      standardDeviation += Math.pow(num - mean, 2);  
		    }

	        return Math.sqrt(standardDeviation/length);
	    }
	  
	  public float calcMean(String data)
	  {
	    String[] tokens = data.split(",");
	    float sum = 0.0F;
	    for(int i=0;i<tokens.length;i++)
	    {
	    	sum+=Integer.parseInt(tokens[i]);
	    }
	     return sum / tokens.length;
	  }

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getStudentData")
	public Students getStudentData(String sid) throws JSONException {

        final JSONObject obj = new JSONObject(sid);
        String data = obj.getString("data");
        data = data.replace('"', ' ');
        data = data.trim();

        String url_c = "jdbc:oracle:thin:@artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu";
        String uname = "stekula";
        String pass = "efoampug";
        try {
     	   Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection(url_c, uname, pass);
            PreparedStatement pstmt = con.prepareStatement("select * from Students where StudentID=?");
            pstmt.setString(1, data);
            ResultSet results = pstmt.executeQuery();
                        
            Students sb = new Students();
            while (results.next()) {
                sb.setFname(results.getString("FName"));
                sb.setLname(results.getString("LName"));
                sb.setStudentid(results.getString("StudentID"));
                sb.setEmail(results.getString("Email"));
                sb.setStreetaddress(results.getString("StreetAddr"));
                sb.setCity(results.getString("City"));
                sb.setCountry(results.getString("Country"));
                sb.setState(results.getString("States"));
                sb.setZipcode(results.getString("Zipcode"));
                sb.setData(results.getString("Dataval"));
                sb.setTelephone(results.getString("Telephone"));
                sb.setUrl(results.getString("Urls"));
                sb.setSurveydate(results.getString("SurveyDate"));
                sb.setLikemost(results.getString("Likemost"));
                sb.setInterestInUniversity(results.getString("InterestInUniv"));
                sb.setComments(results.getString("Comments"));
                sb.setMonth(results.getString("GradMonth"));
                sb.setGraduationYear(results.getString("GradYear"));
                sb.setRecommend(results.getString("Recommend"));
            }
	        pstmt.close();
	        con.close();
	        return sb;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;

	}
		
	}
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("putRecord")
	public String[] putRecord(String record1) throws JSONException {

        final JSONObject obj = new JSONObject(record1);
        String data = obj.getString("data");
        final JSONObject obj1 = new JSONObject(data);
        System.out.println("data1 :"+ obj1);
        System.out.println(obj1.getString("fname"));

        String url_c = "jdbc:oracle:thin:@artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu";
        String uname = "stekula";
        String pass = "efoampug";
        ArrayList<String> si = new ArrayList<String>();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection(url_c, uname, pass);
            PreparedStatement pstmt = con.prepareStatement("insert into Students(FName,LName,StudentID,Email,StreetAddr,City,Country,States,Zipcode,Dataval,Telephone,Urls,SurveyDate,Likemost,InterestInUniv,Comments,GradMonth,GradYear,Recommend) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");


            pstmt.setString(1, obj1.has("fname") ? obj1.getString("fname") : "" );
            pstmt.setString(2, obj1.has("lname") ?obj1.getString("lname") : "" );
            pstmt.setString(3, obj1.has("studentid")? obj1.getString("studentid") : "");
            pstmt.setString(4, obj1.has("email") ? obj1.getString("email") : "");
            pstmt.setString(5, obj1.has("streetaddress") ? obj1.getString("streetaddress") : "");
            pstmt.setString(6, obj1.has("city") ? obj1.getString("city") : "");
            pstmt.setString(7, obj1.has("country")? obj1.getString("country") : "");
            pstmt.setString(8, obj1.has("state")? obj1.getString("state") : "");
            pstmt.setString(9, obj1.has("zipcode")? String.valueOf(obj1.getInt("zipcode")) : "");
            pstmt.setString(10, obj1.has("data") ? obj1.getString("data") : "" );
            pstmt.setString(11, obj1.has("telephone")  ? obj1.getString("telephone") : "" );
            pstmt.setString(12, obj1.has("url") ? obj1.getString("url") : "");
            pstmt.setString(13, obj1.has("surveydate") ? obj1.getString("surveydate") : "");
            pstmt.setString(14, obj1.has("likemost")? obj1.getString("likemost") : "");
            pstmt.setString(15, obj1.has("interestInUniversity")? obj1.getString("interestInUniversity") : "");
            pstmt.setString(16, obj1.has("comments")? obj1.getString("comments") : "");
            pstmt.setString(17, obj1.has("month")? obj1.getString("month") : "");
            pstmt.setString(18, obj1.has("graduationYear")? obj1.getString("graduationYear") : "");
            pstmt.setString(19, obj1.has("recommend")? obj1.getString("recommend") : "");
            
            pstmt.executeUpdate();
            pstmt.close();
            con.close();

            float mean = calcMean(obj1.getString("data"));

    	    double StdDev = calcStd(obj1.getString("data"));

    	    String[] arr = new String[2];
    	    arr[0] = String.valueOf(mean);
    	    arr[1] = String.valueOf(StdDev);



            return arr;
        }
        catch (Exception e) {
        	System.out.println("Connection Error");
        	System.out.println(e);
        	
            e.printStackTrace();
            return null;
        }
	}
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getStudentsID")
	public List<String> getStudentsID() {
        String url_c = "jdbc:oracle:thin:@artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu";
        String uname = "stekula";
        String pass = "efoampug";
        ArrayList<String> si = new ArrayList<String>();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection(url_c, uname, pass);
            PreparedStatement pstmt = con.prepareStatement("select StudentID from Students");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                si.add(rs.getString("StudentID"));
            }
            pstmt.close();
            con.close();
            return si;
        }
        catch (Exception e) {
        	System.out.println("Connection Error");
        	System.out.println(e);
        	
            e.printStackTrace();
            return null;
        }
	}
	


}
 