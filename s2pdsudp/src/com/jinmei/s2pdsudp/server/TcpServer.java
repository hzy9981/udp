/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jinmei.s2pdsudp.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.logging.Level;

import org.apache.log4j.Logger;

import com.jinmei.s2pdsudp.NetServer;
import com.jinmei.s2pdsudp.iface.TerminalSrv;
import com.jinmei.s2pdsudp.listener.UdpServreListener;
import com.jinmei.s2pdsudp.protocol.Protocol;

/**
 * TCP服务器
 * 
 *@author hanzy
 */
public class TcpServer extends Observable implements NetServer, Runnable {

	Logger log = Logger.getLogger(TcpServer.class);
	private int port = TCP_PORT;
	private boolean running = true;
	private static ServerSocket server;
	private Thread thread = null;
	private static TcpServer tcpServer;
	private long sendCount = 0;
	private long recvCount = 0;
	private TerminalSrv terminalSrv;

	public static TcpServer getInstance() {
		if (tcpServer == null) {
			tcpServer = new TcpServer();
		}
		return tcpServer;
	}

	public static TcpServer getInstance(int port) {
		if (tcpServer == null) {
			tcpServer = new TcpServer(port);
		}
		return tcpServer;
	}

	private TcpServer() {
	}

	private TcpServer(int port) {
		this.port = port;

	}

	public long getSendCount() {
		return sendCount;
	}

	public long getRecvCount() {
		return recvCount;
	}

	@Override
	public void start() {
		thread = new Thread(tcpServer, "tcpServer");
		thread.start();
	}
	public int getPort() {
		return port;
	}

	@Override
	public void stop() {
		try {
			running = false;
			server.close();
		} catch (IOException ex) {
			java.util.logging.Logger.getLogger(TcpServer.class.getName()).log(
					Level.SEVERE, null, ex);
		}
		server = null;
		tcpServer = null;
	}

	@Override
	public void send(String msg, String ip, int port) throws IOException {
		send(msg.getBytes(Protocol.CODE), ip, port);
	}

	@Override
	public void send(byte[] buf, String ip, int port) throws IOException {
		Socket socket = null;
		socket = new Socket(ip, port);
		OutputStream out = socket.getOutputStream();
		out.write(buf);
		out.flush();
		out.close();
		socket.close();
		sendCount++;
	}

	@SuppressWarnings("finally")
	@Override
	public void run() {
		try {
			UdpServreListener udpServerLister = new UdpServreListener(tcpServer,terminalSrv);
			server = new ServerSocket(port);
			new Thread(udpServerLister, "tcpSendPacketThread").start();
		} catch (Exception ex) {
			log.warn(ex.getLocalizedMessage(), ex);
			running = false;
			return;
		}
		Socket packet = null;
		long startTime = 0;
		while (running) {
			if(System.currentTimeMillis()-startTime>10*1000){
				startTime=System.currentTimeMillis();
				log.info("TCP["+port+"]通讯服务中...");
			}
			try {
				packet = server.accept();
				setChanged();
				notifyObservers(packet);
				recvCount++;
			} catch (SocketException ex) {
				log.warn(ex.getLocalizedMessage(), ex);
				continue;
			} catch (IOException ex) {
				log.warn(ex.getLocalizedMessage(), ex);
				continue;
			}finally{
				continue;
			}
		}

	}
}
