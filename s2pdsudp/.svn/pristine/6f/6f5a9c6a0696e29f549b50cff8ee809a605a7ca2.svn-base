package com.jinmei.s2pdsudp.protocol;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 报文
 * 
 * @author : cloud
 * @Created : 2015年4月1日
 */
@SuppressWarnings("serial")
public class ClientData extends HashMap<String, String> {

	/**
	 * 缓存收到报文的唯一标识，参数依次为terminalId、seq、uuid
	 */
	private static final Map<String, Map<String, String>> history = new HashMap<String, Map<String, String>>();

	private ClientData() {
	}

	/**
	 * 是否已经接受过此报文（只调用一次有效）
	 * 
	 * @param seq
	 * @return
	 */
	public boolean isDuplicated(String seq) {
		Map<String, String> map = null;
		String t = getTerminalId();
		synchronized (history) {
			if (history.get(t) == null) {
				map = new HashMap<String, String>();
				history.put(t, map);
			} else {
				map = history.get(t);
			}
		}
		synchronized (map) {
			String old_uuid = map.get(seq);
			if (old_uuid == null || !old_uuid.equals(get("_sys_uuid"))) {
				map.put(seq, get("_sys_uuid"));
				return false;
			} else {
				return true;
			}
		}
	}

	/**
	 * 获得终端编号
	 * 
	 * @return
	 */
	public String getTerminalId() {
		return get("_sys_terminalId");
	}

	/**
	 * 获得报文发送类型
	 * 
	 * @return
	 */
	public String getType() {
		return get("_sys_type");
	}

	/**
	 * 客户端请求调用的方法
	 * 
	 * @return
	 */
	public String getMethod() {
		return get("_sys_method");
	}

	@Override
	public String put(String key, String value) {
		// null转换为空字符
		if (value == null) {
			value = "";
		}
		return super.put(key, value);
	}

	/**
	 * 解析报文内容
	 */
	public static ClientData newInstance(byte[] content)
			throws UnsupportedEncodingException {
		ClientData d = new ClientData();
		String msg = new String(content, "ISO-8859-1");
		String[] msgs = msg.split("&");
		for (String en : msgs) {
			String key = en.substring(0, en.indexOf("="));
			String value = en.substring(en.indexOf("=") + 1);
			d.put(URLDecoder.decode(key, "UTF-16LE"),
					URLDecoder.decode(value, "UTF-16LE"));
		}
		return d;
	}
}
