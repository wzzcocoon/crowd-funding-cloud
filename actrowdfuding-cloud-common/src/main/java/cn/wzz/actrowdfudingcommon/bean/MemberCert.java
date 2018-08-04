package cn.wzz.actrowdfudingcommon.bean;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

public class MemberCert implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id, memberid, certid;
	private String certname;
	private String iconpath;
	private MultipartFile file;
	
	public String getCertname() {
		return certname;
	}
	public void setCertname(String certname) {
		this.certname = certname;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getMemberid() {
		return memberid;
	}
	public void setMemberid(Integer memberid) {
		this.memberid = memberid;
	}
	public Integer getCertid() {
		return certid;
	}
	public void setCertid(Integer certid) {
		this.certid = certid;
	}
	public String getIconpath() {
		return iconpath;
	}
	public void setIconpath(String iconpath) {
		this.iconpath = iconpath;
	}
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	
	
}

