package com.qzero.bt.common.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PackedParameter {

    /**
     * Parameter map
     * Key:name of the parameter
     * Value:object in json
     */
    private Map<String,String> parameterMap=new HashMap<>();

    public PackedParameter() {
    }

    public PackedParameter(Map<String, String> parameterMap) {
        this.parameterMap = parameterMap;
    }

    public PackedParameter(Object parameter){
        addParameter(parameter);
    }

    public static String getDefaultName(Class cls){
        return cls.getSimpleName();
    }

    public void addParameter(String name,Object parameter){
        try {
            String json = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(parameter);
            parameterMap.put(name,json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void addParameter(Object parameter){
        addParameter(getDefaultName(parameter.getClass()),parameter);
    }

    public<T> T getParameter(String name,Class<T> cls){
        try {
            String json=parameterMap.get(name);
            ObjectMapper mapper=new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(json,cls);
        }catch (JsonProcessingException e){
            e.printStackTrace();
            return null;
        }
    }

    public<T> T  getParameter(Class<T> cls){
        return getParameter(getDefaultName(cls),cls);
    }

    public Map<String, String> getParameterMap() {
        return parameterMap;
    }

    public void setParameterMap(Map<String, String> parameterMap) {
        this.parameterMap = parameterMap;
    }

    public Collection getCollectionParameter(String name, Class collectionClass,Class... elementClasses){
        try {
            ObjectMapper mapper=new ObjectMapper();
            JavaType type=mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
            return mapper.readValue(parameterMap.get(name), type);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return "PackedRequestParameter{" +
                "parameterMap=" + parameterMap +
                '}';
    }
}
