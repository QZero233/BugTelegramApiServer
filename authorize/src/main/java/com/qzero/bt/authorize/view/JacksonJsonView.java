package com.qzero.bt.authorize.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class JacksonJsonView implements JsonView {

    @Override
    public void render(Map<String, ?> map, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        httpServletResponse.setHeader("Content-Type","text/plain;charset=utf-8");

        ExecuteResult executeResult= (ExecuteResult) map.get("executeResult");
        if(executeResult==null){
            executeResult=new ExecuteResult(false,null);
        }

        PackedParameter packedParameter=executeResult.getPackedParameter();
        packedParameter.addParameter(executeResult.getActionResult());

        ObjectMapper mapper=new ObjectMapper();
        String json=mapper.writerWithDefaultPrettyPrinter().writeValueAsString(packedParameter);

        Logger log= LogManager.getLogger();
        log.debug(json);

        httpServletResponse.getOutputStream().write(json.getBytes("UTF-8"));
    }
}
