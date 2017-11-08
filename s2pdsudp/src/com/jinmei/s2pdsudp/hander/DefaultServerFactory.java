package com.jinmei.s2pdsudp.hander;

/**
 * 按类名创建Server
 * 
 * @author : cloud
 * @Created : 2015年5月6日
 */
public class DefaultServerFactory implements ServerFactory {

	@SuppressWarnings("unchecked")
	@Override
	public Server create(String name) throws Exception {
		return ((Class<Server>) Class.forName(name)).newInstance();
	}

}
