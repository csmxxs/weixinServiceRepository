package com.ylms.utils;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;

public class Bean2WeChatXml {

	/**
	 * 将实体类转换成符合微信接口API要求的xml格式
	 * 
	 * @param obj
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @see
	 */
	@SuppressWarnings("rawtypes")
	public static String beanToXML(Object obj) throws IllegalArgumentException, IllegalAccessException {
		StringBuffer sb = new StringBuffer("<xml>\n");

		Class clazz = obj.getClass();
		Field[] fields = clazz.getDeclaredFields();

		// 父类Class
		Class superClass = clazz.getSuperclass();
		Field[] superFields = superClass.getDeclaredFields();

		// 拼装自身的字段和字段值
		String fieldName = null;
		Object mapObj = null;
		for (Field field : fields) {
			field.setAccessible(true);
			fieldName = field.getName();// 获得字段名
			mapObj = field.get(obj);// 获得字段值

			// 如果字段是Map类型，形如ImageRspMsg类中Map字段
			if (mapObj instanceof Map) {
				// 迭代map集合
				StringBuffer mapFieldValue = new StringBuffer("");
				String key = "";
				Map castMap = (Map) mapObj;
				Iterator iterator = castMap.keySet().iterator();
				while (iterator.hasNext()) {
					// 迭代
					key = (String) iterator.next();
					mapFieldValue.append("<").append(key).append(">");
					// 调用value的toString方法
					mapFieldValue.append("<![CDATA[").append(castMap.get(key).toString()).append("]]>");
					mapFieldValue.append("</").append(key).append(">\n");
				}
				sb.append("<").append(fieldName).append(">\n");
				sb.append(mapFieldValue); // map集合内的迭代结果，勿加CDATA
				sb.append("</").append(fieldName).append(">\n");
			}
			// 字段非Map类型，则按照String类型处理（获得value时直接调用toString方法）
			else {
				sb.append("<").append(fieldName).append(">");
				sb.append("<![CDATA[").append(mapObj.toString()).append("]]>");
				sb.append("</").append(fieldName).append(">\n");
			}
		}

		// 拼装父类的字段和字段值
		String superFieldName = "";
		for (Field field : superFields) {
			field.setAccessible(true);
			superFieldName = field.getName();

			sb.append("<").append(superFieldName).append(">");
			sb.append("<![CDATA[").append(field.get(obj).toString()).append("]]>");
			sb.append("</").append(superFieldName).append(">\n");

		}
		sb.append("</xml>");
		return sb.toString();
	}
}
