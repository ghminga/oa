package com.web.workflow.controller;

import java.util.List;


import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.workflow.pojo.ActiveUser;



@Controller
public class BaoxiaoBillController {
	
	
	//返回主页面
	@RequestMapping("/main")
	public String main(ModelMap model) {
		ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
		model.addAttribute("activeUser", activeUser);		
		return "index";		
	}
	
	
	
	
}
