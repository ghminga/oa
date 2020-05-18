package com.web.workflow.controller;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.workflow.pojo.ActiveUser;
import com.web.workflow.pojo.Employee;
import com.web.workflow.service.EmployeeService;
import com.web.workflow.utils.Constants;

@Controller
public class EmployeeController {
	
//	@Autowired
//	private EmployeeService employeeService;
	
//	@RequestMapping("/login")
//	public String login(String username,String password,HttpSession session,Model model) throws Exception{
//		Employee loginUser = employeeService.findEmployeeByUserName(username);
//		if(loginUser !=null){
//			if(loginUser.getPassword().equals(password)){
//				session.setAttribute(Constants.GLOBAL_SESSION_ID, loginUser);
//				return "index";
//			}else{
//				model.addAttribute("errorMsg", "密码错误!");
//				return "login";
//			}
//		}else{
//			model.addAttribute("errorMsg", "账号错误!");
//			return "login";
//		}
//		
//	}
//	
//	@RequestMapping("/logout")
//	public String logout(HttpSession session){
//		session.invalidate();
//		return "login";
//		
//	}
	
//	@RequestMapping("/login")
//	public String login(HttpServletRequest request,Model model) throws Exception{		
//		String errorException = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
//		System.out.println(errorException);
//		if(UnknownAccountException.class.getName().equals(errorException)){
//			model.addAttribute("errorMsg", "账号错误");
//			return "login";
//		}
//		if(IncorrectCredentialsException.class.getName().equals(errorException)){
//			model.addAttribute("errorMsg", "密码错误");
//			return "login";
//		}
//		return "redict:/main";
//	}
	
		
	
	
//	@RequestMapping("/main")
//	public String main(ModelMap model) {
//		ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
//		model.addAttribute("activeUser", activeUser);		
//		return "index";
//		
//	}
	
	@RequestMapping("/login")
	public String login(HttpServletRequest request,Model model){
		
		String exceptionName = (String) request.getAttribute("shiroLoginFailure");
		if (exceptionName != null) {
			if (UnknownAccountException.class.getName().equals(exceptionName)) {
				model.addAttribute("errorMsg", "用户账号不存在");
			} else if (IncorrectCredentialsException.class.getName().equals(exceptionName)) {
				model.addAttribute("errorMsg", "密码不正确");
			} else if("randomcodeError".equals(exceptionName)) {
				model.addAttribute("errorMsg", "验证码不正确");
			}
			else {
				model.addAttribute("errorMsg", "未知错误");
			}
		}
		return "login";
	}
	
	
	
	
}
