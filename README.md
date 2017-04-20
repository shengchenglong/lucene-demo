# lucene-demo
解密搜索引擎技术实战Lucene&amp;Java精华版第三版学习手敲代码

1.代码基本上是书上写的，有些不一样，我用的jar包版本皆选取当前最新的版本

  比如2.3.1节：

  HttpClient的DefaultHttpClient类已经过时了，我采用了新的CloseableHttpClient类代替

2.在书上代码练习的同时，我也自己试着用不同方式来实现同一种方法

比如2.3.2节：

  书上使用套接字发送If-Modified-Since头信息：
  
    outw.print("GET " + file + " HTTP/1.0\r\n");
    
    outw.print("If-Modified-Since: Thu, 13 Apr 2017 09:25:12 GMT\r\n");
    
  我尝试了使用HttpClient来实现该功能:
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
  
