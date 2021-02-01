package main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class App1 {

	public static void main(String[] args) {

		String sql = null;
		Statement stmt = null;

		try {

			// Fabrica uma conexao com o banco de dados
			Connection conn = JDBCConnection.openConnection();

			// Sql para consulta de alunos que trabalham no projeto com id 1
			sql = "SELECT fname FROM employee;";

			stmt = conn.createStatement();

			// Executa a operacao
			ResultSet rs = stmt.executeQuery(sql);

			// Extracao dos resultados
			while (rs.next()) {

				System.out.println(rs.getString("fname"));

			}

		} catch (SQLException e) {

			e.printStackTrace();

		}
	}
}
