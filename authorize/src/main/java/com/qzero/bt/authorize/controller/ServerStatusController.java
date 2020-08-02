package com.qzero.bt.authorize.controller;

import com.qzero.bt.authorize.data.ServerStatus;
import com.qzero.bt.authorize.view.ExecuteResult;
import com.qzero.bt.authorize.view.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/status")
public class ServerStatusController {

    @Autowired
    private ServerStatus currentServerStatus;

    @Autowired
    private JsonView jsonView;

    @RequestMapping("/request")
    public ModelAndView requestServerStatus(){
        ModelAndView modelAndView=new ModelAndView(jsonView);
        modelAndView.addObject(new ExecuteResult(true,null,currentServerStatus));
        return modelAndView;
    }
}
