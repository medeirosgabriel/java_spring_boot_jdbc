package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.mysql.jdbc.Statement;

import db.DB;
import db.DBException;

public class InsertData {

	public static void main(String[] args) {
		Connection conn = null;
		PreparedStatement st = null;
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			conn = DB.getConnection();
			// ID -> AUTO-INCREMENT
			st = conn.prepareStatement("INSERT INTO seller (Name, Email, BirthDate, BaseSalary, DepartmentId)"
										+ "VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			
			// Replace the ?
			st.setString(1, "Carl Purple");
			st.setString(2, "carl@gmail.com");
			st.setDate(3, new java.sql.Date(sdf.parse("22/04/1985").getTime()));
			st.setDouble(4, 3000.0);
			st.setInt(5, 4);
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rsKeys = st.getGeneratedKeys();
				while (rsKeys.next()) { // We can insert more than one seller
					int id = rsKeys.getInt(1); // Just one column containing the ids
					System.out.println("Done! Id: " + id);
				} 
			} else {
				System.out.println("No Rows Affected");
			}
			
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} catch (ParseException e) { 
			e.printStackTrace();
		} finally {
			DB.closeStatement(st);
			DB.closeConnection();
		}
	}

}
