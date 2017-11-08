package com.jinmei.s2pdsudp.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import oracle.jdbc.driver.OracleDriver;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

public final class C3P0ConnectionManager {
	private static C3P0ConnectionManager instance;

	public ComboPooledDataSource ds;
	private static String c3p0Properties = "/c3p0.properties";

	private C3P0ConnectionManager() throws Exception {
		InputStream ins = this.getClass().getResourceAsStream(c3p0Properties);
		Properties p = new Properties();
		p.load(ins);
		//p.load(this.getClass().getResourceAsStream("E:\\派速魔方\\svn\\svn3\\营销系统\\代码管理\\trunk\\s2pdsudp\\src\\c3p0.properties"));
		ds = new ComboPooledDataSource();
		ds.setUser(p.getProperty("user"));
		ds.setPassword(p.getProperty("password"));
		ds.setJdbcUrl(p.getProperty("jdbcUrl"));
		ds.setDriverClass(p.getProperty("driverClass"));
		ds.setInitialPoolSize(Integer.parseInt(p.getProperty("initialPoolSize")));
		ds.setMinPoolSize(Integer.parseInt(p.getProperty("minPoolSize")));
		ds.setMaxPoolSize(Integer.parseInt(p.getProperty("maxPoolSize")));
		ds.setMaxStatements(Integer.parseInt(p.getProperty("maxStatements")));
		ds.setMaxIdleTime(Integer.parseInt(p.getProperty("maxIdleTime")));
	}

	public static final C3P0ConnectionManager getInstance() {
		if (instance == null) {
			try {
				instance = new C3P0ConnectionManager();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return instance;
	}

	public synchronized final Connection getConnection() {
		try {
			return ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected void finalize() throws Throwable {
		DataSources.destroy(ds); // 关闭datasource
		super.finalize();
	}
}
