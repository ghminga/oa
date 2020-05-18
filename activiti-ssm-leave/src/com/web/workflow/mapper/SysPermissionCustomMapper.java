package com.web.workflow.mapper;

import java.util.List;


import com.web.workflow.pojo.MenuTree;
import com.web.workflow.pojo.SysPermission;




public interface SysPermissionCustomMapper {

	
	public List<MenuTree> getTreeMenu();
	
	public List<SysPermission> getSubMenu(int id);
}
