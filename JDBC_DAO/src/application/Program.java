package application;

import java.util.Date;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.dao.SellerDao;
import model.dao.impl.SellerDaoJDBC;
import model.entities.Department;
import model.entities.Seller;

public class Program {
	
	public static void main(String[] args) {
		
		SellerDao sellerDao = DaoFactory.createSellerDao();
		
		System.out.println("================== TEST 1 - Seller findById ==================");
		Seller seller1 = sellerDao.findById(1);
		System.out.println(seller1);
		
		System.out.println("\n============= TEST 2 - Seller findByDepartment =============");
		Department dep = new Department(4, "");
		List<Seller> sellers1 = sellerDao.findByDepartment(dep);
		for (Seller seller2 : sellers1) {
			System.out.println(seller2);
		}
		
		System.out.println("\n================= TEST 3 - Seller findAll ==================");
		List<Seller> sellers2 = sellerDao.findAll();
		for (Seller seller3 : sellers2) {
			System.out.println(seller3);
		}
		
		System.out.println("\n================== TEST 4 - Seller insert ==================");
		Seller newSeller = new Seller(null, "Greg", "greg@gmail.com", new Date(), 4000.0, dep);
		sellerDao.insert(newSeller);
		System.out.println(newSeller);
		
		System.out.println("\n================== TEST 5 - Seller update ==================");
		Seller seller4 = sellerDao.findById(1);
		seller4.setName("Martha Wayne");
		sellerDao.update(seller4);
		System.out.println("Update Completed");
		
		System.out.println("\n================== TEST 6 - Seller delete ==================");
		//sellerDao.deleteById(14);
		System.out.println("Delete Completed");
		
		
		
		DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
		
		System.out.println("\n\n\n================= TEST 1 - Department findById ===============");
		Department dep1 = departmentDao.findById(4);
		System.out.println(dep1);
		
		
		System.out.println("\n=============== TEST 2 - Department findAll ================");
		List<Department> deps = departmentDao.findAll();
		for (Department dep2 : deps) {
			System.out.println(dep2);
		}
		
		System.out.println("\n================ TEST 3 - Department insert ================");
		Department newDep = new Department(null, "Electronics");
		departmentDao.insert(newDep);
		System.out.println(newDep);
		
		System.out.println("\n================ TEST 4 - Department update ================");
		Department dep3 = departmentDao.findById(5);
		dep3.setName("Foods");
		departmentDao.update(dep3);
		System.out.println("Update Completed");
		
		System.out.println("\n================ TEST 5 - Department delete ================");
		departmentDao.deleteById(9);
		System.out.println("Delete Completed");
		
	}
}
