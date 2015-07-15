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
	 * ����java��������ֲ�androidԭ��json���������
	 * @author yanling
	 * @date 2015-07-13 15:25
	 */
	
	
	/**
	 * 
	 * @Title: objToJson
	 * @Description: ��object����ת��Ϊjson
	 * @author yanling
	 * @date 2015-7-14 ����9:22:13
	 * @param obj,��ת����object����
	 * @return
	 * @return JSONObject������json����
	 * @throws
	 */
	public static JSONObject objToJson(Object obj){
		JSONObject json = new JSONObject();
		//���õݹ�ʵ�ֶ��������д������������͵����
		if (isAllBasicType(obj)){
			//��������ȫ�ǻ������ݻ�String���͵ķ���
			return basicObjToJson(obj);

		}//�ݹ����
		else {
			//�õ����еĹ��з���
			Method[] methods = getPublicMethods(obj);
			for (Method method : methods){
				//�õ��÷�����
				String methodName = method.getName();		
				//�õ����е�get����
				if (methodName.startsWith("get") && !methodName.endsWith("Class")){
					Log.i("Json", "������Ϊ:"+methodName);
					//�õ�������
					String fieldName = getFieldName(methodName);
					
					//�õ�֮�󷽷���ķ���ֵ
					Object value = getInvokeValue(obj, method);
					
					//�жϸ������Ƿ��ǻ������ݻ�String����
					if (isBasicType(value)){
						try {
							json.put(fieldName, value);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}//��������List�ӿ�ʵ����
					else if (isListType(value)){
						//���������ת��ΪList����
						List list = (List) value;
						try {
							//����listToJson������list���϶���ת��ΪJSONArray����
							json.put(fieldName, listToJson(list));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					//�ǻ�����������
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
	 * @Description: ��list���϶���ת��ΪJSONArray���� 
	 * @author yanling
	 * @date 2015-7-14 ����2:40:54
	 * @param list�����϶���
	 * @return
	 * @return JSONArray
	 * @throws
	 */
	public static JSONArray listToJson(List list){
		
		JSONArray array = new JSONArray();
		//��ȡ���б�ĵ�����
		Iterator iterator = list.iterator();
		
		//�õ�list���϶������Ԫ�أ�����֤list�Ƿ�Ϊ�յ����
		if (iterator.hasNext()){
			//��ȡ��Ԫ��
			Object firstChild = iterator.next();
			//�жϸ���Ԫ�������Ƿ��ǻ������ݻ�String����
			if (isBasicType(firstChild)){
				array.put(firstChild);
				//����ʣ�µ���Ԫ�ز����ۼӵ���ʽ��ӵ�json��
				while (iterator.hasNext()){
					Object child = iterator.next();
					array.put(child);
				}
				
			}//��ʾ�ö�����������������
			else {
				array.put(objToJson(firstChild));
				//����ʣ�µ���Ԫ�ز����ۼӵ���ʽ��ӵ�json��
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
	 * @Description:	����������ȫ���ǻ������ݻ�String���͵Ķ���ת��Ϊjson 
	 * @author yanling
	 * @date 2015-7-14 ����8:33:54
	 * @param obj ��ת���Ķ���
	 * @throws 
	 * @return JSONObject ����json����
	 */
	public static JSONObject basicObjToJson(Object obj){
		JSONObject json = new JSONObject();
		
		//�ô�һ����������й��з���
		Method[] methods = getPublicMethods(obj);
		
		for (Method method : methods){
			//��ȡ�÷����ķ�����
			String methodName = method.getName();
			//�ж��Ƿ���get����(ͨ���Ƿ���get�����жϸ���Ϊ�ö����һ������),�����ų�getClass()����
			if (methodName.startsWith("get") && !methodName.endsWith("Class")){
                
				String fieldName = getFieldName(methodName);
                //��ȡִ�и÷�����ķ���ֵ
                Object value = getInvokeValue(obj, method);
                
                //����ֵ��װ��json������
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
	 * @Description:	�ж�һ����������������Ƿ���java�����������ͻ�String���� 
	 * @author yanling
	 * @date 2015-7-13 ����7:26:44
	 * @param obj�����жϵĶ���
	 * @return
	 * @return boolean	true���ö�����������Ծ�Ϊjava�����������ͻ�String����
	 * @throws 
	 */
	public static boolean isAllBasicType(Object obj){
		boolean ok = true;
		//�õ����еĹ��з���
		Method[] methods = getPublicMethods(obj);
		//�������е����Կ��Ƿ�ȫ���ǻ����������ͻ�String����
		for (Method method : methods){
			//��ȡ�÷����ķ�����
			String methodName = method.getName();
			//�ж��Ƿ���get����(ͨ���Ƿ���get�����жϸ���Ϊ�ö����һ������)
			if (methodName.startsWith("get") && !methodName.endsWith("Class")){
				
				//��ȡִ�и÷�����ķ���ֵ
				Object value = getInvokeValue(obj, method);
				//�жϸ������Ƿ�Ϊ�������ݻ�String����
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
	 * @Description: ����get�����ķ������õ��������� 
	 * @author yanling
	 * @date 2015-7-14 ����8:56:21
	 * @param methodName, get������
	 * @return
	 * @return String�����ض�Ӧ��������
	 * @throws
	 */
	public static String getFieldName(String methodName){
		//��get����������ĸ�Ĵ�дת��ΪСд
        String firstChar = methodName.substring(3).substring(0, 1).toLowerCase();
        String fieldName = firstChar + methodName.substring(4);
        
        return fieldName;
	}
	
	/**
	 * 
	 * @Title: getInvokeValue
	 * @Description: ִ��һ�������get������������ִ�к������ 
	 * @author yanling
	 * @date 2015-7-14 ����8:45:54
	 * @param obj������
	 * @param method����ִ�еķ���
	 * @return
	 * @return Object ִ�к�ķ���ֵ
	 * @throws
	 */
	public static Object getInvokeValue(Object obj, Method method){
      
        //����ִ�и÷������ȡ�ķ���ֵ
        Object value = null;
        //ִ�и÷�������ȡִ�к��ֵ
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
	 * @Description: ��ȡһ���������е�public���� 
	 * @author yanling
	 * @date 2015-7-14 ����8:42:05
	 * @param obj, ����ȡ�Ķ���
	 * @return
	 * @throws Exception
	 * @return Method[],�������еķ�������
	 * @throws
	 */
	public static Method[] getPublicMethods(Object obj){
		//��������ԭ��
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
	 * @Description: �ж϶����Ƿ���ʵ��List�ӿڵĶ���
	 * ʵ��List�ӿڵ�3������ArrayList, LinkedList, Vector 
	 * @author 272388 | yanling002@deppon.com
	 * @date 2015-7-14 ����10:51:05
	 * @param obj
	 * @return
	 * @return boolean
	 * @throws
	 */
	public static boolean isListType(Object obj){
		boolean ok = false;
		//�ж϶����Ƿ���List�ӿڵ�ʵ����
		if (obj instanceof ArrayList || obj instanceof LinkedList 
				|| obj instanceof Vector){
			ok = true;
		}
		return ok;
	}
	
	/**
	 * 
	 * @Title: isBasicType
	 * @Description: �ж϶����Ƿ��ǻ�����������+String
	 * java������������: int��short��long��float��double��char��byte��boolean
	 * @author yanling
	 * @date 2015-7-13 ����3:50:05
	 * @param obj	���жϵĶ���
	 * @return
	 * @return boolean	true: ��java������������+String
	 * @throws
	 */
	public static boolean isBasicType(Object obj){
		boolean ok = false;
		//�жϸ������Ƿ��ǻ����������ͻ�String����
		if (obj instanceof Integer || obj instanceof Short || obj instanceof Long
				|| obj instanceof Float || obj instanceof Double || obj instanceof Character
				|| obj instanceof Byte || obj instanceof Boolean || obj instanceof String){
			
			ok = true;
		}

		return ok;
	}

}
