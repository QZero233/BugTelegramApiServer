package com.qzero.server;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.LinkedHashMap;
import java.util.Map;

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

    public void addObject(Object obj){
        if(object==null)
            object=new LinkedHashMap();

        ObjectMapper mapper=new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        object.putAll(mapper.convertValue(obj,Map.class));
    }

    public<T> T parseObject(Class<T> cls){
        ObjectMapper mapper=new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(mapper,cls);
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
}
