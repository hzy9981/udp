package com.jinmei.s2pdsudp.hander;

/**
 * 创建Server对象的工厂
 * 
 * @author : cloud
 * @Created : 2015年5月6日
 */
public interface ServerFactory {

	/**
	 * 创建Server对象
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	Server create(String name) throws Exception;
}
