package application;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.jdbc.SQLError;

import db.DB;
import db.DBException;

public class Transactions {

	public static void main(String[] args) {
		
		Connection conn = null;
		Statement st = null;
		
		try {
			
			conn = DB.getConnection();
			st = conn.createStatement();
			
			conn.setAutoCommit(false); // Confirm the transaction after the two completed consultations
			
			int rows1 = st.executeUpdate("UPDATE seller SET BaseSalary = 2090 WHERE DepartmentId = 1");
			
			/*
			if (true) {
				throw new SQLException("Fake Error");
			}
			*/
					
			int rows2 = st.executeUpdate("UPDATE seller SET BaseSalary = 3090 WHERE DepartmentId = 2");
			
			System.out.println("Rows 1 = " + rows1);
			System.out.println("Rows 2 = " + rows2);
			
			conn.commit();
			
		} catch (SQLException e) {
			try {
				conn.rollback(); // Returns to the initial state of the BD
				throw new DBException("Transaction rolled back! Caused By: " + e.getMessage());
			} catch (SQLException e1) {
				throw new DBException("Error trying to rollback! Caused By: " + e.getMessage());
			}
			
		} finally {
			DB.closeStatement(st);
			DB.closeConnection();
		}

	}

}
