package com.qzero.bt.common.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class NewReturnValue {

    private boolean succeeded;
    private int statusCode;
    private String message;
    private Map<String,Object> object;

    public NewReturnValue(boolean succeeded, int statusCode, String message, LinkedHashMap object) {
        this.succeeded = succeeded;
        this.statusCode = statusCode;
        this.message = message;
        this.object = object;
    }

    public void addObject(String prefix,Object obj){
        if(object==null)
            object=new LinkedHashMap();

        ObjectMapper mapper=new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        Map<String ,Object> map=mapper.convertValue(obj,Map.class);

        if(prefix!=null && !prefix.equals("")){
            String[] keyArray=map.keySet().toArray(new String[0]);
            for(String key:keyArray){
                String newKey=prefix+key;
                Object tmp=map.get(key);
                map.put(newKey,tmp);
                map.remove(key);
            }
        }

        object.putAll(map);
    }

    public void addObject(Object obj){
        addObject(null,obj);
    }


    public<T> T parseObject(String prefix,Class<T> cls){
        ObjectMapper mapper=new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Map<String,Object> modifiedMap;
        if(prefix!=null && !prefix.equals("")){
            modifiedMap=new LinkedHashMap<>();
            Set<String> keySet=object.keySet();
            for(String key:keySet){
                String newKey=key.replaceAll("^"+prefix,"");
                modifiedMap.put(newKey,object.get(key));
            }
        }else{
            modifiedMap=object;
        }
        return mapper.convertValue(modifiedMap,cls);
    }


    public<T> T parseObject(Class<T> cls){
        return parseObject(null,cls);
    }

    public boolean isSucceeded() {
        return succeeded;
    }

    public void setSucceeded(boolean succeeded) {
        this.succeeded = succeeded;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getObject() {
        return object;
    }

    public void setObject(Map<String, Object> object) {
        this.object = object;
    }

    @Override
    public String toString() {
        ObjectMapper mapper=new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
