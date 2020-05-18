package com.web.workflow.controller;

import java.io.IOException;




import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.web.workflow.pojo.ActiveUser;
import com.web.workflow.pojo.BaoXiaobill;
import com.web.workflow.pojo.Employee;
import com.web.workflow.pojo.EmployeeCustom;
import com.web.workflow.pojo.Leavebill;
import com.web.workflow.pojo.SysRole;
import com.web.workflow.service.EmployeeService;
import com.web.workflow.service.SysService;
import com.web.workflow.service.WorkFlowService;
import com.web.workflow.utils.Constants;

@Controller
public class WorkFlowController {
	@Autowired
	private WorkFlowService workFlowService;
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private SysService sysService;
	
	
	
	private static final int PAGE_SIZE = 10;
	
	//部署流程
	@RequestMapping("/deployProcess")
	public String deployProcess(String processName,MultipartFile fileName){		
		try {
			workFlowService.deployProcess(processName, fileName.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "redirect:/processDefinitionList";		
	}
	
	//查询流程定义和部署信息
	@RequestMapping("/processDefinitionList")
	public ModelAndView processDefinitionList(){
		List<Deployment> deploymentList = this.workFlowService.findDeploymentList();
		List<ProcessDefinition> findProcessDefinitionList = this.workFlowService.findProcessDefinitionList();
		ModelAndView mv = new ModelAndView();
		mv.addObject("depList", deploymentList);
		mv.addObject("pdList", findProcessDefinitionList);
		mv.setViewName("workflow_list");
		return mv;
		
	}
	
	//提交申请表单，启动流程实例
	@RequestMapping("/saveStartLeave")
	public String saveStartLeave(BaoXiaobill bill,HttpSession session){
		ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
		
		Employee employee = employeeService.findEmpById(activeUser.getId());
		//Employee employee = (Employee) session.getAttribute(Constants.GLOBAL_SESSION_ID);
		workFlowService.saveBaoXiaoAndStartProcess(bill,employee); 
		return "redirect:/myTaskList";
		
	}
	
	//我的待办事务
	@RequestMapping("/myTaskList")
	public ModelAndView myTaskList(HttpSession session,
			@RequestParam(value="pageNum",required=false,defaultValue="1") int pageNum
			){
		
		ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
		System.out.println(activeUser.getUsername());
		PageHelper.startPage(pageNum,PAGE_SIZE);
		List<Task> list = workFlowService.findMyTaskListByUserId(activeUser.getUsername()); 						
		
		//分页插件		
		PageInfo<Task> pageInfo = new PageInfo<>(list);
		System.out.println(pageInfo.getPages());
		ModelAndView mv = new ModelAndView();		
		mv.addObject("taskList",list);
		mv.addObject("pageInfo",pageInfo);
		mv.setViewName("workflow_task");
		return mv;		
	}
	
	//删除任务实例
	@RequestMapping("/deleteTaskForm")
	public String deleteTaskForm(String taskId){
		workFlowService.removeTaskById(taskId);
		return "redirect:/myTaskList";
		
	}
	
	
	//办理任务：跳转到任务页面
	@RequestMapping("/viewTaskForm")
	public ModelAndView viewTaskForm(String taskId){
		//根据流程的任务id查询对应报销单的信息
		BaoXiaobill bill = this.workFlowService.findBillByTask(taskId);
		//查流程的批注信息
		List<Comment> commentlist = this.workFlowService.findCommentListByTask(taskId);
		//查流程的线路名称，便于生成动态按钮
		List<String> ouputList = this.workFlowService.findOutComeListByTask(taskId);
		
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("bill", bill);
		mv.addObject("commentList", commentlist);
		mv.addObject("taskId", taskId);
		mv.addObject("ouputList", ouputList);
		mv.setViewName("approve_leave");
		return mv;
		
	}
	
	//流程任务完成，流程推进
	@RequestMapping("/submitTask")
	public String submitTask(Integer id,String comment,String taskId,String output,HttpSession session){
		System.out.println(output);
		session.setAttribute("message", output);
		//Employee employee = (Employee) session.getAttribute(Constants.GLOBAL_SESSION_ID);
		ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
		System.out.println(activeUser.getUsername());
		//List<Task> list = workFlowService.findMyTaskListByUserId(activeUser.getUsername()); 
		Employee employee = employeeService.findEmpById(activeUser.getId());
		this.workFlowService.submitTask(taskId,output,employee.getName(),id,comment);
		return "redirect:/myTaskList";
		
	}
	
	@RequestMapping("/viewImage")
	public String viewImage(String deploymentId,String imageName,HttpServletResponse response) throws IOException{
		InputStream in = workFlowService.findImageInputStream(deploymentId,imageName);
		OutputStream out = response.getOutputStream();
		for(int b=-1;(b=in.read())!=-1;){
			out.write(b);
		}
		out.close();
		in.close();
		return null;		
	}
	
	@RequestMapping("/viewCurrentImage")
	public ModelAndView viewCurrentImage(String taskId){
		
		ProcessDefinition pd = workFlowService.findProcessDefinitionByTaskId(taskId);
		ModelAndView mv = new ModelAndView();
		mv.addObject("deploymentId", pd.getDeploymentId());
		mv.addObject("imageName", pd.getDiagramResourceName());
		Map<String,Object> map = workFlowService.findCoordingByTask(taskId);
		mv.addObject("acs", map);
		mv.setViewName("viewimage");
		return mv;
		
	}
	
	//删除流程
	@RequestMapping("/delDeployment")
	public String delDeployment(String deploymentId){
		System.out.println(deploymentId);
		workFlowService.removeProcessDef(deploymentId);
		return "redirect:/myTaskList";
		
	}
	
	//我的报销单,包含分页功能
	@RequestMapping("/myBaoxiaoBill")
		public String home(ModelMap model,HttpSession session,
				@RequestParam(value="pageNum",required=false,defaultValue="1") int pageNum){
			//1：查询所有的请假信息（对应a_leavebill），返回List<LeaveBill>
			//@ModelAttribute("bill") BaoXiaobill bill
			//Employee emp = (Employee) session.getAttribute(Constants.GLOBLE_USER_SESSION);
			ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
			PageHelper.startPage(pageNum, PAGE_SIZE);
			List<BaoXiaobill> list = workFlowService.findBaoxiaoBillListByUser(activeUser.getId()); 
			
			PageInfo<BaoXiaobill> pageInfo = new PageInfo<>(list);
			model.addAttribute("pageInfo", pageInfo);
			//放置到上下文对象中
			model.addAttribute("baoxiaoList", list);
			return "baoxiaobill";
		}
		
		
		
		//查询出所有的用户
		@RequestMapping("/findUserList")
		public ModelAndView findUserList(String userId) {
			ModelAndView mv = new ModelAndView();
			List<SysRole> allRoles = sysService.findAllRoles();
			List<EmployeeCustom> list = employeeService.findUserAndRoleList();
			
			mv.addObject("userList", list);
			mv.addObject("allRoles", allRoles);
			
			mv.setViewName("userlist");
			return mv;
		}
		
		//重新分配用户权限
		@RequestMapping("/assignRole")
		@ResponseBody
		public Map<String, String> assignRole(String roleId,String userId) {
			Map<String, String> map = new HashMap<>(); 
			try {
				employeeService.updateEmployeeRole(roleId, userId);
				map.put("msg", "分配权限成功");
			} catch (Exception e) {
				e.printStackTrace();
				map.put("msg", "分配权限失败");
			}
			return map;
		}
	
		
		//查询出用户权限
		@RequestMapping("/viewPermissionByUser")
		@ResponseBody
		public SysRole viewPermissionByUser(String userName) {
			SysRole sysRole = sysService.findRolesAndPermissionsByUserId(userName);

			System.out.println(sysRole.getName()+"," +sysRole.getPermissionList());
			return sysRole;
		}
		
		//查看流程图
		@RequestMapping("/viewCurrentImageByBill")
		public String viewCurrentImageByBill(long billId,ModelMap model) {
			String BUSSINESS_KEY = Constants.LEAVEBILL_KEY + "." + billId;
			System.out.println(BUSSINESS_KEY);
			Task task = this.workFlowService.findTaskByBussinessKey(BUSSINESS_KEY);
			/**一：查看流程图*/
			//1：获取任务ID，获取任务对象，使用任务对象获取流程定义ID，查询流程定义对象
			ProcessDefinition pd = workFlowService.findProcessDefinitionByTaskId(task.getId());

			model.addAttribute("deploymentId", pd.getDeploymentId());
			model.addAttribute("imageName", pd.getDiagramResourceName());
			/**二：查看当前活动，获取当期活动对应的坐标x,y,width,height，将4个值存放到Map<String,Object>中*/
			Map<String, Object> map = workFlowService.findCoordingByTask(task.getId());

			model.addAttribute("acs", map);
			return "viewimage";
		}
		
		// 查看历史的批注信息
		@RequestMapping("/viewHisComment")
		public String viewHisComment(long id,ModelMap model){
			//1：使用报销单ID，查询报销单对象
			BaoXiaobill bill = workFlowService.findBaoxiaoBillById(id);
			model.addAttribute("baoxiaoBill", bill);
			//2：使用请假单ID，查询历史的批注信息
			List<Comment> commentList = workFlowService.findCommentByBaoxiaoBillId(id);
			model.addAttribute("commentList", commentList);		
			return "workflow_commentlist";
		}
		
		
		@RequestMapping("/leaveBillAction_delete")
		public String leaveBillAction_delete(Integer id){
			System.out.println(id);
			workFlowService.removeBaoxiaoBillById(id);
			return "redirect:/myBaoxiaoBill";			
		}
}
