package com.web.workflow.pojo;

import java.util.List;

/**
 * 树型菜单
 * @author gec
 *
 */
public class TreeMenu {

	private int id;
	private String name;
	
	private List<SysPermission> subMenu;//子菜单

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<SysPermission> getSubMenu() {
		return subMenu;
	}

	public void setSubMenu(List<SysPermission> subMenu) {
		this.subMenu = subMenu;
	}
	
	
	
}
