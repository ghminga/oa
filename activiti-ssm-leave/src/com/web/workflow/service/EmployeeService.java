package com.web.workflow.service;

import java.util.List;

import com.web.workflow.pojo.Employee;
import com.web.workflow.pojo.EmployeeCustom;
import com.web.workflow.pojo.SysPermission;

public interface EmployeeService {
	
	public Employee findEmployeeByUserName(String username);
	
	public Employee findEmpById(long id);

	public List<SysPermission> findMenuListByUserId(Long id);

	public List<EmployeeCustom> findUserAndRoleList();

	public void updateEmployeeRole(String roleId, String userId);
	
}
