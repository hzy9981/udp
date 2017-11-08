/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jinmei.s2pdsudp.listener;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.jinmei.s2pdsudp.TerminalOnlineInfoBean;
import com.jinmei.s2pdsudp.hander.ServerUdpHander;
import com.jinmei.s2pdsudp.iface.TerminalBean;
import com.jinmei.s2pdsudp.iface.TerminalSrv;
import com.jinmei.s2pdsudp.protocol.BufBean;
import com.jinmei.s2pdsudp.protocol.Protocol;
import com.jinmei.s2pdsudp.protocol.ProtocolUtil;
import com.jinmei.s2pdsudp.server.TcpServer;
import com.jinmei.s2pdsudp.server.UdpServer;
import com.jinmei.s2pdsudp.util.DateUtil;

/**
 * 服务端分析
 * 
 *@author hanzy
 */
public class UdpServreListener implements Observer, Runnable {
	private static Logger log=Logger.getLogger(UdpServreListener.class);
	// 收到的数据<终端,序号>
	private static Map<String, LinkedList<Integer>> recvBuf;
	// 需要发送的数据 <终端,发送队列>
	private static Map<String, ConcurrentLinkedQueue<BufBean>> sendBuf;
	private UdpServer udpServer = null;
	private TcpServer tcpServer=null;
	private static Map<String,TerminalBean> allTerminal;
	// 在线设备列表<终端,终端信息>
	private static Map<String, TerminalOnlineInfoBean> onlineTerminal;
	private static Map<String,BufBean> sendReadBuf=null;
	private TerminalSrv terminalSrv=null;
	ExecutorService pool = null;
	static{
		recvBuf = new LinkedHashMap<String, LinkedList<Integer>>();
		sendBuf = new HashMap<String, ConcurrentLinkedQueue<BufBean>>();
		onlineTerminal = new HashMap<String, TerminalOnlineInfoBean>();
		allTerminal=new HashMap<String,TerminalBean>();
		sendReadBuf=new HashMap<String, BufBean>();
	}

	public UdpServreListener(Observable observable,TerminalSrv terminalSrv) {
		this.terminalSrv=terminalSrv;
		observable.addObserver(this);
		pool=Executors.newCachedThreadPool();
		udpServer = UdpServer.getInstance();
		tcpServer=TcpServer.getInstance();

	}
	

	@Override
	public void update(Observable o, Object arg) {
		// 启用终端分析
		pool.execute(new ServerUdpHander(arg,terminalSrv));

		// 启动服务器分析
	}
	/**
	 * 根据终端ID得到对应的终端信息
	 * @param terminalId
	 * @return
	 */
	public static TerminalBean getTerminal(String terminalId){
		return allTerminal.get(terminalId);
	}
	/**
	 * 判断是否存在终端
	 * @param terminalId
	 * @return
	 */
	public static boolean isExistTerminal(String terminalId){
		return allTerminal.containsKey(terminalId);
	}
	/**
	 * 根据终端ID和版本号添加终端
	 * @param terminalId
	 * @param ver
	 * @return
	 */
	public static boolean addTermnal(String terminalId,String ver){
		boolean ret=false;TerminalBean terminal;
		if(isExistTerminal(terminalId)){
			terminal=allTerminal.get(terminalId);
			if(terminal!=null){
				terminal.setSversion(ver);
				allTerminal.put(terminalId, terminal);
				ret=true;
			}
		}else{
				terminal=new TerminalBean();
				terminal.setTerminalId(terminalId);
				terminal.setSversion(ver);
				allTerminal.put(terminalId, terminal);
				ret=false;
			}
		return ret;
	}
	/**
	 * 加载终端
	 * @param terminal
	 */
	public static void addTerminal(TerminalBean terminal){
		allTerminal.put(terminal.getTerminalId(), terminal);
	}
	/**
	 * 批量添加终端
	 * @param terminals
	 */
	public static void addTerminal(List<TerminalBean> terminals){
		Iterator<TerminalBean> li=terminals.iterator();
		while(li.hasNext()){
			addTerminal(li.next());
		}
	}

	/**
	 * 得到在线终端信息
	 * 
	 * @param terminalId
	 *            终端ID
	 * @return 没有返回null
	 */
	public static TerminalOnlineInfoBean getTerminalInfo(String terminalId) {
		return onlineTerminal.get(terminalId);
	}
	public static Collection<TerminalOnlineInfoBean> getAllOnlineTerminals(){
		Collection<TerminalOnlineInfoBean> li=onlineTerminal.values();
		Iterator<TerminalOnlineInfoBean> it=li.iterator();
		TerminalOnlineInfoBean tmp=null;
		Set<TerminalOnlineInfoBean> ret=new HashSet<TerminalOnlineInfoBean>();
		while(it.hasNext()){
			tmp=it.next();
			if(tmp.getOnlineState().equals(TerminalOnlineInfoBean.STATE_ONLINE)){
				ret.add(tmp);
			}
		}
		return ret;
	}

	/**
	 * 判断设备是否在线
	 * 
	 * @param terminalId
	 *            终端ID
	 * @return
	 */
	public static boolean isTerminalOnline(String terminalId) {
		TerminalOnlineInfoBean tmp= onlineTerminal.get(terminalId);
		if(tmp!=null && tmp.getOnlineState().equals(TerminalOnlineInfoBean.STATE_ONLINE)){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 根据终端ID得到终端
	 * @param terminalId
	 * @return
	 */
	public static TerminalOnlineInfoBean getTerminalOnlineInfo(String terminalId){
		return onlineTerminal.get(terminalId);
		
	}
	public static void addOnlineInfo(TerminalOnlineInfoBean onlineInfo){
			onlineTerminal.put(onlineInfo.getTerminalId(), onlineInfo);	
		
	}
	public static void removeOnlineInfo(String terminalId){
		onlineTerminal.remove(terminalId);
	}
	/**
	 * 指定终端待发送缓冲的大小
	 * @param terminalId
	 * @return
	 */
	public static Integer getSendBufSize(String terminalId){
		ConcurrentLinkedQueue<BufBean> li=sendBuf.get(terminalId);
		if(li==null)li=new ConcurrentLinkedQueue<BufBean>();
		return li.size();
	}
	/**
	 * 得到所有等待发送数据的个数
	 * @return
	 */
	public static Integer getSendBufSize(){
		int total=0;
		for(Map.Entry<String, ConcurrentLinkedQueue<BufBean>> entry:sendBuf.entrySet()){
			total+=entry.getValue().size();
		}
		return total;
	}
	/**
	 * 得到指定终端等响应的数据的个数
	 * @param teminalId
	 * @return
	 */
	public static Integer getRecvBufSize(String teminalId){
		LinkedList<Integer> li=recvBuf.get(teminalId);
		if(li==null)li=new LinkedList<Integer>();
		return li.size();
	}
	/**
	 * 得到等待响应的总大小
	 * @return
	 */
	public static Integer getRecvBufSize(){
		int total=0;
		for(Map.Entry<String, LinkedList<Integer>> entry:recvBuf.entrySet()){
			total+=entry.getValue().size();
		}
		return total;
	}

	/**
	 * 新增终端信息
	 * 
	 * @param terminalId
	 *            终端 ID
	 * @param version
	 *            版本号
	 * @param ip
	 *            IP地址
	 */
	public static  TerminalOnlineInfoBean addTerminalInfo(String terminalId, String version,
			String ip,int udpPort,int tcpPort) {
			TerminalOnlineInfoBean tmp=onlineTerminal.get(terminalId);
			if(tmp==null){
				tmp= new TerminalOnlineInfoBean(terminalId,
						version, ip,udpPort,tcpPort);
			}else{
				tmp.setUdpPort(udpPort);
				tmp.setTcpPort(tcpPort);
				tmp.setIp(ip);
			}
			tmp.setLastOnlineTime(DateUtil.getNow(TerminalOnlineInfoBean.TIME_FORMAT));
			tmp.setOnlineState(TerminalOnlineInfoBean.STATE_ONLINE);
			tmp.setLastTime(System.currentTimeMillis());
			onlineTerminal.put(terminalId, tmp);	
			return tmp;
		
	}

	/**
	 * 决断序号是否送达
	 * 
	 * @param seq
	 * @return
	 */
	public static boolean isResponse(String terminalId, Integer seq) {
			LinkedList<Integer> li=recvBuf.get(terminalId);
			if(li==null)return false;
			return recvBuf.get(terminalId).contains(seq);	
		
	}

	/**
	 * 设置已经响应
	 * 
	 * @param seq
	 *            序号
	 */
	public static void setResponse(String terminalId, Integer seq) {
		removeSendBuf(terminalId, seq);
		sendReadBuf.put(terminalId, new BufBean(-1, null));
	}

	/**
	 * 将数据放入发送缓冲
	 * 
	 * @param seq
	 * @param buf
	 */
	public static  void send(String teminalId, Integer seq, byte[] buf) {
			BufBean tmp = new BufBean(seq, buf);
			ConcurrentLinkedQueue<BufBean> li=sendBuf.get(teminalId);
			if(li==null){
				li=new ConcurrentLinkedQueue<BufBean>();
			}
			boolean ret=li.add(tmp);
			sendBuf.put(teminalId, li);	
		
	}
	/**
	 * 发送数据
	 * @param terminalId
	 * @param buf 要发送的内容
	 */
	public static  boolean send(String terminalId,byte[] buf){
		TerminalOnlineInfoBean onlineInfo=getTerminalInfo(terminalId);
		if(onlineInfo!=null){
			int seq=ProtocolUtil.bytes2ToInt(buf, 1);
			send(terminalId,seq,buf);
			return true;
		}else{
			return false;
		}
		
	}

	/**
	 * 得到最新一侦需要发送的数据
	 * 
	 * @return
	 */
	public static BufBean getBufByFIFO(String terminalId) {
			if(terminalId==null)return null;
			if(sendBuf==null){
				sendBuf=new HashMap<String, ConcurrentLinkedQueue<BufBean>>();
			}
			ConcurrentLinkedQueue<BufBean> queue=sendBuf.get(terminalId);
			if(queue==null){
				queue=new ConcurrentLinkedQueue<BufBean>();
				sendBuf.put(terminalId, queue);
			}
			BufBean buf=sendReadBuf.get(terminalId);
			if(buf==null || buf.getSeq()==-1){
				//对应数据发送左边
				buf=queue.poll();
				if(buf!=null){
					log.debug("移除终端["+terminalId+"]缓冲包序号["+buf.getSeq()+"]");
				}
			}
			return buf;	
	}

	/**
	 * 移除已发送成功的数据
	 * 
	 * @param seq
	 *            序号
	 */
	public static void remove(String terminalId, int seq) {
		removeSendBuf(terminalId, seq);
		removeRecvBuf(terminalId, seq);
	}
	/**
	 * 移除已处理的发送缓冲
	 * @param terminalId 终端 ID
	 * @param seq
	 */
	private static void removeSendBuf(String terminalId,int seq){
		ConcurrentLinkedQueue<BufBean> li=sendBuf.get(terminalId);
		if(li==null)return;
		Iterator<BufBean> it = li.iterator();
		while (it.hasNext()) {
			BufBean buf = it.next();
			if (buf.getSeq() == seq) {
				sendBuf.get(terminalId).remove(buf);
				break;
			}
		}
	}
	/**
	 *移除所有的接收缓冲
	 * @param terminalId 终端 ID
	 * @param seq
	 */
	private static void removeRecvBuf(String terminalId,Integer seq){
		LinkedList<Integer> li=recvBuf.get(terminalId);
		if(li==null)return;
		Iterator<Integer> it = li.iterator();
		while (it.hasNext()) {
			Integer buf = it.next();
			if (buf.intValue() == seq.intValue()) {
				recvBuf.get(terminalId).remove(buf);
				break;
			}
		}
	}

	@Override
	public void run() {
		String stype="";
		boolean resend=false;
		while (true) {
				try{
					Iterator<String> it=sendBuf.keySet().iterator();
					if(!it.hasNext()){
						Thread.sleep(1);
					}
					Thread.sleep(1);
					for(;it.hasNext();){
						String terminalId=it.next();
						
						BufBean buf = getBufByFIFO(terminalId);
						if(buf==null || buf.getBuf()==null)continue;
						byte[] tmpBuf=buf.getBuf();
						if(tmpBuf[3]==Protocol.SERVER && tmpBuf[4]==Protocol.TERMINAL){
							resend=true;
						}else{
							resend=false;
						}
						byte type = tmpBuf[5];
						
						int seq=ProtocolUtil.bytes2ToInt(buf.getBuf(), 1);
						if(isResponse(terminalId, buf.getSeq())){
							//更新响应信息
							terminalSrv.onSendSucess(terminalId, tmpBuf);
							remove(terminalId,buf.getSeq());
							log.debug("终端["+terminalId+"]类型["+type+"]序号["+seq+"]已响应");
							sendReadBuf.put(terminalId, new BufBean(-1, null));
							continue;
						}
						
						if(System.currentTimeMillis()-buf.getLastSendTime()>5000){
								//更新发送时间
								//无响应发送
								int sendTimes=buf.getSendTimes();
								buf.setLastSendTime(System.currentTimeMillis());
								//更新发送时间和发送次数
								buf.setSendTimes(++sendTimes);
								sendReadBuf.put(terminalId, buf);
								
								TerminalOnlineInfoBean onlineInfo = onlineTerminal.get(terminalId);
								if(onlineInfo==null){
									log.warn("终端["+terminalId+"]不在线，无法发送数据[]");
									if(buf.getSendTimes()>=5){
										sendBuf.get(terminalId).clear();
										sendReadBuf.remove(terminalId);
									}
									continue;
								}
								
									stype="udp";
									udpServer.send(buf.getBuf(), onlineInfo.getIp(),onlineInfo.getUdpPort());
								log.debug("发>>>>["+stype+"]["+onlineInfo.getTerminalId()+"]"
										+ "ip["+onlineInfo.getIp()+":"+(stype.equals("tcp")?onlineInfo.getTcpPort():onlineInfo.getUdpPort())+"]"+""
										+ "类型["+type+"] "
										+ "序号["+seq+"]长度["+buf.getBuf().length+"]"+new String(buf.getBuf(),Protocol.CODE));
								
								if(!resend){
									//不重发则移除发送缓冲
									//心跳报文\取件\存件\超时件\不重发
									//更新响应信息
									sendReadBuf.put(onlineInfo.getTerminalId(), new BufBean(-1, null));
									remove(onlineInfo.getTerminalId(), seq);
								}
								if(buf.getSendTimes()>=3){
									//发送失败
									//更新响应信息
									terminalSrv.onSendTimeout(onlineInfo.getTerminalId(), buf.getBuf(), onlineInfo.getIp(), onlineInfo.getUdpPort());
									log.warn("发送数据至终端["+onlineInfo.getTerminalId()+"]"
											+ "IP["+onlineInfo.getIp()
											+":"+(stype.equals("tcp")?onlineInfo.getTcpPort():onlineInfo.getUdpPort())+"]超时."
													+ "SEQ["+buf.getSeq()+"]");
									sendReadBuf.put(onlineInfo.getTerminalId(), new BufBean(-1, null));
									remove(onlineInfo.getTerminalId(),seq);
								}
						}
					}
					
				
				} catch (IOException ex) {
					log.warn(ex.getLocalizedMessage(), ex);
				} catch (InterruptedException ex) {
					log.warn(ex.getLocalizedMessage(), ex);
				}finally{
					continue;
				}
		}
	}

}
