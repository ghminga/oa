package com.web.workflow.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.workflow.mapper.SysPermissionMapperCustom;
import com.web.workflow.mapper.SysRoleMapper;
import com.web.workflow.pojo.SysPermission;
import com.web.workflow.pojo.SysRole;
import com.web.workflow.service.SysService;
@Service("/sysService")
public class SysServiceImpl implements SysService {

	@Autowired
	private SysPermissionMapperCustom sysPermissionMapperCustom;
	@Autowired
	private SysRoleMapper sysRoleMapper;
	
	//获取某个人的所有权限
	@Override
	public List<SysPermission> findPermissionListByUserId(String userid) throws Exception{		
		return sysPermissionMapperCustom.findPermissionListByUserId(userid);
	}

	@Override
	public List<SysRole> findAllRoles() {		
		return sysRoleMapper.selectByExample(null);
	}

	@Override
	public SysRole findRolesAndPermissionsByUserId(String userName) {				
		return sysPermissionMapperCustom.findRoleAndPermissionListByUserId(userName);
	}

}
