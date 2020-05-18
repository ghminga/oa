package com.web.workflow.utils;

import javax.servlet.http.HttpSession;


import org.activiti.engine.delegate.DelegateTask;

import org.activiti.engine.delegate.TaskListener;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.web.workflow.pojo.ActiveUser;
import com.web.workflow.pojo.Employee;
import com.web.workflow.service.EmployeeService;

public class TaskAssignHandler implements TaskListener {
	
	
	@Override
	public void notify(DelegateTask task) {
		//得到spring的容器
		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		//EmployeeService employeeService = (EmployeeService) context.getBean("employeeService");
		
		//获取session
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpSession session = requestAttributes.getRequest().getSession();
//		Employee employee = (Employee) session.getAttribute(Constants.GLOBAL_SESSION_ID);
		ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
		EmployeeService employeeService = (EmployeeService) context.getBean("employeeService");
		Employee manager = employeeService.findEmpById(activeUser.getManagerId());
		String message = (String) session.getAttribute("message");
		if(message.equals("金额小于等于5000")){				
			task.setAssignee("li");
		}else{
			task.setAssignee(manager.getName());
		}								
		//System.out.println(manager.getName());
		
	}

}
