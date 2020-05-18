package com.web.workflow.shiro;

import java.util.ArrayList;



import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.web.workflow.pojo.ActiveUser;
import com.web.workflow.pojo.Employee;
import com.web.workflow.pojo.SysPermission;
import com.web.workflow.service.EmployeeService;
import com.web.workflow.service.SysService;


public class CustomRealm extends AuthorizingRealm {
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private SysService sysService;
	
	//认证
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
			throws AuthenticationException {		
		//1.获取账户
		String username = (String) token.getPrincipal();
		Employee loginEmployee = null;
		
		try {
			loginEmployee = employeeService.findEmployeeByUserName(username);
		if(loginEmployee == null){
			return null;
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String password_db = loginEmployee.getPassword();
		System.out.println(password_db);
		ActiveUser activeUser = new ActiveUser();
		activeUser.setUserid(String.valueOf(loginEmployee.getId()));
		activeUser.setUsercode(loginEmployee.getName());
		activeUser.setUsername(loginEmployee.getName());
		activeUser.setManagerId(loginEmployee.getManagerId());
		activeUser.setId(loginEmployee.getId());
		String salt = loginEmployee.getSalt();
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(activeUser,password_db,ByteSource.Util.bytes(salt),"CustomRealm");
		return info;
	}
	
	//授权:查询出当前合法用户的使用权限
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
		ActiveUser activeUser = (ActiveUser) principal.getPrimaryPrincipal();
		List<SysPermission> permissions = null;
		try {
			permissions = sysService.findPermissionListByUserId(activeUser.getUsername());
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<String> userpermissionList = new ArrayList<>();
		for (SysPermission sysPermission : permissions) {
			System.out.println(sysPermission);
			userpermissionList.add(sysPermission.getPercode());
		}
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.addStringPermissions(userpermissionList);
		
		return info;	
	}

	

}
