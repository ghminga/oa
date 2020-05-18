package com.web.workflow.service.impl;

import java.io.FileInputStream;


import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.pagehelper.util.StringUtil;
import com.web.workflow.mapper.BaoXiaobillMapper;
import com.web.workflow.mapper.EmployeeMapper;
import com.web.workflow.mapper.LeavebillMapper;
import com.web.workflow.pojo.BaoXiaobill;
import com.web.workflow.pojo.BaoXiaobillExample;
import com.web.workflow.pojo.Employee;
import com.web.workflow.pojo.EmployeeExample;
import com.web.workflow.pojo.Leavebill;
import com.web.workflow.service.WorkFlowService;
import com.web.workflow.utils.Constants;

@Service
public class WorkFlowServiceImpl implements WorkFlowService {
	
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private LeavebillMapper leavebillMapper;
	@Autowired
	private BaoXiaobillMapper baoXiaobillMapper;
	@Autowired
	private EmployeeMapper employeeMapper;
	
	//部署流程定义
	@Override
	public void deployProcess(String processName, InputStream input) {
		ZipInputStream zipInput = new ZipInputStream(input );
		Deployment deployment = repositoryService.createDeployment()									
									.name(processName)
									.addZipInputStream(zipInput )
									.deploy();
	}

	//查询部署信息
	@Override
	public List<Deployment> findDeploymentList() {
		List<Deployment> list = repositoryService.createDeploymentQuery().list();			
		return list;
	}

	//查询流程定义信息
	@Override
	public List<ProcessDefinition> findProcessDefinitionList() {
		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
		return list;
	}

	//保存报销表到数据库并启动流程实例
	@Override
	public void saveBaoXiaoAndStartProcess(BaoXiaobill bill, Employee employee) {
		//保存报销表
		
		bill.setCreatdate(new Date());
		bill.setState(1);
		int userId = Integer.parseInt(String.valueOf(employee.getId()));
		System.out.println(userId);
		bill.setUserId(userId);
		baoXiaobillMapper.insert(bill);		
		//启动实例
		//把用户表的数据保存到流程数据库中
		String key = Constants.LEAVEBILL_KEY;
		Map<String,Object> map = new HashMap<>();
		map.put("userId",employee.getName());
		String bussiness_key = key+"."+bill.getId();
		//this.runtimeService.startProcessInstanceByKey(key, map);
		this.runtimeService.startProcessInstanceByKey(key,bussiness_key ,map);
	}

	@Override
	public List<Task> findMyTaskListByUserId(String name) {		
		return this.taskService.createTaskQuery().taskAssignee(name).list();
	}

	
	@Override
	public BaoXiaobill findBillByTask(String taskId) {
		Task task = this.taskService.createTaskQuery().taskId(taskId).singleResult();
		ProcessInstance pi = this.runtimeService.createProcessInstanceQuery()
								.processInstanceId(task.getProcessInstanceId()).singleResult();
		
		String business_key = pi.getBusinessKey();
		System.out.println(business_key);
		String billId = business_key.split("\\.")[1];
		//Leavebill bill = leavebillMapper.selectByPrimaryKey(Long.parseLong(billId));
		BaoXiaobill bill = baoXiaobillMapper.selectByPrimaryKey(Integer.parseInt(billId));
		return bill;
	}
	//查看批注信息
	@Override
	public List<Comment> findCommentListByTask(String taskId) {
		Task task = this.taskService.createTaskQuery().taskId(taskId).singleResult();
		List<Comment> list = this.taskService.getProcessInstanceComments(task.getProcessInstanceId());
		return list;
	}
	//流程任务完成
	@Override
	public void submitTask(String taskId,String output,String name,Integer id,String comment) {
		Task task = this.taskService.createTaskQuery().taskId(taskId).singleResult();
		String processinstanceId = task.getProcessInstanceId();
		Authentication.setAuthenticatedUserId(name);
		//1.加批注
		this.taskService.addComment(taskId, processinstanceId, comment);
		//2.任务完成finish
		//利用name判断出当前角色		
		EmployeeExample example = new EmployeeExample();
		EmployeeExample.Criteria criteria = example.createCriteria();
		criteria.andNameEqualTo(name);
		List<Employee> list = this.employeeMapper.selectByExample(example);
		int role = list.get(0).getRole();
		
		//利用id查出该申请的金额,以及方便判断该实例是否结束
		BaoXiaobill bill = this.baoXiaobillMapper.selectByPrimaryKey(id);	
		int money = bill.getMoney().intValue();
		System.out.println(money);
		//int money = Integer.parseInt(bill.getMoney().toString());
		Map<String,Object> map = new HashMap<>();
		if(role==1){
			this.taskService.complete(taskId);
		}else if(role==2){
				if(output.equals("驳回")){
					map.put("message", output);					
					this.taskService.complete(taskId,map);
				}else{					
					map.put("message", output);					
					this.taskService.complete(taskId,map);
				}										
		}else if(role==3){
			map.put("message", output);
			this.taskService.complete(taskId,map);
		}else if(role==4){
			map.put("message", output);
			this.taskService.complete(taskId,map);
		}
		
		//this.taskService.complete(taskId);
		//3.判断流程实例是否结束，如果借宿，请假单的状态要改为"2"
		ProcessInstance pi = this.runtimeService.createProcessInstanceQuery()
								.processInstanceId(processinstanceId).singleResult();
		if(pi!=null){ //流程实例结束
			//Leavebill bill = this.leavebillMapper.selectByPrimaryKey(id);
			//BaoXiaobill bill = this.baoXiaobillMapper.selectByPrimaryKey(id);
			bill.setState(2);
			//̬修改请假单的状态
			//this.leavebillMapper.updateByPrimaryKey(bill);
			this.baoXiaobillMapper.updateByPrimaryKey(bill);
		}
	}

	@Override
	public InputStream findImageInputStream(String deploymentId, String imageName) {		
		return this.repositoryService.getResourceAsStream(deploymentId, imageName);
	}
	
	@Override
	public ProcessDefinition findProcessDefinitionByTaskId(String taskId) {
		//使用任务ID,查询任务对象
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		//获取流程定义ID
		String processDefinitionId = task.getProcessDefinitionId();
		//查询流程定义的对象
		ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
					.processDefinitionId(processDefinitionId)
					.singleResult();
		return pd;
	}

	@Override
	public Map<String, Object> findCoordingByTask(String taskId) {
		//存放坐标
		Map<String, Object> map = new HashMap<String,Object>();
		//使用任务ID,查询任务对象
		Task task = taskService.createTaskQuery()//
					.taskId(taskId)//ʹ使用任务ID查询
					.singleResult();
		//获取流程定义的ID
		String processDefinitionId = task.getProcessDefinitionId();
		//获取流程定义的实体对象(对应.bpmn文件中的数据)
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity)repositoryService.getProcessDefinition(processDefinitionId);
		//流程实例ID
		String processInstanceId = task.getProcessInstanceId();
		//使用流程实例ID，查询正在执行的执行对象表，获取当前活动对应的流程实例对象
		ProcessInstance pi = runtimeService.createProcessInstanceQuery()//创建流程实例查询
											.processInstanceId(processInstanceId)//使用流程实例ID查询
											.singleResult();
		//获取当前活动的ID
		String activityId = pi.getActivityId();
		//获取当前活动对象
		ActivityImpl activityImpl = processDefinitionEntity.findActivity(activityId);//活动ID
		//获取坐标
		map.put("x", activityImpl.getX());
		map.put("y", activityImpl.getY());
		map.put("width", activityImpl.getWidth());
		map.put("height", activityImpl.getHeight());
		return map;
	}

	
	//删除流程
	@Override
	public void removeProcessDef(String deploymentId) {
		repositoryService.deleteDeployment(deploymentId,true);
	}
	
	
	//查询流程线路
	@Override
	public List<String> findOutComeListByTask(String taskId) {
		
		List<String> list = new ArrayList<String>();
		//使用任务id查询任务对象
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		//获取流程定义id
		String processDefinitionId = task.getProcessDefinitionId();
		//查询processDefinitionEntiy对象
		ProcessDefinitionEntity processDefinitionEntity = 
				(ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinitionId);
		//使用对象Task获取流程实例ID
		String processInstanceId = task.getProcessInstanceId();
		ProcessInstance pi = this.runtimeService.createProcessInstanceQuery()
					.processInstanceId(processInstanceId)
					.singleResult();
		//获取当前活动的id
		String activityId = pi.getActivityId();
		ActivityImpl activityImpl = processDefinitionEntity.findActivity(activityId);
		List<PvmTransition> pvmList = activityImpl.getOutgoingTransitions();
		if(pvmList!=null && pvmList.size()>0){
			for (PvmTransition pvm : pvmList) {
				String name = (String) pvm.getProperty("name");
				if(StringUtil.isNotEmpty(name)){
					list.add(name);
				}else{
					list.add("默认提交");
				}
			}
		}
		return list;
	}

	//删除任务实例
	@Override
	public void removeTaskById(String taskId) {
		System.out.println(taskId);
		//this.taskService.deleteTask(taskId, true);
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		runtimeService.deleteProcessInstance(processInstanceId,"删除了");		
	}
	
	
	//我的报销单
	@Override
	public List<BaoXiaobill> findBaoxiaoBillListByUser(long id) {
		BaoXiaobillExample example = new BaoXiaobillExample();
		BaoXiaobillExample.Criteria criteria = example.createCriteria();		
		int userId = Integer.parseInt(String.valueOf(id));
		criteria.andUserIdEqualTo(userId);		
		return baoXiaobillMapper.selectByExample(example);
	}
	
	//查询报销单当前流程图
	@Override
	public Task findTaskByBussinessKey(String bUSSINESS_KEY) {					
			return this.taskService.createTaskQuery()
					.processInstanceBusinessKey(bUSSINESS_KEY).singleResult();
	}
	
	//根据id查找
	@Override
	public BaoXiaobill findBaoxiaoBillById(long id) {
		int i = Integer.parseInt(String.valueOf(id));		
		return baoXiaobillMapper.selectByPrimaryKey(i);
	}

	
	//根据id查找出批注
	@Override
	public List<Comment> findCommentByBaoxiaoBillId(long id) {
		String bussiness_key = Constants.LEAVEBILL_KEY +"."+id;
		HistoricProcessInstance pi = this.historyService.createHistoricProcessInstanceQuery()													.processInstanceBusinessKey(bussiness_key).singleResult();
		List<Comment> commentList = this.taskService.getProcessInstanceComments(pi.getId());		
		return commentList;
	}
	
	@RequestMapping("/leaveBillAction_delete")
	public String leaveBillAction_delete(Integer id){
		
		return "myBaoxiaoBill";
		
	}

	
	//根据id删除报销单
	@Override
	public void removeBaoxiaoBillById(Integer id) {
		baoXiaobillMapper.deleteByPrimaryKey(id);
	}
	
}
