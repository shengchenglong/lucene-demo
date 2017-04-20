package com.scl.chapter2_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * chapter 2.3.1
 * 下载网页的基本方法
 * 
 * chapter 2.3.2
 * 网页更新的判断
 * @author shengchenglong
 *
 */
public class RetrievePage {
	
	/**
	 * 网页下载原理
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static String downloadPage(String path) throws IOException {
		// 创建url链接
		URL url = new URL(path);
		
		// 创建网络流，读取流
		// 该方法使用了HTTP的GET命令
		InputStream is = url.openStream();
		Reader reader = new InputStreamReader(is);
		BufferedReader bReader = new BufferedReader(reader);
		
		String line;
		StringBuilder sb = new StringBuilder();
		while ((line = bReader.readLine()) != null) {
			sb.append(line);
			sb.append("\n");
		}
		
		return sb.toString();
	}
	
	/**
	 * 使用Scanner对象下载网页
	 * @param path
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static String downloadPageByScaner(String path) throws IOException {
		URL url = new URL(path);
		InputStream is = url.openStream();
		Reader reader = new InputStreamReader(is, "utf-8");
		Scanner scanner = new Scanner(reader);
		scanner.useDelimiter("\\z"); // 可以用正则表达式分段读取网页
		StringBuilder sb = new StringBuilder();
		while (scanner.hasNext()) {
			sb.append(scanner.nextLine());
			sb.append("\n");
		}
		return sb.toString();
	}
	
	/**
	 * 直接使用套接字向web服务器发送GET请求获取网页内容
	 * @param path
	 * @return
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static String downloadPageBySocket(String path) throws UnknownHostException, IOException {
		int port = 80;
		// 网页路径（不方便暴露）
		String file = "";
		
		Socket s = new Socket(path, port);
		OutputStream os = s.getOutputStream();
		PrintWriter printWriter = new PrintWriter(os, false);
		
		printWriter.print("GET " + file + " HTTP/1.0\r\n");
		printWriter.print("Accept: text/plain, text/html, text/*\r\n");
		printWriter.print("\r\n");
		printWriter.flush(); // 发送get命令
		
		InputStream is = s.getInputStream();
		InputStreamReader isReader = new InputStreamReader(is);
		BufferedReader bReader = new BufferedReader(isReader);
		
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = bReader.readLine()) != null) {
			sb.append(line);
			sb.append("\n");
		}
		return sb.toString();
	}
	
	/**
	 * 从URLConnection中获取head中的信息
	 * @param path
	 * @param fieldKey
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	public static String getHeaderField(String path, String fieldKey) throws IOException {
		URL url = new URL(path);
		
		URLConnection connection = url.openConnection();
		Map<String,List<String>> header = connection.getHeaderFields();
		
		Iterator<String> i = header.keySet().iterator();
		String key;
		while (i.hasNext()) {
			key = i.next();
			if(fieldKey == null) {
				return header.get(null).get(0);
			} else {
				return header.get(fieldKey).get(0);
			}
		}
		return null;
	}
	
	/**
	 * 使用开源项目HttpClient来抓取网页
	 * @param path
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static void downloadPagebyHttpClient(String path) throws ClientProtocolException, IOException {
		
		// 创建客户端，相当于打开一个浏览器
		CloseableHttpClient httpClient = HttpClients.createDefault();
		// 创建一个GET方法，相当于地址栏输入地址
		HttpGet httpGet = new HttpGet(path);
		// 获取网页内容，相当于回车
		CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
		// 查看返回内容，相当于查看网页源代码
		HttpEntity httpEntity = httpResponse.getEntity();
		
		if(httpEntity != null) {
			// 读入内容流，返回字符串，指定网页编码“utf-8”
			System.out.println(EntityUtils.toString(httpEntity, "utf-8"));
			// 关闭内容流
			EntityUtils.consume(httpEntity);
		}
		
		// 关闭链接，释放资源
		httpResponse.close();
		httpClient.close();
	}
	
	
	
	public static void main(String[] args) throws IOException {
//		System.out.println(RetrievePage.downloadPage("http://www.ouyeelintl.com"));
//		System.out.println(RetrievePage.downloadPageByScaner("http://www.ouyeelintl.com"));
		System.out.println(RetrievePage.downloadPageBySocket("www.ouyeelintl.com"));
//		System.out.println(RetrievePage.getHeaderField("http://www.ouyeelintl.com", "Content-Type"));
//		System.out.println(RetrievePage.getHeaderField("http://www.ouyeelintl.com", "Date"));
//		RetrievePage.downloadPagebyHttpClient("http://www.ouyeelintl.com");
	}

}
