package com.qzero.bt.message.notice.action;

import java.util.HashMap;
import java.util.Map;

public class ParameterBuilder {

    private Map<String,String> parameter;

    public ParameterBuilder() {
        parameter=new HashMap<>();
    }

    public ParameterBuilder(Map<String, String> parameter) {
        this.parameter = parameter;
        if(this.parameter==null)
            this.parameter=new HashMap<>();
    }

    public ParameterBuilder addParameter(String name, String value){
        parameter.put(name,value);
        return this;
    }

    public ParameterBuilder removeParameter(String name){
        parameter.remove(name);
        return this;
    }

    public Map<String,String> build(){
        return parameter;
    }

}
