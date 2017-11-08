package com.jinmei.s2pdsudp;

/**
 * 终端信息列表
 * @author hanzy
 * @version v1.0
 */
public class TerminalOnlineInfoBean {
	public static final String STATE_ONLINE="在线";
	public static final String STATE_OFFLINE="离线";
	public static final String TIME_FORMAT="yyyy-MM-dd HH:mm:ss";
	
	//终端ID
	private String terminalId;
	//版本号
	private String version;
	//ip地址
	private String ip;
	//穿透端口
	private int udpPort;
	private int tcpPort;
	private long lastTime=0;
	private String onlineState="";
	//最后一次在线时间
	private String lastOnlineTime;
	//离线时间
	private String offlineTime;
	
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
	
	public TerminalOnlineInfoBean(String terminalId, String version, String ip,
			int udpPort,int tcpPort) {
		this.terminalId = terminalId;
		this.version = version;
		this.ip = ip;
		if(udpPort!=-1){
			this.udpPort=udpPort;	
		}
		if(tcpPort!=-1){
			this.tcpPort=tcpPort;
		}
		
		
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getTerminalId() {
		return terminalId;
	}
	public String getVersion() {
		return version;
	}
	public String getIp() {
		return ip;
	}
	public long getLastTime() {
		return lastTime;
	}
	public void setLastTime(long lastTime) {
		this.lastTime = lastTime;
	}
	public int getUdpPort() {
		return udpPort;
	}
	public void setUdpPort(int udpPort) {
		this.udpPort = udpPort;
	}
	public int getTcpPort() {
		return tcpPort;
	}
	public void setTcpPort(int tcpPort) {
		this.tcpPort = tcpPort;
	}
	public String getOnlineState() {
		return onlineState;
	}
	public void setOnlineState(String onlineState) {
		this.onlineState = onlineState;
	}
	public String getLastOnlineTime() {
		return lastOnlineTime;
	}
	public void setLastOnlineTime(String lastOnlineTime) {
		this.lastOnlineTime = lastOnlineTime;
	}
	public String getOfflineTime() {
		return offlineTime;
	}
	public void setOfflineTime(String offlineTime) {
		this.offlineTime = offlineTime;
	}
	
	
}
