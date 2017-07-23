package com.web.admin;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;


import com.JSONListFormat;

/**
 * 数据重组
 * @author Lucas
 * @data 2017年2月6日 上午9:29:00
 * @version 1.0
 *
 */
public class FormatDataUtil {
	/**
	 * 
	 * 添加字段型字段
	 * @param data 数据
	 * @param map 添加字段组
	 * @param jsonFormat
	 * @param arryName  二维数组名称
	 * @return
	 * @throws Exception
	 */
	public static JSONListFormat addField(LinkedList<HashMap<String, String>> data,HashMap<String, String> map, JSONListFormat jsonFormat,String arryName) throws Exception {
		
		JSONObject levelJson = null;
		JSONArray levelTowArray = null;
		JSONObject levelTowJson = null;
		if(data.size()>0){ // 判断数据是否大于0
			levelJson = new JSONObject();  // 
			jsonFormat.addJSONObject(levelJson);
			for(Map.Entry<String, String> entry:map.entrySet()){
				levelJson.put(entry.getKey(), entry.getValue()); // 添加字段
			}
			levelJson.put(arryName, new JSONArray());  // 加入数组Arry
			for(HashMap<String, String> maps:data) {
				levelTowArray = levelJson.getJSONArray(arryName);
				levelTowJson = new JSONObject();
				for(Map.Entry<String, String> entry:maps.entrySet()){
					levelTowJson.put(entry.getKey(), entry.getValue());
				}
				levelTowArray.put(levelTowJson);
			}
		}
		return jsonFormat;
	} 
	/**
	 * 数据分组arry
	 * @param data
	 * @param jsonFormat
	 * @param maps
	 * @return
	 * @throws Exception
	 */
	public static JSONListFormat arryFormat(JSONListFormat jsonFormat,HashMap<String, LinkedList<HashMap<String, String>>> maps) throws Exception{
		JSONObject levelJson = null;
		JSONArray levelTowArray = null;
		JSONObject levelTowJson = null;
		
		levelJson = new JSONObject();
		jsonFormat.addJSONObject(levelJson);
		for(Map.Entry<String, LinkedList<HashMap<String, String>>> entry : maps.entrySet()){ // 数组组
			levelJson.put(entry.getKey(), new JSONArray());
			LinkedList<HashMap<String, String>> list = entry.getValue();

			while(list.size()>0){
				HashMap<String, String> map = list.remove();
				levelTowArray = levelJson.getJSONArray(entry.getKey());
				levelTowJson = new JSONObject();
				for(Map.Entry<String,String> entry2:map.entrySet()){
					levelTowJson.put(entry2.getKey(), entry2.getValue());
				}
				levelTowArray.put(levelTowJson);
			}
		}		
		return jsonFormat;
	}
	
	/**
	 * 分组并加字段
	 * @param data
	 * @param jsonFormat
	 * @param map
	 * @param groupKey
	 * @return
	 * @throws Exception
	 */
	public static JSONListFormat groupFormat(LinkedList<HashMap<String, String>> data,JSONListFormat jsonFormat,LinkedList<String> map,String groupKey,String arryName) throws Exception {
		JSONObject levelJson = null;
		JSONArray levelTowArray = null;
		JSONObject levelTowJson = null;
		
		String groupValue = ""; // 分组key
		
		HashMap<String,JSONArray> list = new HashMap<String,JSONArray>();
		LinkedList<String> groupValueList = new LinkedList<>();
		
		for(HashMap<String, String> mapList:data){		
			groupValue = mapList.get(groupKey);
			
			if(!groupValueList.contains(groupValue)){
				
				levelJson = new JSONObject();
				jsonFormat.addJSONObject(levelJson);
				groupValueList.add(groupValue);
				
				for(String key:map){
					levelJson.put(key, mapList.get(key));
					mapList.remove(key);
				}
				
				levelJson.put(arryName, new JSONArray());
				levelTowArray = levelJson.getJSONArray(arryName);
				
			} else {
				levelTowArray = list.get(groupValue);
				for(String key:map){
					mapList.remove(key);
				}
			}
			
			levelTowJson = new JSONObject();
			for(Map.Entry<String, String> entry:mapList.entrySet()) {
				levelTowJson.put(entry.getKey(), entry.getValue());
			}
			
			levelTowArray.put(levelTowJson);
			
			list.put(groupValue, levelTowArray);
		}
		return jsonFormat;
	}
	
	public static JSONListFormat groupFormat(HashMap<String, String> data,HashMap<String, LinkedList<HashMap<String, String>>> maps,JSONListFormat jsonFormat) throws Exception {
		JSONObject levelJson = null;
		JSONArray levelTowArray = null;
		JSONObject levelTowJson = null;
		levelJson = new JSONObject();
		for(Map.Entry<String, String> entry:data.entrySet()) {
			levelJson.put(entry.getKey(), entry.getValue());
		}
		for(Entry<String, LinkedList<HashMap<String, String>>> entry:maps.entrySet()) {
			levelJson.put(entry.getKey(), new JSONArray());
			levelTowArray = levelJson.getJSONArray(entry.getKey());
			for(HashMap<String, String> map:entry.getValue()) {
				levelTowJson = new JSONObject();
				for(Map.Entry<String, String> entryTow:map.entrySet()) {
					levelTowJson.put(entryTow.getKey(), entryTow.getValue());
				}
				levelTowArray.put(levelTowJson);
			}
		}
		jsonFormat.addJSONObject(levelJson);
		return jsonFormat;
	}
	
	public static String map2string(HashMap<String, String> map){
		JSONObject jsonObject = new JSONObject(map);
		return jsonObject.toString();
	}
	
	public static String list2string(LinkedList<HashMap<String, String>> list){
		JSONArray jsonArray = new JSONArray(list);
		return jsonArray.toString();
	}
	
}
