package com.jinmei.s2pdsudp.listener;

import java.util.Collection;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.jinmei.s2pdsudp.TerminalOnlineInfoBean;
import com.jinmei.s2pdsudp.iface.TerminalSrv;
import com.jinmei.s2pdsudp.util.DateUtil;

/**
 * 离线机制,
 * @author hanzy
 *
 */
public class OffLineLister {
	private Logger log=Logger.getLogger(OffLineLister.class);
	private Timer timer=null;
	private  TerminalSrv terminalSrv;
	//离线分钟数
	private int minute=2;
	public OffLineLister() {
		timer=new Timer("offLineLister");
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				Collection<TerminalOnlineInfoBean> li=UdpServreListener.getAllOnlineTerminals();
				if(li==null)return;
				Iterator<TerminalOnlineInfoBean> it=li.iterator();
				while(it.hasNext()){
					TerminalOnlineInfoBean tmp=it.next();
					if(System.currentTimeMillis()-tmp.getLastTime()>minute*60*1000){
						//移除 2分钟无响应
						log.info(tmp.getTerminalId()+"离线。");
						//修改服务器离线信息
						if(terminalSrv!=null){
							terminalSrv.onTerminalOffline(tmp.getTerminalId(),
									TerminalOnlineInfoBean.STATE_OFFLINE+ "["+tmp.getLastOnlineTime()+"]");
						}
						tmp.setOnlineState(TerminalOnlineInfoBean.STATE_OFFLINE);
						tmp.setOfflineTime(DateUtil.getNow(TerminalOnlineInfoBean.TIME_FORMAT));
						UdpServreListener.addOnlineInfo(tmp);
//						li=UdpServreListener.getAllOnlineTerminals();
//						if(li==null)return;
//						it=li.iterator();
					}
				}
				
			}
		}, 0, 5*1000);
	}
	public  void setTerminalSrv(TerminalSrv terminalSrv) {
		this.terminalSrv = terminalSrv;
	}
}
