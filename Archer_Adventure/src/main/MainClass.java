package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MainClass {
	private static final String URL = "jdbc:mysql://localhost/score"; 
    private static final String USERNAME = "root"; 
    private static final String PASSWORD = "";

	public static void main(String[] args) {
		
		try {         
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            
            if (conn != null) {
                System.out.println("Kết nối thành công đến database");
                
            }
        } catch (SQLException ex) {
            System.out.println("Lỗi kết nối đến database: " + ex.getMessage());
        }finally {
        	new Game();
		}
	}
	
}
