/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jinmei.s2pdsudp.hander;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jinmei.s2pdsudp.listener.TcpServreListener;

/**
 *TCP处理函数
 *@author hanzy
 */
public class TcpHander implements Runnable{
        private Socket socket=null;
        private  int BUF_LEN=4096;
        public TcpHander(Socket socket){
            this.socket=socket;
        }
        @Override
        public void run() {
            InetAddress add=socket.getInetAddress();
            int port=socket.getPort();
              String addr=add.getHostAddress();
            try {
                InputStream in=socket.getInputStream();
                byte[] buf=new byte[BUF_LEN];
                int len=in.read(buf);
                System.out.println(addr+":"+port);
                while(len!=-1){
                    System.out.println(new String(buf,"utf-8").trim());
                    len=in.read(buf);
                }
                
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(TcpServreListener.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException("不支持字符编码", ex);
            } catch (IOException ex) {
                Logger.getLogger(TcpServreListener.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException("IO异常", ex);
            }
        }
        
    }
