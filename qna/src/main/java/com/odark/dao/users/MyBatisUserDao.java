package com.odark.dao.users;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.odark.domain.users.User;

@Repository
//또한 Resource 어노테이션 이용시 명시적으로 bean Id 이름을 선언해줄수도 있다.
//@Repository(name="userDao")
public class MyBatisUserDao implements UserDao {
	
	private static final Logger log = LoggerFactory.getLogger(MyBatisUserDao.class);
	
	//아래 autowired는 타입기반으로 injection 으로 접근하지만 이름기반으로 할때는 @Resource 어노테이션 이용
	//@Autowired
	@Resource(name="sqlSession")
	private SqlSession sqlSession;
	
	/**
	 * Mybatis Migration Tool(Flyway)를 이용하여 마이그레이션이 미리 됐기 때문에 database초기화할 필요가 없어져
	 * 관련 부분을 주석 처리한다.
	 */
	/*
	private DataSource dataSource;
	
	@PostConstruct
	public void initialize(){
		ResourceDatabasePopulator populater = new ResourceDatabasePopulator();
		populater.addScript(new ClassPathResource("odark.sql"));
		DatabasePopulatorUtils.execute(populater, dataSource);
		log.info("database initialized success!");
	}
		public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	*/
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	


	@Override
	public User findById(String userId) {
		return sqlSession.selectOne("UserMapper.findById", userId);
	}

	@Override
	public void create(User user) {
		sqlSession.insert("UserMapper.create", user);

	}

	@Override
	public void update(User user) {
		sqlSession.update("UserMapper.update", user);

	}

}
