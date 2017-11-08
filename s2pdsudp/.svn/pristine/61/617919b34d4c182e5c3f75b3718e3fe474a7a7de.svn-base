package com.jinmei.s2pdsudp.protocol;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.jinmei.s2pdsudp.hander.CallBack;

/**
 * 报文
 * 
 * @author : cloud
 * @Created : 2015年4月1日
 */
@SuppressWarnings("serial")
public class ServerData extends HashMap<String, String> {

	/**
	 * （String）seq 与 回调函数 的映射
	 */
	public static final Map<String, Map<String, Map<String, CallBack>>> callbacks = new ConcurrentHashMap<String, Map<String, Map<String, CallBack>>>();

	/**
	 * 终端编号
	 */
	private String terminalId;

	/**
	 * 是否已发送
	 */
	private boolean sent = false;

	/**
	 * 是否设置了将要调用的终端方法
	 */
	private boolean isSetMethod = false;

	public ServerData(String terminalId, String type) {
		// 终端编号
		this.terminalId = terminalId;
		// 报文发送类型
		put("_sys_type", type);
		// 报文唯一标识
		put("_sys_uuid", UUID.randomUUID().toString());
	}

	/**
	 * 服务器发往终端的请求报文
	 * 
	 * @param terminalId
	 * @return
	 */
	public static ServerData newRequestData(String terminalId) {
		return new ServerData(terminalId, Protocol.SERVER_REQUEST);
	}

	/**
	 * 将数据发往终端
	 * 
	 * @return
	 */
	public int send(CallBack cb) {
		if (sent) {
			throw new RuntimeException("不能重复发送...");
		} else {
			sent = true;
		}
		if (!isSetMethod) {
			throw new RuntimeException("未设置将要调用的终端方法...");
		}
		Map<String, Map<String, CallBack>> en = callbacks.get(terminalId);
		if (en == null) {
			en = new ConcurrentHashMap<String, Map<String, CallBack>>();
			callbacks.put(terminalId, en);
		}
		int seq = ProtocolUtil.getSeq();
		String uuid = get("_sys_uuid");
		if (cb != null) {
			Map<String, CallBack> map = new ConcurrentHashMap<String, CallBack>();
			map.put(uuid, cb);
			en.put(new Integer(seq).toString(), map);
		}
		ServerProtocolUtil.sendBuffer(seq, this);
		return seq;
	}

	/**
	 * 将数据发往终端
	 * 
	 * @return
	 */
	public int send() {
		return send(null);
	}

	/**
	 * 获得终端编号
	 * 
	 * @return
	 */
	public String getTerminalId() {
		return terminalId;
	}

	/**
	 * 设置将要调用的终端方法
	 * 
	 * @param name
	 */
	public void setMethod(String name) {
		put("_sys_method", name);
		isSetMethod = true;
	}

	@Override
	public String put(String key, String value) {
		// null转换为空字符
		if (value == null) {
			value = "";
		}
		return super.put(key, value);
	}

	@Override
	public String toString() {
		return "ServerData [terminalId=" + terminalId + ", sent=" + sent
				+ ", isSetMethod=" + isSetMethod + ", " + super.toString()
				+ "]";
	}

}
