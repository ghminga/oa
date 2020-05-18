package com.web.workflow.service;

import java.util.List;

import com.web.workflow.pojo.SysPermission;
import com.web.workflow.pojo.SysRole;



public interface SysService {

	//获取某个人的所有权限
	public List<SysPermission> findPermissionListByUserId(String userid) throws Exception;

	public List<SysRole> findAllRoles();

	public SysRole findRolesAndPermissionsByUserId(String userName);
	
}
