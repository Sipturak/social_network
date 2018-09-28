package com.social_network.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Provider {

	private Connection connection;
	private PreparedStatement preparedStatement;
	
	public Provider() {
		// TODO Auto-generated constructor stub
	}
	
	public PreparedStatement getPreparedStatament(String sql) throws SQLException {
		return connection.prepareStatement(sql);
	}
	
	public ResultSet getResultSetCheckingUser(String sql,int id) throws SQLException {
		preparedStatement = getPreparedStatament(sql);
		preparedStatement.setInt(1, id);
		return preparedStatement.executeQuery();
	}
	
	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public static class DBConnection {

		enum DbTypes {

			Mysql("jdbc:mysql://localhost/social_network?useSSL=false", "root", "15109215");

			private String url;
			private String userl;
			private String password;

			private DbTypes(String url, String userl, String password) {
				this.url = url;
				this.userl = userl;
				this.password = password;
			}

			public String getUrl() {
				return url;
			}

			public String getUserl() {
				return userl;
			}

			public String getPassword() {
				return password;
			}

		}

		public static Connection getConnection(DbTypes dbTypes) throws SQLException {
			return DriverManager.getConnection(dbTypes.getUrl(), dbTypes.getUserl(), dbTypes.getPassword());
		}

	}
}
