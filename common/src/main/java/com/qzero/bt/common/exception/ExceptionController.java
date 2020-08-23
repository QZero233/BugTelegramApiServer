package com.qzero.bt.common.exception;

import com.qzero.bt.common.view.ActionResult;
import com.qzero.bt.common.view.IPackedObjectFactory;
import com.qzero.bt.common.view.PackedObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.UndeclaredThrowableException;

@RestControllerAdvice
public class ExceptionController{

    private final Logger log= LoggerFactory.getLogger(getClass());

    @Autowired
    private IPackedObjectFactory packedObjectFactory;

    @ExceptionHandler
    public PackedObject handleException(HttpServletRequest request,
                                        Throwable e){
        log.error(e.getMessage(),e);
        ActionResult actionResult=processException(e);

        PackedObject result=packedObjectFactory.getPackedObject();
        result.addObject(actionResult);
        return result;
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
