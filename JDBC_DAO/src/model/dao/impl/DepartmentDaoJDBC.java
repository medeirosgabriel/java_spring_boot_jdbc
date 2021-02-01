package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;

import db.DB;
import db.DBException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {

	private Connection conn;

	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Department dep) {
		PreparedStatement ps = null;

		try {

			ps = conn.prepareStatement("INSERT INTO department (Name) " 
									 + "Values (?)",
									 Statement.RETURN_GENERATED_KEYS);

			ps.setString(1, dep.getName());

			int rowsAffected = ps.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = ps.getGeneratedKeys();
				if (rs.next()) {
					dep.setId(rs.getInt(1));
				}
				DB.closeResultSet(rs);
			} else {
				throw new DBException("Unexpected Error! No affected rows");
			}
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(ps);
		}
	}

	@Override
	public void update(Department dep) {
		PreparedStatement ps = null;

		try {

			ps = conn.prepareStatement("UPDATE department " 
									 + "SET Name = ? " 
									 + "WHERE Id = ?",
									 Statement.RETURN_GENERATED_KEYS);

			ps.setString(1, dep.getName());
			ps.setInt(2, dep.getId());

			int rowsAffected = ps.executeUpdate();

			if (rowsAffected == 0) {
				throw new DBException("Unexpected Error! No affected rows");
			}
			
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(ps);
		}
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement ps = null;
		
		try {
			
			ps = conn.prepareStatement("DELETE FROM department "
									 + "WHERE Id = ?");
			
			ps.setInt(1, id);
			
			int rowsAffected = ps.executeUpdate();
			
			if (rowsAffected == 0) {
				throw new DBException("Unexpected Error! No Rows Affected!");
			}
			
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(ps);
		}

	}

	@Override
	public Department findById(Integer id) {

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			ps = conn.prepareStatement("SELECT department.* " 
									 + "FROM department " 
									 + "WHERE Id = ?",
									 Statement.RETURN_GENERATED_KEYS);

			ps.setInt(1, id);
			rs = ps.executeQuery();
			Department dep = null;

			if (rs.next()) {
				dep = this.instantiateDepartment(rs);
			}

			return dep;

		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Department> findAll() {
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			ps = conn.prepareStatement("SELECT department.* " 
									 + "FROM department", 
									 Statement.RETURN_GENERATED_KEYS);

			rs = ps.executeQuery();

			Department dep = null;
			List<Department> listDep = new ArrayList<>();

			while (rs.next()) {
				dep = this.instantiateDepartment(rs);
				listDep.add(dep);
			}

			return listDep;
			
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("Id"));
		dep.setName(rs.getString("Name"));
		return dep;
	}

}
