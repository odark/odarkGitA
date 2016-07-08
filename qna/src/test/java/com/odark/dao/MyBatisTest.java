package com.odark.dao;

import java.io.InputStream;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import com.odark.domain.users.User;

public class MyBatisTest {
	private static final Logger log = LoggerFactory.getLogger(MyBatisTest.class);
	private SqlSessionFactory sqlSessionFactory;

	@Before
	public void before() throws Exception {
		String resource = "mybatis-config-test.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		
		ResourceDatabasePopulator populater = new ResourceDatabasePopulator();
		populater.addScript(new ClassPathResource("odark.sql"));
		DatabasePopulatorUtils.execute(populater, getDataSource());
		log.info("database initialized success!");

	}
	
	private DataSource getDataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName("org.h2.Driver");
		dataSource.setUrl("jdbc:h2:~/odark");
		dataSource.setUsername("od");
		return dataSource;
	}

	@Test
	public void gettingStarted() throws Exception {
		/**
		 * JDK 7.0 이전버전의 코드
		 */
/*		SqlSession session = sqlSessionFactory.openSession();
		try {
			User user = session.selectOne("UserMapper.findById", "odark");
			log.debug("User : {}", user);
		} finally {
			session.close(); //리소스 반환
		}
*/		
		/**
		 * JDK7.0 이상부터는 리소스 생성하고 close하는 부분이 자동으로 지원된다.
		 * SqlSession 클래스가 Closeable 인터페이스를 상속받고있어야 한다.
		 */
		try (SqlSession session = sqlSessionFactory.openSession()){
			User user = session.selectOne("UserMapper.findById", "odark");
			log.debug("User : {}", user);
		} 
		
	}
	
	@Test
	public void insert() throws Exception {
		try (SqlSession session = sqlSessionFactory.openSession()){
			User user = new User("odark1", "password", "dhfgfgdfgd", "sanjigi@gmail.com");
			session.insert("UserMapper.create", user);
			
			User actual = session.selectOne("UserMapper.findById", user.getUserId());
			assertThat(user, is(actual));
		} 
	}
	
	

}
