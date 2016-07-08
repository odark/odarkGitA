package com.odark.dao.users;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import com.odark.domain.users.User;

public class JdbcUserDao extends JdbcDaoSupport implements UserDao{
	private static final Logger log = LoggerFactory.getLogger(JdbcUserDao.class);
	
	@PostConstruct
	public void initialize(){
		ResourceDatabasePopulator populater = new ResourceDatabasePopulator();
		populater.addScript(new ClassPathResource("odark.sql"));
		DatabasePopulatorUtils.execute(populater, getDataSource());
		log.info("database initialized success!");
	}

	/* (non-Javadoc)
	 * @see com.odark.dao.users.IUserDao#findById(java.lang.String)
	 */
	@Override
	public User findById(String userId) {
		String sql ="select * from USERS where userId = ?";
		RowMapper<User> rowMapper = new RowMapper<User>(){

			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new User(
						rs.getString("userId"),
						rs.getString("password"),
						rs.getString("name"),
						rs.getString("email")
						);
			}
			
		};
		try{
			return getJdbcTemplate().queryForObject(sql, rowMapper, userId);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}	
	}

	/* (non-Javadoc)
	 * @see com.odark.dao.users.IUserDao#create(com.odark.domain.users.User)
	 */
	@Override
	public void create(User user) {
		String sql = "insert into USERS values (?,?,?,?)";
		getJdbcTemplate().update(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail() );
		
	}

	/* (non-Javadoc)
	 * @see com.odark.dao.users.IUserDao#update(com.odark.domain.users.User)
	 */
	@Override
	public void update(User user) {
		String sql = "update USERS set password = ?, name = ?, email = ? where userId = ?";
		getJdbcTemplate().update(sql, user.getPassword(), user.getName(), user.getEmail(), user.getUserId() );
		
	}
}
