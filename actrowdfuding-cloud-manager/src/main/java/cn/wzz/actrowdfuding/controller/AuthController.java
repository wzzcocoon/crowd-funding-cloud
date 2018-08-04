package cn.wzz.actrowdfuding.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.wzz.actrowdfuding.service.MemberService;
import cn.wzz.actrowdfuding.service.ProcessService;
import cn.wzz.actrowdfudingcommon.BaseController;
import cn.wzz.actrowdfudingcommon.bean.MemberCert;
import cn.wzz.actrowdfudingcommon.bean.Page;

@Controller
@RequestMapping("/auth")
public class AuthController extends BaseController{
	
	@Autowired
	private ProcessService processService;
	@Autowired
	private MemberService memberService;
	
	@RequestMapping("/index")
	public String index() {
		return "auth/index";
	}
	
	/**分页查询*/
	@ResponseBody
	@RequestMapping("/pageQuery")
	public Object pageQuery( Integer pageno, Integer pagesize ) {
		start();
		
		try {
			int count = processService.pageQueryTaskCount();
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("pageno", pageno);
			paramMap.put("pagesize", pagesize);
			
			List<Map<String, Object>> taskMaps = processService.pageQueryTaskData( paramMap );
			
			Page<Map<String, Object>> taskPage = new Page<Map<String, Object>>();
			taskPage.setTotalsize(count);
			taskPage.setPageno(pageno);
			taskPage.setPagesize(pagesize);
			taskPage.setDatas(taskMaps);
			
			//把分页对象传给页面
			putData(taskPage);
			success();
		} catch ( Exception e ) {
			e.printStackTrace();
			fail();
		}
		
		return end();
	}
	

	/**查询会员上传的资质文件,转到页面展示*/
	@RequestMapping("/detail")
	public String detail(String taskid,Integer memberid,Model model) {
		List<MemberCert> mcs = memberService.queryMemberCertsByMemberid(memberid);
		model.addAttribute("mcs", mcs);
		//用来进行完成审核功能
		model.addAttribute("memberid", memberid);
		model.addAttribute("taskid", taskid);
		return "auth/detail";
	}
	
	/**完成审核*/
	@ResponseBody
	@RequestMapping("/ok")
	public Object ok(String taskid,Integer memberid) {
		start();
		try {
			//不能通过session获取会员信息。因为现在登录的是后台管理用户
			//session.getAttribute("");
			
			//更新会员的实名认证状态
			Map<String,Object> varMap = new HashMap<String,Object>();
			varMap.put("memberid", memberid);
			varMap.put("taskid", taskid);
			memberService.finishAuth(varMap);
			
			success();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		return end();
	}
}
