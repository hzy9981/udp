package com.jinmei.s2pdsudp.hander;

import com.jinmei.s2pdsudp.protocol.ClientData;

/**
 * 回调
 * 
 * @author : cloud
 * @Created : 2015年4月13日
 */
public interface CallBack {

	void handle(ClientData obj);
}