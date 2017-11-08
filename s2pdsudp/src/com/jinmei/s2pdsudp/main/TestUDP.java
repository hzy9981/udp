package com.jinmei.s2pdsudp.main;

import com.jinmei.s2pdsudp.iface.TerminalSrv;
import com.jinmei.s2pdsudp.server.UdpServer;

public class TestUDP {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		UdpServer udp_s = UdpServer.getInstance(3001);
		TerminalSrv terminalSrv=new TerminalSrvImpl();
		udp_s.setTerminalSrv(terminalSrv);
		udp_s.start();
	}
	

}
