package cn.wzz.actrowdfudingService.service;

import java.util.List;

import cn.wzz.actrowdfudingcommon.bean.Cert;
import cn.wzz.actrowdfudingcommon.bean.Member;
import cn.wzz.actrowdfudingcommon.bean.MemberCert;
import cn.wzz.actrowdfudingcommon.bean.Ticket;

public interface MemberService {

	Member queryMemberByLoginacct(String loginacct);

	Ticket queryTicketByMemberid(Integer id);

	void insertTicket(Ticket t);

	void updateAcctoutType(Member loginMember);

	void updateStep(Ticket t);

	void updateBasicinfo(Member loginMember);

	List<Cert> queryCertByAccountType(String accttype);

	Member queryById(Integer memberid);

	void insertMemberCerts(List<MemberCert> mcs);

	void updateEmail(Member loginMember);

	void updateStepAndAuthCode(Ticket t);

	void updateAuthstatus(Member loginMember);

	Member queryMemberByPiid(String piid);

	List<MemberCert> queryMemberCertsByMemberid(Integer memberid);

	void updateTicketStatus(Ticket t);


}
