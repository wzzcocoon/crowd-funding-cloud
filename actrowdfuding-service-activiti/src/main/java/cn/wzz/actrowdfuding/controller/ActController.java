package cn.wzz.actrowdfuding.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.wzz.actrowdfuding.service.MemberService;
import cn.wzz.actrowdfudingcommon.bean.Member;

@RestController
public class ActController {
	
	@Autowired
	private MemberService memberService;
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	
	/**分页查询流程定义数据*/
	@RequestMapping("/pageQueryProcDefData")
	public List<Map<String,Object>> pageQueryProcDefData(@RequestBody Map<String,Object> paramMap){
		ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();
		
		int pageno = (Integer)paramMap.get("pageno");
		int pagesize = (Integer)paramMap.get("pagesize");
		List<ProcessDefinition> pds = query.listPage((pageno-1)*pagesize,pagesize);
		
		//由于直接返回List<ProcessDefinition>时，json深复制会造成异常
		List<Map<String,Object>> pdMapList = new ArrayList<Map<String,Object>>();
		for(ProcessDefinition pd : pds) {
			Map<String,Object> pdMap = new HashMap<String,Object>();
			pdMap.put("id", pd.getId());
			pdMap.put("name", pd.getName());
			pdMap.put("key", pd.getKey());
			pdMap.put("version", pd.getVersion());
			pdMapList.add(pdMap);
		}
		return pdMapList;
	}
	
	
	@RequestMapping("/pageQueryProcDefCount")
	public int pageQueryProcDefCount() {
		ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();
		return (int) query.count();
	}
	
	
	/**上传文件-增加对应的请求参数接收文件*/
	@RequestMapping("/depolyProcDef")
	public String depolyProcDef( @RequestParam("pdfile") MultipartFile file ) {
		
		//部署流程定义
		try {
			repositoryService
				.createDeployment()
				//这个方法需要在该项目的classpath下存在该文件....
				//.addClasspathResource(file.getOriginalFilename())
				.addInputStream(file.getOriginalFilename(), file.getInputStream())
				.deploy();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "success";
	}

	
	/**展示图片-在Web服务的处理方法中返回字节数组。逻辑中将文件转换为流后再转换为字节数组即可*/
	@RequestMapping("/loadImgById/{id}")
	public byte[] loadImgById(@PathVariable("id") String id) {
		//查询流程定义的对象
		ProcessDefinitionQuery query =
				repositoryService.createProcessDefinitionQuery();
		ProcessDefinition pd = 
				query.processDefinitionId(id).singleResult();
		String imgName = pd.getDiagramResourceName();
		String deploymentId = pd.getDeploymentId();
		//读取流程定义的图像
		InputStream in = 
				repositoryService.getResourceAsStream(deploymentId, imgName);
		
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream(); 
		byte[] buff = new byte[100]; //buff用于存放循环读取的临时数据 
		int rc = 0; 
		try {
			while ((rc = in.read(buff, 0, 100)) > 0) { 
			    swapStream.write(buff, 0, rc); 
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
		byte[] in_b = swapStream.toByteArray(); //in_b为转换之后的结果
		
		return in_b;
	}

	/**根据流程定义的id删除一个流程*/
	@RequestMapping("/delete/{id}")
	public void delete(@PathVariable("id") String id) {
		//获取流程定义的id
		ProcessDefinitionQuery query =
				repositoryService.createProcessDefinitionQuery();
		ProcessDefinition pd = 
				query.processDefinitionId(id).singleResult();
		String deploymentId = pd.getDeploymentId();

		//删除流程定义(级联删除)
		repositoryService.deleteDeployment(deploymentId, true);
	}
	
	/**启动流程实例*/
	@RequestMapping("/startProcessInstance/{loginacct}")
	public String startProcessInstance(@PathVariable("loginacct")String loginacct) {
		ProcessDefinitionQuery query = 
				repositoryService.createProcessDefinitionQuery();
		//查询实名认证的流程定义
		ProcessDefinition pd = query
			.processDefinitionKey("authflow")
			.latestVersion()
			.singleResult();
		//实名认证流程定义的id
		String pdid = pd.getId();
		Map<String,Object> varMap = new HashMap<String,Object>();
		varMap.put("loginacct", loginacct);
		//根据流程定义的id启动流程实例(需要注意是否有变量)
		ProcessInstance pi = runtimeService.startProcessInstanceById(pdid,varMap);
		return pi.getId();
	}
	
	/**让流程定义继续执行*/
	@RequestMapping("/process/{loginacct}")
	public void process(@PathVariable("loginacct")String loginacct) {
		//查询任务
		TaskQuery query = taskService.createTaskQuery();
		List<Task> tasks = query
				.processDefinitionKey("authflow")
				.taskAssignee(loginacct)
				.list();
		//完成任务
		for(Task task : tasks) {
			taskService.complete(task.getId());
		}
	}
	
	/**让流程定义继续执行（有网关）*/
	@RequestMapping("/nextProcess/{loginacct}")
	public void nextProcess(@PathVariable("loginacct")String loginacct) {
		//查询任务
		TaskQuery query = taskService.createTaskQuery();
		List<Task> tasks = query
				.processDefinitionKey("authflow")
				.taskAssignee(loginacct)
				.list();
		Map<String,Object> varMap = new HashMap<String,Object>(); 
		varMap.put("status", "next");
		//完成任务
		for(Task task : tasks) {
			taskService.complete(task.getId(),varMap);
		}
	}

	/**发送邮件,返回验证码。让流程定义继续执行*/
	@RequestMapping("/sendEmail")
	public String sendEmail(@RequestBody Member loginMember) {
		TaskQuery query = taskService.createTaskQuery();
		List<Task> tasks = query
				.processDefinitionKey("authflow")
				.taskAssignee(loginMember.getLoginacct())
				.list();
		
		Map<String,Object> varMap = new HashMap<String,Object>(); 
		varMap.put("userEmail", loginMember.getEmail());
		//生成随机验证码
		StringBuilder builder = new StringBuilder();
		for(int i = 0 ; i < 4 ; i++) {
			builder.append(new Random().nextInt(10));
		}
		String authcode = builder.toString();
		varMap.put("authcode", authcode);
		
		//完成任务
		for(Task task : tasks) {
			taskService.complete(task.getId(),varMap);
		}
		//更新验证码
		return authcode;
	}
	
	/**分页查询实名申请任务数据*/
	@RequestMapping("/pageQueryTaskData")
	public List<Map<String, Object>> pageQueryTaskData( @RequestBody Map<String, Object> paramMap ) {
		
		TaskQuery query = taskService.createTaskQuery();
		
		int pageno = (Integer)paramMap.get("pageno");
		int pagesize = (Integer)paramMap.get("pagesize");
		//查询实名认证的任务(让manager组审核的（已经处于待审核状态的会员）)
		List<Task> tasks =
			query
			    .processDefinitionKey("authflow")
			    .taskCandidateGroup("manager")
			    .listPage((pageno-1)*pagesize, pagesize);
		
		//由于直接返回List<Task>时，json深复制会造成异常
		List<Map<String, Object>> taskMapList = new ArrayList<Map<String, Object>>();
		
		for ( Task task : tasks ) {
			Map<String, Object> taskMap = new HashMap<String, Object>();
			taskMap.put("id", task.getId());
			taskMap.put("name", task.getName());
			
			// task ==> pi ==> pd
			String pdid = task.getProcessDefinitionId();
			
			ProcessDefinitionQuery pdQuery =
			  repositoryService
			    .createProcessDefinitionQuery();
			ProcessDefinition pd =
				pdQuery.processDefinitionId(pdid).singleResult();
			
			taskMap.put("pdname", pd.getName());
			taskMap.put("pdversion", pd.getVersion());
			
			// task ==> pi ==> member
			String piid = task.getProcessInstanceId();
			Member member = memberService.queryMemberByPiid(piid);
			if(member != null) {
				taskMap.put("memberid", member.getId());
				taskMap.put("membername", member.getMembername());
				taskMapList.add(taskMap);
			}
		}
		return taskMapList;
	}
	
	@RequestMapping("/pageQueryTaskCount")
	public int pageQueryTaskCount() {
		TaskQuery query = taskService.createTaskQuery();
		int count = (int)query
			    .processDefinitionKey("authflow")
			    .taskCandidateGroup("manager")
			    .count();
	    return count;
	}
	
	/**让流程定义继续执行（结束）*/
	@RequestMapping("/endProcess/{taskid}")
	public void endProcess(@PathVariable("taskid")String taskid) {
		Map<String,Object> varMap = new HashMap<String,Object>(); 
		varMap.put("status", "pass");
		//完成任务
		taskService.complete(taskid,varMap);
	}

}

