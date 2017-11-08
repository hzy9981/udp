/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jinmei.s2pdsudp.listener;

import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.jinmei.s2pdsudp.hander.TcpHander;

/**
 *服务端分析
 *@author hanzy
 */
public class TcpServreListener implements Observer{
    ExecutorService pool=null;
    public TcpServreListener(Observable observable) {
        observable.addObserver(this);
        pool=Executors.newCachedThreadPool();
    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof Socket)
        pool.execute(new TcpHander((Socket)arg));
    }
    
    
}
