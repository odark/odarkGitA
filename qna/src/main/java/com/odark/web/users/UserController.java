package com.odark.web.users;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.odark.dao.users.UserDao;
import com.odark.domain.users.Authenticate;
import com.odark.domain.users.User;

@Controller
@RequestMapping("/users")
public class UserController {
	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private ResourceBundleMessageSource messageSource;
	
	//@Autowired
	//아래처럼 MyBatisUserDao는 별도로 bean id등록을 xml에 추가해주지 않았는데 자동으로 클래스 이름 기준으로 생성됨을 알수 있고
	//이 이름마저 변경하고 싶다면 MyBatisUserDao 클래스 @Repository(name="userDao") 이렇게 이름을 선언을 해줘서 userDao이름으로 inject해줄수도 있다.
	@Resource(name="myBatisUserDao")
	private UserDao userDao;
	
	@RequestMapping("/form")
	public String createForm(Model model){
		model.addAttribute("user", new User());
		return "users/form";
	}
	
	@RequestMapping(value="", method=RequestMethod.POST)
	public String create(@Valid User user, BindingResult bindingResult){
		log.debug("User : {}", user);
		if(bindingResult.hasErrors()){
			log.debug("Binding Result has error!");
			List<ObjectError> errors =bindingResult.getAllErrors();
			for (ObjectError error: errors){
				log.debug("error : {}, {}", error.getCode(),error.getDefaultMessage());
			}
			return "users/form";
		}
		userDao.create(user);
		log.debug("Database : {}", userDao.findById(user.getUserId()));
		return "redirect:/";
	}	
	
	@RequestMapping("{userId}/form")
	public String updateForm(@PathVariable String userId, Model model){
		if (userId == null) {
			throw new IllegalArgumentException("사용자 아이디가 필요합니다.");
		}
		
		User user = userDao.findById(userId);
		model.addAttribute("user", user);
		return "users/form";
	}
	
	@RequestMapping(value="", method=RequestMethod.PUT)
	public String update(@Valid User user, BindingResult bindingResult, HttpSession session){
		log.debug("User : {}", user);
		if(bindingResult.hasErrors()){
			log.debug("Binding Result has error!");
			List<ObjectError> errors =bindingResult.getAllErrors();
			for (ObjectError error: errors){
				log.debug("error : {}, {}", error.getCode(),error.getDefaultMessage());
			}
			return "users/form";
		}
		
		Object temp = session.getAttribute("userId");
		if (temp == null) {
			throw new NullPointerException();
		}
		
		//도메인 클래스들에게 로직을 끌어들이면 필요가 없어진다. 예외처리로직을 모두 다 담당하고있다.
		//도메인 클래스들에게 로직을 끌어들일 필요가 있다.
		/*
		String userId = (String)temp;
		if (user.matchUserId(userId)){
			throw new NullPointerException();
		}*/
		
		userDao.update(user);
		log.debug("Database : {}", userDao.findById(user.getUserId()));
		return "redirect:/";
	}	
	
	@RequestMapping("/login/form")
	public String loginForm(Model model){
		model.addAttribute("authenticate", new Authenticate());
		return "users/login";
	}
	
	@RequestMapping("/login")
	public String login(@Valid Authenticate authenicate, BindingResult bindingResult, HttpSession session, Model model){
		log.debug("Authenicate : {}", authenicate);
		if(bindingResult.hasErrors()){
			return "users/login";
		}
		User user = userDao.findById(authenicate.getUserId());
		if(user == null) {
			// TODO 에러처리 - 존재하지 않는 사용자 입니다.
			model.addAttribute("errorMessage","존재하지 않는 사용자 입니다.");
			return "users/login";
			
		}
		if(!user.matchPassword(authenicate)){
			// TODO 에러처리 - 비밀번호가 틀립니다.
			model.addAttribute("errorMessage","비밀번호가 틀립니다.");
			return "users/login";
		}
		
		// TODO 세션에 사용자 정보 저장
		session.setAttribute("userId", user.getUserId());
		
		return "redirect:/";
	}
	
	@RequestMapping("/logout")
	public String logout(HttpSession session){
		session.removeAttribute("userId");
		return "redirect:/";
	}
}
