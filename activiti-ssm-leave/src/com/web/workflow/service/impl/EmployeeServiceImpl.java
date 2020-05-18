package com.web.workflow.service.impl;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.workflow.mapper.EmployeeMapper;
import com.web.workflow.mapper.SysPermissionMapper;
import com.web.workflow.mapper.SysPermissionMapperCustom;
import com.web.workflow.mapper.SysUserRoleMapper;
import com.web.workflow.pojo.Employee;
import com.web.workflow.pojo.EmployeeCustom;
import com.web.workflow.pojo.EmployeeExample;
import com.web.workflow.pojo.EmployeeExample.Criteria;
import com.web.workflow.pojo.SysPermission;
import com.web.workflow.pojo.SysUserRole;
import com.web.workflow.pojo.SysUserRoleExample;
import com.web.workflow.service.EmployeeService;

@Service("employeeService")
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeMapper employeeMapper;
	@Autowired
	private SysPermissionMapper sysPermissionMapper;
	@Autowired
	private SysPermissionMapperCustom sysPermissionMapperCustom;
	@Autowired
	private SysUserRoleMapper userRoleMapper;
	@Override
	public Employee findEmployeeByUserName(String username) {
		EmployeeExample example = new EmployeeExample();
		EmployeeExample.Criteria criteria = example.createCriteria();
		criteria.andNameEqualTo(username);
		List<Employee> list = employeeMapper.selectByExample(example);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public Employee findEmpById(long id) {
		return employeeMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<SysPermission> findMenuListByUserId(Long id) {
		
		return null;
	}

	@Override
	public List<EmployeeCustom> findUserAndRoleList() {
		
		return sysPermissionMapperCustom.findUserAndRoleList();
	}

	@Override
	public void updateEmployeeRole(String roleId, String userId) {
		SysUserRoleExample example = new SysUserRoleExample();
		SysUserRoleExample.Criteria criteria = example.createCriteria();
		criteria.andSysUserIdEqualTo(userId);
		
		SysUserRole userRole = userRoleMapper.selectByExample(example).get(0);
		userRole.setSysRoleId(roleId);
		
		userRoleMapper.updateByPrimaryKey(userRole);
	}
	
	

}
