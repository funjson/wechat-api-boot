package cn.funjson.wechatapiboot.common.utils.demo;

public class Sample {

	public static void main(String[] args) throws Exception {
		String sToken = "weixinYKP";
		String sCorpID = "wx6b847d8754f9747d";
		String sEncodingAESKey = "QJmlSYjTsNSC1w0GlPt87r6NxHBOTT334oylsozNYeQ";

		WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(sToken, sEncodingAESKey, sCorpID);
		/*
		------------使用示例一：验证回调URL---------------
		*企业开启回调模式时，企业号会向验证url发送一个get请求 
		假设点击验证时，企业收到类似请求：
		* GET /cgi-bin/wxpush?msg_signature=5c45ff5e21c57e6ad56bac8758b79b1d9ac89fd3&timestamp=1409659589&nonce=263014780&echostr=P9nAzCzyDtyTWESHep1vC5X9xho%2FqYX3Zpb4yKa9SKld1DsH3Iyt3tP3zNdtp%2B4RPcs8TgAE7OaBO%2BFZXvnaqQ%3D%3D 
		* HTTP/1.1 Host: qy.weixin.qq.com

		接收到该请求时，企业应		1.解析出Get请求的参数，包括消息体签名(msg_signature)，时间戳(timestamp)，随机数字串(nonce)以及公众平台推送过来的随机加密字符串(echostr),
		这一步注意作URL解码。
		2.验证消息体签名的正确性 
		3. 解密出echostr原文，将原文当作Get请求的response，返回给公众平台
		第2，3步可以用公众平台提供的库函数VerifyURL来实现。

		*/
		// 解析出url上的参数值如下：
		// String sVerifyMsgSig = HttpUtils.ParseUrl("msg_signature");
		String sVerifyMsgSig = "c846ec144d0566ada1064b56d6c2682c6d7b8af1";
		// String sVerifyTimeStamp = HttpUtils.ParseUrl("timestamp");
		String sVerifyTimeStamp = "1613987168";
		// String sVerifyNonce = HttpUtils.ParseUrl("nonce");
		String sVerifyNonce = "976279043";
		// String sVerifyEchoStr = HttpUtils.ParseUrl("echostr");
		String sVerifyEchoStr = "3497238777128287602";
		String sEchoStr; //需要返回的明文
		try {
			sEchoStr = wxcpt.verifyUrl(sVerifyMsgSig, sVerifyTimeStamp,
					sVerifyNonce, sVerifyEchoStr);
			System.out.println("verifyurl echostr: " + sEchoStr);
			// 验证URL成功，将sEchoStr返回
			// HttpUtils.SetResponse(sEchoStr);
		} catch (Exception e) {
			//验证URL失败，错误原因请查看异常
			e.printStackTrace();
		}
	}
}
