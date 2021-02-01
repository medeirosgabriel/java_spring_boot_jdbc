package main;

import java.sql.Connection;
import java.sql.DriverManager;

public class JDBCConnection {

	private static String driver = "org.postgresql.Driver";
	private static String url = "jdbc:postgresql://150.165.15.11:5432/"; // Alterar (host=150.165.15.11 port=5432)

	private static String nomeBD = "gabrielpm_db"; // Alterar (usuario_db)
	private static String usuarioBD = "gabrielpm"; // Alterar
	private static String senhaBD = "bd1#BD1"; // Alterar (bd1#BD1)

	public static Connection openConnection() {

		final String urlConexao = url + nomeBD;

		Connection conexao = null;

		try {

			Class.forName(driver);
			conexao = DriverManager.getConnection(urlConexao, usuarioBD, senhaBD);
			conexao.setAutoCommit(false);

			System.out.println("Conexao com o banco " + nomeBD + " aberta com sucesso!!! \n\n");

		} catch (Exception e) {

			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);

		}

		return conexao;

	}

}
