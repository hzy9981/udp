package com.jinmei.s2pdsudp.hander;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * method-class映射
 * 
 * @author : cloud
 * @Created : 2015年4月9日
 */
public class Mapping {

	public static Logger log = Logger.getLogger(Mapping.class);

	public static final Map<String, Server> map = new ConcurrentHashMap<String, Server>();

	static {
		// 输出版本识别号
		log.info("-- > s2pdsudp.jar version：2.2");
	}

	/**
	 * 解析method-class映射
	 * 
	 * @param file
	 * @throws Exception
	 */
	public static void bulid(String file) throws Exception {
		InputStream is = null;
		try {
			is = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream(file);
			bulid(is, new DefaultServerFactory());
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * 解析method-class映射
	 * 
	 * @param is
	 * @param fac
	 * @throws Exception
	 */
	public static void bulid(InputStream is, ServerFactory fac)
			throws Exception {
		Document doc = null;
		doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
				.parse(is);
		NodeList services = doc.getElementsByTagName("service");
		for (int i = 0; i < services.getLength(); i++) {
			String method = null;
			String cls = null;
			Node service = services.item(i);
			NodeList childNodes = service.getChildNodes();
			for (int j = 0; j < childNodes.getLength(); j++) {
				Node node = childNodes.item(j);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					String name = node.getNodeName();
					if ("method".equals(name)) {
						method = node.getFirstChild().getNodeValue().trim();
					} else if ("class".equals(name)) {
						cls = node.getFirstChild().getNodeValue().trim();
					}
				}
			}
			if (method != null && cls != null) {
				map.put(method, fac.create(cls));
			}
		}
	}

	/**
	 * 返回method对应的实例
	 * 
	 * @param method
	 * @return
	 */
	public static Server getServer(String method) {
		return map.get(method);
	}
}
