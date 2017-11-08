/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jinmei.s2pdsudp.protocol;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import com.jinmei.s2pdsudp.hander.ServerUdpHander;
import com.jinmei.s2pdsudp.listener.UdpServreListener;
import com.jinmei.s2pdsudp.server.UdpServer;

/**
 *服务器协议工具类
 * @author hanzy
 */
public class ServerProtocolUtil implements Protocol {

    /**
     *
     * 心跳响应
     *
     * @param seq
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] responseHart(int seq) throws UnsupportedEncodingException {
        byte[] tmp = null;
        byte[] header = getHeader(seq,false);
        byte[] type = new byte[]{TYPE_HEART};
        byte[] content = OK.getBytes(CODE);
        byte[] len = ProtocolUtil.intToBytes2(content.length);
        tmp = ProtocolUtil.merge(header, type);
        tmp = ProtocolUtil.merge(tmp, len);
        tmp = ProtocolUtil.merge(tmp, content);
        tmp = ProtocolUtil.merge(tmp, ProtocolUtil.toAbstract(tmp));
        return tmp;
    }
    /**
     * 向终端下发更新命令 
     * @param terminalId
     * @param addr  地址格式以http:/localhost:8080/xxx/v14.05.29.001.zip  ，addr=8080/xxx/v14.05.29.001.zip
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] requestUpdate(String terminalId,String addr,String updateType,String hour) throws UnsupportedEncodingException {
        byte[] tmp = null;
        Integer seq=ServerUdpHander.getTerminalSeq(terminalId);
        if(seq==null){
        	seq=ProtocolUtil.getSeq();
        }
        byte[] header = getHeader(seq,true);
        byte[] type = new byte[]{TYPE_UPDATE};
        byte[] content = (terminalId+SPLIT+addr+SPLIT+
        		updateType+SPLIT+hour).getBytes(CODE);
        byte[] len = ProtocolUtil.intToBytes2(content.length);
        tmp = ProtocolUtil.merge(header, type);
        tmp = ProtocolUtil.merge(tmp, len);
        tmp = ProtocolUtil.merge(tmp, content);
        tmp = ProtocolUtil.merge(tmp, ProtocolUtil.toAbstract(tmp));
        return tmp;
    }


    /**
     * 响应上报的取件信息
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] responseUploadPick(int seq) throws UnsupportedEncodingException {
        byte[] tmp = null;
        byte[] header = getHeader(seq,false);
        byte[] type = new byte[]{TYPE_UPLOADFORPICK};
        byte[] content = (OK).getBytes(CODE);
        byte[] len = ProtocolUtil.intToBytes2(content.length);
        tmp = ProtocolUtil.merge(header, type);
        tmp = ProtocolUtil.merge(tmp, len);
        tmp = ProtocolUtil.merge(tmp, content);
        tmp = ProtocolUtil.merge(tmp, ProtocolUtil.toAbstract(tmp));

        return tmp;
    }

    /**
     * 响应存件成功
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] responseUploadSave(int seq) throws UnsupportedEncodingException {
        byte[] tmp = null;
        byte[] header = getHeader(seq,false);
        byte[] type = new byte[]{TYPE_UPLOADFORSAVE};
        byte[] content = (OK).getBytes(CODE);
        byte[] len = ProtocolUtil.intToBytes2(content.length);
        tmp = ProtocolUtil.merge(header, type);
        tmp = ProtocolUtil.merge(tmp, len);
        tmp = ProtocolUtil.merge(tmp, content);
        tmp = ProtocolUtil.merge(tmp, ProtocolUtil.toAbstract(tmp));
        return tmp;
    }

    /**
     * 响应拒收
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] responseReject(int seq) throws UnsupportedEncodingException {
        byte[] tmp = null;
        byte[] header = getHeader(seq,false);
        byte[] type = new byte[]{TYPE_UPLOADREJECT};
        byte[] content = (OK).getBytes(CODE);
        byte[] len = ProtocolUtil.intToBytes2(content.length);
        tmp = ProtocolUtil.merge(header, type);
        tmp = ProtocolUtil.merge(tmp, len);
        tmp = ProtocolUtil.merge(tmp, content);
        tmp = ProtocolUtil.merge(tmp, ProtocolUtil.toAbstract(tmp));
        return tmp;
    }

    /**
     * 请求设置用户
     *
     * @param cardNo 卡号
     * @param phoneNo 电话号码
     * @param company 公司
     * @param password 操作密码
     * @param valid 是否有效（1，有效。0，无效）
     * @param cardType 卡类型（1，快递员。0，管理员）
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] requestSetCardInfo(String terminalId,String cardNo,
            String phoneNo, String company,
            String password, String valid,String cardType,String userName,String paid,Float overtime,Integer saveNum) throws UnsupportedEncodingException {
        byte[] tmp = null;
        Integer seq=ServerUdpHander.getTerminalSeq(terminalId);
        if(seq==null){
        	seq=ProtocolUtil.getSeq();
        }
        byte[] header = getHeader(ProtocolUtil.getSeq(),true);
        byte[] type = new byte[]{TYPE_SETUSER};
        byte[] content = (
        		cardNo + SPLIT + 
        		phoneNo + SPLIT + 
        		company + SPLIT+
        		password + SPLIT+ 
        		valid+SPLIT+
        		cardType+SPLIT+
        		userName+SPLIT+
        		paid+SPLIT+
        		overtime.toString()+SPLIT+
        		saveNum.toString()).getBytes(CODE);
        byte[] len = ProtocolUtil.intToBytes2(content.length);
        tmp = ProtocolUtil.merge(header, type);
        tmp = ProtocolUtil.merge(tmp, len);
        tmp = ProtocolUtil.merge(tmp, content);
        tmp = ProtocolUtil.merge(tmp, ProtocolUtil.toAbstract(tmp));
        return tmp;
    }

    /**
     * 响应上传错误信息
     *
     * @param seq 序号
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] responseUploadError(int seq) throws UnsupportedEncodingException {
        byte[] tmp = null;
        byte[] header = getHeader(seq,false);
        byte[] type = new byte[]{TYPE_UPLOADERROR};
        byte[] content = (OK).getBytes(CODE);
        byte[] len = ProtocolUtil.intToBytes2(content.length);
        tmp = ProtocolUtil.merge(header, type);
        tmp = ProtocolUtil.merge(tmp, len);
        tmp = ProtocolUtil.merge(tmp, content);
        tmp = ProtocolUtil.merge(tmp, ProtocolUtil.toAbstract(tmp));
        return tmp;
    }

    /**
     * 请求下发短信或错号登记
     * @param terminalId
     * @param tableId
     * @param expressNo
     * @param phoneNo
     * @param newPhoneNo
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] requestSendMsg(String terminalId,String tableId,
    		String expressNo,String phoneNo,String newPhoneNo)throws UnsupportedEncodingException {
        byte[] tmp = null;
        Integer seq=ServerUdpHander.getTerminalSeq(terminalId);
        if(seq==null){
        	seq=ProtocolUtil.getSeq();
        }
        byte[] header = getHeader(seq,true);
        byte[] type = new byte[]{TYPE_SENDMSG};
        byte[] content = (terminalId+Protocol.SPLIT
        		+tableId+SPLIT+
        		phoneNo+SPLIT+
        		expressNo+SPLIT+
        		newPhoneNo).getBytes(CODE);
        byte[] len = ProtocolUtil.intToBytes2(content.length);
        tmp = ProtocolUtil.merge(header, type);
        tmp = ProtocolUtil.merge(tmp, len);
        tmp = ProtocolUtil.merge(tmp, content);
        tmp = ProtocolUtil.merge(tmp, ProtocolUtil.toAbstract(tmp));
        return tmp;
    }

    /**
     * 响应上传文件
     *
     * @param seq 序号
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] responseUploadFile(int seq) throws UnsupportedEncodingException {
        byte[] tmp = null;
        byte[] header = getHeader(seq,false);
        byte[] type = new byte[]{TYPE_UPLOADFILE};
        byte[] content = (OK).getBytes(CODE);
        byte[] len = ProtocolUtil.intToBytes2(content.length);
        tmp = ProtocolUtil.merge(header, type);
        tmp = ProtocolUtil.merge(tmp, len);
        tmp = ProtocolUtil.merge(tmp, content);
        tmp = ProtocolUtil.merge(tmp, ProtocolUtil.toAbstract(tmp));
        return tmp;
    }
    /**
     * 请求上传文件
     * @param terminalId
     * @param fileType
     * @param path
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] requestUploadFile(String terminalId,String fileType,String path) throws UnsupportedEncodingException{
    	 byte[] tmp = null;
    	 Integer seq=ServerUdpHander.getTerminalSeq(terminalId);
         if(seq==null){
         	seq=ProtocolUtil.getSeq();
         }
         byte[] header = getHeader(seq,true);
         byte[] type = new byte[]{TYPE_UPLOADFILE};
         byte[] content = (terminalId+SPLIT+fileType+SPLIT+path).getBytes(CODE);
         byte[] len = ProtocolUtil.intToBytes2(content.length);
         tmp = ProtocolUtil.merge(header, type);
         tmp = ProtocolUtil.merge(tmp, len);
         tmp = ProtocolUtil.merge(tmp, content);
         tmp = ProtocolUtil.merge(tmp, ProtocolUtil.toAbstract(tmp));
         return tmp;
    }

    /**
     * 上传柜子信息
     *
     * @param seq 序号
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] responseUploadTabelInfo(int seq) throws UnsupportedEncodingException {
        byte[] tmp = null;
        byte[] header = getHeader(seq,false);
        byte[] type = new byte[]{TYPE_UPLOADTABLE};
        byte[] content = (OK).getBytes(CODE);
        byte[] len = ProtocolUtil.intToBytes2(content.length);
        tmp = ProtocolUtil.merge(header, type);
        tmp = ProtocolUtil.merge(tmp, len);
        tmp = ProtocolUtil.merge(tmp, content);
        tmp = ProtocolUtil.merge(tmp, ProtocolUtil.toAbstract(tmp));
        return tmp;
    }

    /**
     * 响应超时件
     *
     * @param seq 序号
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] responseUploadOvertime(int seq) throws UnsupportedEncodingException {
        byte[] tmp = null;
        byte[] header = getHeader(seq,false);
        byte[] type = new byte[]{TYPE_UPLOADFOROVERTIME};
        byte[] content = (OK).getBytes(CODE);
        byte[] len = ProtocolUtil.intToBytes2(content.length);
        tmp = ProtocolUtil.merge(header, type);
        tmp = ProtocolUtil.merge(tmp, len);
        tmp = ProtocolUtil.merge(tmp, content);
        tmp = ProtocolUtil.merge(tmp, ProtocolUtil.toAbstract(tmp));
        return tmp;
    }

    /**
     * 请求设置超时
     *
     * @param time 时间
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] requestSetOvertime(Long time) throws UnsupportedEncodingException {
        byte[] tmp = null;
        byte[] header = getHeader(ProtocolUtil.getSeq(),true);
        byte[] type = new byte[]{TYPE_SETOVERTIME};
        byte[] content = (time.toString()).getBytes(CODE);
        byte[] len = ProtocolUtil.intToBytes2(content.length);
        tmp = ProtocolUtil.merge(header, type);
        tmp = ProtocolUtil.merge(tmp, len);
        tmp = ProtocolUtil.merge(tmp, content);
        tmp = ProtocolUtil.merge(tmp, ProtocolUtil.toAbstract(tmp));
        return tmp;
    }

    /**
     * 响应发送状态
     *
     * @param seq 序号
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] responseUploadMessage(int seq) throws UnsupportedEncodingException {
        byte[] tmp = null;
        byte[] header = getHeader(seq,false);
        byte[] type = new byte[]{TYPE_UPLOADMSGSTA};
        byte[] content = (OK).getBytes(CODE);
        byte[] len = ProtocolUtil.intToBytes2(content.length);
        tmp = ProtocolUtil.merge(header, type);
        tmp = ProtocolUtil.merge(tmp, len);
        tmp = ProtocolUtil.merge(tmp, content);
        tmp = ProtocolUtil.merge(tmp, ProtocolUtil.toAbstract(tmp));
        return tmp;
    }
    /**
     * 响应上传用户信息
     * @param seq
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] responseUploadUser(int seq) throws UnsupportedEncodingException {
        byte[] tmp = null;
        byte[] header = getHeader(seq,false);
        byte[] type = new byte[]{TYPE_UPLOADUSER};
        byte[] content = (OK).getBytes(CODE);
        byte[] len = ProtocolUtil.intToBytes2(content.length);
        tmp = ProtocolUtil.merge(header, type);
        tmp = ProtocolUtil.merge(tmp, len);
        tmp = ProtocolUtil.merge(tmp, content);
        tmp = ProtocolUtil.merge(tmp, ProtocolUtil.toAbstract(tmp));
        return tmp;
    }

    /**
     * 得到服务器发往终端的报头
     *@param 序号
     *@param resend 是否重发 主动发送的报文要求重发
     * @return
     */
    private static byte[] getHeader(int sn,boolean resend) {
        byte[] tmp = new byte[5];
        tmp[0] = HEADER;
        byte[] seq = ProtocolUtil.intToBytes2(sn);
        byte[] way = null;
        if(resend){
        	way=new byte[]{SERVER,TERMINAL};
        }else{
        	way=new byte[]{TERMINAL,SERVER};
        }

        System.arraycopy(seq, 0, tmp, 1, seq.length);
        System.arraycopy(way, 0, tmp, seq.length + 1, way.length);
        return tmp;
    }
    
	private static byte[] getContent(Map<String, String> d) {
		StringBuilder sb = new StringBuilder();
		for (String key : d.keySet()) {
			String value = d.get(key);
			try {
				sb.append(URLEncoder.encode(key, "UTF-16LE"));
				sb.append("=");
				sb.append(URLEncoder.encode(value, "UTF-16LE"));
				sb.append("&");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		if (sb.length() > 1) {
			sb.deleteCharAt(sb.length() - 1);
		}
		try {
			return sb.toString().getBytes("ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new byte[0];
	}

	private static byte[] make(byte[] header, byte[] type, byte[] content) {
		byte[] buf = ProtocolUtil.merge(header, type);
		buf = ProtocolUtil.merge(buf, ProtocolUtil.intToBytes2(content.length));
		buf = ProtocolUtil.merge(buf, content);
		return ProtocolUtil.merge(buf, ProtocolUtil.toAbstract(buf));
	}

	private static byte[] make(int seq, ServerData d) {
		byte[] header = getHeader(seq, true);
		byte[] type = new byte[] { Protocol.TYPE_DEFAULT };
		return make(header, type, getContent(d));
	}

	/**
	 * 将报文放入发送缓冲
	 * @param seq
	 * @param d
	 */
	public static void sendBuffer(int seq, ServerData d) {
		UdpServreListener.send(d.getTerminalId(), seq, make(seq, d));
	}
	
	/**
	 * 直接发送报文
	 * @param seq
	 * @param d
	 * @param ip
	 * @param port
	 */
	private static void sendImmediate(int seq, ServerData d, String ip, int port) {
		try {
			UdpServer.getInstance().send(make(seq, d), ip, port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 将报文放入发送缓冲
	 * @param d
	 * @return
	 */
	public static int send(ServerData d) {
		int seq = ProtocolUtil.getSeq();
		sendBuffer(seq, d);
		return seq;
	}
    
	/**
	 * 确认收到报文
	 * @param seq
	 * @param ip
	 * @param port
	 */
	public static void confirm(int seq, String ip, int port) {
		// 必须直接发送，因为“确认报文”不会返回确认信息，如果放入发送缓冲，将无限次重发
		sendImmediate(seq, new ServerData(null, Protocol.SERVER_RESPONSE), ip, port);
	}
	
}
