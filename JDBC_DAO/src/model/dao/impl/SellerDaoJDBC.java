package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.Statement;

import java.sql.Date;

import db.DB;
import db.DBException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {
	
	private Connection conn;

	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Seller seller) {
		PreparedStatement ps = null;
		try {
			
			ps = conn.prepareStatement("INSERT INTO seller (Name, Email, BirthDate, BaseSalary, DepartmentId) "
									 + "Values (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			
			ps.setString(1, seller.getName());
			ps.setString(2, seller.getEmail());
			ps.setDate(3, new Date(seller.getBirthDate().getTime()));
			ps.setDouble(4, seller.getBaseSalary());
			ps.setInt(5, seller.getDepartment().getId());
			
			int rowsAffected = ps.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = ps.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					seller.setId(id);
				}
				DB.closeResultSet(rs);
			} else {
				throw new DBException("Unexpected Error! No affected rows!");
			}
			
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(ps);
		}
	}

	@Override
	public void update(Seller seller) {
		PreparedStatement ps = null;
		try {
			
			ps = conn.prepareStatement("UPDATE seller "
									 + "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
									 + "WHERE Id = ?", Statement.RETURN_GENERATED_KEYS);
			
			ps.setString(1, seller.getName());
			ps.setString(2, seller.getEmail());
			ps.setDate(3, new Date(seller.getBirthDate().getTime()));
			ps.setDouble(4, seller.getBaseSalary());
			ps.setInt(5, seller.getDepartment().getId());
			ps.setInt(6, seller.getDepartment().getId());
			
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
	public void deleteById(Integer id) {
		PreparedStatement ps = null;
		try {
			
			ps = conn.prepareStatement("DELETE FROM seller "
									 + "WHERE Id = ?", Statement.RETURN_GENERATED_KEYS);
			
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
	public Seller findById(Integer id) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT seller.*, department.Name as DepName "
										+ "FROM seller INNER JOIN department ON seller.DepartmentId = department.Id "
										+ "WHERE seller.Id = ?");
			ps.setInt(1, id);
			rs = ps.executeQuery();
			Seller seller = null;
			if (rs.next()) {
				Department dep = this.instantiateDepartment(rs);
				seller = this.instantiateSeller(rs, dep);
			}
			return seller;
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT seller.*, department.Name as DepName "
									+ "FROM seller INNER JOIN department ON seller.DepartmentId = department.Id "
									+ "ORDER BY Name");
			
			rs = ps.executeQuery();
			
			List<Seller> listSeller = new ArrayList<>();
			Map<Integer, Department> departments = new HashMap<>();
			
			while (rs.next()) {
				
				int key = rs.getInt("DepartmentId");
				Department dep = departments.get(key);
				
				if (dep == null) {
					dep = this.instantiateDepartment(rs);
					departments.put(key, dep);
				}
				
				Seller seller = this.instantiateSeller(rs, dep);
				listSeller.add(seller);
			}
			
			return listSeller;
			
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
	}
	
	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT seller.*, department.Name as DepName "
									+ "FROM seller INNER JOIN department ON seller.DepartmentId = department.Id "
									+ "WHERE department.Id = ? "
									+ "ORDER BY Name");
			
			ps.setInt(1, department.getId());
			rs = ps.executeQuery();
			
			List<Seller> listSeller = new ArrayList<>();
			Map<Integer, Department> departments = new HashMap<>();
			
			while (rs.next()) {
				
				int key = rs.getInt("DepartmentId");
				Department dep = departments.get(key);
				
				if (dep == null) {
					dep = this.instantiateDepartment(rs);
					departments.put(key, dep);
				}
				
				Seller seller = this.instantiateSeller(rs, dep);
				listSeller.add(seller);
			}
			
			return listSeller;
			
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
	}
	
	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}
	
	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller seller = new Seller();
		seller.setId(rs.getInt("Id"));
		seller.setName(rs.getString("Name"));
		seller.setEmail(rs.getString("Email"));
		seller.setBaseSalary(rs.getDouble("BaseSalary"));
		seller.setBirthDate(rs.getDate("BirthDate"));
		seller.setDepartment(dep);
		return seller;
	}
}
