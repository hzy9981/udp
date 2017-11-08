package com.jinmei.s2pdsudp.util;

import java.io.File;
import java.io.IOException;

public class FileUtil {
	/**
     * 生产文件 如果文件所在路径不存在则生成路径
     * @param fileName 文件名 带路径
     * @param isDirectory 是否为路径
     * @return
     */
    public static File buildFile(String fileName, boolean isDirectory) {
        File target = new File(fileName);
        if (isDirectory) {
            target.mkdirs();
        } else {
            if (!target.getParentFile().exists()) {
                target.getParentFile().mkdirs();
                target = new File(target.getAbsolutePath());
            }
        }
        return target;
    }
    /**
     * 得到 当前路径
     * @return
     */
    public static String getCurrentPath(){
    	File f=new File("");
    	return f.getAbsolutePath();
    }
    
    /**
	 * 得到WEB应用程序的相对路径
	 */
	public static String getWebRootUrl() throws IOException{
		ClassLoader classLoader=Thread.currentThread().getContextClassLoader();
		String fullPath=classLoader.getResource("log4j.properties").getPath().replace("%20", " ");
		String currentPath=fullPath.substring(1, fullPath.indexOf("WEB-INF/classes/log4j.properties"));
		return currentPath;
	}
}
