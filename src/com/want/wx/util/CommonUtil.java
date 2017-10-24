package com.want.wx.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class CommonUtil {
	private static Logger log = LoggerFactory.getLogger(CommonUtil.class);

	    // 凭证获取（GET）
	    public final static String token_url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=wxf0914a6fe4aab002&corpsecret=RS9Pjybb8yjZ7KwXIOBdBDEcYuI5aWRDlYqr4Pch_cpFEmdwuEL0GH64TW1EDV96";
	    /*requestUrl 请求地址 requestMethod 请求方式（GET、POST） outputStr 提交的数据 JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值) */
	    public static JSONObject httpsRequest(String requestUrl, String requestMethod, String outputStr) {
	        JSONObject jsonObject = null;
	        try {
	            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
	            TrustManager[] tm = { new MyX509TrustManager() };
	            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
	            sslContext.init(null, tm, new java.security.SecureRandom());
	            // 从上述SSLContext对象中得到SSLSocketFactory对象
	            SSLSocketFactory ssf = sslContext.getSocketFactory();

	            URL url = new URL(requestUrl);
	            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
	            conn.setSSLSocketFactory(ssf);
	            
	            conn.setDoOutput(true);
	            conn.setDoInput(true);
	            conn.setUseCaches(false);
	            // 设置请求方式（GET/POST）
	            conn.setRequestMethod(requestMethod);

	            // 当outputStr不为null时向输出流写数据
	            if (null != outputStr) {
	                OutputStream outputStream = conn.getOutputStream();
	                // 注意编码格式
	                outputStream.write(outputStr.getBytes("UTF-8"));
	                outputStream.close();
	            }

	            // 从输入流读取返回内容
	            InputStream inputStream = conn.getInputStream();
	            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
	            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	            String str = null;
	            StringBuffer buffer = new StringBuffer();
	            while ((str = bufferedReader.readLine()) != null) {
	                buffer.append(str);
	            }

	            // 释放资源
	            bufferedReader.close();
	            inputStreamReader.close();
	            inputStream.close();
	            inputStream = null;
	            conn.disconnect();
	            jsonObject = JSONObject.fromObject(buffer.toString());
	        } catch (ConnectException ce) {
	            log.error("连接超时：{}", ce);
	        } catch (Exception e) {
	            log.error("https请求异常：{}", e);
	        }
	        return jsonObject;
	    }

	    /* 获取接口访问凭证     appid 凭证 appsecret 密钥 */
	    public static String getToken(String appid, String appsecret) {
	        String accesstoken = null;
	        String requestUrl = token_url.replace("APPID", appid).replace("APPSECRET", appsecret);
	        // 发起GET请求获取凭证
	        JSONObject jsonObject = httpsRequest(requestUrl, "GET", null);

	        if (null != jsonObject) {
	            try {           	
	                accesstoken=jsonObject.getString("access_token");
	            } catch (JSONException e) {
	                log.error("获取token失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
	            }
	        }
	        return accesstoken;
	    }
	    
	    
	    //oauth2验证获取用户员工信息
		public static String URL = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=ACCESS_TOKEN&code=CODE";		
	    /**
		 * 根据code获取成员信息
		 * 		
		 */
		public static String getUserByCode(String token, String code) {
			String userid = null;
			String menuUrl = URL.replace("ACCESS_TOKEN", token).replace("CODE", code);			
			JSONObject jsonObject = httpsRequest(menuUrl, "GET", null);		
			if (null != jsonObject) {
	            try {           	
	            	userid=jsonObject.getString("UserId");
	            } catch (JSONException e) {
	                e.printStackTrace();
	            }
	        }
			return userid;			
		}

		
		
	  	      
	/*    
	 	//根据userid获取员工信息
	    public static String getWxEmp(String accesstoken, String userid) {   	
			String result = "";
			try {
				String urlName = "https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token="+accesstoken+"&userid="+userid;
				URL U = new URL(urlName);
				URLConnection connection = U.openConnection();
				connection.connect();
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String line;
				while ((line = in.readLine()) != null) {
					result += line;
				}
				in.close();
				if(result.indexOf("error")>-1){
					result = null;
				}
			} catch (Exception e) {
				System.out.println("获取员工信息失败！" + e);
				result = null;
			}
			System.out.println(result);
			return result;
		}
		*/
		
		
}
