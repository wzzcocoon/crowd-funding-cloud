package cn.wzz.actrowdfudingService.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import cn.wzz.actrowdfudingcommon.bean.Cert;
import cn.wzz.actrowdfudingcommon.bean.Member;
import cn.wzz.actrowdfudingcommon.bean.MemberCert;
import cn.wzz.actrowdfudingcommon.bean.Ticket;

public interface MemberDao {

	@Select("select * from t_member where loginacct = #{loginacct}")
	Member queryMemberByLoginacct(String loginacct);

	@Select("select * from t_ticket where memberid = #{id} and status = '0'")
	Ticket queryTicketByMemberid(Integer id);

	void insertTicket(Ticket t);

	void updateAcctoutType(Member loginMember);

	void updateStep(Ticket t);

	void updateBasicinfo(Member loginMember);

	List<Cert> queryCertByAccountType(String accttype);

	@Select("select * from t_member where id = #{memberid}")
	Member queryById(Integer memberid);

	void insertMemberCerts(@Param("mcs")List<MemberCert> mcs);

	@Update("update t_member set email = #{email} where id = #{id}")
	void updateEmail(Member loginMember);

	void updateStepAndAuthCode(Ticket t);

	@Update("update t_member set authstatus = #{authstatus} where id = #{id}")
	void updateAuthstatus(Member loginMember);

	Member queryMemberByPiid(String piid);

	List<MemberCert> queryMemberCertsByMemberid(Integer memberid);

	@Update("update t_ticket set status = #{status} where id = #{id}")
	void updateTicketStatus(Ticket t);


}
