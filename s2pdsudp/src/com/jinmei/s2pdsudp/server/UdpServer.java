/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jinmei.s2pdsudp.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Observable;

import org.apache.log4j.Logger;

import com.jinmei.s2pdsudp.NetServer;
import com.jinmei.s2pdsudp.iface.TerminalSrv;
import com.jinmei.s2pdsudp.listener.OffLineLister;
import com.jinmei.s2pdsudp.listener.UdpServreListener;

/**
 *UDP服务器
 *@author hanzy
 */
public class UdpServer extends Observable implements NetServer, Runnable {
	Logger log = Logger.getLogger(UdpServer.class);

    private int port = UDP_PORT;
    private static DatagramSocket server;
    private Thread thread = null;
    private static UdpServer udpServer;
    private  long recvCount=0;
    private  long sendCount=0;
    private TerminalSrv terminalSrv;
    public synchronized static UdpServer getInstance() {
        if (udpServer == null) {
            udpServer = new UdpServer();
        }
        return udpServer;
    }

    public synchronized static UdpServer getInstance(int port) {
        if (udpServer == null) {
            udpServer = new UdpServer(port);
        }
        return udpServer;
    }

    private UdpServer() {
    }
    /**
     * 设置终端信息处理接口
     * @param terminalSrv
     */
    public void setTerminalSrv(TerminalSrv terminalSrv) {
		this.terminalSrv = terminalSrv;
	}
    public TerminalSrv getTerminalSrv() {
		return terminalSrv;
	}

    private UdpServer(int port) {
        this.port = port;

    }

    public int getPort() {
		return port;
	}
    @Override
    public void start() {
        thread = new Thread(udpServer, "udpserver");
        thread.start();
    }

    @Override
    public void stop() {
        server.disconnect();
        server.close();
        server = null;
        udpServer = null;

    }

	@Override
	public void send(String msg, String ip, int port) throws IOException {
		DatagramPacket dp = new DatagramPacket(msg.getBytes(), msg.length());
		dp.setSocketAddress(new InetSocketAddress(ip, port));
		server.send(dp);
		sendCount++;
	}

	public void send(byte[] buf, String ip, int port, boolean sendNow)
			throws IOException {
		DatagramPacket dp = new DatagramPacket(buf, buf.length);
		dp.setSocketAddress(new InetSocketAddress(ip, port));
		server.send(dp);
		sendCount++;
	}
    @Override
	public void send(byte[] buf, String ip, int port) throws IOException {
		DatagramPacket dp = new DatagramPacket(buf, buf.length);
		dp.setSocketAddress(new InetSocketAddress(ip, port));
		server.send(dp);
		sendCount++;
	}

    @SuppressWarnings("finally")
	@Override
    public void run() {
		try {
			UdpServreListener udpServerLister = new UdpServreListener(udpServer,terminalSrv);
			server = new DatagramSocket(port);
			new Thread(udpServerLister, "udpServerLister").start();
			OffLineLister offline = new OffLineLister();
			offline.setTerminalSrv(terminalSrv);
		} catch (Exception exception) {
			log.warn(exception.getLocalizedMessage(),exception);
			return;
		}
        DatagramPacket packet = null;
        byte[] buf=null;
        long startTime=0;
        while (true) {
            try {
            	if(System.currentTimeMillis()-startTime>30*1000){
    				startTime=System.currentTimeMillis();
    				log.info("UDP["+port+"]通讯服务中...");
    			}
            	buf=new byte[BUF_LEN];
                packet = new DatagramPacket(buf, BUF_LEN);
                server.receive(packet);
                setChanged();
                notifyObservers(packet);
                recvCount++;
            } catch (SocketException ex) {
                log.warn(ex.getLocalizedMessage(), ex);
            } catch (IOException ex) {
                log.warn(ex.getLocalizedMessage(), ex);            
             }finally{
            	 continue;
             }
            
        }

    }
    public long getRecvCount() {
		return recvCount;
	}
    public long getSendCount() {
		return sendCount;
	}
}
