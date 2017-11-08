/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jinmei.s2pdsudp.hander;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import org.apache.log4j.Logger;

import com.jinmei.s2pdsudp.NetServer;
import com.jinmei.s2pdsudp.TerminalOnlineInfoBean;
import com.jinmei.s2pdsudp.exception.UdpException;
import com.jinmei.s2pdsudp.iface.TerminalSrv;
import com.jinmei.s2pdsudp.listener.UdpServreListener;
import com.jinmei.s2pdsudp.protocol.ClientData;
import com.jinmei.s2pdsudp.protocol.Protocol;
import com.jinmei.s2pdsudp.protocol.ProtocolUtil;
import com.jinmei.s2pdsudp.protocol.ServerData;
import com.jinmei.s2pdsudp.protocol.ServerProtocolUtil;
import com.jinmei.s2pdsudp.server.TcpServer;
import com.jinmei.s2pdsudp.server.UdpServer;
import com.jinmei.s2pdsudp.util.FileUtil;
import com.jinmei.s2pdsudp.util.PropertiesConfiguration;

/**
 * 处理UDP协议
 * @author hanzy
 */
public class ServerUdpHander extends Observable implements Runnable {

	private DatagramPacket udpPacket = null;
	private Socket tcpSocket=null;
	private UdpServer udpServer = null;
	private TcpServer tcpServer=null;
	private static Map<String,List<Integer>> recvFile=new HashMap<String,List<Integer>>();
	//终端序号表,保存每个终端对应的最大序号
	private static Map<String,Integer> seqlist=new HashMap<String,Integer>();
	private TerminalSrv terminalSrv=null;
	
	private static long recvFileLen=0;
	Logger log = Logger.getLogger(ServerUdpHander.class);

	public  void setTerminalSrv(TerminalSrv terminalSrv) {
		this.terminalSrv = terminalSrv;
	}
	public  TerminalSrv getTerminalSrv() {
		return terminalSrv;
	}
	public static void updTerminalSeq(String terminalId,int seq){
		Integer tseq=seqlist.get(terminalId);
		if(tseq!=null){
			if(tseq<seq){
				tseq=seq;
			}
		}
		seqlist.put(terminalId, tseq);
	}
	public static Integer getTerminalSeq(String terminalId){
		return seqlist.get(terminalId);
	}
	/**
	 * 添加终端收到的文件序号
	 * @param terminalId 终端号
	 * @param fileName 文件名称
	 * @param seq 序号
	 * @return 如果序号存在返回true，否则返回false
	 */
	private boolean setRecvFileSeq(String terminalId,String fileName,Integer seq){
		boolean existSeq=false;
		List<Integer> li=recvFile.get(terminalId+fileName);
		if(li==null){
			existSeq=false;
			li=new ArrayList<Integer>();
		}else{
			existSeq=li.contains(seq);
		}
		if(!existSeq){
			li.add(seq);
			recvFile.put(terminalId+fileName, li);
		}
		
		return existSeq;
	}
	/**
	 * 终端UDP处理分析
	 * 
	 * @param packet
	 *            要分析的信息
	 */
	public ServerUdpHander(Object packet,TerminalSrv terminalSrv) {
		this.terminalSrv=terminalSrv;
		udpServer = UdpServer.getInstance();
		tcpServer=TcpServer.getInstance();
		if(packet instanceof DatagramPacket){
			this.udpPacket = (DatagramPacket)packet;
			this.tcpSocket=null;
		}else if(packet instanceof Socket){
			this.tcpSocket=(Socket)packet;
			this.udpPacket=null;
			
		}
		
	}

	@Override
	public void run() {
	byte[] tmp = null;
        InetAddress add = null;
        String addr = null;
        try {
            if (udpPacket != null) {
                add = udpPacket.getAddress();
                tmp = ProtocolUtil.subBytes(udpPacket.getData(), 0, udpPacket.getLength());
                proccess(add.getHostAddress(),udpPacket.getPort(),-1, tmp,true);
                log.debug("收<<<<UDP["+udpPacket.getAddress()+":"+udpPacket.getPort()+"] "+udpPacket.getLength()+"Byte "+new String(tmp,Protocol.CODE));
            } else if (tcpSocket != null) {
                add = tcpSocket.getInetAddress();
                InputStream in = null;
                in = tcpSocket.getInputStream();
                addr = add.getHostAddress();
                tmp=new byte[NetServer.BUF_LEN];
                int readLen=0;
                	readLen=in.read(tmp);
                	if(readLen>0){
                		proccess(addr,-1,tcpSocket.getPort(), tmp,false);
                		tmp=new byte[NetServer.BUF_LEN];
                		log.debug("收<<<<TCP["+tcpSocket.getRemoteSocketAddress()+":"+tcpSocket.getPort()+"] "+readLen+"Byte"+new String(tmp,Protocol.CODE));
                	}
                in.close();
                tcpSocket.close();
                
            }
            
        } catch (IOException ex) {
            log.warn(ex.getLocalizedMessage(),ex);
        }catch (UdpException ex){
        	log.warn(ex.getLocalizedMessage(),ex);
        }
	}

	/**
	 * 分析数据
	 * 
	 * @param ip
	 *            对方地址
	 * @param data
	 *            要分析的数据
	 * @throws IOException 
	 */
	private byte[] proccess(String ip,int udpPort,int tcpPort, byte[] data,boolean udpway)
			throws UdpException,IOException {
		if (data[0] == Protocol.HEADER && data.length > 24) {
			int i = 0;
			int dlen=ProtocolUtil.bytes2ToInt(data, 6);
			dlen=8+16+dlen;
			data=ProtocolUtil.subBytes(data, 0, dlen);
				// 终端到服务器数据
				int seq = ProtocolUtil.bytes2ToInt(data, 1);
				byte type = data[5];
				byte[] content = ProtocolUtil.subBytes(data, 8,
						data.length - 16);
				String msg = "";
				String[] msgs = null;
				byte[] buf = null;
				switch (type) {
				case Protocol.TYPE_DEFAULT:
					// 新交互方式通用
					ClientData d = ClientData.newInstance(content);
					// 终端请求
					if (Protocol.CLIENT_REQUEST.equals(d.getType())) {
						// 确认收到请求
						ServerProtocolUtil.confirm(seq, ip, udpPort);
						updTerminalSeq(d.getTerminalId(), seq);
						// 丢弃重复发送的报文
						if (d.isDuplicated(new Integer(seq).toString())) {
							break;
						}
						// doSomething
						String method = d.getMethod();
						Server obj = Mapping.getServer(method);
						if (obj != null) {
							ServerData res = ServerData.newRequestData(d
									.getTerminalId());
							res.put("_sys_type", Protocol.SERVER_RESPONSE2);
							res.put("_sys_seq", new Integer(seq).toString());
							res.put("_sys_uuid", d.get("_sys_uuid"));
							res.setMethod(null);
							obj.service(d, res);
						} else {
							log.debug("终端 " + d.getTerminalId() + " 请求调用的 " + method
									+ " 方法不存在..");
						}
					} else if (Protocol.CLIENT_RESPONSE.equals(d.getType())) {
						// 终端响应
						UdpServreListener.setResponse(d.getTerminalId(), seq);
					} else if (Protocol.CLIENT_RESPONSE2.equals(d.getType())) {
						// 确认收到请求
						ServerProtocolUtil.confirm(seq, ip, udpPort);
						updTerminalSeq(d.getTerminalId(), seq);
						// callback
						String oldSeq = d.get("_sys_seq");
						String uuid = d.get("_sys_uuid");
						String terminalId = d.getTerminalId();
						Map<String, Map<String, CallBack>> en = ServerData.callbacks
								.get(terminalId);
						if (en != null) {
							CallBack callback = null;
							synchronized (en) {
								Map<String, CallBack> map = en.get(oldSeq);
								if (map != null) {
									callback = map.get(uuid);
									if (callback != null) {
										en.remove(oldSeq);
									}
								}
							}
							if (callback != null) {
								callback.handle(d);
							}
						}
					}
					break;
				case Protocol.TYPE_HEART:
					// 心跳报文
					try {
						msg = new String(content, Protocol.CODE);
						msgs = msg.split(Protocol.DESPLIT);
//						terminalSrv.addTerminalInfo(msgs[0], msgs[1]);
						updTerminalSeq(msgs[0],seq);
						log.debug(msgs[0]+"["+ip+":"+udpPort+"]版本["+msgs[1]+"]心跳["+seq+"]");
						TerminalOnlineInfoBean online=UdpServreListener.addTerminalInfo(msgs[0], msgs[1], ip,udpPort,tcpPort);
						//更新数据库信息
						terminalSrv.onTerminalOnline(msgs[0]
								,msgs[1],
								TerminalOnlineInfoBean.STATE_ONLINE+"["+online.getLastOnlineTime()+"]"
								);
						
						// 回复已收到
						byte[] ct = ServerProtocolUtil.responseHart(seq);
						UdpServreListener.send(msgs[0], seq, ct);
					} catch (Exception e) {
						// TODO: handle exception
						throw new UdpException("心跳报文异常", e);
					}

					break;				
				case Protocol.TYPE_UPDATE:
					// 更新
					msg = new String(content, Protocol.CODE);
					msgs = msg.split(Protocol.DESPLIT);
					this.terminalSrv.onTerminalUpdateState(msgs[0], msgs[1]+"["+msgs[2]+"]");
					UdpServreListener.setResponse(msgs[0], seq);
					break;
				case Protocol.TYPE_UPLOADFORPICK:
					// 上报取件
					try {
						msg = new String(content, Protocol.CODE);
						msgs = msg.split(Protocol.DESPLIT);
						updTerminalSeq(msgs[0],seq);						
						String ret=terminalSrv.onUploadPick(msgs[0], msgs[1], msgs[2], msgs[3], msgs[4]);
						// 回复已收到
						if(ret!=null){
							buf = ServerProtocolUtil.responseUploadPick(seq);
							UdpServreListener.send(msgs[0], seq, buf);
						}
						if(udpway){
							log.debug(msgs[0]+"["+ip+":"+udpPort+"]上报取件["+msg+"]SEQ["+seq+"]");	
						}else{
							log.debug(msgs[0]+"["+ip+":"+tcpPort+"]上报取件["+msg+"]SEQ["+seq+"]");
						}
						
					} catch (Exception e) {
						// TODO: handle exception
						log.debug("通信格式错误。");
					}
					
					break;
				case Protocol.TYPE_UPLOADFORSAVE:
					// 上报存件
					
					msg = new String(content, Protocol.CODE);
					msgs = msg.split(Protocol.DESPLIT);
					updTerminalSeq(msgs[0],seq);
					if(udpway){
						log.debug(msgs[0]+"["+ip+":"+udpPort+"]上报存件["+msg+"]SEQ["+seq+"]");	
					}else{
						log.debug(msgs[0]+"["+ip+":"+tcpPort+"]上报存件["+msg+"]SEQ["+seq+"]");
					}
					terminalSrv.onUploadSave(msgs[0], msgs[1], msgs[3], msgs[2], msgs[4], msgs[5], msgs[6]);
					buf = ServerProtocolUtil.responseUploadSave(seq);
					UdpServreListener.send(msgs[0],seq, buf);
					break;
				case Protocol.TYPE_SETUSER:
					// 修改用户
					msg = new String(content, Protocol.CODE);
					msgs = msg.split(Protocol.DESPLIT);
					UdpServreListener.setResponse(msgs[0], seq);
					updTerminalSeq(msgs[0],seq);
					String[] contents = msg.split(Protocol.DESPLIT);
					if(contents.length==3){
						terminalSrv.onServerUpdTerminalCardInfo(contents[1], contents[0], contents[2]);
						if(udpway){
							log.debug(msgs[0]+"["+ip+":"+udpPort+"]修改用户["+msg+"]SEQ["+seq+"]");	
						}else{
							log.debug(msgs[0]+"["+ip+":"+tcpPort+"]修改用户["+msg+"]SEQ["+seq+"]");
						}
						
					}else{
						//设置错误
						log.error("报文格式错误ip["+ip+"]["+msg+"]");
					}

					break;
				case Protocol.TYPE_SENDMSG:
					// 下发短信响应
					msg = new String(content, Protocol.CODE);
					msgs = msg.split(Protocol.DESPLIT);
					UdpServreListener.setResponse(msgs[0], seq);
					updTerminalSeq(msgs[0],seq);
					this.terminalSrv.onSendMsgResponse(msgs[0], msgs[1], msgs[2], msgs[3],msgs[4]);
					log.warn("终端["+msgs[0]+"]下发短信 SEQ["+seq+"]响应");
					break;
				case Protocol.TYPE_UPLOADFILE:
					// 上传文件
					long startTime=System.currentTimeMillis();
					buf = ServerProtocolUtil.responseUploadFile(seq);
					if(udpPort==-1){
						tcpServer.send(buf, ip, 
								tcpPort);
						log.debug("TCP响应["+ip+":"+tcpPort+"]上传文件 序号["+seq+"SEQ["+seq+"]]"
								+ "耗时["+(System.currentTimeMillis()-startTime)+"]ms"
								+new String(buf,Protocol.CODE));
					}else{
						udpServer.send(buf, ip, udpPort);
						log.debug("UDP响应["+ip+":"+tcpPort+"]上传文件 序号["+seq+"SEQ["+seq+"]]"
								+ "耗时["+(System.currentTimeMillis()-startTime)+"]ms"+
								new String(buf,Protocol.CODE));
					}
					writeFile(content,seq);
					break;
				case Protocol.TYPE_UPLOADFOROVERTIME:
					// 上报超时件
					msg = new String(content, Protocol.CODE);
					msgs = msg.split(Protocol.DESPLIT);
					if(msgs.length==4){
						terminalSrv.onUploadOvertime(msgs[0], msgs[1], msgs[3], msgs[2],"");
					}else if(msgs.length==5){
						terminalSrv.onUploadOvertime(msgs[0], msgs[1], msgs[3], msgs[2],msgs[4]);	
					}
					buf = ServerProtocolUtil.responseUploadOvertime(seq);
					UdpServreListener.send(msgs[0], seq, buf);
					break;
				case Protocol.TYPE_SETOVERTIME:
					// 设置超时时间
					msg = new String(content, Protocol.CODE);
					msgs = msg.split(Protocol.DESPLIT);
					if(udpway){
						log.debug(msgs[0]+"["+ip+":"+udpPort+"]设置超时时间响应["+msg+"]SEQ["+seq+"]");	
					}else{
						log.debug(msgs[0]+"["+ip+":"+tcpPort+"]设置超时时间响应["+msg+"]SEQ["+seq+"]");
					}
					UdpServreListener.setResponse(msgs[0], seq);
					break;
				case Protocol.TYPE_UPLOADMSGSTA:
					// 上报短信发送信息
					msg = new String(content, Protocol.CODE);
					msgs = msg.split(Protocol.DESPLIT);
					updTerminalSeq(msgs[0],seq);
					if(udpway){
						log.debug(msgs[0]+"["+ip+":"+udpPort+"]上报短信发送["+msg+"]SEQ["+seq+"]");	
					}else{
						log.debug(msgs[0]+"["+ip+":"+tcpPort+"]上报短信发送["+msg+"]SEQ["+seq+"]");
					}
					
					if(msgs.length==7){
						this.terminalSrv.onUploadMsg(msgs[0], msgs[1], msgs[2], msgs[3], msgs[4], msgs[5],msgs[6]);
					}else{
						this.terminalSrv.onUploadMsg(msgs[0], msgs[1], msgs[2], msgs[3], msgs[4], msgs[5],null);
					}
					buf = ServerProtocolUtil.responseUploadMessage(seq);
					UdpServreListener.send(msgs[0],seq, buf);
					if(udpPort==-1){
						tcpServer.send(buf, ip, 
								tcpPort);
						log.debug("TCP响应["+ip+":"+tcpPort+"]上报短信发送 序号["+seq+"SEQ["+seq+"]]"
								+new String(buf,Protocol.CODE));
					}else{
						udpServer.send(buf, ip, udpPort);
						
						log.debug("UDP响应["+ip+":"+tcpPort+"]上报短信发送 序号["+seq+"SEQ["+seq+"]]"+
								new String(buf,Protocol.CODE));
					}
					break;
				case Protocol.TYPE_UPLOADUSER:
					msg = new String(content, Protocol.CODE);
					log.debug("上报用户信息"+msg);
					msgs = msg.split(Protocol.DESPLIT);
					log.debug("TCP响应["+ip+":"+tcpPort+"]上报用户信息 序号["+seq+"SEQ["+seq+"]]");
					if(msgs.length==11){
						terminalSrv.onUploadCardInfo(msgs[0], msgs[1], msgs[2], msgs[3], msgs[4], msgs[5], msgs[6], msgs[7],msgs[8],msgs[9],msgs[10]);	
					}else{
						terminalSrv.onUploadCardInfo(msgs[0], msgs[1], msgs[2], msgs[3], msgs[4], msgs[5], msgs[6], msgs[7],msgs[8],null,null);
					}
					
					buf=ServerProtocolUtil.responseUploadUser(seq);
					UdpServreListener.send(msgs[0],seq, buf);
					break;
				
					default:
					// 其他情况不处理
					break;

				}
				return buf;
//			}

		}else{
			log.warn("通讯格式错误ip["+ip+"]["+new String(data,Protocol.CODE).trim()+"]");
			return null;
		}
		
	}
	/**
	 * 写文件
	 * @param content
	 * @param seq
	 */
	private  void writeFile(byte[] content, int seq) {
		int partNo=ProtocolUtil.bytes2ToInt(content, 0);
		int len=ProtocolUtil.bytes2ToInt(content,3);
		long totolLen=ProtocolUtil.byte4ToLong(content,6);
		byte[] tmp=ProtocolUtil.subBytes(content, len+12, content.length);
		String terminalId,fileName;
		try {
			String[] msg=new String(tmp,Protocol.CODE).split(Protocol.DESPLIT);
			updTerminalSeq(msg[0],seq);
			if(msg.length==2){
				log.trace("段号["+partNo+"]序号["+seq+"]总长度["+totolLen+"]当前长度["+len+"]");
				//
				terminalId=msg[0];
				fileName=msg[1];
				String thatTime=fileName.substring(1, fileName.lastIndexOf("."));
				SimpleDateFormat sf=new SimpleDateFormat("yyyyMMddhhmmssS");
				SimpleDateFormat destSf=new SimpleDateFormat("yyyy.MM.dd");
				thatTime=destSf.format(sf.parse(thatTime));
				if(totolLen==-1){
					UdpServreListener.setResponse(terminalId, seq);
					terminalSrv.onRequestImgNotfound(terminalId, fileName);
					log.debug("终端["+terminalId+"]已收到，上传图片["+fileName+"]不存在SEQ["+seq+"]");
					return;
				}
				//收到序号
				if(partNo==0){
					//确定开始上传文件
					UdpServreListener.setResponse(terminalId, seq);
					terminalSrv.onRequestImgStartRecv(terminalId, fileName);
					log.debug("终端["+terminalId+"]已收到，准备上传图片["+fileName+"]总长度["+totolLen+"]SEQ["+seq+"]");
					return ;
				}
				if(setRecvFileSeq(terminalId,fileName,seq)){
					//分段重复，不处理
					log.debug("终端["+terminalId+"]上传图片["+fileName+"]SEQ["+seq+"]重复，将忽略");
					return ;
				}
				//保证路径存在
				PropertiesConfiguration pc=new PropertiesConfiguration("conf.properties");
				String dir=pc.getProperty("upload_dir");
				if(dir==null || dir.equals(""))dir="d:\\";
				dir=FileUtil.getWebRootUrl()+"\\upload\\";
				File file=new File(dir+terminalId+"\\"+thatTime+"\\");
				file.mkdirs();
				File totalFile=new File(file.getAbsolutePath()+File.separator+fileName+".tmp");
				//判断是否完成
				int lastPartNo=new Double(Math.ceil(totolLen/(float)(1000))).intValue();
				if(partNo==1){
					//删除已接收的临时文件
					totalFile.delete();
					totalFile.createNewFile();
				}
				if(partNo==lastPartNo){
					//完成，组装包
					//写入最后一段
					FileOutputStream out=new FileOutputStream(totalFile, true);
					recvFileLen+=len;
					out.write(content, 2+2+4+3, len);
					out.close();
					//
					String newFile=totalFile.getAbsolutePath();
					newFile=newFile.substring(0,newFile.lastIndexOf(".tmp"));
					File nFile=new File(newFile);
					if(nFile.exists())nFile.delete();
					totalFile.renameTo(nFile);
					//更新数据库的内容
					String relativePath=nFile.getAbsolutePath();
					relativePath=relativePath.substring(relativePath.indexOf("upload"));
					terminalSrv.onRequestImgComplete(terminalId, fileName, relativePath);
					recvFile.remove(terminalId+fileName);
					log.debug("文件"+relativePath+File.separator+fileName+"接收完成,实收长度["+recvFileLen+"]应收长度["+totolLen+"]");
					recvFileLen=0;
					return;
				}
				FileOutputStream out=new FileOutputStream(totalFile, true);
				recvFileLen+=len;
				out.write(content, 2+2+4+3, len);
				out.close();
				
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			log.warn(e.getLocalizedMessage(),e);
		} catch (ParseException e) {
			e.printStackTrace();
			log.warn(e.getLocalizedMessage(),e);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log.warn(e.getLocalizedMessage(),e);
		} catch (IOException e) {
			e.printStackTrace();
			log.warn(e.getLocalizedMessage(),e);
		}
		
	}

}
