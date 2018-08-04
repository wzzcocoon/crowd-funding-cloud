package cn.wzz.actrowdfuding.controller;

import java.io.File;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.wzz.actrowdfuding.service.ActService;
import cn.wzz.actrowdfuding.service.MemberService;
import cn.wzz.actrowdfudingcommon.BaseController;
import cn.wzz.actrowdfudingcommon.bean.Cert;
import cn.wzz.actrowdfudingcommon.bean.Datas;
import cn.wzz.actrowdfudingcommon.bean.Member;
import cn.wzz.actrowdfudingcommon.bean.MemberCert;
import cn.wzz.actrowdfudingcommon.bean.Ticket;
import cn.wzz.actrowdfudingcommon.constant.AttrConst;

@Controller
@RequestMapping("/member")
public class MemberController extends BaseController{

	@Autowired
	private MemberService memberService;
	@Autowired
	private ActService actService;
	
	@RequestMapping("/apply")
	public String apply(HttpSession session,Model model) {
		
		//获取当前的会员信息
		Member loginMember = (Member) session.getAttribute(AttrConst.SESSION_MEMBER);
		
		//查询会员的流程审批单
		Ticket t = memberService.queryTicketByMemberid(loginMember.getId());
		
		if(t == null) {
			//启动流程，获取流程实例id
			String piid = actService.startProcessInstance(loginMember.getLoginacct());
			
			//第一次申请完成，需要改变账户的状态
			t = new Ticket(); 
			t.setMemberid(loginMember.getId());
			t.setPstep("accttype");
			t.setPiid(piid);
			t.setStatus("0");
			
			memberService.insertTicket(t);
			//第一次申请时，跳转到账户类型选择页面
			return "member/apply-accttype-select";
		}else {
			//根据流程步骤跳转页面
			String step = t.getPstep();
			if("accttype".equals(step)) {
				return "member/apply-accttype-select";
			}else if("basicinfo".equals(step)) {
				return "member/apply-basic-info";
			}else if("cert".equals(step)) {
				//查询当前会员需要上传的资质文件列表
				List<Cert> certs = 
						memberService.queryCertByAccountType(loginMember.getAccttype());
				model.addAttribute("certs", certs);
				return "member/apply-cert-upload";
			}else if("email".equals(step)) {
				return "member/apply-email";
			}else if("checkcode".equals(step)) {
				return "member/apply-check-code";
			}else {
				return "member/apply-accttype-select";
			}
		}
	}
	
	/**更新账户类型*/
	@ResponseBody
	@RequestMapping("/updateAcctoutType")
	public Object updateAcctoutType(HttpSession session,Member member) {
		start();
		try {
			//获取登录会员
			Member loginMember = (Member) session.getAttribute(AttrConst.SESSION_MEMBER);
			//member.setId(loginMember.getId());
			//memberService.updateAcctoutType(member);
			loginMember.setAccttype(member.getAccttype());
			//更新会员的账户类型
			memberService.updateAcctoutType(loginMember);
			//若不是分布式环境，不需要这一步。但是分布式环境中，session数据是保存在缓存服务器中
			//所以更新session中的数据以后，应该显示的调用setAttribute方法
			//作用是 将缓存服务器中的数据也同时更新
			session.setAttribute(AttrConst.SESSION_MEMBER, loginMember);
			
			success();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		return end();
	}

	/**更新会员基本信息*/
	@ResponseBody
	@RequestMapping("/updateBasicinfo")
	public Object updateBasicinfo(HttpSession session,Member member) {
		start();
		try {
			Member loginMember = (Member) session.getAttribute(AttrConst.SESSION_MEMBER);
			loginMember.setRealname(member.getRealname());
			loginMember.setTel(member.getTel());
			loginMember.setCardnum(member.getCardnum());
			
			memberService.updateBasicinfo(loginMember);
			
			session.setAttribute(AttrConst.SESSION_MEMBER, loginMember);
			
			success();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		return end();
	}
	
	/**上传所有的资质文件(图片)*/
	@ResponseBody
	@RequestMapping("/uploadCerts")
	public Object uploadCerts(Datas ds,HttpSession session) {
		start();
		try {
			//获取登录会员信息
			Member loginMember = (Member) session.getAttribute(AttrConst.SESSION_MEMBER);
			//获取传递过来的资质文件集合
			List<MemberCert> mcs = ds.getMcs();
			for (MemberCert mc : mcs) {
				mc.setMemberid(loginMember.getId());
				
				//获取传来的文件
				MultipartFile file = mc.getFile();
				String filename = file.getOriginalFilename();
				String suffix = filename.substring(filename.lastIndexOf("."));
				String uuidStr = UUID.randomUUID().toString();
				//保存图片(放到路径下)
				File destFile = 
						new File("H:\\atguigu-atcrowdfunding\\04_atcrowdfunding-docs\\代码\\尚筹网页面原型\\img\\down\\"+ uuidStr + suffix);
				file.transferTo(destFile);
				
				mc.setIconpath(uuidStr + suffix);
				//两个服务器之间传递对象是以feign的方式传递，
				//但是文件是以RestTemplate方式传递的，不置为null会报错
				mc.setFile(null);
			}
			memberService.insertMemberCerts(mcs);
			success();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		return end();
	}
	
	/**发送邮件*/
	@ResponseBody
	@RequestMapping("/sendMail")
	public Object sendMail(String email,HttpSession session) {
		start();
		try {
			//获取登录会员信息
			Member loginMember = (Member) session.getAttribute(AttrConst.SESSION_MEMBER);
			loginMember.setEmail(email);
			session.setAttribute(AttrConst.SESSION_MEMBER, loginMember);
			//更新会员的邮箱地址并且发送邮件
			memberService.updateEmail(loginMember);
			success();
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
		return end();
	}
	
	/**完成申请*/
	@ResponseBody
	@RequestMapping("/finishApply")
	public Object finishApply(String authcode,HttpSession session) {
		start();
		try {
			//获取会员信息
			Member loginMember = (Member) session.getAttribute(AttrConst.SESSION_MEMBER);
			//获取数据库中的验证码
			Ticket t = memberService.queryTicketByMemberid(loginMember.getId());
			//判断验证码是否正确
			if(authcode.equals(t.getAuthcode())) {
				//更新会员的实名认证状态
				loginMember.setAuthstatus("1");
				session.setAttribute(AttrConst.SESSION_MEMBER, loginMember);
				memberService.updateAuthstatus(loginMember);
				success();
			} else {
				fail();
			} 
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		return end();
	}
}
