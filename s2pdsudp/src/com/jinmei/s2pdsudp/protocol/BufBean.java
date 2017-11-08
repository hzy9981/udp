/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jinmei.s2pdsudp.protocol;

import java.io.Serializable;
import java.util.Arrays;

/**
 *发送缓冲
 * @author hanzy
 */
public class BufBean implements Serializable{
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(buf);
		result = prime * result + (int) (lastSendTime ^ (lastSendTime >>> 32));
		result = prime * result + sendTimes;
		result = prime * result + seq;
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BufBean other = (BufBean) obj;
		if (!Arrays.equals(buf, other.buf))
			return false;
		if (lastSendTime != other.lastSendTime)
			return false;
		if (sendTimes != other.sendTimes)
			return false;
		if (seq != other.seq)
			return false;
		return true;
	}


	//序号
    private int seq;
    private int sendTimes;
    //需要发送的数据
    private byte[] buf;
    private  long lastSendTime;

    public BufBean(int seq, byte[] buf) {
        this.seq = seq;
        this.buf = buf;
    }

    
    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public byte[] getBuf() {
        return buf;
    }

    public void setBuf(byte[] buf) {
        this.buf = buf;
    }

    public int getSendTimes() {
        return sendTimes;
    }

    public void setSendTimes(int sendTimes) {
        this.sendTimes = sendTimes;
    }


	public long getLastSendTime() {
		return lastSendTime;
	}


	public void setLastSendTime(long lastSendTime) {
		this.lastSendTime = lastSendTime;
	}
    
    
}
