package cn.wzz.actrowdfudingService.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.wzz.actrowdfudingService.dao.MemberDao;
import cn.wzz.actrowdfudingService.service.MemberService;
import cn.wzz.actrowdfudingcommon.bean.Cert;
import cn.wzz.actrowdfudingcommon.bean.Member;
import cn.wzz.actrowdfudingcommon.bean.MemberCert;
import cn.wzz.actrowdfudingcommon.bean.Ticket;

@Service
@Transactional(readOnly=true)
public class MemberServiceImpl implements MemberService {

	@Autowired
	private MemberDao memberDao;

	@Override
	public Member queryMemberByLoginacct(String loginacct) {
		return memberDao.queryMemberByLoginacct(loginacct);
	}

	@Override
	public Ticket queryTicketByMemberid(Integer id) {
		return memberDao.queryTicketByMemberid(id);
	}

	@Override
	@Transactional
	public void insertTicket(Ticket t) {
		memberDao.insertTicket(t);
	}

	@Override
	@Transactional
	public void updateAcctoutType(Member loginMember) {
		memberDao.updateAcctoutType(loginMember);
	}

	@Override
	@Transactional
	public void updateStep(Ticket t) {
		memberDao.updateStep(t);
	}

	@Override
	@Transactional
	public void updateBasicinfo(Member loginMember) {
		memberDao.updateBasicinfo(loginMember);
	}

	@Override
	public List<Cert> queryCertByAccountType(String accttype) {
		return memberDao.queryCertByAccountType(accttype);
	}

	@Override
	public Member queryById(Integer memberid) {
		return memberDao.queryById(memberid);
	}

	@Override
	@Transactional
	public void insertMemberCerts(List<MemberCert> mcs) {
		memberDao.insertMemberCerts(mcs);
	}

	@Override
	@Transactional
	public void updateEmail(Member loginMember) {
		memberDao.updateEmail(loginMember);
	}

	@Override
	@Transactional
	public void updateStepAndAuthCode(Ticket t) {
		memberDao.updateStepAndAuthCode(t);
	}

	@Override
	@Transactional
	public void updateAuthstatus(Member loginMember) {
		memberDao.updateAuthstatus(loginMember);
	}

	@Override
	public Member queryMemberByPiid(String piid) {
		return memberDao.queryMemberByPiid(piid);
	}

	@Override
	public List<MemberCert> queryMemberCertsByMemberid(Integer memberid) {
		return memberDao.queryMemberCertsByMemberid(memberid);
	}

	@Override
	@Transactional
	public void updateTicketStatus(Ticket t) {
		memberDao.updateTicketStatus(t);
	}
	
}
