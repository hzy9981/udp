/**
 * 
 */
package com.jinmei.s2pdsudp.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Properties文件配置类<br>
 * <h2>用法举例：</h2><br>
 * 加载classpath下的test.properties文件<br>
 * PropertiesConfiguration pc=new PropertiesConfiguration().config("test.properties");<br>
 * pc.getProperty(key) //得到key的值<br>
 * pc.updProperties(key,value);//更新key，value值对<br>
 * Map<String,String> allProperties=pc.getAllProperties();//得到"test.properties"所有的键值对<br>
 * @author hanzy
 *
 */
public class PropertiesConfiguration {

//	properties文件对象
	private static Properties config;
//	输入流
	private static InputStream inStream;
//	输出流
	private static OutputStream outStream;

//	资源名称
	private String resourceName;
	/**
	 * 加载资源文件
	 * @param resourceName 资源文件名称
	 */
	public PropertiesConfiguration(String resourceName) {
		this.resourceName=resourceName;
		this.config(resourceName);		
	}
	/**
	 * 加载配置文件
	 * @param resourceName 资源名称
	 */
	private void config(String resourceName){
		config=new Properties();
		inStream=Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName);
		try {
			config.load(inStream);
		} catch (FileNotFoundException e){
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				inStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 用指定的键在此属性列表中搜索属性。如果在此属性列表中未找到该键，
	 * 则接着递归检查默认属性列表及其默认值。如果未找到属性，则此方法返回 null。 
	 * @param key 属性键
	 * @param resourceName 资源文件名称
	 * @return 返回列表中具有指定键的值
	 */
	public  String getProperty(String key){
		return config.getProperty(key);
	}
	/**
	 * 返回所有的Properties
	 * @param resourceName 资源文件名称
	 * @return 返回指定Properties文件中的所有的键值对
	 */
	public  Map<String,String> getAllProperties(){
		Map<String,String> tmp=new HashMap<String,String>();
		Set<String> keys=config.stringPropertyNames();
		for(String key:keys){
			tmp.put(key, config.getProperty(key));
		}
		return tmp;
	}
	/**
	 * 修改Properties文件中的值,如果对于的键值存在则修改值，否则新增一条键值<br>
	 * bug:对于WEB应用程序，修改此值时可能会出现空指针异常。
	 * @param key 属性键
	 * @param value 目标值
	 * @return 修改成功返回true,否则返回false
	 * @throws FileNotFoundException 
	 */
	@SuppressWarnings("static-access")
	public  void updProperties(String key,String value) throws FileNotFoundException{
		ClassLoader classLoader=Thread.currentThread().getContextClassLoader();
		outStream=new FileOutputStream(classLoader.getSystemResource(resourceName).getFile());
		config.setProperty(key, value);
		try {
			config.store(outStream, "Last Update Or Add "+key+"="+value);
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				outStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
