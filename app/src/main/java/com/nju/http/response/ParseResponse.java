package com.nju.http.response;

import android.util.Log;

import com.nju.util.Constant;
import com.nju.util.SchoolFriendGson;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

import java.io.IOException;

/**
 * Created by xiaojuzhang on 2016/3/24.
 */
public class ParseResponse {

    private static final SchoolFriendGson gson = SchoolFriendGson.newInstance();
    private ObjectNode getRoot(final String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = (ObjectNode) mapper.readTree(json);
        return root;
    }

    private  int code(ObjectNode root){
        int code = 0;
        if (root != null){
            ObjectNode exceptionNode = (ObjectNode) root.get(Constant.EXCEPTION);
            if (exceptionNode != null){
                code = exceptionNode.get(Constant.CODE).asInt();
            }
        }
        return code;
    }

    public Object getInfo (final String json,Class aClass) throws IOException {
        ObjectNode root = getRoot(json);
        int code = code(root);
        if (code == Constant.OK){
            JsonNode jInfoNode = root.get(Constant.INFO);
            Log.i("tag",jInfoNode.toString());
            if (jInfoNode.isArray()){
                return gson.fromJsonToList(jInfoNode.toString(),aClass);
            } else if (jInfoNode.isObject()){
                return jInfoNode.asText();
            }
        }
        return null;
    }


}
