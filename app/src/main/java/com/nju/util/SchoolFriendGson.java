package com.nju.util;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class SchoolFriendGson{

	private static SchoolFriendGson friendGson = null;
	private Gson gson ;
	
	public static SchoolFriendGson newInstance (){
		if(friendGson == null) {
			friendGson = new SchoolFriendGson();
		}
		return friendGson;
	}
	private SchoolFriendGson(){
		gson = new Gson();
	}
	
	public <T,V> String toJson(Map<T,V> map) {
		return gson.toJson(map);
	}
	
	public <T> String toJson(List<T> list) {
		return gson.toJson(list);
	}
	
	public <T> String toJson(T t) {
		return gson.toJson(t);
	}
	
	public <T> List<T> fromJsonToList(String json) {
		Type datasetListType = new TypeToken<Collection<T>>() {}.getType();
		return gson.fromJson(json, datasetListType);
	}
	
	public <K,V> Map<K,V> fromJsonToMap(String json) {
		Type datasetMapType = new TypeToken<Map<K,V>>() {}.getType();
		return gson.fromJson(json, datasetMapType);
	}
	 
}
