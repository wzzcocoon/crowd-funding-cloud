package cn.wzz.actrowdfuding.service;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.wzz.actrowdfudingcommon.bean.Cert;
import cn.wzz.actrowdfudingcommon.bean.Member;
import cn.wzz.actrowdfudingcommon.bean.MemberCert;
import cn.wzz.actrowdfudingcommon.bean.Ticket;

@FeignClient("eureka-member-service")
public interface MemberService {
   

	@RequestMapping("/login/{loginacct}")
	public Member login(@PathVariable("loginacct") String loginacct);

	@RequestMapping("/queryTicketByMemberid/{id}")
	public Ticket queryTicketByMemberid(@PathVariable("id")Integer id);

	@RequestMapping("/insertTicket")
	public void insertTicket(@RequestBody Ticket t);

	@RequestMapping("/updateAcctoutType")
	public void updateAcctoutType(@RequestBody Member loginMember);

	@RequestMapping("/updateBasicinfo")
	public void updateBasicinfo(@RequestBody Member loginMember);

	@RequestMapping("/queryCertByAccountType/{accttype}")
	public List<Cert> queryCertByAccountType(@PathVariable("accttype")String accttype);

	@RequestMapping("/insertMemberCerts")
	public void insertMemberCerts(@RequestBody List<MemberCert> mcs);

	@RequestMapping("/updateEmail")
	public void updateEmail(@RequestBody Member loginMember);

	@RequestMapping("/updateAuthstatus")
	public void updateAuthstatus(@RequestBody Member loginMember);
	
}
