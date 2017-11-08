/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jinmei.s2pdsudp.protocol;

/**
 *协议接口，定义了侦的结构
 * @author hanzy
 */
public interface Protocol {
	/**
	 * 报文发送类型，终端请求
	 */
	public static final String CLIENT_REQUEST = "CLIENT_REQUEST";
	/**
	 * 报文发送类型，终端响应
	 */
	public static final String CLIENT_RESPONSE = "CLIENT_RESPONSE";
	/**
	 * 报文发送类型，终端响应
	 */
	public static final String CLIENT_RESPONSE2 = "CLIENT_RESPONSE2";
	/**
	 * 报文发送类型，服务器请求
	 */
	public static final String SERVER_REQUEST = "SERVER_REQUEST";
	/**
	 * 报文发送类型，服务器响应
	 */
	public static final String SERVER_RESPONSE = "SERVER_RESPONSE";
	/**
	 * 报文发送类型，服务器响应
	 */
	public static final String SERVER_RESPONSE2 = "SERVER_RESPONSE2";
    /**
     * 编码方式
     */
    String CODE="utf-8";
    /**
     * 报头
     */
    byte HEADER=0x7e;
    /**
     * 终端
     */
    byte TERMINAL=0x01;
    /**
     * 服务器
     */
    byte SERVER=0x02;
    /**
     * 忽略的类型，表示使用新的交互方式
     */
    byte TYPE_DEFAULT = 0x00;
    /**
     * 心跳类型
     */
    byte TYPE_HEART=0x01;
    /**
     * 终端日志上报
     */
    byte TYPE_TERMINAL_LOG=0x02;
    /**
     * 更新
     */
    byte TYPE_UPDATE=0x03;
    /**
     * 终端控制
     */
    byte TYPE_TERMINAL_CONTROL=0x04;
    /**
     * 上报取件
     */
    byte TYPE_UPLOADFORPICK=0x05;
    /**
     * 上报存件信息
     */
    byte TYPE_UPLOADFORSAVE=0x06;
    /**
     * 上报拒收件
     */
    byte TYPE_UPLOADREJECT=0x07;
    /**
     * 设置用户
     */
    byte TYPE_SETUSER=0x08;
    /**
     * 上报错误信息
     */
    byte TYPE_UPLOADERROR=0x09;
    /**
     * 上传文件
     */
    byte TYPE_UPLOADFILE=0x0a;
    /**
     * 上报柜子信息
     */
    byte TYPE_UPLOADTABLE=0x0b;
    /**
     * 上传超时件
     */
    byte TYPE_UPLOADFOROVERTIME=0x0c;
    /**
     * 设置超时时间
     */
    byte TYPE_SETOVERTIME=0x0d;
    /**
     * 上传发送短信状态
     */
    byte TYPE_UPLOADMSGSTA=0x0e;
    /**
     * 下发短信
     */
    byte TYPE_SENDMSG=0x0f;
    /**
     * 上传用户信息
     */
    byte TYPE_UPLOADUSER=0x10;
    /**
     * 上传新增的寄件信息
     */
    byte TYPE_INSSENDINFO=0x11;
    /**
     * 上传编辑寄件信息
     */
    byte TYPE_UPDSENDINFO=0x12;
    //接收到OK
    String OK="OK";
    /**
     * 分割符号
     */
    String SPLIT="|";
    /**
     * 反解分割符号
     */
    String DESPLIT="\\|";
}
