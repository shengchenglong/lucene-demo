package com.scl.chapter3_1;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * chapter 3.1.1 识别网页的编码
 * 
 * @author shengchenglong
 *
 */
public class PageCharset {

	public static final String CHARSET_STRING = "charset";

	/**
	 * 从返回的头信息中提取网页编码
	 * @param content
	 * @return
	 */
	public static String getCharset(String content) {
		int index;
		String ret;

		ret = null;
		if (null != content) {
			index = content.indexOf(CHARSET_STRING);

			if (index != -1) {
				content = content.substring(index + CHARSET_STRING.length()).trim();
				if (content.startsWith("=")) {
					content = content.substring(1).trim();
					index = content.indexOf(";");
					if (index != -1)
						content = content.substring(0, index);

					// 从字符串开始和结尾处删除双引号
					if (content.startsWith("\"") && content.endsWith("\"") && (1 < content.length()))
						content = content.substring(1, content.length() - 1);

					// 从字符串开始和结尾处删除单引号
					if (content.startsWith("'") && content.endsWith("'") && (1 < content.length()))
						content = content.substring(1, content.length() - 1);

					ret = findCharset(content, ret);
				}
			}
		}
		return (ret);
	}

	/**
	 * Lookup a character set name. <em>Vacuous for JVM's without
	 * <code>java.nio.charset</code>.</em> This uses reflection so the code will
	 * still run under prior JDK's but in that case the default is always
	 * returned.
	 * 
	 * @param name
	 *            The name to look up. One of the aliases for a character set.
	 * @param _default
	 *            The name to return if the lookup fails.
	 */
	public static String findCharset(String name, String _default) {
		String ret;

		try {
			Class cls;
			Method method;
			Object object;

			cls = Class.forName("java.nio.charset.Charset");
			method = cls.getMethod("forName", new Class[] { String.class });
			object = method.invoke(null, new Object[] { name });
			method = cls.getMethod("name", new Class[] {});
			object = method.invoke(object, new Object[] {});
			ret = (String) object;
		} catch (ClassNotFoundException cnfe) {
			// for reflection exceptions, assume the name is correct
			ret = name;
		} catch (NoSuchMethodException nsme) {
			// for reflection exceptions, assume the name is correct
			ret = name;
		} catch (IllegalAccessException ia) {
			// for reflection exceptions, assume the name is correct
			ret = name;
		} catch (InvocationTargetException ita) {
			// java.nio.charset.IllegalCharsetNameException
			// and java.nio.charset.UnsupportedCharsetException
			// return the default
			ret = _default;
			System.out.println("unable to determine cannonical charset name for " + name + " - using " + _default);
		}
		return (ret);
	}

}
