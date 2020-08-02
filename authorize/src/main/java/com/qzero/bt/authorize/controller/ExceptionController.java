package com.qzero.bt.authorize.controller;

import com.qzero.bt.authorize.exception.ErrorCodeList;
import com.qzero.bt.authorize.exception.ResponsiveException;
import com.qzero.bt.authorize.view.ActionResult;
import com.qzero.bt.authorize.view.ExecuteResult;
import com.qzero.bt.authorize.view.JsonView;
import com.qzero.bt.authorize.view.PackedParameter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.UndeclaredThrowableException;

@ControllerAdvice
public class ExceptionController{

    private static final Logger log= LogManager.getLogger();

    @Autowired
    private JsonView jsonView;

    @ExceptionHandler
    public ModelAndView handleException(HttpServletRequest request,
                                        Throwable e){

        log.error(e.getMessage(),e);
        ActionResult actionResult=processException(e);
        ExecuteResult result=new ExecuteResult(actionResult,new PackedParameter());

        ModelAndView modelAndView=new ModelAndView(jsonView);
        modelAndView.addObject(result);
        return modelAndView;
    }

    private ActionResult processException(Throwable e){
        if(e instanceof UndeclaredThrowableException){
            Throwable undeclared=((UndeclaredThrowableException) e).getUndeclaredThrowable();
            if(undeclared!=null && !(undeclared instanceof UndeclaredThrowableException))
                return processException(undeclared);
        }

        ActionResult actionResult=new ActionResult(false,null);

        if(e instanceof ResponsiveException){
            actionResult.setStatusCode(((ResponsiveException) e).getErrorCode());
            actionResult.setMessage(e.getMessage());
        }else if(e instanceof HttpMessageNotReadableException){
            //Missing parameter
            actionResult.setStatusCode(ErrorCodeList.CODE_BAD_REQUEST_PARAMETER);
            actionResult.setMessage("Missing parameter,please check your input");
        }else{
            actionResult.setStatusCode(ErrorCodeList.CODE_UNKNOWN_ERROR);
            actionResult.setMessage("Unknown error\nError type:"+e.getClass()+"\nError message:\n"+e.getMessage());
        }

        return actionResult;
    }
}
