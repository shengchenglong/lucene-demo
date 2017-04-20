package com.scl.chapter2_3;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * chapter 2.3.2 网页更新
 * @author shengchenglong
 *
 */
public class LastModifiedPage {

	/**
	 * 获取网页更新时间
	 * @param path
	 * @throws IOException
	 */
	public static void getPageLastModifiedTime(String path) throws IOException {
		URL url = new URL(path);
		
		// 使用jdk自带包来获取信息
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("HEAD");
		// connection.getLastModified()返回时间戳
		System.out.println("jdk自带包： " + url + " 更新时间：" + new Date(connection.getLastModified()));
		System.out.println("+++++++++++++++++++");
		
		// ------------------------------
		// 使用HttpClient来获取header信息
		CloseableHttpClient client = HttpClients.createDefault();
		HttpHead head = new HttpHead(path);
		CloseableHttpResponse response = client.execute(head);
		
		// 获取所有header
		Header[] headers = response.getAllHeaders();
		for(Header header : headers) {
			System.out.println(header);
			System.out.println("header name: " + header.getName() + "\nheader value: " + header.getValue());
			System.out.println("--------------------------");
		}
		
		// 获取指定header
		Header[] eHeaders = response.getHeaders("Last-Modified");
		for(Header header : eHeaders) {
			System.out.println(header);
			System.out.println("**************************");
		}
		response.close();
		client.close();
	}
	
	/**
	 * 在get请求中添加头信息If-Modified-Since参数，可以在Web服务端验证页面是否修改
	 * 若有修改，返回200 和 新的内容
	 * 若没有修改，返回304
	 * @param path
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static void getStatusByLastModified(String path) throws ClientProtocolException, IOException {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet get = new HttpGet(path);
		get.addHeader(new Header() { // 这里的Header并不是deader请求，而是头信息参数
			@Override
			public String getValue() {
				return "Thu, 13 Apr 2017 09:25:12 GMT";
			}
			@Override
			public String getName() {
				return "If-Modified-Since";
			}
			@Override
			public HeaderElement[] getElements() throws ParseException {
				return null;
			}
		});
		CloseableHttpResponse httpResponse = client.execute(get);
		System.out.println("httpResponse: "  + httpResponse);
		// 如果返回包含网页内容，说明有修改，将其打印出来
		HttpEntity httpEntity = httpResponse.getEntity();
		if(httpEntity != null) {
			System.out.println(EntityUtils.toString(httpEntity, "utf-8"));
			EntityUtils.consume(httpEntity);
		}
		httpResponse.close();
		client.close();
	}
	
	
	
	public static void main(String[] args) throws IOException {
//		LastModifiedPage.getPageLastModifiedTime("http://www.ouyeelintl.com");
		getStatusByLastModified("http://www.ouyeelintl.com");
	}
}
