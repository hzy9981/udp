package com.jinmei.s2pdsudp.iface;

import com.jinmei.s2pdsudp.exception.UdpException;

/**
 * UDP状态监听
 * @author hanzy
 *
 */
public interface UdpListener {

	public void onSendException(byte[] packet,Exception ex) throws UdpException;
	public void onSendSuccess(byte[] packet,Exception ex)  throws UdpException;
}
