package com.jinmei.s2pdsudp.hander;

import com.jinmei.s2pdsudp.protocol.ClientData;
import com.jinmei.s2pdsudp.protocol.ServerData;

/**
 * 终端向服务器发送请求时执行
 * @author : cloud
 * @Created : 2015年4月9日
 */
public interface Server {

	/**
	 * 终端向服务器发送请求时执行
	 * @param req 终端发送的数据
	 * @param res 返回的数据
	 */
	void service(ClientData req, ServerData res);
}
