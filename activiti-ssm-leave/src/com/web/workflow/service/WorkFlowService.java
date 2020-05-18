package com.web.workflow.service;

import java.io.InputStream;

import java.util.List;
import java.util.Map;

import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;

import com.web.workflow.pojo.BaoXiaobill;
import com.web.workflow.pojo.Employee;
import com.web.workflow.pojo.Leavebill;

public interface WorkFlowService {
	
	public void deployProcess(String processName,InputStream input);
	
	public List<Deployment> findDeploymentList();
	
	public List<ProcessDefinition> findProcessDefinitionList();

	public void saveBaoXiaoAndStartProcess(BaoXiaobill bill, Employee employee);

	public List<Task> findMyTaskListByUserId(String name);

	public BaoXiaobill findBillByTask(String taskId);

	public List<Comment> findCommentListByTask(String taskId);

	public void submitTask(String taskId,String output,String name,Integer id,String comment);

	public InputStream findImageInputStream(String deploymentId, String imageName);

	public ProcessDefinition findProcessDefinitionByTaskId(String taskId);

	public Map<String, Object> findCoordingByTask(String taskId);

	public void removeProcessDef(String deploymentId);

	public List<String> findOutComeListByTask(String taskId);

	public void removeTaskById(String taskId);

	public List<BaoXiaobill> findBaoxiaoBillListByUser(long id);

	public Task findTaskByBussinessKey(String bUSSINESS_KEY);

	public BaoXiaobill findBaoxiaoBillById(long id);

	public List<Comment> findCommentByBaoxiaoBillId(long id);

	public void removeBaoxiaoBillById(Integer id);
	
}
