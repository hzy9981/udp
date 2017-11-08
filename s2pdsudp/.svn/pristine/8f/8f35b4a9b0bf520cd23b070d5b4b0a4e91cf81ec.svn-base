/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jinmei.s2pdsudp;

import java.io.IOException;

/**
 *
 *@author hanzy
 */
public interface NetServer{
    /**
     * UDP端口
     */
    public int UDP_PORT=3000;
    /**
     * TCP端口
     */
    public int TCP_PORT=4000
    		;
    /**
     * 缓冲区大小
     */
    public int BUF_LEN=1024*2;
    /**
     * 启动
     */
    public void start();
    /**
     * 停止服务器
     */
    public void stop();
    /**
     * 发送消息
     * @param msg 消息
     * @param ip 目的地址
     * @param port  目的端口
     * @throws IOException 
     */
    public void send(String msg,String ip,int port)throws IOException;
    /**
     * 发送文件
     * @param buf 要发送的文件
     * @param ip IP地址
     * @param port  端口
     * @throws IOException 
     */
    public void send(byte[] buf,String ip,int port) throws IOException;
}
