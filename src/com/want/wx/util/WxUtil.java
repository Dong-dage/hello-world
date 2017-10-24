package com.want.wx.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.springframework.stereotype.Component;

@Component
public class WxUtil {

	public String getWxList(String ACCESS_TOKEN,String wechat_tagid){
		String result = "";
		try{
			String urlName = "https://qyapi.weixin.qq.com/cgi-bin/tag/get?access_token="+ACCESS_TOKEN+"&tagid="+wechat_tagid;
			URL u = new URL(urlName);
			//打开和RUL之间的连接
			URLConnection connection = u.openConnection();
			connection.connect();
			//获取输入流
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while((line = in.readLine()) != null){
				result += line;
			}
			in.close();
			if(result.indexOf("error")>-1){
				result = null;
			}
		}catch(Exception e){
			System.out.println("获取员工工号失败！" +e);
			result = null;
		}
		System.out.println(result);
		return result;
	}
}
