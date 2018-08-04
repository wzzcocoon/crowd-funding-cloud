package cn.wzz.actrowdfudingService.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.wzz.actrowdfudingService.service.ActService;
import cn.wzz.actrowdfudingService.service.MemberService;
import cn.wzz.actrowdfudingcommon.bean.Cert;
import cn.wzz.actrowdfudingcommon.bean.Member;
import cn.wzz.actrowdfudingcommon.bean.MemberCert;
import cn.wzz.actrowdfudingcommon.bean.Ticket;

@RestController
public class MemberController {

	@Autowired
	private MemberService memberService;
	@Autowired
	private ActService actService;
	
	/**根据登录账号查找一个会员*/
	@RequestMapping("/login/{loginacct}")
	public Object login(@PathVariable("loginacct") String loginacct) {
		Member member = memberService.queryMemberByLoginacct(loginacct);
		return member;
	}
	
	/**根据会员id查找流程审批单(状态)*/
	@RequestMapping("/queryTicketByMemberid/{id}")
	public Ticket queryTicketByMemberid(@PathVariable("id")Integer id) {
		Ticket t = memberService.queryTicketByMemberid(id);
		return t;
	}
	
	/**插入一个流程审批单*/
	@RequestMapping("/insertTicket")
	public void insertTicket(@RequestBody Ticket t) {
		memberService.insertTicket(t);
	}

	/**更新账户类型*/
	@RequestMapping("/updateAcctoutType")
	public void updateAcctoutType(@RequestBody Member loginMember) {
		//更新会员账户类型
		memberService.updateAcctoutType(loginMember);
		//更新流程步骤
		Ticket t  = memberService.queryTicketByMemberid(loginMember.getId());
		t.setPstep("basicinfo");
		memberService.updateStep(t);
		//流程继续执行
		actService.process(loginMember.getLoginacct());
	}
	
	/**更新基本信息*/
	@RequestMapping("/updateBasicinfo")
	public void updateBasicinfo(@RequestBody Member loginMember) {
		//更新会员基本信息
		memberService.updateBasicinfo(loginMember);
		//更新流程步骤
		Ticket t  = memberService.queryTicketByMemberid(loginMember.getId());
		t.setPstep("cert");
		memberService.updateStep(t);
		//流程继续执行
		actService.nextProcess(loginMember.getLoginacct());
	}
	
	/**查询该会员需要上传的资质内容*/
	@RequestMapping("/queryCertByAccountType/{accttype}")
	public List<Cert> queryCertByAccountType(@PathVariable("accttype")String accttype){
		return memberService.queryCertByAccountType(accttype);
	}
	
	/**插入会员上传的资质文件信息*/
	@RequestMapping("/insertMemberCerts")
	public void insertMemberCerts(@RequestBody List<MemberCert> mcs) {
		//增加会员证明文件数据
		memberService.insertMemberCerts(mcs);

		//更新流程步骤
		Integer memberid = mcs.get(0).getMemberid();
		Ticket t  = memberService.queryTicketByMemberid(memberid);
		t.setPstep("email");
		memberService.updateStep(t);
		//流程继续执行
		Member loginMember = memberService.queryById(memberid);
		actService.nextProcess(loginMember.getLoginacct());
	}
	
	/**更新会员的邮箱地址并且发送邮件*/
	@RequestMapping("/updateEmail")
	public void updateEmail(@RequestBody Member loginMember) {
		//更新邮箱地址
		memberService.updateEmail(loginMember);

		//发送邮件,返回验证码。让流程定义继续执行
		String authcode = actService.sendEmail(loginMember);
		
		Ticket t  = memberService.queryTicketByMemberid(loginMember.getId());
		t.setPstep("checkcode");
		//保存验证码
		t.setAuthcode(authcode);
		//更新流程步骤和验证码
		memberService.updateStepAndAuthCode(t);

	}
	
	/**更新会员的实名认证状态*/
	@RequestMapping("/updateAuthstatus")
	public void updateAuthstatus(@RequestBody Member loginMember) {
		//更新会员的实名认证状态
		memberService.updateAuthstatus(loginMember);
		//让流程继续执行
		actService.process(loginMember.getLoginacct());
	}
	
	/**acticiti服务过来的，用来查询会员信息*/
	@RequestMapping("/queryMemberByPiid/{piid}")
	public Member queryMemberByPiid(@PathVariable("piid")String piid) {
		return memberService.queryMemberByPiid(piid);
	}
	
	/**查询会员上传的资质文件*/
	@RequestMapping("queryMemberCertsByMemberid/{memberid}")
	List<MemberCert> queryMemberCertsByMemberid(@PathVariable("memberid")Integer memberid){
		return memberService.queryMemberCertsByMemberid(memberid);
	}
	
	/**完成实名认证流程*/
	@RequestMapping("finishAuth")
	void finishAuth(@RequestBody Map<String, Object> varMap) {
		//更新会员的实名认证状态
		Integer memberid = (Integer) varMap.get("memberid");
		Member member = memberService.queryById(memberid);
		member.setAuthstatus("2");
		memberService.updateAuthstatus(member);
		//让流程继续执行
		String taskid = (String) varMap.get("taskid");
		actService.endProcess(taskid);
		//更新流程审批单的状态
		Ticket t = memberService.queryTicketByMemberid(memberid);
		t.setStatus("1");
		memberService.updateTicketStatus(t);
	}
	
}
