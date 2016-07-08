package com.odark.dao.users;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.odark.domain.users.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/applicationContext.xml")
//메소드가 실행됐을때 default로 롤백처리 한다고 transactionManager지정
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=true)
//해당 클래스에 어노테이션기반으로 트랜잭션처리를 하겠다고 선언함
@Transactional
public class UserDaoTest {
	private static final Logger log = LoggerFactory.getLogger(UserDaoTest.class);
	@Autowired
	private UserDao userDao;

	@Test
	public void findById() {
		User user = userDao.findById("odark");
		log.debug("User : {}", user);
		log.debug(user.getEmail());
	}
	
	@Test
	//@Transactional 메소드level에서도 설정가능
	public void create() throws Exception {
		User user = new User("odarker","password","산지기","sanjigi@gmail.com");
		userDao.create(user);
		User actual = userDao.findById(user.getUserId());
		
		assertThat(actual, is(user));
	}

}
