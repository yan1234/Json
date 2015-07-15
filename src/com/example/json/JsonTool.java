package com.example.json;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JsonTool {
	
	
	/**
	 * 利用java反射机制弥补android原生json不足的问题
	 * @author yanling
	 * @date 2015-07-13 15:25
	 */
	
	
	/**
	 * 
	 * @Title: objToJson
	 * @Description: 将object对象转换为json
	 * @author yanling
	 * @date 2015-7-14 上午9:22:13
	 * @param obj,待转换的object对象
	 * @return
	 * @return JSONObject，返回json对象
	 * @throws
	 */
	public static JSONObject objToJson(Object obj){
		JSONObject json = new JSONObject();
		//利用递归实现对象属性中带引用数据类型的情况
		if (isAllBasicType(obj)){
			//返回属性全是基本数据或String类型的方法
			return basicObjToJson(obj);

		}//递归进入
		else {
			//得到所有的公有方法
			Method[] methods = getPublicMethods(obj);
			for (Method method : methods){
				//得到该方法名
				String methodName = method.getName();		
				//得到所有的get方法
				if (methodName.startsWith("get") && !methodName.endsWith("Class")){
					Log.i("Json", "方法名为:"+methodName);
					//得到属性名
					String fieldName = getFieldName(methodName);
					
					//得到之后方法后的返回值
					Object value = getInvokeValue(obj, method);
					
					//判断该属性是否是基本数据或String类型
					if (isBasicType(value)){
						try {
							json.put(fieldName, value);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}//集合类型List接口实现类
					else if (isListType(value)){
						//将结果对象转化为List对象
						List list = (List) value;
						try {
							//调用listToJson方法将list集合对象转化为JSONArray对象
							json.put(fieldName, listToJson(list));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					//非基本数据类型
					else {
						try {
							json.put(fieldName, objToJson(value));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
												
			}
			return json;
		}
		
	}
	
	/**
	 * 
	 * @Title: listToJson
	 * @Description: 将list集合对象转化为JSONArray对象 
	 * @author yanling
	 * @date 2015-7-14 下午2:40:54
	 * @param list，集合对象
	 * @return
	 * @return JSONArray
	 * @throws
	 */
	public static JSONArray listToJson(List list){
		
		JSONArray array = new JSONArray();
		//获取该列表的迭代器
		Iterator iterator = list.iterator();
		
		//得到list集合对象的子元素，并验证list是否为空的情况
		if (iterator.hasNext()){
			//获取子元素
			Object firstChild = iterator.next();
			//判断该子元素类型是否是基本数据或String类型
			if (isBasicType(firstChild)){
				array.put(firstChild);
				//遍历剩下的子元素并以累加的形式添加到json中
				while (iterator.hasNext()){
					Object child = iterator.next();
					array.put(child);
				}
				
			}//表示该对象是引用数据类型
			else {
				array.put(objToJson(firstChild));
				//遍历剩下的子元素并以累加的形式添加到json中
				while (iterator.hasNext()){
					Object child = iterator.next();
					array.put(objToJson(child));
				}
			}
		}
		
		return array;
	}
	
	/**
	 * 
	 * @Title: basicObjToJson
	 * @Description:	将所有属性全部是基本数据或String类型的对象转化为json 
	 * @author yanling
	 * @date 2015-7-14 上午8:33:54
	 * @param obj 待转换的对象
	 * @throws 
	 * @return JSONObject 返回json对象
	 */
	public static JSONObject basicObjToJson(Object obj){
		JSONObject json = new JSONObject();
		
		//得打一个对象的所有公有方法
		Method[] methods = getPublicMethods(obj);
		
		for (Method method : methods){
			//获取该方法的方法名
			String methodName = method.getName();
			//判断是否是get方法(通过是否有get方法判断该项为该对象的一个属性),并且排除getClass()方法
			if (methodName.startsWith("get") && !methodName.endsWith("Class")){
                
				String fieldName = getFieldName(methodName);
                //获取执行该方法后的返回值
                Object value = getInvokeValue(obj, method);
                
                //将该值封装在json对象中
                try {
					json.put(fieldName, value);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
			}
		}
		
		return json;
	}
	
	/**
	 * 
	 * @Title: isAllBasicType
	 * @Description:	判断一个对象的所有属性是否是java基本数据类型或String类型 
	 * @author yanling
	 * @date 2015-7-13 下午7:26:44
	 * @param obj，待判断的对象
	 * @return
	 * @return boolean	true：该对象的所有属性均为java基本数据类型或String类型
	 * @throws 
	 */
	public static boolean isAllBasicType(Object obj){
		boolean ok = true;
		//得到所有的公有方法
		Method[] methods = getPublicMethods(obj);
		//遍历所有的属性看是否全部是基本数据类型或String类型
		for (Method method : methods){
			//获取该方法的方法名
			String methodName = method.getName();
			//判断是否是get方法(通过是否有get方法判断该项为该对象的一个属性)
			if (methodName.startsWith("get") && !methodName.endsWith("Class")){
				
				//获取执行该方法后的返回值
				Object value = getInvokeValue(obj, method);
				//判断该属性是否为基本数据或String类型
				if (!isBasicType(value)){
					ok = false;
					break;
				}
			}
		}
		
		return ok;
	}
	
	/**
	 * 
	 * @Title: getFieldname
	 * @Description: 根据get方法的方法名得到该属性名 
	 * @author yanling
	 * @date 2015-7-14 上午8:56:21
	 * @param methodName, get方法名
	 * @return
	 * @return String，返回对应的属性名
	 * @throws
	 */
	public static String getFieldName(String methodName){
		//将get方法的首字母的大写转化为小写
        String firstChar = methodName.substring(3).substring(0, 1).toLowerCase();
        String fieldName = firstChar + methodName.substring(4);
        
        return fieldName;
	}
	
	/**
	 * 
	 * @Title: getInvokeValue
	 * @Description: 执行一个对象的get方法，并返回执行后的数据 
	 * @author yanling
	 * @date 2015-7-14 上午8:45:54
	 * @param obj，对象
	 * @param method，待执行的方法
	 * @return
	 * @return Object 执行后的返回值
	 * @throws
	 */
	public static Object getInvokeValue(Object obj, Method method){
      
        //定义执行该方法后获取的返回值
        Object value = null;
        //执行该方法并获取执行后的值
		try {
			value = method.invoke(obj, null);
			
			return value;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @Title: getPublicMethod
	 * @Description: 获取一个对象所有的public方法 
	 * @author yanling
	 * @date 2015-7-14 上午8:42:05
	 * @param obj, 待获取的对象
	 * @return
	 * @throws Exception
	 * @return Method[],返回所有的方法集合
	 * @throws
	 */
	public static Method[] getPublicMethods(Object obj){
		//声明对象原型
		Class c = null;
		try {
			Log.i("Json", obj.getClass().getName());
			c = Class.forName(obj.getClass().getName());
			
			return c.getMethods();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;		
	}
	
	/**
	 * 
	 * @Title: isListType
	 * @Description: 判断对象是否是实现List接口的对象
	 * 实现List接口的3个对象：ArrayList, LinkedList, Vector 
	 * @author 272388 | yanling002@deppon.com
	 * @date 2015-7-14 上午10:51:05
	 * @param obj
	 * @return
	 * @return boolean
	 * @throws
	 */
	public static boolean isListType(Object obj){
		boolean ok = false;
		//判断对象是否是List接口的实现类
		if (obj instanceof ArrayList || obj instanceof LinkedList 
				|| obj instanceof Vector){
			ok = true;
		}
		return ok;
	}
	
	/**
	 * 
	 * @Title: isBasicType
	 * @Description: 判断对象是否是基本数据类型+String
	 * java基本数据类型: int、short、long、float、double、char、byte、boolean
	 * @author yanling
	 * @date 2015-7-13 下午3:50:05
	 * @param obj	待判断的对象
	 * @return
	 * @return boolean	true: 是java基本数据类型+String
	 * @throws
	 */
	public static boolean isBasicType(Object obj){
		boolean ok = false;
		//判断该类型是否是基本数据类型或String类型
		if (obj instanceof Integer || obj instanceof Short || obj instanceof Long
				|| obj instanceof Float || obj instanceof Double || obj instanceof Character
				|| obj instanceof Byte || obj instanceof Boolean || obj instanceof String){
			
			ok = true;
		}

		return ok;
	}

}
