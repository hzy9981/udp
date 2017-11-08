package com.jinmei.s2pdsudp.iface;

/**
 * 终端实体体，代表一个终端
 * @author hanzy
 *
 */
public class TerminalBean { 
	//终端ID
	private String terminalId;
	//当前版本号
	private String sversion;

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public String getSversion() {
		return sversion;
	}

	public void setSversion(String sversion) {
		this.sversion = sversion;
	}
	
	
}
