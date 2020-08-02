package com.qzero.bt.authorize.permission;

import com.qzero.bt.authorize.exception.ResourceDoesNotExistException;
import com.qzero.bt.authorize.dao.TokenDao;
import com.qzero.bt.authorize.data.TokenEntity;
import com.qzero.bt.authorize.exception.ErrorCodeList;
import com.qzero.bt.authorize.exception.PermissionDeniedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;

@Aspect
@Component
@Transactional
public class PermissionCheckAspect {

    private static final Logger log= LogManager.getLogger();

    @Autowired
    private TokenDao tokenDao;

    @Autowired
    @Qualifier("permissionConfigurator")
    private PermissionConfigurator permissionConfigurator;

    @Pointcut("execution(* com.qzero.bt.authorize.service.*.*(com.qzero.bt.authorize.data.TokenEntity,..))")
    public void methodsInService(){

    }

    @Around("methodsInService()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method=((MethodSignature)joinPoint.getSignature()).getMethod();
        PermissionCheck checkAnnotation=method.getAnnotation(PermissionCheck.class);
        if(checkAnnotation==null)
            return joinPoint.proceed();

        log.trace(String.format("Start permission check\nMethod %s\nPermissionName %s",method.getName(),checkAnnotation.value()));
        TokenEntity tokenEntity= (TokenEntity) joinPoint.getArgs()[0];
        TokenEntity tokenEntityFromDao=tokenDao.getTokenById(tokenEntity);

        if(tokenEntityFromDao==null)
            throw new ResourceDoesNotExistException(ErrorCodeList.CODE_ILLEGAL_TOKEN,"Token does not exist");

        if(!tokenEntityFromDao.getOwnerUserName().equals(tokenEntity.getOwnerUserName()))
            throw new PermissionDeniedException(ErrorCodeList.CODE_ILLEGAL_TOKEN,"Token owner name does not match");

        if(tokenEntityFromDao.getEndTime()>0 && tokenEntityFromDao.getEndTime()<System.currentTimeMillis()){
            tokenDao.deleteToken(tokenEntityFromDao);
            throw new PermissionDeniedException(ErrorCodeList.CODE_ILLEGAL_TOKEN,"Token expired");
        }


        if(!permissionConfigurator.checkPermission(tokenEntityFromDao,checkAnnotation.value()))
            throw new PermissionDeniedException(ErrorCodeList.CODE_PERMISSION_DENIED,"Permission denied");

        return joinPoint.proceed();
    }

}
