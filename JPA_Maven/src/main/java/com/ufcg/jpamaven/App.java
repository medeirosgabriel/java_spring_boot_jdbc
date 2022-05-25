package com.ufcg.jpamaven;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import domain.Person;


public class App {
	
    public static void main( String[] args ) {
    	Person p1 = new Person(null, "Carlos da Silva", "carlos@gmail.com");
    	Person p2 = new Person(null, "Joaquim Torres", "joaquim@gmail.com");
    	Person p3 = new Person(null, "Ana Maria", "ana@gmail.com");
    	
    	EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-example"); // persistence.xml
    	EntityManager em = emf.createEntityManager();
    	
    	em.getTransaction().begin();
    	em.persist(p1);
    	em.persist(p2);
    	em.persist(p3);
    	System.out.println("Persistence OK!");
    	
    	Person p4 = new Person(2, "Joaquim Torres", "joaquim@gmail.com"); // Detached Instance
    	Person p5 = em.find(Person.class, 2); // Monitored Instance
    	
    	//em.remove(p4); // Error - Detached instance can't be removed
    	em.remove(p5);
    	
    	System.out.println(p5);
    	
    	em.getTransaction().commit();
    	em.close();
    	emf.close();
    }
    
}
