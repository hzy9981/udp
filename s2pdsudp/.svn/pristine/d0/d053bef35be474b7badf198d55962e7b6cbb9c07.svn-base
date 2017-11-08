package client;

import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

import org.apache.log4j.Logger;

import com.jinmei.s2pdsudp.protocol.Protocol;
import com.jinmei.s2pdsudp.protocol.ProtocolUtil;

public class Terminal implements Runnable, Protocol {
	private static Logger log=Logger.getLogger(Terminal.class);
	private String remoteIP="192.168.0.7";
	String terminalNo = null;//终端号
	String cabinet = null;//柜子号
	String saveDeliveryManCardNo = null;//存件人卡号
	
	String bundleNo = null;//包裹号
	String recipientManMobile = null;//取件号码
	String recipientManPassword = null;//取件人密码
	String recipientManPic = null;//取件人头像
	public Terminal(String remoteIP) {
		
		super();
		this.remoteIP=remoteIP;
		long l = System.currentTimeMillis();
		this.setTerminalNo(String.valueOf(l).substring(0,4));
		this.setCabinet(String.valueOf(l).substring(0,11));
		this.setSaveDeliveryManCardNo(String.valueOf(l).substring(0,8));
		this.setBundleNo(String.valueOf(l).substring(0,13));
		this.setRecipientManMobile(String.valueOf(l).substring(0,6));
		this.setRecipientManPassword(String.valueOf(l).substring(0,6));
		this.setRecipientManPic(String.valueOf(l).substring(0,4));
	}
	public String getTerminalNo() {
		
		return terminalNo;
	}
	public void setTerminalNo(String terminalNo) {
		this.terminalNo = terminalNo;
	}
	public String getCabinet() {
		return cabinet;
	}
	public void setCabinet(String cabinet) {
		this.cabinet = cabinet;
	}
	public String getSaveDeliveryManCardNo() {
		return saveDeliveryManCardNo;
	}
	public void setSaveDeliveryManCardNo(String saveDeliveryManCardNo) {
		this.saveDeliveryManCardNo = saveDeliveryManCardNo;
	}
	public String getBundleNo() {
		return bundleNo;
	}
	public void setBundleNo(String bundleNo) {
		this.bundleNo = bundleNo;
	}
	public String getRecipientManMobile() {
		return recipientManMobile;
	}
	public void setRecipientManMobile(String recipientManMobile) {
		this.recipientManMobile = recipientManMobile;
	}
	public String getRecipientManPassword() {
		return recipientManPassword;
	}
	public void setRecipientManPassword(String recipientManPassword) {
		this.recipientManPassword = recipientManPassword;
	}
	
	public String getRecipientManPic() {
		return recipientManPic;
	}
	public void setRecipientManPic(String recipientManPic) {
		this.recipientManPic = recipientManPic;
	}
	 private byte[] getHeader() {
	        byte[] tmp = new byte[5];
	        tmp[0] = HEADER;
	        byte[] seq = ProtocolUtil.intToBytes2(ProtocolUtil.getSeq());
	        byte[] way = new byte[]{TERMINAL,SERVER};
	        System.arraycopy(seq, 0, tmp, 1, seq.length);
	        System.arraycopy(way, 0, tmp, seq.length + 1, way.length);
	        return tmp;
	    }
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			
			//log.info("aaaaaaaaaaaaaaaaaaa");
			byte[] header = this.getHeader();//前五个字节
			byte[] heatType = new byte[]{0x01};//心跳报文类型一个字节
			String terminalNo = this.getTerminalNo();
			String version = "v1.0.0.21";
			String strHart = terminalNo + "|" + version;
			byte[] hartConLen = ProtocolUtil.getByteInt2(strHart.length());//心跳报文长度字节
			byte[] hartConByte = strHart.getBytes();
			byte[] hartDemoByte = ProtocolUtil.toAbstract("摘要");//摘要字节
			byte[] hartZong= null;
			hartZong = ProtocolUtil.merge(header, heatType);
			hartZong = ProtocolUtil.merge(hartZong, hartConLen);
			hartZong = ProtocolUtil.merge(hartZong, hartConByte);
			hartZong = ProtocolUtil.merge(hartZong, hartDemoByte);
			
			
			
			try {
				DatagramSocket client = new DatagramSocket();
				DatagramPacket dp = new DatagramPacket(hartZong, hartZong.length);
				
				dp.setSocketAddress(new InetSocketAddress(this.remoteIP, 3000));
				client.send(dp);
				Thread.sleep(1000);
				client=null;
				dp=null;
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			heatType=null;
			terminalNo=null;
			hartConLen=null;
			hartConByte=null;
			hartDemoByte=null;
			hartZong=null;
			
			byte[] type = new byte[]{0x06};//报文类型一个字节
			
			
			String content = 
					terminalNo+"|"
					+this.getCabinet()
					+"|"+this.getSaveDeliveryManCardNo()
					+"|"+this.getBundleNo()
					+"|"+this.getRecipientManMobile()
					+"|"+this.getRecipientManPassword()
					+"|"+this.getRecipientManPic();
			int contentLentth = content.length();
			byte[] conLenByte = ProtocolUtil.getByteInt2(contentLentth);//报文长度字节
			byte[] conByte = content.getBytes();//报文字节
			byte[] demoByte = ProtocolUtil.toAbstract("摘要");//摘要字节
			byte[] zong= null;
			zong = ProtocolUtil.merge(header, type);
			zong = ProtocolUtil.merge(zong, conLenByte);
			zong = ProtocolUtil.merge(zong, conByte);
			zong = ProtocolUtil.merge(zong, demoByte);
			try {
				DatagramSocket client = new DatagramSocket();
				DatagramPacket dp = new DatagramPacket(zong, zong.length);
				
				dp.setSocketAddress(new InetSocketAddress(this.remoteIP, 3000));
				client.send(dp);
				client=null;
				dp=null;
				Thread.sleep(5000);
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			type=null;
			conLenByte=null;
			conByte=null;
			demoByte=null;
			zong=null;
			header=null;
			
			
		}
		
		
	}
	public static void main(String[] args) throws IOException{
		int threads=10;
		InputStream in=null;
		int LEN=1024;
		byte[] buf=new byte[LEN];
		System.out.println("请输入要模拟的终端量：");
		in=System.in;
		in.read(buf);
		String scount=new String(buf,"utf-8").replace("\r\n", "").trim();
		int icount=Integer.valueOf(scount);
		System.out.println("将模拟"+icount+"个终端，对UDP服务器进行访问");
		System.out.println("请输入服务器IP");
		in.read(buf);
		scount=new String(buf,"utf-8").replace("\r\n", "").trim();
		
		for(int i=0;i<icount;i++){
			Thread thi  = new Thread(new Terminal(scount));
			thi.start();
		}
		System.out.println("已启动"+icount+"个终端");

	}

}
