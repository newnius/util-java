package com.newnius.util;

import java.util.Properties;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSourceFactory;

/**
 * 
 * provide simple interface for database access
 * 
 * @author Newnius
 * @version 0.1.0(JAVA SE)
 * Dependencies
 * 	com.newnius.util.CRLogger
 *  
 *  Before call getInstance(), you have to call init(Properties) once
 *  example code as follows:
 *  <code>
   		String confFile = "dbcp.properties";
		String configFile = confFile;
		Properties dbProperties = new Properties();
		try {
			dbProperties.load(CRDAO.class.getClassLoader().getResourceAsStream(configFile));
			CRDAO.init(dbProperties);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
 *  </code>
 * 
 */
public class CRDAO {
	private static final String TAG = "CRDBManager";
	private static CRLogger logger;
	private static DataSource dataSource;
	private boolean autoCommit = true;
	private Connection connection;
	private PreparedStatement preparedStatement;

	private CRDAO() {
		connection = getConnection();
	}
	
	public void setAutoCommit(boolean autoCommit){
		this.autoCommit = autoCommit;
	}
	
	public static void init(Properties dbProperties) {
		logger = CRLogger.getLogger(TAG);
		try {
			dataSource = BasicDataSourceFactory.createDataSource(dbProperties);
			logger.info("CRDAO initailed.");
		} catch (Exception ex) {
			logger.error(ex);
		}
	}

	private static Connection getConnection() {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
		} catch (Exception ex) {
			logger.error(ex);
		}
		return conn;
	}

	
	public static CRDAO getInstance() throws Exception{
		if (dataSource == null) {
			throw new Exception("dataSource is not initailed. Please class CRDAO.inint(Properties dbProperties) first");
		}
		return new CRDAO();
	}

	public ResultSet executeQuery(String sql, String[] args) {
		try {
			preparedStatement = connection.prepareStatement(sql);
			if (args != null) {
				for (int i = 0; i < args.length; i++) {
					preparedStatement.setString(i + 1, args[i]);
				}
			}
			logger.debug(preparedStatement.toString());
			return preparedStatement.executeQuery();

		} catch (Exception ex) {
			logger.error(ex);
			return null;
		} finally {
			if(autoCommit)
				commit();
		}
	}

	public int executeUpdate(String sql, String[] args) {
		try {
			preparedStatement = connection.prepareStatement(sql);
			if (args != null) {
				for (int i = 0; i < args.length; i++) {
					preparedStatement.setString(i + 1, args[i]);
				}
			}
			logger.debug(preparedStatement.toString());
			return preparedStatement.executeUpdate();
		} catch (Exception ex) {
			logger.error(ex);
			return 0;
		} finally {
			if (autoCommit)
				commit();
		}
	}

	public void commit() {
		if (connection != null) {
			try {
				if(!connection.getAutoCommit())
					connection.commit();
				connection.close();
				connection = null;
			} catch (Exception ex) {
				logger.error(ex);
			}
		}

		if (preparedStatement != null) {
			try {
				preparedStatement.close();
				preparedStatement = null;
			} catch (Exception ex) {
				logger.error(ex);
			}
		}
	}

}
