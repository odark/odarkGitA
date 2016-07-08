package com.odark.web.users;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.odark.dao.users.UserDao;

/*
 * MockitoJUnitRunner 이용하여
   밑에설정되어있는 어노테이션을 기반으로 클래스간의 의존관계를 설정해준다.
*/
@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {
	/**
	 * 실제로 DB와 연동하지 않으면서 UserDao에 있는 메소드를 호출할 수있다.
	 */
	@Mock
	private UserDao userDao;
	
	/*
	 * userController가 생성될때  UserDao mock을 만들어서 두 클래스간의 의존관계를 만들어줄 수 있다.
	 */
	@InjectMocks
	private UserController userController;
	
	private MockMvc mockMvc;
	
	@Before
	public void setup() throws Exception {
	/*	this.mockMvc = standaloneSetup(new UserController())
				.alwaysExpect(status().isMovedTemporarily()).build();*/
		this.mockMvc = standaloneSetup(userController).build();
	}

	@Test
	public void createWhenValid() throws Exception{
		this.mockMvc.perform(post("/users")
				.param("userId", "odark")
				.param("password", "password")
				.param("name", "자바")
				.param("email", ""))
				.andDo(print())
			.andExpect(status().isMovedTemporarily())
			.andExpect(redirectedUrl("/"));
	}
	
	@Test
	public void createWhenInvalid() throws Exception{
		this.mockMvc.perform(post("/users")
				.param("userId", "odark")
				.param("password", "password")
				.param("name", "자바")
				.param("email", "odark"))
				.andDo(print())
				.andExpect(status().isOk())
			.andExpect(forwardedUrl("users/form"));
	}

}
