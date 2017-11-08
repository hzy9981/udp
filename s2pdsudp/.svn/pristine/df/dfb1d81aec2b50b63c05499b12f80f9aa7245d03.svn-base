package com.jinmei.s2pdsudp.iface;


/**
 * 终端通讯接口
 * @author hanzy
 *
 */
public interface TerminalSrv {

	/**
	 * 当新终端到来，无论服务器终端信息是否存在均会上报
	 * 更新数据库状态，处理完后将自动回复心跳响应
	 * @param terminalId 终端ID
	 * @param string 终端软件版本 
	 * @param string2 状态信息（在线【时间yyyy-MM-dd HH:mm:ss】）
	 */
	void onTerminalOnline(String terminalId, String string, String string2);

	/**
	 * 修改卡终端卡信息
	 * @param string
	 * @param string2
	 * @param string3
	 */
	void onServerUpdTerminalCardInfo(String string, String string2, String string3);

	/**
	 * 上报存件信息,处理完后将自动回复终端
	 * @param string 终端号
	 * @param string2 柜子号
	 * @param string3 存件人
	 * @param string4 快递单号
	 * @param string5 收件人号码
	 * @param string6 取件密码
	 * @param string7 存件人图片 ，图片名称包含存件时间，时间格式为（yyyyMMddHHmmssS）
	 */
	void onUploadSave(String string, String string2, String string3,
			String string4, String string5, String string6, String string7);

	/**新增终端信息
	 * @param string
	 * @param string2
	 */
	void addTerminalInfo(String string, String string2);

	/**
	 * 自动更新时，更新自动更新状态，处理完后将自动回复终端
	 * @param string 终端ID
	 * @param string2 更新信息（参考协议说明）
	 */
	void onTerminalUpdateState(String string, String string2);

	/**更新终端离线信息，当离线时触发该函数
	 * @param terminalId 终端ID
	 * @param string 离线【时间详细格式yyyy-MM-dd HH:mm:ss 】
	 */
	void onTerminalOffline(String terminalId, String string);

	/**上传取件信息，处理完后将自动回复终端
	 * @param string 终端号
	 * @param string2 柜子号
	 * @param string3 快递单号
	 * @param string4 取件人图片，图片名称包含取件时间，时间格式为（yyyyMMddHHmmssS）
	 * @param string5 取件类型（1快递员取超时/拒收件，2客户正常取件）
	 */
	String onUploadPick(String string, String string2, String string3,
			String string4, String string5);

	/**下发短信响应，终端回复下发短信后（重发短信或者错号登记）
	 * @param string 终端号
	 * @param string2 柜子号
	 * @param string3 手机号
	 * @param string4 快递单号
	 * @param string5 发送状态
	 */
	void onSendMsgResponse(String string, String string2, String string3,
			String string4, String string5);

	/**
	 * 上传超时件，处理完成后将自动回复终端信息
	 * @param string 终端号
	 * @param string2 柜子号
	 * @param string3 存件人卡号
	 * @param string4 快递单号
	 * @param string5 超时时间（时间格式 yyyyMMddHHmmssS）
	 */
	void onUploadOvertime(String string, String string2, String string3,
			String string4, String string5);

	/**上传短信发送信息，处理完后将自动回复终端
	 * @param string 终端号
	 * @param string2 柜子号
	 * @param string3 手机号
	 * @param string4 快递单号
	 * @param string5 发送状态
	 * @param string6 类型
	 * @param string7 操作时间
	 */
	void onUploadMsg(String string, String string2, String string3,
			String string4, String string5, String string6, String string7);

	/**上报用户信息，处理完将自动回复终端
	 * @param string	终端号
	 * @param string2	卡号
	 * @param string3	电话号码
	 * @param string4	姓名
	 * @param string5	所属公司
	 * @param string6	操作密码
	 * @param string7	是否有效
	 * @param string8	类型
	 * @param string9	PAID号
	 * @param string10	可存件量
	 * @param string11	超时时间（float类型）
	 */
	void onUploadCardInfo(String string, String string2, String string3,
			String string4, String string5, String string6, String string7,
			String string8, String string9, String string10, String string11);

	////////调取图片流程/////////
	///1.服务器请求图片
	///2.回复已收到响应（如有图片则触发onRequestImgStartRecv函数，否则触发onRequestImgNotfound函数）
	///3.接收完成触发onRequestImgComplete函数
	/**图片上传完成
	 * @param terminalId 终端号
	 * @param fileName 文件名称
	 * @param relativePath 相对服务器路径
	 */
	void onRequestImgComplete(String terminalId, String fileName, String relativePath);
	/**
	 * 未找到请求图片
	 * @param terminalId 终端号
	 * @param fileName 文件名称 
	 */
	void onRequestImgNotfound(String terminalId,String fileName);
	/**已找到图片，开始接收
	 * @param terminalId 终端号
	 * @param fileName 文件名称
	 */
	void onRequestImgStartRecv(String terminalId,String fileName);
	/**
	 * 当发送超时
	 * @param terminalId 终端号
	 * @param buf 发送的内容
	 * @param ip 发送时IP
	 * @param port	发送时端口
	 * 
	 */
	void onSendTimeout(String terminalId,byte[] buf,String ip,int port);
	/**
	 * 当发送成功
	 * @param terminalId 终端号
	 * @param buf 发送的内容
	 */
	void onSendSucess(String terminalId,byte[] buf);
	
}
