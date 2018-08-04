package cn.wzz.actrowdfuding.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.wzz.actrowdfuding.service.MemberService;
import cn.wzz.actrowdfudingcommon.BaseController;
import cn.wzz.actrowdfudingcommon.bean.Member;
import cn.wzz.actrowdfudingcommon.util.MD5Util;

@Controller
public class DispatcherController extends BaseController{

	@Autowired
	private MemberService memberService;
	
	@RequestMapping("/login")
	public String loginPage() {
		return "login";
	}

	@RequestMapping("/main")
	public String mainPage() {
		return "main";
	}

	//@HystrixCommand(fallbackMethod="服务失败时调用的方法名") 使用时，方法必须完全一致--熔断器
	//@HystrixCommand(fallbackMethod="checkLoginError")
	@ResponseBody
	@RequestMapping("/checkLogin")
	public Object checkLogin(Member member,HttpSession session) {
		start();
		try {
			//查询会员信息
			Member dbMember = memberService.login(member.getLoginacct());
			if(dbMember == null) {
				fail();
			}else {
				//判断密码是否正确
				if(dbMember.getMemberpswd().equals(MD5Util.digest(member.getMemberpswd()))) {
					session.setAttribute("loginMember", dbMember);
					success();
				}else {
					fail();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		return end();
	}
//	@ResponseBody
//	@RequestMapping("/checkLogin")
//	public Object checkLoginError(Member member) {
//		start();
//		fail();
//		return end();
//	}
}
